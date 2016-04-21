package com.kenny.baselibrary.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.kenny.baselibrary.BaseActivity;
import com.kenny.baselibrary.R;
import com.kenny.baselibrary.entity.UpdataInfo;
import com.kenny.baselibrary.utils.common.AppUtils;
import com.kenny.baselibrary.utils.common.SDCardUtils;
import com.kenny.baselibrary.utils.common.SPUtils;
import com.kenny.baselibrary.utils.common.T;
import com.kenny.baselibrary.utils.network.StringNetWorkResponse;
import com.kenny.baselibrary.utils.network.filedown.DownloadTask;

import java.io.File;
import java.util.HashMap;

/**
 * 启动画面 (0)检测版本更新
 *         (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 *         (2)是，则进入GuideActivity；否，则进入MainActivity (3)3s后执行(2)操作
 * @author kenny
 * @time 2016/4/18 16:59
 */
public class SplashActivity extends BaseActivity implements DownloadTask.Listener {

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 版本信息显示的view
     */
    private TextView tvSplashVersion;
    /**
     * 此界面父容器
     */
    private RelativeLayout rlSplashMain;
    /**
     * 版本信息实体类
     */
    private UpdataInfo mInfo;
    /**
     * 下载对话框
     */
    private ProgressDialog pd;
    /**
     * 客户端版本号
     */
    private String mVersiontext;
    /**
     * 进入主界面
     */
    private static final int GO_HOME = 1000;
    /**
     * 进入指引界面
     */
    private static final int GO_GUIDE = 1001;
    /**
     * 延迟3秒
     */
    private static final long SPLASH_DELAY_MILLIS = 3000;
    /**
     * handler
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        mContext = this;

        //初始化view
        initViews();

        //初始化数据
        initDatas();

        //窗体全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载...");

        rlSplashMain = (RelativeLayout) this.findViewById(R.id.rl_splash_main);
        //设置版本号
        tvSplashVersion = (TextView) this.findViewById(R.id.tv_splash_version);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        //获取版本信息
        mVersiontext = AppUtils.getAppVersionName(mContext);

        Log.i(TAG, "客户端版本号：" + mVersiontext);

        //让当前的activity延迟两秒中检查更新，这样稍微好点
        new Thread() {

            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        //请求服务端版本号
        requestVersionUpdate();
        //设置版本号信息
        tvSplashVersion.setText("版本号" + mVersiontext + " by kenny 出品");

        //设置动画效果
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(2000);
        rlSplashMain.startAnimation(aa);
    }

    /**
     * 升级的对话框
     */
    private void showDialogFragment(String message,String positiveButtonText,String negativeButtonText) {
        VersionUpdateDialogFragment dialog = new VersionUpdateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message",message);
        bundle.putString("positiveButtonText",positiveButtonText);
        bundle.putString("negativeButtonText",negativeButtonText);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(),"");
    }

    /**
     * 推荐使用dialogFragment弹对话框
     * @author kenny
     * @time 2016/4/19 14:26
     */
    public class VersionUpdateDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle bundle = getArguments();
            String message = bundle.getString("message");
            String positiveButtonText = bundle.getString("positiveButtonText");
            String negativeButtonText = bundle.getString("negativeButtonText");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message + mInfo.getDescription())
                    .setPositiveButton(positiveButtonText,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

//                                    Log.i(TAG, "下载apk文件" + mInfo.getApkurl());
                                    //先判断SDK是否可用
                                    if (SDCardUtils.isSDCardEnable()) {
                                        //下载文件
                                        DownloadTask task = new DownloadTask(mInfo.getApkurl(), "/sdcard/newapk.apk", SplashActivity.this);
                                        //因为是耗时操作，所以弹个对话框
                                        pd.setMax(100);
                                        pd.show();
                                        task.start();
                                    } else {
                                        T.showShort(getApplicationContext(), "sd卡不可用");
                                        //不可用照样进入主界面
                                        loadMainUI();
                                    }
                                }
                            })
                    .setNegativeButton(negativeButtonText,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

//                                    Log.i(TAG, "用户取消进入程序主界面");
                                    //用户取消，进入主界面
                                    loadMainUI();
                                }
                            });
            return builder.create();
        }
    }

    /**
     * 请求获取服务器上app版本号
     */
    private void requestVersionUpdate() {
        HashMap<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("xxx", "xxxx");
        mRequestHelp.submitPostNoDialog("http://www.csdn.net/", requestMap);
    }

    /**
     * @param response 服务端返回的版本信息数据
     * @return 是否需要更新
     */
    private boolean isNeedUpdate(StringNetWorkResponse response) {
        mInfo = new UpdataInfo();
        //得到服务端版本号
        mInfo.setVersion(response.getData());
        //得到apk下载地址
        mInfo.setApkurl(response.getData());
        //更新内容
        mInfo.setDescription(response.getData());

        Log.d(TAG, "服务器版本：" + mInfo.getVersion());
        Log.d(TAG, "客户端版本：" + mVersiontext);

        if (mVersiontext.equals(mInfo.getVersion())) {
            Log.i(TAG, "版本相同，无需升级，进入主界面");
            //进入主界面
            loadMainUI();
            return false;
        } else {
            Log.i(TAG, "版本不同，需要升级");
            return true;
        }
    }

    /**
     * 跳转到主界面
     */
    private void loadMainUI() {
        //是否第一次使用此app
        boolean isFirstUSE = (boolean) SPUtils.get(mContext,"isFirstUSE",true);
        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (!isFirstUSE) {
            // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        }
    }

    /**
     * 进入主界面
     */
    private void goHome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    /**
     * 进入指引界面
     */
    private void goGuide() {
        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    /**
     * 安装apk
     */
    private void install(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        //设置数据类型tomcat7\conf\web.xml里面包含各种文件类型参数，打开web.xml搜索apk就能找到下面这段文件类型
        //application/vnd.android.package-archive
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        //finish到当前activity，开启安装任务
        finish();
        startActivity(intent);
    }

    @Override
    public void success() {

        T.showShort(getApplicationContext(), "文件下载成功,准备安装");

        //关闭下载进度条
        pd.dismiss();

        File installFile = new File("/sdcard/newapk.apk");
        //安装apk
        install(installFile);
    }

    @Override
    public void error(VolleyError error) {

        T.showShort(getApplicationContext(), "文件下载出错");

        //关闭下载进度条
        pd.dismiss();

        //提示下载出错对话框
        showDialogFragment("下载出现异常","重试","取消");
    }

    @Override
    public void progress(int total, int current) {
        //更新下载进度条
        pd.setProgress((current / total) * 100);
    }

    /**
     * 网络请求接口回调
     *
     * @param response 返回数据
     */
    @Override
    public void onResponse(StringNetWorkResponse response) {
        super.onResponse(response);

        if (!validateResponse(response)) {
            Log.i(TAG, "请求异常，进入主界面");
            //异常，照样进入主界面
            loadMainUI();
            return;
        }

        //在创建请求的时候传入一个url，返回的数据用url区分
        String currUrl = mRequestHelp.getRequest().getUrl();

        if (currUrl.equals("http://www.csdn.net/")) {

            //判断服务器版本号和客户端的版本号是否相同
            if (isNeedUpdate(response)) {
                Log.i(TAG, "弹出升级对话框");
                showDialogFragment("本次更新了非常牛B的功能哦，赶紧下载体验吧...","马上更新","下次再说");
            }
        }
    }

    /**
     * 请求出错回调接口
     * @param e 异常信息
     */
    @Override
    public void onErrorResponse(VolleyError e) {
        super.onErrorResponse(e);
        //异常，照样进入主界面
        loadMainUI();
    }
}
