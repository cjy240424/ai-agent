package com.cjy.exception;

import com.cjy.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result handleException(Exception e) {
        log.error("程序出错了！", e);
        return Result.error("出现错误了，请你不要原神牛逼！");
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
