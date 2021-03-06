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
import com.smartlife.qintin.model.DianBoRecommendModel;
import com.smartlife.qintin.model.DianBoRecommendModel.DataBean.RecommendsBean;
import com.smartlife.qintin.model.ErrorModel;
import com.smartlife.qintin.model.LoodModel;
import com.smartlife.qintin.uitl.NetUtils;
import com.smartlife.qintin.uitl.PreferencesUtility;
import com.smartlife.qintin.widget.LoodView;
import com.smartlife.utils.GsonUtil;
import com.smartlife.utils.LogUtil;
import com.smartlife.utils.ToastUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by wm on 2016/4/9.
 */
public class SelectFragment extends BaseFragment {

    private String TAG = "SmartLifee/SelectFra";
    private RecommendAdapter mAdapter;
    private int width = 160, height = 160;
    private LinearLayout mViewContent;
    ;
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
    private OnFragmentInteractionListener mListener;
    View mLoadingTargetView;

    public void setChanger(ChangeView changer) {
        mChangeView = changer;
    }

    @Override
    protected void onFirstUserVisible() {
        if (mLoodView != null)
            mLoodView.requestFocus();
//        toggleShowLoading(true, null);
        if (NetUtils.isNetworkConnected(mContext)) {
            dianBoCategoryRecommend();
        } else {
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    toggleShowLoading(true, null);
                    dianBoCategoryRecommend();
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
    protected View getLoadingTargetView() {
        return mLoadingTargetView;
    }

    public interface OnFragmentInteractionListener {
        void startPlaylistActivity(DianBoRecommendModel.DataBean.RecommendsBean bean);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        mContent = (ViewGroup) inflater.inflate(R.layout.fragment_recommend_container, container, false);

        mLayoutInflater = LayoutInflater.from(mContext);
        mRecommendView = mLayoutInflater.inflate(R.layout.recommend, container, false);
        mViewContent = (LinearLayout) mRecommendView.findViewById(R.id.recommend_layout);

        mLoadView = mLayoutInflater.inflate(R.layout.loading, null, false);
        mLoadingTargetView = (View) mLoadView.findViewById(R.id.player_loading_view);
        mViewContent.addView(mLoadView);
        mViewHashMap = new HashMap<>();

        mLoodView = (LoodView) mRecommendView.findViewById(R.id.loop_view);
        if (!isDayFirst) {
            mContent.addView(mRecommendView);
        }
        return mContent;
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
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemView viewholder = new ItemView(layoutInflater.inflate(R.layout.recommend_playlist_item, parent, false));
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

            ((ItemView) holder).name.setText(info.getTitle());
            ((ItemView) holder).count.setText(spanString);

            ((ItemView) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.startPlaylistActivity(info);
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

    private void dianBoCategoryRecommend() {
        OkRequestEvents.dianBoCategoryRecommend(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id, String jsonString) {
                if (call == null && e == null && id == 0) {
                    // 没有access_token
                    LogUtil.getLog().d("no token");
                    OkRequestEvents.qinTinCredential(new TokenCallBack() {
                        @Override
                        public void onResponse() {
                            dianBoCategoryRecommend();
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
                            dianBoCategoryRecommend();
                        }
                        return;
                    }
                    LogUtil.getLog().d("dianBoCategoryRecommend onError = " + e);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                int count = response.length() / 1000;
                for (int i = 0; i < count; i++) {
                    Log.d(TAG, response.substring(i * 1000, i * 1000 + 1000));
                }
                Log.d(TAG, response.substring(count * 1000, response.length()));

                DianBoRecommendModel mDianBoRecommendModel;
                List<RecommendsBean> mList;
                LoodModel mLoodModel;
                List<LoodModel> mLoodModelList = new ArrayList<>();

                Gson gson = new Gson();
                mDianBoRecommendModel = gson.fromJson(response, DianBoRecommendModel.class);
                mViewContent.removeView(mLoadView);
                if (mDianBoRecommendModel.getErrorno() == 0) {
                    for (DianBoRecommendModel.DataBean mdata : mDianBoRecommendModel.getData()) {
                        mList = new ArrayList<>();
                        mAdapter = new RecommendAdapter(null);
                        mLoodModelList.clear();
                        for (DianBoRecommendModel.DataBean.RecommendsBean mRecommend : mdata.getRecommends()) {
                            if (mRecommend.getDetail().getType().equals("program_ondemand")) {
                                mList.add(mRecommend);
                                mLoodModel = new LoodModel();
                                mLoodModel.setThumb(mRecommend.getThumb());
                                mLoodModel.setId(mRecommend.getParent_info().getParent_id());
                                mLoodModelList.add(mLoodModel);
                            }
                        }

                        if (mLoodModelList.size() > 0) {
                            if (mdata.getName().equals("banner")) {
                                mLoodView.updataData(mLoodModelList);
                            }
                        }

                        if (mList.size() > 2) {
                            if (!mdata.getName().equals("banner")) {
                                showList(mdata.getName(), mAdapter);
                                mAdapter.update(mList);
                            }
                        }
                    }
                } else {
                    ToastUtil.showShort("无数据");
                }

            }
        });
    }

    public void showList(String title, RecommendAdapter adapter) {

        RecyclerView mRecyclerView;
        GridLayoutManager mGridLayoutManager;
        View mView;

        if (mViewHashMap.containsKey(title)) {
            mViewContent.removeView(mViewHashMap.get(title));
        }

        mView = mLayoutInflater.inflate(R.layout.select_list, mViewContent, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.banner_recyclerview);
        ((TextView) mView.findViewById(R.id.tv_banner1)).setText(title);
        mGridLayoutManager = new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);
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

    @Override
    public void showError(String msg) {
        super.showError(msg);
        toggleShowError(true, msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dianBoCategoryRecommend();
            }
        });
    }
}
