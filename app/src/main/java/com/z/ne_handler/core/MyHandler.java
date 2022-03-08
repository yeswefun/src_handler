package com.z.ne_handler.core;

public class MyHandler {

    private final MyLooper mLooper;
    private final MyMessageQueue mQueue;

    public MyHandler() {
        mLooper = MyLooper.myLooper();
        if (mLooper == null) {
            // 从子线程中获取looper(如果没有prepare的话是拿不到的)
            throw new RuntimeException("Can't create handler inside thread "
                    + Thread.currentThread()
                    + " that has not called Looper.prepare()");
        }
        mQueue = mLooper.mQueue;
    }

    /**
     * Subclasses must implement this to receive messages.
     */
    public void handleMessage(MyMessage msg) {

    }

    /*
        将消息放入队列中
     */
    public void sendMessage(MyMessage msg) {
        enqueueMessage(msg);
    }

    private void enqueueMessage(MyMessage msg) {
        // 赋值为当前Handler对象
        msg.target = this;
        // 使用mQueue将消息存入
        mQueue.enqueueMessage(msg);
    }

    public void dispatchMessage(MyMessage msg) {
        handleMessage(msg);
    }
}
