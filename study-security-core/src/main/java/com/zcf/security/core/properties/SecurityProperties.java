package com.zcf.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 使用ConfigurationProperties配置一个zcf.security的属性
 */
@ConfigurationProperties(prefix = "zcf.security")
public class SecurityProperties {
    /**
     * 提供BrowserProperties类获取的方法
     */
    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties code = new ValidateCodeProperties();


    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }
}
