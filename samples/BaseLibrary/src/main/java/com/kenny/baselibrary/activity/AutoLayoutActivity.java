package com.kenny.baselibrary.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.kenny.baselibrary.BaseActivity;
import com.kenny.baselibrary.R;
import com.kenny.baselibrary.fragment.ListFragment;
import com.kenny.baselibrary.fragment.PayFragment;
import com.kenny.baselibrary.fragment.RegisterFragment;

import java.util.ArrayList;


/**
 * 适配原理 AutoLayoutActivity(至今为止最强适配方案)
 *
   那么首先说一下：这个1px并不代表1像素，我在内部会进行百分比化处理，也就是说：720px高度的屏幕，你这里填写72px，占据10%；当这个布局文件运行在任何分辨率的手机上，这个72px都代表10%的高度，这就是本库适配的原理。
   重点在各布局(AutoFrameLayout、AutoLinearLayout、AutoRelativeLayout)的onMeasure()方法中
   通过获取moduleAndroidManifest.xml配置的
    <meta-data android:name="design_width" android:value="768"></meta-data>
    <meta-data android:name="design_height" android:value="1280"></meta-data>
    以这个为标准，获取设备分辨率来计算百分比，最终适配

   用法
  （1）注册设计图尺寸,将autolayout引入项目
   dependencies {
    compile project(':autolayout')
   }
   在你的项目的AndroidManifest中注明你的设计稿的尺寸。
   <meta-data android:name="design_width" android:value="768"></meta-data>
   <meta-data android:name="design_height" android:value="1280"></meta-data>

  （2）Activity中开启设配
   让你的Activity去继承AutoLayoutActivity

   ok，上面是最简单的用法，当然你也可以不去继承AutoLayoutActivity来使用。

   AutoLayoutActivity的用法实际上是完成了一件事：

   LinearLayout -> AutoLinearLayout
   RelativeLayout -> AutoRelativeLayout
   FrameLayout -> AutoFrameLayout
   如果你不想继承AutoLayoutActivity，那么你就得像Google的百分比库一样，去用AutoXXXLayout代替系统原有的XXXLayout。当然，你可以放心的是，所有的系统属性原有的属性都会支持，不过根布局上就不支持px的自动百分比化了，但是一般根布局都是MATCH_PARENT，而上述的方式，根布局也是可以直接px的百分比化的。

 * @author kenny
 * @time 2016/1/31 22:25
 */
public class AutoLayoutActivity extends BaseActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.auto_layout);

        initView();
        initDatas();
    }

    private void initDatas() {
        ArrayList<Fragment> mList = new ArrayList<Fragment>();
        mList.add(new ListFragment());
        mList.add(new RegisterFragment());
        mList.add(new PayFragment());
        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), mList));
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> tabs = null;

        public MyAdapter(FragmentManager fm, ArrayList<Fragment> tabs) {
            super(fm);
            this.tabs = tabs;
        }

        @Override
        public Fragment getItem(int pos) {
            return tabs.get(pos);
        }

        @Override
        public int getCount() {
            return tabs.size();
        }
    }
}
