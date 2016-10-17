package com.highersun.sign.ui;

import android.support.annotation.NonNull;

/**
 * Created by admin on 2016/8/5.
 */

public interface BasePresenter<T extends BaseView> {

        void attachView(@NonNull T view);

        void detachView();
    }
