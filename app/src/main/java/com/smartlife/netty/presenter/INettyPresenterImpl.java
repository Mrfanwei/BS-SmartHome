package com.smartlife.netty.presenter;

import android.content.Context;

import com.smartlife.netty.listener.NettyManagerListener;
import com.smartlife.netty.listener.NettyManagerListenerImpl;
import com.smartlife.netty.manager.NettyManager;
import com.smartlife.netty.view.INettyView;

/**
 * Created by Administrator on 2017/6/17.
 */

public class INettyPresenterImpl implements INettyPresenter {

    private static String TAG = "SmartLife/INettyPre";
    private Context mContext;
    private NettyManager mNettyManager;
    private NettyManagerListener mNettyManagerListener;

    public INettyPresenterImpl(Context context, INettyView view){
        this.mContext = context;
        mNettyManager = NettyManager.getInstance(mContext.getApplicationContext());
        mNettyManagerListener = new NettyManagerListenerImpl();
        mNettyManagerListener.setINettyView(view);
        mNettyManager.registerNettyManagerListener(mNettyManagerListener);
    }

    @Override
    public void startSocket() {
        if(mNettyManager != null){
            mNettyManager.startSocket();
        }
    }

    @Override
    public void turnLeft() {
        if(mNettyManager != null){
            mNettyManager.turnLeft();
        }
    }

    @Override
    public void turnRight() {
        if(mNettyManager != null){
            mNettyManager.turnRight();
        }
    }

    @Override
    public void turnUp() {
        if(mNettyManager != null){
            mNettyManager.turnUp();
        }
    }

    @Override
    public void turnDown() {
        if(mNettyManager != null){
            mNettyManager.turnDown();
        }
    }

    @Override
    public void stopSocket() {
        if(mNettyManager != null){
            mNettyManager.stopSocket();
        }
    }

    @Override
    public void collectionLike() {
        if(mNettyManager != null){
            mNettyManager.collectionLike();
        }
    }
}
