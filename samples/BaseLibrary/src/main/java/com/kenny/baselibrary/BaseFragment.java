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

    public RequestHelp requestHelp;

    protected View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestHelp = new RequestHelp(getFragmentManager(), this, this);
    }

    @Override
    public void initView() {
        if (view != null) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
    }

    @Override
    public void setListener() {
    }

    @Override
    public void initData() {
    }

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
        if(null!=view){
            ViewGroup viewParent = (ViewGroup) view.getParent();
            if(null!=viewParent){
                viewParent.removeView(view);
            }
        }
    }

}
