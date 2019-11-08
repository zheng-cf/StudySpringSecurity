package com.zcf.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcf.security.browser.support.SimpleResponse;
import com.zcf.security.core.properties.LoginType;
import com.zcf.security.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationFailureHandler失败处理器接口
 */
@Component("zcfAuthenticationFailureHandler")
public class ZcfAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        /**
         * 与登录成功处理器接口不同的是，方法第三个参数AuthenticationException不是Authentication
         * 因为登录失败时没法拿到用户的信息的
         */
        logger.info("登录失败");
        if(LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            //将authentication转换成json格式的字符串返回
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));

        }else{
            super.onAuthenticationFailure(request,response,exception);

        }

    }
}
