package com.kenny.baselibrary.utils.common;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Log统一管理类
 */
public class L {

	private L() {
		throw new UnsupportedOperationException("不能实例化类，此类中的方法可直接使用");
	}

	public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
	private static final String TAG = "L:";
	/**把异常保存到文件中*/
	public static void saveExceptionToFile(Context context,String className,String methodName,Exception e){
		
		String fileName = null;
		StringBuffer sb = new StringBuffer();
		
		sb.append("className="+className+"\n");
		sb.append("methodName="+methodName+"\n");
		sb.append(obtainExceptionInfo(e));
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File dir = new File(SDCardUtils.getSDCardPath() + "database/crash" + File.separator);
			if(! dir.exists()){
				dir.mkdirs();
			}
			try{
				fileName = dir.toString() + File.separator + paserTime(System.currentTimeMillis()) + ".log";
				FileOutputStream fos = new FileOutputStream(fileName);
				fos.write(sb.toString().getBytes());
				fos.flush();
				fos.close();
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
	
	/**把异常保存到文件中*/
	public static void saveExceptionToFile(Context context, String whatLocationExc,Exception e){
		
		String fileName = null;
		StringBuffer sb = new StringBuffer();
		
		sb.append("错误信息="+whatLocationExc+"\n");
		sb.append(obtainExceptionInfo(e));
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File dir = new File(SDCardUtils.getSDCardPath() + "database/crash" + File.separator);
			if(! dir.exists()){
				dir.mkdirs();
			}
			try{
				fileName = dir.toString() + File.separator + paserTime(System.currentTimeMillis()) + ".log";
				FileOutputStream fos = new FileOutputStream(fileName);
				fos.write(sb.toString().getBytes());
				fos.flush();
				fos.close();
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 获取系统捕捉的错误信息
	 * @param e
	 * @return
	 */
	private static String obtainExceptionInfo(Exception e) {
		StringWriter mStringWriter = new StringWriter();
		PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
		e.printStackTrace(mPrintWriter);
		mPrintWriter.close();
		
		Log.e(TAG, mStringWriter.toString());
		return mStringWriter.toString();
	}
	
	/**
	 * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格式
	 * @param milliseconds
	 * @return
	 */
	private static String paserTime(long milliseconds) {
		System.setProperty("user.timezone", "Asia/Shanghai");
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String times = format.format(new Date(milliseconds));
		
		return times;
	}
	
	// 下面四个是默认tag的函数
	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			Log.v(TAG, msg);
	}

	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			Log.d(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			Log.e(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (isDebug)
			Log.v(tag, msg);
	}
}