package com.highersun.sign.ui.status;

import android.support.annotation.NonNull;
import android.util.Log;

import com.highersun.sign.api.sign.SignApi;
import com.highersun.sign.bean.MonthDataBean;
import com.highersun.sign.bean.StringBaseDataBean;
import com.highersun.sign.ui.sign.SignContract;
import com.highersun.sign.utils.ToastUtil;

import rx.Subscriber;

/**
 * Author: ShenDanLai on 2016/9/8.
 * Email: 17721129316@163.com
 */

public class StatusPresenter implements StatusContract.Presenter {
    StatusContract.View mStatusView;

    @Override
    public void getMonthStatus(String userId, String dataTime, String mCount) {
        Subscriber<MonthDataBean> subscriber = new Subscriber<MonthDataBean>() {
            @Override
            public void onCompleted() {
                mStatusView.hideLoading();
//                Log.e("getCurrent", "getCurrent");
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请检查您的网络1" + e.toString());
//                mSingView.showToastMessage("error" + e.toString() + "请检查网络");
//                Log.e("getCurrent", "error" + e.toString());
            }

            @Override
            public void onNext(MonthDataBean baseDataBean) {
                Log.e("getCurrent", baseDataBean.toString());
                mStatusView.addMonthStatus(baseDataBean);
                if (baseDataBean.getCode() == 0) {
                } else {
                    ToastUtil.showToast("获取考勤失败");
//                    mSingView.showToastMessage("获取时间失败");
                }
            }
        };
        mStatusView.showLoading();
        SignApi.getInstance().getMonthRecord(subscriber, userId, dataTime, mCount);
    }

    @Override
    public void attachView(@NonNull StatusContract.View view) {
        mStatusView = view;
    }

    @Override
    public void detachView() {
        mStatusView = null;
    }
}
