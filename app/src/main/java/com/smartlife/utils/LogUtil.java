package com.smartlife.utils;

import android.util.Log;

/**
 * 描述：封装的日志类
 * 作者：傅健
 * 创建时间：2017/7/31 10:38
 */
public class LogUtil {

    private static String TAG;

    private final static int logLevel = Log.VERBOSE;

    private static LogUtil mLogUtil;

    private LogUtil() {

    }

    public static LogUtil getLog() {
        if (mLogUtil == null) {
            mLogUtil = new LogUtil();
        }
        return mLogUtil;
    }

    public static void doException(Exception e) {
        if (Constants.isDebug) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * Get The Current Function Name
     *
     * @return
     */
    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }
            // TAG = st.getFileName();
            TAG = "SmartHome";
            return "[ " + Thread.currentThread().getName() + ": " + st.getFileName() + ":" + st.getLineNumber() + " " + st.getMethodName() + " ]";
        }
        return null;
    }

    /**
     * The Log Level:i
     *
     * @param str
     */
    public void i(Object str) {
        if (Constants.isDebug) {
            if (logLevel <= Log.INFO) {
                String name = getFunctionName();
                if (name != null) {
                    Log.i(TAG, name + " - " + str);
                } else {
                    Log.i(TAG, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:d
     *
     * @param str
     */
    public void d(Object str) {
        if (Constants.isDebug) {
            if (logLevel <= Log.DEBUG) {
                String name = getFunctionName();
                if (name != null) {
                    Log.d(TAG, name + " - " + str);
                } else {
                    Log.d(TAG, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:V
     *
     * @param str
     */
    public void v(Object str) {
        if (Constants.isDebug) {
            if (logLevel <= Log.VERBOSE) {
                String name = getFunctionName();
                if (name != null) {
                    Log.v(TAG, name + " - " + str);
                } else {
                    Log.v(TAG, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:w
     *
     * @param str
     */
    public void w(Object str) {
        if (Constants.isDebug) {
            if (logLevel <= Log.WARN) {
                String name = getFunctionName();
                if (name != null) {
                    Log.w(TAG, name + " - " + str);
                } else {
                    Log.w(TAG, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param str
     */
    public void e(Object str) {
        if (Constants.isDebug) {
            if (logLevel <= Log.ERROR) {
                String name = getFunctionName();
                if (name != null) {
                    Log.e(TAG, name + " - " + str);
                } else {
                    Log.e(TAG, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param ex
     */
    public void e(Exception ex) {
        if (Constants.isDebug) {
            if (logLevel <= Log.ERROR) {
                Log.e(TAG, "error", ex);
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param log
     * @param tr
     */
    public void e(String log, Throwable tr) {
        if (Constants.isDebug) {
            String line = getFunctionName();
            Log.e(TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + line + ":] " + log + "\n", tr);
        }
    }
}
