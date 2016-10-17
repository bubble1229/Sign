package com.highersun.sign.otto;

import com.squareup.otto.Bus;

/**
 * Author: ShenDanLai on 2016/9/9.
 * Email: 17721129316@163.com
 */

public class AppBus extends Bus {
    private static AppBus bus;

    public static AppBus getInstance() {
        if (bus == null) {
            bus = new AppBus();
        }
        return bus;
    }
}
