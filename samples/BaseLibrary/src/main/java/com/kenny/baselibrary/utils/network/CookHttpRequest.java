package com.kenny.baselibrary.utils.network;

import android.support.v4.app.DialogFragment;
import android.util.Xml;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * 设置请求弹出的dialog、cookie信息等
 * @author kenny
 * @time 2015/6/18 22:44
 */
public class CookHttpRequest extends Request<StringNetWorkResponse> {

    private Map<String,String> params;

    private DialogFragment dialogFragment;

    Response.Listener<StringNetWorkResponse> listener;

    public CookHttpRequest(int mMethod, String mUrl, Response.Listener<StringNetWorkResponse> listener, Response.ErrorListener mErrorListener) {
        this(null, mMethod, mUrl, listener, mErrorListener);
    }

    public CookHttpRequest(DialogFragment dialogFragment, int mMethod, String mUrl, Response.Listener<StringNetWorkResponse> listener, Response.ErrorListener mErrorListener) {
        super(mMethod,mUrl,mErrorListener);
        this.dialogFragment = dialogFragment;
        this.listener = listener;
        setTag(mUrl);
    }

    @Override
    protected Response<StringNetWorkResponse> parseNetworkResponse(NetworkResponse response) {

        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException var4) {
            parsed = new String(response.data);
        }

        TwoTuple<String,String> twoTuple = resolveXml(parsed);
        return Response.success(new StringNetWorkResponse(twoTuple.a, parsed, twoTuple.b, this),
                HttpHeaderParser.parseCacheHeaders(response));
    }

    public void setParams(Map<String,String> map){
        params = map;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> hearders = new HashMap<String,String>();
        hearders.put("Cookie",new CookieInfoUnit().getCookie());
        return hearders;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (params == null){
            params = new HashMap<String,String>();
        }
        return params;
    }

    @Override
    protected void deliverResponse(StringNetWorkResponse stringNetWorkResponse) {
        try {
            if (dialogFragment != null){
                dialogFragment.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listener != null)
            listener.onResponse(stringNetWorkResponse);
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        if (dialogFragment != null){
            dialogFragment.dismissAllowingStateLoss();
        }

    }

    public TwoTuple<String,String> resolveXml(String result){
        XmlPullParser parser = Xml.newPullParser();
        TwoTuple<String,String> twoTuple = new TwoTuple<String,String>();
        try {
            parser.setInput(new ByteArrayInputStream(result.getBytes()),"UTF-8");
            int event = parser.getEventType();
            String name = "";
            while (event != XmlPullParser.END_DOCUMENT){
                name = parser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        if ("result".equals(name)){
                            twoTuple.a = parser.nextText();
                        }else if ("desc".equals(name)){
                            twoTuple.b = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
        } catch (XmlPullParserException e) {
            return  null;
        } catch (IOException e) {
            return  null;
        }
        return  twoTuple;
    }
}
