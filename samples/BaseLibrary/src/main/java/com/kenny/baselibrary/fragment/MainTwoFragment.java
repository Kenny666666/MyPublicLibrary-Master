package com.kenny.baselibrary.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kenny.baselibrary.LazyFragment;
import com.kenny.baselibrary.R;
import com.kenny.baselibrary.adapter.RefreshRecyclerAdapter;
import com.kenny.baselibrary.view.AdvanceDecoration;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面--通讯录（本界面展示RecyclerView下拉刷新案例）
 * Created by kenny on 2015/12/21.
 */
public class MainTwoFragment extends LazyFragment{

    private final String TAG = MainTwoFragment.this.getClass().getName();
    private View mView;

    /**
     * 标志位，标志界面view已经初始化完成。
     */
    private boolean mIsPrepared;
    /**
     * RecyclerView
     */
    private RecyclerView mRecyclerView;
    /**
     * RecyclerView适配器
     */
    private RefreshRecyclerAdapter mAdapter;
    /**
     * RecyclerView适配器数据
     */
    private List<String> mTitles;
    private SwipeRefreshLayout mSwiperefreshlayout;
    private LinearLayoutManager mLinearLayoutManager;
    private int mLastVisibleItem;
    /**
     * handler
     */
    private Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main_two_fragment, container, false);
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
        /**
         * (四).RecyclerView设置滚动事件加入上拉加载更多功能
            下面我们再来看RecyclerView和相关类的一些特性，
            LayoutManger给我们提供了以下几个方法来让开发者方便的获取到屏幕上面的顶部item和顶部item相关的信息:
            findFirstVisibleItemPosition()
            findFirstCompletlyVisibleItemPosition()
            findLastVisibleItemPosition()
            findLastCompletlyVisibleItemPosition()
            同时通过Recycler.Adapter的getItemCount()方法可以轻松获取到RecyclerView列表中Item View的个数。
         * */
        mSwiperefreshlayout=(SwipeRefreshLayout)mView.findViewById(R.id.demo_swiperefreshlayout);
        mRecyclerView=(RecyclerView)mView.findViewById(R.id.demo_recycler);
        //设置刷新时动画的颜色，可以设置4个
        mSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwiperefreshlayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        //添加分隔线
        mRecyclerView.addItemDecoration(new AdvanceDecoration(getActivity(), OrientationHelper.VERTICAL));
    }

    @Override
    public void setListener() {
        super.setListener();
        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("kenny", "invoke onRefresh...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> newDatas = new ArrayList<String>();
                        for (int i = 0; i < 5; i++) {
                            int index = i + 1;
                            newDatas.add("new item" + index);
                        }
                        mAdapter.addItem(newDatas);
                        mSwiperefreshlayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "更新了五条数据...", Toast.LENGTH_SHORT).show();
                    }
                }, 1500);
            }
        });
        //RecyclerView滑动监听
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mAdapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<String> newDatas = new ArrayList<String>();
                            for (int i = 0; i < 5; i++) {
                                int index = i + 1;
                                newDatas.add("more item" + index);
                            }
                            mAdapter.addMoreItem(newDatas);
                        }
                    }, 1000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    /**
     * 界面懒加载数据
     */
    @Override
    protected void lazyLoad() {
        //只在视图可见的时候才加载数据
        if(!mIsPrepared || !mIsVisible) {
            return;
        }
        //加载数据，启动任务，这里设置500毫秒后开始加载数据
        mHandler.postDelayed(LOAD_DATA, 500);
    }

    /**
     * 加载数据的任务线程
     */
    private Runnable LOAD_DATA = new Runnable() {

        @Override
        public void run() {
            //在这里将数据内容加载到Fragment上
            mTitles = new ArrayList<String>();
            for (int i=0;i<20;i++){
                int index=i+1;
                mTitles.add("item"+index);
            }
            mRecyclerView.setAdapter(mAdapter = new RefreshRecyclerAdapter(getActivity(),mTitles));
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.e(TAG, "界面被销毁");
    }
}
