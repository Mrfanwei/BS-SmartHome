package com.smartlife.xunfei.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import com.smartlife.R;
import com.smartlife.MainActivity;
import com.smartlife.dlan.manager.DlanManager;
import com.smartlife.dlan.service.DlanService;
import com.smartlife.dlan.service.IDlanManager;
import com.smartlife.xunfei.service.VoiceWake;

import org.cybergarage.upnp.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wm on 2016/3/22.
 */
public class SpeechFragment extends DialogFragment implements View.OnClickListener {

    private String TAG = "SmartLife/DlanFragment";
    public MainActivity mActivity;
    public Activity mContext;
    private View mContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (MainActivity)getActivity();
        mContent = inflater.inflate(R.layout.fragment_speech, container, false);
        mContent.findViewById(R.id.btn_wakeup).setOnClickListener(this);
        mContent.findViewById(R.id.btn_stop).setOnClickListener(this);

        return mContent;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG,"onClick");
        switch (v.getId()) {
            case R.id.btn_wakeup:
                Log.d(TAG,"btn_wakeup");
                VoiceWake.getInstance(getActivity().getApplicationContext()).voiceStart();
                break;
            case R.id.btn_stop:
                VoiceWake.getInstance(getActivity().getApplicationContext()).stopListening();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VoiceWake.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        int dialogHeight = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.56);
        int dialogWidth = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.63);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }
}
