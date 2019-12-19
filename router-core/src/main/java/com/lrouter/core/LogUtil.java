package com.lrouter.core;

import android.util.Log;

/**
 * 日志工具类
 * @Author: lqc
 */
public class LogUtil {
    private static final String TAG = "LRouter-->>";
    private static boolean sLoggable = false;

    public static void setEnabled(boolean loggable) {
        sLoggable = loggable;
    }

    public static void i(String msg) {
        if (sLoggable && msg != null) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sLoggable && msg != null) {
            Log.i(tag, msg);
        }
    }

    public static void w(String msg) {
        if (sLoggable && msg != null) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (sLoggable&&msg != null){
            Log.e(TAG, msg);
        }
    }
    public static void d(String msg) {
        if (sLoggable&&msg != null){
            Log.d(TAG, msg);
        }
    }

}
