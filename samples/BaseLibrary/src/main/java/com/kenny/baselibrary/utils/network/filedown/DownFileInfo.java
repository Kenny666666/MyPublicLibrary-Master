package com.kenny.baselibrary.utils.network.filedown;


import com.android.volley.VolleyError;

/**
 * 下载文件信息类
 * Created by kenny on 15/5/5.
 */
public class DownFileInfo {

    /**
     * 文件总大小
     */
    private int total;
    /**
     * 当前进度
     */
    private int current;
    /**
     * 文件下载状态
     */
    private DownState state;
    /**
     * 异常
     */
    private VolleyError error;

    public DownFileInfo(DownState state,int total, int current) {
        this.total = total;
        this.state = state;
        this.current = current;
    }

    public DownFileInfo(DownState state, VolleyError error) {
        this.state = state;
        this.error = error;
    }


    public VolleyError getError() {
        return error;
    }

    public void setError(VolleyError error) {
        this.error = error;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public DownState getState() {
        return state;
    }

    public void setState(DownState state) {
        this.state = state;
    }

    public  enum  DownState{
        error,
        success,
        progress
    }
}
