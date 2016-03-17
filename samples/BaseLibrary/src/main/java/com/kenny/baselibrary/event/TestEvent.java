package com.kenny.baselibrary.event;


/**
 * eventbus测试类
 * @author kenny
 * @time 2016/2/17 22:35
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
