package com.highersun.sign;

import android.app.Application;

import com.highersun.sign.utils.ToastUtil;

/**
 * Author: ShenDanLai on 2016/9/6.
 * Email: 17721129316@163.com
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ToastUtil.register(this);
    }
}
