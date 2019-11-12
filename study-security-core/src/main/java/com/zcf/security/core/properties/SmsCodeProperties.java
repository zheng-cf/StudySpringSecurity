package com.zcf.security.core.properties;

/**
 * 默认配置
 */
public class SmsCodeProperties {

    /**
     * 验证码长度，也就是验证码数字位数
     */
    private int length = 6;
    /**
     * 失效时间
     */
    private int expireIn = 60;
    /**
     * 需要使用验证码的请求
     */
    private String url;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
