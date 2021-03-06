package com.smartlife.qintin.fragmentnet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.smartlife.MainApplication;
import com.smartlife.R;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.http.TokenCallBack;
import com.smartlife.qintin.fragment.BaseFragment;
import com.smartlife.qintin.model.CategoryPropertyModel;
import com.smartlife.qintin.model.DianBoModel;
import com.smartlife.qintin.model.DianBoRecommendModel;
import com.smartlife.qintin.model.ErrorModel;
import com.smartlife.qintin.model.LoodModel;
import com.smartlife.qintin.uitl.NetUtils;
import com.smartlife.qintin.uitl.PreferencesUtility;
import com.smartlife.qintin.widget.LoodView;
import com.smartlife.utils.GsonUtil;
import com.smartlife.utils.LogUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class NovelFragment extends BaseFragment {

    private String TAG = "SmartLife/RadioFrag";
    private int width = 160, height = 160;
    private LinearLayout mViewContent;
    ;
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
    private List<CategoryPropertyModel.DataBean.ValuesBean> mCategoryPropertyList;
    private List<CategoryPropertyModel.DataBean> mDataBean;
    MusicListAdapter mMusicRecommendAdapter;
    private OnFragmentInteractionListener mListener;
    View mLoadingTargetView;

    public void setChanger(ChangeView changer) {
        mChangeView = changer;
    }

    @Override
    protected View getLoadingTargetView() {
        return mLoadingTargetView;
    }

    public interface OnFragmentInteractionListener {
        void startMusicListActivity(DianBoRecommendModel.DataBean.RecommendsBean bean);

        void startCategoryDirectoryActivity(String musicname, int musicid);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
        mContent = (ViewGroup) inflater.inflate(R.layout.fragment_novel_container, container, false);

        mLayoutInflater = LayoutInflater.from(mContext);
        mRecommendView = mLayoutInflater.inflate(R.layout.category, container, false);
        mViewContent = (LinearLayout) mRecommendView.findViewById(R.id.category_layout);

        mLoadView = mLayoutInflater.inflate(R.layout.loading, null, false);
        mLoadingTargetView = (View) mLoadView.findViewById(R.id.player_loading_view);
        mViewContent.addView(mLoadView);
        mViewHashMap = new HashMap<>();
        mLoodView = (LoodView) mRecommendView.findViewById(R.id.loop_view);
        if (!isDayFirst) {
            mContent.addView(mRecommendView);
        }
        mCategoryPropertyList = new ArrayList<>();
        mDataBean = new ArrayList<>();
        return mContent;
    }

    @Override
    protected void onFirstUserVisible() {
        if (mLoodView != null)
            mLoodView.requestFocus();
        toggleShowLoading(true, null);
        if (NetUtils.isNetworkConnected(mContext)) {
            dianBoCategoryProgram();
        } else {
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleShowLoading(true, null);
                    dianBoCategoryProgram();
                }
            });
        }
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

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
    private int showCount = 8;

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
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ContentItemView viewholder = new ContentItemView(layoutInflater.inflate(R.layout.radio_playlist_item, parent, false));
            return viewholder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final CategoryPropertyModel.DataBean.ValuesBean info = mList.get(position);

            if (showCount == 8 && position == 7) {
                ((ContentItemView) holder).mtitle.setText("more");
            } else {
                ((ContentItemView) holder).mtitle.setText(info.getName());
            }

            ((ContentItemView) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick position = " + position);
                    if (position == 7) {
                        showCount = mList.size();
                        notifyDataSetChanged();
                    } else if (position == mList.size() - 1) {
                        showCount = 8;
                        notifyDataSetChanged();
                    } else {
                        mListener.startCategoryDirectoryActivity(info.getName(), musicalbumid);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mList == null) {
                return 0;
            }
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
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
            ((ItemView) holder).title.setText(info.getParent_info().getParent_name());
            ((ItemView) holder).subtitle.setText(info.getTitle());

            ((ItemView) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.startMusicListActivity(info);
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

    private void dianBoCategoryProgram() {
        OkRequestEvents.dianBoCategoryProgram(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id, String jsonString) {
                if (call == null && e == null && id == 0) {
                    // 没有access_token
                    LogUtil.getLog().d("no token");
                    OkRequestEvents.qinTinCredential(new TokenCallBack() {
                        @Override
                        public void onResponse() {
                            dianBoCategoryProgram();
                        }

                        @Override
                        public void onError(String s) {
                            LogUtil.getLog().d("get token onError = " + s);
                        }

                        @Override
                        public void onEmpty() {
                            LogUtil.getLog().d("get token onEmpty");
                        }
                    });
                } else {
                    if (jsonString != null) {
                        ErrorModel errorModel = GsonUtil.json2Bean(jsonString, ErrorModel.class);
                        if (errorModel.getErrorno() == ErrorModel.TOKEN_EXPIRED || errorModel.getErrorno() == ErrorModel.TOKEN_NOT_FOUND) {
                            // Token问题
                            MainApplication.getInstance().setAccessToken(null);
                            dianBoCategoryProgram();
                        }
                        return;
                    }
                    LogUtil.getLog().d("dianBoCategoryProgram onError = " + e);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                DianBoModel mDianBoModel;
                Gson gson = new Gson();
                mDianBoModel = gson.fromJson(response, DianBoModel.class);
                if (mDianBoModel.getErrorno() == 0) {
                    mViewContent.removeView(mLoadView);
                    for (DianBoModel.DataBean mData : mDianBoModel.getData()) {
                        if (mData.getName().equals("小说")) {
                            musicalbumid = mData.getId();
                            musicsectionid = mData.getSection_id();
                            dianBoMusicAlbum();
                        }
                    }
                }
            }
        });
    }

    private void dianBoMusicAlbum() {
        OkRequestEvents.dianBoMusicAlbum(musicalbumid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id, String jsonString) {
                if (call == null && e == null && id == 0) {
                    // 没有access_token
                    LogUtil.getLog().d("no token");
                    OkRequestEvents.qinTinCredential(new TokenCallBack() {
                        @Override
                        public void onResponse() {
                            dianBoMusicAlbum();
                        }

                        @Override
                        public void onError(String s) {
                            LogUtil.getLog().d("get token onError = " + s);
                        }

                        @Override
                        public void onEmpty() {
                            LogUtil.getLog().d("get token onEmpty");
                        }
                    });
                } else {
                    if (jsonString != null) {
                        ErrorModel errorModel = GsonUtil.json2Bean(jsonString, ErrorModel.class);
                        if (errorModel.getErrorno() == ErrorModel.TOKEN_EXPIRED || errorModel.getErrorno() == ErrorModel.TOKEN_NOT_FOUND) {
                            // Token问题
                            MainApplication.getInstance().setAccessToken(null);
                            dianBoMusicAlbum();
                        }
                        return;
                    }
                    LogUtil.getLog().d("qinTinDomainCenter onError = " + e);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                CategoryPropertyModel mCategoryPropertyModel;
                ContentCategoryListAdapter mAdapter = new ContentCategoryListAdapter(null);

                Gson gson = new Gson();
                mCategoryPropertyModel = gson.fromJson(response, CategoryPropertyModel.class);
                if (mCategoryPropertyModel.getErrorno() == 0) {
                    mViewContent.removeView(mLoadView);
                    for (CategoryPropertyModel.DataBean mdata : mCategoryPropertyModel.getData()) {
                        if (mdata.getName().equals("内容")) {
                            for (CategoryPropertyModel.DataBean.ValuesBean mvalue : mdata.getValues()) {
                                mCategoryPropertyList.add(mvalue);
                            }
                            CategoryPropertyModel.DataBean.ValuesBean mValuesBean = new CategoryPropertyModel.DataBean.ValuesBean();
                            mValuesBean.setName("more");
                            mCategoryPropertyList.add(mValuesBean);
                            showContentCategoryList(mdata.getName(), mAdapter);
                            mAdapter.update(mCategoryPropertyList);
                        }
                    }
                    dianboMusic();
                }
            }
        });
    }

    private void dianboMusic() {
        OkRequestEvents.dianBoMusic(musicsectionid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id, String jsonString) {
                if (call == null && e == null && id == 0) {
                    // 没有access_token
                    LogUtil.getLog().d("no token");
                    OkRequestEvents.qinTinCredential(new TokenCallBack() {
                        @Override
                        public void onResponse() {
                            dianboMusic();
                        }

                        @Override
                        public void onError(String s) {
                            LogUtil.getLog().d("get token onError = " + s);
                        }

                        @Override
                        public void onEmpty() {
                            LogUtil.getLog().d("get token onEmpty");
                        }
                    });
                } else {
                    if (jsonString != null) {
                        ErrorModel errorModel = GsonUtil.json2Bean(jsonString, ErrorModel.class);
                        if (errorModel.getErrorno() == ErrorModel.TOKEN_EXPIRED || errorModel.getErrorno() == ErrorModel.TOKEN_NOT_FOUND) {
                            // Token问题
                            MainApplication.getInstance().setAccessToken(null);
                            dianboMusic();
                        }
                        return;
                    }
                    LogUtil.getLog().d("qinTinDomainCenter onError = " + e);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                DianBoRecommendModel mDianBoRecommendModel;
                List<DianBoRecommendModel.DataBean.RecommendsBean> mRecommendList;
                LoodModel mLoodModel;
                List<LoodModel> mLoodModelList = new ArrayList<>();

                Gson gson = new Gson();
                mDianBoRecommendModel = gson.fromJson(response, DianBoRecommendModel.class);
                if (mDianBoRecommendModel.getErrorno() == 0) {
                    mViewContent.removeView(mLoadView);
                    for (DianBoRecommendModel.DataBean mdata : mDianBoRecommendModel.getData()) {
                        mMusicRecommendAdapter = new MusicListAdapter(null);
                        mRecommendList = new ArrayList<>();
                        mLoodModelList.clear();
                        for (DianBoRecommendModel.DataBean.RecommendsBean mRecommend : mdata.getRecommends()) {
                            if (mRecommend.getDetail().getType().equals("program_ondemand")) {
                                mRecommendList.add(mRecommend);
                                mLoodModel = new LoodModel();
                                mLoodModel.setThumb(mRecommend.getThumb());
                                mLoodModel.setId(mRecommend.getParent_info().getParent_id());
                                mLoodModelList.add(mLoodModel);
                            }
                        }
                        if (mRecommendList.size() > 0) {
                            if (mdata.getName().equals("banner")) {
                                mLoodView.updataData(mLoodModelList);
                            } else {
                                showPlayingList(mdata.getName(), mMusicRecommendAdapter);
                                mMusicRecommendAdapter.update(mRecommendList);
                            }
                        }
                    }
                }
            }
        });
    }

    public void showPlayingList(String title, MusicListAdapter adapter) {
        RecyclerView mRecyclerView;
        GridLayoutManager mGridLayoutManager;
        View mView;
        if (mViewHashMap.containsKey(title)) {
            mViewContent.removeView(mViewHashMap.get(title));
        }

        mView = mLayoutInflater.inflate(R.layout.select_list, mViewContent, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.banner_recyclerview);
        ((TextView) mView.findViewById(R.id.tv_banner1)).setText(title);
        mGridLayoutManager = new GridLayoutManager(mContext, 3, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mViewHashMap.put(title, mView);
        mViewContent.addView(mView);
    }

    public void showContentCategoryList(String title, ContentCategoryListAdapter adapter) {

        RecyclerView mRecyclerView;
        GridLayoutManager mGridLayoutManager;
        View mView;
        if (mViewHashMap.containsKey(title)) {
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
