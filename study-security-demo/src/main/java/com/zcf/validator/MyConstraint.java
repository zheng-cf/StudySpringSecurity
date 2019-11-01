package com.zcf.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//Target声明注解可以写在哪些地方
@Target({ElementType.METHOD,ElementType.FIELD})
//Retention声明注解在什么时候启用
@Retention(RetentionPolicy.RUNTIME)
//声明注解使用的校验逻辑是哪个类
@Constraint(validatedBy = MyConstraintValidator.class)
public @interface MyConstraint {
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
