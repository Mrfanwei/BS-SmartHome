/*
 * Copyright (C) 2015 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.smartlife.qintin.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartlife.R;
import com.smartlife.qintin.fragmentnet.SearchWords;
import com.smartlife.qintin.provider.SearchHistory;

import java.util.ArrayList;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ItemHolder> {
    private Context mContext;
    private ArrayList<String> recentSearches = new ArrayList<>();
    private SearchWords searchWords;

    public RecentSearchAdapter(Activity context, SearchWords search) {
        mContext = context;
        searchWords = search;
        recentSearches = SearchHistory.getInstance(context).getRecentSearches();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_search_item, viewGroup, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemHolder itemHolder, int i) {

        itemHolder.setData(recentSearches.get(i));
        setOnPopupMenuListener(itemHolder, i);
    }

    @Override
    public int getItemCount() {
        return recentSearches.size();
    }

    private void setOnPopupMenuListener(ItemHolder itemHolder, final int position) {

        itemHolder.menu.setOnClickListener(v -> {
            SearchHistory.getInstance(mContext).deleteRecentSearches(recentSearches.get(position));
            recentSearches = SearchHistory.getInstance(mContext).getRecentSearches();
            notifyDataSetChanged();
        });
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public final TextView title;
        public final ImageView menu;

        public ItemHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            menu = (ImageView) view.findViewById(R.id.menu);
        }

        public void setData(String hotWord) {
            title.setText(hotWord);
            itemView.setOnClickListener(v -> {
                if (searchWords != null) {
                    searchWords.onSearch(hotWord);
                }
            });
        }
    }
}





