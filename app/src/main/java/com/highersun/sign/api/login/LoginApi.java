package com.highersun.sign.api.login;


import com.highersun.sign.bean.LoginDataBean;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by admin on 2016/8/12.
 */

public class LoginApi {
    public static final String BASE_URL = "http://42.96.195.27:8090/sign/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private LoginService loginService;


    //构造方法私有
    private LoginApi() {
        //手动创建一个OkHttpClient并设置超时时间
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

     loginService = retrofit.create(LoginService.class);
    }
    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final LoginApi INSTANCE = new LoginApi();
    }

    //获取单例
    public static LoginApi getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void postSign(Subscriber<LoginDataBean> subscriber, String phone, String pass) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", pass);
        loginService.login(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
