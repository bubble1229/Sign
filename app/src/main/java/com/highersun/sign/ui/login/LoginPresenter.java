package com.highersun.sign.ui.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.highersun.sign.api.login.LoginApi;
import com.highersun.sign.bean.LoginDataBean;
import com.highersun.sign.ui.BaseView;
import com.highersun.sign.utils.ToastUtil;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2016/8/5.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private final String TAG = getClass().getSimpleName();

    private LoginContract.View mLoginView;
    private Subscription mSubscription;

    @Override
    public void login(String userName, String passWord) {
        if (TextUtils.isEmpty(userName)) {
            mLoginView.showUserNameError("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(passWord)) {
            mLoginView.showPassWordError("请输入密码");
            return;
        }
//        mLoginView.showLoading();
        Subscriber subscriber = new Subscriber<LoginDataBean>() {
            @Override
            public void onCompleted() {
                mLoginView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
//                Log.e(TAG, e.toString());
                ToastUtil.showToast("请检查您的网络5"+e.toString());
            }

            @Override
            public void onNext(LoginDataBean loginDataBean) {
                if (loginDataBean.getCode() == 0) {

                    mLoginView.saveLoginStatue(true);
                    mLoginView.startActivity(loginDataBean.getData().getUserId(), loginDataBean.getData().getName());//跳转到Sign页面
                    ToastUtil.showToast("登录成功");
                    return;
                }
                ToastUtil.showToast(loginDataBean.getMsg());  //显示错误信息
                // mLoginView.showPassWordError(loginDataBean.getMsg()); //显示错误信息
            }
        };
        mLoginView.showLoading();
        LoginApi.getInstance().postSign(subscriber, userName, passWord);
    }

    @Override
    public void attachView(@NonNull LoginContract.View view) {
        mLoginView = view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mLoginView = null;
    }
}
