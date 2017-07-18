package com.smartlife.netty.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.smartlife.netty.listener.NettyManagerListener;
import com.smartlife.netty.service.INettyCallback;
import com.smartlife.netty.service.INettyManager;
import com.smartlife.netty.utils.NettyHandler;
import com.smartlife.netty.utils.NettyHandlerUtils;
import com.smartlife.utils.Constants;

/**
 * Created by Administrator on 2017/6/17.
 */

public class NettyManager {

    private String TAG = "SmartLife/NettyManager";
    private static NettyManager mNettyStubManager = null;
    private INettyManager mNettyCommand = null;
    private INettyCallback callback;
    private NettyHandler mNettyHandler;

    private NettyManager(Context mContext){
        this.callback = new NettyCallbackStub();
        mNettyHandler = NettyHandler.getNettyHandler();
        bindService(mContext,conn);
    }

    public static NettyManager getInstance(Context mContext){
        if (mNettyStubManager == null) {
            synchronized (NettyManager.class) {
                if (mNettyStubManager == null) {
                    mNettyStubManager = new NettyManager(mContext);
                }
            }

        }
        return mNettyStubManager;
    }

    void bindService(Context mContext,ServiceConnection conn){
        Intent intent = new Intent(Constants.NETTY_SERVICE);
        mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (mNettyCommand == null) {
                mNettyCommand = INettyManager.Stub.asInterface(service);
                if(callback != null){
                    try {
                        mNettyCommand.registerINettyCallback(callback);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            mNettyCommand = null;
        }
    };

    public void startSocket(){
        try {
            if(mNettyCommand != null)
                mNettyCommand.startSocket();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void turnLeft(){
        try {
            if(mNettyCommand != null)
                mNettyCommand.turnLeft();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void turnRight(){
        try {
            if(mNettyCommand != null)
                mNettyCommand.turnRight();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void turnUp(){
        try {
            if(mNettyCommand != null)
                mNettyCommand.turnUp();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void turnDown(){
        try {
            if(mNettyCommand != null)
                mNettyCommand.turnDown();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stopSocket(){
        try {
            if(mNettyCommand != null)
                mNettyCommand.stopSocket();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void collectionLike(){
        try {
            if(mNettyCommand != null)
                mNettyCommand.collectionLike();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    class NettyCallbackStub extends INettyCallback.Stub{

        @Override
        public void onCollectionLike(String phoneid) throws RemoteException {
            Log.d(TAG,"onCollectionLike phoneid="+phoneid);
            NettyHandlerUtils.sendMessage(mNettyHandler, Constants.a,0,null);
        }
    }

    public void registerNettyManagerListener(NettyManagerListener l) {
        mNettyHandler.setNettyManagerListener(l);
    }

    public void unRegisterNettyManagerListener() {

    }
}
