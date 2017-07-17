package com.smartlife.netty.listener;

import com.smartlife.netty.view.INettyView;

/**
 * Created by Administrator on 2017/6/17.
 */

public class NettyManagerListenerImpl implements NettyManagerListener {
    private INettyView mINettyView;

    @Override
    public INettyView getINettyView(){
        return mINettyView;
    }

    @Override
    public void setINettyView(INettyView mINettyView){
        this.mINettyView = mINettyView;
    }

    @Override
    public void onCollectionLike() {
        mINettyView.onCollectionLike();
    }
}
