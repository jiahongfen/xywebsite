package com.xiangyou.account;

import org.springframework.security.core.AuthenticationException;

public class PhoneCodeNotFoundException extends AuthenticationException {

    /**
     *
     */
    private static final long serialVersionUID = 188326200765789243L;

    public PhoneCodeNotFoundException() {
        super("Phone code not found");
    }

    public PhoneCodeNotFoundException(String msg) {
        super(msg);
    }

    public PhoneCodeNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

}
