package com.smartlife.netty.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SocketErrorReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent Intent) {
		/*if (Intent.getAction().equals("socket_error")) {
			context.sendBroadcast(new Intent(Constants.Stop));
			try {
				EMClient.getInstance().callManager().endCall();
			} catch (EMNoActiveCallException e) {
				e.printStackTrace();
			}

			context.stopService(new Intent(context, SocketService.class));
			context.startActivity(new Intent(context, ConnectActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(
							Intent.FLAG_ACTIVITY_CLEAR_TASK));
		}*/

	}

}
