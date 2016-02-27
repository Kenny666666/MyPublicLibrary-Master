package com.kenny.baselibrary;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.j256.ormlite.support.ConnectionSource;
import com.kenny.baselibrary.db.DBHelper;
import com.kenny.baselibrary.utils.common.FileUtil;
import com.kenny.baselibrary.utils.common.L;
import com.kenny.baselibrary.utils.common.ResourcesConstant;
import com.kenny.baselibrary.utils.common.SPUtils;
import com.kenny.baselibrary.utils.common.Utility;
import com.kenny.baselibrary.utils.crash.CustomCrashHandler;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePalApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * description 应用程序application，初始化网络请求队列，推送，检测数据库升级，图片管理框架，异常捕获机制等
 * Created by kenny on 2016/1/26.
 */
public class BaseLibraryApplication extends LitePalApplication {

    /**应用中维护一个单例的请求队列*/
    private static RequestQueue requestQueue;
    private static BaseLibraryApplication baseApplication;
    public static BaseLibraryApplication getInstance() {
        return baseApplication;
    }
    public static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getInstance().getBaseContext());
        }
        return requestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        //Crash机制，全局异常捕获
        CustomCrashHandler mCustomCrashHandler = CustomCrashHandler.getInstance();
        mCustomCrashHandler.setCustomCrashHanler(getApplicationContext());
        //腾讯bugly，false--上传，true开发中不上传
        CrashReport.initCrashReport(getApplicationContext(), "900018995", true);
        //初始化图片管理框架
        initImageLoader(getApplicationContext());
        //初始化数据库
        copyDBFile();
        initOrUpgradeDB();
        //开启推送服务
//        ServiceManager serviceManager = new ServiceManager(getApplicationContext());
//        serviceManager.setNotificationIcon(R.drawable.notification);
//        serviceManager.startService();
    }

    /**
     * 初始化图片管理框架
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 拷贝数据库文件
     */
    private void copyDBFile() {
        FileUtil fileUtil = new FileUtil(getApplicationContext());
        File file = new File(ResourcesConstant.getDBPath());
        if ((file != null && !file.exists())
                || ( Utility.isEmpty(SPUtils.get(getApplicationContext(), ResourcesConstant.COPY_DB_FLAG,"")))) {
            fileUtil.copyFileFromAsset(ResourcesConstant.ASSET_DB_FILE_PATH, ResourcesConstant.DB_DIR_PATH, ResourcesConstant.DB_File_Name);
            SPUtils.put(getApplicationContext(), ResourcesConstant.COPY_DB_FLAG, "yes");
        }
    }

    /**
     * 初始化或升级sdk中的数据库
     */
    private void initOrUpgradeDB(){
        try {
            DBHelper.initDBHelper(this, ResourcesConstant.DB_VERSION, ResourcesConstant.getDBPath(), new DBHelper.DatabaseUpdate() {

                @Override
                public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

                    AssetManager assets = getAssets();
                    for (int i = oldVersion + 1; i <= newVersion; i++) {

                        try {
                            InputStreamReader ir = new InputStreamReader(assets.open("upgraded_version/v" + i + ".sql"), "utf-8");
                            BufferedReader br = new BufferedReader(ir);
                            String inLine = null;
                            while ((inLine = br.readLine()) != null) {
                                try {
                                    db.execSQL(inLine);
                                } catch (Exception sqlEx) {
                                    L.saveExceptionToFile(BaseLibraryApplication.this, "数据库版本升级读取文件出错,升级版本 :" + i, sqlEx);
                                }
                            }
                            ir.close();
                            br.close();
                        } catch (Exception e) {
                            L.saveExceptionToFile(BaseLibraryApplication.this, "数据库版本升级读取文件出错,升级版本 :" + i, e);
                        }
                    }
                }

                @Override
                public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {

                }
            });
            DBHelper.getHelper(getApplicationContext()).getReadableDatabase().close();
        } catch (Exception e) {
            String whatLocationExc = "BaseLibraryApplication(数据库升级出现异常! 方法 : initOrUpgradeDB" + " 错误信息 :" + e.getMessage();
            L.saveExceptionToFile(this, whatLocationExc, e);
        }
    }
}
