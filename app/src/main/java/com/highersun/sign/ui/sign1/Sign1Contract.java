package com.highersun.sign.ui.sign1;

import com.highersun.sign.ui.BasePresenter;
import com.highersun.sign.ui.BaseView;

/**
 * Author: ShenDanLai on 2016/9/13.
 * Email: 17721129316@163.com
 */

public interface Sign1Contract {
    interface View extends BaseView {
        void refreshTime(int hour, int minute);

        void setIbeconId(String ibeconId);

    }

    interface Presenter extends BasePresenter<View> {
        void getCurrent();  //获取当前时间

        void postSign(String userId, String signEquipment, String type, String iBeaconId); //提交考勤

        void getSignInfo(String userId, String iBeaconId, String signTime, String Model);  //获取考勤信息

        void getIbeaconInfo(String pageNo, String pageSize);


    }
}
