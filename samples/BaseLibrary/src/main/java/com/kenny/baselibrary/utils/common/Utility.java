package com.kenny.baselibrary.utils.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author kenny
 * @version 创建时间：2015-2-12
 * 类说明：生成表主键的工具类
 */
public class Utility {

	private static long lastClickTime;
	/**
	 * 禁止按钮被连续点击的方法，如listview中item包含按钮时，用户同时点击都触发(这是不允许的)
	 * if (Utility.isFastClick()) {
	 		L.e("点太快，不给触发");
	 	return ;
	 	}
	 */
	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if ( time - lastClickTime < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/** 地球半径:单位:m */
	public static final double EARTH_RADIUS = 6378137;
	/***
	 * 根据两点间维度坐标(double值)，计算两点间的距离
	 * 
	 * @param latitude1
	 *            维度
	 * @param longitude1
	 *            经度
	 * @param latitude2
	 *            维度
	 * @param longitude2
	 *            经度
	 * @return 单位是米
	 */
	public static double distanceOfTwoPoints(double latitude1, double longitude1, double latitude2, double longitude2) {
		double radLat1 = rad(latitude1);
		double radLat2 = rad(latitude2);
		double a = radLat1 - radLat2;
		double b = rad(longitude1) - rad(longitude2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		// 保留2位小数
		// BigDecimal bd = new BigDecimal(s);
		// bd = bd.setScale(2, RoundingMode.HALF_UP);
		// bd.doubleValue();
		return s;
	}

	/***
	 * 在一定范围内，判断是否到位 true到位;false未到位
	 * 
	 * @param latitude1
	 *            维度
	 * @param longitude1
	 *            经度
	 * @param latitude2
	 *            维度
	 * @param longitude2
	 *            经度
	 * @param arriveDistance
	 * @return
	 */
	public static boolean isInSamePlace(double latitude1, double longitude1, double latitude2, double longitude2, double arriveDistance) {
		return distanceOfTwoPoints(latitude1, longitude1, latitude2, longitude2) <= arriveDistance;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 判断数据是否为空
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isEmpty(Object data) {
		boolean ret = false;
		if (null == data) {
			return true;
		}
		if (data instanceof String) {
			if (((String) data).length() == 0) {
				ret = true;
			} else {
				ret = false;
			}
		} else if (data instanceof List<?>) {
			if (((List<?>) data).size() == 0) {
				ret = true;
			} else {
				ret = false;
			}
		} else if (data instanceof Map<?, ?>) {
			if (((Map<?, ?>) data).size() == 0) {
				ret = true;
			} else {
				ret = false;
			}
		} else if (data instanceof String[]) {
			if (((String[]) data).length == 0) {
				ret = true;
			} else {
				ret = false;
			}
		} else if (data instanceof JSONObject) {
			if (((JSONObject) data).length() == 0) {
				ret = true;
			} else {
				ret = false;
			}
		} else if (data instanceof JSONArray) {
			if (((JSONArray) data).length() == 0) {
				ret = true;
			} else {
				ret = false;
			}
		} else if (data instanceof Cursor) {
			if (((Cursor) data).getCount() == 0) {
				ret = true;
			} else {
				ret = false;
			}
		}
		return ret;
	}

	/**
	 * 设置对话框全屏显示
	 * 
	 * @param context
	 *            当前上下文
	 * @param dialog
	 *            当前对话框
	 */
	public static void setDialogFullScreen(Context context, Dialog dialog) {
		if (null != context && null != dialog) {
			Activity ac = (Activity) context;
			WindowManager windowManager = ac.getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.width = (int) (display.getWidth()); // 设置宽度
			lp.height = (int) (display.getHeight()); // 设置高度
			dialog.getWindow().setAttributes(lp);
		}
	}

	/**
	 * 设置对话框屏幕尺寸
	 * 
	 * @param context
	 * @param dialog
	 * @param widthScale
	 *            宽度占全屏比例(0-1之间的数字)
	 * @param heightScale
	 *            高度占全屏比例(0-1之间的数字)
	 */
	public static void setDialogScreenSize(Context context, Dialog dialog, float widthScale, float heightScale) {
		if (null != context && null != dialog) {
			Activity ac = (Activity) context;
			WindowManager windowManager = ac.getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			if (widthScale > 0 && heightScale > 0) {
				lp.width = (int) (display.getWidth() * widthScale); // 设置宽度
				lp.height = (int) (display.getHeight() * heightScale); // 设置高度
			}
			dialog.getWindow().setAttributes(lp);
		}
	}

	/**
	 * 判断是否为图片文件
	 * 
	 * @param fName
	 *            文件名称
	 * @return 返回是否为图片
	 */
	public static boolean isImageFile(String fName) {
		boolean re;
		String end = fName.substring(fName.lastIndexOf("") + 1, fName.length()).toLowerCase();
		/**
		 * 依据文件扩展名判断是否为图像文件
		 */
		if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
			re = true;
		} else {
			re = false;
		}
		return re;
	}

	/**
	 * 判断字符串是否是浮点数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str) {
		if (TextUtils.isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 计算百分比
	 * 
	 * @param progress 进度数
	 *
	 * @param max 最大数
	 *
	 * @return 百分比
	 */
	public static int computePercent(float progress, float max) {
		float value = progress / (float) max;
		return (int) (value * 100);
	}

	/** 用于查询ContentProvider的Handler(在数据量大的时候效率会比较高) */
	private static AsyncQueryHandler queryHandler;


	/**
	 * 数组按分隔符转换为字符串
	 * 
	 * @param strArray 数组
	 * @param separator 分隔符
	 * @return
	 */
	public static String stringArrayJoin(String[] strArray, String separator) {
		StringBuffer strbuf = new StringBuffer();
		if (strArray == null) {
			return "";
		}
		if (strArray.length == 1) {
			return "'" + strArray[0] + "'";
		}
		for (int i = 0; i < strArray.length; i++) {
			strbuf.append(separator).append("'" + strArray[i] + "'");
		}
		return strbuf.deleteCharAt(0).toString();
	}
	/**
	 * 对double类型的List进行排序
	 * 
	 * @param list
	 */
	public static List<Double> sortDoubleNum(List<Double> list) {
		Collections.sort(list, new Comparator<Double>() {
			@Override
			public int compare(Double num1, Double num2) {				
				return num1.compareTo(num2);
			}
		});
		return list;
	}
	
	
	/** 
     * 获得本周的第一天，周日 
     *  
     * @return 
     */  
	private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
	
    public static String getCurrentWeekDayStartTime() {  
        Calendar c = Calendar.getInstance(); 
        String format = null;
        try {  
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;  
            c.add(Calendar.DATE, 1 - weekday);  
            format = shortSdf.format(c.getTime());
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return format;
    }  
  
    /** 
     * 获得本周的最后一天，周六
     *  
     * @return 
     */  
    public static String getCurrentWeekDayEndTime() {  
        Calendar c = Calendar.getInstance();
        String format = null;
        try {  
            int weekday = c.get(Calendar.DAY_OF_WEEK)+1;  
            c.add(Calendar.DATE, 16 - weekday); 
            format = shortSdf.format(c.getTime());
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return format;  
    }

	/**
	 * 数组合并
	 * @param first 第一个
	 * @param rest 若干个
	 * @param <T> 类型
	 * @return
	 */
	public static <T> T[] concatAll(T[] first, T[]... rest) {
		int totalLength = first.length;
		for (T[] array : rest) {
			totalLength += array.length;
		}
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

}
