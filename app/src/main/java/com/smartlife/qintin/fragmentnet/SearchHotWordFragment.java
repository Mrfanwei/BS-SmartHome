package com.smartlife.qintin.fragmentnet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.smartlife.MainApplication;
import com.smartlife.R;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.http.TokenCallBack;
import com.smartlife.qintin.adapter.HotWordAdapter;
import com.smartlife.qintin.adapter.RecentSearchAdapter;
import com.smartlife.qintin.fragment.AttachFragment;
import com.smartlife.qintin.model.ErrorModel;
import com.smartlife.qintin.model.SearchHotWordModel;
import com.smartlife.qintin.net.NetworkUtils;
import com.smartlife.utils.GsonUtil;
import com.smartlife.utils.LogUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class SearchHotWordFragment extends AttachFragment {
    private String TAG = "SmartLife/SearchHot";
    private String[] texts = new String[10];
    private SearchWords searchWords;
    private HotWordAdapter mAdapter;
    private RecyclerView hot_words;
    private RelativeLayout rl_word;
    private View dataView;
    private boolean isFromCache = true;

    private FrameLayout frameLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.load_framelayout, container, false);
        frameLayout = (FrameLayout) view.findViewById(R.id.loadframe);
        initView();
        loadWords();
        return view;
    }

    private void initView() {
        View loadView = LayoutInflater.from(mContext).inflate(R.layout.loading, frameLayout, false);
        frameLayout.addView(loadView);

        dataView = LayoutInflater.from(mContext).inflate(R.layout.fragment_search_hot_words, frameLayout, false);
        RecyclerView search_history = (RecyclerView) dataView.findViewById(R.id.search_history);
        search_history.setLayoutManager(new LinearLayoutManager(mContext));
        search_history.setHasFixedSize(true);
        RecentSearchAdapter adapter = new RecentSearchAdapter(mContext, searchWords);
        search_history.setAdapter(adapter);

        hot_words = (RecyclerView) dataView.findViewById(R.id.hot_words);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        hot_words.setLayoutManager(layoutManager);

        rl_word = (RelativeLayout) dataView.findViewById(R.id.rl_word);

        /*frameLayout.removeAllViews();
        frameLayout.addView(dataView);*/
    }

    private void loadWords() {
        if (NetworkUtils.isConnectInternet(mContext)) {
            isFromCache = false;
        }

        OkRequestEvents.searchHotWord(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id, String jsonString) {
                if (call == null && e == null && id == 0) {
                    // 没有access_token
                    LogUtil.getLog().d("no token");
                    OkRequestEvents.qinTinCredential(new TokenCallBack() {
                        @Override
                        public void onResponse() {
                            loadWords();
                        }

                        @Override
                        public void onError(String s) {
                            LogUtil.getLog().d("get token onError = " + s);
                            frameLayout.removeAllViews();
                            frameLayout.addView(dataView);
                        }

                        @Override
                        public void onEmpty() {
                            LogUtil.getLog().d("get token onEmpty");
                            frameLayout.removeAllViews();
                            frameLayout.addView(dataView);
                        }
                    });
                } else {
                    if (jsonString != null) {
                        ErrorModel errorModel = GsonUtil.json2Bean(jsonString, ErrorModel.class);
                        if (errorModel.getErrorno() == ErrorModel.TOKEN_EXPIRED || errorModel.getErrorno() == ErrorModel.TOKEN_NOT_FOUND) {
                            // Token问题
                            MainApplication.getInstance().setAccessToken(null);
                            loadWords();
                        }
                        return;
                    }
                    LogUtil.getLog().d("qinTinDomainCenter onError = " + e);
                    frameLayout.removeAllViews();
                    frameLayout.addView(dataView);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                int i = 0;
                SearchHotWordModel mSearchHotWordModel;
                Gson gson = new Gson();
                mSearchHotWordModel = gson.fromJson(response, SearchHotWordModel.class);
                if (mSearchHotWordModel.getErrorno() == 0) {
                    for (SearchHotWordModel.DataBean mdata : mSearchHotWordModel.getData()) {
                        texts[i] = mdata.getName();
                        i++;
                    }
                    mAdapter = new HotWordAdapter(texts, searchWords);
                    hot_words.setAdapter(mAdapter);
                }
                frameLayout.removeAllViews();
                frameLayout.addView(dataView);
                rl_word.setVisibility(View.VISIBLE);
            }
        });
    }

    public void searchWords(SearchWords searchWords) {
        this.searchWords = searchWords;
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }
}
