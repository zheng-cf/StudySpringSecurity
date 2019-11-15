package com.zcf.security.core.validate.code;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ValidateCode implements Serializable {

    /**
     * 验证码随机数
     */
    private String code;
    /**
     * 验证码过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 传递进来的不是一个确定过期时间，而是多少秒之后过期
     * @param
     * @param code
     * @param expireIn
     */
    public ValidateCode(String code, int expireIn) {
        // 调用2个标准的构造方法
        this(code, LocalDateTime.now().plusSeconds(expireIn));
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
