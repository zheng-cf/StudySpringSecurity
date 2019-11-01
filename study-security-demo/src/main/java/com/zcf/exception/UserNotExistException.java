package com.zcf.exception;

public class UserNotExistException extends RuntimeException {

    private static final long serialVersionUID = 6499960308360766672L;

    private String id;

    public UserNotExistException(String id){
        //使用父类构造当抛出该异常的时候附带异常信息
        super("user not exist");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
