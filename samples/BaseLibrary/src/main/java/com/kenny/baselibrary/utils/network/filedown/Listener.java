package com.kenny.baselibrary.utils.network.filedown;


import com.android.volley.VolleyError;

/**
 * Created by kenny on 15/5/18.
 */
public interface Listener<T> {

    public void success(T t);

    public void error(VolleyError error);

//    public void progress(int total,int current);
}
