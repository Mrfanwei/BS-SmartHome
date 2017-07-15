package com.smartlife.netty.listener;

import com.smartlife.netty.view.INettyView;

/**
 * Created by Administrator on 2017/6/17.
 */

public interface NettyManagerListener {
    void onCollectionLike();
    INettyView getINettyView();
    void setINettyView(INettyView mINettyView);
}
