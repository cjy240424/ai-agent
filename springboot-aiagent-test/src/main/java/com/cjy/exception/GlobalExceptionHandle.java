package com.cjy.exception;

import com.cjy.domain.po.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    @ExceptionHandler
    public Result exceptionHandle(Exception e){
        log.error("【系统异常】{}",e.getMessage(),e);
        return Result.error("您此时的操作出现了问题");
    }

    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e){
        log.error("程序出DuplicateKeyException了！", e);

        //获取错误信息
        String message = e.getMessage();
        //定位错误关键点
        int i = message.indexOf("Duplicate entry");
        String s = message.substring(i);
        String[] split = s.split(" ");

        return Result.error(split[2] + "已经存在了！");
    }
}
