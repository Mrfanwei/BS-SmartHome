package com.smartlife;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.google.gson.Gson;
import com.smartlife.dlan.fragment.DlanFragment;
import com.smartlife.http.OkRequestEvents;
import com.smartlife.http.TokenCallBack;
import com.smartlife.huanxin.fragment.RobotStatusFragment;
import com.smartlife.netty.fragment.NettyFragment;
import com.smartlife.qintin.activity.BaseActivity;
import com.smartlife.qintin.activity.CategoryDirectoryActivity;
import com.smartlife.qintin.activity.NetSearchWordsActivity;
import com.smartlife.qintin.activity.PlaylistActivity;
import com.smartlife.qintin.adapter.MenuItemAdapter;
import com.smartlife.qintin.dialog.CardPickerDialog;
import com.smartlife.qintin.fragmentnet.CategoryFragment;
import com.smartlife.qintin.fragmentnet.MusicFragment;
import com.smartlife.qintin.fragmentnet.NovelFragment;
import com.smartlife.qintin.fragmentnet.SelectFragment;
import com.smartlife.qintin.model.DianBoModel;
import com.smartlife.qintin.model.DianBoRecommendModel;
import com.smartlife.qintin.model.DomainCenterModel;
import com.smartlife.qintin.model.ErrorModel;
import com.smartlife.qintin.service.MusicPlayer;
import com.smartlife.qintin.uitl.ThemeHelper;
import com.smartlife.qintin.widget.CustomViewPager;
import com.smartlife.qintin.widget.SplashScreen;
import com.smartlife.utils.GsonUtil;
import com.smartlife.utils.LogUtil;
import com.smartlife.xunfei.fragment.SpeechFragment;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends BaseActivity implements CardPickerDialog.ClickListener, MusicFragment.OnFragmentInteractionListener, CategoryFragment.OnFragmentInteractionListener,
                        SelectFragment.OnFragmentInteractionListener,NovelFragment.OnFragmentInteractionListener {
    public String TAG = "SmartLifee/MainAct";
    private ActionBar ab;
    private TextView tvSelectionBar, tvCategoryBar, tvNovelBar, tvMusicBar;
    private ImageView ivSearchBar;
    private ArrayList<TextView> tabs = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ListView mLvLeftMenu;
    private long time = 0;
    private SplashScreen splashScreen;
    SelectFragment mSelectFragment;
    CategoryFragment mCategoryFragment;
    NovelFragment mNovelFragment;
    MusicFragment mMusicFragment;

    public void onCreate(Bundle savedInstanceState) {
        //        splashScreen = new SplashScreen(this);
        //        splashScreen.show(R.drawable.art_login_bg,
        //                SplashScreen.SLIDE_LEFT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.color.background_material_light_1);

        initView();
        initData();

        //        HandlerUtil.getInstance(this).postDelayed(() -> splashScreen.removeSplashScreen(), 3000);
        // mSelectFragment.requestData();
    }

    private void initData() {
        qinTinDomainCenter();
        setUpDrawer();
    }

    private void initView() {
        tvSelectionBar = (TextView) findViewById(R.id.tv_selection_bar);
        tvCategoryBar = (TextView) findViewById(R.id.tv_category_bar);
        tvNovelBar = (TextView) findViewById(R.id.tv_novel_bar);
        tvMusicBar = (TextView) findViewById(R.id.tv_music_bar);
        ivSearchBar = (ImageView) findViewById(R.id.iv_search_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.fd);
        mLvLeftMenu = (ListView) findViewById(R.id.id_lv_left_menu);

        setToolBar();
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setTitle("");
    }

    private void qinTinDomainCenter() {

        OkRequestEvents.qinTinDomainCenter(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id, String jsonString) {
                if (call == null && e == null && id == 0) {
                    // 没有access_token
                    LogUtil.getLog().d("no token");
                    OkRequestEvents.qinTinCredential(new TokenCallBack() {
                        @Override
                        public void onResponse() {
                            qinTinDomainCenter();
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
                            LogUtil.getLog().d("token error");
                            MainApplication.getInstance().setAccessToken(null);
                            qinTinDomainCenter();
                        }
                        return;
                    }
                    LogUtil.getLog().d("qinTinDomainCenter onError = " + e);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                DomainCenterModel domainCenterModel = gson.fromJson(response, DomainCenterModel.class);
                MainApplication.getInstance().setDomainCenterModel(domainCenterModel);
                for (DomainCenterModel.DataBean.StoredaudioM4aBean.MediacentersBeanXXX ll : domainCenterModel.getData().getStoredaudio_m4a().getMediacenters()) {
                    Log.d(TAG, "mDomainCenterModel domain =" + ll.getDomain() + " name=" + ll.getName() + " protocol=" + ll.getProtocol());
                    Log.d(TAG, "mDomainCenterModel access =" + ll.getAccess());
                }
                setViewPager();
            }
        });
    }

    private void setViewPager() {
        tabs.add(tvSelectionBar);
        tabs.add(tvCategoryBar);
        tabs.add(tvMusicBar);
        tabs.add(tvNovelBar);
        final CustomViewPager customViewPager = (CustomViewPager) findViewById(R.id.main_viewpager);
        mSelectFragment = new SelectFragment();
        mCategoryFragment = new CategoryFragment();
        mMusicFragment = new MusicFragment();
        mNovelFragment = new NovelFragment();
        CustomViewPagerAdapter customViewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        customViewPagerAdapter.addFragment(mSelectFragment);
        customViewPagerAdapter.addFragment(mCategoryFragment);
        customViewPagerAdapter.addFragment(mMusicFragment);
        customViewPagerAdapter.addFragment(mNovelFragment);
        customViewPager.setAdapter(customViewPagerAdapter);
        customViewPager.setOffscreenPageLimit(3);
        customViewPager.setCurrentItem(0);
        tvSelectionBar.setSelected(true);
        ivSearchBar.setSelected(true);
        customViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvSelectionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customViewPager.setCurrentItem(0);
            }
        });
        tvCategoryBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customViewPager.setCurrentItem(1);
            }
        });
        tvMusicBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customViewPager.setCurrentItem(2);
            }
        });
        tvNovelBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customViewPager.setCurrentItem(3);
            }
        });

        ivSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, NetSearchWordsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                MainActivity.this.startActivity(intent);
            }
        });
    }


    private void setUpDrawer() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mLvLeftMenu.addHeaderView(inflater.inflate(R.layout.nav_header_main, mLvLeftMenu, false));
        mLvLeftMenu.setAdapter(new MenuItemAdapter(this));
        mLvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        drawerLayout.closeDrawers();
                        break;
                    case 2:
                        CardPickerDialog dialog = new CardPickerDialog();
                        dialog.setClickListener(MainActivity.this);
                        dialog.show(getSupportFragmentManager(), "theme");
                        drawerLayout.closeDrawers();
                        break;
                    case 3:
                        RobotStatusFragment rsragment = new RobotStatusFragment();
                        rsragment.show(getSupportFragmentManager(), "robotstatus");
                        drawerLayout.closeDrawers();
                        break;
                    case 4:
                        DlanFragment dlanfragment = new DlanFragment();
                        dlanfragment.show(getSupportFragmentManager(), "dlan");
                        drawerLayout.closeDrawers();
                        break;
                    case 5:
                        SpeechFragment speechfragment = new SpeechFragment();
                        speechfragment.show(getSupportFragmentManager(), "speech");
                        drawerLayout.closeDrawers();
                        break;
                    case 6:
                        NettyFragment nettyfragment = new NettyFragment();
                        nettyfragment.show(getSupportFragmentManager(), "netty");
                        drawerLayout.closeDrawers();
                        break;
                    case 7:
                        if (MusicPlayer.isPlaying()) {
                            MusicPlayer.playOrPause();
                        }
                        unbindService();
                        finish();
                        drawerLayout.closeDrawers();
                        break;
                }
            }
        });
    }


    private void switchTabs(int position) {
        for (int i = 0; i < tabs.size(); i++) {
            if (position == i) {
                tabs.get(i).setSelected(true);
            } else {
                tabs.get(i).setSelected(false);
            }
        }
    }

    @Override
    public void onConfirm(int currentTheme) {
        if (ThemeHelper.getTheme(MainActivity.this) != currentTheme) {
            ThemeHelper.setTheme(MainActivity.this, currentTheme);
            ThemeUtils.refreshUI(MainActivity.this, new ThemeUtils.ExtraRefreshable() {
                        @Override
                        public void refreshGlobal(Activity activity) {
                            //for global setting, just do once
                            if (Build.VERSION.SDK_INT >= 21) {
                                final MainActivity context = MainActivity.this;
                                ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null, null, ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
                                setTaskDescription(taskDescription);
                                getWindow().setStatusBarColor(ThemeUtils.getColorById(context, R.color.theme_color_primary));
                            }
                        }

                        @Override
                        public void refreshSpecificView(View view) {
                        }
                    }
            );
        }
        changeTheme();
    }

    @Override
    public void startMusicListActivity(DianBoRecommendModel.DataBean.RecommendsBean bean) {
        Intent intent = new Intent(this, PlaylistActivity.class);
        intent.putExtra("dbcategoryname", bean.getTitle());
        intent.putExtra("itemcount", 20);
        intent.putExtra("playlistid", "1");
        intent.putExtra("recommendsTitle", bean.getTitle());
        intent.putExtra("parent_id", bean.getParent_info().getParent_id());
        intent.putExtra("thumb", bean.getThumb());
        intent.putExtra("detailTitle", bean.getTitle());
        intent.putExtra("detailDuration", 111);
        startActivity(intent);
    }

    @Override
    public void startCategoryDirectoryActivity(String musicname, int musicid) {
        Intent intent = new Intent(this, CategoryDirectoryActivity.class);
        intent.putExtra("dbcategoryname", musicname);
        intent.putExtra("dbcategoryid", musicid);
        startActivity(intent);
    }

    @Override
    public void startCategoryDirectoryActivity(DianBoModel.DataBean bean) {
        Intent intent = new Intent(this, CategoryDirectoryActivity.class);
        intent.putExtra("dbcategoryname", bean.getName());
        intent.putExtra("dbcategoryid", bean.getId());
        intent.putExtra("dbcategorysectionid", bean.getSection_id());
        startActivity(intent);
    }

    @Override
    public void startPlaylistActivity(DianBoRecommendModel.DataBean.RecommendsBean bean) {
        Intent intent = new Intent(this, PlaylistActivity.class);
        intent.putExtra("itemcount", 20);
        intent.putExtra("playlistid", "1");
        intent.putExtra("playlistcount", "11");
        intent.putExtra("recommendsTitle", bean.getTitle());
        intent.putExtra("parent_id", bean.getParent_info().getParent_id());
        if (bean.getParent_info() != null) {
            intent.putExtra("parent_name", bean.getParent_info().getParent_name());
        } else {
            intent.putExtra("parent_name", "null");

        }
        intent.putExtra("thumb", bean.getThumb());
        intent.putExtra("recommendsSequence", bean.getSequence());
        intent.putExtra("detailTitle", bean.getDetail().getTitle());
        intent.putExtra("detailDuration", bean.getDetail().getDuration());
        if (bean.getDetail().getMediainfo() != null) {
            intent.putExtra("file_path", bean.getDetail().getMediainfo().getBitrates_url().get(0).getFile_path());
        }

        startActivity(intent);
    }

    static class CustomViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();

        public CustomViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case android.R.id.home: //Menu icon
                drawerLayout.openDrawer(Gravity.LEFT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        splashScreen.removeSplashScreen();
    }

    /**
     * 双击返回桌面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - time > 1000)) {
                Toast.makeText(this, "再按一次返回桌面", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //        moveTaskToBack(true);
        // System.exit(0);
        // finish();
    }
}
