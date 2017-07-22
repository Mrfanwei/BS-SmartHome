package com.smartlife.qintin.fragment.CategoryDirectory;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.smartlife.R;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.qintin.activity.CategoryDirectoryActivity;
import com.smartlife.qintin.activity.PlaylistActivity;
import com.smartlife.qintin.fragment.AttachFragment;
import com.smartlife.qintin.handler.HandlerUtil;
import com.smartlife.qintin.model.CategoryAllRadioModel;
import com.smartlife.qintin.model.CategoryAllRadioModel.DataBean;
import com.smartlife.qintin.net.NetworkUtils;
import com.smartlife.qintin.widget.DividerItemDecoration;
import com.smartlife.qintin.widget.SwipeRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class CategoryListFragment extends AttachFragment {
    private String TAG = "SmartLifee/ListFr";
    private Handler mHandler;
    private PlaylistDetailAdapter mAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSpList;
    private View view;
    private boolean isFromCache = true;
    public CategoryDirectoryActivity mActivity;
    private int width = 160, height = 160;
    private int mDataId;
    private int mValueId;
    private boolean isFirstLoad = true;
    private List<DataBean> adapterList;
    private CategoryAllRadioModel mCategoryAllRadioModel;
    private int totalAlbumCount =0;
    private int currentpage;
    private boolean noMoreData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mValueId = getArguments().getInt("valueid");
        mDataId = getArguments().getInt("dataid");
        mHandler = HandlerUtil.getInstance(getActivity());
        adapterList = new ArrayList<>();
        mAdapter = new PlaylistDetailAdapter();
        noMoreData = false;
        currentpage =1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        mActivity = (CategoryDirectoryActivity)getActivity();
        view = inflater.inflate(R.layout.fragment_list, container, false);
        setList();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isFirstLoad){
            dianBoCategoryList(currentpage);
            isFirstLoad = false;
        }
    }

    private void setList() {
        mSpList = (SwipeRefreshLayout)view.findViewById(R.id.sr_push);
        mSpList.setOnPushLoadMoreListener(new SwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG,"onLoadMore");
                if(!noMoreData){
                    currentpage++;
                    dianBoCategoryList(currentpage);
                }
                mSpList.setLoadMore(false);
            }

            @Override
            public void onPushDistance(int distance) {
                Log.d(TAG,"onPushDistance");
            }

            @Override
            public void onPushEnable(boolean enable) {
                Log.d(TAG,"onPushEnable");
            }
        });

        mSpList.setOnPullRefreshListener(new SwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG,"onRefresh");
                mSpList.setRefreshing(false);
            }

            @Override
            public void onPullDistance(int distance) {
                Log.d(TAG,"onPullDistance");
            }

            @Override
            public void onPullEnable(boolean enable) {
                Log.d(TAG,"onPullEnable");
            }
        });
        recyclerView = (RecyclerView)view.findViewById(R.id.list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
    }

    class PlaylistDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        final static int FIRST_ITEM = 0;
        final static int ITEM = 1;
        private List<DataBean>  arraylist;

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

                ((ItemViewHolder) itemHolder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mActivity, PlaylistActivity.class);
                        intent.putExtra("itemcount",localItem.getProgram_count());
                        intent.putExtra("playlistid", "1");
                        intent.putExtra("recommendsTitle",localItem.getTitle());
                        intent.putExtra("parent_id",localItem.getId());
                        intent.putExtra("thumb",localItem.getThumbs().getSmall_thumb());
                        intent.putExtra("detailTitle",localItem.getTitle());
                        intent.putExtra("detailDuration",111);
                        intent.putExtra("domainUrl",mApplicatin.getDomainUrl());
                        getActivity().startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return arraylist == null ? 0 : arraylist.size();
        }

        public void updateDataSet(List<DataBean> arraylist) {
            this.arraylist = arraylist;
            this.notifyDataSetChanged();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            protected TextView title, subtitle;
            private SimpleDraweeView art;

            public ItemViewHolder(View view) {
                super(view);
                art = (SimpleDraweeView) itemView.findViewById(R.id.viewpager_list_img);
                this.title = (TextView) view.findViewById(R.id.viewpager_list_toptext);
                this.subtitle = (TextView) view.findViewById(R.id.viewpager_list_bottom_text);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

            }
        }
    }

    private void dianBoCategoryList(int page){
        OkRequestEvents.dianBoCategoryList(mApplicatin.getAccessToken(), mDataId, mValueId, page, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG,"response = "+response);

                Gson gson = new Gson();
                mCategoryAllRadioModel = gson.fromJson(response, CategoryAllRadioModel.class);
                totalAlbumCount = mCategoryAllRadioModel.getTotal();
                Log.d(TAG,"totalAlbumCount ="+totalAlbumCount);

                if(totalAlbumCount>0){
                    for(DataBean mdata:mCategoryAllRadioModel.getData()){
                        Log.d(TAG,"mCategoryPropertyModel dataName = "+ mdata.getTitle());
                        adapterList.add(mdata);
                    }
                }else{
                    noMoreData = true;
                }
                recyclerView.setAdapter(mAdapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
                mAdapter.updateDataSet(adapterList);
            }
        });
    }
}

