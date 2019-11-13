package com.zcf.security.core.validate.code.image;

import com.zcf.security.core.validate.code.ValidateCode;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public class ImageCode extends ValidateCode {

    private BufferedImage image;

    /**
     * 传递进来的不是一个确定过期时间，而是多少秒之后过期
     * @param image
     * @param code
     * @param expireIn
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code,expireIn);
        this.image = image;
    }

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        super(code,expireTime);
        this.image = image;
    }

    /**
     * 判断是否过期，过期时间expireTime如果在当前时间之后就没有过期
     * 否则就过期
     * @return
     */

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
