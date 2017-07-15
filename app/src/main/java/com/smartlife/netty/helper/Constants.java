package com.smartlife.netty.helper;

public class Constants {

    /**
     * 是否是调试模式 !!! 重要 !!! 正式发布一定要改为false !!! 否则会打印全部日志和请求 !!!
     */
    public static final boolean isDebug = true;

    public static final String API_URL = "http://139.224.27.119:83/api/%s";

    /**
     * 请求超时时间8秒
     */
    public static final long TIMEOUT = 8000;

    // 请求地址
    public static String address = "http://server.yydrobot.com";

    // socket ip地址
    //public static String ip = "112.74.175.96";
    public static String ip = "192.168.0.6";

    // socket 端口号
    public static String port = "8889";
    // 任务添加操作请求码
    public static int add_RequestCode = 888;

    // 修改操作请求码
    public static int update_RequestCode = 999;

    // 绑定机器人请求码aad
    public static int bindrobot_RequestCode = 555;

    // 请求成功
    public static int IS_OK = 200;

    // 请求失败
    public static int Error = 500;

    // 修改任务标识
    public static String Update = "update_task";

    // 添加任务标识
    public static String Add = "add_task";

    // 控制命令机器人id
    public static String rid = "";

    // 行走方向
    public static String execode;

    // 命令内容
    public static String text;

    // 任务的四种action
    public static String Task_Add = "task_add";

    public static String Task_Remove = "task_remove";

    public static String Task_Query = "task_query";

    public static String Task_Updata = "task_updata";

    // 语音action
    public static String Speech_action = "speak";

    // 移动action
    public static String Move_aciton = "move";

    // 任务结果
    public static String Result = "result";

    // 照片查询
    public static String Photo_Query = "photo_query";

    public static String Photo_Query_Name = "photo_query_name";
    // 照片删除
    public static String Photo_Delete = "photo_delete";

    // 照片接收
    public static String Photo_Reply = "photo_reply";

    // 照片名字接收
    public static String Photo_Reply_Names = "photo_reply_names";

    // 照片下载
    public static String Photo_Download = "photo_download";

    // 停止服务
    public static String Stop = "stop";

    // 断网情况socket处理
    public static String Socket_Error = "socket_error";

    // 机器人信息修改
    public static String Robot_Info_Update = "robot_info_update";

    // 视频模式
    public static String Video_Mode = "yongyida.robot.video.videomode";

    public static boolean flag = false;

    public static String Start_Socket = "start_socket";

    public static final int STOP = 0;

    public static final int PLAY = 1;

    public static final int PAUSE = 2;

    // 公用的task类
    public static Task task;

    public final static String SLEEP_INTENT = "org.videolan.vlc.SleepIntent";
    public final static String INCOMING_CALL_INTENT = "org.videolan.vlc.IncomingCallIntent";
    public final static String CALL_ENDED_INTENT = "org.videolan.vlc.CallEndedIntent";

    public static final String ARTIST = "artist";
    public static final String SONG = "song";
}
