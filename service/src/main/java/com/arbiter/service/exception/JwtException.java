package com.arbiter.service.exception;

public class JwtException extends Exception{
    //异常信息
    private String message;
    //构造函数
    public JwtException(String message){
        this.message = message;
    }
}
