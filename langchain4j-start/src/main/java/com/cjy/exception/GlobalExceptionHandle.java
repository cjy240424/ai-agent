package com.cjy.exception;

import com.cjy.record.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    @ExceptionHandler
    public R exceptionHandle(Exception e){
        log.error("【系统异常】{}",e.getMessage(),e);
        return R.error("您此时的操作出现了问题");
    }

    @ExceptionHandler
    public R handleDuplicateKeyException(RuntimeException e){
        log.error("程序出RuntimeException了！", e);

        return R.error("按规矩办事！你好，我好，大家好！");
    }
}
