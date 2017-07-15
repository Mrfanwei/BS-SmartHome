package com.smartlife.huanxin.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

public class StartUtil {

	static Random random=new Random();
	final static String TAG = "Robotfw/StartUtil";
	// 无数据页面跳转，如果传入‘finish’销毁前一个页面，否则不销毁
	public static void startintent(Activity activity, Class toclass, String flag) {
		Log.d(TAG, "startintent");
		Intent intent = new Intent(activity, toclass);
		activity.startActivity(intent);
		int num= random.nextInt(2);
		Log.d(TAG, "startintent1");
//		switch (num) {
//		case 0:
//			activity.overridePendingTransition(R.anim.dialogenter,R.anim.dialogexit);
//			break;
//		case 1:
//			activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
//			break;
//		case 2:
//			activity.overridePendingTransition(android.R.anim.slide_in_left,R.anim.slide_out_to_right);
//			break;
//		default:
//			break;
//		}
		Log.d(TAG, "startintent2");

		if (flag.equals("finish")) {
			activity.finish();
		}
	}

	// 有数据页面跳转，同上
	public static void startintent(Activity activity, Class toclass,
                                   String flag, Bundle params) {
		Intent intent = new Intent(activity, toclass);
		intent.putExtras(params);
		activity.startActivity(intent);
		//activity.overridePendingTransition(R.anim.dialogenter,R.anim.dialogexit);
		if (flag.equals("finish")) {
			activity.finish();
		}
	}

	public static void startintentforresult(Activity activity, Class toclass,
                                            Bundle params, int code) {
		Intent intent = new Intent(activity, toclass);
		intent.putExtras(params);		
		activity.startActivityForResult(intent, code);
		//activity.overridePendingTransition(R.anim.dialogenter,R.anim.dialogexit);
	}

	public static void startintentforresult(Activity activity, Class toclass,
                                            int code) {
		Intent intent = new Intent(activity, toclass);
		activity.startActivityForResult(intent, code);
		//activity.overridePendingTransition(R.anim.dialogenter,R.anim.dialogexit);
	}

}
