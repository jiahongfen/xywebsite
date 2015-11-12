package com.xiangyou.account;

import org.springframework.security.core.AuthenticationException;

public class UsernameNotFoundException extends AuthenticationException {

    /**
     * 
     */
    private static final long serialVersionUID = -7483355071092978762L;

    public UsernameNotFoundException(String msg) {
        super(msg);
    }

    public UsernameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

}
