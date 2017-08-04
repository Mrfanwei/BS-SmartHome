package com.smartlife.xunfei.protocol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.smartlife.MainApplication;
import com.smartlife.dlan.manager.DlanManager;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.huanxin.gui.VideoCallActivity;
import com.smartlife.netty.helper.Constants;
import com.smartlife.qintin.info.MusicInfo;
import com.smartlife.qintin.model.DianBoSearchModel;
import com.smartlife.qintin.model.DianBoSearchModel.DataBean;
import com.smartlife.qintin.model.DianBoSearchModel.DataBean.DoclistBean.DocsBean;
import com.smartlife.qintin.service.MusicPlayer;
import com.smartlife.qintin.handler.HandlerUtil;
import com.smartlife.xunfei.constant.XunConstants;
import com.smartlife.xunfei.model.SmartHomeAirObjModel;
import com.smartlife.xunfei.model.SmartHomeModel;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/6/27.
 */

public class NluProtocolImpl{
    private String TAG = "SmartLife/NluPro";
    private Context context;
    MainApplication mApplicatin=null;

    private SmartHomeModel mSmartHomeModel;
    private SmartHomeAirObjModel mSmartHomeAirObjModel;
    String substring;
    int position;
    private Handler mHandler;



    public NluProtocolImpl(Context context){
        this.context = context;
        mApplicatin = (MainApplication)context;
        mHandler = HandlerUtil.getInstance(context);
    }

    Gson mGson = new Gson();

    public void dealNulData(String data) {
        String nluType=null;
        JSONObject mSemantic;
        JSONObject mSlots;
        String attrType = null;
        JSONObject jo=null;
        try {
            jo = new JSONObject(data);
            nluType = jo.getString("service");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if("airControl_smartHome".equals(nluType)){
            try {
                mSemantic = jo.getJSONObject("semantic");
                mSlots = mSemantic.getJSONObject("slots");
                attrType = mSlots.getString("attrType");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(attrType.equals("String")){
                mSmartHomeModel = mGson.fromJson(data,SmartHomeModel.class);
            }else if(attrType.equals("Object(digital)")){
                mSmartHomeAirObjModel = mGson.fromJson(data,SmartHomeAirObjModel.class);
            }
        }else if("tv_smartHome".equals(nluType)){
            try {
                mSemantic = jo.getJSONObject("semantic");
                mSlots = mSemantic.getJSONObject("slots");
                attrType = mSlots.getString("attrType");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(attrType.equals("String")){
                mSmartHomeModel = mGson.fromJson(data,SmartHomeModel.class);
            }
        }else if("joke".equals(nluType)){
            JSONObject mdata;
            JSONArray mresult;
            JSONObject mcontent;
            String mp3=null;

            try {
                mdata = jo.getJSONObject("data");
                mresult = mdata.getJSONArray("result");
                mcontent = mresult.getJSONObject(0);
                mp3 = mcontent.getString("mp4Url");
                Log.d(TAG,"mp3 ="+mp3);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            DlanManager.getInstance(context).play(mp3);
        }else if("telephone".equals(nluType)){
            tovideo("chat","ss");
        }else{
            dealRadioProtocol(data);
        }
    }

    public void sendmsg(String mode, String touser) {
        EMMessage msg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        msg.setAttribute("mode", mode);
        EMCmdMessageBody cmd = new EMCmdMessageBody(Constants.Video_Mode);
        msg.setTo(touser);
        msg.addBody(cmd);
        EMClient.getInstance().chatManager().sendMessage(msg);
    }

    private void tovideo(String mode,String name) {
        sendmsg(mode,context.getSharedPreferences("Receipt", context.MODE_PRIVATE).getString(
                "username", null));
        Bundle params = new Bundle();
        params.putString("mode", mode);
        context.startActivity(new Intent(context, VideoCallActivity.class).putExtra("username",name)
                .putExtra("isComingCall", false));
    }


    private void dealRadioProtocol(String data){

        String mdata=null;
        JSONObject jo;
        try {
            jo = new JSONObject(data);
            mdata = jo.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if((position=mdata.indexOf(XunConstants.XUNFEI_BOFAN))!=-1){
            substring = mdata.substring(position+XunConstants.XUNFEI_BOFAN.length());
            if(substring.length() >= "节目".length()){
                searchRadio(substring);
            }else{
                return;
            }
        }else if((position=mdata.indexOf(XunConstants.XUNFEI_WANTSEE))!=-1){
            substring = mdata.substring(position+XunConstants.XUNFEI_WANTSEE.length());
        }else if((position=mdata.indexOf(XunConstants.XUNFEI_BOFAN))!=-1){
            substring = mdata.substring(position+XunConstants.XUNFEI_BOFAN.length());
        }else{

        }
    }

    private void searchRadio(String data){
        String name;
        name = data.substring(0,"节目".length());

        OkRequestEvents.searchRadio(name, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id, String jsonString) {
                Log.d(TAG,"onError");
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                DianBoSearchModel mDianBoSearchModel;
                List<DocsBean> mSearchList = new ArrayList<>();
                List<DocsBean> mResultList;
                long[] itemlist;
                HashMap<Long, MusicInfo> itemInfos = new HashMap<>();

                mDianBoSearchModel = gson.fromJson(response,DianBoSearchModel.class);
                for(DataBean data:mDianBoSearchModel.getData()){
                    for(DocsBean doc:data.getDoclist().getDocs()){
                        mSearchList.add(doc);
                    }
                }

                if(mSearchList.size() < 1){
                    return;
                }else if(mSearchList.size() == 1){
                    mResultList = mSearchList;
                }else{
                    mResultList = matchRadioList(mSearchList,data);
                }

                for(DocsBean mdoc : mResultList){
                    Log.d(TAG,"mdoc title ="+mdoc.getTitle());
                }

                itemlist = new long[mResultList.size()];
                for(DocsBean mdata:mResultList){
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.songId = (long)mdata.getId();
                    musicInfo.musicName = mdata.getTitle();
                    musicInfo.artist = mdata.getTitle();
                    musicInfo.islocal = false;
                    musicInfo.albumName = mdata.getParent_name();
                    musicInfo.albumId = mdata.getParent_id();
                    musicInfo.artistId = (long)mdata.getId();
                    musicInfo.lrc = "1";
                    musicInfo.albumData = "1";
                    musicInfo.filepath = mdata.getFile_path();
                    musicInfo.url = "http://"+mApplicatin.getDomainUrl()+"/"+mdata.getFile_path()+"/"+musicInfo.songId+".mp3"+"?"+"deviceid=00002000-6822-8da4-ffff-ffffca74";
                    itemlist[position] = (long)mdata.getId();
                    position++;
                    itemInfos.put((long)mdata.getId(),musicInfo);
                }

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MusicPlayer.playAll(itemInfos,itemlist,0, false);
                    }
                }, 10);
            }
        });
    }

    List<DocsBean> matchRadioList(List<DocsBean> list,String data){
        List<DocsBean> resultList = new ArrayList<>();
        List<DocsBean> tempList;
        int position =1;
        String matchString;
        tempList = list;

        while(data.length() > (position+1)*2){
            matchString = data.substring(position*2,(position+1)*2);
            for(DocsBean docs : tempList){
                if(docs.getParent_name().contains(matchString) || docs.getTitle().contains(matchString)){
                    resultList.add(docs);
                }
            }

            if(resultList.size() < 1){
                resultList = tempList;
                break;
            }else if(resultList.size() == 1){
                break;
            }

            tempList = resultList;
            position++;
        }

        return resultList;
    }
}
