package com.z.ne_handler;

import androidx.annotation.Nullable;

import org.junit.Test;

public class ThreadLocalTest {

    /*
        ThreadLocal
            key: thread
            val: T

        方法:
            get
            set
            remove

         阅读一下ThreadLocal的源码
     */
    @Test
    public void test00() {

        ThreadLocal<String> tl = new ThreadLocal<String>() {
            @Nullable
            @Override
            protected String initialValue() {
                // tl.get(); // 看一下get的源码
                return "默认值";
            }
        };

        System.out.println("ThreadLocal#get: " + tl.get()); // 默认值

        // ---------> thread-0
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 拿不到值就初始化一个新map
                System.out.println("ThreadLocal#get(thread-0): " + tl.get()); // 默认值
                tl.set("thread-0");
                System.out.println("ThreadLocal#get(thread-0): " + tl.get()); // thread-0
                // 使用完成之后建议remove, 避免无意义的内存占用
                tl.remove();
            }
        });
        thread.start();


        // ---------> thread-1
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // 拿不到值就初始化一个新map
                System.out.println("ThreadLocal#get(thread-1): " + tl.get()); // 默认值
                tl.set("thread-1");
                System.out.println("ThreadLocal#get(thread-1): " + tl.get()); // thread-1
                // 使用完成之后建议remove, 避免无意义的内存占用
                tl.remove();
            }
        });
        thread1.start();


        System.out.println("ThreadLocal#get: " + tl.get()); // 默认值
    }
}
