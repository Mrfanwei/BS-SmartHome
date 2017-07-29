package com.smartlife.qintin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bilibili.magicasakura.widgets.TintImageView;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;
import com.nineoldandroids.view.ViewHelper;
import com.smartlife.MainApplication;
import com.smartlife.R;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.qintin.dialog.LoadAllDownInfos;
import com.smartlife.qintin.fragment.MoreFragment;
import com.smartlife.qintin.fragment.NetMoreFragment;
import com.smartlife.qintin.handler.HandlerUtil;
import com.smartlife.qintin.info.MusicInfo;
import com.smartlife.qintin.model.DianBoProgram;
import com.smartlife.qintin.json.MusicDetailInfo;
import com.smartlife.qintin.net.NetworkUtils;
import com.smartlife.qintin.provider.PlaylistInfo;
import com.smartlife.qintin.provider.PlaylistsManager;
import com.smartlife.qintin.service.MusicPlayer;
import com.smartlife.qintin.widget.SwipeRefreshLayout;
import com.smartlife.utils.CommonUtils;
import com.smartlife.qintin.uitl.IConstants;
import com.smartlife.qintin.uitl.ImageUtils;
import com.smartlife.qintin.uitl.L;
import com.smartlife.qintin.widget.DividerItemDecoration;
import com.smartlife.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class PlaylistActivity extends BaseActivity implements ObservableScrollViewCallbacks,SwipeRefreshLayout.OnPushLoadMoreListener, SwipeRefreshLayout.OnPullRefreshListener {
    private String TAG = "SmartLifee/Playlist";
    private String playlsitId;
    private int playParentId,playRecommendsSequence,playDetailDuration;
    private String playParentName,playThumb,playRecommendsTitle,playDetailTitle;
    private ArrayList<DianBoProgram.DataBean> mList = new ArrayList<>();
    private ArrayList<MusicInfo> adapterList = new ArrayList<>();
    private SimpleDraweeView albumArtSmall;
    private ImageView albumArt;
    private TextView playlistTitleView, playlistDetailView;
    private boolean isLocalPlaylist;
    private PlaylistDetailAdapter mAdapter;
    private Toolbar toolbar;
    private SparseArray<MusicDetailInfo> sparseArray = new SparseArray<>();
    private FrameLayout loadFrameLayout;
    private Handler mHandler;
    private View loadView;
    private int mFlexibleSpaceImageHeight;
    private ActionBar actionBar;
    private int mActionBarSize;
    private int mStatusSize;
    private TextView tryAgain;
    private FrameLayout headerViewContent; //上部header
    private RelativeLayout headerDetail; //上部header信息
    private Context mContext;
    private boolean mCollected;
    private TextView collectText;
    private ImageView collectView;
    private FrameLayout favLayout;
    private LinearLayout share;
    private PlayListTask mCollectList;
    private ObservableRecyclerView recyclerView;
    private boolean d = true;
    HashMap<Long, MusicInfo> itemInfos;
    long[] itemlist;
    private String mDomainUrl;
    private int currentpage;
    private boolean noMoreData;
    private SwipeRefreshLayout mSpList;

    private DianBoProgram mDianBoProgramModel=null;
    MainApplication mApplicatin=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mApplicatin = (MainApplication)getApplication();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        currentpage =1;
        noMoreData = false;
        adapterList.clear();
        if (getIntent().getExtras() != null) {
            isLocalPlaylist = false;
            playlsitId = getIntent().getStringExtra("playlistid");
            playParentId = getIntent().getIntExtra("parent_id",-1);
            playParentName = getIntent().getStringExtra("parent_name");
            playThumb = getIntent().getStringExtra("thumb");
            playRecommendsTitle = getIntent().getStringExtra("recommendsTitle");
            playRecommendsSequence = getIntent().getIntExtra("recommendsSequence",-1);
            playDetailTitle = getIntent().getStringExtra("detailTitle");
            playDetailDuration = getIntent().getIntExtra("detailDuration",-1);
        }
        mContext = this;
        setContentView(R.layout.activity_playlist);
        loadFrameLayout = (FrameLayout) findViewById(R.id.state_container);
        headerViewContent = (FrameLayout) findViewById(R.id.headerview);
        headerDetail = (RelativeLayout) findViewById(R.id.headerdetail);
        favLayout = (FrameLayout) findViewById(R.id.playlist_fav);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mHandler = HandlerUtil.getInstance(this);
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = CommonUtils.getActionBarHeight(this);
        mStatusSize = CommonUtils.getStatusHeight(this);
        tryAgain = (TextView) findViewById(R.id.try_again);
        collectText = (TextView) findViewById(R.id.playlist_collect_state);
        collectView = (ImageView) findViewById(R.id.playlist_collect_view);
        share = (LinearLayout) findViewById(R.id.playlist_share);
        setUpEverything();
        itemInfos = new HashMap<>();
        mDomainUrl = ((MainApplication)getApplication()).getDomainUrl();
    }

    private void setUpEverything() {
        setupToolbar();
        setHeaderView();
        setAlbumart();
        setList();
        loadAllLists();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.actionbar_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("专辑");
        toolbar.setPadding(0, mStatusSize, 0, 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (!isLocalPlaylist) {
            toolbar.setSubtitle(playRecommendsTitle);
        }
    }

    private void setHeaderView() {
        albumArt = (ImageView) findViewById(R.id.album_art);
        playlistTitleView = (TextView) findViewById(R.id.album_title);
        playlistDetailView = (TextView) findViewById(R.id.album_details);
        albumArtSmall = (SimpleDraweeView) findViewById(R.id.playlist_art);
        SpannableString spanString;
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.index_icn_earphone);
        ImageSpan imgSpan = new ImageSpan(this, b, ImageSpan.ALIGN_BASELINE);
        spanString = new SpannableString("icon");
        spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        LinearLayout downAll = (LinearLayout) headerViewContent.findViewById(R.id.playlist_down);
        downAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadAllDownInfos((Activity) mContext, mList).execute();
            }
        });
        final LinearLayout addToplaylist = (LinearLayout) headerViewContent.findViewById(R.id.playlist_collect);
        addToplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCollected) {
                    collectText.setText("已收藏");
                    if (mCollectList == null || mCollectList.getStatus().equals(AsyncTask.Status.FINISHED)) {
                        mCollectList = new PlayListTask();
                        mCollectList.execute(0, 0, 1);
                    }
                } else {
                    collectText.setText("收藏");
                    PlaylistInfo.getInstance(mContext).deletePlaylist(Long.parseLong(playlsitId));
                    mCollected = false;
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("http://music.baidu.com/songlist/" + playlsitId));
                shareIntent.setType("html/*");
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.shared_to)));
            }
        });

        if (!isLocalPlaylist)
            headerDetail.setVisibility(View.GONE);

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAllLists();
            }
        });

        if(Integer.parseInt(playlsitId) == IConstants.FAV_PLAYLIST){
            favLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setList() {
        recyclerView = (ObservableRecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setScrollViewCallbacks(PlaylistActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(PlaylistActivity.this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new PlaylistDetailAdapter(PlaylistActivity.this, adapterList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(PlaylistActivity.this, DividerItemDecoration.VERTICAL_LIST));
    }


    protected void updateViews(int scrollY, boolean animated) {
        ViewHelper.setTranslationY(headerViewContent, getHeaderTranslationY(scrollY));
    }

    protected float getHeaderTranslationY(int scrollY) {
        final int headerHeight = headerViewContent.getHeight();
        int headerTranslationY = mActionBarSize + mStatusSize - headerHeight;
        if (mActionBarSize + mStatusSize <= -scrollY + headerHeight) {
            headerTranslationY = -scrollY;
        }
        return headerTranslationY;
    }
    

    private void loadAllLists() {
        if (NetworkUtils.isConnectInternet(this)) {
            tryAgain.setVisibility(View.GONE);
            loadView = LayoutInflater.from(this).inflate(R.layout.loading, loadFrameLayout, false);
            loadFrameLayout.addView(loadView);
            dianBoPlayList(currentpage);
        } else {
            tryAgain.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void updateTrack() {
       mAdapter.notifyDataSetChanged();
    }

    Runnable showInfo = new Runnable() {
        @Override
        public void run() {
            playlistDetailView.setText(playRecommendsTitle);
            headerDetail.setVisibility(View.VISIBLE);
            if (mCollected) {
                L.D(d, TAG, "collected");
                collectText.setText("已收藏");
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void setAlbumart() {
        playlistTitleView.setText(playParentName);
        if(playThumb == null){
            albumArtSmall.setImageResource(R.drawable.placeholder_disk_210);
        }else {
            albumArtSmall.setImageURI(Uri.parse(playThumb));
        }

        try {

            if (isLocalPlaylist && !URLUtil.isNetworkUrl(playThumb)) {
                new setBlurredAlbumArt().execute(ImageUtils.getArtworkQuick(PlaylistActivity.this, Uri.parse(playThumb), 300, 300));
                L.D(d, TAG, "playThumb = " + playThumb);
            } else {
                //drawable = Drawable.createFromStream( new URL(playThumb).openStream(),"src");
                ImageRequest imageRequest = ImageRequest.fromUri(playThumb);
                CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                        .getEncodedCacheKey(imageRequest);
                BinaryResource resource = ImagePipelineFactory.getInstance()
                        .getMainDiskStorageCache().getResource(cacheKey);
                File file = ((FileBinaryResource) resource).getFile();
                if (file != null)
                    new setBlurredAlbumArt().execute(ImageUtils.getArtworkQuick(file, 300, 300));
            }

        } catch (Exception e) {
              e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh");
        currentpage = 1;
        dianBoPlayList(currentpage);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

    @Override
    public void onLoadMore() {
        if (!noMoreData) {
            currentpage++;
            dianBoPlayList(currentpage);
        }
    }

    @Override
    public void onPushDistance(int distance) {

    }

    @Override
    public void onPushEnable(boolean enable) {

    }


    private class setBlurredAlbumArt extends AsyncTask<Bitmap, Void, Drawable> {

        @Override
        protected Drawable doInBackground(Bitmap... loadedImage) {
            Drawable drawable = null;

            try {
                drawable = ImageUtils.createBlurredImageFromBitmap(loadedImage[0], PlaylistActivity.this, 20);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                if (albumArt.getDrawable() != null) {
                    final TransitionDrawable td =
                            new TransitionDrawable(new Drawable[]{
                                    albumArt.getDrawable(),
                                    result
                            });
                    albumArt.setImageDrawable(td);
                    td.startTransition(200);

                } else {
                    albumArt.setImageDrawable(result);
                }
            }
        }
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        updateViews(scrollY, false);

        if (scrollY > 0 && scrollY < mFlexibleSpaceImageHeight - mActionBarSize - mStatusSize) {
            toolbar.setTitle(playParentName);
            toolbar.setSubtitle(playRecommendsTitle);
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_background));
        }
        if (scrollY == 0) {
            actionBar.setBackgroundDrawable(null);
        }

        float a = (float) scrollY / (mFlexibleSpaceImageHeight - mActionBarSize - mStatusSize);
        headerDetail.setAlpha(1f - a);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.playlit_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    class PlaylistDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        final static int FIRST_ITEM = 0;
        final static int ITEM = 1;
        private ArrayList<MusicInfo> arraylist;
        private Activity mContext;
        private int itemcount=0;

        public PlaylistDetailAdapter(Activity context, ArrayList<MusicInfo> mList) {
            this.arraylist = mList;
            this.mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            if (viewType == FIRST_ITEM) {
                return new CommonItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.header_common_item, viewGroup, false));
            } else {
                return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_playlist_detail_item, viewGroup, false));
            }
        }

        //判断布局类型
        @Override
        public int getItemViewType(int position) {
            return position == FIRST_ITEM ? FIRST_ITEM : ITEM;

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder itemHolder, final int i) {
            int htime=0,mtime=0,stime=0;
            if (itemHolder instanceof ItemViewHolder) {
                final MusicInfo localItem = arraylist.get(i - 1);

                //判断该条目音乐是否在播放
                if (MusicPlayer.getCurrentAudioId() == localItem.songId) {
                    ((ItemViewHolder) itemHolder).playState.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) itemHolder).playState.setImageResource(R.drawable.song_play_icon);
                    ((ItemViewHolder) itemHolder).playState.setImageTintList(R.color.theme_color_primary);
                } else {
                    ((ItemViewHolder) itemHolder).playState.setVisibility(View.GONE);
                }

                ((ItemViewHolder) itemHolder).tvTitle.setText(localItem.musicName);
                ((ItemViewHolder) itemHolder).tvUpdateTime.setText(localItem.updataTime);
                ((ItemViewHolder) itemHolder).tvPlayCount.setText(localItem.playCount);

                if(localItem.duration>60){
                    stime = localItem.duration%60;
                    mtime = localItem.duration/60;
                    if(mtime>60){
                        htime = mtime/60;
                        mtime = mtime%60;
                    }
                }else{
                    stime = localItem.duration;
                }

                if(mtime == 0){
                    ((ItemViewHolder) itemHolder).tvDuration.setText(Integer.toString(stime));
                }else if(htime == 0){
                    ((ItemViewHolder) itemHolder).tvDuration.setText(Integer.toString(mtime) +":" +Integer.toString(stime));
                }else{
                    ((ItemViewHolder) itemHolder).tvDuration.setText(Integer.toString(htime) +":" +Integer.toString(mtime) +":" +Integer.toString(stime));
                }

                ((ItemViewHolder) itemHolder).menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (localItem.islocal) {
                            MoreFragment morefragment = MoreFragment.newInstance(arraylist.get(i - 1),
                                    IConstants.MUSICOVERFLOW);
                            morefragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "music");
                        } else {
                            NetMoreFragment morefragment = NetMoreFragment.newInstance(arraylist.get(i - 1),
                                    IConstants.MUSICOVERFLOW);
                            morefragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "music");
                        }
                    }
                });

            } else if (itemHolder instanceof CommonItemViewHolder) {

                ((CommonItemViewHolder) itemHolder).textView.setText("(共" + itemcount + "期)");

                ((CommonItemViewHolder) itemHolder).select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            return arraylist == null ? 0 : arraylist.size() + 1;
        }

        public void updateDataSet(ArrayList<MusicInfo> arraylist,int count) {
            itemcount = count;
            this.arraylist = arraylist;
            this.notifyDataSetChanged();
        }

        public class CommonItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
            ImageView select;
            RelativeLayout layout;

            CommonItemViewHolder(View view) {
                super(view);
                this.textView = (TextView) view.findViewById(R.id.play_all_number);
                this.select = (ImageView) view.findViewById(R.id.select);
                this.layout = (RelativeLayout) view.findViewById(R.id.play_all_layout);
                layout.setOnClickListener(this);
            }

            public void onClick(View v) {
             mHandler.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     HashMap<Long, MusicInfo> infos = new HashMap<Long, MusicInfo>();
                     int len = arraylist.size();
                     long[] list = new long[len];
                     for (int i = 0; i < len; i++) {
                         MusicInfo info = arraylist.get(i);
                         list[i] = info.songId;
                         infos.put(list[i], info);
                     }
                     if (getAdapterPosition() > -1)
                         MusicPlayer.playAll(infos,null, 0, false);
                 }
             },70);

            }

        }

        public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            protected TextView tvTitle,tvUpdateTime,tvPlayCount,tvDuration;
            protected ImageView menu;
            TintImageView playState;

            public ItemViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvUpdateTime = (TextView) view.findViewById(R.id.tv_update_time);
                tvPlayCount = (TextView)view.findViewById(R.id.tv_play_count);
                tvDuration = (TextView) view.findViewById(R.id.tv_duration);
                this.menu = (ImageView) view.findViewById(R.id.popup_menu);
                this.playState = (TintImageView) view.findViewById(R.id.play_state);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final int position = getAdapterPosition();
                        if (position > 0){
                            MusicPlayer.setQueuePosition(position-1);
                        }
                    }
                },20);
            }

        }
    }

    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    loadFrameLayout.removeAllViews();
                    recyclerView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private void dianBoPlayList(int page){
        Log.d(TAG,"parentff dianBoPlayList playParentId ="+playParentId);
        OkRequestEvents.dianBoPlayList(mApplicatin.getAccessToken(), playParentId, page, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG,"dianBoPlayList onError"+e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG,"response PlayListTask = "+response);
                int count;
                itemInfos.clear();
                Gson gson = new Gson();
                mDianBoProgramModel = gson.fromJson(response,DianBoProgram.class);
                count = mDianBoProgramModel.getTotal();
                if(mDianBoProgramModel.getErrorno() == 0 && count>0){
                    if(page*15 < count ){
                        //itemlist = new long[page * 15];
                    }else{
                        //itemlist = new long[count];
                        noMoreData = true;
                    }

                    mHandler.post(showInfo);
                    for(DianBoProgram.DataBean mdata:mDianBoProgramModel.getData()){
                        mList.add(mdata);
                    }
                    for(DianBoProgram.DataBean mData:mList){
                        MusicInfo musicInfo = new MusicInfo();
                        musicInfo.updataTime = mData.getUpdate_time();
                        musicInfo.playCount = mData.getPlaycount();
                        musicInfo.duration = mData.getDuration();
                        musicInfo.songId = mData.getId();
                        musicInfo.musicName = mData.getTitle();
                        musicInfo.artist = mData.getDescription();
                        musicInfo.islocal = false;
                        musicInfo.albumName = playParentName;
                        musicInfo.albumId = playParentId;
                        musicInfo.artistId = mData.getId();
                        musicInfo.lrc = "1";
                        musicInfo.albumData = playThumb;
                        musicInfo.filepath = mData.getMediainfo().getBitrates_url().get(0).getFile_path();
                        musicInfo.url = "http://"+mDomainUrl+"/"+mData.getMediainfo().getBitrates_url().get(0).getFile_path()+"/"+mData.getId()+".mp3"+"?"+"deviceid=00002000-6822-8da4-ffff-ffffca74";
                        itemInfos.put((long)mData.getId(),musicInfo);
                        adapterList.add(musicInfo);
                    }
                    if(adapterList.size()>0){
                        itemlist = new long[adapterList.size()];
                        for(int i=0;i<adapterList.size();i++){
                            itemlist[i] = adapterList.get(i).songId;
                        }
                        mAdapter.updateDataSet(adapterList,count);
                    }
                    MusicPlayer.musicLoad(itemInfos,itemlist,false);
                    myHandler.sendEmptyMessage(1);
                }else{
                    ToastUtil.showShort("无数据");
                    noMoreData = true;
                }

//                if (page == 1) {
//                    mSpList.setRefreshing(false);
//                } else {
//                    mSpList.setLoadMore(false);
//                }
            }
        });
    }

    class PlayListTask extends AsyncTask<Integer,Integer,Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            int result=0;

            switch (integers[2]) {
                case 1:
                    result =1;
                    String albumart = null;
                    for (MusicInfo info : adapterList) {
                        albumart = info.albumData;
                        if (!TextUtils.isEmpty(albumart)) {
                            break;
                        }
                    }
                    PlaylistInfo.getInstance(mContext).addPlaylist(Long.parseLong(playlsitId), playParentName,
                            adapterList.size(), albumart, "net");
                    PlaylistsManager.getInstance(mContext).insertLists(mContext, Long.parseLong(playlsitId), adapterList);
                    Intent intent = new Intent(IConstants.PLAYLIST_COUNT_CHANGED);
                    MainApplication.context.sendBroadcast(intent);

                    mCollected = true;
                    break;
            }
            return result;
        }
    }
}
