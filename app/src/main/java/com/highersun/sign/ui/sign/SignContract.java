package com.highersun.sign.ui.sign;

import com.highersun.sign.ui.BasePresenter;
import com.highersun.sign.ui.BaseView;
import com.highersun.sign.ui.login.LoginContract;

/**
 * Created by admin on 2016/8/16.
 */

public interface SignContract {
    interface View extends BaseView {
        void showPop();

        void hidePop( int duration);

        void showUpdate(String model,String formerTime);

        void hideUpdate();

        void showToastMessage(String toastMessage);
        void setCurrentTime(String currentTime);
        void signAfterCheck(String  model);
    }

    interface Presenter extends BasePresenter<View> {
        void getCurrent();  //获取当前时间

        void postSign(String userId, String signEquipment, String type, String iBeaconId); //提交考勤

        void getSignInfo(String userId, String iBeaconId, String signTime,String Model);  //获取考勤信息


    }
}
