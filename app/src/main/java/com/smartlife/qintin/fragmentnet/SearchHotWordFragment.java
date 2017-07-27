package com.smartlife.qintin.fragmentnet;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.smartlife.R;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.qintin.adapter.RecentSearchAdapter;
import com.smartlife.qintin.fragment.AttachFragment;
import com.smartlife.qintin.fragment.BaseFragment;
import com.smartlife.qintin.json.SearchSongInfo;
import com.smartlife.qintin.model.SearchHotWordModel;
import com.smartlife.qintin.net.BMA;
import com.smartlife.qintin.net.HttpUtil;
import com.smartlife.qintin.net.NetworkUtils;
import com.smartlife.qintin.widget.WidgetController;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

public class SearchHotWordFragment extends BaseFragment implements View.OnClickListener, SearchWords {
    private String TAG = "SmartLife/SearchHot";
    String[] texts = new String[10];
    ArrayList<TextView> views = new ArrayList<>();
    SearchWords searchWords;
    private RecentSearchAdapter adapter;
    private RecyclerView recyclerView;
    private boolean isFromCache = true;

    private List searchResults = Collections.emptyList();
    private ArrayList<SearchSongInfo> songList = new ArrayList<>();
    View loadview;
    FrameLayout frameLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.load_framelayout, container, false);
        frameLayout = (FrameLayout) view.findViewById(R.id.loadframe);
        loadview = LayoutInflater.from(mContext).inflate(R.layout.loading, frameLayout, false);
        frameLayout.addView(loadview);
        loadWordText();
        loadWords();
        return view;
    }

    private void loadWordText(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_search_hot_words, frameLayout, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        adapter = new RecentSearchAdapter(mContext);
        adapter.setListenter(SearchHotWordFragment.this);
        recyclerView.setAdapter(adapter);

        TextView text1 = (TextView) view.findViewById(R.id.text1);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        TextView text3 = (TextView) view.findViewById(R.id.text3);
        TextView text4 = (TextView) view.findViewById(R.id.text4);
        TextView text5 = (TextView) view.findViewById(R.id.text5);
        TextView text6 = (TextView) view.findViewById(R.id.text6);
        TextView text7 = (TextView) view.findViewById(R.id.text7);
        TextView text8 = (TextView) view.findViewById(R.id.text8);
        TextView text9 = (TextView) view.findViewById(R.id.text9);
        TextView text10 = (TextView) view.findViewById(R.id.text10);
        views.add(text1);
        views.add(text2);
        views.add(text3);
        views.add(text4);
        views.add(text5);
        views.add(text6);
        views.add(text7);
        views.add(text8);
        views.add(text9);
        views.add(text10);

        frameLayout.removeAllViews();
        frameLayout.addView(view);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ( mContext == null) {
                return;
            }

            int w = mContext.getResources().getDisplayMetrics().widthPixels;
            int xdistance = -1;
            int ydistance = 0;
            int distance = dip2px(mContext, 16);
            for (int i = 0; i < 10; i++) {
                views.get(i).setOnClickListener(SearchHotWordFragment.this);
                views.get(i).setText(texts[i]);
                if (xdistance == -1) {
                    xdistance = 0;
                    WidgetController.setLayout(views.get(i), xdistance, ydistance);
                    continue;
                }
                xdistance += WidgetController.getWidth(views.get(i - 1)) + distance;
                if (xdistance + WidgetController.getWidth(views.get(i)) + distance > w) {
                    xdistance = -1;
                    ydistance += 120;
                    i--;
                    continue;
                }
                WidgetController.setLayout(views.get(i), xdistance, ydistance);
            }
        }
    };

    private void loadWords() {
        if (NetworkUtils.isConnectInternet(mContext)) {
            isFromCache = false;
        }

        OkRequestEvents.searchHotWord(mApplication.getAccessToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                int i=0;
                SearchHotWordModel mSearchHotWordModel;
                Gson gson = new Gson();
                mSearchHotWordModel = gson.fromJson(response,SearchHotWordModel.class);
                if(mSearchHotWordModel.getErrorno() == 0){
                    for(SearchHotWordModel.DataBean mdata:mSearchHotWordModel.getData()){
                        texts[i] = mdata.getName();
                        i++;
                    }
                    mHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    public void searchWords(SearchWords searchWords) {
        this.searchWords = searchWords;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text1:
                searchWords.onSearch(texts[0]);
                break;
            case R.id.text2:
                searchWords.onSearch(texts[1]);
                break;
            case R.id.text3:
                searchWords.onSearch(texts[2]);
                break;
            case R.id.text4:
                searchWords.onSearch(texts[3]);
                break;
            case R.id.text5:
                searchWords.onSearch(texts[4]);
                break;
            case R.id.text6:
                searchWords.onSearch(texts[5]);
                break;
            case R.id.text7:
                searchWords.onSearch(texts[6]);
                break;
            case R.id.text8:
                searchWords.onSearch(texts[7]);
                break;
            case R.id.text9:
                searchWords.onSearch(texts[8]);
                break;
            case R.id.text10:
                searchWords.onSearch(texts[9]);
                break;
        }
    }

    @Override
    public void onSearch(String t) {
        if (searchWords != null)
            searchWords.onSearch(t);
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
