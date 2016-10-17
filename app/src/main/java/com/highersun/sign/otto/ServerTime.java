package com.highersun.sign.otto;

/**
 * Author: ShenDanLai on 2016/9/20.
 * Email: 17721129316@163.com
 * 获取的服务器时间
 */

public class ServerTime {
    int hour;
    int minute;

    public ServerTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }


}
