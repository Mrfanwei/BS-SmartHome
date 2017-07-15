package com.smartlife.dlan.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import com.smartlife.R;
import com.smartlife.MainActivity;
import com.smartlife.dlan.manager.DlanManager;
import com.smartlife.dlan.service.DlanService;
import com.smartlife.dlan.service.IDlanManager;
import com.smartlife.utils.BroadcastReceiverRegister;
import com.smartlife.utils.Constants;

import org.cybergarage.upnp.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wm on 2016/3/22.
 */
public class DlanFragment extends DialogFragment implements View.OnClickListener {

    private String TAG = "SmartLife/DlanFragment";
    public MainActivity mActivity;
    public Activity mContext;
    private ListView mList;
    private ViewGroup mContent;
    private LayoutInflater mLayoutInflater;
    private View mLoadView;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLinearLayoutManager;
    private DlanAdapter mDlanAdapter;
    DlanManager mDlanManager = null;
    Intent bindintent;
    private List<String> mDlanList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (MainActivity)getActivity();

        mContent = (ViewGroup) inflater.inflate(R.layout.fragment_recommend_container, container, false);

        mLayoutInflater = LayoutInflater.from(mContext);
        //设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDlanAdapter = new DlanAdapter(mDlanList);

        mLoadView = mLayoutInflater.inflate(R.layout.fragment_dlan, container, false);
        mRecyclerView = (RecyclerView) mLoadView.findViewById(R.id.lv_show);
        mLoadView.findViewById(R.id.btn_search).setOnClickListener(this);
        mLoadView.findViewById(R.id.btn_pause).setOnClickListener(this);
        mLoadView.findViewById(R.id.btn_stop).setOnClickListener(this);
        mLoadView.findViewById(R.id.btn_goon).setOnClickListener(this);

        mLinearLayoutManager = new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mDlanAdapter);
        mRecyclerView.setHasFixedSize(true);
        mContent.addView(mLoadView);

        return mContent;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG,"onClick");
        switch (v.getId()) {
            case R.id.btn_search:
                Log.d(TAG,"btn_search");
                mDlanManager.startSearchDevice();
                break;
            case R.id.btn_pause:
                mDlanManager.pause();
                break;
            case R.id.btn_stop:
                mDlanManager.stop();
                break;
            case R.id.btn_goon:
                mDlanManager.goOn("10");
                break;
        }
    }

    /*void bindService(){
        bindintent = new Intent(getActivity(), DlanService.class);
        getActivity().bindService(bindintent,conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if(mDlanManager==null)
                mDlanManager = IDlanManager.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mDlanManager=null;
        }
    };*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDlanManager = DlanManager.getInstance(null);
        //bindService();
        //设置样式
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDatePickerDialog);

        BroadcastReceiverRegister.reg(getActivity(),
                new String[] {Constants.DLAN_DEVICE }, dlanshow);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mDlanManager.startSearchDevice();
    }

    BroadcastReceiver dlanshow = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mDlanList = intent.getStringArrayListExtra("result");
            for(String mdata:mDlanList){
                Log.d(TAG,"mDlanList ="+mdata);
            }
            mDlanAdapter.update(mDlanList);
        }
    };

    class DlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> mList;

        public DlanAdapter(List<String>  list) {
            mList = list;
        }

        public void update(List<String> list) {
            mList.clear();
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG,"onCreateViewHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemView viewholder = new ItemView(layoutInflater.inflate(R.layout.item_main_dlan, parent, false));
            return viewholder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final int mposition = position;
            //((ItemView) holder).mitem.setText(mList.get(position));
            ((ItemView) holder).mitem.setText(mList.get(position));

            ((ItemView) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("DlanSelectDevice",mList.get(mposition));
                    // Commit the edits!
                    editor.commit();
                    dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mList == null) {
                return 0;
            }
            return mList.size();
        }

        class ItemView extends RecyclerView.ViewHolder {

           private TextView mitem;
            public ItemView(View itemView) {
                super(itemView);
                mitem = (TextView) itemView.findViewById(R.id.tv_main_item);
            }
        }
    }
}
