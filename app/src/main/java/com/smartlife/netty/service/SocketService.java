package com.smartlife.netty.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.smartlife.netty.helper.Constants;
import com.smartlife.netty.helper.Decoder;
import com.smartlife.netty.helper.SocketConnect;
import com.smartlife.netty.helper.SocketConnect.*;
import com.smartlife.netty.utils.NetUtil;
import com.smartlife.utils.ThreadPool;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

public class SocketService extends Service {

	private String TAG = "SmartLife/SocketSer";
	Timer time = new Timer();
	private SocketListener listener;
	private ChannelHandlerContext ctx;
	private MessageEvent mevent;
	private INettyCallback mINettyCallback;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		return mStub;
	}

	private final INettyManager.Stub mStub = new INettyManager.Stub() {

		@Override
		public void registerINettyCallback(INettyCallback callback) throws RemoteException {
			mINettyCallback = callback;
		}

		@Override
		public void unregisterINettyCallback(INettyCallback callback) throws RemoteException {
			mINettyCallback = null;
		}

		@Override
		public void startSocket() throws RemoteException {

			listener = new SocketListener() {

				@Override
				public void writeData(final ChannelHandlerContext ctx,
									  final MessageEvent e) {
					Log.d(TAG, "WriteData");
					keepChannel(ctx,e);
					NetUtil.SocketStart(getSharedPreferences("userinfo", MODE_PRIVATE).getString("phoneid", null),
							getSharedPreferences("userinfo", MODE_PRIVATE).getString("robotid", null),
							getSharedPreferences("Receipt", MODE_PRIVATE).getString("session", null),
							e,ctx);
				}

				@Override
				public void receiveSuccess(final ChannelHandlerContext ctx,
										   final MessageEvent e) {
					Log.d(TAG,"receiveSuccess");
					time.cancel();
					int ret = 0;
					Object o = e.getMessage();
					String callback = "";
					JSONObject Result = null;
					Intent in = new Intent("online");
					if (o instanceof JSONObject) {
						Log.d(TAG, e.getMessage().toString());
						try {
							Result = (JSONObject) o;
							callback = Result.getString("cmd");
							if (callback.equals("/robot/callback")) {
								JSONObject queryresult = new JSONObject(
										Result.getString("command"));
								if (queryresult.getString("cmd").equals(
										"photo_names")) {
									ArrayList<String> names = new ArrayList<String>();

									String querynames = queryresult
											.getString("names");
									JSONArray array = null;
									if (querynames.equals("null")) {
										return;
									} else {
										array = new JSONArray(querynames);
									}
									for (int i = 0; i < array.length(); i++) {
										names.add(array.getJSONObject(i)
												.getString("name"));
									}
									sendBroadcast(new Intent(
											Constants.Photo_Reply_Names)
											.putExtra("result", names));
								} else if (queryresult.getString("cmd").equals("")) {
									sendBroadcast(new Intent(
											Constants.Photo_Query).putExtra(
											"result", ""));
								} else {
									sendBroadcast(new Intent("result")
											.putExtra("result", queryresult
													.getString("data")));
								}
							}else if(callback.equals("phone_collectionlike")){
								Log.d(TAG,"robot_collectionlike");
								NetUtil.SocketCollectioned(Result.getString("phoneid"),Result.getString("robotid"),mevent, ctx);
								try {
									mINettyCallback.onCollectionLike(Result.getString("phoneid"));
								} catch (RemoteException e1) {
									e1.printStackTrace();
								}
							}else if(callback.equals("robot_collectioned")){
								Log.d(TAG,"robot_collectioned time="+ System.currentTimeMillis());
							} else if(callback.equals("robot_disconnect")){
								Log.d(TAG,"robot_disconnect");
							} else if (callback.equals("phone_uncontroll")) {
							} else if (callback.equals("phone_flush")) {
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						/*try {
							ret = Result.getInt("ret");
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						switch (ret) {
							case 0:
								flag = 1;
								time = new Timer();
								time.schedule(new TimerTask() {
									@Override
									public void run() {
										NetUtil.Scoket(new JSONObject(), 3, e, ctx);
									}
								}, new Date(), 9000);
								in.putExtra("ret", 0);
								Constants.flag = true;
								break;
							case -1:
								in.putExtra("ret", -1);
								the = true;
								Channels.close(ctx, e.getFuture());
								break;
							case 1:
								in.putExtra("ret", 1);
								the = true;
								Channels.close(ctx, e.getFuture());
								break;
							case 2:
								in.putExtra("ret", 2);
								Channels.close(ctx, e.getFuture());
								the = true;
								break;
							case 3:
								in.putExtra("ret", 3);
								Channels.close(ctx, e.getFuture());
								the = true;
								break;
							case 4:
								in.putExtra("ret", 4);
								Channels.close(ctx, e.getFuture());
								the = true;
								break;
							case 5:
								in.putExtra("ret", 5);
								Channels.close(ctx, e.getFuture());
								the = true;
								break;
							case 6:
								in.putExtra("ret", 6);
								Channels.close(ctx, e.getFuture());
								the = true;
								break;
							case 7:
								in.putExtra("ret", 6);
								Channels.close(ctx, e.getFuture());
								the = true;
								break;
							default:
								the = true;
								Channels.close(ctx, e.getFuture());
								break;

						}
						sendBroadcast(in);*/
					 } else if (o instanceof Decoder.Result2) {
						/*Log.i("Success", "收到图片");
						final Decoder.Result2 res = (Decoder.Result2) o;
						Result result = new Result();
						result.setDw(BitmapFactory.decodeByteArray(res.datas,
								0, res.datas.length));
						try {
							name = new JSONObject(res.json.getString("command"))
									.getString("name");

							result.setName(name);
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						size = "small";
						ThreadPool.execute(new Runnable() {

							@Override
							public void run() {
								String path = getApplication().getExternalFilesDir(null).getAbsolutePath()
										+ "/" + getSharedPreferences("Receipt", MODE_PRIVATE)
										.getString("username",null) + size;
								File file = new File(path);
								if (!file.exists()) {
									file.mkdir();
								}

								FileUtil.writefile(path, res.datas, name);
								Intent reply = new Intent(Constants.Photo_Reply);
								sendBroadcast(reply);
							}
						});*/

					}

				}

				@Override
				public void connectSuccess(final ChannelHandlerContext ctx,
										   final ChannelStateEvent e) {
					NetUtil.Scoket(new JSONObject(), ctx);
				}

				@Override
				public void connectFail() {
					Log.i("Connect", "connectFail");
					connectsocket();
				}

				@Override
				public void connectClose(ChannelHandlerContext ctx,
										 ChannelStateEvent e) {

				}
			};
			if (ctx != null && mevent != null) {
				Channels.close(ctx, mevent.getFuture());
			}
			connectsocket();
		}

		@Override
		public void turnLeft() throws RemoteException {
			if (mevent != null) {
				Log.d(TAG,"turnLeft");
				NetUtil.SocketMove("turn_left",mevent, ctx);
			}
		}

		@Override
		public void turnRight() throws RemoteException {
			if (mevent != null) {
				Log.d(TAG,"turnRight");
				NetUtil.SocketMove("turn_right",mevent, ctx);
			}
		}

		@Override
		public void turnUp() throws RemoteException {
			if(mevent != null)
				Log.d(TAG,"turnUp");
				NetUtil.SocketMove("turn_up",mevent,ctx);
		}

		@Override
		public void turnDown() throws RemoteException {
			if(mevent != null) {
				Log.d(TAG, "turnDown");
				NetUtil.SocketMove("turn_down", mevent, ctx);
			}
		}

		@Override
		public void stopSocket() throws RemoteException {
			if(mevent != null){
				Log.d(TAG,"stopSocket");
				NetUtil.SocketStop(getSharedPreferences("userinfo", MODE_PRIVATE).getString("id", null),
						getSharedPreferences("userinfo", MODE_PRIVATE).getString("session", null),
						getSharedPreferences("Receipt", MODE_PRIVATE).getString("robotid", null),
						mevent,ctx);
				Channels.close(ctx, mevent.getFuture());
				stopSelf();
			}
		}

		@Override
		public void collectionLike() throws RemoteException {
			Log.d(TAG,"collectionLike time ="+ System.currentTimeMillis());
			if(mevent != null){
				NetUtil.SocketCollectionLike(getSharedPreferences("userinfo", MODE_PRIVATE).getString("phoneid", null),
						getSharedPreferences("userinfo", MODE_PRIVATE).getString("robotid", null),mevent, ctx);
			}
		}
	};

	void keepChannel(final ChannelHandlerContext ctx,
					 final MessageEvent e){
		this.ctx = ctx;
		this.mevent =e;
	}

	static int flag = 0;

	private void connectsocket() {
		Log.d(TAG,"connectsocket");
		ThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				SocketConnect.InitSocket(listener, Constants.ip,
						Integer.parseInt(Constants.port));
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
