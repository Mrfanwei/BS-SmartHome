package com.smartlife.qintin.fragmentnet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.smartlife.MainApplication;
import com.smartlife.R;
import com.smartlife.qintin.fragment.AttachFragment;
import com.smartlife.qintin.fragment.BaseFragment;
import com.smartlife.qintin.handler.HandlerUtil;
import com.smartlife.qintin.json.ArtistInfo;
import com.smartlife.qintin.net.BMA;
import com.smartlife.qintin.net.HttpUtil;

/**
 * Created by wm on 2016/8/3.
 */
public class ArtistInfoFragment extends BaseFragment {
    FrameLayout frameLayout;
    private TextView artistInfoView, artistName;
    private String artistid;
    ArtistInfo artistInfo;
    private View v;
    private boolean firstCreate;

    public static ArtistInfoFragment getInstance(String id) {
        ArtistInfoFragment fragment = new ArtistInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.load_framelayout, container, false);
        frameLayout = (FrameLayout) view.findViewById(R.id.loadframe);
        View loadView = LayoutInflater.from(mContext).inflate(R.layout.loading, frameLayout, false);
        frameLayout.addView(loadView);

        v = LayoutInflater.from(mContext).inflate(R.layout.fragment_artistinfo, frameLayout, false);
        artistName = (TextView) v.findViewById(R.id.artist_name);
        artistInfoView = (TextView) v.findViewById(R.id.artist_info);
        if (getArguments() != null) {
            artistid = getArguments().getString("id");
        }

        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && firstCreate) {
            loadContent();
        }
        firstCreate = true;
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

    private void loadContent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JsonObject object = HttpUtil.getResposeJsonObject(BMA.Artist.artistInfo("", artistid));
                artistInfo = MainApplication.gsonInstance().fromJson(object, ArtistInfo.class);
                if (artistInfo != null && artistInfo.getAvatar_s500() != null) {
                    HandlerUtil.getInstance(mContext).post(new Runnable() {
                        @Override
                        public void run() {
                            artistName.setText(artistInfo.getName());
                            artistInfoView.setText(artistInfo.getIntro());
                            frameLayout.removeAllViews();
                            frameLayout.addView(v);

                        }
                    });
                }
            }
        }).start();
    }
}
