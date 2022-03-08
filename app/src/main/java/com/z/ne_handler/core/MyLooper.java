package com.z.ne_handler.core;

public class MyLooper {

    /*
        sThreadLocal.get() will return null unless you've called prepare().
     */
    static final ThreadLocal<MyLooper> sThreadLocal = new ThreadLocal<>();
    public MyMessageQueue mQueue;

    public static MyLooper myLooper() {
        return sThreadLocal.get();
    }

    private MyLooper() {
        mQueue = new MyMessageQueue();
    }

    public static void prepare() {
        // 保证单个线程中唯一
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        // 应用启动时，初始化
        sThreadLocal.set(new MyLooper());
    }

    public static void loop() {
        /*
            从全局唯一ThreadLocalMap中获取looper对象
         */
        MyLooper looper = myLooper();
        /*
            从全局唯一looper对象中获取全局唯一MessageQueue
         */
        MyMessageQueue queue = looper.mQueue;

        MyMessage message;
        for (; ; ) {
            MyMessage msg = queue.next();
            if (msg != null && msg.target != null) {
                msg.target.dispatchMessage(msg);
            }
        }
    }
}
