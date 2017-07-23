package com.smartlife.qintin.fragmentnet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.smartlife.http.OkRequestEvents;
import com.smartlife.qintin.activity.CategoryDirectoryActivity;
import com.smartlife.qintin.fragment.AttachFragment;
import com.smartlife.qintin.model.DianBoModel;
import com.smartlife.qintin.model.DianBoModel.DataBean;
import com.smartlife.qintin.uitl.PreferencesUtility;
import com.smartlife.qintin.widget.LoodView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class CategoryFragment extends AttachFragment {

    private String TAG = "SmartLifee/SelectF";
    private int width = 160, height = 160;
    private LinearLayout mViewContent;;
    private LayoutInflater mLayoutInflater;
    private View mLoadView;
    private HashMap<String, View> mViewHashMap;
    private String mPosition;
    private ChangeView mChangeView;
    private boolean isFromCache = true;
    private boolean isDayFirst;
    private ViewGroup mContent;
    private View mRecommendView;
    private LoodView mLoodView;
    public MainActivity mActivity;
    private boolean isFirstLoad = true;

    DianBoModel mDianBoModel=null;

    public void setChanger(ChangeView changer) {
        mChangeView = changer;
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
        mViewHashMap = new HashMap<>();
        mLoodView = (LoodView) mRecommendView.findViewById(R.id.loop_view);
        if(!isDayFirst){
            mContent.addView(mRecommendView);
        }
        //initReloadAdapter();
        return mContent;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(mLoodView != null && isFirstLoad)
                mLoodView.requestFocus();
            dianBoCategoryRecommend();
            isFirstLoad = false;
        }
    }

    private void initReloadAdapter(){
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
        for (int i = 0; i < 6; i++) {
            Log.d(TAG,"strs ="+strs[i] +" length="+strs.length);
            mViewContent.addView(mViewHashMap.get(strs[i]));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (mPosition == null) {
            return;
        }
        String st = PreferencesUtility.getInstance(mContext).getItemPosition();
        if (!st.equals(mPosition)) {
            mPosition = st;
            mViewContent.removeAllViews();
            addViews();
        }*/
        //mViewContent.removeAllViews();
        //requestData();
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
        private List<DataBean> mList;
        SpannableString spanString;

        public CategoryListAdapter(List<DataBean> list) {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.index_icn_earphone);
            ImageSpan imgSpan = new ImageSpan(mContext, b, ImageSpan.ALIGN_BASELINE);
            spanString = new SpannableString("icon");
            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mList = list;
        }

        public void update(List<DataBean> list) {
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

    private void dianBoCategoryRecommend(){
        OkRequestEvents.dianBoCategoryProgram(mApplication.getAccessToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG,"onError");
            }

            @Override
            public void onResponse(String response, int id) {
                List<DataBean> mList = new ArrayList<>();
                CategoryListAdapter mAdapter = new CategoryListAdapter(null);
                int count =0;
                Gson gson = new Gson();
                mDianBoModel = gson.fromJson(response,DianBoModel.class);
                mViewContent.removeView(mLoadView);
                //mViewContent.removeAllViews();
                for(DataBean mData : mDianBoModel.getData()){
                    if(count%6 == 0 && count/6!=0){
                        mList = new ArrayList<>();
                        mList.add(mData);
                        mAdapter = new CategoryListAdapter(null);
                    }else if(count%6 == 5){
                        mList.add(mData);
                        showList(mData.getName(),mAdapter);
                        mAdapter.update(mList);
                    }else{
                        mList.add(mData);
                    }
                    count++;
                }
            }
        });
    }

    public void showList(String title,CategoryListAdapter adapter){

        RecyclerView mRecyclerView;
        GridLayoutManager mGridLayoutManager;
        View mView;

        if(mViewHashMap.containsKey(title)){
            mViewContent.removeView(mViewHashMap.get(title));
        }
        mView = mLayoutInflater.inflate(R.layout.category_list, mViewContent, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.banner_recyclerview);
        mGridLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

        mViewHashMap.put(title, mView);
        mViewContent.addView(mView);
    }
}
