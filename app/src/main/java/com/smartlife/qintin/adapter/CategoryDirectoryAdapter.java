package com.smartlife.qintin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.smartlife.qintin.fragment.CategoryDirectory.CategoryListFragment;
import com.smartlife.qintin.model.CategoryPropertyModel;

import java.util.List;

/**
 * Created by Administrator on 2017/5/20.
 */

public class CategoryDirectoryAdapter extends FragmentPagerAdapter {

    private final FragmentManager mFm;
    private List<CategoryListFragment> fragments;
    private List<String> mChannels;
    private List<CategoryPropertyModel.DataBean.ValuesBean> mlist=null;

    public CategoryDirectoryAdapter(FragmentManager fm, List<CategoryListFragment> fragments, List<String> channels){
        super(fm);
        mFm = fm;
        this.fragments = fragments;
        this.mChannels = channels;
    }

    public void setData(List<CategoryPropertyModel.DataBean.ValuesBean> list){
        mlist = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return mChannels.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels == null ? "" : mChannels.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
