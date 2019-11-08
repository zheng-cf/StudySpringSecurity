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


    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }
}
