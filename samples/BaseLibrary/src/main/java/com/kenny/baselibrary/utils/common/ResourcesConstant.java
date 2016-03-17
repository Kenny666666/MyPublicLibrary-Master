package com.kenny.baselibrary.utils.common;

import android.os.Environment;

import java.io.File;


/**
 * 存放在SDCard里资源常量
 * @author kenny
 * @time 2016/2/17 22:40
 */
public class ResourcesConstant {

	/**
	 * 获取存放在SDCard里数据库文件绝对路径
	 * 
	 * @return
	 */
	public static String getDBPath() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File file = new File(DB_DIR_PATH + File.separator);
			if(!file.exists()){
				file.mkdirs();
			}
			return ResourcesConstant.DB_File_Path;
		} else {
			return "";
		}
	}

	public static String getUpdateFileDirPath() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return UPDATE_FILE_DIR;
		} else {
			return "";
		}
	}
	
	public static final int DB_VERSION = 5;

	/**
	 * DB 文件名
	 */
	public static final String DB_File_Name = "base_library.db";

	/**
	 * 存放在asset里数据库文件的相对路径
	 */
	public static final String ASSET_DB_FILE_PATH = "database/" + DB_File_Name;

	/**
	 * SDCard的绝对路径 
	 */
	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

	/**
	 * 项目所有文件在SD卡的目录的相对路径
	 */
	private static final String ROOT_DIR_RELATIVE_PATH = "/Kenny/BaseLibrary";
	
	/**
	 * 该项目存放在SD卡里资源的根目录的决对路径
	 */
	public static final String ROOT_DIR_PATH = SDCARD_PATH + ROOT_DIR_RELATIVE_PATH;

	/**
	 * 存放DB文件目录的路径(最后没带'/')
	 */
	public static final String DB_DIR_PATH = ROOT_DIR_PATH + "/db";

	/**
	 * DB的文件绝对路径
	 */
	public static final String DB_File_Path = DB_DIR_PATH + File.separator + DB_File_Name;
	
	/**
	 * 存放升级apk文件的目录绝对路径(没带 '/')
	 */
	public static final String UPDATE_FILE_DIR = ROOT_DIR_PATH + "/updateFile/";
	
	/**
	 * 存放临时文件的目录绝对路径(没带 '/')
	 */
	public static final String TEMP_FILE_DIR = ROOT_DIR_PATH + "/tempFile/";
	
	/**
	 * 临时文件
	 */
	public static final String TEMP_FILE_PATH = TEMP_FILE_DIR+"/temp.xml";

	/** 图库中的图片相对地址 */
	public static String IMAGE_LOCAL_PATH = ROOT_DIR_RELATIVE_PATH + "/image/local/";
	
	/** 附件的图片相对地址 */
	public static String IMAGE_ATTACHMENT_PATH = ROOT_DIR_RELATIVE_PATH + "/image/attachment/";

	/** 屏幕截图的图片相对地址 */
	public static String IMAGE_SCREEN_SHOT_PATH = ROOT_DIR_RELATIVE_PATH + "/image/shot/";

	/** 日志文件地址 */
	public static String LOG_FILE_PATH = ROOT_DIR_RELATIVE_PATH + "/log/";
	
	/** 复制db文件的标识*/
	public static String COPY_DB_FLAG="copy_v1";
	

}
