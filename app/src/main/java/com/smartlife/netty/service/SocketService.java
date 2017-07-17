package com.smartlife.netty.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.smartlife.netty.utils.NetUtil;

import java.util.Timer;

public class SocketService extends Service {

	private String TAG = "SmartLife/SocketSer";
	Timer time = new Timer();


	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		return mStub;
	}

	private final INettyManager.Stub mStub = new INettyManager.Stub() {

		@Override
		public void registerINettyCallback(INettyCallback callback) throws RemoteException {

		}

		@Override
		public void unregisterINettyCallback(INettyCallback callback) throws RemoteException {

		}

		@Override
		public void startSocket() throws RemoteException {
			Log.d(TAG,"startSocket");
			NetUtil.socketConnect();
		}

		@Override
		public void turnLeft() throws RemoteException {
			Log.d(TAG,"turnLeft");
			NetUtil.sendCommand("turnLeft");
		}

		@Override
		public void turnRight() throws RemoteException {
			NetUtil.sendCommand("turnRight");
		}

		@Override
		public void turnUp() throws RemoteException {
			NetUtil.sendCommand("turnUp");
		}

		@Override
		public void turnDown() throws RemoteException {
			NetUtil.sendCommand("turnDown");
		}

		@Override
		public void stopSocket() throws RemoteException {
			NetUtil.sendCommand("stopSocket");
		}

		@Override
		public void collectionLike() throws RemoteException {
			NetUtil.sendCommand("collectionLike");
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
