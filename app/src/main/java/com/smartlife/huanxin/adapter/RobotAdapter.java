package com.smartlife.huanxin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartlife.R;
import com.smartlife.huanxin.model.RobotModel;

import java.util.List;

public class RobotAdapter extends BaseAdapter {

	private String TAG = "SmartLife/RobotAdapter";

	private Context context;
	private static List<RobotModel.DataBean> robots;

	public RobotAdapter(Context context, List<RobotModel.DataBean> robots) {
		super();
		this.context = context;
		synchronized (robots) {
			this.robots = robots;
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return robots.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	LayoutInflater layout = null;
	RobotHolder robotholder = null;

	@Override
	public View getView(int index, View v, ViewGroup arg2) {
		if (layout == null) {
			layout = LayoutInflater.from(context);
		}
		if (v == null) {
			v = layout.inflate(R.layout.robot_item, null);
			robotholder = new RobotHolder();
			robotholder.robotname = (TextView) v.findViewById(R.id.robot_item);
			robotholder.rid = (TextView) v.findViewById(R.id.rid);
			robotholder.online = (TextView) v.findViewById(R.id.online);
			robotholder.control = (TextView) v.findViewById(R.id.robot_control);
			if (robots.size() != 0) {
				robotholder.robotname
						.setText(robots.get(index).getRname() + "");
				robotholder.rid.setText(robots.get(index).getId() + "");
				if (robots.get(index).getController() == 0) {
					robotholder.control.setText("未被控制");
					//robotholder.control.setTextColor(Color.back_perss);
					robotholder.control.setTextColor(Color.GREEN);
				} else {
					robotholder.control.setText("已被控制");
				}
				if (robots.get(index).isOnline()) {
					robotholder.online.setTextColor(Color.GREEN);
					robotholder.online.setText("在线");
				} else {
					robotholder.online.setTextColor(Color.RED);
					robotholder.online.setText("离线");
				}
//				if (robots.get(index).getAir().equals(RobotModel.air.bind)) {
//
//				} else {
//
//				}
				Log.d(TAG,"");
				v.setTag(robotholder);
			}
		} else if (v != null) {
			Log.d(TAG,"getView3");
			robotholder = (RobotHolder) v.getTag();
		}
		Log.d(TAG,"");
		return v;
	}

	class RobotHolder {
		private TextView robotname;
		private TextView rid;
		private TextView online;
		private TextView control;
	}

}
