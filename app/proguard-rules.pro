-keepclassmembers class com.paytm.pgsdk.PaytmWebView$PaytmJavaScriptInterface {
 	public *;
 }

-keep class com.shockwave.*

-keepclasseswithmembers class net.manish.mybscitsem06.Blogger** { *; }
-keepclasseswithmembers class net.manish.mybscitsem06.Blog** { *; }
-keepclasseswithmembers class net.manish.mybscitsem06.DetailActivity** { *; }
-keepclasseswithmembers class net.manish.mybscitsem06.BloggerAPI** { *; }
-keepclasseswithmembers class net.manish.mybscitsem06.PostAdapter** { *; }
-keepclasseswithmembers class net.manish.mybscitsem06.PostList** { *; }
-keepclasseswithmembers class net.manish.mybscitsem06.Item** { *; }
-keepclasseswithmembers class net.manish.mybscitsem06.Image** { *; }

-keepattributes SourceFile, LineNumberTable
-keepattributes LocalVariableTable, LocalVariableTypeTable
-keepattributes *Annotation*, Signature, Exception

-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keepnames class org.jsoup.nodes.Entities