// INettyManager.aidl
package com.smartlife.netty.service;

import com.smartlife.netty.service.INettyCallback;

// Declare any non-default types here with import statements

interface INettyManager {
    void registerINettyCallback(INettyCallback callback);
    void unregisterINettyCallback(INettyCallback callback);
    void startSocket();
    void turnLeft();
    void turnRight();
    void turnUp();
    void turnDown();
    void stopSocket();
    void collectionLike();
}
