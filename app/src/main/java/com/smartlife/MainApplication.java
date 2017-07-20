package com.smartlife;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.smartlife.dlan.manager.DlanManager;
import com.smartlife.huanxin.DemoHelper;
import com.smartlife.qintin.handler.UnceHandler;
import com.smartlife.qintin.model.CredentialModel;
import com.smartlife.qintin.model.DomainCenterModel;
import com.smartlife.qintin.permissions.Nammu;
import com.smartlife.qintin.provider.PlaylistInfo;
import com.smartlife.qintin.uitl.IConstants;
import com.smartlife.qintin.uitl.PreferencesUtility;
import com.smartlife.qintin.uitl.ThemeHelper;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.cybergarage.upnp.ControlPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import okhttp3.Call;

/**
 * Created by wm on 2016/2/23.
 */
public class MainApplication extends Application implements ThemeUtils.switchColor {
    private String TAG = "SmartLifee/MainApp";
    public static String currentUserNick = "";
    public static Context context;
    private static int MAX_MEM = (int) Runtime.getRuntime().maxMemory() / 4;
    private long favPlaylist = IConstants.FAV_PLAYLIST;
    private static Gson gson;
    CredentialModel mCredentialModel=null;
    DomainCenterModel mDomainCenterModel=null;
    private DlanManager mDlanManager;
    public ControlPoint mControlPoint;

    private String username = "18825281243";

    QinTinTask mQinTinCredentTask=null;
    QinTinTask mQinTinDomainTask=null;
    QinTinTask mDlanTask=null;
    private static MainApplication instance;
    public static MainApplication getInstance() {
        return instance;
    }

    public static Gson gsonInstance() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private ImagePipelineConfig getConfigureCaches(Context context) {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEM,// 内存缓存中总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,// 内存缓存中图片的最大数量。
                MAX_MEM,// 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,// 内存缓存中准备清除的总图片的最大数量。
                Integer.MAX_VALUE / 10);// 内存缓存中单个图片的最大大小。

        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };
        ImagePipelineConfig.Builder builder = ImagePipelineConfig.newBuilder(context)
                .setDownsampleEnabled(true);
        builder.setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams);


        //小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(context.getApplicationContext().getCacheDir())//缓存图片基路径
                .build();

        //默认图片的磁盘配置
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(Environment.getExternalStorageDirectory().getAbsoluteFile())//缓存图片基路径
                .build();

        //缓存图片配置
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)//内存缓存配置（一级缓存，已解码的图片）
                .setMainDiskCacheConfig(diskCacheConfig)//磁盘缓存配置（总，三级缓存）
                ;
        return builder.build();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        //清空内存缓存（包括Bitmap缓存和未解码图片的缓存）
        imagePipeline.clearMemoryCaches();
    }


    private void frescoInit() {
        Fresco.initialize(this, getConfigureCaches(this));
    }

    //捕获全局Exception 重启界面
    public void initCatchException() {
        //设置该CrashHandler为程序的默认处理器
        UnceHandler catchExcep = new UnceHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
    }

    @Override
    public void onCreate() {

        context = getApplicationContext();
        instance = this;
        qinTinCredential();
        frescoInit();
        super.onCreate();
        buglyInit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Nammu.init(this);
        }
        ThemeUtils.setSwitchColor(this);
        initCatchException();
        if (!PreferencesUtility.getInstance(this).getFavriateMusicPlaylist()) {
            PlaylistInfo.getInstance(this).addPlaylist(favPlaylist, getResources().getString(com.smartlife.R.string.my_fav_playlist),
                    0, "res:/" + com.smartlife.R.mipmap.lay_protype_default, "local");
            PreferencesUtility.getInstance(this).setFavriateMusicPlaylist(true);
        }

        DemoHelper.getInstance().init(this);
        mDlanManager = DlanManager.getInstance(getApplicationContext());

        SharedPreferences settings = getSharedPreferences("userinfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("phoneid",username);
        editor.putString("robotid",username);
        editor.commit();

        xunFeiInit();
    }

    private void buglyInit(){
        String packageName = context.getPackageName();
        String processName = getProcessName(android.os.Process.myPid());
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(getApplicationContext(),"b3e7148288",true, strategy);
       // CrashReport.initCrashReport(getApplicationContext(),"b3e7148288", true,strategy);
    }

    private void xunFeiInit(){
        StringBuffer param = new StringBuffer();
        param.append("appid="+getString(R.string.xunfei_app_id));
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(MainApplication.this, param.toString());
    }

    public DlanManager getmDlanManager(){ return mDlanManager;}

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public int replaceColorById(Context context, @ColorRes int colorId) {
        if (ThemeHelper.isDefaultTheme(context)) {
            return context.getResources().getColor(colorId);
        }
        String theme = getTheme(context);
        if (theme != null) {
            colorId = getThemeColorId(context, colorId, theme);
        }
        return context.getResources().getColor(colorId);
    }

    @Override
    public int replaceColor(Context context, @ColorInt int originColor) {
        if (ThemeHelper.isDefaultTheme(context)) {
            return originColor;
        }
        String theme = getTheme(context);
        int colorId = -1;
        if (theme != null) {
            colorId = getThemeColor(context, originColor, theme);
        }
        return colorId != -1 ? getResources().getColor(colorId) : originColor;
    }

    private String getTheme(Context context) {
        if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_STORM) {
            return "blue";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_HOPE) {
            return "purple";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_WOOD) {
            return "green";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_LIGHT) {
            return "green_light";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_THUNDER) {
            return "yellow";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_SAND) {
            return "orange";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_FIREY) {
            return "red";
        }
        return null;
    }

    private
    @ColorRes
    int getThemeColorId(Context context, int colorId, String theme) {
        switch (colorId) {
            case com.smartlife.R.color.theme_color_primary:
                return context.getResources().getIdentifier(theme, "color", getPackageName());
            case com.smartlife.R.color.theme_color_primary_dark:
                return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
            case com.smartlife.R.color.playbarProgressColor:
                return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
        }
        return colorId;
    }

    private
    @ColorRes
    int getThemeColor(Context context, int color, String theme) {
        switch (color) {
            case 0xd20000:
                return context.getResources().getIdentifier(theme, "color", getPackageName());
        }
        return -1;
    }

    public String getAccessToken(){
        if(mCredentialModel == null)
            return null;
        return mCredentialModel.getAccess_token();
    }

    public String getDomainUrl(){
        return mDomainCenterModel.getData().getStoredaudio_m4a().getMediacenters().get(0).getDomain();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 0:
                    qinTinDomainCenter();
                    break;
            }
        }
    };

    private void   qinTinCredential(){

        if (mQinTinCredentTask == null || mQinTinCredentTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            mQinTinCredentTask = new QinTinTask();
            mQinTinCredentTask.execute(0, 0, 1);
        }
    }

    private void   qinTinDomainCenter(){
        if (mQinTinDomainTask == null || mQinTinDomainTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            mQinTinDomainTask = new QinTinTask();
            mQinTinDomainTask.execute(0, 0, 2);
        }
    }

    private void dlanStart(){
        if (mDlanTask == null || mDlanTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            mDlanTask = new QinTinTask();
            mDlanTask.execute(0, 0, 3);
        }
    }

    class QinTinTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... integers) {
            switch (integers[2]) {
                case 1:
                    OkHttpUtils
                            .post()
                            .url("http://api.open.qingting.fm/access?&grant_type=client_credentials")
                            .addParams("client_id", "ZmRlMWQxYTItMmE3YS0xMWU3LTkyM2YtMDAxNjNlMDAyMGFk")
                            .addParams("client_secret", "YTExMzJiNDgtY2UwOS0zMDEwLWFlN2EtMmFjNzk4ZWQyZjVl")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d(TAG,"onError");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Gson gson = new Gson();
                                    mCredentialModel = gson.fromJson(response, CredentialModel.class);
                                    Log.d(TAG,"access_token ="+ mCredentialModel.getAccess_token()+" getToken_type="+mCredentialModel.getToken_type());
                                    mHandler.sendEmptyMessage(0);
                                }
                            });
                    break;
                case 2:
                    OkHttpUtils
                            .post()
                            .url("http://api.open.qingting.fm/v6/media/mediacenterlist")
                            .addParams("access_token",mCredentialModel.getAccess_token())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d(TAG,"onError");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Gson gson = new Gson();
                                    mDomainCenterModel = gson.fromJson(response, DomainCenterModel.class);
                                    for(DomainCenterModel.DataBean.StoredaudioM4aBean.MediacentersBeanXXX ll:mDomainCenterModel.getData().getStoredaudio_m4a().getMediacenters()){
                                        Log.d(TAG,"mDomainCenterModel domain ="+ ll.getDomain()+" name="+ll.getName()+" protocol="+ll.getProtocol());
                                        Log.d(TAG,"mDomainCenterModel access ="+ll.getAccess());
                                    }
                                }
                            });
                    break;
            }
            return null;
        }
    }

    public void setControlPoint(ControlPoint controlPoint) {
        mControlPoint = controlPoint;
    }
}