package com.service.codingtest.network;

import android.util.Log;

import com.service.codingtest.BuildConfig;

public class MLog {
    public static boolean displayLog = BuildConfig.DEBUG;
	private static String mTag = "MLog ";

    public static void i(String tag, String message) {
        if (displayLog) Log.i(mTag+tag, message);
    }

    public static void w(String tag, String message) {
        if (displayLog) Log.w(mTag+tag, message);
    }

    public static void w(String tag, String message, Exception e) {
        if (displayLog) Log.w(mTag+tag, message, e);
    }

    public static void w(String tag, String message, Throwable t) {
        if (displayLog) Log.w(mTag+tag, message, t);
    }

    public static void d(String tag, String message) {
        if (displayLog) Log.d(mTag+tag, message);
    }

    public static void e(String tag, String message) {
        if (displayLog) Log.e(mTag+tag, message);
    }

    public static void e(String tag, String message, Throwable t) {
        if (displayLog) Log.e(mTag+tag, message, t);
    }

    public static void v(String tag, String message) {
        if (displayLog) Log.v(mTag+tag, message);
    }

}