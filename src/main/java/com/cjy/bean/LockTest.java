package com.cjy.bean;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    // 共享资源：火车站总票数
    static int tickets = 10000;
    private static final Lock l = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 🚂 疯狂售票系统启动 ===");

        // 定义卖票的任务：每个线程连卖 100 张
        Runnable sellTask =  () -> {
            l.lock();
            try {
                for (int i = 0; i < 100; i++) {
                    tickets--; // 卖出一张票
                }
            } finally {
                l.unlock();
            }
        };

        // 创建 100 个线程窗口
        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(sellTask);
            threads[i].start(); // 开卖！
        }

        // 让主线程耐心等待这 100 个窗口全部卖完
        for (int i = 0; i < 100; i++) {
            threads[i].join();
        }

        // 公布最终结果
        System.out.println("卖票结束！最终剩余票数：" + tickets);
    }
}