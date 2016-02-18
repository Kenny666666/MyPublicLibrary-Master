package com.kenny.baselibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.kenny.baselibrary.BaseActivity;
import com.kenny.baselibrary.R;
import com.kenny.baselibrary.event.Event;
import com.kenny.baselibrary.event.TestEvent;
import com.kenny.baselibrary.utils.common.L;
import com.kenny.baselibrary.utils.common.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * description 此activity中包含evenBus框架的介绍
 * Created by kenny on 2016/1/26.
 *
 * 总结一下：register会把当前类中匹配的方法，存入一个map，而post会根据实参去map查找进行反射调用。分析这么久，一句话就说完了~~

   其实不用发布者，订阅者，事件，总线这几个词或许更好理解，以后大家问了EventBus，可以说，就是在一个单例内部维持着一个map对象存储了一堆的方法；post无非就是根据参数去查找方法，进行反射调用。

 * 什么是EventBus

    EventBus是一个Android端优化的publish/subscribe消息总线，简化了应用程序内各组件间、组件与后台线程间的通信。比如请求网络，等网络返回时通过Handler或Broadcast通知UI，两个Fragment之间需要通过Listener通信，这些需求都可以通过 EventBus 实现。
     其中ThreadMode提供了四个常量：

     MainThread 首先去判断当前如果是UI线程，则直接调用；否则： mainThreadPoster.enqueue(subscription, event);把当前的方法加入到队列，然后直接通过handler去发送一个消息，在handler的handleMessage中，去执行我们的方法。说白了就是通过Handler去发送消息，然后执行的。

     BackgroundThread 后台线程：当事件是在UI线程发出，那么事件处理实际上是需要新建单独线程，如果是在后台线程发出，那么事件处理就在该线程。该事件处理方法应该是快速的，避免阻塞后台线程。如果当前非UI线程，则直接调用；(如果是UI线程，则将任务加入到后台的一个队列，最终由Eventbus中的一个线程池去调用)

     Async 后台线程：发送事件方不需要等待事件处理完毕。这种方式适用于该事件处理方法需要较长时间，例如网络请求。（将任务加入到后台的一个队列，最终由Eventbus中的一个线程池去调用；线程池与BackgroundThread用的是同一个。）
     这么说BackgroundThread和Async有什么区别呢？
     BackgroundThread中的任务，一个接着一个去调用，中间使用了一个布尔型变量handlerActive进行的控制。
     Async则会动态控制并发。

     PostThread 发送线程（默认）

     sticky = true

     默认情况下，其为false。什么情况下使用sticky呢？看  helloEventSticky() 方法
 */
public class EvenBusActivity1 extends BaseActivity implements OnClickListener{

    private Button bt_one;
    private TextView tv_one;
    private TextView tv_two;
    private TextView tv_three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evenbus_test1);

        initView();
        setListener();

        //订阅事件(将当前类中所有总线方法加入到总线中的map中)
        EventBus.getDefault().register(this);
    }

    private void initView(){
        bt_one = (Button) this.findViewById(R.id.bt_one);
        tv_one = (TextView) this.findViewById(R.id.tv_one);
        tv_two = (TextView) this.findViewById(R.id.tv_two);
        tv_three = (TextView) this.findViewById(R.id.tv_three);
    }

    private void setListener(){
        bt_one.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_one:
                startActivity(new Intent(EvenBusActivity1.this,EvenBusActivity2.class));
                break;
        }
    }

    /**
     * Subscribe订阅者:收到消息 进行相关处理. priority = 1 相信大部分人知道该用法，值越小优先级越低，默认为0。
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventMainThread(Event.ItemListEvent1 event) {
        L.e(TAG, "helloEventMainThread收到消息:" + event.getMsg());
        tv_one.setText(event.getMsg());
        T.showShort(EvenBusActivity1.this,event.getMsg());
    }

    /**
     * 收到消息 使用后台线程处理
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void helloEventBackgroundThread(Event.ItemListEvent2 event) {

        Log.e(TAG, "helloEventBackgroundThread收到消息:" + event.getMsg());
        Looper.prepare();
        T.showShort(EvenBusActivity1.this, "后台线程处理中："+event.getMsg());
        Looper.loop();
//        tv_two.setText(event.getMsg());后台线程不能更新UI
    }

    /**
     * sticy使用
     * 什么时候使用sticy,当你希望你的事件不被马上处理的时候，举个栗子，比如说，在一个详情页点赞之后，产生一个VoteEvent，VoteEvent并不立即被消费，而是等用户退出详情页回到商品列表之后，接收到该事件，然后刷新Adapter等。其实这就是之前我们用startActivityForResult和onActivityResult做的事情。
     * @param event
     */
    @Subscribe(sticky = true)
    public void helloEventSticky(TestEvent event) {

        Log.e(TAG, "helloEventSticky收到消息:" + event.getMsg());
        tv_three.setText(event.getMsg());
        T.showShort(EvenBusActivity1.this, event.getMsg());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件(将当前类中所有方法在总线中的map中移除)
        EventBus.getDefault().unregister(this);
    }
}
