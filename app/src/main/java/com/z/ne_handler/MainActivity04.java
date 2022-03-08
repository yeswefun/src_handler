package com.z.ne_handler;

import android.os.Bundle;
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
public class MainActivity04 extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tv_main);
        test();
    }

    /*
        void checkThread() {
            if (mThread != Thread.currentThread()) {
                throw new CalledFromWrongThreadException(
                        "Only the original thread that created a view hierarchy can touch its views.");
            }
        }
        它检查的并不是当前线程是否是UI线程，
        而是当前线程是否是操作线程。这个操作线程就是创建ViewRootImpl对象的线程：
        public ViewRootImpl(Context context, Display display) {
           ...
           mThread = Thread.currentThread();
           ...
        }
     */
    private void test() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                /*
                    android.view.ViewRootImpl$CalledFromWrongThreadException:
                    Only the original thread that created a view hierarchy can touch its views.
                 */
//                SystemClock.sleep(3000); // comment，为什么延迟一下就会报错

                /*
                    TODO: 3. textView.setText()只能在主线程执行，这句话是错误的

                    TextView#setText
                        if (mLayout != null) {
                            checkForRelayout();
                        }
                    TextView#checkForRelayout
                        requestLayout();
                    View#requestLayout
                        if (mParent != null && !mParent.isLayoutRequested()) {
                            mParent.requestLayout(); // mParent(LinearLayout -> FrameLayout -> ViewRootImpl)
                        }
                    ViewRootImpl#requestLayout
                        checkThread();
                    ViewRootImpl#checkThread
                        if (mThread != Thread.currentThread()) {
                            throw new CalledFromWrongThreadException(
                                    "Only the original thread that created a view hierarchy can touch its views.");
                        }

                    ViewRootImpl对象是在onResume方法回调之后才创建，
                    那么就说明了为什么在生命周期的onCreate方法里，甚至是onResume方法里都可以实现子线程更新UI，
                    因为此时还没有创建ViewRootImpl对象，并不会进行是否为主线程的判断；

                    子线程可以在ViewRootImpl还没有创建之前更新UI的

                    访问UI是没有加对象锁的，在子线程环境下更新UI，会造成同步问题


                 */
                textView.setText("Non-UIThread");

                /*
                    java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
                    Toast里面也调用了setText
                    在子线程中更新UI是可以的，前提是
                 */
//                Toast.makeText(MainActivity.this, "Non-UIThread", Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("fuck", "MainActivity#onDestroy");
    }

}