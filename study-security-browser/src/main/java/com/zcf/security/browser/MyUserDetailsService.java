package com.zcf.security.browser;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("myUserDetailService")
public class MyUserDetailsService implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("登录用户名："+username);
        /**
         * 根据用户名查找用户信息
         * 根据查找的用户信息判断用户是否被冻结
         * 这里返回的是spring默认的User对象，通过查看源码可以发现这个User对象实现了UserDetails接口
         * 在实际的开发中我们只需要让自己的User对象去实现UserDetails
         * 然后通过去数据库中查询到这个User对象，判断账户的一些信息
         * 例如是否被冻结，密码是否正确，具有什么权限，账户是否可用，总共七个。
         */
        String password = passwordEncoder.encode("123456");
        logger.info("数据库密码是："+password);
        /**
         * 创建一个User，赋予这个user密码为123456，并且具有权限为"admin"
         */
        User user = new User(username,"123456",AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

        return new User(username,password,true,true,true,true, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
