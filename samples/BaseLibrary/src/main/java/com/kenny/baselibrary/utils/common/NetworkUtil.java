package com.kenny.baselibrary.utils.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class NetworkUtil {

	/** 没有网络 */
	public static final int NETWORKTYPE_INVALID = -1;
	/** wap网络 */
	public static final int NETWORKTYPE_WAP = 1;
	/** 2G网络 */
	public static final int NETWORKTYPE_2G = 2;

	/** 3G和3G以上网络，或统称为快速网络 */
	public static final int NETWORKTYPE_3G = 3;

	/** wifi网络 */
	public static final int NETWORKTYPE_WIFI = 4;

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isConnectedOrConnecting();
			}
		}
		return false;
	}

	/**
	 * 判断是否是FastMobileNetWork，将3G或者3G以上的网络称为快速网络
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isFastMobileNetwork(Context context) {

		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int networkType = telephonyManager.getNetworkType();

		if (networkType == TelephonyManager.NETWORK_TYPE_1xRTT) {

			return false; // ~ 50-100 kbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_CDMA) {

			return false; // ~ 14-64 kbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_EDGE) {

			return false; // ~ 50-100 kbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_EVDO_0) {

			return true; // ~ 400-1000 kbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_EVDO_A) {

			return true; // ~ 600-1400 kbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_GPRS) {

			return false; // ~ 100 kbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_HSDPA) {

			return true; // ~ 2-14 Mbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_HSPA) {

			return true; // ~ 700-1700 kbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_HSUPA) {

			return true; // ~ 1-23 Mbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_UMTS) {

			return true; // ~ 400-7000 kbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_EHRPD) {

			return true; // ~ 1-2 Mbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_EVDO_B) {

			return true; // ~ 5 Mbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_IDEN) {

			return false; // ~25 kbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_LTE) {

			return true; // ~ 10+ Mbps

		} else if (networkType == TelephonyManager.NETWORK_TYPE_UNKNOWN) {

			return false;

		} else {

			return false;
		}

	}

	/**
	 * 获取网络状态，wifi,wap,2g,3g.
	 * 
	 * @param context
	 * @return {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G},
	 *         {@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP},
	 *         {@link #NETWORKTYPE_WIFI}
	 */
	public static int getNetWorkType(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		int mNetWorkType = -1;

		if (networkInfo != null && networkInfo.isConnected()) {
			String type = networkInfo.getTypeName();

			if (type.equalsIgnoreCase("WIFI")) {
				mNetWorkType = NETWORKTYPE_WIFI;
			} else if (type.equalsIgnoreCase("MOBILE")) {
				String proxyHost = android.net.Proxy.getDefaultHost();

				mNetWorkType = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G) : NETWORKTYPE_WAP;
			}
		} else {
			mNetWorkType = NETWORKTYPE_INVALID;
		}

		return mNetWorkType;
	}

	/**
	 * 判断是否是3G
	 * 
	 * @param context
	 * @return
	 */
	public static boolean is3GNet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

}
