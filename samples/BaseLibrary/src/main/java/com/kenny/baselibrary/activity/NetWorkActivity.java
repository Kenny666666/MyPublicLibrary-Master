package com.kenny.baselibrary.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kenny.baselibrary.R;
import com.kenny.baselibrary.utils.common.T;
import com.kenny.baselibrary.utils.network.StringNetWorkResponse;

/**
 * description
 * Created by kenny on 2016/1/30.
 * version
 */
public class NetWorkActivity extends BaseActivity implements View.OnClickListener{

    private final String TAG = NetWorkActivity.this.getClass().getName();
    private Context mContext;
    private Button bt_str_request_get;
    private Button bt_str_request_post;
    private Button bt_gson_request_post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_test);

        mContext = this;
        initViews();
        setListener();
    }

    private void initViews(){
        bt_str_request_get = (Button) findViewById(R.id.bt_str_request_get);
        bt_str_request_post = (Button) findViewById(R.id.bt_str_request_post);
        bt_gson_request_post = (Button) findViewById(R.id.bt_gson_request_post);
    }

    private void setListener(){
        bt_str_request_get.setOnClickListener(this);
        bt_str_request_post.setOnClickListener(this);
        bt_gson_request_post.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_str_request_get:
                requestHelp.submitGet("http://www.csdn.net/",null);
                break;
            case R.id.bt_str_request_post:
                requestHelp.submitPost("http://www.baidu.com", null);
                break;
            case R.id.bt_gson_request_post:
                requestHelp.submitPostNoXml("http://www.csdn.net/",null);
                break;
        }
    }

    @Override
    public void onResponse(StringNetWorkResponse response) {
        super.onResponse(response);
        if(!validateResponse(response)){
            T.showShort(mContext, "请求失败!");
            return;
        }
        //在创建请求的时候传入一个url，返回的数据用url区分
        String currUrl = requestHelp.getRequest().getUrl();
        if (currUrl.equals("http://www.csdn.net/")){

        }
    }
}
