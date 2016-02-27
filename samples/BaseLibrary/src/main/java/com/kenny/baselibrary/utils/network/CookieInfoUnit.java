package com.kenny.baselibrary.utils.network;

import com.kenny.baselibrary.BaseLibraryApplication;
import com.kenny.baselibrary.utils.common.AppUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * cookie实体类
 * Created by kenny on 15/6/18.
 */
public class CookieInfoUnit {

    public Map<String, String> headerMap = new HashMap<String, String>();

    /** 应用系统标志 */
    public static String SYSFLAG_TAG = "SysFlag";

    /** 客户端版本号 */
    public static final String CLIENTVERSION_TAG = "ClientVersion";

    /** 命令标志 */
    public static final String CMD_TAG = "Cmd";

    /** 国际移动设备身份码
     * imei =((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getDeviceId();
     */
    public static final String IMEI_TAG = "IMEI";

    /**数据类型 格式化*/
    public static final String FORMAT_TAG = "Format";

    /** 地址信息 */
    public static final String GPS_INFO_TAG = "gpsLocation";

    /** 会话ID */
    public static final String SESSIONID_TAG = "sessionId";

    /** 终端编号 */
    public static final String TADDRR_TAG = "taddr";

    /**用户ID*/
    public static final String USER_ID = "userId";

    public CookieInfoUnit(){

       BaseLibraryApplication baseApplication =  BaseLibraryApplication.getInstance();

        addHeader(SYSFLAG_TAG,"");
        addHeader(CLIENTVERSION_TAG,""+AppUtils.getAppVersionCode(BaseLibraryApplication.getInstance()));
        addHeader(CMD_TAG,"");
        addHeader(FORMAT_TAG,"");
        addHeader(GPS_INFO_TAG, "unknown");
//            addHeader(IMEI_TAG,BaseApplication.imei);
//            addHeader(SESSIONID_TAG, SharedPreferencesUnit.readSessionId(baseApplication));
//            addHeader(TADDRR_TAG, ConstVar.getImeiTarr(baseApplication));
//
//            if (SharedPreferencesUnit.readDeptUser(baseApplication) != null){
//                addHeader(USER_ID,SharedPreferencesUnit.readDeptUser(baseApplication).user_id);
//            }
    }

    /**
     * 添加头信息属性
     *
     * @param tag
     * @param value
     */
    public void addHeader(String tag, String value) {
        headerMap.put(tag, value);
    }

    /**
     * 获取Cookien内容
     * @return
     */
    public String getCookie(){
        StringBuffer cookie=new StringBuffer();
        Iterator<String> it=headerMap.keySet().iterator();
        while (it.hasNext()) {
            String key=it.next();
            cookie.append(key+"="+headerMap.get(key)+";");
        }
        return 	cookie.toString();
    }
}
