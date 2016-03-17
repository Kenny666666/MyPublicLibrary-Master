package com.kenny.baselibrary.utils.network.filedown;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * Created by kenny on 15/5/5.
 */
/**
 * 文件下载线程
 * @author kenny
 * @time 2015/5/5 22:42
 */
public class FileDownloadThread extends Thread {

    /**
     * 当前下载是否完成
     */
    private boolean isCompleted = false;
    /**
     * 当前下载文件长度
     */
    private int downloadLength = 0;
    /**
     * 文件保存路径
     */
    private File file;
    /**
     * 文件下载路径
     */
    private URL downloadUrl;
    /**
     * 线程下载数据长度
     */
    private int blockSize;
    /**
     * 是否出错
     */
    private boolean isError = false;
    /**
     * 异常
     */
    public Exception error;

    /**
     * @param downloadUrl:文件下载地址
     * @param file:文件保存路径
     * @param blocksize:下载数据长度
     */
    public FileDownloadThread(URL downloadUrl, File file, int blocksize) {
        this.downloadUrl = downloadUrl;
        this.file = file;
        this.blockSize = blocksize;
    }

    private boolean quit = false;

    public void finish() {
        quit = true;
//        this.interrupt();
    }

    @Override
    public void run() {

        BufferedInputStream bis = null;
        BufferedOutputStream raf = null;
        downloadLength = (int) file.length();

        try {
            URLConnection conn = downloadUrl.openConnection();
            conn.setAllowUserInteraction(true);
            conn.setConnectTimeout(4 * 1000);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            conn.setRequestProperty("Accept", "*/*");
            //设置当前线程下载的起点、终点
            conn.setRequestProperty("Range", "bytes=" + file.length() + "-" + blockSize);

            byte[] buffer = new byte[1024];
            bis = new BufferedInputStream(conn.getInputStream());

            raf = new BufferedOutputStream(new FileOutputStream(file));
            int len;
            while ((len = bis.read(buffer)) != -1 && !quit) {
                raf.write(buffer, 0, len);
                downloadLength += len;
            }
            isCompleted = true;
            if (file.exists()){
                String prefix = ".tmp";
                String fileName = file.getName();
                String newFileName = fileName.substring(0, fileName.indexOf(prefix));

                File newFile = new File(file.getParent(),newFileName);
                file.renameTo(newFile);
            }
            quit = true;
        } catch (IOException e) {
            retry(e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    retry(e);
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    retry(e);
                }
            }
        }

    }

    /**
     * 重试
     * @param e
     */
    public void retry(Exception e) {
        error = e;
        isError = true;
        quit = true;
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 线程文件是否下载完毕
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * 下载出现异常
     * @return
     */
    public boolean isError() {
        return isError;
    }

    /**
     * 线程下载文件长度
     */
    public int getDownloadLength() {
        return downloadLength;
    }

}
