package com.zcf.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 定义一个接口，这个接口用来生成验证码的
 * 也就是我们这个框架默认的验证码生成器
 * 如果用户要使用自己的验证码生成器
 * 要做的不是来改我们的代码，而是实现这个接口
 * 在createImageCode方法中实现自己的验证码生成逻辑
 */
public interface ValidateCodeGenerator {
    ValidateCode createImageCode(ServletWebRequest request);
}
