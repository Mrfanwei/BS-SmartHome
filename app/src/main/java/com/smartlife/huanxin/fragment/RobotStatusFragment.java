package com.smartlife.huanxin.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.smartlife.R;
import com.smartlife.huanxin.Constant;
import com.smartlife.huanxin.DemoHelper;
import com.smartlife.huanxin.adapter.RobotAdapter;
import com.smartlife.huanxin.db.UserDao;
import com.smartlife.huanxin.domain.EaseUser;
import com.smartlife.huanxin.gui.VideoCallActivity;
import com.smartlife.huanxin.model.RobotModel;
import com.smartlife.netty.helper.Constants;
import com.smartlife.MainActivity;
import com.smartlife.netty.manager.NettyManager;
import com.smartlife.utils.CommonUtils;
import com.smartlife.utils.ThreadPool;
import com.smartlife.qintin.widget.SwipeRefreshLayout;
import com.smartlife.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wm on 2016/3/22.
 */
public class RobotStatusFragment extends DialogFragment implements View.OnClickListener {

    private String TAG = "SmartLife/RobotStatus";
    private RobotTask mCallTask;
    public MainActivity mActivity;
    public Activity mContext;
    private SwipeMenuListView robots_bind;
    private SwipeRefreshLayout refreshableView;
    private long time;
    private static List<RobotModel.DataBean> list_robots;
    private SharedPreferences sharedPreferences;
    private boolean isFirstLoad = true;
    private String username = "18825281243";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (MainActivity)getActivity();
        //设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_robot_status, container);
        robots_bind = (SwipeMenuListView) view.findViewById(R.id.robotlist_bind);
        refreshableView = (SwipeRefreshLayout) view.findViewById(R.id.refresh_bind);
        robots_bind.setOnItemClickListener(itemClickListener);
        refreshableView.setOnPullRefreshListener(new SwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                if (System.currentTimeMillis() - time < 1000) {
                    ToastUtil.showtomain(getActivity(), "不要刷新得这么频繁嘛...");
                    refreshableView.setRefreshing(false);
                } else {
                    getrobotinfo();
                }
            }

            @Override
            public void onPullDistance(int distance) {

            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list_robots = new ArrayList<>();
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

    @Override
    public void onResume() {
        time = 0;
        super.onResume();
    }

    @Override
    public void onPause() {
        time = 0;
        super.onPause();
    }

    Handler handler = new Handler() {

        public void dispatchMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    ToastUtil.showtomain(mContext, "机器人不存在");
                    break;
                case 2:
                    refreshableView.setRefreshing(false);
                    setadapter();
                    refreshableView.setEnabled(true);
                    break;
                case 4:
                    ToastUtil.showtomain(mContext, "连接失败");
                    break;
                case 5:
                    if (refreshableView.isRefreshing()) {
                        refreshableView.setRefreshing(false);
                    }
                    if (unbind != null) {
                        list_robots.add(unbind);
                        handler.sendEmptyMessage(2);
                    }
                    ToastUtil.showtomain(mContext,
                            msg.getData().getString("result"));
                    break;
                case 6:
                    ToastUtil.showtomain(mContext, "该机器人不在线！");
                    break;
                case 7:
                    backlogin("用户登录过期，请重新登录！");
                    break;
                case 8:
                    backlogin("用户登录错误，请重新登录！");
                    break;
                case 9:
                    ToastUtil.showtomain(mContext, "机器人已被控制");
                    break;
                case 10:
                    ToastUtil.showtomain(mContext, "机器人已被绑定！");
                    break;
                case 11:
                    ToastUtil.showtomain(mContext, "已超过绑定数量！");
                    break;
                case 12:
                    ToastUtil.showtomain(mContext, "绑定参数不正确！");
                    break;
                default:
                    break;
            }
        }
    };

    private void backlogin(String content) {
        ToastUtil.showtomain(mContext, content);
        mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE).edit().clear().commit();
//        StartUtil.startintent(mContext, LoginActivity.class,
//                "finish");
    }

    public void getrobotinfo() {

        list_robots.clear();
        OkHttpUtils
                .post()
                .url("http://112.74.175.96:8080/getRobotInfo")
                .addParams("phoneid",username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG,"onError");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG,"onResponse19 ="+ response+" id="+id);
                        RobotModel mRobotModel;
                        Gson gson = new Gson();
                        mRobotModel = gson.fromJson(response,RobotModel.class);
                        if(mRobotModel == null){
                            return;
                        }
                        for(RobotModel.DataBean mdata:mRobotModel.getData()){
                            list_robots.add(mdata);
                        }
                        if(list_robots.size()>0){
                            handler.sendEmptyMessage(2);
                        }
                        refreshableView.setRefreshing(false);
                        refreshableView.setEnabled(true);
                    }
                });
    }

    ProgressDialog pro = null;
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view,
                                int position, long id) {
            //NettyManager.getInstance(getActivity().getApplicationContext()).startSocket();
            if (!list_robots.get(position).isOnline()) {
                ToastUtil.showtomain(getActivity(), "机器人处于离线状态");
                return;
            } else if (list_robots.get(position).getController() != 0) {
                ToastUtil.showtomain(getActivity(), "机器人已被控制");
                return;
            }

            if (mCallTask == null || mCallTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                mCallTask = new RobotTask();
                mCallTask.execute(list_robots.get(position).getRname(),null, "3");
            }
        }
    };

    BroadcastReceiver bro = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals("online")) {
//                pro.dismiss();
//                switch (intent.getIntExtra("ret", 0)) {
//                    case 0:
//                        StartUtil.startintent(ConnectActivity.this,
//                                PowerListActivity.class, "no");
//                        unregisterReceiver(bro);
//                        break;
//                    case -1:
//                        handler.sendEmptyMessage(4);
//                        break;
//                    case -5:
//                        break;
//                    case 1:
//                        handler.sendEmptyMessage(7);
//                        break;
//                    case 2:
//                        handler.sendEmptyMessage(8);
//                        break;
//                    case 3:
//                        handler.sendEmptyMessage(8);
//                        break;
//                    case 4:
//                        break;
//                    case 5:
//                        handler.sendEmptyMessage(6);
//                        break;
//                    case 6:
//                        handler.sendEmptyMessage(9);
//                        break;
//
//                    default:
//                        break;
//                }

            }
        }
    };

    private static RobotAdapter mRobotAdapter;
    RobotModel.DataBean unbind = null;

    public void setadapter() {
        //list_robots = sort(list_robots);
        mRobotAdapter = new RobotAdapter(mContext, list_robots);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                Log.d(TAG,"");
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        robots_bind.setMenuCreator(creator);
        robots_bind.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean  onMenuItemClick(final int position, SwipeMenu menu,
                                        int index) {
                View v = robots_bind.getChildAt(position);
                v.setBackgroundColor(Color.BLACK);
                TranslateAnimation tran = new TranslateAnimation(-(menu
                        .getMenuItem(0).getWidth()), -(v.getLeft()
                        + v.getWidth() + menu.getMenuItem(0).getWidth()), 0, 0);
                AlphaAnimation al = new AlphaAnimation(1, 0);
                AnimationSet set = new AnimationSet(true);
                set.addAnimation(tran);
                set.addAnimation(al);
                set.setDuration(500);
                set.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        unbind = list_robots.get(position);

                        // 解除绑定
                        final Map<String, String> param = new HashMap<>();
                        param.put("id",
                                mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE)
                                        .getInt("id", 0) + "");
                        param.put("session",
                                mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE)
                                        .getString("session", null));
                        param.put("robot_id", list_robots.get(position).getId()
                                + "");
                        param.put("robot_serial", list_robots.get(position)
                                .getRobot_serial());
                        list_robots.remove(position);
                        mRobotAdapter.notifyDataSetChanged();
                        ThreadPool.execute(new Runnable() {

                            @Override
                            public void run() {
//                                try {
//                                    NetUtil.getinstance().http("/robot/unbind",
//                                            param, new callback() {
//
//                                                @Override
//                                                public void success(
//                                                        JSONObject json) {
//
//                                                    Log.i("Success",
//                                                            json.toString());
//                                                    getrobotinfo();
//                                                    refreshableView
//                                                            .setRefreshing(false);
//                                                }
//
//                                                @Override
//                                                public void error(
//                                                        String errorresult) {
//                                                    HandlerUtil.sendmsg(
//                                                            handler,
//                                                            errorresult, 5);
//                                                    Log.i("Error", "Error");
//                                                }
//                                            },getActivity() );
//                                } catch (SocketTimeoutException e) {
//                                    HandlerUtil.sendmsg(handler, "请求超时", 5);
//                                    e.printStackTrace();
//
//                                }
                            }
                        });

                    }
                });
                v.startAnimation(set);
                return false;
            }
        });

        synchronized (this) {
            robots_bind.setAdapter(mRobotAdapter);
        }
    }

    private void initializeContacts() {
        Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
        EaseUser newFriends = new EaseUser();
        newFriends.setNickname(Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(
                R.string.Application_and_notify);
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        EaseUser groupUser = new EaseUser();
        String strGroup = getResources().getString(R.string.group_chat);
        groupUser.setNickname(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        EaseUser robotUser = new EaseUser();
        String strRobot = getResources().getString(R.string.robot_chat);
        robotUser.setNickname(Constant.CHAT_ROBOT);
        robotUser.setNick(strRobot);
        robotUser.setHeader("");
        userlist.put(Constant.CHAT_ROBOT, robotUser);

        DemoHelper.getInstance().setContactList(userlist);
        UserDao dao = new UserDao(getActivity());
        List<EaseUser> users = new ArrayList<EaseUser>(userlist.values());
        dao.saveContactList(users);
        Log.d(TAG,"");
    }

    private void huanxinlogin() {
        if (!CommonUtils.isNetWorkConnected(getActivity())) {
            Toast.makeText(getActivity(), R.string.network_isnot_available,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG,"huanxinlogin");

        if (getActivity().getSharedPreferences("userinfo", getActivity().MODE_PRIVATE).getString("username",
                null) == null) {
            try {
                EMClient.getInstance().createAccount(username,username);
                Log.d(TAG,"huanxinlogin1");
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            Log.d(TAG,"huanxinlogin2");
            getActivity().getSharedPreferences("userinfo", getActivity().MODE_PRIVATE)
                    .edit()
                    .putString("phoneid",username)
                    .putString("phoneid",username)
                    .commit();
        }

        EMClient.getInstance().login(
                getActivity().getSharedPreferences("huanxin", getActivity().MODE_PRIVATE).getString("username", null),
                getActivity().getSharedPreferences("huanxin", getActivity().MODE_PRIVATE).getString("password", null),
                new EMCallBack() {
            @Override
            public void onSuccess() {
                try {
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    initializeContacts();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            R.string.login_failure_failed,
                            Toast.LENGTH_SHORT).show();

                    return;
                }
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG,"onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d(TAG,"onError");

            }
        });
    }

    private void tovideo(String mode,String name) {
        sendmsg(mode,getActivity().getSharedPreferences("Receipt", getActivity().MODE_PRIVATE).getString(
                "username", null));
        Bundle params = new Bundle();
        params.putString("mode", mode);
        getActivity().startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username",name)
                .putExtra("isComingCall", false));
    }

    public void sendmsg(String mode, String touser) {
        EMMessage msg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        msg.setAttribute("mode", mode);
        EMCmdMessageBody cmd = new EMCmdMessageBody(Constants.Video_Mode);
        msg.setTo(touser);
        msg.addBody(cmd);
        EMClient.getInstance().chatManager().sendMessage(msg);
    }

    class RobotTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[2]){
                case "2":
                    huanxinlogin();
                    //NettyManager.getInstance(getActivity().getApplicationContext()).startSocket();
                    break;
                case "3":
                    tovideo("chat",strings[0]);
                    break;
            }
            return null;
        }
    }

//    public List<RobotModel> sort(List<RobotModel> list) {
//        List<RobotModel> rs = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getAir().equals(RobotModel.air.nearby)) {
//                rs.add(list.get(i));
//            }
//
//        }
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getAir().equals(RobotModel.air.bind)
//                    && list.get(i).isOnline()) {
//                rs.add(list.get(i));
//            }
//        }
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getAir().equals(RobotModel.air.bind)
//                    && !list.get(i).isOnline()) {
//                rs.add(list.get(i));
//            }
//        }
//        return rs;
//
//    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
