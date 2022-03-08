package com.z.ne_handler.core;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MyMessageQueue {

    /*
        阻塞队列
     */
    BlockingQueue<MyMessage> blockingQueue = new ArrayBlockingQueue<>(16);

    public void enqueueMessage(MyMessage msg) {
        try {
            blockingQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public MyMessage next() {
        try {
            return blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
