package com.zcf.security.core.properties;

public class BrowserProperties {
    /**
     * 赋予loginPage默认值，也就是如果用户不指定自己的登录页面，
     * 则使用我们提供的默认登录页面
     */
    private String loginPage = "/zcf-signIn.html";

    /**
     * 配置登录之后是跳转还是返回json
     * @return
     */
    private LoginType loginType = LoginType.JSON;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }
}
