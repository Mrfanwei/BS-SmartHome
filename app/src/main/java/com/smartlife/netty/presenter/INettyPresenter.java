package com.smartlife.netty.presenter;

/**
 * Created by Administrator on 2017/6/17.
 */

public interface INettyPresenter {
    void startSocket();
    void turnLeft();
    void turnRight();
    void turnUp();
    void turnDown();
    void stopSocket();
    void collectionLike();
}
