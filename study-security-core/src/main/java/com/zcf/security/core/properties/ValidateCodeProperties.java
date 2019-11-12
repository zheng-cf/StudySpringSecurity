package com.zcf.security.core.properties;

import com.zcf.security.core.validate.code.ImageCode;

/**
 * 图形验证码的配置
 */
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

    public ImageCodeProperties getImage() {
        return image;
    }

    public void setImage(ImageCodeProperties image) {
        this.image = image;
    }

    public SmsCodeProperties getSms() {
        return sms;
    }

    public void setSms(SmsCodeProperties sms) {
        this.sms = sms;
    }
}
