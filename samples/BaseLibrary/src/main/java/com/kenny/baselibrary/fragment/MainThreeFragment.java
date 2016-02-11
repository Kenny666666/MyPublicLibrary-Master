package com.kenny.baselibrary.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kenny.baselibrary.LazyFragment;
import com.kenny.baselibrary.R;
import com.kenny.baselibrary.adapter.RefreshFootAdapter;
import com.kenny.baselibrary.utils.common.T;
import com.kenny.baselibrary.view.AdvanceDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面--发现
 */
public class MainThreeFragment extends LazyFragment{
    private View mView;
    private String mTitle = "Default";

    public static final String TITLE = "title";
    /** 标志位，标志界面view已经初始化完成。 */
    private boolean mIsPrepared;
    private SwipeRefreshLayout demo_swiperefreshlayout;
    private RecyclerView demo_recycler;
    private RefreshFootAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main_three_fragment, container, false);
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
        demo_swiperefreshlayout=(SwipeRefreshLayout)mView.findViewById(R.id.demo_swiperefreshlayout);
        demo_recycler=(RecyclerView)mView.findViewById(R.id.demo_recycler);
        //设置刷新时动画的颜色，可以设置4个
        demo_swiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        demo_swiperefreshlayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        demo_swiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        demo_recycler.setLayoutManager(linearLayoutManager);
        //添加分隔线
        demo_recycler.addItemDecoration(new AdvanceDecoration(getActivity(), OrientationHelper.VERTICAL));
        demo_recycler.setAdapter(adapter = new RefreshFootAdapter(getActivity()));
        demo_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("zttjiangqq", "invoke onRefresh...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> newDatas = new ArrayList<String>();
                        for (int i = 0; i < 5; i++) {
                            int index = i + 1;
                            newDatas.add("new item" + index);
                        }
                        adapter.addItem(newDatas);
                        demo_swiperefreshlayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "更新了五条数据...", Toast.LENGTH_SHORT).show();
                    }
                }, 5000);
            }
        });
        //RecyclerView滑动监听
        demo_recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    adapter.changeMoreStatus(RefreshFootAdapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<String> newDatas = new ArrayList<String>();
                            for (int i = 0; i < 5; i++) {
                                int index = i + 1;
                                newDatas.add("more item" + index);
                            }
                            adapter.addMoreItem(newDatas);
                            adapter.changeMoreStatus(RefreshFootAdapter.PULLUP_LOAD_MORE);
                        }
                    }, 2500);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
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

        T.showShort(getActivity(),"three");



    }
}
