package com.smartlife.http;

import android.util.Log;

import com.google.gson.Gson;
import com.smartlife.MainApplication;
import com.smartlife.huanxin.DemoHelper;
import com.smartlife.qintin.model.CredentialModel;
import com.smartlife.utils.Constants;
import com.smartlife.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * 描述：采用okHttp进行网络请求
 * 作者：傅健
 * 创建时间：2017/7/19 19:42
 */
public class OkRequestEvents {

    private static String getAbsoultUrl(String partUrl) {
        return String.format(Constants.API_URL, partUrl);
    }


    private static HashMap<String, String> BaseRequestParams() {

        HashMap<String, String> params = new HashMap<>();
        // params.put("appid", "smartlife"); // APP标识
        return params;
    }

    /**
     * post请求具体的执行者
     *
     * @param url      请求url
     * @param callback 回调
     * @param params   请求参数
     * @param tag      取消请求会需要
     */
    private static void postImpl(String url, StringCallback callback, HashMap<String, String> params, String tag) {
        if (tag == null) {
            OkHttpUtils.post().params(params).url(url).build().readTimeOut(Constants.TIMEOUT)
                    .writeTimeOut(Constants.TIMEOUT).connTimeOut(Constants.TIMEOUT).execute(callback);
        } else {
            OkHttpUtils.post().params(params).url(url).tag(tag).build().readTimeOut(Constants.TIMEOUT)
                    .writeTimeOut(Constants.TIMEOUT).connTimeOut(Constants.TIMEOUT).execute(callback);
        }

    }

    /**
     * get请求的具体执行者
     *
     * @param url      请求url
     * @param callback 回调
     * @param params   请求参数
     * @param tag      取消请求会需要
     */
    private static void getImpl(String url, StringCallback callback, HashMap<String, String> params, String tag) {
        if (tag == null) {
            OkHttpUtils.get().params(params).url(url).build().readTimeOut(Constants.TIMEOUT)
                    .writeTimeOut(Constants.TIMEOUT).connTimeOut(Constants.TIMEOUT).execute(callback);
        } else {
            OkHttpUtils.get().params(params).url(url).tag(tag).build().readTimeOut(Constants.TIMEOUT)
                    .writeTimeOut(Constants.TIMEOUT).connTimeOut(Constants.TIMEOUT).execute(callback);
        }
    }

    /**
     * 蜻蜓获取token
     */
    public static void qinTinCredential(TokenCallBack tokenCallBack) {

        HashMap<String, String> params = BaseRequestParams();
        params.put("client_id", Constants.QT_CLIENT_ID);
        params.put("client_secret", Constants.QT_CLIENT_SECRET);

        postImpl("http://api.open.qingting.fm/access?&grant_type=client_credentials", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id, Response response) {
                tokenCallBack.onError(e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                CredentialModel credentialModel = gson.fromJson(response, CredentialModel.class);
                Log.d(TAG, credentialModel.toString());
                String access_token = credentialModel.getAccess_token();
                if (StringUtils.isNotEmpty(access_token)) {
                    MainApplication.getInstance().setAccessToken(access_token);
                    tokenCallBack.onResponse();
                    return;
                }
                tokenCallBack.onEmpty();
            }
        }, params, null);
    }

    /**
     * 音频数据中心
     */
    public static void qinTinDomainCenter(StringCallback callback) {

        if (MainApplication.getInstance().getAccessToken() == null) {
            callback.onError(null, null, 0, null);
        } else {
            HashMap<String, String> params = BaseRequestParams();
            params.put("access_token", MainApplication.getInstance().getAccessToken());

            postImpl("http://api.open.qingting.fm/v6/media/mediacenterlist", callback, params, null);
        }
    }

    /**
     * 点播分类推荐
     */
    public static void dianBoCategoryRecommend(StringCallback callback) {

        if (MainApplication.getInstance().getAccessToken() == null) {
            callback.onError(null, null, 0, null);
        } else {
            HashMap<String, String> params = BaseRequestParams();
            params.put("access_token", MainApplication.getInstance().getAccessToken());

            postImpl("http://api.open.qingting.fm/v6/media/recommends/guides/section/0", callback, params, null);
        }
    }

    /**
     * 点播分类节目
     */
    public static void dianBoCategoryProgram(StringCallback callback) {

        if (MainApplication.getInstance().getAccessToken() == null) {
            callback.onError(null, null, 0, null);
        } else {
            HashMap<String, String> params = BaseRequestParams();
            params.put("access_token", MainApplication.getInstance().getAccessToken());

            postImpl("http://api.open.qingting.fm/v6/media/categories", callback, params, null);
        }
    }

    /**
     * 点播分类节目
     */
    public static void dianBoCategoryDirectory(int id, StringCallback callback) {

        if (MainApplication.getInstance().getAccessToken() == null) {
            callback.onError(null, null, 0, null);
        } else {
            HashMap<String, String> params = BaseRequestParams();
            params.put("access_token", MainApplication.getInstance().getAccessToken());

            postImpl("http://api.open.qingting.fm/v6/media/categories/" + id, callback, params, null);
        }
    }

    /**
     * 点播分类节目list
     */
    public static void dianBoCategoryList(int dataid, int valueid, int page, StringCallback callback) {

        if (MainApplication.getInstance().getAccessToken() == null) {
            callback.onError(null, null, 0, null);
        } else {
            HashMap<String, String> params = BaseRequestParams();
            params.put("access_token", MainApplication.getInstance().getAccessToken());

            postImpl("http://api.open.qingting.fm/v6/media/categories/" + dataid + "/channels/order/0/attr/" + valueid + "/curpage/" + page + "/pagesize/10", callback, params, null);
        }
    }

    /**
     * 点播音乐album
     */
    public static void dianBoMusicAlbum(int albumid, StringCallback callback) {

        if (MainApplication.getInstance().getAccessToken() == null) {
            callback.onError(null, null, 0, null);
        } else {
            HashMap<String, String> params = BaseRequestParams();
            params.put("access_token", MainApplication.getInstance().getAccessToken());

            postImpl("http://api.open.qingting.fm/v6/media/categories/" + albumid, callback, params, null);
        }
    }

    /**
     * 点播音乐album
     */
    public static void dianBoMusic(int musicid, StringCallback callback) {

        if (MainApplication.getInstance().getAccessToken() == null) {
            callback.onError(null, null, 0, null);
        } else {
            HashMap<String, String> params = BaseRequestParams();
            params.put("access_token", MainApplication.getInstance().getAccessToken());

            postImpl("http://api.open.qingting.fm/v6/media/recommends/guides/section/" + musicid, callback, params, null);
        }
    }

    /**
     * 点播音乐play list
     */
    public static void dianBoPlayList(int parentid, int page, StringCallback callback) {

        if (MainApplication.getInstance().getAccessToken() == null) {
            callback.onError(null, null, 0, null);
        } else {
            int count = 15;
            HashMap<String, String> params = BaseRequestParams();
            params.put("access_token", MainApplication.getInstance().getAccessToken());

            postImpl("http://api.open.qingting.fm/wapi/channelondemands/" + parentid + "/programs/curpage/" + page + "/pagesize/" + count, callback, params, null);
        }
    }

    /**
     * 广播分类
     */
    public static void radioCategory(StringCallback callback) {

        if (MainApplication.getInstance().getAccessToken() == null) {
            callback.onError(null, null, 0, null);
        } else {
            HashMap<String, String> params = BaseRequestParams();
            params.put("access_token", MainApplication.getInstance().getAccessToken());

            postImpl("http://api.open.qingting.fm/v6/media/categories/5", callback, params, null);
        }
    }

    /**
     * 星期广播
     */
    public static void radioWeek(String week, StringCallback callback) {

        if (MainApplication.getInstance().getAccessToken() == null) {
            callback.onError(null, null, 0, null);
        } else {
            HashMap<String, String> params = BaseRequestParams();
            params.put("access_token", MainApplication.getInstance().getAccessToken());

            postImpl("http://api.open.qingting.fm/v6/media/recommends/nowplaying/day/" + week, callback, params, null);
        }
    }

    /**
     * 搜索点播
     */
    public static void searchRadio(String name, StringCallback callback) {

        if (MainApplication.getInstance().getAccessToken() == null) {
            callback.onError(null, null, 0, null);
        } else {
            HashMap<String, String> params = BaseRequestParams();
            params.put("access_token", MainApplication.getInstance().getAccessToken());

            postImpl("http://api.open.qingting.fm/newsearch/" + name + "/type/program_ondemand", callback, params, null);
        }
    }

    /**
     * 热门搜索
     */
    public static void searchHotWord(StringCallback callback) {

        if (MainApplication.getInstance().getAccessToken() == null) {
            callback.onError(null, null, 0, null);
        } else {
            HashMap<String, String> params = BaseRequestParams();
            params.put("access_token", MainApplication.getInstance().getAccessToken());

            postImpl("http://api.open.qingting.fm/search/hotkeywords", callback, params, null);
        }
    }

    /**
     * 取消绑定robot
     */
    public static void unBindRobot(String phoneid, String robotname, StringCallback callback) {
        HashMap<String, String> params = BaseRequestParams();
        params.put("phoneid", phoneid);
        params.put("robotname", robotname);
        postImpl("http://112.74.175.96:8080/unBindRobot", callback, params, null);
    }

    /**
     * 获取绑定robot信息
     */
    public static void getrobotinfo(StringCallback callback) {
        HashMap<String, String> params = BaseRequestParams();
        params.put("phoneid", DemoHelper.getInstance().getCurrentUsernName());

        postImpl("http://112.74.175.96:8080/getRobotInfo", callback, params, null);
    }

    /**
     * 插入music数据
     */
    public static void insertMusicData(String parentid, String filepath, int albumid, StringCallback callback) {
        HashMap<String, String> params = BaseRequestParams();
        params.put("phone", DemoHelper.getInstance().getCurrentUsernName());
        params.put("parentid", parentid);
        params.put("filepath", filepath);
        params.put("albumid", Integer.toString(albumid));

        postImpl("http://112.74.175.96:8080/insertMusicData", callback, params, null);
    }

    /**
     * 获取music数据
     */
    public static void getMusicInfo(StringCallback callback) {
        HashMap<String, String> params = BaseRequestParams();
        params.put("phone", DemoHelper.getInstance().getCurrentUsernName());
        postImpl("http://112.74.175.96:8080/getMusicInfo", callback, params, null);
    }

    /**
     * delete music数据
     */
    public static void deleteMusicInfo(String filepath, StringCallback callback) {
        HashMap<String, String> params = BaseRequestParams();
        params.put("phone", DemoHelper.getInstance().getCurrentUsernName());
        params.put("filepath", filepath);
        postImpl("http://192.168.0.6:8080/deleteMusicInfo", callback, params, null);
    }
}
