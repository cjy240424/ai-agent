package com.cjy.utils;

/**
 * 👑 架构师利器：基于 ThreadLocal 的业务上下文收纳盒
 * 确保在并发环境下，每个用户的请求都能安全隔离自己的 category
 */
public class CategoryContextHolder {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    // 存入分类标识 (例如: "hr_rule" 或 "logistics")
    public static void set(String category) {
        CONTEXT.set(category);
    }

    // 获取当前线程的分类标识
    public static String get() {
        return CONTEXT.get();
    }

    // 物理拔管：请求结束时必须清理，防止内存泄漏和线程池污染！
    public static void clear() {
        CONTEXT.remove();
    }
}