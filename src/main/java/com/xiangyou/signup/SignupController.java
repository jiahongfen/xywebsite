package com.xiangyou.signup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xiangyou.Constants;
import com.xiangyou.account.Account;
import com.xiangyou.account.AccountRepository;
import com.xiangyou.account.UserService;
import com.xiangyou.support.web.MessageHelper;

@Controller
public class SignupController {
    static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    private static final String SIGNUP_VIEW_NAME = "signup/signup";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "signup")
    public String signup(Model model) {
        model.addAttribute(new SignupForm());
        return SIGNUP_VIEW_NAME;
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String signup(@Valid @ModelAttribute SignupForm signupForm, Errors errors, RedirectAttributes ra,
            HttpServletRequest request) {
        if (errors.hasErrors()) {
            return SIGNUP_VIEW_NAME;
        }
        Account account = signupForm.createAccount();
        if (accountRepository.isPhoneExisted(account.getPhone())) {
            errors.rejectValue("phone", "error.phone.existed");
        }
        if (!StringUtils.isEmpty(account.getEmail()) && accountRepository.isEmailExisted(account.getEmail())) {
            errors.rejectValue("email", "error.email.existed");
        }
        HttpSession session = request.getSession();
        long signupCodeTs = 0;
        try {
            signupCodeTs = Long.valueOf(session.getAttribute(Constants.ATTR_SIGNUP_CODE_TS).toString());
        } catch (NumberFormatException ignore) {
        }
        // Check if the phone code is expired
        if ((System.currentTimeMillis() - signupCodeTs) > 5 * 60 * 1000) {
            errors.rejectValue("phoneCode", "error.phoneCode.expired");
        } else {
            logger.info("Phone=" + session.getAttribute(Constants.ATTR_PHONE) + "|signupCode="
                    + session.getAttribute(Constants.ATTR_SIGNUP_CODE));
            logger.info("account.getPhone()=" + account.getPhone() + "|signupForm.getPhoneCode()="
                    + signupForm.getPhoneCode());
            if (!account.getPhone().equals(session.getAttribute(Constants.ATTR_PHONE))
                    || !signupForm.getPhoneCode().equals(session.getAttribute(Constants.ATTR_SIGNUP_CODE))) {
                errors.rejectValue("phoneCode", "error.phoneCode.notMatch");
            }
        }
        if (errors.hasErrors()) {
            return SIGNUP_VIEW_NAME;
        }
        account = accountRepository.save(account);
        userService.signin(account);
        // see /WEB-INF/i18n/messages.properties and
        // /WEB-INF/views/homeSignedIn.html
        MessageHelper.addSuccessAttribute(ra, "signup.success");
        return "redirect:/";
    }
}
