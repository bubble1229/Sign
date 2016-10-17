package com.highersun.sign.ui.login;

import com.highersun.sign.ui.BasePresenter;
import com.highersun.sign.ui.BaseView;

/**
 * Created by admin on 2016/8/5.
 */

public interface LoginContract {
    interface View extends BaseView{

        void startActivity(String userId,String userName);

        void showUserNameError(String error);

        void showPassWordError(String error);
        void saveLoginStatue(boolean isLogin);
    }
    interface Presenter extends  BasePresenter<View> {
        void login(String userName, String passWord);
    }

}
