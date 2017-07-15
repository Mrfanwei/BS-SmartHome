package com.smartlife.netty.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2017/6/17.
 */

public class NettyHandlerUtils {
    public static void sendMessage(Handler handler, int what, int index, Object o) {
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.arg1 = index;
        if (o != null && !o.equals(""))
            msg.obj = o;
        handler.sendMessage(msg);
    }
}
