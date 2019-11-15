package com.zcf.security.browser.validate.code.impl;

import com.zcf.security.core.validate.code.ValidateCode;
import com.zcf.security.core.validate.code.ValidateCodeRepository;
import com.zcf.security.core.validate.code.ValidateCodeType;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

@Component
public class SessionValidateCodeRepository implements ValidateCodeRepository {
    /**
     * 验证码放入session时的前缀
     */
    private String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";
    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
        sessionStrategy.setAttribute(request,getSessionKey(validateCodeType),code);
    }



    @Override
    public ValidateCode get(ValidateCodeType processorType, ServletWebRequest request) {
        return (ValidateCode) sessionStrategy.getAttribute(request,getSessionKey(processorType));
    }

    @Override
    public void remove(ServletWebRequest request, ValidateCodeType processorType) {
        sessionStrategy.removeAttribute(request,getSessionKey(processorType));
    }

    private String getSessionKey(ValidateCodeType validateCodeType) {
        return SESSION_KEY_PREFIX+validateCodeType.toString().toUpperCase();
    }


}
