package com.xiangyou.account;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiangyou.Constants;

@Controller
class PhoneCodeController {
    static final Logger logger = LoggerFactory.getLogger(PhoneCodeController.class);
    private static int TYPE_SIGNUP = 1;
    private static int TYPE_SIGNIN = 2;

    private AccountRepository accountRepository;
    private PhoneCodeSender phoneCodeSender;

    @Autowired
    public PhoneCodeController(AccountRepository accountRepository, PhoneCodeSender phoneCodeSender) {
        this.accountRepository = accountRepository;
        this.phoneCodeSender = phoneCodeSender;
    }

    @RequestMapping(value = "/sendCode", method = RequestMethod.GET)
    @ResponseBody
    public String sendCode(@RequestParam(value = "type", required = true) int type,
            @RequestParam(value = "phone", required = true) String phone, HttpServletRequest request) {
        logger.debug("sendCode - type=" + type + "|phone=" + phone);
        HttpSession session = request.getSession();
        if (type == TYPE_SIGNUP) {
            if (!hasSentSignupCode(session)) {
                boolean isPhoneExisted = accountRepository.isPhoneExisted(phone);
                if (isPhoneExisted) {
                    logger.info(phone + " was already signed up");
                    return "phoneSignedUp";
                } else {
                    String code = getRandomCode();
                    session.setAttribute(Constants.ATTR_PHONE, phone);
                    session.setAttribute(Constants.ATTR_SIGNUP_CODE, code);
                    session.setAttribute(Constants.ATTR_SIGNUP_CODE_TS, System.currentTimeMillis());
                    sendSignupCode(phone, code);
                }
            }
        } else if (type == TYPE_SIGNIN) {
            if (!hasSentSigninCode(session)) {
                boolean isPhoneExisted = accountRepository.isPhoneExisted(phone);
                if (isPhoneExisted) {
                    String code = getRandomCode();
                    session.setAttribute(Constants.ATTR_PHONE, phone);
                    session.setAttribute(Constants.ATTR_SIGNIN_CODE, code);
                    session.setAttribute(Constants.ATTR_SIGNIN_CODE_TS, System.currentTimeMillis());
                    sendSigninCode(phone, code);
                } else {
                    logger.info(phone + " is not signed up");
                    return "phoneNotSignedUp";
                }
            }
        }
        return Constants.SUCCESS;
    }

    private void sendSigninCode(String phone, String code) {
        phoneCodeSender.send(phone, "1", code, "5");
    }

    private void sendSignupCode(String phone, String code) {
        phoneCodeSender.send(phone, "1", code, "5");
    }

    private String getRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        // 4位验证码
        for (int i = 0; i < 4; i++) {
            code.append(String.valueOf(random.nextInt(10)));
        }
        return code.toString();
    }

    private boolean hasSentSignupCode(HttpSession session) {
        if (session.getAttribute(Constants.ATTR_SIGNUP_CODE) == null) {
            return false;
        }
        long sentTs = (long) session.getAttribute(Constants.ATTR_SIGNUP_CODE_TS);
        if (System.currentTimeMillis() - sentTs > 60 * 1000) {
            session.removeAttribute(Constants.ATTR_PHONE);
            session.removeAttribute(Constants.ATTR_SIGNUP_CODE);
            session.removeAttribute(Constants.ATTR_SIGNUP_CODE_TS);
            return false;
        }
        return true;
    }

    private boolean hasSentSigninCode(HttpSession session) {
        if (session.getAttribute(Constants.ATTR_SIGNIN_CODE) == null) {
            return false;
        }
        long sentTs = (long) session.getAttribute(Constants.ATTR_SIGNIN_CODE_TS);
        if (System.currentTimeMillis() - sentTs > 60 * 1000) {
            session.removeAttribute(Constants.ATTR_PHONE);
            session.removeAttribute(Constants.ATTR_SIGNIN_CODE);
            session.removeAttribute(Constants.ATTR_SIGNIN_CODE_TS);
            return false;
        }
        return true;
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
