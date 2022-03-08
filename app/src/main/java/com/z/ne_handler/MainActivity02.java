package com.z.ne_handler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
public class MainActivity02 extends AppCompatActivity {

    private TextView textView;

    private Handler handler1 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            startActivity(new Intent(MainActivity02.this, PersonActivity.class));
            return false;
        }
    });

    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            textView.setText(msg.obj.toString());   // (String) msg.obj
        }
    };
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tv_main);

        message = new Message();

        test();
    }

    private void test() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                第一种方式，err
                SystemClock.sleep(3000);    // 在其暂停过程中销毁activity
                message.what = 1;
                if (handler1 != null) {
//                java.lang.IllegalStateException: { when=-1d15h39m38s601ms what=1 target=android.os.Handler }
//                This message is already in use.
                    handler1.sendMessage(message);  // 启动另一个页面
                }

//                第二种方式，ok
//                message.what = 1;
//                handler1.sendMessageDelayed(message, 3000);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("fuck", "MainActivity#onDestroy");
        // 不推荐
        message.recycle();
    }

}