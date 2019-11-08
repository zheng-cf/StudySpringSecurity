package com.zcf.security.core.validate.code;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 继承OncePerRequestFilter类，保证我们自己的过滤器只会被调用一次
 */
public class ValidateCodeFilter extends OncePerRequestFilter {

    private static AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /**
         * 这里要进行判断一下，保证逻辑的严谨。
         * 请求的地址必须是"/authentication/form"，而且要是post请求
         * 否则直接放行
         */
        if(StringUtils.equals("/authentication/form",request.getRequestURI()) &&
        StringUtils.endsWithIgnoreCase(request.getMethod(),"post")){

            try {
                /**
                 * 使用validate方法验证请求中验证码的信息
                 * 这里因为后边需要使用到ServletWebRequest
                 * 所以对我们的HttpServletRequest进行包装一下
                 */
                validate(new ServletWebRequest(request));

            }catch (ValidateCodeException e){
                /**
                 * 使用前边写的登录失败处理器处理异常
                 */
                authenticationFailureHandler.onAuthenticationFailure(request,response,e);
                return;
            }

        }

        filterChain.doFilter(request,response);
    }

    /**
     * 从请求中拿到验证码，完成验证。
     * @param request
     * @throws ServletRequestBindingException
     */
    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        /**
         * 从session中拿到放进去的ImageCode
         */
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request,ValidateCodeController.SESSION_KEY);

        /**
         * 从Form请求中拿到imageCode的值，
         * 这里imageCode是我们在<input type="text" name="imageCode">中name的值
         */
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),"imageCode");

        if(StringUtils.isBlank(codeInRequest)){
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if(codeInSession == null){
            throw new ValidateCodeException("验证码不存在");
        }

        if(codeInSession.isExpried()){
            sessionStrategy.removeAttribute(request,ValidateCodeController.SESSION_KEY);
            throw new ValidateCodeException("验证码已过期");
        }
        if(!StringUtils.equals(codeInSession.getCode(),codeInRequest)){
            throw new ValidateCodeException("验证码不匹配");
        }

        sessionStrategy.removeAttribute(request,ValidateCodeController.SESSION_KEY);

    }
    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }


}
