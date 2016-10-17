package com.highersun.sign.ui.status;

import com.highersun.sign.bean.MonthDataBean;
import com.highersun.sign.ui.BasePresenter;
import com.highersun.sign.ui.BaseView;
import com.highersun.sign.ui.sign.SignContract;

/**
 * Author: ShenDanLai on 2016/9/8.
 * Email: 17721129316@163.com
 */

public interface StatusContract {
    interface View extends BaseView {
        void addMonthStatus(MonthDataBean monthDataBean);
    }
    interface Presenter extends BasePresenter<StatusContract.View> {
        void getMonthStatus(String userId,String dataTime,String mCount);
    }
}
