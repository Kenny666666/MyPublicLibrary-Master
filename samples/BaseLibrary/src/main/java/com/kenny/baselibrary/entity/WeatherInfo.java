package com.kenny.baselibrary.entity;

/**
 * 天气信息类
 * Created by kenny on 2015/6/21.
 */
public class WeatherInfo {
    private String city;
    private String temp;
    private String time;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
