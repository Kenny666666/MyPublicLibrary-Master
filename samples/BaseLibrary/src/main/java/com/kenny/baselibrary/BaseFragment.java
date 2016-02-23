package com.kenny.baselibrary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.kenny.baselibrary.utils.common.T;
import com.kenny.baselibrary.utils.network.RequestHelp;
import com.kenny.baselibrary.utils.network.StringNetWorkResponse;

/**
 * Created by kenny on 15/4/23.
 */
public class BaseFragment extends Fragment implements Template,Response.ErrorListener,Response.Listener<StringNetWorkResponse> {

    /**
     * 接口请求帮助类
     */
    public RequestHelp mRequestHelp;

    protected View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestHelp = new RequestHelp(getFragmentManager(), this, this);
    }

    /**
     * 初始view
     */
    @Override
    public void initView() {
        if (mView != null) {
            mView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
    }

    /**
     * 设置view监听
     */
    @Override
    public void setListener() {
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
    }

    /**
     * 接口请求异常回调
     * @param e
     */
    @Override
    public void onErrorResponse(VolleyError e) {
            if (e instanceof TimeoutError) {
                T.showShort(getActivity(), "连接超时");
            } else if (e instanceof NoConnectionError) {
                T.showShort(getActivity(), "暂无网络连接");
            } else if (e instanceof NetworkError) {
                T.showShort(getActivity(), "网络连接错误");
            } else {
                T.showShort(getActivity(), "连接失败");
            }
    }

    /**
     * 接口请求正常回调
     * @param stringNetWorkResponse
     */
    @Override
    public void onResponse(StringNetWorkResponse stringNetWorkResponse) {

    }

    /**
     * false： 请求数据验证不通过
     * @param stringNetWorkResponse
     * @return
     */
    public boolean validateResponse(StringNetWorkResponse stringNetWorkResponse) {
        if (null == stringNetWorkResponse || null == stringNetWorkResponse.getRequest() || null == stringNetWorkResponse.getResult()) {
            return false;
        }
        return true;
    }

    public String createXmlItem(String tagName, String tagValue) {
        StringBuffer buffer = new StringBuffer();
        if (TextUtils.isEmpty(tagValue)) {
            buffer.append("<");
            buffer.append(tagName);
            buffer.append("/>");
            return buffer.toString();
        }
        buffer.append("<");
        buffer.append(tagName);
        buffer.append(">");
        buffer.append(tagValue);
        buffer.append("</");
        buffer.append(tagName);
        buffer.append(">");
        return buffer.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null!=mView){
            ViewGroup viewParent = (ViewGroup) mView.getParent();
            if(null!=viewParent){
                viewParent.removeView(mView);
            }
        }
    }

}
