package com.highersun.sign.ui.sign;

import android.support.annotation.NonNull;
import android.util.Log;

import com.highersun.sign.api.sign.SignApi;
import com.highersun.sign.bean.BaseDataBean;
import com.highersun.sign.bean.SignDataBean;
import com.highersun.sign.bean.StringBaseDataBean;
import com.highersun.sign.otto.AppBus;
import com.highersun.sign.otto.SignEvent;
import com.highersun.sign.utils.ToastUtil;

import rx.Subscriber;

/**
 * Created by admin on 2016/8/16.
 */

public class SignPresenter implements SignContract.Presenter {
    SignContract.View mSingView;

    @Override
    public void getCurrent() {
        Subscriber<StringBaseDataBean> subscriber = new Subscriber<StringBaseDataBean>() {
            @Override
            public void onCompleted() {
                mSingView.hideLoading();
//                Log.e("getCurrent", "getCurrent");
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请检查您的网络2"+e.toString());
//                mSingView.showToastMessage("error" + e.toString() + "请检查网络");
//                Log.e("getCurrent", "error" + e.toString());
            }

            @Override
            public void onNext(StringBaseDataBean baseDataBean) {
                Log.e("getCurrent", baseDataBean.toString());

                if (baseDataBean.getCode() == 0) {
                    mSingView.setCurrentTime(baseDataBean.getData());
                    mSingView.showPop();
                } else {
                    ToastUtil.showToast("获取时间失败");
//                    mSingView.showToastMessage("获取时间失败");
                }
            }
        };
        mSingView.showLoading();
        SignApi.getInstance().getCurrent(subscriber);
    }

    @Override
    public void postSign(String userId, String signEquipment, final String type, String iBeaconId) {

        Subscriber subscriber = new Subscriber<StringBaseDataBean>() {
            @Override
            public void onCompleted() {
                mSingView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请检查您的网络3"+e.toString());
            }

            @Override
            public void onNext(StringBaseDataBean baseDataBean) {
                if (baseDataBean.getCode() == 0) {
                    Log.e("zhangkai type",type);
                    if ("0".equals(type)) {
                        mSingView.showToastMessage("签到成功");
                        AppBus.getInstance().post(new SignEvent(baseDataBean.getData(),"signIn"));
                        mSingView.hidePop(500);
                        Log.e("zhangkai0",baseDataBean.getData());
                    } else {
                        mSingView.showToastMessage("签退成功");
                        AppBus.getInstance().post(new SignEvent(baseDataBean.getData(),"signOut"));
                        mSingView.hidePop(500);
                        Log.e("zhangkai1",baseDataBean.getData());
                    }
                } else {
                    mSingView.showToastMessage(baseDataBean.getMsg());
                }
                Log.e("SignPostbaseDataBean", baseDataBean.toString());
            }
        };
        mSingView.showLoading();
        SignApi.getInstance().postSign(subscriber, userId, signEquipment, type, iBeaconId);
    }

    @Override
    public void getSignInfo(final String userId, String iBeaconId, final String signTime, final String model) {
        Subscriber<SignDataBean> subscriber = new Subscriber<SignDataBean>() {
            @Override
            public void onCompleted() {
                mSingView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请检查您的网络4"+e.toString());
            }

            @Override
            public void onNext(SignDataBean signDataBean) {

                if (signDataBean.getCode() == 0) {
                    if ("checkIn".equals(model)) {
                        if (signDataBean.getData().getSignIn() != null) {
                            mSingView.showUpdate(model,signDataBean.getData().getSignIn().getSignTime());
                        } else {
                            mSingView.signAfterCheck("0");
                        }
                    }
                    if ("checkOut".equals(model)) {
                        if (signDataBean.getData().getSignOut() != null) {
                            mSingView.showUpdate(model,signDataBean.getData().getSignOut().getSignTime());
                        } else {
                            mSingView.signAfterCheck("1");
                        }
                    }
                } else {
                    mSingView.showToastMessage(signDataBean.getMsg());
                }
            }
        };
        mSingView.showLoading();
        SignApi.getInstance().getSignInfo(subscriber, userId, iBeaconId, signTime);
    }

    @Override
    public void attachView(@NonNull SignContract.View view) {
        mSingView = view;
    }

    @Override
    public void detachView() {
        mSingView = null;
    }
}
