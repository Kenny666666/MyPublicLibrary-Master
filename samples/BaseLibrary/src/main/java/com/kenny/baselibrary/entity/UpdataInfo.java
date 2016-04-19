package com.kenny.baselibrary.entity;

/**
 * 版本更新信息实体类
 *
 * @author kenny
 * @time 2016/4/18 17:13
 */
public class UpdataInfo {
    /**
     * 版本号
     */
    private String version;
    /**
     * 版本描述
     */
    private String description;
    /**
     * apk下载地址
     */
    private String apkurl;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApkurl() {
        return apkurl;
    }

    public void setApkurl(String apkurl) {
        this.apkurl = apkurl;
    }
}
