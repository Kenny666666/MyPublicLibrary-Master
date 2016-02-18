package com.kenny.baselibrary.event;

/**
 * description
 * Created by kenny on 2016/2/17.
 */
public class TestEvent {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TestEvent(String msg){
        this.msg=msg;
    }
}
