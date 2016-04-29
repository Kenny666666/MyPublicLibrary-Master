package com.kenny.baselibrary;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 全局handler
 * @author kenny
 * @time 2016/4/26 9:09
 */
public class UIHandler extends Handler{

    private IHandler handler;//回调接口

    public UIHandler(Looper looper){
        super(looper);
    }

    public UIHandler(Looper looper, IHandler handler){
        super(looper);
        this.handler = handler;
    }

    public void setHandler(IHandler handler){
       this.handler = handler;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(handler != null){
            handler.handleMessages(msg);//有消息就传递
        }
    }

    public interface IHandler{
        void handleMessages(Message msg);
    }

}
