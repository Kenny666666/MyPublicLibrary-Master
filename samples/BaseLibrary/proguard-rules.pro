# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\androidstudio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
-ignorewarnings

# 指定代码的压缩级别
-optimizationpasses 5
# 不使用大小写混合
-dontusemixedcaseclassnames
# 混淆第三方jar
-dontskipnonpubliclibraryclasses
# 混淆时不做预校验
-dontpreverify
 # 混淆时记录日志
-verbose
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

 # 保持哪些类不被混淆：四大组件，应用类，配置类等等
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

 # 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

 # 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

 # 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

 # 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

 # 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
 # 这个主要是在layout中写的onclick方法android:onclick="onClick"，不进行混淆
 -keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
 }

 #保持注解
 -keepattributes *Annotation*

  #不混淆org.apache.http.legacy.jar
  -dontwarn android.net.compatibility.**
  -dontwarn android.net.http.**
  -dontwarn com.android.internal.http.multipart.**
  -dontwarn org.apache.**
  -keep class android.net.compatibility.**{*;}
  -keep class android.net.http.**{*;}
  -keep class com.android.internal.http.multipart.**{*;}
  -keep class org.apache.** { *;}
  #不混淆ormlite-android-4.48.jar
  -dontwarn com.j256.ormlite.android.**
  -dontwarn com.j256.ormlite.db.**
  -dontwarn com.j256.ormlite.**
  -keep class com.j256.ormlite.android.** { *;}
  -keep class com.j256.ormlite.db.** { *;}
  -keep class com.j256.ormlite.** { *;}
  #不混淆gson-2.2.4.jar
  -dontwarn com.google.gson.**
  -keep class com.google.gson.** { *;}
  #不混淆universal-image-loader-1.9.4.jar
  -dontwarn com.nostra13.universalimageloader.**
  -keep class com.nostra13.universalimageloader.** { *;}
  #不混淆volley.jar
  -dontwarn com.android.volley.**
  -keep class com.android.volley.** { *;}
  #不混淆litepal-1.2.0-src.jar
  -dontwarn org.litepal.**
  -keep class org.litepal.** { *;}
  #不混淆android-push.jar
  -dontwarn com.kenai.jbosh.**
  -dontwarn com.novell.sasl.client.**
  -dontwarn de.measite.smack.**
  -dontwarn org.androidpn.**
  -dontwarn org.apache.harmony.javax.security.**
  -dontwarn org.apache.qpid.management.common.sasl.**
  -dontwarn org.jivesoftware.**
  -dontwarn org.xbill.DNS.**
  -keep class com.kenai.jbosh.** { *;}
  -keep class com.novell.sasl.client.** { *;}
  -keep class de.measite.smack.** { *;}
  -keep class org.androidpn.** { *;}
  -keep class org.apache.harmony.javax.security.** { *;}
  -keep class org.apache.qpid.management.common.sasl.** { *;}
  -keep class org.jivesoftware.** { *;}
  -keep class org.xbill.DNS.** { *;}
  #腾讯bugly
  -keep public class com.tencent.bugly.**{*;}
# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
