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
-optimizationpasses 5# 指定代码的压缩级别
-dontusemixedcaseclassnames# 混淆时不生成大小写混合的类名
-dontskipnonpubliclibraryclasses #不忽略非公共的类库
-dontskipnonpubliclibraryclassmembers
-dontpreverify # 混淆时是否做预校验
-verbose# 混淆过程中打印详细信息
-ignorewarnings
#-libraryjars libs(*.jar;) #添加支持的jar(引入libs下的所有jar包)
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*# 混淆时所采用的算法
-printmapping build/outputs/mapping/release/mapping.txt
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes Exception, Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,Synthetic,EnclosingMethod
#2.默认保留区
 -keep public class * extends android.app.Activity # 保持哪些类不被混淆
 -keep public class * extends android.app.Application # 保持哪些类不被混淆
# -keep public class * extends android.app.MultiDexApplication
 -keep public class * extends android.app.Service # 保持哪些类不被混淆
 -keep public class * extends android.content.BroadcastReceiver # 保持哪些类不被混淆
 -keep public class * extends android.content.ContentProvider # 保持哪些类不被混淆
 -keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
 -keep public class * extends android.preference.Preference # 保持哪些类不被混淆
# -keep public class com.android.vending.licensing.ILicensingService # 保持哪些类不被混淆
 -keep public class * extends android.view.View
# -keep class android.support.** {*;}
# -keep class android.support.v4.view.** { *; }
# -keep class android.view.View$OnUnhandledKeyEventListener { *; }

 -keepclasseswithmembernames class * { # 保持 native 方法不被混淆
    native <methods>;
 }

 -keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
 }

 # 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
   public static **[] values();
   public static ** valueOf(java.lang.String);
}
#-keepclassmembers class * implements android.os.Parcelable {#保持Parcelable不被混淆
#   public static final android.os.Parcelable$Creator *;
#}
 #保持注解继承类不混淆
-keep class * extends java.lang.annotation.Annotation {*;}
 #保持Serializable实现类不被混淆
-keep class * implements java.io.Serializable {*;}
-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   private static final java.io.ObjectStreamField[] serialPersistentFields;
   !static !transient <fields>;
   !private <fields>;
   !private <methods>;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
   void *(**On*Event);
}
#自定义组件不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#不混淆资源类
-keep class **.R$* {
 *;
}
#保持R类静态成员不被混淆
-keepclassmembers class **.R$* {
    public static <fields>;
}

   #不混内部类
-keepclassmembers class **.$* {
 public <fields>;
 public <methods>;
}
#3.webview
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.webView, jav.lang.String);
#}
#-dontwarn android.support.v4.**
#-keep class android.support.v4.** { *; }
#-keep interface android.support.v4.app.** { *; }
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.app.Fragment

