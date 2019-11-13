package com.zcf.security.core.validate.code;

import com.zcf.security.core.properties.SecurityConstants;
import com.zcf.security.core.properties.SecurityProperties;
import com.zcf.security.core.utils.ApplicationUtils;
import com.zcf.security.core.validate.code.image.ImageCode;
import com.zcf.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 继承OncePerRequestFilter类，保证我们自己的过滤器只会被调用一次
 * 实现这个InitializingBean接口，其他参数组装完毕之后去初始化url
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private static AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    private SecurityProperties securityProperties = new SecurityProperties();

    /**
     * 系统中的校验码处理器
     */
//    @Autowired
//    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 存放所有需要校验验证码的url
     */
    private Map<String, ValidateCodeType> urlMap = new HashMap<>();

    private Set<String> urls = new HashSet<>();

    String s = ValidateCodeProcessor.SESSION_KEY_PREFIX;
    /**
     * 工具类，因为请求url中有,/user,/user/*，所以不能使用使用equals去完成路径的匹配
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM,ValidateCodeType.IMAGE);
        addUrlToMap(securityProperties.getCode().getImage().getUrl(),ValidateCodeType.IMAGE);

        urlMap.put(SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE,ValidateCodeType.SMS);
        addUrlToMap(securityProperties.getCode().getImage().getUrl(),ValidateCodeType.SMS);
    }

    private void addUrlToMap(String urlString, ValidateCodeType type) {
        if(StringUtils.isNotBlank(urlString)){
            StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString,",");
            for (String url : urls) {
                urlMap.put(url,type);
            }
        }

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ValidateCodeType type = getValidateCodeType(request);
        if(type != null){
            logger.info("校验请求("+request.getRequestURI()+")中的验证码，验证码类型" + type);
            try {
                ValidateCodeProcessorHolder validateCodeProcessorHolder = ApplicationUtils.getBean(ValidateCodeProcessorHolder.class);
                validateCodeProcessorHolder.findValidateCodeProcessor(type).validate(new ServletWebRequest(request,response));
                logger.info("验证码校验通过");
            }catch (ValidateCodeException e){
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }

        }
        filterChain.doFilter(request,response);
    }

    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType result = null;
        if(!StringUtils.endsWithIgnoreCase(request.getMethod(),"get")){
            //遍历集合
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if(pathMatcher.match(url,request.getRequestURI())){
                    result = urlMap.get(url);
                }
            }

        }
        return result;
    }


    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }
}
