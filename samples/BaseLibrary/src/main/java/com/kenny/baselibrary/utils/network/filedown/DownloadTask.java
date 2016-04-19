package com.kenny.baselibrary.utils.network.filedown;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.VolleyError;
import com.kenny.baselibrary.utils.common.ZipUtils;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;


/**
 * 下载任务线程
 * @author kenny
 * @time 2015/5/5 22:42
 */
public class DownloadTask extends Thread {

    /**
     * 下载链接地址
     */
    private String mDownloadUrl;

    /**
     * 开启的线程数
     */
    private int threadNum;

    /**
     * 保存文件路径地址
     */
    private String mSaveFilePath;

    /**
     * 每一个线程的下载量
     */
    private int blockSize;

    /**
     * 回调接口（成功，出错，下载进度）
     */
    private final Listener mListener;

    /**
     *
     */
    private int count = 0;

    /**
     * 文件线程
     */
    private FileDownloadThread mFileDownloadThread;

    /**
     * 是否完成
     */
    private boolean mIsfinished = false;

    /**
     * 下载文件的信息
     */
    private DownFileInfo mDownFileInfo;

    /**
     * 保存的文件
     */
    private File mSaveFile;

    /**
     * 文件名
     */
    private String mRealFileName;

    /**
     * handler更新UI，下载进度
     */
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     *
     * @param downloadUrl 文件下载地址
     * @param filepath 文件保存路径
     * @param listener 回调接口(成功，出错，进度)
     */
    public DownloadTask(String downloadUrl, String filepath, Listener listener) {
        this.mDownloadUrl = downloadUrl;
        this.mRealFileName = filepath;
        this.mSaveFilePath = filepath + ".tmp";
        this.mListener = listener;

    }

    /**
     * 下载文件
     */
    @Override
    public void run() {

        mDownFileInfo = new DownFileInfo(DownFileInfo.DownState.progress, 0, 0);
        try {
            URL url = new URL(mDownloadUrl);
            URLConnection conn = url.openConnection();
            // 读取下载文件总大小
            int fileSize = conn.getContentLength();
            if (fileSize <= 0) {
                mDownFileInfo.setError(new VolleyError());
                mainHandler.post(new ResponseDeliveryRunnable(mDownFileInfo));
                return;
            }

            mDownFileInfo.setTotal(fileSize);

            mSaveFile = new File(mSaveFilePath);
            if (mSaveFile.length() == fileSize) {
                mDownFileInfo.setCurrent(fileSize);
                mDownFileInfo.setState(DownFileInfo.DownState.success);
                mainHandler.post(new ResponseDeliveryRunnable(mDownFileInfo));
                return;
            } else if (mSaveFile.length() != 0) {

                mDownFileInfo.setCurrent((int) mSaveFile.length());
                mDownFileInfo.setState(DownFileInfo.DownState.progress);
                mainHandler.post(new ResponseDeliveryRunnable(mDownFileInfo));
            }
            // 启动线程，分别下载每个线程需要下载的部分
            mFileDownloadThread = new FileDownloadThread(url, mSaveFile, fileSize);
            mFileDownloadThread.start();


            while (!mIsfinished) {
                if (mFileDownloadThread.isError()) {
                    mIsfinished = true;
                    mDownFileInfo.setError(new VolleyError(mFileDownloadThread.error));
                    mDownFileInfo.setState(DownFileInfo.DownState.error);
                    mainHandler.post(new ResponseDeliveryRunnable(mDownFileInfo));
                    return;
                }
                mDownFileInfo.setCurrent(mFileDownloadThread.getDownloadLength());

                if (mFileDownloadThread.isCompleted()) {
                    mIsfinished = true;
                    File realFile = new File(mRealFileName);
                    if (realFile.exists()) {
                        String nFileName = realFile.getName();
                        nFileName = nFileName.toLowerCase();
                        if (nFileName.endsWith(".zip")) {
                            ZipUtils.UnzipMoreFile(realFile, realFile.getParent());
                            realFile.delete();
                        }
                    }
                    mDownFileInfo.setState(DownFileInfo.DownState.success);
                } else {
                    mDownFileInfo.setState(DownFileInfo.DownState.progress);
                }
                mainHandler.post(new ResponseDeliveryRunnable(mDownFileInfo));
                Thread.sleep(100);
            }

        } catch (Exception e) {
            e.printStackTrace();
            deleteFile(e);
        }

    }

    public void deleteFile(Exception e) {
        mDownFileInfo.setError(new VolleyError(e));
        mDownFileInfo.setState(DownFileInfo.DownState.error);
        mainHandler.post(new ResponseDeliveryRunnable(mDownFileInfo));
        mSaveFile.delete();
    }


    public void finish() {
        mIsfinished = true;
        if (mFileDownloadThread != null)
            mFileDownloadThread.finish();
    }


    public class ResponseDeliveryRunnable extends Thread {

        private DownFileInfo mDownFileInfo;

        public ResponseDeliveryRunnable(DownFileInfo mDownFileInfo) {
            this.mDownFileInfo = mDownFileInfo;
        }

        @Override
        public void run() {
            if (mListener == null)
                return;

            switch (mDownFileInfo.getState()) {
                case success:
                    mListener.success();
                    break;
                case error:
                    mListener.error(mDownFileInfo.getError());
                    break;
                case progress:
                    mListener.progress(mDownFileInfo.getTotal(), mDownFileInfo.getCurrent());
                    break;

            }
        }
    }


    public interface Listener {

        /**
         * 下载成功
         */
        void success();

        /**
         * 下载出错
         * @param error
         */
        void error(VolleyError error);

        /**
         * 下载进度
         * @param total 文件总长
         * @param current 文件当前下载进度
         */
        void progress(int total, int current);
    }


}
