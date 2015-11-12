package com.rhymax.signup;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rhymax.account.Account;
import com.rhymax.account.AccountRepository;
import com.rhymax.account.UserService;
import com.rhymax.support.web.MessageHelper;

@Controller
public class SignupController {

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
    public String signup(@Valid @ModelAttribute SignupForm signupForm, Errors errors, RedirectAttributes ra) {
        if (errors.hasErrors()) {
            return SIGNUP_VIEW_NAME;
        }
        Account account = signupForm.createAccount();
        if (accountRepository.isNumberExisted(account.getNumber())) {
            errors.rejectValue("number", "error.number.existed", "The number was aleary signed up");
        }
        if (!StringUtils.isEmpty(account.getEmail()) && accountRepository.isEmailExisted(account.getEmail())) {
            errors.rejectValue("email", "error.email.existed", "The Email was already signed up");
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
