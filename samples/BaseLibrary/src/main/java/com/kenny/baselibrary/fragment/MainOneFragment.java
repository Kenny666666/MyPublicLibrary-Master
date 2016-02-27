package com.kenny.baselibrary.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kenny.baselibrary.BaseFragment;
import com.kenny.baselibrary.R;

import java.util.LinkedList;

/**
 * 主界面--通用架构（本界面展示PullToRefresh上下拉刷新框架案例）
 * Created by kenny on 2015/12/21.
 */
public class MainOneFragment extends BaseFragment {

    private View mView;
    private String mTitle = "Default";
    public static final String TITLE = "title";

    /**
     * 上拉刷新的控件使用
     *
     * 1、自定义下拉指示器文本内容等效果
     *
       ILoadingLayout startLabels = mPullRefreshListView.getLoadingLayoutProxy();
       startLabels.setPullLabel("你可劲拉，拉...");// 刚下拉时，显示的提示
       startLabels.setRefreshingLabel("好嘞，正在刷新...");// 刷新时
       startLabels.setReleaseLabel("你敢放，我就敢刷新...");// 下来达到一定距离时，显示的提示

       2、默认是上拉和下拉的字同时改变的，如果我希望单独改变呢？
       private void initIndicator(){
        ILoadingLayout startLabels = mPullRefreshListView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("你可劲拉，拉...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("好嘞，正在刷新...");// 刷新时
        startLabels.setReleaseLabel("你敢放，我就敢刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = mPullRefreshListView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("你可劲拉，拉2...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("好嘞，正在刷新2...");// 刷新时
        endLabels.setReleaseLabel("你敢放，我就敢刷新2...");// 下来达到一定距离时，显示的提示
     }

     //mPullRefreshListView.getLoadingLayoutProxy(true, false);接收两个参数，为true,false返回设置下拉的ILoadingLayout；为false,true返回设置上拉的。
     */

    private PullToRefreshListView mPullRefreshListView;
    private ArrayAdapter<String> mAdapter;
    private LinkedList<String> mListItems;

    private int mItemCount = 9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main_one_fragment, container, false);
        //初始化view
        initView();
//        //给控件设置监听
        setListener();
//        //加载数据
        initData();
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
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.e("TAG", "onPullDownToRefresh");
                        //这里写下拉刷新的任务
                        new GetDataTask().execute();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.e("TAG", "onPullUpToRefresh");
                        //这里写上拉加载更多的任务
                        new GetDataTask().execute();
                    }
                });
    }

    @Override
    public void initData() {
        super.initData();
        // 初始化数据和数据源
        mListItems = new LinkedList<String>();

        for (int i = 0; i < mItemCount; i++) {
            mListItems.add("" + i);
        }
        // 设置适配器
        mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, mListItems);
        mPullRefreshListView.setAdapter(mAdapter);
    }

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

}
