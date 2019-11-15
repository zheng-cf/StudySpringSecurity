package com.zcf.security.browser;

import com.zcf.security.core.authentication.AbstractChannelSecurityConfig;
import com.zcf.security.core.authentication.mobile.SmsCodeAuthenticationFilter;
import com.zcf.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.zcf.security.core.properties.SecurityProperties;
import com.zcf.security.core.validate.code.ValidateCodeFilter;
import com.zcf.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.UserDetailsServiceConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    /**
     * 导入自己写的AuthenticationSuccessHandler的实现类
     */
    @Autowired
    private AuthenticationSuccessHandler zcfAuthenticationSuccessHandler;

    /**
     * 导入自己写的AuthenticationFailureHandler的实现类
     */
    @Autowired
    private AuthenticationFailureHandler zcfAuthenticationFailureHandler;

    /**
     *
     *
     */
    @Autowired
    private UserDetailsService myUserDetailService;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        /**
         * 配置数据源
         */
        tokenRepository.setDataSource(dataSource);
        /**
         * 在JdbcTokenRepositoryImpl中有数据库脚本
         * 我们使用脚本来创建表
         * 但是在第二次重启服务的时候需要注释掉
         */
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //配置和用户名密码登录相关的配置
        applyPasswordAuthenticationConfig(http);

        ValidateCodeFilter filter = new ValidateCodeFilter();
        filter.setSecurityProperties(securityProperties);
        filter.afterPropertiesSet();

        filter.setAuthenticationFailureHandler(zcfAuthenticationFailureHandler);
        http
                // 设置验证码相关的配置
                .apply(validateCodeSecurityConfig)
                .and()
                // 设置短信登录相关的配置
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()

                .rememberMe()//配置记住我
                .tokenRepository(persistentTokenRepository())//设置tokenRepository
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())//设置过期时间
                .userDetailsService(myUserDetailService)//设置登录的服务
                .and()
                .authorizeRequests()        //关于请求的一些配置
                .antMatchers("/authentication/require",securityProperties.getBrowser().getLoginPage(),
                        "/code/*").permitAll() //访问这个路径不需要身份验证
                .anyRequest()               //所有的请求
                .authenticated()            //身份验证
                .and()
                .csrf().disable();          //关闭跨站防护
    }
}
