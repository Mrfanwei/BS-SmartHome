package com.smartlife.http;

import com.smartlife.huanxin.DemoHelper;
import com.smartlife.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

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
    public static void qinTinCredential(String client_id, String client_secret, StringCallback callback) {

        HashMap<String, String> params = BaseRequestParams();
        params.put("client_id", client_id);
        params.put("client_secret", client_secret);

        postImpl("http://api.open.qingting.fm/access?&grant_type=client_credentials", callback, params, null);
    }

    /**
     * 音频数据中心
     */
    public static void qinTinDomainCenter(String access_token, StringCallback callback) {

        HashMap<String, String> params = BaseRequestParams();
        params.put("access_token", access_token);

        postImpl("http://api.open.qingting.fm/v6/media/mediacenterlist", callback, params, null);
    }

    /**
     * 点播分类推荐
     */
    public static void dianBoCategoryRecommend(String access_token, StringCallback callback) {

        HashMap<String, String> params = BaseRequestParams();
        params.put("access_token", access_token);

        postImpl("http://api.open.qingting.fm/v6/media/recommends/guides/section/0", callback, params, null);
    }

    /**
     * 点播分类节目
     */
    public static void dianBoCategoryProgram(String access_token, StringCallback callback) {

        HashMap<String, String> params = BaseRequestParams();
        params.put("access_token", access_token);

        postImpl("http://api.open.qingting.fm/v6/media/categories", callback, params, null);
    }

    /**
     * 点播分类节目
     */
    public static void dianBoCategoryDirectory(String access_token,int id ,StringCallback callback) {

        HashMap<String, String> params = BaseRequestParams();
        params.put("access_token", access_token);

        postImpl("http://api.open.qingting.fm/v6/media/categories/"+id, callback, params, null);
    }

    /**
     * 点播分类节目list
     */
    public static void dianBoCategoryList(String access_token,int dataid,int valueid,int page ,StringCallback callback) {

        HashMap<String, String> params = BaseRequestParams();
        params.put("access_token", access_token);

        postImpl("http://api.open.qingting.fm/v6/media/categories/" + dataid +"/channels/order/0/attr/"+valueid+"/curpage/"+page+"/pagesize/10", callback, params, null);
    }

    /**
     * 点播音乐album
     */
    public static void dianBoMusicAlbum(String access_token,int albumid,StringCallback callback) {

        HashMap<String, String> params = BaseRequestParams();
        params.put("access_token", access_token);

        postImpl("http://api.open.qingting.fm/v6/media/categories/"+albumid, callback, params, null);
    }

    /**
     * 点播音乐album
     */
    public static void dianBoMusic(String access_token,int musicid,StringCallback callback) {

        HashMap<String, String> params = BaseRequestParams();
        params.put("access_token", access_token);

        postImpl("http://api.open.qingting.fm/v6/media/recommends/guides/section/"+musicid, callback, params, null);
    }

    /**
     * 点播音乐play list
     */
    public static void dianBoPlayList(String access_token,int parentid,int page,StringCallback callback) {
        int count = 15;
        HashMap<String, String> params = BaseRequestParams();
        params.put("access_token", access_token);

        postImpl("http://api.open.qingting.fm/wapi/channelondemands/" + parentid +"/programs/curpage/"+ page +"/pagesize/"+count, callback, params, null);
    }

    /**
     * 广播分类
     */
    public static void radioCategory(String access_token,StringCallback callback) {

        HashMap<String, String> params = BaseRequestParams();
        params.put("access_token", access_token);

        postImpl("http://api.open.qingting.fm/v6/media/categories/5", callback, params, null);
    }

    /**
     * 星期广播
     */
    public static void radioWeek(String access_token,String week,StringCallback callback) {

        HashMap<String, String> params = BaseRequestParams();
        params.put("access_token", access_token);

        postImpl("http://api.open.qingting.fm/v6/media/recommends/nowplaying/day/" + week, callback, params, null);
    }

    /**
     * 取消绑定robot
     * */
    public static void unBindRobot(String phoneid,String robotname,StringCallback callback){
        HashMap<String, String> params = BaseRequestParams();
        params.put("phoneid",phoneid);
        params.put("robotname", robotname);
        postImpl("http://112.74.175.96:8080/unBindRobot", callback, params, null);
    }

    /**
     * 获取绑定robot信息
     * */
    public static void getrobotinfo(StringCallback callback){
        HashMap<String, String> params = BaseRequestParams();
        params.put("phoneid", DemoHelper.getInstance().getCurrentUsernName());

        postImpl("http://112.74.175.96:8080/getRobotInfo", callback, params, null);
    }

    /**
     * 插入music数据
     * */
    public static void insertMusicData(String parentid,String filepath,int albumid,StringCallback callback){
        HashMap<String, String> params = BaseRequestParams();
        params.put("phone", DemoHelper.getInstance().getCurrentUsernName());
        params.put("parentid",parentid);
        params.put("filepath",filepath);
        params.put("albumid",Integer.toString(albumid));

        postImpl("http://112.74.175.96:8080/insertMusicData", callback, params, null);
    }

    /**
     * 获取music数据
     * */
    public static void getMusicInfo(StringCallback callback){
        HashMap<String, String> params = BaseRequestParams();
        params.put("phone", DemoHelper.getInstance().getCurrentUsernName());
        postImpl("http://112.74.175.96:8080/getMusicInfo", callback, params, null);
    }

    /**
     * delete music数据
     * */
    public static void deleteMusicInfo(String filepath,StringCallback callback){
        HashMap<String, String> params = BaseRequestParams();
        params.put("phone", DemoHelper.getInstance().getCurrentUsernName());
        params.put("filepath", filepath);
        postImpl("http://192.168.0.6:8080/deleteMusicInfo", callback, params, null);
    }
}
