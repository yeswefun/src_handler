package com.z.ne_handler;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/*
    1. Handler内存泄露测试

    2. 为什么不能在子线程创建Handler

    3. textView.setText()只能在主线程执行，这句话是错误的

    4. new Handler()两种写法有什么区别

    5. ThreadLocal用法和原理
 */
public class MainActivity03 extends AppCompatActivity {

    private TextView textView;

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
                /*
                TODO: 2. 为什么不能在子线程创建Handler

                android.app.ActivityThread#main
                    Looper.prepareMainLooper();
                        prepare(false);
                            主线程创建looper并放到ThreadLocal中
                            在子线程中并没有创建looper并放到ThreadLocal中，所以找不到
                            java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
                 */
                new Handler();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("fuck", "MainActivity#onDestroy");
    }

}