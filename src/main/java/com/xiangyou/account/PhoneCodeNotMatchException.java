package com.xiangyou.account;

import org.springframework.security.core.AuthenticationException;

public class PhoneCodeNotMatchException extends AuthenticationException {

    /**
     *
     */
    private static final long serialVersionUID = -6178477976963783014L;

    public PhoneCodeNotMatchException() {
        super("Phone code not match");
    }

    public PhoneCodeNotMatchException(String msg) {
        super(msg);
    }

    public PhoneCodeNotMatchException(String msg, Throwable t) {
        super(msg, t);
    }

}
