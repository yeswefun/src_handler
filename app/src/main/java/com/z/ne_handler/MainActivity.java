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
public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private Handler handler1 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            startActivity(new Intent(MainActivity.this, PersonActivity.class));
            return true; // TODO: 返回值
        }
    });

    /*
        不推荐使用

        ActivityThread
            static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();

        ActivityThread#main
            Looper.prepareMainLooper(); // only one looper per thread
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

    /*
        TODO: 5. ThreadLocal用法和原理, 在单元测试文件中

            public Handler(@Nullable Callback callback, boolean async) {
                // ...
                mLooper = Looper.myLooper();
                // ...
            }

            public static @Nullable Looper myLooper() {
                return sThreadLocal.get();
            }
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tv_main);
        test();
    }

    private void test() {
        /*
            子线程中更新Ui
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                textView.setText("Non-UIThread");
            }
        }).start();


        /*
            runOnUiThread
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("Non-UIThread(runOnUiThread)");
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("fuck", "MainActivity#onDestroy");
    }

}