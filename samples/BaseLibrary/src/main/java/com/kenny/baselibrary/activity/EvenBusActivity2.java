package com.kenny.baselibrary.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.kenny.baselibrary.BaseActivity;
import com.kenny.baselibrary.R;
import com.kenny.baselibrary.event.Event;
import com.kenny.baselibrary.event.TestEvent;


import org.greenrobot.eventbus.EventBus;

/**
 * description 此activity中包含evenBus框架的介绍
 * Created by kenny on 2016/1/26.
 */
public class EvenBusActivity2 extends BaseActivity implements OnClickListener{

    private Button btOne;
    private Button btTwo;
    private Button btThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evenbus_test2);

        initView();
        setListener();
    }

    private void initView(){
        btOne = (Button) this.findViewById(R.id.bt_one);
        btTwo = (Button) this.findViewById(R.id.bt_two);
        btThree = (Button) this.findViewById(R.id.bt_three);
    }

    private void setListener(){
        btOne.setOnClickListener(this);
        btTwo.setOnClickListener(this);
        btThree.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_one:
                EventBus.getDefault().post(new Event.ItemListEvent1("MainThread"));
                EvenBusActivity2.this.finish();
                break;
            case R.id.bt_two:
                EventBus.getDefault().post(new Event.ItemListEvent2("BackgroundThread"));
                break;
            case R.id.bt_three:
                EventBus.getDefault().postSticky(new TestEvent("Sticky"));
                break;
        }
    }
}
