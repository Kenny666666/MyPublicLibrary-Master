package com.kenny.baselibrary.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.kenny.baselibrary.BaseActivity;
import com.kenny.baselibrary.R;
import com.kenny.baselibrary.utils.common.T;
import com.kenny.baselibrary.utils.network.StringNetWorkResponse;

/**
 * 网络测试界面
 * @author kenny
 * @time 2016/1/31 22:25
 */
public class NetWorkActivity extends BaseActivity implements View.OnClickListener{

    private final String TAG = NetWorkActivity.this.getClass().getName();
    private Context mContext;
    private Button btStrRequestGet;
    private Button btStrRequestPost;
    private Button btGsonRequestPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_test);
        mContext = this;
        initViews();
        setListener();
    }

    private void initViews(){
        btStrRequestGet = (Button) findViewById(R.id.bt_str_request_get);
        btStrRequestPost = (Button) findViewById(R.id.bt_str_request_post);
        btGsonRequestPost = (Button) findViewById(R.id.bt_gson_request_post);
    }

    private void setListener(){
        btStrRequestGet.setOnClickListener(this);
        btStrRequestPost.setOnClickListener(this);
        btGsonRequestPost.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_str_request_get:
                mRequestHelp.submitGet("http://www.csdn.net/",null);
                break;
            case R.id.bt_str_request_post:
                mRequestHelp.submitPost("http://www.baidu.com", null);
                break;
            case R.id.bt_gson_request_post:
                mRequestHelp.submitPostNoXml("http://www.csdn.net/",null);
                break;
        }
    }

    /**
     * 接口回调
     * @param response
     */
    @Override
    public void onResponse(StringNetWorkResponse response) {
        super.onResponse(response);
        if(!validateResponse(response)){
            T.showShort(mContext, "请求失败!");
            return;
        }
        //在创建请求的时候传入一个url，返回的数据用url区分
        String currUrl = mRequestHelp.getRequest().getUrl();
        if (currUrl.equals("http://www.csdn.net/")){

        }
    }

    @Override
    protected void handler(Message msg) {

    }
}
