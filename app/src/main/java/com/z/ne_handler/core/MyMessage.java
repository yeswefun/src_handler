package com.z.ne_handler.core;

public class MyMessage {

    // 标识
    public int what;
    // 内容
    public Object obj;
    // 分发对象
    public MyHandler target;

    public MyMessage() {
    }

    public MyMessage(Object obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "MyMessage{" +
                "what=" + what +
                ", obj=" + obj +
                ", target=" + target +
                '}';
    }
}
