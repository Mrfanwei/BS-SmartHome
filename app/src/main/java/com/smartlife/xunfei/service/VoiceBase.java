package com.smartlife.xunfei.service;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2016/11/6.
 */

public class VoiceBase {
    Handler handler = new Handler() {
        public void dispatchMessage(Message msg) {
            onHandlerMessage(msg);
        };
    };

    public  void onHandlerMessage(Message msg){

    }
}
