package com.kenny.baselibrary.utils.network;

import android.support.v4.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.kenny.baselibrary.BaseLibraryApplication;
import com.kenny.baselibrary.view.dialog.DialogValue;
import com.kenny.baselibrary.utils.xml.XMLParserUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by kenny on 15/6/24.
 */
public class RequestHelp {

    private Response.Listener<StringNetWorkResponse> listener;

    private Response.ErrorListener errorListener;

    private FragmentManager fm;

    private CookHttpRequest request;

    public RequestHelp(FragmentManager fm, Response.Listener<StringNetWorkResponse> listener, Response.ErrorListener errorListener) {
        this.listener = listener;
        this.errorListener = errorListener;
        this.fm = fm;
    }

    public void submitPost(String url,Map<String,String> requestParams){
        submit(true, true, Request.Method.POST, url, requestParams);
    }

    public void submitPost(boolean isShowDialog,String url,Map<String,String> requestParams){
        submit(isShowDialog,true, Request.Method.POST, url, requestParams);
    }

    public void submitGet(String url,Map<String,String> requestParams){
        submit(true,true, Request.Method.GET, url, requestParams);
    }

    public void submitPostNoXml(String url,Map<String,String> requestParams){
        submit(true, false, Request.Method.POST, url, requestParams);
    }

    public void submitPostNoAndNoDialogXml(String url,Map<String,String> requestParams){
        submit(false, false, Request.Method.POST, url, requestParams);
    }
    public void submitPostNoDialog(String url,Map<String,String> requestParams){
        submit(false, true, Request.Method.POST, url, requestParams);
    }


    public void submit(boolean isShowDialog,boolean isXml,int method,String url,Map<String,String> requestParams){
        if(isShowDialog){
            DialogValue dialogValue = new DialogValue(fm);
            request = new CookHttpRequest(dialogValue.showConfirmAndCancle(),method, url, listener, errorListener);
        }else{
            request = new CookHttpRequest(method, url, listener, errorListener);
        }

        request.setRetryPolicy( new DefaultRetryPolicy(
                200000,//默认超时时间，应设置一个稍微大点儿的，例如本处的200000 == 200秒
                1,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        Map<String,String> params = null;
        try {
            params = request.getParams();
            if (isXml) {
                params.put("content", XMLParserUtil.convertXml(requestParams));
            }
            else{
                Set<Map.Entry<String,String>> set = requestParams.entrySet();
                Iterator<Map.Entry<String,String>> iterator = set.iterator();
                while (iterator.hasNext()){
                    Map.Entry<String,String> map = iterator.next();
                    params.put(map.getKey(),map.getValue());
                }
            }
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        BaseLibraryApplication.getInstance().getRequestQueue().add(request);
    }

    public Request getRequest(){
        return request;
    }

    /**取消请求*/
    public void cancelTag(Object tag) {
        BaseLibraryApplication.getInstance().getRequestQueue().cancelAll(tag);
    }
}
