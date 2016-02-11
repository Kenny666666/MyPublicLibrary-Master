package com.kenny.baselibrary.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kenny.baselibrary.LazyFragment;
import com.kenny.baselibrary.R;
import com.kenny.baselibrary.utils.common.T;

/**
 * 主界面--我
 */
public class MainfourFragment extends LazyFragment{
    private View mView;
    private String mTitle = "Default";

    public static final String TITLE = "title";
    /** 标志位，标志界面view已经初始化完成。 */
    private boolean mIsPrepared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main_four_fragment, container, false);
        //初始化view
        initView();
        //给控件设置监听
        setListener();
        //视图初始化完成加载数据
        mIsPrepared = true;
        //view加载完成，开始加载数据，如fragment无需懒加载，可不调用lazyLoad()，直接调用加载数据方法
        lazyLoad();
        return mView;
    }

    @Override
    public void initView() {
        super.initView();

    }

    @Override
    public void setListener() {
        super.setListener();
    }

    @Override
    protected void lazyLoad() {
        //只在视图可见的时候才加载数据
        if(!mIsPrepared || !mIsVisible) {
            return;
        }
        //加载数据
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }

        TextView tv = new TextView(getActivity());
        tv.setTextSize(20);
        tv.setBackgroundColor(Color.parseColor("#ffffffff"));
        tv.setText(mTitle);
        tv.setGravity(Gravity.CENTER);

        T.showShort(getActivity(),"four");



    }
}
