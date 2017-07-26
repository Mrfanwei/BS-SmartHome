package com.smartlife.qintin.fragmentnet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartlife.MainApplication;
import com.smartlife.R;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.qintin.fragment.AttachFragment;
import com.smartlife.qintin.json.SearchAlbumInfo;
import com.smartlife.qintin.json.SearchArtistInfo;
import com.smartlife.qintin.json.SearchSongInfo;
import com.smartlife.qintin.model.DianBoSearchModel;
import com.smartlife.qintin.net.BMA;
import com.smartlife.qintin.net.HttpUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

public class SearchTabPagerFragment extends AttachFragment {

    private ViewPager viewPager;
    private int page = 0;
    String key;
    private List searchResults = Collections.emptyList();
    FrameLayout frameLayout;
    View contentView;
    ArrayList<SearchSongInfo> songResults = new ArrayList<>();
    ArrayList<SearchAlbumInfo> albumResults = new ArrayList<>();


    public static final SearchTabPagerFragment newInstance(int page, String key) {
        SearchTabPagerFragment f = new SearchTabPagerFragment();
        Bundle bdl = new Bundle(1);
        bdl.putInt("page_number", page);
        bdl.putString("key", key);
        f.setArguments(bdl);
        return f;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (mContext == null) {
                        return;
                    }
                    contentView = LayoutInflater.from(mContext).inflate(R.layout.fragment_net_tab, frameLayout, false);
                    viewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
                    if (viewPager != null) {
                        Adapter adapter = new Adapter(getChildFragmentManager());
                        adapter.addFragment(SearchMusicFragment.newInstance(songResults), "单曲");
                        adapter.addFragment(SearchAlbumFragment.newInstance(albumResults), "专辑");
                        viewPager.setAdapter(adapter);
                        viewPager.setOffscreenPageLimit(2);
                    }

                    TabLayout tabLayout = (TabLayout) contentView.findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(viewPager);
                    viewPager.setCurrentItem(page);
                    tabLayout.setTabTextColors(R.color.text_color, ThemeUtils.getThemeColorStateList(mContext, R.color.theme_color_primary).getDefaultColor());
                    tabLayout.setSelectedTabIndicatorColor(ThemeUtils.getThemeColorStateList(mContext, R.color.theme_color_primary).getDefaultColor());
                    frameLayout.removeAllViews();
                    frameLayout.addView(contentView);
                    break;
            }
        }
    };

    private void search(final String key){
        OkRequestEvents.searchRadio(mApplication.getAccessToken(), key, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                DianBoSearchModel mDianBoSearchModel;
                Gson gson = new Gson();
                mDianBoSearchModel = gson.fromJson(response,DianBoSearchModel.class);
                if(mDianBoSearchModel.getErrorno() == 0){
                    for(DianBoSearchModel.DataBean mdata:mDianBoSearchModel.getData()){
                        for(DianBoSearchModel.DataBean.DoclistBean.DocsBean mdoc:mdata.getDoclist().getDocs()){

                            SearchSongInfo songInfo = new SearchSongInfo();
                            songInfo.setSong_id(Integer.toString((int)mdoc.getId()));
                            songInfo.setTitle(mdoc.getTitle());
                            songInfo.setAlbum_title(mdoc.getParent_name());
                            songInfo.setAlbum_id(Integer.toString(mdoc.getParent_id()));
                            songResults.add(songInfo);

                            SearchAlbumInfo albumInfo = new SearchAlbumInfo();
                            albumInfo.setTitle(mdoc.getTitle());
                            albumInfo.setAuthor(mdoc.getParent_name());
                            albumInfo.setPic_small(mdoc.getCover());
                            albumResults.add(albumInfo);
                        }
                    }
                    mHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    private void search1(final String key) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    JsonObject jsonObject = HttpUtil.getResposeJsonObject(BMA.Search.searchMerge(key, 1, 10)).get("result").getAsJsonObject();
                    JsonObject songObject = jsonObject.get("song_info").getAsJsonObject();
                    JsonArray songArray = songObject.get("song_list").getAsJsonArray();
                    for (JsonElement o : songArray) {
                        SearchSongInfo songInfo = MainApplication.gsonInstance().fromJson(o, SearchSongInfo.class);
                        Log.e("songinfo", songInfo.getTitle());
                        songResults.add(songInfo);
                    }

                    JsonObject albumObject = jsonObject.get("album_info").getAsJsonObject();
                    JsonArray albumArray = albumObject.get("album_list").getAsJsonArray();
                    for (JsonElement o : albumArray) {
                        SearchAlbumInfo albumInfo = MainApplication.gsonInstance().fromJson(o, SearchAlbumInfo.class);
                        albumResults.add(albumInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);


            }
        }.execute();
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.load_framelayout, container, false);
        frameLayout = (FrameLayout) rootView.findViewById(R.id.loadframe);
        View loadview = LayoutInflater.from(mContext).inflate(R.layout.loading, frameLayout, false);
        frameLayout.addView(loadview);


        if (getArguments() != null) {
            key = getArguments().getString("key");
        }
        search(key);

        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}

