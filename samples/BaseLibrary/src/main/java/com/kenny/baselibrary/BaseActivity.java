package com.kenny.baselibrary;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.kenny.baselibrary.utils.common.L;
import com.kenny.baselibrary.utils.common.T;
import com.kenny.baselibrary.utils.crash.ExitAppUtils;
import com.kenny.baselibrary.utils.network.RequestHelp;
import com.kenny.baselibrary.utils.network.StringNetWorkResponse;
import com.zhy.autolayout.AutoLayoutActivity;


/**
 * 所有activity的基类
 * Created by kenny on 2015/6/21.
 */
public abstract  class BaseActivity extends AutoLayoutActivity implements Response.ErrorListener,Response.Listener<StringNetWorkResponse> {

    public RequestHelp requestHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化请求帮助类
        requestHelp = new RequestHelp(getSupportFragmentManager(),this,this);
        //将activity加入到维护队列
        ExitAppUtils.getInstance().addActivity(this);
    }

    @Override
    public void onErrorResponse(VolleyError e) {
        if (e instanceof TimeoutError){
            T.showShort(BaseActivity.this, R.string.connection_time_out);
        }else if(e instanceof NoConnectionError){
            T.showShort(BaseActivity.this, R.string.not_found_request);
            e.printStackTrace();
        }else if (e instanceof NetworkError){
            T.showShort(BaseActivity.this, R.string.network_connection_error);
        }else{
            T.showShort(BaseActivity.this, R.string.server_exception);
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(StringNetWorkResponse response) {

    }

    public void show(int id){
        show(getString(id));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        hideSoft();
        L.e(this.getLocalClassName()+":onDestroy");
        //将activity移除到维护队列
        ExitAppUtils.getInstance().delActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //activity被暂停时取消请求
        requestHelp.cancelTag(this);
    }

    public void show(String str){
        T.showShort(BaseActivity.this, str);
    }

    /**
     * false: 请求异常
     * @param response
     * @return
     */
    public boolean validateResponse(StringNetWorkResponse response) {
        if (null == response || null == response.getRequest() || null == response.getResult()) {
            return false;
        }
        return true;
    }

    /**隐藏软键盘*/
    public void hideSoft(){
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED){
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
