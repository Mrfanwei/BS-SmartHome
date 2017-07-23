package com.smartlife.qintin.fragmentnet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.smartlife.MainActivity;
import com.smartlife.R;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.qintin.activity.CategoryDirectoryActivity;
import com.smartlife.qintin.fragment.AttachFragment;
import com.smartlife.qintin.model.ZhiBoCategoryModel;
import com.smartlife.qintin.model.ZhiBoCategoryModel.DataBean;
import com.smartlife.qintin.model.ZhiBoCategoryModel.DataBean.ValuesBean;
import com.smartlife.qintin.model.ZhiBoRadioList;
import com.smartlife.qintin.uitl.PreferencesUtility;
import com.smartlife.qintin.widget.LoodView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class RadioFragment extends AttachFragment {

    private String TAG = "SmartLife/RadioFrag";
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

    public void setChanger(ChangeView changer) {
        mChangeView = changer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity)getActivity();
        mContent = (ViewGroup) inflater.inflate(R.layout.fragment_radio_container, container, false);

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
        //if(isVisibleToUser && isFirstLoad){
        if(isVisibleToUser && isFirstLoad){
            if(mLoodView != null)
                mLoodView.requestFocus();
            radioCategory();
            radioWeek();
            isFirstLoad = false;
        }
    }

    private void initReloadAdapter(){
        mPosition = PreferencesUtility.getInstance(mContext).getContentItemPosition();
        mViewContent.removeView(mLoadView);
        if(isDayFirst){
            mContent.removeAllViews();
            mContent.addView(mRecommendView);
        }

        addViews();
    }

    private void addViews() {
        String[] strs = mPosition.split(" ");
        for (int i = 0; i < 3; i++) {
            Log.d(TAG,"strs ="+strs[i]);
            mViewContent.addView(mViewHashMap.get(strs[i]));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (mPosition == null) {
            return;
        }
        String st = PreferencesUtility.getInstance(mContext).getContentItemPosition();
        if (!st.equals(mPosition)) {
            mPosition = st;
            mViewContent.removeAllViews();
            addViews();
        }*/
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

    private List<ValuesBean> mList;
    private int showCount =8;
    class ContentCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public ContentCategoryListAdapter(List<ValuesBean> list) {
            mList = list;
        }

        public void update(List<ValuesBean> list) {
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG,"onCreateViewHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ContentItemView viewholder = new ContentItemView(layoutInflater.inflate(R.layout.radio_playlist_item, parent, false));
            return viewholder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ValuesBean info = mList.get(position);

            if(showCount == 8 && position ==7){
                ((ContentItemView) holder).mtitle.setText("more");
            }else{
                ((ContentItemView) holder).mtitle.setText(info.getName());
            }

            ((ContentItemView) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"onClick position = "+position);
                    if(position==7){
                        showCount=mList.size();
                        notifyDataSetChanged();
                    }else if(position==mList.size()-1){
                        showCount=8;
                        notifyDataSetChanged();
                    }else {
                        Intent intent = new Intent(mActivity, CategoryDirectoryActivity.class);
                        intent.putExtra("contentcategoryname", info.getName());
                        intent.putExtra("contentcategoryid", info.getId());
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mList == null) {
                return 0;
            }

            Log.d(TAG,"getItemCount count ="+showCount);
            return showCount;
        }

        class ContentItemView extends RecyclerView.ViewHolder {
            private TextView mtitle;

            public ContentItemView(View itemView) {
                super(itemView);
                this.mtitle = (TextView) itemView.findViewById(R.id.fragment_main_item_title);
                this.mtitle.setGravity(Gravity.CENTER);
            }
        }
    }

    class PlayingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<ZhiBoRadioList.DataBean> mList;
        SpannableString spanString;

        public PlayingAdapter(List<ZhiBoRadioList.DataBean> list) {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.index_icn_earphone);
            ImageSpan imgSpan = new ImageSpan(mContext, b, ImageSpan.ALIGN_BASELINE);
            spanString = new SpannableString("icon");
            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mList = list;
        }

        public void update(List<ZhiBoRadioList.DataBean> list) {
            Log.d(TAG,"update");
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG,"onCreateViewHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemView viewholder = new ItemView(layoutInflater.inflate(R.layout.playing_list_item, parent, false));
            return viewholder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            Log.d(TAG,"onBindViewHolder");
            final ZhiBoRadioList.DataBean info = mList.get(position);

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(info.getThumb() ))
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(((ItemView) holder).art.getController())
                    .setImageRequest(request)
                    .build();

            ((ItemView) holder).art.setController(controller);
            ((ItemView) holder).title.setText(info.getDetail().getTitle());
            ((ItemView) holder).subtitle.setText(info.getSub_title());

            ((ItemView) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, CategoryDirectoryActivity.class);
                    intent.putExtra("dbcategoryname",info.getDetail().getTitle());
                    intent.putExtra("dbcategoryid",info.getId());
                    mContext.startActivity(intent);
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
            private SimpleDraweeView art;
            private TextView title, subtitle;

            public ItemView(View itemView) {
                super(itemView);
                art = (SimpleDraweeView) itemView.findViewById(R.id.viewpager_list_img);
                title = (TextView) itemView.findViewById(R.id.viewpager_list_toptext);
                subtitle = (TextView) itemView.findViewById(R.id.viewpager_list_bottom_text);
            }
        }
    }

    private void radioCategory(){
        OkRequestEvents.radioCategory(mApplication.getAccessToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ZhiBoCategoryModel mZhiBoCategoryModel;
                List<ValuesBean> mContentCategoryList = new ArrayList<>();
                ContentCategoryListAdapter mAdapter = new ContentCategoryListAdapter(null);

                Gson gson = new Gson();
                mZhiBoCategoryModel = gson.fromJson(response,ZhiBoCategoryModel.class);
                mViewContent.removeView(mLoadView);
                for(DataBean mdata:mZhiBoCategoryModel.getData()){
                    if(mdata.getName().contains("类型")){
                        for(ValuesBean mvalue:mdata.getValues()){
                            mContentCategoryList.add(mvalue);
                        }
                        ValuesBean mValuesBean = new ValuesBean();
                        mValuesBean.setName("more");
                        mContentCategoryList.add(mValuesBean);
                        showContentCategoryList(mdata.getName(),mAdapter);
                        mAdapter.update(mContentCategoryList);
                    }
                }
            }
        });
    }

    private void radioWeek(){
        String mweek;
        final Calendar c = Calendar.getInstance();
        mweek = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        OkRequestEvents.radioWeek(mApplication.getAccessToken(), mweek, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ZhiBoRadioList mZhiBoRadioList;
                List<ZhiBoRadioList.DataBean> mPlayingRadioList = new ArrayList<>();
                PlayingAdapter mAdapter = new PlayingAdapter(null);

                Gson gson = new Gson();
                mZhiBoRadioList = gson.fromJson(response, ZhiBoRadioList.class);
                mViewContent.removeView(mLoadView);
                for(ZhiBoRadioList.DataBean mdata:mZhiBoRadioList.getData()){
                    mPlayingRadioList.add(mdata);
                }
                showPlayingList("zhiboplay",mAdapter);
                mAdapter.update(mPlayingRadioList);
            }
        });
    }

    public void showPlayingList(String title,PlayingAdapter adapter){
        RecyclerView mRecyclerView;
        GridLayoutManager mGridLayoutManager;
        View mView;
        if(mViewHashMap.containsKey(title)){
            mViewContent.removeView(mViewHashMap.get(title));
        }

        mView = mLayoutInflater.inflate(R.layout.select_list, mViewContent, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.banner_recyclerview);
        //mGridLayoutManager = new GridLayoutManager(mContext, 3, LinearLayoutManager.HORIZONTAL,false);
        mGridLayoutManager = new GridLayoutManager(mContext, 1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

        mViewHashMap.put(title, mView);
        mViewContent.addView(mView);
    }

    public void showContentCategoryList(String title,ContentCategoryListAdapter adapter){

        RecyclerView mRecyclerView;
        GridLayoutManager mGridLayoutManager;
        View mView;
        if(mViewHashMap.containsKey(title)){
            mViewContent.removeView(mViewHashMap.get(title));
        }
        mView = mLayoutInflater.inflate(R.layout.category_list, mViewContent, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.banner_recyclerview);
        mGridLayoutManager = new GridLayoutManager(mContext, 4);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

        mViewHashMap.put(title, mView);
        mViewContent.addView(mView);
    }
}
