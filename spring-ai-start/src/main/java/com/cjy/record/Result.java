package com.cjy.record;

// 全局统一的 API 响应体
public record Result<T>(int code, String msg, T data) {

    // 1. 成功：携带具体业务数据返回 (例如：查询操作)
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    // 2. 成功：无参数返回 (例如：更新、删除操作，只需要告诉前端成功了即可)
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    // 3. 失败：自定义错误码和错误信息返回
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    // 4. 失败：只传错误信息的快速返回 (默认返回 500 状态码，后端日常报错最常用)
    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }
}