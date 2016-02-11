package com.kenny.baselibrary;

/**
 * Created by kenny on 15/6/24.
 */
public interface Template {

    public void initView();

    /***
     * 绑定控件事件
     */
    public void setListener();


    /**
     * 初始化数据
     * */
    public void initData();
}
