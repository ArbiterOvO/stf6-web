package com.arbiter.service.exception;

public class FileUploadException  extends Exception{
    //异常信息
    private String message;
    //构造函数
    public FileUploadException(String message){
        this.message = message;
    }
}
