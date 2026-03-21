package com.cjy.domain.po;

import lombok.Data;

import java.util.List;

@Data
public class Result {
    private Integer code; // 状态码 1成功 0失败
    private String message; // 提示信息
    private Object data; // 数据

    public static Result success() {
        Result result = new Result();
        result.code = 1;
        result.message = "success";
        return result;
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.data = object;
        result.code = 1;
        result.message = "success";
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.message = msg;
        result.code = 0;
        return result;
    }
}
