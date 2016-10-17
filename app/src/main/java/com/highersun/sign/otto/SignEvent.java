package com.highersun.sign.otto;

/**
 * Author: ShenDanLai on 2016/9/9.
 * Email: 17721129316@163.com
 */

public class SignEvent {
    private String signTime;
    private String singMode;

    public SignEvent(String signTime, String singMode) {
        this.signTime = signTime;
        this.singMode = singMode;
    }

    public String getSignTime() {
        return signTime;
    }

    public String getSingMode() {
        return singMode;
    }
}
