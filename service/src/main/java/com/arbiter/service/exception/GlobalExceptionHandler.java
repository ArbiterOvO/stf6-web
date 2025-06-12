package com.arbiter.service.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import com.arbiter.common.result.Result;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //JWT令牌出错
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result JwtError(JwtException e) {
        e.printStackTrace();
        return Result.error(ErrorConstant.JWTRrror,e.getMessage());
    }

    @ExceptionHandler(Exception.class)//捕获所有异常
    public Result exception(Exception e) {
        e.printStackTrace();
        return Result.error("操作失败，联系管理员");
    }

}
