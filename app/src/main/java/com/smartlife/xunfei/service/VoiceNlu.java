package com.smartlife.xunfei.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.smartlife.utils.HandlerUtil;
import com.smartlife.xunfei.model.NluModel;
import com.smartlife.xunfei.model.NluObjModel;
import com.smartlife.xunfei.protocol.NluProtocolImpl;
import com.smartlife.xunfei.set.UnderstanderSettings;

/**
 * Created by Administrator on 2016/11/6.
 */

public class VoiceNlu extends VoiceBase {
    private String TAG = "SmartLifee/Nul";
    private SpeechUnderstander mSpeechUnderstander;
    private SharedPreferences mSharedPreferences;
    int ret = 0;// 函数调用返回值
    static Context mcontext;
    private volatile static VoiceNlu mVoiceNlu;
    private NluProtocolImpl mNluProtocolImpl;

    private String mdevicename;
    private String mdeviceattr;
    private String mdeviceaction;
    Gson mgson = new Gson();

    public static VoiceNlu getInstance(Context context) {
        if (mVoiceNlu == null) {
            synchronized (VoiceNlu.class) {
                if (mVoiceNlu == null) {
                    mcontext = context;
                    mVoiceNlu = new VoiceNlu();
                }
            }
        }
        return mVoiceNlu;
    }

    public VoiceNlu() {
        Log.d(TAG, "Nlu");
        /*****语义理解**/
        mSpeechUnderstander = SpeechUnderstander.createUnderstander(mcontext, speechUnderstanderListener);
        mSharedPreferences = mcontext.getSharedPreferences(UnderstanderSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        //setUnderstanderParam();
        setParam();
        mNluProtocolImpl = new NluProtocolImpl(mcontext);
    }

    public void onHandlerMessage(Message msg) {
        Log.d(TAG, "onHandlerMessage");
        if (msg.what == 0) {
            Log.d(TAG, "stopListening VoiceWake");
            stopUnderstander();
            VoiceWake.getInstance(mcontext).startListening();
        } else if (msg.what == 1) {
            Log.d(TAG, "stopListening");
            stopUnderstander();
            VoiceTts.getInstance(mcontext).TtsPlay(msg.getData().getString("result"));
        }
    }

    /**
     * 初始化监听器（语音到语义）。
     */
    private InitListener speechUnderstanderListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "speechUnderstanderListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG, "onInit Understand error");
            }
        }
    };

    public void startUnderstander() {
        Log.d(TAG, "startUnderstander");
        if (mSpeechUnderstander.isUnderstanding()) {// 开始前检查状态
            mSpeechUnderstander.stopUnderstanding();
            Log.d(TAG, "stop record");
        }
        ret = mSpeechUnderstander.startUnderstanding(speechUnderstandListener);
        if (ret != 0) {
            Log.d(TAG, "understander failed" + ret);
        } else {
            //showTip(getString(R.string.text_begin));
            Log.d(TAG, "text begin");
        }
    }

    public void stopUnderstander() {
        Log.d(TAG, "stopUnderstander");
        mSpeechUnderstander.stopUnderstanding();
    }

    public void setParam() {
        Log.d(TAG, "setParam");
        String lag = mSharedPreferences.getString("understander_language_preference", "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mSpeechUnderstander.setParameter(SpeechConstant.ACCENT, lag);
        }
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("understander_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("understander_vadeos_preference", "1000"));

        // 设置标点符号，默认：1（有标点）
        //mSpeechUnderstander.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("understander_punc_preference", "1"));
        mSpeechUnderstander.setParameter(SpeechConstant.RESULT_TYPE, "json");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mSpeechUnderstander.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/sud.wav");
    }

    /**
     * 识别回调。
     */
    private SpeechUnderstanderListener speechUnderstandListener = new SpeechUnderstanderListener() {

        @Override
        public void onResult(final UnderstanderResult result) {
            Log.d(TAG, "onResult");
            // 显示
            String text = result.getResultString();
            if (!TextUtils.isEmpty(text)) {
                Log.d(TAG, "text=" + text);
            }

            mNluProtocolImpl.dealNulData(text);
            /*if (text.contains("Object(digital)")) {
                dealNluObjModel(text);
            } else {
                dealNluModel(text);
            }*/
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, data.length + "");
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //showTip("结束说话");
            Log.d(TAG, "end speed");
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            //showTip("开始说话");
            Log.d(TAG, "begin speek");
        }

        @Override
        public void onError(SpeechError error) {
            //showTip(error.getPlainDescription(true));
            Log.d(TAG, "SpeechUnderstanderListener=" + error.getPlainDescription(true));
            HandlerUtil.sendmsg(handler, error.getPlainDescription(true), 1);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
            Log.d(TAG, "onEvent");

        }
    };

    void onDestroy() {
        mSpeechUnderstander.cancel();
        mSpeechUnderstander.destroy();
    }


    void dealNluObjModel(String text) {
        NluObjModel mnlumodel = mgson.fromJson(text, NluObjModel.class);
        Log.d(TAG, "dealNluObjModel");
        if (null != mnlumodel.getService()) {
            if (mnlumodel.getService().contains("smartHome") || mnlumodel.getService().contains("tvControl")) {
                NluObjModel.SemanticBean.SlotsBean mslot = mnlumodel.getSemantic().getSlots();
                mdevicename = mslot.getModifier();
                mdeviceattr = mslot.getAttr();
                NluObjModel.SemanticBean.SlotsBean.AttrValueBean mattrvalue = mslot.getAttrValue();
                mdeviceaction = mattrvalue.getDirect();
                Log.d(TAG, "mdevicename= " + mdevicename + " mdeviceattr=" + mdeviceattr + " mdeviceaction=" + mdeviceaction);
                if (mnlumodel.getService().equals("tv_smartHome")) {
                    //DeviceControlTv.getInstance().remoteControl(mslot.getModifier(), mslot.getAttr(), mattrvalue.getDirect());
                } else if (mnlumodel.getService().equals("freezer_smartHome")) {

                }
                HandlerUtil.sendmsg(handler, mnlumodel.getText(), 1);
            } else {
                NluObjModel.AnswerBean manswer = mnlumodel.getAnswer();
                HandlerUtil.sendmsg(handler, manswer.getText(), 1);
            }
        } else {
            HandlerUtil.sendmsg(handler, mnlumodel.getText(), 1);
        }
    }

    void dealNluModel(String text) {
        Log.d(TAG,"textfff"+text);
        /*NluModel mnlumodel = mgson.fromJson(text, NluModel.class);
        Log.d(TAG, "dealNluModel");
         if (null != mnlumodel.getService()) {
            if (mnlumodel.getService().contains("smartHome") || mnlumodel.getService().contains("tvControl")) {
                NluModel.SemanticBean.SlotsBean mslot = mnlumodel.getSemantic().getSlots();
                mdevicename = mslot.getModifier();
                mdeviceattr = mslot.getAttr();
                mdeviceaction = mslot.getAttrValue();
                Log.d(TAG, "mdevicename= " + mdevicename + " mdeviceattr=" + mdeviceattr + " mdeviceaction=" + mdeviceaction);
                if (mnlumodel.getService().equals("tv_smartHome")) {
                    //DeviceControlTv.getInstance().remoteControl(mdevicename, mdeviceattr, mdeviceaction);
                } else if (mnlumodel.getService().equals("freezer_smartHome")) {

                }
                HandlerUtil.sendmsg(handler, mnlumodel.getText(), 1);
            } else if (mnlumodel.getService().contains("music")) {
                NluModel.SemanticBean.SlotsBean mslot = mnlumodel.getSemantic().getSlots();
                String artist = mslot.getArtist();
                String song = mslot.getSong();

                HandlerUtil.sendmsg(handler, mnlumodel.getText(), 1);

                // 打开播放界面
                Bundle bundle = new Bundle();
                //bundle.putString(Constants.ARTIST, artist);
                //bundle.putString(Constants.SONG, song);
                //Intent intent = new Intent(mcontext, MusicActivity.class);
                //intent.putExtras(bundle);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //mcontext.startActivity(intent);
            } else {
                NluModel.AnswerBean manswer = mnlumodel.getAnswer();
                HandlerUtil.sendmsg(handler, manswer.getText(), 1);
            }
        } else {
            HandlerUtil.sendmsg(handler, mnlumodel.getText(), 1);
        }*/
    }
}
