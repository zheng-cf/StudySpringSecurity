package com.zcf.security.core.authentication;

import com.zcf.security.core.properties.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationFailureHandler zcfAuthenticationFailureHandler;

    @Autowired
    private AuthenticationSuccessHandler zcfAuthenticationSuccessHandler;

    protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception{
        http
                // formLogin()是表单登录,httpBasic()是默认的弹窗登录
                .formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                // 告诉UsernamePasswordAuthenticationFilter处理下面这个请求
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(zcfAuthenticationSuccessHandler)
                .failureHandler(zcfAuthenticationFailureHandler);

    }


}