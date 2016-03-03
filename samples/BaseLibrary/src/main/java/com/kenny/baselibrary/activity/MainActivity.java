package com.kenny.baselibrary.activity;

import android.content.Context;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kenny.baselibrary.BaseActivity;
import com.kenny.baselibrary.R;
import com.kenny.baselibrary.fragment.MainOneFragment;
import com.kenny.baselibrary.fragment.MainThreeFragment;
import com.kenny.baselibrary.fragment.MainTwoFragment;
import com.kenny.baselibrary.fragment.MainfourFragment;
import com.kenny.baselibrary.utils.common.T;
import com.kenny.baselibrary.utils.crash.ExitAppUtils;
import com.kenny.baselibrary.view.ChangeColorIconWithText;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * description app主界面
 * Created by kenny on 2015/6/21.
 * ps:ViewPager优化方案
 *    1.Fragment轻量化（这种还是得看业务情况来）
 *    2.防止Fragment被销毁
 *     A:（在PagerAdapter里覆盖destroyItem方法可阻止销毁Fragment），通过PagerAdapter的setOffscreenPageLimit()方法可以设置保留几个Fragment，适当增大参数可防止Fragment频繁地被销毁和创建。
 *      风险：在Fragment比较多的情况下，部分低端机型容易产生OOM问题。
 *      ……额，当然不会这么简单就能解决了……%>_<%……
        测试OK是因为使用的测试数据较少，每个盘存工作薄最多也就十几页—— 在进行大数据量进行测试的问题，问题就出现了：实际使用的时候，工作簿的页数常常是好几百，一下子加载超过100页数据几乎每次都会让程序崩溃。

       B:另一个方法就是手动建立一个缓存表了。
         在本地建立一个List，用以存储页面视图，每次请求页面视图的时候，首先尝试从List中进行获取，如果获取不到再联网从后台获取数据设置视图，并将其存入到List中。

 *    3.Fragment内容延迟加载（在切换到当前Fragment的时候，并不立刻去加载Fragment的内容，而是先加载一个简单的空布局，然后启动一个延时任务，延时时长为T，当用户在该Fragment停留时间超过T时，继续执行加载任务；而当用户切换到其他Fragment，停留时间低于T，则取消该延时任务。）
 *
 *      ViewPager默认会自动加载保存当前页以及左右页的数据，比如：
 *       当前在第1页时会保存第1页和第2页的数据
 *       当前在第2页时会保存第1、2、3页的数据
 *       当前在第3页时会保存第2、3、4页的数据，(注意：第1页被回收了！！！)
 *
 * ps:开发的时候此目录下有很多可用图片
 * sdk\platforms\android-17\data\res\drawable-hdpi
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private Context mContext;
    /**左侧抽屉*/
    private DrawerLayout mDrawerLayout;
    /**左侧菜单导航*/
    private NavigationView mNavigationView;
    /**主界面viewpage+fragment风格*/
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private String[] mTitles = new String[]
            {"First Fragment !", "Second Fragment !", "Third Fragment !", "Fourth Fragment !"};
    private FragmentPagerAdapter mAdapter;
    /**底部4个自定义view*/
    private ChangeColorIconWithText one, two, three, four;
    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
    /** 再按一次退出程序 */
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        setImmersionStatus();
        setContentView(R.layout.activity_main);
        mContext = this;
        //初始化view
        initViews();
        //初始化toolbar
        initToolbar();
        //注册监听
        setListener();
        //初始化数据
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

    /**
     * 注册监听
     */
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

                            T.showShort(mContext,"工具正在开发中......");

                        }else if("JNI".equals(menuItem.getTitle())) {

                            T.showShort(mContext,"工具正在开发中......");

                        }else if("Http".equals(menuItem.getTitle())) {

                            T.showShort(mContext,"工具正在开发中......");

                        }else if("屏幕适配".equals(menuItem.getTitle())) {

                            startActivity(new Intent(MainActivity.this,AutoLayoutActivity.class));
                        }
                        return true;
                    }
                });
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        //通用架构
        MainOneFragment oneFragment = new MainOneFragment();
        mTabs.add(oneFragment);
        //通讯录
        MainTwoFragment twoFragment = new MainTwoFragment();
        mTabs.add(twoFragment);
        //发现
        MainThreeFragment threeFragment = new MainThreeFragment();
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

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //super.destroyItem(container, position, object);
            }

        };
        mViewPager.setAdapter(mAdapter);
        //缓存4个界面，防止界面被销毁
        mViewPager.setOffscreenPageLimit(4);
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
        //点击底部4个自定义view切换界面
        clickTab(v);

        //如果主界面还有其它view有点击事件，则在下面代码中添加，防止事件冲突
        /*
        switch (v.getId()){
            case R.id.test:
                break;
        }*/
    }

    /**
     * 点击Tab按钮，切换界面
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
        //在这里可以做取消各fragment中加载数据的操作 mHandler.removeCallbacks(LOAD_DATA);

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

    /**
     * 回退
     * @param keyCode
     * @param event
     * @return
     */
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
