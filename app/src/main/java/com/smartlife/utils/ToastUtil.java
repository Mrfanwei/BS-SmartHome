package com.smartlife.utils;

import android.content.Context;
import android.widget.Toast;

import com.smartlife.MainApplication;

public class ToastUtil {

    //主线程toast方法
    public static void showtomain(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    private static Toast mToast;

    /**
     * Toast短时间的提示
     *
     * @param info 需要显示的文字
     */
    public static void showShort(final String info) {
        if (StringUtils.isNotEmpty(info)) {
            UIUtils.postTaskSafely(() -> {
                if (null == mToast) {
                    mToast = Toast.makeText(MainApplication.getInstance(), info, Toast.LENGTH_SHORT);
                }
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.setText(info);
                mToast.show();
            });
        }
    }

    /**
     * Toast长时间的提示
     *
     * @param info 需要显示的文字
     */
    public static void showLong(final String info) {
        if (StringUtils.isNotEmpty(info)) {
            UIUtils.postTaskSafely(() -> {
                if (null == mToast) {
                    mToast = Toast.makeText(MainApplication.getInstance(), info, Toast.LENGTH_LONG);
                }
                mToast.setDuration(Toast.LENGTH_LONG);
                mToast.setText(info);
                mToast.show();
            });
        }
    }

    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
