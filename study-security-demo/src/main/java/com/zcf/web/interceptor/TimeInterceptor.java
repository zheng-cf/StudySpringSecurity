package com.zcf.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import java.util.Date;
//@Component
public class TimeInterceptor implements HandlerInterceptor {
    /**
     * 访问controller里面方法之前被调用
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("preHandle");
        System.out.println("调用controller中的类名为"+((HandlerMethod)o).getBean().getClass().getName());
        System.out.println("调用controller中的方法名为"+((HandlerMethod)o).getMethod().getName());
        httpServletRequest.setAttribute("startTime",new Date().getTime());
        //返回false则controller中的方法不会被调用
        return true;
    }

    /**
     * 当controller方法执行完毕紧接着调用这个方法，如果controller中的方法抛出了异常那么这个方法就不会被调用
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
        Long start = (Long)httpServletRequest.getAttribute("startTime");
        System.out.println("time interceptor:" + (new Date().getTime() - start));

    }

    /**
     * 这个方法时不管是否有异常等等，都会被调用
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("afterCompletion");
        Long start = (Long)httpServletRequest.getAttribute("startTime");
        System.out.println("time interceptor:" + (new Date().getTime() - start));
        System.out.println("exception = "+e);

    }
}
