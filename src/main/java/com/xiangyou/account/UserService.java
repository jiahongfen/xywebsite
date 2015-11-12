package com.xiangyou.account;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

public class UserService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    protected void initialize() {
        accountRepository.save(new Account("13813391019", "idingli@gmail.com", "123456", "ROLE_USER"));
        accountRepository.save(new Account("123456", "admin", "admin", "ROLE_ADMIN"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByPhoneOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return createUser(account);
    }

    public void signin(Account account) {
        SecurityContextHolder.getContext().setAuthentication(authenticate(account));
    }

    private Authentication authenticate(Account account) {
        return new UsernamePasswordAuthenticationToken(createUser(account), null,
                Collections.singleton(createAuthority(account)));
    }

    public Authentication authenticate(String username) {
        Account account = accountRepository.findByPhoneOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return new UsernamePasswordAuthenticationToken(createUser(account), null,
                Collections.singleton(createAuthority(account)));
    }

    private User createUser(Account account) {
        return new User(!StringUtils.isEmpty(account.getPhone()) ? account.getPhone() : account.getEmail(),
                account.getPassword(), Collections.singleton(createAuthority(account)));
    }

    private GrantedAuthority createAuthority(Account account) {
        return new SimpleGrantedAuthority(account.getRole());
    }

}
