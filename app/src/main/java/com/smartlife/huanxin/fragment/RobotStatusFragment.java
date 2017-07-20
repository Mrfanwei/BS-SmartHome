package com.smartlife.huanxin.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.smartlife.R;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.huanxin.DemoHelper;
import com.smartlife.huanxin.adapter.RobotAdapter;
import com.smartlife.huanxin.gui.VideoCallActivity;
import com.smartlife.huanxin.model.RobotModel;
import com.smartlife.netty.helper.Constants;
import com.smartlife.MainActivity;
import com.smartlife.netty.manager.NettyManager;
import com.smartlife.utils.ThreadPool;
import com.smartlife.qintin.widget.SwipeRefreshLayout;
import com.smartlife.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;

public class RobotStatusFragment extends DialogFragment{

    private String TAG = "SmartLife/RobotStatus";
    private RobotTask mCallTask;
    public MainActivity mActivity;
    public Activity mContext;
    private SwipeMenuListView robots_bind;
    private SwipeRefreshLayout refreshableView;
    private long time;
    private static List<RobotModel.DataBean> list_robots;

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
                } else {
                    getrobotinfo();
                }
                refreshableView.setRefreshing(false);
                refreshableView.setEnabled(true);
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

    public void unBindRobot(String phoneid,String robotname){
        OkRequestEvents.unBindRobot(phoneid,robotname,new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG,"onResponse unBindRobot ="+ response);
            }
        });
    }

    public void getrobotinfo() {

        list_robots.clear();
        OkRequestEvents.getrobotinfo(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "onError");
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


    private static RobotAdapter mRobotAdapter;
    RobotModel.DataBean unbind = null;

    public void setadapter() {
        //list_robots = sort(list_robots);
        mRobotAdapter = new RobotAdapter(mContext, list_robots);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
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
                        unBindRobot(unbind.getId(),unbind.getRname());
                        list_robots.remove(position);
                        mRobotAdapter.notifyDataSetChanged();
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
                case "3":
                    //NettyManager.getInstance(getActivity().getApplicationContext()).startSocket();
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
