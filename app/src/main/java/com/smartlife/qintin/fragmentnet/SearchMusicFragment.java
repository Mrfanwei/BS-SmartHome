package com.smartlife.qintin.fragmentnet;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.smartlife.MainApplication;
import com.smartlife.R;
import com.smartlife.qintin.downmusic.Down;
import com.smartlife.qintin.fragment.AttachFragment;
import com.smartlife.qintin.fragment.BaseFragment;
import com.smartlife.qintin.info.MusicInfo;
import com.smartlife.qintin.json.MusicDetailInfo;
import com.smartlife.qintin.json.SearchSongInfo;
import com.smartlife.qintin.net.BMA;
import com.smartlife.qintin.net.HttpUtil;
import com.smartlife.qintin.service.MusicPlayer;
import com.smartlife.qintin.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchMusicFragment extends BaseFragment {

    private MusicAdapter mAdapter;
    private ArrayList<SearchSongInfo> songInfos;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;


    public static SearchMusicFragment newInstance(ArrayList<SearchSongInfo> list) {
        SearchMusicFragment fragment = new SearchMusicFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("searchMusic", list);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recylerview, container, false);
        if (getArguments() != null) {
            songInfos = getArguments().getParcelableArrayList("searchMusic");
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MusicAdapter(songInfos);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        return view;
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


    public class MusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        final static int FIRST_ITEM = 0;
        final static int ITEM = 1;
        private ArrayList<SearchSongInfo> mList;

        public MusicAdapter(ArrayList<SearchSongInfo> list) {
            if (list == null) {
                throw new IllegalArgumentException("model Data must not be null");
            }
            mList = list;
        }

        public void updateDataSet(ArrayList<SearchSongInfo> list) {
            this.mList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//            if (viewType == FIRST_ITEM)
//                return new CommonItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_item, viewGroup, false));

            return new ListItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_musci_common_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SearchSongInfo model = mList.get(position);
            if (holder instanceof ListItemViewHolder) {
                ((ListItemViewHolder) holder).mainTitle.setText(model.getTitle());
                ((ListItemViewHolder) holder).title.setText(model.getAlbum_title());
            }
        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() : 0);
        }

        public class ListItemViewHolder extends RecyclerView.ViewHolder {
            //ViewHolder
            ImageView moreOverflow, playState;
            TextView mainTitle, title;

            ListItemViewHolder(View view) {
                super(view);
                this.mainTitle = (TextView) view.findViewById(R.id.viewpager_list_toptext);
                this.title = (TextView) view.findViewById(R.id.viewpager_list_bottom_text);
                this.playState = (ImageView) view.findViewById(R.id.play_state);
                this.moreOverflow = (ImageView) view.findViewById(R.id.viewpager_list_button);

                moreOverflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final SearchSongInfo model = mList.get(getAdapterPosition());
                        new AlertDialog.Builder(mContext).setTitle("要下载音乐吗").
                                setPositiveButton(mContext.getString(R.string.sure), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Down.downMusic(MainApplication.context, model.getSong_id() + "", model.getTitle(), model.getAuthor());
                                        dialog.dismiss();
                                    }
                                }).
                                setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final SearchSongInfo model = mList.get(getAdapterPosition());
                        new AsyncTask<Void,Void,Void>(){

                            @Override
                            protected Void doInBackground(Void... params) {
                                MusicInfo musicInfo = new MusicInfo();
                                try {
                                    MusicDetailInfo info = null;
                                    JsonObject jsonObject = HttpUtil.getResposeJsonObject(BMA.Song.songBaseInfo(model.getSong_id()))
                                            .get("result").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject();
                                    info = MainApplication.gsonInstance().fromJson(jsonObject, MusicDetailInfo.class);
                                    musicInfo.albumData = info.getPic_small();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }


                                musicInfo.songId = Integer.parseInt(model.getSong_id());
                                musicInfo.musicName = model.getTitle();
//                                musicInfo.artist = model.getAuthor();
                                musicInfo.islocal = false;
                                musicInfo.albumName = model.getAlbum_title();
                                musicInfo.albumId = Integer.parseInt(model.getAlbum_id());
//                                musicInfo.artistId = Integer.parseInt(model.getArtist_id());
//                                musicInfo.lrc = model.getLrclink();

                                HashMap<Long, MusicInfo> infos = new HashMap<Long, MusicInfo>();
                                long[] list = new long[1];
                                list[0] = musicInfo.songId;
                                infos.put(list[0], musicInfo);
                                MusicPlayer.playAll(infos, list, 0, false);
                                return null;
                            }
                        }.execute();
                    }
                });
            }

        }
    }

}
