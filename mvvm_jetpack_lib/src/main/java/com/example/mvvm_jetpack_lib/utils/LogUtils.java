package com.example.mvvm_jetpack_lib.utils;

import android.util.Log;
import com.example.mvvm_jetpack_lib.ConstantKt;


public class LogUtils {

    private LogUtils() {
    }

    public static final String TAG = "LOG";

    //以后发正式版要要为下面的标志，除去log
    //BuildConfig.DEBUG

    private static boolean isDebug=ConstantKt.isDebug;

    public static void d(String tag, String message) {
        if (isDebug)
            Log.d(tag, message);
    }

    public static void v(String tag, String message) {
        if (isDebug)
            Log.v(tag, message);
    }

    public static void i(String tag, String message) {
        if (isDebug)
            Log.i(tag, message);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }

    public static void w(String tag, String message) {
        if (isDebug)
            Log.w(tag, message);
    }


    public static void d(String message) {
        d(TAG, message);
    }

    public static void v(String message) {
        v(TAG, message);
    }

    public static void i(String message) {
        i(TAG, message);
    }

    public static void e(String message) {
        e(TAG, message);
    }

    public static void w(String message) {
        w(TAG, message);
    }


}
