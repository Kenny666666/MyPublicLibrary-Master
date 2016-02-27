package com.kenny.baselibrary.event;

/**
 * description 推荐大家在使用EventBus的时候，创建一个事件类，把你的每一个参数（或者可能发生冲突的参数），封装成一个类
 * Created by kenny on 2015/8/2.
 */
public class Event {

    /**列表加载事件*/
    public static class ItemListEvent1{

        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public ItemListEvent1(String msg){
            this.msg=msg;
        }
    }

    /**列表加载事件*/
    public static class ItemListEvent2{

        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public ItemListEvent2(String msg){
            this.msg=msg;
        }
    }
}