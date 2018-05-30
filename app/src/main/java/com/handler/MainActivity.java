package com.handler;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView mTextView;
    private HandlerTest1 mHandlerTest1;
    private HandlerTest2 mHandlerTest2;
    private int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mTextView = (TextView) findViewById(R.id.TextView);

        //1 子线程发送消息给本身
        new Thread() {
            public void run() {
                Looper.prepare();
                mHandlerTest1=new HandlerTest1(Looper.myLooper());
                Message message = new Message();
                message.obj = "子线程发送的消息Hi~Hi";
                mHandlerTest1.sendMessage(message);
                Looper.loop();
            };
        }.start();

    }

    private class HandlerTest1 extends Handler {

        private HandlerTest1(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            System.out.println("子线程收到:" + msg.obj);

            //2  收到消息后可再发消息到主线程
            mHandlerTest2=new HandlerTest2(getMainLooper());
            Message message = new Message();
            message.obj = "O(∩_∩)O";
            mHandlerTest2.sendMessage(message);
        }
    }

    private class HandlerTest2 extends Handler {

        private HandlerTest2(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTextView.setText("在主线程中,收到子线程发来消息:" + msg.obj);

            //3  收到消息后再发消息到子线程
            if (counter==0) {
                Message message = new Message();
                message.obj = "主线程发送的消息Xi~Xi";
                mHandlerTest1.sendMessage(message);
                counter++;
            }

        }
    }

}