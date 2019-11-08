package com.zcf.security.browser;

import com.zcf.security.browser.support.SimpleResponse;
import com.zcf.security.core.properties.SecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class BrowserSecurityController {
    private RequestCache requestCache = new HttpSessionRequestCache(); //请求缓存到session中

    private Logger logger = LoggerFactory.getLogger(getClass());

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy(); //完成请求跳转

    @Autowired
    private SecurityProperties securityProperties;
    /**
     * 当需要身份认证的时候，跳转到这里
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request,response); //拿到请求
        if(savedRequest != null){
            String targetUrl = savedRequest.getRedirectUrl(); //拿到引发跳转的请求
            logger.info("引发跳转的请求是："+targetUrl);
            //判断这个请求的url是不是以html结尾
            if(StringUtils.endsWithIgnoreCase(targetUrl,".html")){
                //跳转到指定的页面，如果用户有自定义的页面就跳转到自定义的页面，如果没有则跳转到我们自己自定义的页面
                redirectStrategy.sendRedirect(request,response,securityProperties.getBrowser().getLoginPage());
            }
        }
        return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
    }
}
