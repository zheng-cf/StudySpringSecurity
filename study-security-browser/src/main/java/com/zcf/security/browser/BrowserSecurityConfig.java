package com.zcf.security.browser;

import com.zcf.security.core.properties.SecurityProperties;
import com.zcf.security.core.validate.code.ValidateCodeFilter;
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
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
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

        ValidateCodeFilter filter = new ValidateCodeFilter();
        filter.setSecurityProperties(securityProperties);
        filter.afterPropertiesSet();

        filter.setAuthenticationFailureHandler(zcfAuthenticationFailureHandler);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()                                      //基于form表单的登录验证
                .loginPage("/authentication/require")                //指定登录页面
                .loginProcessingUrl("/authentication/form")   //表单的action路径
                .successHandler(zcfAuthenticationSuccessHandler)//配置自己写的登录成功处理器
                .failureHandler(zcfAuthenticationFailureHandler)
                .and()
                .rememberMe()//配置记住我
                .tokenRepository(persistentTokenRepository())//设置tokenRepository
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())//设置过期时间
                .userDetailsService(myUserDetailService)//设置登录的服务
                .and()
                .authorizeRequests()        //关于请求的一些配置
                /**
                 * 这里需要注意一个错误，如果不适用antMatchers().permitAll()将登录的url设置为不需要身份验证的话
                 * 那么就会导致，请求重定向次数过多错误
                 */
                .antMatchers("/authentication/require",securityProperties.getBrowser().getLoginPage(),
                        "/code/*").permitAll() //访问这个路径不需要身份验证
                .anyRequest()               //所有的请求
                .authenticated()            //身份验证
                .and()
                .csrf().disable();          //关闭跨站防护
    }
}
