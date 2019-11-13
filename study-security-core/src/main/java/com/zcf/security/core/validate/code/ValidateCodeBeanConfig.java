package com.zcf.security.core.validate.code;

import com.zcf.security.core.properties.SecurityProperties;
import com.zcf.security.core.validate.code.image.ImageCodeGenerator;
import com.zcf.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.zcf.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidateCodeBeanConfig {
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 这里ConditionalOnMissingBean注解一定要有，
     * 这个注解意味着，如果spring容器中没有名称为imageCodeGenerator的对象
     * 那么就用我们下边这个自己书写的
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator(){
        /**
         * 创建ValidateCodeGenerator的实现类ImageCodeGenerator
         */
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        //codeGenerator.setSecurityProperties(securityProperties);
        return codeGenerator;
    }
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeGenerator(){
        return new DefaultSmsCodeSender();
    }
}
