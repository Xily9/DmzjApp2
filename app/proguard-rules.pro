# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontusemixedcaseclassnames          #混淆时不使用大小写混合类名
-dontskipnonpubliclibraryclasses     #不跳过library中的非public的类
-verbose                             #打印混淆的详细信息
-dontoptimize                        #不进行优化，建议使用此选项，
-dontpreverify                       #不进行预校验,Android不需要,可加快混淆速度。
-ignorewarnings                      #忽略警告
-repackageclasses ''
-keepattributes Signature #范型
# 抛出异常时保留代码行号
-keepattributes LineNumberTable
#native方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#Gson混淆配置
-keep class com.google.gson.** { *; }

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

#实体类不混淆
-keep class com.xily.dmzj2.data.remote.model.**{*;}

#ViewHolder不混淆,会影响反射调用构造方法
-keep class * extends com.xily.dmzj2.base.BaseAdapter$BaseViewHolder{*;}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class okhttp3.internal.publicsuffix.PublicSuffixDatabase

-dontwarn com.jeremyliao.liveeventbus.**
-keep class com.jeremyliao.liveeventbus.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.arch.core.** { *; }