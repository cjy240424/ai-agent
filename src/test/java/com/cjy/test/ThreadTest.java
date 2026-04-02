package com.cjy.test;

import com.cjy.bean.MyCallable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class ThreadTest {
    @Test
    public void test1() {
        Callable<String> c1 = new MyCallable(100);
        FutureTask<String> ft1 = new FutureTask<>(c1);
        new Thread(ft1).start();

        Callable<String> c2 = new MyCallable(200);
        FutureTask<String> ft2 = new FutureTask<>(c2);
        new Thread(ft2).start();

        //取ft1的结果
        try {
            String result1 = ft1.get();
            System.out.println(result1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //取ft2的结果
        try {
            String result2 = ft2.get();
            System.out.println(result2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public  void ThreadPoolTest() {

            System.out.println("=== 🚀 Day 2 线程池测试启动 ===");

            // 🎯 你的任务：查阅资料，把下面这 7 个参数用正确的 Java 代码填进去！
            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                                                         2,
                                                         5,
                                                         2,
                                                         TimeUnit.SECONDS,
                                                        new ArrayBlockingQueue<Runnable>(3),
                                                        Executors.defaultThreadFactory(),
                                                        new ThreadPoolExecutor.AbortPolicy()
            );

            // 我们写一个循环，模拟突然来了 10 个并发请求（比如 10 个用户同时让 Agent 写文章）
            for (int i = 1; i <= 10; i++) {
                final int taskId = i;
                try {
                    // 提交任务给线程池执行
                    executor.execute(() -> {
                        System.out.println(Thread.currentThread().getName() + " 正在处理任务: " + taskId);
                        try {
                            // 模拟每个任务需要执行 1 秒钟
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    // 触发拒绝策略时会跑到这里
                    System.err.println("🚨 警告！任务 " + taskId + " 被线程池残忍拒绝了！");
                }
            }

            // 关闭线程池
            executor.shutdown();

        try {
            // 让主线程在这里死等，最多等 5 秒钟，等所有子线程干完活再往下走
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
