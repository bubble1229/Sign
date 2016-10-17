package com.highersun.sign.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Author: ShenDanLai on 2016/9/6.
 * Email: 17721129316@163.com
 */

public class ToastUtil {

    public static Context mContext;

    private ToastUtil() {
    }

    public static void register(Context context) {
        mContext = context.getApplicationContext();
    }

    public static void showToast(int resId) {
        Toast.makeText(mContext, mContext.getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}