package com.smartlife.xunfei.protocol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.smartlife.MainApplication;
import com.smartlife.dlan.manager.DlanManager;
import com.smartlife.huanxin.gui.VideoCallActivity;
import com.smartlife.netty.helper.Constants;
import com.smartlife.qintin.info.MusicInfo;
import com.smartlife.qintin.model.DianBoSearchModel;
import com.smartlife.qintin.model.DianBoSearchModel.DataBean;
import com.smartlife.qintin.model.DianBoSearchModel.DataBean.DoclistBean.DocsBean;
import com.smartlife.qintin.service.MusicPlayer;
import com.smartlife.utils.HandlerUtil;
import com.smartlife.xunfei.constant.XunConstants;
import com.smartlife.xunfei.model.NluJokeModel;
import com.smartlife.xunfei.model.NluTelModel;
import com.smartlife.xunfei.model.QueryTvModel;
import com.smartlife.xunfei.model.SmartHomeAirObjModel;
import com.smartlife.xunfei.model.SmartHomeModel;
import com.zhy.http.okhttp.OkHttpUtils;
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
    private QueryTvModel mQueryTvModel;
    private NluJokeModel mNluJokeModel;
    private SmartHomeModel mSmartHomeModel;
    private SmartHomeAirObjModel mSmartHomeAirObjModel;
    private NluTelModel mNluTelModel;
    private List<SmartHomeModel> mList;
    private List<DocsBean> mSearchList;
    List<DocsBean> mList2;
    String albumname;
    String titlename;
    String substring;
    int position,index=2;
    long[] itemlist;
    HashMap<Long, MusicInfo> itemInfos;


    public NluProtocolImpl(Context context){
        this.context = context;
        mApplicatin = (MainApplication)context;
        mSearchList = new ArrayList<>();
    }

    Gson mGson = new Gson();

    public void dealNulData(String data) {
        String substring="";
        String nluType=null;
        JSONObject mSemantic;
        JSONObject mSlots;
        String attrType = null;
        JSONObject jo=null;
        int position;
        try {
            jo = new JSONObject(data);
            nluType = jo.getString("service");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(nluType!=null && nluType.equals("airControl_smartHome")){
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
        }else if(nluType!=null && nluType.equals("tv_smartHome")){
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
        }else if(nluType!=null && nluType.equals("joke")){
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
            // mNluJokeModel = mGson.fromJson(data,NluJokeModel.class);
            DlanManager.getInstance(context).play(mp3);
        }else if(nluType!=null && nluType.equals("telephone")){
            //mNluTelModel = mGson.fromJson(data,NluTelModel.class);
            Log.d(TAG,"telephone");
            tovideo("chat","ss");
        }else{
            dealProtocol(data);
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


    private void dealProtocol(String data){

        String mdata=null;
        JSONObject jo=null;
        index =2;
        try {
            jo = new JSONObject(data);
            mdata = jo.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if((position=mdata.indexOf(XunConstants.XUNFEI_BOFAN))!=-1){
            substring = mdata.substring(position+XunConstants.XUNFEI_BOFAN.length());
            albumname = substring.substring(0,index);
            Log.d(TAG,"albumname = "+albumname+" substring="+substring+" length="+substring.length());
            searchHttp(albumname);
        }else if((position=mdata.indexOf(XunConstants.XUNFEI_WANTSEE))!=-1){
            substring = mdata.substring(position+XunConstants.XUNFEI_WANTSEE.length());
        }else if((position=mdata.indexOf(XunConstants.XUNFEI_BOFAN))!=-1){
            substring = mdata.substring(position+XunConstants.XUNFEI_BOFAN.length());
        }else{

        }
    }

    List<DocsBean> mResult;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int position=0;
            Log.d(TAG,"");
            itemInfos = new HashMap<>();
            switch (msg.what){
                case 1:
                    Log.d(TAG,"handleMessage position="+index+" length="+substring.length()+" substring="+substring);
                    while(index < substring.length() && (mList2=searchList(mSearchList,albumname)).size()>0){
                        mSearchList.clear();

                        index++;
                        mSearchList = mList2;
                        albumname = substring.substring(0,index);
                        Log.d(TAG,"albumname ="+albumname);
                    }
                    index--;
                    Log.d(TAG,"mList2 ="+mSearchList.size()+" substring.length()="+substring.length()+" index="+index);
                    if(substring.length()-index>2){
                        titlename = substring.substring(index);
                        while(substring.length()-index>2 && (mList2=searchList(mSearchList,titlename)).size()==0){
                            index++;
                            titlename = substring.substring(index);
                        }
                    }

                    itemlist = new long[mSearchList.size()];
                    for(DocsBean mdoc:mSearchList){
                        MusicInfo musicInfo = new MusicInfo();
                        musicInfo.songId = (long)mdoc.getId();
                        musicInfo.musicName = mdoc.getTitle();
                        musicInfo.artist = mdoc.getTitle();
                        musicInfo.islocal = false;
                        musicInfo.albumName = mdoc.getParent_name();
                        musicInfo.albumId = mdoc.getParent_id();
                        musicInfo.artistId = (long)mdoc.getId();
                        musicInfo.lrc = "1";
                        musicInfo.albumData = "1";
                        musicInfo.filepath = mdoc.getFile_path();
                        musicInfo.url = "http://"+mApplicatin.getDomainUrl()+"/"+mdoc.getFile_path()+"/"+musicInfo.songId+".mp3"+"?"+"deviceid=00002000-6822-8da4-ffff-ffffca74";
                        Log.d(TAG,"mSearchList ="+mdoc.getTitle());
                        itemlist[position] = (long)mdoc.getId();
                        position++;
                        itemInfos.put((long)mdoc.getId(),musicInfo);
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                MusicPlayer.playAll(itemInfos,itemlist,0, false);
                        }
                    }, 10);
                    break;
                case 2:
                    mResult = matchList(mSearchList,substring);
                    Log.d(TAG,"mResult size =" + mResult.size()+" name ="+mResult.get(0).getTitle());
                    itemlist = new long[mResult.size()];
                    for(DocsBean mdata:mResult){
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
                    break;
            }
        }
    };

    void searchHttp(String data){
        String mSearchProgramUrl = "http://api.open.qingting.fm/newsearch/"+data+"/type/program_ondemand";
        OkHttpUtils
                .post()
                .url(mSearchProgramUrl)
                .addParams("access_token",mApplicatin.getAccessToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG,"onError");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        DianBoSearchModel mDianBoSearchModel;
                        Log.d(TAG,"response ="+response);
                        mDianBoSearchModel = gson.fromJson(response,DianBoSearchModel.class);

                        for(DataBean data:mDianBoSearchModel.getData()){
                            for(DocsBean doc:data.getDoclist().getDocs()){
                                mSearchList.add(doc);
                            }
                        }
                        HandlerUtil.sendmsg(mHandler,null,2);
                    }
                });
    }

    List<DocsBean> searchList(List<DocsBean> list,String data){
        List<DocsBean> mList = new ArrayList<>();
        for(DocsBean doc:list){
            if(doc.getParent_name().contains(data) || doc.getTitle().contains(data)){
                mList.add(doc);
            }
        }
        return  mList;
    }

    List<DocsBean> matchList(List<DocsBean> list,String data){
        List<DocsBean> mMatchList1 = new ArrayList<>();
        List<DocsBean> mMatchList2 = new ArrayList<>();
        List<DocsBean> mMatchList3 = new ArrayList<>();
        List<DocsBean> mMatchList4 = new ArrayList<>();
        List<DocsBean> mMatchList5 = new ArrayList<>();
        List<DocsBean> mResultList;

        for(DocsBean mdata:list){
            int n =2;
            try {
                if (mdata.getParent_name().contains(data.substring(n, n + 2)) || mdata.getTitle().contains(data.substring(n, n + 2))) {
                    n = n + 2;
                    mMatchList1.add(mdata);
                    if (mdata.getParent_name().contains(data.substring(n, n + 2)) || mdata.getTitle().contains(data.substring(n, n + 2))) {
                        mMatchList2.add(mdata);
                        n = n+2;
                        if (mdata.getParent_name().contains(data.substring(n, n + 2)) || mdata.getTitle().contains(data.substring(n, n + 2))) {
                            mMatchList3.add(mdata);
                        }
                    }
                }
            }catch (IndexOutOfBoundsException e){

            }
            try{
                n =2;
                if( mMatchList3.size()>1 || mMatchList2.size()>1 || mMatchList1.size()>1){
                    if(mdata.getParent_name().contains(data.substring(n,n+3)) || mdata.getTitle().contains(data.substring(n,n+3))){
                        n=n+3;
                        mMatchList4.add(mdata);
                        if(mdata.getParent_name().contains(data.substring(n,n+3)) || mdata.getTitle().contains(data.substring(n,n+3))){
                            mMatchList5.add(mdata);
                        }
                    }
                }
            }catch (IndexOutOfBoundsException e){

            }
        }

        if(mMatchList5.size()>0){
            Log.d(TAG,"mMatchList5");
            mResultList = mMatchList5;
        }else if(mMatchList4.size()>0){
            Log.d(TAG,"mMatchList4");
            mResultList = mMatchList4;
        }else if(mMatchList3.size()>0){
            Log.d(TAG,"mMatchList3");
            mResultList = mMatchList3;
        }else if(mMatchList2.size()>0){
            Log.d(TAG,"mMatchList2");
            mResultList = mMatchList2;
        }else if(mMatchList1.size()>0){
            Log.d(TAG,"mMatchList1");
            mResultList = mMatchList1;
        }else{
            Log.d(TAG,"mMatchList");
            mResultList = list;
        }
        return mResultList;
    }
}
