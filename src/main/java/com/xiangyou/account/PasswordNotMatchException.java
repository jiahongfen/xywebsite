package com.xiangyou.account;

import org.springframework.security.core.AuthenticationException;

public class PasswordNotMatchException extends AuthenticationException {

    /**
     * 
     */
    private static final long serialVersionUID = -5671807052230497271L;

    public PasswordNotMatchException(String msg) {
        super(msg);
    }

    public PasswordNotMatchException(String msg, Throwable t) {
        super(msg, t);
    }

}
