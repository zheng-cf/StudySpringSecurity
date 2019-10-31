package com.zcf.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MyConstraintValidator implements ConstraintValidator<MyConstraint,Object> {
    /**
     * 这里可以注入其他的类，然后使用这个类对传进来的值进行验证
     *
     */
    @Override
    public void initialize(MyConstraint myConstraint) {
        System.out.println("my validator init");
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println("校验值"+o);
        return false;
    }
}
