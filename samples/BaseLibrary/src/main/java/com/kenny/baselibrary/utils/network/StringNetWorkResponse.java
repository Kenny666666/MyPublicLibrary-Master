package com.kenny.baselibrary.utils.network;


import android.text.TextUtils;

import com.android.volley.Request;


/**
 * 网络请求返回后封装成的数据类
 * @author kenny
 * @time 2015/5/14 22:44
 */
public class StringNetWorkResponse {

    private String data;

    private String result;

    private String desc = "";

    private Request request;

    public StringNetWorkResponse(String result, String data, String desc, Request request) {
        this.data = data;
        this.result = result;
        this.desc = desc;
        this.request = request;
    }

    public String getResult() {
        return TextUtils.isEmpty(result)?"":result;
    }

    public String getData() {
        return TextUtils.isEmpty(data)?"":data;
    }

    public String getDesc() {
        return TextUtils.isEmpty(desc)?"":desc;
    }

    public StringNetWorkResponse(Request request, String data) {
        this.data = data;
    }

    public Request getRequest() {
        return request;
    }
}
