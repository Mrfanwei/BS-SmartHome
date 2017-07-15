package com.smartlife.xunfei.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;
import com.smartlife.utils.HandlerUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2016/11/1.
 */

public class VoiceWake extends VoiceBase {

    private String TAG = "SmartLifee/VoiceWake";
    // 语音唤醒对象
    private VoiceWakeuper mIvw;
    // 唤醒结果内容
    private String resultString;
    private int curThresh = 10;
    private String keep_alive = "1";
    private String ivwNetMode = "0";
    static private Context mcontext;
    private VoiceNlu mNlu;
    private volatile static VoiceWake mVoiceWake;

    public static VoiceWake getInstance(Context context){
        if(mVoiceWake==null){
            synchronized (VoiceWake.class){
                if(mVoiceWake==null){
                    mcontext=context;
                    mVoiceWake=new VoiceWake();
                }
            }
        }
        return mVoiceWake;
    }

    public VoiceWake(){
        // 初始化唤醒对象
        mIvw = VoiceWakeuper.createWakeuper(mcontext, null);
        //voiceStart();
    }

    public void voiceStart(){
        //非空判断，防止因空指针使程序崩溃


        mIvw = VoiceWakeuper.getWakeuper();
        if(mIvw != null) {
            Log.d(TAG,"voiceStart init");
            //setRadioEnable(false);
            resultString = "";

            // 清空参数
            mIvw.setParameter(SpeechConstant.PARAMS, null);
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:"+ curThresh);
            // 设置唤醒模式
            mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置持续进行唤醒
            mIvw.setParameter(SpeechConstant.KEEP_ALIVE, keep_alive);
            // 设置闭环优化网络模式
            mIvw.setParameter(SpeechConstant.IVW_NET_MODE, ivwNetMode);
            // 设置唤醒资源路径
            mIvw.setParameter(SpeechConstant.IVW_RES_PATH, getResource(mcontext));
            // 设置唤醒录音保存路径，保存最近一分钟的音频
            mIvw.setParameter( SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath()+"/msc/ivw.wav" );
            mIvw.setParameter( SpeechConstant.AUDIO_FORMAT, "wav" );
            // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
            //mIvw.setParameter( SpeechConstant.NOTIFY_RECORD_DATA, "1" );

            // 启动唤醒
            mIvw.startListening(mWakeuperListener);
        } else {
            Log.d(TAG,"唤醒未初始化");
        }
    }

    public void onHandlerMessage(Message msg){
        Log.d(TAG,"onHandlerMessage");
        if(msg.what==0){
            Log.d(TAG,"error");
        }else if(msg.what==1){
            Log.d(TAG,"stopListening");
            //stopListening();
            VoiceNlu.getInstance(mcontext).startUnderstander();
        }
    }
    private WakeuperListener mWakeuperListener = new WakeuperListener() {

        @Override
        public void onResult(WakeuperResult result) {
            Log.d(TAG, "onResult");
            if(!"1".equalsIgnoreCase(keep_alive)) {
               // setRadioEnable(true);
            }
            try {
                String text = result.getResultString();
                JSONObject object;
                object = new JSONObject(text);
                StringBuffer buffer = new StringBuffer();
                buffer.append("【RAW】 "+text);
                buffer.append("\n");
                buffer.append("【操作类型】"+ object.optString("sst"));
                buffer.append("\n");
                buffer.append("【唤醒词id】"+ object.optString("id"));
                buffer.append("\n");
                buffer.append("【得分】" + object.optString("score"));
                buffer.append("\n");
                buffer.append("【前端点】" + object. optString("bos"));
                buffer.append("\n");
                buffer.append("【尾端点】" + object.optString("eos"));
                resultString =buffer.toString();

            } catch (JSONException e) {
                resultString = "结果解析出错";
                e.printStackTrace();
            }
            //textView.setText(resultString);
            Log.d(TAG,"resultString="+resultString);
            HandlerUtil.sendmsg(handler,"",1);
        }

        @Override
        public void onError(SpeechError error) {
            //showTip(error.getPlainDescription(true));
            Log.d(TAG,error.getPlainDescription(true));
            //setRadioEnable(true);
            HandlerUtil.sendmsg(handler,"",0);
        }

        @Override
        public void onBeginOfSpeech() {
        }

        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {

        }

        @Override
        public void onVolumeChanged(int volume) {

        }
    };

    private String getResource(Context context) {
        return ResourceUtil.generateResourcePath(context,
                ResourceUtil.RESOURCE_TYPE.assets, "ivw/"+"594a09d9"+".jet");
    }

    public void startListening(){
        Log.d(TAG, "startListening");
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.startListening(mWakeuperListener);
        }
    }

    public void stopListening(){
        Log.d(TAG, "stopListening");
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.stopListening();
        }
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy VoiceWake");
        // 销毁合成对象
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.destroy();
        }
    }
}
