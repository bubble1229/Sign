package com.highersun.sign.bean;

import android.text.TextUtils;

/**
 * Created by admin on 2016/8/15.
 */

public class BaseDataBean {
    private int code = -100;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        if (TextUtils.isEmpty(msg)) {
            return "后台正在处理,请联系客服....";
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseDataBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
