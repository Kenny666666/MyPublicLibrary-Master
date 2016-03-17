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
    private String downloadUrl;

    /**
     * 开启的线程数
     */
    private int threadNum;

    /**
     * 保存文件路径地址
     */
    private String filePath;

    /**
     * 每一个线程的下载量
     */
    private int blockSize;

    /**
     * 回调接口（成功，出错，下载进度）
     */
    private final Listener listener;

    /**
     *
     */
    private int count = 0;

    /**
     * 文件线程
     */
    private FileDownloadThread fileDownloadThread;

    /**
     * 是否完成
     */
    private boolean isfinished = false;

    /**
     * 下载文件的信息
     */
    private DownFileInfo downFileInfo;

    /**
     * 保存的文件
     */
    private File file;

    /**
     * 文件名
     */
    private String realFileName;

    /**
     * handler更新UI，下载进度
     */
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public DownloadTask(String downloadUrl, String fileptah, Listener listener) {
        this.downloadUrl = downloadUrl;
        this.realFileName = fileptah;
        this.filePath = fileptah + ".tmp";
        this.listener = listener;

    }

    /**
     * 下载文件
     */
    @Override
    public void run() {

        downFileInfo = new DownFileInfo(DownFileInfo.DownState.progress, 0, 0);
        try {
            URL url = new URL(downloadUrl);
            URLConnection conn = url.openConnection();
            // 读取下载文件总大小
            int fileSize = conn.getContentLength();
            if (fileSize <= 0) {
                downFileInfo.setError(new VolleyError());
                mainHandler.post(new ResponseDeliveryRunnable(downFileInfo));
                return;
            }

            downFileInfo.setTotal(fileSize);

            file = new File(filePath);
            if (file.length() == fileSize) {
                downFileInfo.setCurrent(fileSize);
                downFileInfo.setState(DownFileInfo.DownState.success);
                mainHandler.post(new ResponseDeliveryRunnable(downFileInfo));
                return;
            } else if (file.length() != 0) {

                downFileInfo.setCurrent((int) file.length());
                downFileInfo.setState(DownFileInfo.DownState.progress);
                mainHandler.post(new ResponseDeliveryRunnable(downFileInfo));
            }
            // 启动线程，分别下载每个线程需要下载的部分
            fileDownloadThread = new FileDownloadThread(url, file, fileSize);
            fileDownloadThread.start();


            while (!isfinished) {
                if (fileDownloadThread.isError()) {
                    isfinished = true;
                    downFileInfo.setError(new VolleyError(fileDownloadThread.error));
                    downFileInfo.setState(DownFileInfo.DownState.error);
                    mainHandler.post(new ResponseDeliveryRunnable(downFileInfo));
                    return;
                }
                downFileInfo.setCurrent(fileDownloadThread.getDownloadLength());

                if (fileDownloadThread.isCompleted()) {
                    isfinished = true;
                    File realFile = new File(realFileName);
                    if (realFile.exists()) {
                        String nFileName = realFile.getName();
                        nFileName = nFileName.toLowerCase();
                        if (nFileName.endsWith(".zip")) {
                            ZipUtils.UnzipMoreFile(realFile, realFile.getParent());
                            realFile.delete();
                        }
                    }
                    downFileInfo.setState(DownFileInfo.DownState.success);
                } else {
                    downFileInfo.setState(DownFileInfo.DownState.progress);
                }
                mainHandler.post(new ResponseDeliveryRunnable(downFileInfo));
                Thread.sleep(100);

            }

        } catch (Exception e) {
            e.printStackTrace();
            deleteFile(e);
        }

    }

    public void deleteFile(Exception e) {
        downFileInfo.setError(new VolleyError(e));
        downFileInfo.setState(DownFileInfo.DownState.error);
        mainHandler.post(new ResponseDeliveryRunnable(downFileInfo));
        file.delete();
    }


    public void finish() {
        isfinished = true;
        if (fileDownloadThread != null)
            fileDownloadThread.finish();
    }


    public class ResponseDeliveryRunnable extends Thread {

        private DownFileInfo downFileInfo;

        public ResponseDeliveryRunnable(DownFileInfo downFileInfo) {
            this.downFileInfo = downFileInfo;
        }

        @Override
        public void run() {
            if (listener == null)
                return;

            switch (downFileInfo.getState()) {
                case success:
                    listener.success();
                    break;
                case error:
                    listener.error(downFileInfo.getError());
                    break;
                case progress:
                    listener.progress(downFileInfo.getTotal(), downFileInfo.getCurrent());
                    break;

            }
        }
    }


    public interface Listener {

        /**
         * 下载成功
         */
        public void success();

        /**
         * 下载出错
         * @param error
         */
        public void error(VolleyError error);

        /**
         * 下载进度
         * @param total 文件总长
         * @param current 文件当前下载进度
         */
        public void progress(int total, int current);
    }


}
