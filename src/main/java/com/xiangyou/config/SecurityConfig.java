package com.xiangyou.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.RequestContextFilter;

import com.xiangyou.Constants;
import com.xiangyou.account.AccountRepository;
import com.xiangyou.account.PhoneCodeExpiredException;
import com.xiangyou.account.PhoneCodeNotFoundException;
import com.xiangyou.account.PhoneCodeNotMatchException;
import com.xiangyou.account.UserService;

@Configuration
@EnableWebMvcSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {
    static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    public static final String DEFAULT_FAILURE_URL = "/signin?error";
    public static final String ATTR_DO_USER_AUTHENTICATION = "doUserAuthentication";
    public static final String PARAM_PHONE_CODE_SIGNIN = "phoneCodeSignin";

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserService userService;

    @Autowired
    TokenBasedRememberMeServices rememberMeServices;

    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("remember-me-key", userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Sandi - Init passwordEncoder");
        return new StandardPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.eraseCredentials(true);
        auth.authenticationProvider(phoneCodeAuthenticationProvider());
        auth.authenticationProvider(userAuthenticationProvider());
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManager authenticationManager = super.authenticationManager();
        logger.info("authenticationManager=" + authenticationManager);
        return authenticationManager;
    }

    @Bean
    DaoAuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider() {
            public boolean supports(Class<?> authentication) {
                Boolean doUserAuthentication = (Boolean) ((ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes()).getRequest().getAttribute(ATTR_DO_USER_AUTHENTICATION);
                if (doUserAuthentication == null) {
                    doUserAuthentication = true;
                }
                return doUserAuthentication;
            }
        };
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    AuthenticationProvider phoneCodeAuthenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                logger.info("Sandi - phoneCodeAuthenticationProvider - authenticate");
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes()).getRequest();
                HttpSession session = request.getSession();
                logger.info("Sandi - AuthenticationProvider - requestUri=" + request.getRequestURI() + "|request="
                        + request + "|session=" + session);
                final boolean isPhoneCodeSignin = Boolean.valueOf(request.getParameter(PARAM_PHONE_CODE_SIGNIN));
                logger.info("Sandi - isPhoneCodeSignin=" + isPhoneCodeSignin);
                if (!isPhoneCodeSignin) {
                    return authentication;
                }
                // Check phone code in session
                if (session.getAttribute(Constants.ATTR_PHONE) == null
                        || session.getAttribute(Constants.ATTR_SIGNIN_CODE) == null
                        || session.getAttribute(Constants.ATTR_SIGNIN_CODE_TS) == null) {
                    request.setAttribute(ATTR_DO_USER_AUTHENTICATION, false);
                    throw new PhoneCodeNotFoundException();
                }
                long signinCodeTs = 0;
                try {
                    signinCodeTs = Long.valueOf(session.getAttribute(Constants.ATTR_SIGNIN_CODE_TS).toString());
                } catch (NumberFormatException ignore) {
                }
                // Check if the phone code is expired
                if ((System.currentTimeMillis() - signinCodeTs) > 5 * 60 * 1000) {
                    request.setAttribute(ATTR_DO_USER_AUTHENTICATION, false);
                    throw new PhoneCodeExpiredException();
                }
                UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
                String username = String.valueOf(auth.getPrincipal());
                String password = String.valueOf(auth.getCredentials());
                logger.info("Sandi - username=" + username + "|password=" + password);
                logger.info("Sandi - Phone=" + session.getAttribute(Constants.ATTR_PHONE) + "|signinCode="
                        + session.getAttribute(Constants.ATTR_SIGNIN_CODE));
                // Check the username and password if is match with the phone
                // and the phone code.
                if (username.equals(session.getAttribute(Constants.ATTR_PHONE))
                        && password.equals(session.getAttribute(Constants.ATTR_SIGNIN_CODE))) {
                    return userService.authenticate(username);
                } else {
                    request.setAttribute(ATTR_DO_USER_AUTHENTICATION, false);
                    throw new PhoneCodeNotMatchException();
                }
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return true;
            }
        };
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        SimpleUrlAuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler() {
            final Map<String, String> failureUrlMap = new HashMap<String, String>();

            public SimpleUrlAuthenticationFailureHandler init() {
                Map<String, String> exceptionMappings = new HashMap<String, String>();
                exceptionMappings.put("com.xiangyou.account.UsernameNotFoundException",
                        "/signin?error=usernameNotFound");
                exceptionMappings.put("com.xiangyou.account.PasswordNotMatchException",
                        "/signin?error=passwordNotMatch");
                exceptionMappings.put("com.xiangyou.account.PhoneCodeNotFoundException",
                        "/signin?error=phoneCodeNotFound");
                exceptionMappings.put("com.xiangyou.account.PhoneCodeExpiredException",
                        "/signin?error=phoneCodeExpired");
                exceptionMappings.put("com.xiangyou.account.PhoneCodeNotMatchException",
                        "/signin?error=phoneCodeNotMatch");
                setExceptionMappings(exceptionMappings);

                setDefaultFailureUrl(DEFAULT_FAILURE_URL);

                return this;
            }

            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                    AuthenticationException exception) throws IOException, ServletException {
                String url = failureUrlMap.get(exception.getClass().getName());
                if (url == null) {
                    url = DEFAULT_FAILURE_URL;
                }
                final boolean isPhoneCodeSignin = Boolean.valueOf(request.getParameter(PARAM_PHONE_CODE_SIGNIN));
                if (isPhoneCodeSignin) {
                    url += "#phoneCodeSignin";
                }
                logger.info("Sandi - onAuthenticationFailure - exception is " + exception + " | url is " + url);
                getRedirectStrategy().sendRedirect(request, response, url);
            }

            public void setExceptionMappings(Map<?, ?> failureUrlMap) {
                this.failureUrlMap.clear();
                for (Map.Entry<?, ?> entry : failureUrlMap.entrySet()) {
                    Object exception = entry.getKey();
                    Object url = entry.getValue();
                    Assert.isInstanceOf(String.class, exception,
                            "Exception key must be a String (the exception classname).");
                    Assert.isInstanceOf(String.class, url, "URL must be a String");
                    Assert.isTrue(UrlUtils.isValidRedirectUrl((String) url), "Not a valid redirect URL: " + url);
                    this.failureUrlMap.put((String) exception, (String) url);
                }
            }
        }.init();
        return authenticationFailureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/favicon.ico", "/resources/**", "/signin", "/signup", "/product/details",
                        /*"/product/order", "/product/doOrder", "/product/orderSuccess", */"/product/orderList",
                        "/product/features", "/sendCode")
                .permitAll().anyRequest().authenticated().and().formLogin().loginPage("/signin").permitAll()
                .failureUrl(DEFAULT_FAILURE_URL).loginProcessingUrl("/authenticate")
                .failureHandler(authenticationFailureHandler()).and()
                .addFilterBefore(new RequestContextFilter(), SecurityContextPersistenceFilter.class).logout()
                .logoutUrl("/logout").permitAll().logoutSuccessUrl("/signin?logout").and().rememberMe()
                .rememberMeServices(rememberMeServices).key("remember-me-key");
    }
}