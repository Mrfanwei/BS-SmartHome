package com.smartlife.qintin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.smartlife.MainApplication;
import com.smartlife.R;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.http.TokenCallBack;
import com.smartlife.qintin.adapter.CategoryDirectoryAdapter;
import com.smartlife.qintin.fragment.CategoryDirectory.CategoryListFragment;
import com.smartlife.qintin.model.CategoryAllRadioModel;
import com.smartlife.qintin.model.CategoryPropertyModel;
import com.smartlife.qintin.model.ErrorModel;
import com.smartlife.utils.CommonUtils;
import com.smartlife.utils.GsonUtil;
import com.smartlife.utils.LogUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class CategoryDirectoryActivity extends BaseActivity implements CategoryListFragment.OnFragmentInteractionListener {
    private String TAG = "SmartLifee/Category";
    private String dbcategoryname;
    private int dbcategoryid;
    private boolean isFromCache = true;
    CategoryPropertyModel mCategoryPropertyModel = null;
    MainApplication mApplicatin = null;
    ArrayList<CategoryPropertyModel.DataBean.ValuesBean> mValues;
    ArrayList<CategoryListFragment> mListFragment;
    TabLayout tabLayout;
    ViewPager mViewPager;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private int mStatusSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplicatin = (MainApplication) getApplication();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (getIntent().getExtras() != null) {
            dbcategoryname = getIntent().getStringExtra("dbcategoryname");
            dbcategoryid = getIntent().getIntExtra("dbcategoryid", -1);
        }
        setContentView(R.layout.activity_category_directory);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mStatusSize = CommonUtils.getStatusHeight(this);
        setupToolbar();

        mViewPager = (ViewPager) findViewById(R.id.vp_showpage);
        mListFragment = new ArrayList<>();

        tabLayout = (TabLayout) findViewById(R.id.tb_layout);
        dianBoCategoryDirectory();
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
        toolbar.setSubtitle(dbcategoryname);
    }

    private void dianBoCategoryDirectory() {
        OkRequestEvents.dianBoCategoryDirectory(dbcategoryid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id, String jsonString) {
                if (call == null && e == null && id == 0) {
                    // 没有access_token
                    LogUtil.getLog().d("no token");
                    OkRequestEvents.qinTinCredential(new TokenCallBack() {
                        @Override
                        public void onResponse() {
                            dianBoCategoryDirectory();
                        }

                        @Override
                        public void onError(String s) {
                            LogUtil.getLog().d("get token onError = " + s);
                        }

                        @Override
                        public void onEmpty() {
                            LogUtil.getLog().d("get token onEmpty");
                        }
                    });
                } else {
                    if (jsonString != null) {
                        ErrorModel errorModel = GsonUtil.json2Bean(jsonString, ErrorModel.class);
                        if (errorModel.getErrorno() == ErrorModel.TOKEN_EXPIRED || errorModel.getErrorno() == ErrorModel.TOKEN_NOT_FOUND) {
                            // Token问题
                            MainApplication.getInstance().setAccessToken(null);
                            dianBoCategoryDirectory();
                        }
                        return;
                    }
                    LogUtil.getLog().d("dianBoCategoryDirectory onError = " + e);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                int mStatus = 0;
                mValues = new ArrayList<>();
                List<String> mValueName = new ArrayList<>();
                int i = 0, index = 0;
                Gson gson = new Gson();
                mCategoryPropertyModel = gson.fromJson(response, CategoryPropertyModel.class);
                if (mCategoryPropertyModel.getErrorno() == 0) {
                    for (CategoryPropertyModel.DataBean mdata : mCategoryPropertyModel.getData()) {
                        Log.d(TAG, "onResponse name =" + mdata.getName());
                        if (mStatus == 0) {
                            for (CategoryPropertyModel.DataBean.ValuesBean mvalue : mdata.getValues()) {
                                tabLayout.addTab(tabLayout.newTab().setText(mvalue.getName()));
                                mListFragment.add(CategoryListFragment.newInstance(mdata.getId(), mvalue.getId(), mvalue.getName()));
                                mValueName.add(mvalue.getName());
                                mValues.add(mvalue);
                                if (mvalue.getName().equals(dbcategoryname)) {
                                    index = i;
                                }
                                i++;
                            }

                            CategoryDirectoryAdapter mCategoryDirectoryAdapter = new CategoryDirectoryAdapter(getSupportFragmentManager(), mListFragment, mValueName);
                            mViewPager.setAdapter(mCategoryDirectoryAdapter);
                            mViewPager.setOffscreenPageLimit(mListFragment.size());
                            mViewPager.setCurrentItem(index);
                            tabLayout.setupWithViewPager(mViewPager);
                            mStatus = 1;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void startPlaylistActivity(CategoryAllRadioModel.DataBean bean) {
        Intent intent = new Intent(this, PlaylistActivity.class);
        intent.putExtra("itemcount", bean.getProgram_count());
        intent.putExtra("playlistid", "1");
        intent.putExtra("recommendsTitle", bean.getTitle());
        intent.putExtra("parent_id", bean.getId());
        intent.putExtra("thumb", bean.getThumbs().getSmall_thumb());
        intent.putExtra("detailTitle", bean.getTitle());
        intent.putExtra("detailDuration", 111);
        intent.putExtra("domainUrl", mApplicatin.getDomainUrl());
        startActivity(intent);
    }
}
