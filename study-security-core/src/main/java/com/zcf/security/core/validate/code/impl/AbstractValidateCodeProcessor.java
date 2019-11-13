package com.zcf.security.core.validate.code.impl;

import com.zcf.security.core.validate.code.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {
    /**
     * spring提供的操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    /**
     * 手机代码中所有的ValidateCodeGenerator验证码生成器接口的实现类
     * 这里是利用了spring框架的功能，使用Autowired，spring会将所有ValidateCodeGenerator接口
     * 的实现放入到map中，以bean的名称为key
     */
    @Autowired
    private Map<String , ValidateCodeGenerator> validateCodeGenerators;

    /**
     * 生成校验码
     * @param request
     * @throws Exception
     */
    @Override
    public void create(ServletWebRequest request) throws Exception {
        C validateCode = generate(request);
        //保存验证码
        save(request,validateCode);
        //发送验证码
        send(request,validateCode);
    }

    /**
     * 发送验证码由子类实现
     * @param request
     * @param validateCode
     */
    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

    /**
     * 保存校验码
     * 保存在session中时的前缀为SEESION_KEY_PREFIXIMAGE或者SEESION_KEY_PREFIXSMS
     * @param request
     * @param validateCode
     */
    private void save(ServletWebRequest request, C validateCode) {
        sessionStrategy.setAttribute(request,getSessionKey(request),validateCode);
    }

    private String getSessionKey(ServletWebRequest request) {
        return SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
    }

    /**
     * 生成校验码
     * 根据请求的路径判断请求的是什么类型的校验码
     * 因为只要生成校验码就必须得实现接口ValidateCodeGenerator
     * 拿到所有实现ValidateCodeGenerator接口的bean
     * 调用对应bean中的generate方法
     * @param request
     * @return
     */
    private C generate(ServletWebRequest request) {
        String type = getValidateCodeType(request).toString().toLowerCase();
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(type+"ValidateCodeGenerator");
        return (C) validateCodeGenerator.generate(request);
    }



    /**
     * 根据请求的后半段，判断请求的是短信验证码还是图片验证码
     * @param request
     * @return
     */

    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        String simpleName = getClass().getSimpleName();
        String type = StringUtils.substringBefore(simpleName, "CodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    @Override
    public void validate(ServletWebRequest request) {
        ValidateCodeType processorType = getValidateCodeType(request);
        String sessionKey = getSessionKey(request);

        C codeInSession = (C) sessionStrategy.getAttribute(request,sessionKey);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),processorType.getParamNameOnValidate());
        }catch (ServletRequestBindingException e){
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException(processorType + "验证码不存在");
        }

        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException(processorType + "验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, sessionKey);

    }


}
