package com.kenny.baselibrary.utils.network.filedown;


import com.android.volley.VolleyError;


/**
 *
 * @author kenny
 * @time 2015/5/18 22:43
 */
public interface Listener<T> {

    public void success(T t);

    public void error(VolleyError error);

//    public void progress(int total,int current);
}
