package com.smartlife.qintin.fragment.CategoryDirectory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.smartlife.MainApplication;
import com.smartlife.R;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.http.TokenCallBack;
import com.smartlife.qintin.fragment.BaseFragment;
import com.smartlife.qintin.model.CategoryAllRadioModel;
import com.smartlife.qintin.model.CategoryAllRadioModel.DataBean;
import com.smartlife.qintin.model.ErrorModel;
import com.smartlife.qintin.net.NetworkUtils;
import com.smartlife.qintin.widget.DividerItemDecoration;
import com.smartlife.qintin.widget.SwipeRefreshLayout;
import com.smartlife.utils.GsonUtil;
import com.smartlife.utils.LogUtil;
import com.smartlife.utils.ToastUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class CategoryListFragment extends BaseFragment implements SwipeRefreshLayout.OnPushLoadMoreListener, SwipeRefreshLayout.OnPullRefreshListener {

    private static final String TAG = "SmartLifee/ListFr";
    private static final String DATA_ID = "data_id";
    private static final String VALUE_ID = "value_id";
    private static final String TAB_NAME = "tab_name";

    private PlaylistDetailAdapter mAdapter;
    private SwipeRefreshLayout mSpList;
    private boolean isFromCache = true;
    private int width = 160, height = 160;
    private int mDataId;
    private int mValueId;
    private List<DataBean> adapterList = new ArrayList<>();
    private CategoryAllRadioModel mCategoryAllRadioModel;
    private int totalAlbumCount = 0;
    private int currentpage;
    private boolean noMoreData;
    private OnFragmentInteractionListener mListener;
    private LayoutInflater mLayoutInflater;
    private View mLoadView;
    View mLoadingTargetView;

    public interface OnFragmentInteractionListener {
        void startPlaylistActivity(CategoryAllRadioModel.DataBean bean);
    }

    public static CategoryListFragment newInstance(int dataId, int valueId, String tabName) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putInt(DATA_ID, dataId);
        args.putInt(VALUE_ID, valueId);
        args.putString(TAB_NAME, tabName);
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mValueId = getArguments().getInt(VALUE_ID);
            mDataId = getArguments().getInt(DATA_ID);
        }
        noMoreData = false;
        currentpage = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.fragment_list, container, false);
        mLoadView = mLayoutInflater.inflate(R.layout.loading, null, false);
        mLoadingTargetView = (View) mLoadView.findViewById(R.id.player_loading_view);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(mApplication, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new PlaylistDetailAdapter(adapterList);
        recyclerView.setAdapter(mAdapter);
        mSpList = (SwipeRefreshLayout) view.findViewById(R.id.sr_push);
        mSpList.setOnPushLoadMoreListener(this);
        mSpList.setOnPullRefreshListener(this);
        return view;
    }

    @Override
    protected void onFirstUserVisible() {
        toggleShowLoading(true, null);
        if (NetworkUtils.isConnectWifi(mContext)) {
            dianBoCategoryList(currentpage);
        } else {
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleShowLoading(true, null);
                    dianBoCategoryList(currentpage);
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

    class PlaylistDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        final static int FIRST_ITEM = 0;
        final static int ITEM = 1;
        private List<DataBean> arraylist;

        public PlaylistDetailAdapter(List<DataBean> arraylist) {
            this.arraylist = arraylist;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list_item, viewGroup, false));
        }

        //判断布局类型
        @Override
        public int getItemViewType(int position) {
            return position == FIRST_ITEM ? FIRST_ITEM : ITEM;

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder itemHolder, final int i) {
            if (itemHolder instanceof ItemViewHolder) {
                final DataBean localItem = arraylist.get(i);

                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(localItem.getThumbs().getSmall_thumb()))
                        .setResizeOptions(new ResizeOptions(width, height))
                        .build();

                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setOldController(((ItemViewHolder) itemHolder).art.getController())
                        .setImageRequest(request)
                        .build();

                ((ItemViewHolder) itemHolder).art.setController(controller);
                ((ItemViewHolder) itemHolder).title.setText(localItem.getTitle());
                ((ItemViewHolder) itemHolder).subtitle.setText(localItem.getDescription());

                ((ItemViewHolder) itemHolder).itemView.setOnClickListener(view -> {
                    mListener.startPlaylistActivity(localItem);
                });
            }
        }

        @Override
        public int getItemCount() {
            return arraylist == null ? 0 : arraylist.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            protected TextView title, subtitle;
            private SimpleDraweeView art;

            public ItemViewHolder(View view) {
                super(view);
                art = (SimpleDraweeView) itemView.findViewById(R.id.viewpager_list_img);
                this.title = (TextView) view.findViewById(R.id.viewpager_list_toptext);
                this.subtitle = (TextView) view.findViewById(R.id.viewpager_list_bottom_text);
            }
        }
    }

    private void dianBoCategoryList(int page) {
        OkRequestEvents.dianBoCategoryList(mDataId, mValueId, page, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id, Response response) {
                currentpage--;
                if (page == 1) {
                    mSpList.setRefreshing(false);
                } else {
                    mSpList.setLoadMore(false);
                }

                if (call == null && e == null && id == 0) {
                    // 没有access_token
                    LogUtil.getLog().d("no token");
                    OkRequestEvents.qinTinCredential(new TokenCallBack() {
                        @Override
                        public void onResponse() {
                            dianBoCategoryList(page);
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
                    if (response != null) {
                        try {
                            ErrorModel errorModel = GsonUtil.json2Bean(response.body().string(), ErrorModel.class);
                            if (errorModel.getErrorno() == ErrorModel.TOKEN_EXPIRED || errorModel.getErrorno() == ErrorModel.TOKEN_NOT_FOUND) {
                                // Token问题
                                MainApplication.getInstance().setAccessToken(null);
                                dianBoCategoryList(page);
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        return;
                    }
                    LogUtil.getLog().d("dianBoCategoryList onError = " + e);
                    ToastUtil.showShort("网络异常");
                }
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "response = " + response);

                Gson gson = new Gson();
                mCategoryAllRadioModel = gson.fromJson(response, CategoryAllRadioModel.class);
                totalAlbumCount = mCategoryAllRadioModel.getTotal();
                Log.d(TAG, "totalAlbumCount =" + totalAlbumCount);

                if (totalAlbumCount > 0) {
                    if (page == 1) {
                        adapterList.clear();
                    }
                    for (DataBean mdata : mCategoryAllRadioModel.getData()) {
                        Log.d(TAG, "mCategoryPropertyModel dataName = " + mdata.getTitle());
                        adapterList.add(mdata);
                    }
                } else {
                    noMoreData = true;
                    ToastUtil.showShort("没有更多数据了");
                }
                mAdapter.notifyDataSetChanged();
                if (page == 1) {
                    mSpList.setRefreshing(false);
                } else {
                    mSpList.setLoadMore(false);
                }
            }
        });
    }

    @Override
    public void onLoadMore() {
        Log.d(TAG, "onLoadMore");
        if (!noMoreData) {
            currentpage++;
            dianBoCategoryList(currentpage);
        }
    }

    @Override
    public void onPushDistance(int distance) {
        Log.d(TAG, "onPushDistance");
    }

    @Override
    public void onPushEnable(boolean enable) {
        Log.d(TAG, "onPushEnable");
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh");
        currentpage = 1;
        dianBoCategoryList(currentpage);
    }

    @Override
    public void onPullDistance(int distance) {
        Log.d(TAG, "onPullDistance");
    }

    @Override
    public void onPullEnable(boolean enable) {
        Log.d(TAG, "onPullEnable");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

