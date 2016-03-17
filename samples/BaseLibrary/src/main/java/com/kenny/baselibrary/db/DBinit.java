package com.kenny.baselibrary.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.kenny.baselibrary.R;
import com.kenny.baselibrary.utils.common.FileUtil;
import com.kenny.baselibrary.utils.common.L;
import com.kenny.baselibrary.utils.common.ResourcesConstant;
import com.kenny.baselibrary.utils.common.T;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * 数据库工具类
 * @author kenny
 * @time 2016/1/26 22:34
 */
public class DBinit {
	
	/**
	 * 删除指定文件
	 * @param context 上下文
	 * @param dir 文件路径
	 * @return
	 */
	public static boolean deleteFile(Context context,String dir){
		FileUtil fileUtil = new FileUtil(context);
		
		return fileUtil.deleteDirectory(dir);
	}
	
	/**
	 * 拷贝数据库文件
	 */
	public static boolean copyDBFile(Context context) {
		FileUtil fileUtil = new FileUtil(context);
		return fileUtil.copyFileFromAsset(ResourcesConstant.ASSET_DB_FILE_PATH, ResourcesConstant.DB_DIR_PATH, ResourcesConstant.DB_File_Name);
	}
	
	/**
	 * 初始化数据库
	 * @param context
	 * @return
	 */
	public static boolean initDB(final Context context){
		try {
			DBHelper.revert();
			DBHelper.initDBHelper(context, ResourcesConstant.DB_VERSION, ResourcesConstant.getDBPath(), new DBHelper.DatabaseUpdate() {

				@Override
				public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
					AssetManager assets = context.getAssets();
					for (int i = oldVersion + 1; i <= newVersion; i++) {

						try {
							InputStreamReader ir = new InputStreamReader(assets.open("upgraded_version/v" + i + ".sql"), "utf-8");
							BufferedReader br = new BufferedReader(ir);
							String inLine = null;
							while ((inLine = br.readLine()) != null) {
								db.execSQL(inLine);
							}
							ir.close();
							br.close();
						} catch (Exception e) {
							L.saveExceptionToFile(context, "数据库版本升级读取文件出错,升级版本 :" + i, e);
						}

					}
					T.showShort(context, context.getResources().getString(R.string.revert_database_suc));
				}

				@Override
				public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {

				}
			});
			DBHelper.getHelper(context).getReadableDatabase().close();
		} catch (Exception e) {
			String whatLocationExc = "还原数据库出错!错误信息 :" + e.getMessage();
			L.saveExceptionToFile(context, whatLocationExc, e);
			return false;
		}
		return true;
	} 
}
