package com.smartlife.dlan.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.smartlife.dlan.presenter.MultiPointController;
import com.smartlife.dlan.util.DLNAUtil;
import com.smartlife.utils.Constants;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.DeviceChangeListener;

import java.util.ArrayList;
import java.util.List;

public class DlanService extends Service {
    private static final String TAG = "SmartLife/DlanService";
    private ControlPoint mControlPoint;
    private WifiStateReceiver mWifiStateReceiver;
    private List<Device> mDevices;
    private List<String> mList;
    private Device mSelectDevice;
    private int mSearchTimes=0;
    private static final int mNormalInternalTime = 3600000;
    private static final int mFastInternalTime = 15000;
    private String pausePosition = null;
    private boolean mStartComplete=false;
    private DlanTask mStartSearchTask=null;
    private DlanTask mPauseTask = null;
    private DlanTask mStopTask = null;
    private DlanTask mGoOnTask = null;
    private String mPlayUri = null;
    private IDlanManager mIDlanManager;
    private IDlanCallback mIDlanCallback;

    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDevices = new ArrayList<>();
        mList = new ArrayList<>();
        mControlPoint = new ControlPoint();
        mControlPoint.addDeviceChangeListener(mDeviceChangeListener);
        registerWifiStateReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unInit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void unInit() {
        unregisterWifiStateReceiver();
    }

    private void registerWifiStateReceiver() {
        if (mWifiStateReceiver == null) {
            mWifiStateReceiver = new WifiStateReceiver();
            registerReceiver(mWifiStateReceiver, new IntentFilter(
                    ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void unregisterWifiStateReceiver() {
        if (mWifiStateReceiver != null) {
            unregisterReceiver(mWifiStateReceiver);
            mWifiStateReceiver = null;
        }
    }

    private class WifiStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            Bundle bundle = intent.getExtras();
            int statusInt = bundle.getInt("wifi_state");
            switch (statusInt) {
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.e(TAG, "wifi enable");
                    if (mStartSearchTask == null || mStartSearchTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                        mStartSearchTask = new DlanTask();
                        mStartSearchTask.execute(0, 0, 1);
                    }
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Log.e(TAG, "wifi disabled");
                    break;
                default:
                    break;
            }
        }
    }

    private DeviceChangeListener mDeviceChangeListener = new DeviceChangeListener() {

        @Override
        public void deviceRemoved(Device dev) {
            Log.d(TAG, "control point remove a device");
            if (!DLNAUtil.isMediaRenderDevice(dev)) {
                return;
            }
            int size = mDevices.size();
            for (int i = 0; i < size; i++) {
                String udnString = mDevices.get(i).getUDN();
                if (dev.getUDN().equalsIgnoreCase(udnString)) {
                    Device device = mDevices.remove(i);
                    String devicename =mList.remove(i);
                }
            }

            try {
                mIDlanCallback.onShowList(mList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deviceAdded(Device dev) {
            ArrayList<String> mList = new ArrayList<>();
            if (!DLNAUtil.isMediaRenderDevice(dev))
                return;
            int size = mDevices.size();
            for (int i = 0; i < size; i++) {
                String udnString = mDevices.get(i).getUDN();
                if (dev.getUDN().equalsIgnoreCase(udnString)) {
                    return;
                }
            }

            mDevices.add(dev);
            mList.add(dev.getFriendlyName());
            Log.d(TAG, "control point add a device..." + dev.getDeviceType() + dev.getFriendlyName());
            Intent intent = new Intent(Constants.DLAN_DEVICE);
            intent.putExtra("result",mList);
            sendBroadcast(intent);
            try {
                mIDlanCallback.onShowList(mList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    };

    private final IDlanManager.Stub mStub = new IDlanManager.Stub() {

        @Override
        public void registerINettyCallback(IDlanCallback callback) throws RemoteException {
            mIDlanCallback = callback;
        }

        @Override
        public void unregisterINettyCallback(IDlanCallback callback) throws RemoteException {
            mIDlanCallback = null;
        }

        @Override
        public void startSearchDevice() throws RemoteException {
            Log.d(TAG,"startSearchDevice");
            if (mStartSearchTask == null || mStartSearchTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                mStartSearchTask = new DlanTask();
                mStartSearchTask.execute(0, 0, 1);
            }
        }

        @Override
        public void stopSearchDevice() throws RemoteException {

            mControlPoint.stop();
        }

        @Override
        public boolean play(String uri) throws RemoteException {
            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
            String name = settings.getString("DlanSelectDevice","");
            for(Device mdevice:mDevices){
                if(mdevice.getFriendlyName().equals(name)){
                    mSelectDevice = mdevice;
                }
            }

            mPlayUri = uri;
            Log.d(TAG,"playuri="+mPlayUri);

            Log.d(TAG,"mDevices play ="+mSelectDevice.getFriendlyName());
            new Thread() {
                public void run() {
                    boolean isSuccess = MultiPointController.getInstance().play(mSelectDevice, mPlayUri);
                    if (isSuccess) {
                        Log.d(TAG, "play success");
                    } else {
                        Log.d(TAG, "play failed..");
                    }

                };
            }.start();
            return true;
        }

        @Override
        public boolean pause() throws RemoteException {

            if (mPauseTask == null || mPauseTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                mPauseTask = new DlanTask();
                mPauseTask.execute(0, 0, 3);
            }
            return true;
        }

        @Override
        public boolean stop() throws RemoteException {
            if (mStopTask == null || mStopTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                mStopTask = new DlanTask();
                mStopTask.execute(0, 0, 4);
            }
            return true;
        }

        @Override
        public boolean goOn(final String pausePosition) throws RemoteException {

            if (mGoOnTask == null || mGoOnTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                mGoOnTask = new DlanTask();
                mGoOnTask.execute(0, 0, 5);
            }
            return true;
        }

        @Override
        public String getTransportState() throws RemoteException {
            return MultiPointController.getInstance().getTransportState(mSelectDevice);
        }

        @Override
        public String getVolumeDbRange(String argument) throws RemoteException {
            return MultiPointController.getInstance().getVolumeDbRange(mSelectDevice,argument);
        }

        @Override
        public int getMinVolumeValue() throws RemoteException {
            return MultiPointController.getInstance().getMinVolumeValue(mSelectDevice);
        }

        @Override
        public int getMaxVolumeValue() throws RemoteException {
            return MultiPointController.getInstance().getMaxVolumeValue(mSelectDevice);
        }

        @Override
        public boolean seek(String targetPosition) throws RemoteException {
            return MultiPointController.getInstance().seek(mSelectDevice,targetPosition);
        }

        @Override
        public String getPositionInfo() throws RemoteException {
            return MultiPointController.getInstance().getPositionInfo(mSelectDevice);
        }

        @Override
        public String getMediaDuration() throws RemoteException {
            return MultiPointController.getInstance().getMediaDuration(mSelectDevice);
        }

        @Override
        public boolean setMute(String targetValue) throws RemoteException {
            return MultiPointController.getInstance().setMute(mSelectDevice,targetValue);
        }

        @Override
        public String getMute() throws RemoteException {
            return MultiPointController.getInstance().getMute(mSelectDevice);
        }

        @Override
        public boolean setVoice(int value) throws RemoteException {
            return false;
        }

        @Override
        public int getVoice() throws RemoteException {
            return MultiPointController.getInstance().getVoice(mSelectDevice);
        }
    };

    class DlanTask extends AsyncTask<Integer, Integer, Integer>{

        @Override
        protected Integer doInBackground(Integer... integers) {
            switch (integers[2]) {
                case 1:
                    try {
                        if (mStartComplete) {
                            mControlPoint.search();
                        } else {
                            mControlPoint.stop();
                            if (mControlPoint.start()) {
                                mStartComplete = true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    MultiPointController.getInstance().play(mDevices.get(0),"http://video19.ifeng.com/video07/2013/11/11/281708-102-007-1138.mp4");
                    break;
                case 3:
                    if (MultiPointController.getInstance().pause(mSelectDevice)) {
                        Log.d(TAG, "pause success");
                    } else {
                        Log.d(TAG, "pause failed..");
                    }
                    break;
                case 4:
                    if (MultiPointController.getInstance().stop(mSelectDevice)) {
                        Log.d(TAG, "stop success");
                    } else {
                        Log.d(TAG, "stop failed..");
                    }
                    break;
                case 5:
                    if (MultiPointController.getInstance().goon(mSelectDevice,pausePosition)) {
                        Log.d(TAG, "goOn success");
                    } else {
                        Log.d(TAG, "goOn failed..");
                    }
                    break;

            }
            return null;
        }
    }

}
