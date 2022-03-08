package com.z.ne_handler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/*
    1. Handler内存泄露测试

    2. 为什么不能在子线程创建Handler

    3. textView.setText()只能在主线程执行，这句话是错误的

    4. new Handler()两种写法有什么区别

    5. ThreadLocal用法和原理
 */
public class MainActivity05 extends AppCompatActivity {

    private TextView textView;

    // TODO: 4. new Handler()两种写法有什么区别
    private Handler handler1 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            startActivity(new Intent(MainActivity05.this, PersonActivity.class));
            return false; // TODO: 返回值
        }
    });

    /*
        不推荐使用

        ActivityThread#main
            Looper.prepareMainLooper();
            ...
            Looper.loop();
     */
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            textView.setText(msg.obj.toString());   // (String) msg.obj
        }
    };

//    android.os.Handler
//
//    /**
//     * Subclasses must implement this to receive messages.
//     */
//    public void handleMessage(@android.annotation.NonNull Message msg) {
//    }
//
//    /**
//     * Handle system messages here.
//     */
//    public void dispatchMessage(@android.annotation.NonNull Message msg) {
//        if (msg.callback != null) { // post
//            handleCallback(msg);
//        } else {
//            if (mCallback != null) { // 函数传参的写法
//                if (mCallback.handleMessage(msg)) {
//                    return;
//                }
//            }
//            handleMessage(msg); // 匿名内部的写法
//        }
//    }
//
//    将子线程的Runnable对象切换到主线程中执行它的run方法
//    public final boolean post(@android.annotation.NonNull Runnable r) {
//        return  sendMessageDelayed(getPostMessage(r), 0); // getPostMessage(r)
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tv_main);
        test();
    }

    private void test() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                textView.setText("Non-UIThread");
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("fuck", "MainActivity#onDestroy");
    }

}