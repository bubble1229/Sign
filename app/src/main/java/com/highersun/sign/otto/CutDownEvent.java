package com.highersun.sign.otto;

/**
 * Author: ShenDanLai on 2016/9/14.
 * Email: 17721129316@163.com
 */

public class CutDownEvent {
    String hour;
    String minute;

    public CutDownEvent(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }
}
