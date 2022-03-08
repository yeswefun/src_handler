package com.z.ne_handler;

import com.z.ne_handler.core.MyHandler;
import com.z.ne_handler.core.MyLooper;
import com.z.ne_handler.core.MyMessage;

import org.junit.Test;

/*
    存入消息
        handler.sendMessage(msg)

    消费消息
        looper.loop()
 */
public class ActivityThreadTest {

    @Test
    public static void main(String[] args) {
        /*
            创建全局唯一，主线程looper对象，以及messageQueue对象
         */
        MyLooper.prepare();

        /*
            模拟activity中创建handler对象
         */
        MyHandler handler = new MyHandler() {
            @Override
            public void handleMessage(MyMessage msg) {
                System.out.println(msg.obj.toString());
            }
        };

        /*
            子线程发送消息
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyMessage msg = new MyMessage();
                msg.obj = "Non-UIThread";
                handler.sendMessage(msg);
            }
        }).start();

        /*
            轮询消费消息
         */
        MyLooper.loop();
    }
}
