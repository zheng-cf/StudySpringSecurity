package com.zcf.security.core.properties;

import lombok.Data;

/**
 * 图形验证码的配置
 */
@Data
public class ValidateCodeProperties {
    /**
     * 类ImageCodeProperties用来定义验证码的属性，长宽高，验证码位数，过期时间等
     */
    private ImageCodeProperties image = new ImageCodeProperties();

    /**
     * 短信验证码配置
     *
     */
    private SmsCodeProperties sms = new SmsCodeProperties();


}
