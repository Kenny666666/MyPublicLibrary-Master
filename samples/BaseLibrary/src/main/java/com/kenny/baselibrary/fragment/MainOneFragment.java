package com.kenny.baselibrary.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kenny.baselibrary.LazyFragment;
import com.kenny.baselibrary.R;
import com.nostra13.universalimageloader.utils.L;

import java.util.LinkedList;


/**
 * 主界面--通用架构（本界面展示PullToRefresh上下拉刷新框架案例）
 * @author kenny
 * @time 2015/12/21 22:36
 */
public class MainOneFragment extends LazyFragment {

    private final String TAG = MainOneFragment.this.getClass().getName();
    private View mView;

    /**
     * 上拉刷新的控件使用
     * <p/>
     * 1、自定义下拉指示器文本内容等效果
     * <p/>
     * ILoadingLayout startLabels = mPullRefreshListView.getLoadingLayoutProxy();
     * startLabels.setPullLabel("你可劲拉，拉...");// 刚下拉时，显示的提示
     * startLabels.setRefreshingLabel("好嘞，正在刷新...");// 刷新时
     * startLabels.setReleaseLabel("你敢放，我就敢刷新...");// 下来达到一定距离时，显示的提示
     * <p/>
     * 2、默认是上拉和下拉的字同时改变的，如果我希望单独改变呢？
     * private void initIndicator(){
     * ILoadingLayout startLabels = mPullRefreshListView.getLoadingLayoutProxy(true, false);
     * startLabels.setPullLabel("你可劲拉，拉...");// 刚下拉时，显示的提示
     * startLabels.setRefreshingLabel("好嘞，正在刷新...");// 刷新时
     * startLabels.setReleaseLabel("你敢放，我就敢刷新...");// 下来达到一定距离时，显示的提示
     * <p/>
     * ILoadingLayout endLabels = mPullRefreshListView.getLoadingLayoutProxy(false, true);
     * endLabels.setPullLabel("你可劲拉，拉2...");// 刚下拉时，显示的提示
     * endLabels.setRefreshingLabel("好嘞，正在刷新2...");// 刷新时
     * endLabels.setReleaseLabel("你敢放，我就敢刷新2...");// 下来达到一定距离时，显示的提示
     * }
     * <p/>
     * //mPullRefreshListView.getLoadingLayoutProxy(true, false);接收两个参数，为true,false返回设置下拉的ILoadingLayout；为false,true返回设置上拉的。
     */
    private PullToRefreshListView mPullRefreshListView;
    /**
     * listview适配器
     */
    private ArrayAdapter<String> mAdapter;
    /**
     * 适配器数据
     */
    private LinkedList<String> mListItems;
    /**
     * handler
     */
    private Handler mHandler = new Handler();
    /**
     * 标志位，标志界面view已经初始化完成。
     */
    private boolean mIsPrepared;

    private int mItemCount = 9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main_one_fragment, container, false);
        //初始化view，这里最好先设置一个进度对话框，提示用户正在加载数据
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
        // 得到控件
        mPullRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.pull_refresh_list);
    }

    @Override
    public void setListener() {
        super.setListener();

        // 设置监听事件
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.e("TAG", "onPullDownToRefresh");
                //这里写下拉刷新的任务
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh");
                //这里写上拉加载更多的任务
                new GetDataTask().execute();
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
            initData();
        }
    };

    @Override
    public void initData() {
        super.initData();
        // 初始化数据和数据源
        mListItems = new LinkedList<String>();

        for (int i = 0; i < mItemCount; i++) {
            mListItems.add("" + i);
        }
        // 设置适配器
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mListItems);
        mPullRefreshListView.setAdapter(mAdapter);
    }

    /**
     * 下拉刷新执行的加载数据任务
     */
    private class GetDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return "" + (mItemCount++);
        }

        @Override
        protected void onPostExecute(String result) {
            mListItems.add(result);
            mAdapter.notifyDataSetChanged();
            mPullRefreshListView.onRefreshComplete();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.e(TAG,"界面被销毁");
    }
}
