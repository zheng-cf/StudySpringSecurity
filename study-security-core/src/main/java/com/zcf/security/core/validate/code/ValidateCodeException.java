package com.zcf.security.core.validate.code;


import org.springframework.security.core.AuthenticationException;

public class ValidateCodeException extends AuthenticationException {
    /**
     * 自定义验证码异常
     */
    private static final long serialVersionUID = 7012308720900016006L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
