package com.smartlife.xunfei.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.smartlife.utils.HandlerUtil;
import com.smartlife.xunfei.set.TtsSettings;

/**
 * Created by Administrator on 2016/11/6.
 */

public class VoiceTts extends VoiceBase {

    private String TAG = "SmartLifee/VoiceTts";
    // 语音合成对象
    private SpeechSynthesizer mTts;
    private SharedPreferences mSharedPreferences;
    static private Context mcontext;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认云端发音人
    public static String voicerCloud="xiaoyan";
    // 默认本地发音人
    public static String voicerLocal="xiaoyan";
    //缓冲进度
    private int mPercentForBuffering = 0;
    //播放进度
    private int mPercentForPlaying = 0;

    private volatile static VoiceTts mVoiceTts;

    public static VoiceTts getInstance(Context context){
        if(mVoiceTts==null){
            synchronized (VoiceTts.class){
                if(mVoiceTts==null){
                    mcontext=context;
                    mVoiceTts=new VoiceTts();
                }
            }
        }
        return mVoiceTts;
    }

    public VoiceTts(){
        // 初始化唤醒对象
        Log.d(TAG,"VoiceTts");
        mTts = SpeechSynthesizer.createSynthesizer(mcontext, mTtsInitListener);
        mSharedPreferences = mcontext.getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        mEngineType = SpeechConstant.TYPE_CLOUD;
        setParam();
    }

    public void onHandlerMessage(Message msg){
        Log.d(TAG,"onHandlerMessage");
        if(msg.what==0){
            Log.d(TAG,"stopListening VoiceWake");
            TtsStop();
            VoiceWake.getInstance(mcontext).startListening();
        }else if(msg.what==1){
            Log.d(TAG,"stopListening");
            TtsStop();
        }
    }

    void TtsPlay(String text){
        Log.d(TAG,"TtsPlay"+text);
       // String text = "地球你好地球你好地球你好地球你好地球你好地球你好";
        // 设置参数
        //setParam();
        int code = mTts.startSpeaking(text, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            Toast.makeText(mcontext,"语音合成失败,错误码:"+code, Toast.LENGTH_SHORT);
        }else{
           // HandlerUtil.sendmsg(handler,"",0);
        }
    }

    void TtsStop(){
        Log.d(TAG,"TtsStop");
        mTts.stopSpeaking();
    }

    private void setParam(){
        Log.d(TAG,"setParam");
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD))
        {
            //设置使用云端引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME,voicerCloud);
        }else {
            //设置使用本地引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath());
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME,voicerLocal);
        }
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            Log.d(TAG,"开始播放");
        }

        @Override
        public void onSpeakPaused() {
            Log.d(TAG,"暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            Log.d(TAG,"继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            Log.d(TAG,"继续播放"+"mPercentForBuffering="+mPercentForBuffering+" mPercentForPlaying="+mPercentForPlaying);
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            Log.d(TAG,"mPercentForBuffering="+mPercentForBuffering+" mPercentForPlaying="+mPercentForPlaying);
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                Log.d(TAG,"播放完成");
            } else if (error != null) {
                Log.d(TAG,"播放error");
            }
            HandlerUtil.sendmsg(handler,"",0);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
            Log.d(TAG,"onEvent");
        }
    };
    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG,"初始化失败,错误码："+code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
                Log.d(TAG,"初始化成功："+code);
            }
        }
    };

    //获取发音人资源路径
    private String getResourcePath(){
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mcontext, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mcontext, ResourceUtil.RESOURCE_TYPE.assets, "tts/"+VoiceTts.voicerLocal+".jet"));
        return tempBuffer.toString();
    }
}
