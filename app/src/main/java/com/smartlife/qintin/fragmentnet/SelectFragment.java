package com.smartlife.qintin.fragmentnet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.smartlife.qintin.activity.PlaylistActivity;
import com.smartlife.qintin.fragment.AttachFragment;
import com.smartlife.qintin.model.DianBoRecommendModel;
import com.smartlife.qintin.model.DianBoRecommendModel.DataBean.RecommendsBean;
import com.smartlife.qintin.model.LoodModel;
import com.smartlife.qintin.net.NetworkUtils;
import com.smartlife.qintin.uitl.PreferencesUtility;
import com.smartlife.qintin.widget.LoodView;
import com.smartlife.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by wm on 2016/4/9.
 */
public class SelectFragment extends AttachFragment {

    private String TAG = "SmartLifee/SelectF";

    private RecommendAdapter mAdapter;
    private int width = 160, height = 160;
    private LinearLayout mViewContent;;
    private LayoutInflater mLayoutInflater;
    private View mLoadView;
    private HashMap<String, View> mViewHashMap;
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
        mContent = (ViewGroup) inflater.inflate(R.layout.fragment_recommend_container, container, false);

        mLayoutInflater = LayoutInflater.from(mContext);
        mRecommendView = mLayoutInflater.inflate(R.layout.recommend,container,false);
        String date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
        mViewContent = (LinearLayout) mRecommendView.findViewById(R.id.recommend_layout);
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
        return mContent;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isFirstLoad){
            if(mLoodView != null)
                mLoodView.requestFocus();
            dianBoCategoryRecommend();
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

    class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<RecommendsBean> mList;
        SpannableString spanString;

        public RecommendAdapter(ArrayList<DianBoRecommendModel.DataBean.RecommendsBean> list) {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.index_icn_earphone);
            ImageSpan imgSpan = new ImageSpan(mContext, b, ImageSpan.ALIGN_BASELINE);
            spanString = new SpannableString("icon");
            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mList = list;
        }

        public void update(List<RecommendsBean> list) {
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG,"onCreateViewHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemView viewholder = new ItemView(layoutInflater.inflate(R.layout.recommend_playlist_item, parent, false));
            return viewholder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d(TAG,"onBindViewHolder");
            final DianBoRecommendModel.DataBean.RecommendsBean info = mList.get(position);

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(info.getThumb()))
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(((ItemView) holder).art.getController())
                    .setImageRequest(request)
                    .build();

            ((ItemView) holder).art.setController(controller);

            ((ItemView) holder).name.setText(info.getTitle());
            ((ItemView) holder).count.setText(spanString);

            //int count = Integer.parseInt(info.getListenum());
            int count = Integer.parseInt("100");
            if (count > 10000) {
                count = count / 10000;
                ((ItemView) holder).count.append(" " + count + "万");
            } else {
               // ((ItemView) holder).count.append(" " + info.getListenum());
                ((ItemView) holder).count.append(" " + "万");
            }
            ((ItemView) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"itemView.setOnClickListener");
                Intent intent = new Intent(mActivity, PlaylistActivity.class);
                intent.putExtra("itemcount",20);
                intent.putExtra("playlistid", "1");
                intent.putExtra("playlistcount", "11");
                intent.putExtra("recommendsTitle",info.getTitle());
                Log.d(TAG,"recommendsTitle ="+info.getTitle());
                intent.putExtra("parent_id",info.getParent_info().getParent_id());
                if(info.getParent_info()!=null){
                    intent.putExtra("parent_name",info.getParent_info().getParent_name());
                }else{
                    intent.putExtra("parent_name","null");
                }
                intent.putExtra("thumb",info.getThumb());
                intent.putExtra("recommendsSequence",info.getSequence());
                intent.putExtra("detailTitle",info.getDetail().getTitle());
                intent.putExtra("detailDuration",info.getDetail().getDuration());
                if(info.getDetail().getMediainfo()!=null){
                    intent.putExtra("file_path",info.getDetail().getMediainfo().getBitrates_url().get(0).getFile_path());
                }else{
                    intent.putExtra("file_path","null");
                }
                mContext.startActivity(intent);
            }
            });
        }

        @Override
        public int getItemCount() {
            if (mList == null) {
                return 0;
            }

            return 3;
        }

        class ItemView extends RecyclerView.ViewHolder {
            private SimpleDraweeView art;
            private TextView name, count;

            public ItemView(View itemView) {
                super(itemView);
                art = (SimpleDraweeView) itemView.findViewById(R.id.playlist_art);
                name = (TextView) itemView.findViewById(R.id.playlist_name);
                count = (TextView) itemView.findViewById(R.id.playlist_listen_count);
            }
        }
    }

    private void dianBoCategoryRecommend(){
        OkRequestEvents.dianBoCategoryRecommend(mApplicatin.getAccessToken(),new StringCallback(){
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG,"onError");
            }

            @Override
            public void onResponse(String response, int id) {
                DianBoRecommendModel mDianBoRecommendModel;
                List<RecommendsBean> mList;
                LoodModel mLoodModel;
                List<LoodModel> mLoodModelList = new ArrayList<>();

                Gson gson = new Gson();
                mDianBoRecommendModel = gson.fromJson(response,DianBoRecommendModel.class);
                mViewContent.removeView(mLoadView);
                for(DianBoRecommendModel.DataBean mdata: mDianBoRecommendModel.getData()){
                    mList = new ArrayList<>();
                    mAdapter = new RecommendAdapter(null);
                    mLoodModelList.clear();
                    for(DianBoRecommendModel.DataBean.RecommendsBean mRecommend:mdata.getRecommends()){
                        if(mRecommend.getDetail().getType().equals("program_ondemand")){
                            mList .add(mRecommend);

                            mLoodModel = new LoodModel();
                            mLoodModel.setThumb(mRecommend.getThumb());
                            mLoodModel.setId(mRecommend.getParent_info().getParent_id());
                            mLoodModelList.add(mLoodModel);
                        }
                    }
                    if(mList.size() >2){

                        if(mdata.getName().equals("banner")){
                            mLoodView.updataData(mLoodModelList);
                        }else{
                            showList(mdata.getName(),mAdapter);
                            mAdapter.update(mList);
                        }

                    }
                }
            }
        });
    }

    public void showList(String title,RecommendAdapter adapter){

        RecyclerView mRecyclerView;
        GridLayoutManager mGridLayoutManager;
        View mView;

        if(mViewHashMap.containsKey(title)){
            mViewContent.removeView(mViewHashMap.get(title));
        }

        mView = mLayoutInflater.inflate(R.layout.select_list, mViewContent, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.banner_recyclerview);
        ((TextView)mView.findViewById(R.id.tv_banner1)).setText(title);
        mGridLayoutManager = new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        TextView refresh = (TextView) mView.findViewById(R.id.tv_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //requestData();
            }
        });
        mViewHashMap.put(title, mView);
        mViewContent.addView(mView);
    }
}
