package com.kenny.baselibrary.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kenny.baselibrary.BaseActivity;
import com.kenny.baselibrary.R;
import com.kenny.baselibrary.fragment.MainOneFragment;
import com.kenny.baselibrary.fragment.MainThreeFragment;
import com.kenny.baselibrary.fragment.MainTwoFragment;
import com.kenny.baselibrary.fragment.MainfourFragment;
import com.kenny.baselibrary.utils.crash.ExitAppUtils;
import com.kenny.baselibrary.view.ChangeColorIconWithText;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenny on 2015/6/21.
 * <p/>
 * 开发的时候此目录下有很多可用图片
 * D:\Android\androidstudio\sdk\platforms\android-17\data\res\drawable-hdpi
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private String[] mTitles = new String[]
            {"First Fragment !", "Second Fragment !", "Third Fragment !", "Fourth Fragment !"};
    private FragmentPagerAdapter mAdapter;
    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
    /**底部4个自定义view*/
    private ChangeColorIconWithText one, two, three, four;
    /**
     * 再按一次退出程序
     */
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_main);
        //初始化view
        initViews();
        //初始化toolbar
        initToolbar();
        //注册事件
        setListener();
        //
        initDatas();
        setOverflowButtonAlways();
    }

    /**
     * 初始化view
     */
    private void initViews() {
        //抽屉
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawer_layout);
        //左侧菜单
        mNavigationView = (NavigationView) findViewById(R.id.id_nv_menu);
        //viewpager部分
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);
        four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(four);

    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
    }

    private void setListener() {
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        one.setIconAlpha(1.0f);
        mViewPager.setOnPageChangeListener(this);

        mNavigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {

                    private MenuItem mPreMenuItem;

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (mPreMenuItem != null) mPreMenuItem.setChecked(false);
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        mPreMenuItem = menuItem;
                        if ("EvenBus".equals(menuItem.getTitle())) {

                            startActivity(new Intent(MainActivity.this,EvenBusActivity1.class));

                        }else if("ORM".equals(menuItem.getTitle())) {

                        }else if("JNI".equals(menuItem.getTitle())) {

                        }else if("Http".equals(menuItem.getTitle())) {

                        }else if("屏幕适配".equals(menuItem.getTitle())) {
                            startActivity(new Intent(MainActivity.this,AutoLayoutActivity.class));
                        }
                        return true;
                    }
                });
    }

    private void initDatas() {
        //通用架构
        MainOneFragment oneFragment = new MainOneFragment();
        Bundle oneBundle = new Bundle();
        oneBundle.putString(MainOneFragment.TITLE, mTitles[0]);
        oneFragment.setArguments(oneBundle);
        mTabs.add(oneFragment);
        //通讯录
        MainTwoFragment twoFragment = new MainTwoFragment();
        Bundle twoBundle = new Bundle();
        twoBundle.putString(MainTwoFragment.TITLE, mTitles[1]);
        twoFragment.setArguments(twoBundle);
        mTabs.add(twoFragment);
        //发现
        MainThreeFragment threeFragment = new MainThreeFragment();
        Bundle threeBundle = new Bundle();
        threeBundle.putString(MainThreeFragment.TITLE, mTitles[2]);
        threeFragment.setArguments(threeBundle);
        mTabs.add(threeFragment);
        //我
        MainfourFragment fourFragment = new MainfourFragment();
        Bundle fourBundle = new Bundle();
        fourBundle.putString(MainfourFragment.TITLE, mTitles[3]);
        fourFragment.setArguments(fourBundle);
        mTabs.add(fourFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }
        };
        mViewPager.setAdapter(mAdapter);
    }

    private void setOverflowButtonAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        clickTab(v);

        //如果主界面还有其它view有点击事件，则在下面代码中添加
        /*
        switch (v.getId()){
            case R.id.test:
                break;
        }*/
    }

    /**
     * 点击Tab按钮
     *
     * @param v
     */
    private void clickTab(View v) {
        resetOtherTabs();

        switch (v.getId()) {
            case R.id.id_indicator_one:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_indicator_four:
                mTabIndicators.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置menu显示icon
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // Log.e("TAG", "position = " + position + " ,positionOffset =  "
        // + positionOffset);
        if (positionOffset > 0) {
            ChangeColorIconWithText left = mTabIndicators.get(position);
            ChangeColorIconWithText right = mTabIndicators.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }
    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 状态栏透明
     */
    private void setImmersionStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏4.4及以上版本才有效果
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                ExitAppUtils.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
