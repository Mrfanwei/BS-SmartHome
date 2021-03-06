package com.smartlife.qintin.fragmentnet;

import android.content.Context;
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
import com.smartlife.MainApplication;
import com.smartlife.R;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.http.TokenCallBack;
import com.smartlife.qintin.fragment.BaseFragment;
import com.smartlife.qintin.model.DianBoModel;
import com.smartlife.qintin.model.DianBoModel.DataBean;
import com.smartlife.qintin.model.ErrorModel;
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

public class CategoryFragment extends BaseFragment {

    private String TAG = "SmartLifee/CategoryFra";
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
    private CategoryFragment.OnFragmentInteractionListener mListener;
    DianBoModel mDianBoModel = null;
    View mLoadingTargetView;

    public void setChanger(ChangeView changer) {
        mChangeView = changer;
    }

    public interface OnFragmentInteractionListener {

        void startCategoryDirectoryActivity(DianBoModel.DataBean bean);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MusicFragment.OnFragmentInteractionListener) {
            mListener = (CategoryFragment.OnFragmentInteractionListener) context;
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
        mContent = (ViewGroup) inflater.inflate(R.layout.fragment_category_container, container, false);

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
        //initReloadAdapter();
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
                    //toggleShowLoading(true, null);
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
    protected View getLoadingTargetView() {
        return mLoadingTargetView;
    }

    private void initReloadAdapter() {
        mPosition = PreferencesUtility.getInstance(mContext).getItemPosition();
        mViewContent.removeView(mLoadView);
        if (isDayFirst) {
            mContent.removeAllViews();
            mContent.addView(mRecommendView);
        }
        addViews();
    }

    private void addViews() {
        String[] strs = mPosition.split(" ");
        for (int i = 0; i < 6; i++) {
            Log.d(TAG, "strs =" + strs[i] + " length=" + strs.length);
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
            Log.d(TAG, "update");
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemView viewholder = new ItemView(layoutInflater.inflate(R.layout.category_playlist_item, parent, false));
            return viewholder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder");
            final DianBoModel.DataBean info = mList.get(position);
            ((ItemView) holder).mimage.setImageResource(R.drawable.music_icn_local);
            ((ItemView) holder).mtitle.setText(info.getName());

            ((ItemView) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.startCategoryDirectoryActivity(info);
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
                List<DataBean> mList = new ArrayList<>();
                CategoryListAdapter mAdapter = new CategoryListAdapter(null);
                int count = 0;
                Gson gson = new Gson();
                mDianBoModel = gson.fromJson(response, DianBoModel.class);
                mViewContent.removeView(mLoadView);
                //mViewContent.removeAllViews();
                for (DataBean mData : mDianBoModel.getData()) {
                    if (count % 6 == 0 && count / 6 != 0) {
                        mList = new ArrayList<>();
                        mList.add(mData);
                        mAdapter = new CategoryListAdapter(null);
                    } else if (count % 6 == 5) {
                        mList.add(mData);
                        showList(mData.getName(), mAdapter);
                        mAdapter.update(mList);
                    } else {
                        mList.add(mData);
                    }
                    count++;
                }
            }
        });
    }

    public void showList(String title, CategoryListAdapter adapter) {

        RecyclerView mRecyclerView;
        GridLayoutManager mGridLayoutManager;
        View mView;

        if (mViewHashMap.containsKey(title)) {
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
