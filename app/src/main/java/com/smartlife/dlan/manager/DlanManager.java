package com.smartlife.dlan.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.smartlife.dlan.service.DlanService;
import com.smartlife.dlan.service.IDlanCallback;
import com.smartlife.dlan.service.IDlanManager;

import java.util.List;

/**
 * Created by Administrator on 2017/6/20.
 */

public class DlanManager {
    private String TAG = "SmartLife/DlanManager";
    private static DlanManager mDlanStubManager = null;
    private IDlanManager mDlanCommand = null;
    private IDlanCallback callback;

    private DlanManager(Context mContext) {
        this.callback = new DlanCallbackStub();
        //mNettyHandler = NettyHandler.getNettyHandler();
        bindService(mContext, conn);
    }

    public static DlanManager getInstance(Context mContext) {
        if (mDlanStubManager == null) {
            synchronized (DlanManager.class) {
                if (mDlanStubManager == null) {
                    mDlanStubManager = new DlanManager(mContext);
                }
            }

        }
        return mDlanStubManager;
    }

    void bindService(Context mContext, ServiceConnection conn) {
        Intent intent = new Intent(mContext, DlanService.class);
        mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (mDlanCommand == null) {
                mDlanCommand = IDlanManager.Stub.asInterface(service);
                if (callback != null) {
                    try {
                        mDlanCommand.registerINettyCallback(callback);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            mDlanCommand = null;
        }
    };

    public void startSearchDevice() {
        try {
            if (mDlanCommand != null)
                mDlanCommand.startSearchDevice();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stopSearchDevice() {
        try {
            if (mDlanCommand != null)
                mDlanCommand.stopSearchDevice();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void play(String uri) {
        try {
            if (mDlanCommand != null)
                mDlanCommand.play(uri);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        try {
            if (mDlanCommand != null)
                mDlanCommand.pause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            if (mDlanCommand != null)
                mDlanCommand.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void goOn(String pausePosition) {
        try {
            if (mDlanCommand != null)
                mDlanCommand.goOn(pausePosition);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    class DlanCallbackStub extends IDlanCallback.Stub {
        @Override
        public void onShowList(List<String> mList) throws RemoteException {

        }
    }
}
