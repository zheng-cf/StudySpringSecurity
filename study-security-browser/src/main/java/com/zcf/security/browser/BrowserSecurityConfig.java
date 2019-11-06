package com.zcf.security.browser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder(){
        /**
         * 这里返回的是spring默认的PasswordEncoder的实现类，用的也是默认的加密方法
         * 通过查看PasswordEncoder接口的源码可以看到有两个方法，一个是对传进来的数据进行加密的方法
         * 一个是调用这个方法，spring会自己判断传进来的数据是否符合加密规则
         * 如果需要使用自己的加密方法，那么这个方法要实现PasswordEncoder这个接口，然后返回
         */
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()                    //基于form表单的登录验证
                .and()
                .authorizeRequests()        //关于请求的一些配置
                .anyRequest()               //所有的请求
                .authenticated();            //身份验证
    }
}
