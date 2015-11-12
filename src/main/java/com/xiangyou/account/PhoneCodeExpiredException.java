package com.xiangyou.account;

import org.springframework.security.core.AuthenticationException;

public class PhoneCodeExpiredException extends AuthenticationException {

    /**
     *
     */
    private static final long serialVersionUID = 2871923925916424480L;

    public PhoneCodeExpiredException() {
        super("Phone code expired");
    }

    public PhoneCodeExpiredException(String msg) {
        super(msg);
    }

    public PhoneCodeExpiredException(String msg, Throwable t) {
        super(msg, t);
    }

}
