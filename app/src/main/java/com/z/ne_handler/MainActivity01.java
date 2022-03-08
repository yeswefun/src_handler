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
public class MainActivity01 extends AppCompatActivity {

    private TextView textView;

    private Handler handler1 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            startActivity(new Intent(MainActivity01.this, PersonActivity.class));
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
                Message message = new Message();
//                message.obj = "6";
//                message.what = 6;
//                handler2.sendMessage(message);


//                TODO: 1. Handler内存泄露测试

//                第一种写法，无法在onDestroy中移除，因为在暂停过程中消息并没有放入消息队列中
//                SystemClock.sleep(3000);    // 在其暂停过程中销毁activity
//                message.what = 1;
//                handler1.sendMessage(message);  // 启动另一个页面

//                fix-1
                SystemClock.sleep(3000);    // 在其暂停过程中销毁activity
                message.what = 1;
                if (handler1 != null) {
                    handler1.sendMessage(message);  // 启动另一个页面
                }

//                第二种写法，可以在onDestroy中移除，因为已经放入消息队列中
//                message.what = 1;
//                handler1.sendMessageDelayed(message, 3000);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("fuck", "MainActivity#onDestroy");
        handler1.removeMessages(1); // 移除消息队列中的消息
        // fix-1
        handler1 = null;
    }

}