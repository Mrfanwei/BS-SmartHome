package com.smartlife.qintin.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartlife.R;
import com.smartlife.qintin.fragmentnet.SearchWords;

/**
 * 描述：
 * 作者：傅健
 * 创建时间：2017/8/8 16:05
 */

public class HotWordAdapter extends RecyclerView.Adapter<HotWordAdapter.ViewHolder> {

    private String[] mStrings;

    private final SearchWords mListener;

    public HotWordAdapter(String[] strings, SearchWords listener) {
        mStrings = strings;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mStrings[position]);
    }

    @Override
    public int getItemCount() {
        return mStrings.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }

        public void setData(String hotWord) {
            mTextView.setText(hotWord);
            mTextView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onSearch(hotWord);
                }
            });
        }
    }
}
