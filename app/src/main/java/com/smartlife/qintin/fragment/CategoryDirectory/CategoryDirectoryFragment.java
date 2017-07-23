package com.smartlife.qintin.fragment.CategoryDirectory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bilibili.magicasakura.widgets.TintImageView;
import com.google.gson.Gson;
import com.smartlife.MainActivity;
import com.smartlife.R;
import com.smartlife.qintin.activity.CategoryDirectoryActivity;
import com.smartlife.qintin.fragment.AttachFragment;
import com.smartlife.qintin.fragmentnet.ChangeView;
import com.smartlife.qintin.model.DianBoModel;
import com.smartlife.qintin.net.NetworkUtils;
import com.smartlife.qintin.uitl.PreferencesUtility;
import com.smartlife.qintin.widget.LoodView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.Call;

public class CategoryDirectoryFragment extends AttachFragment {

    private String TAG = "SmartLifee/SelectF";
    private RecyclerView mRecyclerView1, mRecyclerView2, mRecyclerView3;
    private GridLayoutManager mGridLayoutManager, mGridLayoutManager2, mGridLayoutManager3;
    private CategoryListAdapter mBannerAdapter;
    private ArrayList<DianBoModel.DataBean> mBannerList = new ArrayList<>();

    private int width = 160, height = 160;
    private LinearLayout mViewContent;;
    private LayoutInflater mLayoutInflater;
    private View mLoadView, v1, v2;
    private HashMap<String, View> mViewHashMap;
    private String mPosition;
    private ChangeView mChangeView;
    private boolean isFromCache = true;
    private boolean isDayFirst;
    private ViewGroup mContent;
    private View mRecommendView;
    private LoodView mLoodView;
    MainActivity mActivity;

    DianBoModel mDianBoModel=null;
    SelectTask mSelectNovelTask=null;
    public static final String ARGS_PAGE = "args_page";

    public void setChanger(ChangeView changer) {
        mChangeView = changer;
    }

    public static CategoryDirectoryFragment newInstance(int page) {
        CategoryDirectoryFragment fragment = new CategoryDirectoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE,page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity)getActivity();
        mContent = (ViewGroup) inflater.inflate(R.layout.fragment_category_container, container, false);

        mLayoutInflater = LayoutInflater.from(mContext);
        mRecommendView = mLayoutInflater.inflate(R.layout.category,container,false);
        String date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
        mViewContent = (LinearLayout) mRecommendView.findViewById(R.id.category_layout);
        if(!PreferencesUtility.getInstance(mContext).isCurrentDayFirst(date)){
            PreferencesUtility.getInstance(mContext).setCurrentDate(date);
            View dayRec = mLayoutInflater.inflate(R.layout.loading_daymusic,container,false);
            ImageView view1 = (ImageView) dayRec.findViewById(R.id.loading_dayimage) ;
            RotateAnimation rotateAnimation = new RotateAnimation(0,360, 1, 0.5F, 1, 0.5F );
            rotateAnimation.setDuration(20000L);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            rotateAnimation.setRepeatMode(Animation.INFINITE);
            view1.startAnimation(rotateAnimation);
            isDayFirst = true;
            mContent.addView(dayRec);
        }

        mLoadView = mLayoutInflater.inflate(R.layout.loading, null, false);
        mViewContent.addView(mLoadView);

        mBannerAdapter = new CategoryListAdapter(null);

        mLoodView = (LoodView) mRecommendView.findViewById(R.id.loop_view);
        if(!isDayFirst){
            mContent.addView(mRecommendView);
        }
        return mContent;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(mLoodView != null)
            mLoodView.requestFocus();
        }
    }

    public void requestData(){
        initReloadAdapter();
        if (mSelectNovelTask == null || mSelectNovelTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            mSelectNovelTask = new SelectTask();
            Log.d(TAG, "mSelectNovelTask 11");
            mSelectNovelTask.execute(0, 0, 1);
        } else {
            Log.d(TAG, "mSelectNovelTask 22");
        }
    }

    private void initReloadAdapter(){
        v1 = mLayoutInflater.inflate(R.layout.select_list, mViewContent, false);
        mRecyclerView1 = (RecyclerView) v1.findViewById(R.id.banner_recyclerview);
        mGridLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView1.setLayoutManager(mGridLayoutManager);
        mRecyclerView1.setAdapter(mBannerAdapter);
        mRecyclerView1.setHasFixedSize(true);
        TextView more = (TextView) v1.findViewById(R.id.tv_refresh);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangeView.changeTo(1);
            }
        });

        mViewHashMap = new HashMap<>();
        mViewHashMap.put("小说", v1);

        mPosition = PreferencesUtility.getInstance(mContext).getItemPosition();
        mViewContent.removeView(mLoadView);
        if(isDayFirst){
            mContent.removeAllViews();
            mContent.addView(mRecommendView);
        }

        addViews();
    }

    private void addViews() {
        String[] strs = mPosition.split(" ");
        for (int i = 0; i < 1; i++) {
            Log.d(TAG,"strs ="+strs[i] +" length="+strs.length);
            mViewContent.addView(mViewHashMap.get(strs[i]));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPosition == null) {
            return;
        }
        String st = PreferencesUtility.getInstance(mContext).getItemPosition();
        if (!st.equals(mPosition)) {
            mPosition = st;
            mViewContent.removeAllViews();
            addViews();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoodView.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<DianBoModel.DataBean> mList;
        SpannableString spanString;

        public CategoryListAdapter(ArrayList<DianBoModel.DataBean> list) {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.index_icn_earphone);
            ImageSpan imgSpan = new ImageSpan(mContext, b, ImageSpan.ALIGN_BASELINE);
            spanString = new SpannableString("icon");
            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mList = list;
        }

        public void update(ArrayList<DianBoModel.DataBean> list) {
            Log.d(TAG,"update");
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG,"onCreateViewHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemView viewholder = new ItemView(layoutInflater.inflate(R.layout.category_playlist_item, parent, false));
            return viewholder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d(TAG,"onBindViewHolder");
            final DianBoModel.DataBean info = mList.get(position);
            ((ItemView) holder).mimage.setImageResource(R.drawable.music_icn_local);
            ((ItemView) holder).mtitle.setText(info.getName());

            ((ItemView) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, CategoryDirectoryActivity.class);
                    intent.putExtra("dbcategoryname",info.getName());
                    intent.putExtra("dbcategoryid",info.getId());
                    intent.putExtra("dbcategorysectionid",info.getSection_id());
                    mContext.startActivity(intent);
                    Log.d(TAG,"itemview onclick");
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mList == null) {
                return 0;
            }

            if (mList.size() < 7) {
                return mList.size();
            } else {
                return 6;
            }
        }

        class ItemView extends RecyclerView.ViewHolder {
            protected TintImageView mimage;
            private TextView mtitle;

            public ItemView(View itemView) {
                super(itemView);
                this.mimage = (TintImageView) itemView.findViewById(R.id.fragment_main_item_img);
                this.mtitle = (TextView) itemView.findViewById(R.id.fragment_main_item_title);
            }
        }
    }

    class SelectTask extends AsyncTask<Integer, Integer, Integer>{
        @Override
        protected Integer doInBackground(Integer... integers) {
            if (NetworkUtils.isConnectInternet(mContext)) {
                isFromCache = false;
            }

            switch (integers[2]){
                case 1:
                    String mDianboCategoryRecommendUrl = "http://api.open.qingting.fm/v6/media/categories";
                    OkHttpUtils
                            .post()
                            .url(mDianboCategoryRecommendUrl)
                            .addParams("access_token", mApplication.getAccessToken())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d(TAG,"onError14");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.d(TAG,"onResponse111="+response);
                                    Gson gson = new Gson();
                                    mDianBoModel = gson.fromJson(response,DianBoModel.class);
                                    for(DianBoModel.DataBean mData: mDianBoModel.getData()){
                                        mBannerList.add(mData);
                                    }
                                    mBannerAdapter.update(mBannerList);
                                }
                            });
                    break;
            }
            return null;
        }
    }
}
