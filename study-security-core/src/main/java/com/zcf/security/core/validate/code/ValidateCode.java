package com.zcf.security.core.validate.code;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public class ValidateCode {

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
    public ValidateCode( String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ValidateCode( String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    /**
     * 判断是否过期，过期时间expireTime如果在当前时间之后就没有过期
     * 否则就过期
     * @return
     */
    public boolean isExpried(){
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
