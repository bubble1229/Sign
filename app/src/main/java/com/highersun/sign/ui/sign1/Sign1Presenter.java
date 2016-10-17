package com.highersun.sign.ui.sign1;

import android.support.annotation.NonNull;
import android.util.Log;

import com.highersun.sign.api.sign.SignApi;
import com.highersun.sign.bean.IbeaconListBean;
import com.highersun.sign.bean.SignDataBean;
import com.highersun.sign.bean.SignSuccessData;
import com.highersun.sign.bean.StringBaseDataBean;
import com.highersun.sign.otto.AppBus;
import com.highersun.sign.otto.SignEvent;
import com.highersun.sign.utils.ToastUtil;

import rx.Subscriber;

/**
 * Author: ShenDanLai on 2016/9/13.
 * Email: 17721129316@163.com
 */

public class Sign1Presenter implements Sign1Contract.Presenter {
    private Sign1Contract.View mSignView;

    @Override
    public void getCurrent() {
        Subscriber<StringBaseDataBean> subscriber = new Subscriber<StringBaseDataBean>() {
            @Override
            public void onCompleted() {
//                Log.e("getCurrent", "getCurrent");
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请检查您的网络6"+e.toString());
//                mSingView.showToastMessage("error" + e.toString() + "请检查网络");
//                Log.e("getCurrent", "error" + e.toString());
            }

            @Override
            public void onNext(StringBaseDataBean baseDataBean) {
                Log.e("getCurrent", baseDataBean.toString());

                if (baseDataBean.getCode() == 0) {
                    mSignView.refreshTime(Integer.valueOf(baseDataBean.getData().substring(11,13)),Integer.valueOf(baseDataBean.getData().substring(11,13)));
                } else {
                    ToastUtil.showToast("获取时间失败");
//                    mSingView.showToastMessage("获取时间失败");
                }
            }
        };
        SignApi.getInstance().getCurrent(subscriber);
    }

    @Override
    public void postSign(String userId, String signEquipment, final String type, String iBeaconId) {

        Subscriber subscriber = new Subscriber<SignSuccessData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

                ToastUtil.showToast("请检查您的网络7"+e.toString());
            }

            @Override
            public void onNext(SignSuccessData baseDataBean) {
                if (baseDataBean.getCode() == 0) {
                    if ("0".equals(type)) {
                        AppBus.getInstance().post(new SignEvent(baseDataBean.getData().getSignIn()+"", "signIn"));
                    } else {
                        AppBus.getInstance().post(new SignEvent(baseDataBean.getData().getSignOut()+"", "signOut"));
                    }
                }
                Log.e("SignPostbaseDataBean", baseDataBean.toString());
            }
        };
        SignApi.getInstance().postSign(subscriber, userId, signEquipment, type, iBeaconId);
    }

    @Override
    public void getSignInfo(final String userId, String iBeaconId, final String signTime, final String model) {
        Subscriber<SignDataBean> subscriber = new Subscriber<SignDataBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请检查您的网络8"+e.toString());
            }

            @Override
            public void onNext(SignDataBean signDataBean) {

            }
        };
        SignApi.getInstance().getSignInfo(subscriber, userId, iBeaconId, signTime);
    }

    @Override
    public void getIbeaconInfo(String pageNo, String pageSize) {
        Subscriber<IbeaconListBean> subscriber = new Subscriber<IbeaconListBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请检查您的网络9"+e.toString());
            }

            @Override
            public void onNext(IbeaconListBean ibeaconListBean) {
                if(ibeaconListBean.getCode()==0){
                    mSignView.setIbeconId(ibeaconListBean.getData().getList().get(0).getId());
                }


            }
        };
        SignApi.getInstance().getIbeaconInfo(subscriber, pageNo, pageSize);
    }



    @Override
    public void attachView(@NonNull Sign1Contract.View view) {
        mSignView = view;
    }

    @Override
    public void detachView() {
        mSignView = null;
    }
}
