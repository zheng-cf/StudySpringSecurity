package com.zcf.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 操作验证码
 */
public interface ValidateCodeRepository {
    /**
     * 保存验证码
     * @param request
     * @param code
     * @param validateCodeType
     */
    void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType);

    /**
     * 获取验证码
     * @param processorType
     * @param request
     * @return
     */
    ValidateCode get(ValidateCodeType processorType, ServletWebRequest request);

    /**
     * 删除验证码
     * @param request
     * @param processorType
     */
    void remove(ServletWebRequest request, ValidateCodeType processorType);
}
