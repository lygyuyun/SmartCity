# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/lihui/work/AndroidStudio/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.tourcool.smartcity.BuildConfig { *; }
-keep public class * implements androidx.versionedparcelable.VersionedParcelable
#实体类不要混淆不然无法解析
-keep class com.tourcool.bean. { *; }
-keep class com.aries.library.fast.demo.entity.**{*;}
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
#
#-------------------------------------------基本不用动区域----------------------------------------------
#
#
# -----------------------------基本 -----------------------------
#

# 指定代码的压缩级别 0 - 7(指定代码进行迭代优化的次数，在Android里面默认是5，这条指令也只有在可以优化时起作用。)
-optimizationpasses 5
# 混淆时不会产生形形色色的类名(混淆时不使用大小写混合类名)
-dontusemixedcaseclassnames
# 指定不去忽略非公共的库类(不跳过library中的非public的类)
-dontskipnonpubliclibraryclasses
# 指定不去忽略包可见的库类的成员
-dontskipnonpubliclibraryclassmembers
#不进行优化，建议使用此选项，
-dontoptimize
 # 不进行预校验,Android不需要,可加快混淆速度。
-dontpreverify
# 屏蔽警告
-ignorewarnings
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 保护代码中的Annotation不被混淆
-keepattributes *Annotation*
# 避免混淆泛型, 这在JSON实体映射时非常重要
-keepattributes Signature
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
 #优化时允许访问并修改有修饰符的类和类的成员，这可以提高优化步骤的结果。
# 比如，当内联一个公共的getter方法时，这也可能需要外地公共访问。
# 虽然java二进制规范不需要这个，要不然有的虚拟机处理这些代码会有问题。当有优化和使用-repackageclasses时才适用。
#指示语：不能用这个指令处理库中的代码，因为有的类和类成员没有设计成public ,而在api中可能变成public
-allowaccessmodification
#当有优化和使用-repackageclasses时才适用。
-repackageclasses ''
 # 混淆时记录日志(打印混淆的详细信息)
 # 这句话能够使我们的项目混淆后产生映射文件
 # 包含有类名->混淆后类名的映射关系
-verbose

-keepclassmembers class ** {
     @com.squareup.otto.Subscribe public *;
     @com.squareup.otto.Produce public *;
}

#-----------------------------------------------分割线----------------------------------------------------
  # 支付宝人脸识别混淆
-keep public class com.alipay.mobile.security.zim.api.**{
    public <fields>;
    public <methods>;
}

-keep class com.alipay.mobile.security.zim.biz.ZIMFacadeBuilder {
  !private <fields>;
   !private <methods>;
}

-keep class com.alipay.android.phone.mobilecommon.logger.AlipayMonitorLogService {
    !private <fields>;
    !private <methods>;
}

-keep class com.alipay.android.phone.mobilecommon.rpc.AlipayRpcService {
    !private <fields>;
    !private <methods>;
}

-keep class com.alipay.android.phone.mobilecommon.apsecurity.AlipayApSecurityService {
    !private <fields>;
    !private <methods>;
}

-keep class com.alipay.zoloz.toyger.bean.ToygerMetaInfo {
    !private <fields>;
    !private <methods>;
}

-keep class com.alipay.zoloz.toyger.algorithm.** { *; }

-keep class com.alipay.zoloz.toyger.blob.** {
    !private <fields>;
    !private <methods>;
}

-keep class com.alipay.zoloz.toyger.face.** {
    !private <fields>;
    !private <methods>;
}

-keep class com.alipay.zoloz.hardware.camera.impl.** {
    !private <fields>;
    !private <methods>;
}


-keep public class com.alipay.mobile.security.zim.plugin.**{
    public <fields>;
    public <methods>;
}

-keep class * extends com.alipay.mobile.security.zim.gw.BaseGwService{
    !private <fields>;
    !private <methods>;
}

-keep class * extends com.alipay.mobile.security.bio.service.BioMetaInfo{
    !private <fields>;
    !private <methods>;
}

-keep class com.alipay.zoloz.toyger.workspace.FaceRemoteConfig{
    *;
}

-keep public class com.alipay.zoloz.toyger.**{
    *;
}

-keep public class com.alipay.mobile.security.zim.gw.**{
    *;
}
-keep class com.alta.audiopolicyloader.* {*;}
#----------------------------------------分割线----------------------------------------------------
-keep class org.greenrobot.greendao.**{*;}
-keep public interface org.greenrobot.greendao.**
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class net.sqlcipher.database.**{*;}
-keep public interface net.sqlcipher.database.**
-dontwarn net.sqlcipher.database.**
-dontwarn org.greenrobot.greendao.**

-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static void dropTable(org.greenrobot.greendao.database.Database, boolean);
    public static void createTable(org.greenrobot.greendao.database.Database, boolean);
}
