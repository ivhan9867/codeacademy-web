# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Room Database
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
-keep class androidx.room.** { *; }
-keep class com.example.data.local.** { *; }
-keepclassmembers class com.example.data.local.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <methods>;
}
-keep class * implements androidx.room.migration.Migration

# Data Models & Entities (Room & Supabase)
-keep class com.example.data.models.** { *; }
-keepclassmembers class com.example.data.models.** { *; }
-keep class com.example.data.model.** { *; }
-keepclassmembers class com.example.data.model.** { *; }
-keep class com.example.data.remote.supabase.** { *; }
-keepclassmembers class com.example.data.remote.supabase.** { *; }

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}
-keepclassmembers class * {
    companion object;
}
-keepclassmembers class * {
    *** Companion;
}
-keepclassmembers class * implements kotlinx.serialization.KSerializer {
    <fields>;
    <methods>;
}
-keepclassmembers class * {
    @kotlinx.serialization.SerialInfo <methods>;
}

# ViewModel
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# OkHttp & Retrofit
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**

# Moshi
-keepclassmembers class * {
    @com.squareup.moshi.* <methods>;
}
-dontwarn com.squareup.moshi.**

# AI Tutor & BuildConfig
-keep class com.example.BuildConfig { *; }
-keepclassmembers class com.example.BuildConfig {
    public static final java.lang.String GEMINI_API_KEY;
}
-keep class com.example.ai.** { *; }
-keepclassmembers class com.example.ai.** { *; }

# WebView Javascript Interface (if needed)
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

