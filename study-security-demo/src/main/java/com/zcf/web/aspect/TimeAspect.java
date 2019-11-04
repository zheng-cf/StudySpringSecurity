package com.zcf.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.bouncycastle.math.Primes;
import org.springframework.stereotype.Component;

import java.util.Date;

/*@Aspect
@Component*/
public class TimeAspect {
    @Around("execution(* com.zcf.web.controller.UserController.*(..))")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("time aspect start ");
        Object[] args = pjp.getArgs();
        /**
         * 请求传递的参数
         */
        for (Object arg : args) {
            System.out.println("arg is "+arg);
        }
        Long start = new Date().getTime();
        Object proceed = pjp.proceed();
        System.out.println("time aspect 耗时："+(new Date().getTime() - start));
        System.out.println("time aspect end");
        return proceed;
    }

}
