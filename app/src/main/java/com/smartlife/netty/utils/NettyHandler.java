package com.smartlife.netty.utils;

import android.os.Handler;
import android.os.Message;

import com.smartlife.netty.listener.NettyManagerListener;
import com.smartlife.utils.Constants;

/**
 * Created by Administrator on 2017/6/17.
 */

public class NettyHandler extends Handler {

    private NettyManagerListener mNettyManagerListener;
    private static NettyHandler mNettyHandler;

    private NettyHandler(){

    }

    public void setNettyManagerListener(NettyManagerListener mNettyManagerListener){
        this.mNettyManagerListener = mNettyManagerListener;
    }

    public static NettyHandler getNettyHandler(){
        if (mNettyHandler == null) {
            synchronized (NettyHandler.class) {
                if (mNettyHandler == null) {
                    mNettyHandler = new NettyHandler();
                }
            }
        }
        return mNettyHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case Constants.a:
                mNettyManagerListener.onCollectionLike();
                break;
        }
    }
}
