package com.smartlife.qintin.fragmentnet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.smartlife.qintin.activity.PlaylistActivity;
import com.smartlife.qintin.fragment.AttachFragment;
import com.smartlife.qintin.model.CategoryPropertyModel;
import com.smartlife.qintin.model.DianBoModel;
import com.smartlife.qintin.model.DianBoRecommendModel;
import com.smartlife.qintin.model.LoodModel;
import com.smartlife.qintin.model.ZhiBoCategoryModel.DataBean;
import com.smartlife.qintin.uitl.PreferencesUtility;
import com.smartlife.qintin.widget.LoodView;
import com.smartlife.utils.HandlerUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class MusicFragment extends AttachFragment {

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
    private int musicalbumid;
    private int musicsectionid;
    private List<CategoryPropertyModel.DataBean.ValuesBean > mCategoryPropertyList;
    private List<CategoryPropertyModel.DataBean> mDataBean;
    MusicListAdapter mMusicRecommendAdapter;

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
        mCategoryPropertyList = new ArrayList<>();
        mDataBean = new ArrayList<>();
        return mContent;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isFirstLoad){
            if(mLoodView != null)
                mLoodView.requestFocus();
            dianBoCategoryProgram();
            isFirstLoad = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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

    private List<CategoryPropertyModel.DataBean.ValuesBean> mList;
    private int showCount =8;
    class ContentCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public ContentCategoryListAdapter(List<CategoryPropertyModel.DataBean.ValuesBean> list) {
            mList = list;
        }

        public void update(List<CategoryPropertyModel.DataBean.ValuesBean> list) {
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
            final CategoryPropertyModel.DataBean.ValuesBean info = mList.get(position);

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
                        intent.putExtra("dbcategoryname", info.getName());
                        intent.putExtra("dbcategoryid", musicalbumid);
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

    class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<DianBoRecommendModel.DataBean.RecommendsBean> mList;
        SpannableString spanString;

        public MusicListAdapter(List<DianBoRecommendModel.DataBean.RecommendsBean> list) {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.index_icn_earphone);
            ImageSpan imgSpan = new ImageSpan(mContext, b, ImageSpan.ALIGN_BASELINE);
            spanString = new SpannableString("icon");
            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mList = list;
        }

        public void update(List<DianBoRecommendModel.DataBean.RecommendsBean> list) {
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

            final DianBoRecommendModel.DataBean.RecommendsBean info = mList.get(position);

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(info.getThumb()))
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(((ItemView) holder).art.getController())
                    .setImageRequest(request)
                    .build();
            ((ItemView) holder).art.setController(controller);
            Log.d(TAG,"getParent_name()="+info.getParent_info().getParent_name()+" title="+info.getTitle());
            ((ItemView) holder).title.setText(info.getParent_info().getParent_name());
            ((ItemView) holder).subtitle.setText(info.getTitle());

            ((ItemView) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, PlaylistActivity.class);
                    intent.putExtra("dbcategoryname",info.getTitle());
                    intent.putExtra("itemcount",20);
                    intent.putExtra("playlistid", "1");
                    intent.putExtra("recommendsTitle",info.getTitle());
                    intent.putExtra("parent_id",info.getParent_info().getParent_id());
                    intent.putExtra("thumb",info.getThumbs().getSmall_thumb());
                    intent.putExtra("detailTitle",info.getTitle());
                    intent.putExtra("detailDuration",111);
                    getActivity().startActivity(intent);
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

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    dianBoMusicAlbum();
                    break;
                case 1:
                    dianboMusic();
                    break;
            }
        }
    };

    private void dianBoCategoryProgram(){
        OkRequestEvents.dianBoCategoryProgram(mApplication.getAccessToken(), new StringCallback(){
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                List<DataBean> mList = new ArrayList<>();
                DianBoModel mDianBoModel=null;
                int count =0;
                Gson gson = new Gson();
                mDianBoModel = gson.fromJson(response,DianBoModel.class);
                mViewContent.removeView(mLoadView);
                for(DianBoModel.DataBean mData : mDianBoModel.getData()){
                    if(mData.getName().equals("音乐")){
                        musicalbumid=mData.getId();
                        musicsectionid = mData.getSection_id();
                        HandlerUtil.sendmsg(mHandler,null,0);
                    }
                }
            }
        });
    }

    private void dianBoMusicAlbum(){
        OkRequestEvents.dianBoMusicAlbum(mApplication.getAccessToken(),musicalbumid,new StringCallback(){
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                CategoryPropertyModel mCategoryPropertyModel;
                ContentCategoryListAdapter mAdapter = new ContentCategoryListAdapter(null);

                Gson gson = new Gson();
                mCategoryPropertyModel = gson.fromJson(response, CategoryPropertyModel.class);
                mViewContent.removeView(mLoadView);
                for(CategoryPropertyModel.DataBean mdata:mCategoryPropertyModel.getData()){
                    if(mdata.getName().equals("内容")){
                        for(CategoryPropertyModel.DataBean.ValuesBean mvalue:mdata.getValues()){
                            mCategoryPropertyList.add(mvalue);
                        }
                        CategoryPropertyModel.DataBean.ValuesBean mValuesBean = new CategoryPropertyModel.DataBean.ValuesBean();
                        mValuesBean.setName("more");
                        mCategoryPropertyList.add(mValuesBean);
                        showContentCategoryList(mdata.getName(),mAdapter);
                        mAdapter.update(mCategoryPropertyList);
                    }
                }
                HandlerUtil.sendmsg(mHandler,null,1);
            }
        });
    }

    private void dianboMusic(){
        OkRequestEvents.dianBoMusic(mApplication.getAccessToken(),musicsectionid,new StringCallback(){
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                DianBoRecommendModel mDianBoRecommendModel;
                List<DianBoRecommendModel.DataBean.RecommendsBean> mRecommendList;
                LoodModel mLoodModel;
                List<LoodModel> mLoodModelList = new ArrayList<>();

                Gson gson = new Gson();
                mDianBoRecommendModel = gson.fromJson(response, DianBoRecommendModel.class);
                mViewContent.removeView(mLoadView);
                for(DianBoRecommendModel.DataBean mdata:mDianBoRecommendModel.getData()){
                    mMusicRecommendAdapter = new MusicListAdapter(null);
                    mRecommendList = new ArrayList<>();
                    mLoodModelList.clear();
                    for(DianBoRecommendModel.DataBean.RecommendsBean mRecommend:mdata.getRecommends()){
                        if(mRecommend.getDetail().getType().equals("program_ondemand")){
                            mRecommendList .add(mRecommend);
                            mLoodModel = new LoodModel();
                            mLoodModel.setThumb(mRecommend.getThumb());
                            mLoodModel.setId(mRecommend.getParent_info().getParent_id());
                            mLoodModelList.add(mLoodModel);
                        }
                    }
                    if(mRecommendList.size()>0){
                        if(mdata.getName().equals("banner")){
                            mLoodView.updataData(mLoodModelList);
                        }else{
                            showPlayingList(mdata.getName(),mMusicRecommendAdapter);
                            mMusicRecommendAdapter.update(mRecommendList);
                        }
                    }
                }
            }
        });
    }

    public void showPlayingList(String title,MusicListAdapter adapter){
        RecyclerView mRecyclerView;
        GridLayoutManager mGridLayoutManager;
        View mView;
        if(mViewHashMap.containsKey(title)){
            mViewContent.removeView(mViewHashMap.get(title));
        }

        mView = mLayoutInflater.inflate(R.layout.select_list, mViewContent, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.banner_recyclerview);
        ((TextView)mView.findViewById(R.id.tv_banner1)).setText(title);
        mGridLayoutManager = new GridLayoutManager(mContext, 3, LinearLayoutManager.HORIZONTAL,false);
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
