package com.kenny.baselibrary.utils.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 跟App相关的辅助类
 */
public class AppUtils {

	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	private static String packageName = "";
	/**
	 * 取得AndroidManifest.xml中应用程序的包名
	 *
	 * @return package的值
	 */
	public static String getPackageName(Context context) {
		if ("".equals(packageName)) {
			PackageManager manager = context.getPackageManager();
			try {
				PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
				// 包名
				packageName = info.packageName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return packageName;
	}
	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取得AndroidManifest.xml中android:versionName的值
	 *
	 * @return versionName的值
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			// 版本名
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 取得AndroidManifest.xml中android:versionCode的值
	 *
	 * @return versionCode的值
	 */
	public static int getAppVersionCode(Context context) {
		int versionCode = 0;
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			// 版本名
			versionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
}
