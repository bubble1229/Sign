package com.highersun.sign.api.sign;

import android.util.Log;

import com.highersun.sign.bean.BaseDataBean;
import com.highersun.sign.bean.IbeaconListBean;
import com.highersun.sign.bean.MonthDataBean;
import com.highersun.sign.bean.SignDataBean;
import com.highersun.sign.bean.SignSuccessData;
import com.highersun.sign.bean.StringBaseDataBean;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2016/8/12.
 */

public class SignApi {
    public static final String BASE_URL = "http://42.96.195.27:8090/sign/";
    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private SignService signService;

    private SignApi() {
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
        signService = retrofit.create(SignService.class);

    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final SignApi INSTANCE = new SignApi();
    }

    //获取单例
    public static SignApi getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public void getCurrent(Subscriber<StringBaseDataBean> subscriber) {
        signService.getCurrent()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void getSignInfo(Subscriber<SignDataBean> subscriber, String userId, String iBeaconId, String signTime) {
        signService.getSignInfo(userId, iBeaconId, signTime)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    public void postSign(Subscriber<SignSuccessData> subscriber, String userId, String signEquipment, String type, String iBeaconId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("signEquipment", signEquipment);
        params.put("type", type);
        params.put("iBeaconId", iBeaconId);
        Log.e("RequesetParams", params.toString());
        signService.postSign(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMonthRecord(Subscriber<MonthDataBean> subscriber, String userId, String dateTime, String mCount) {
        signService.getMonthRecord(userId, dateTime, mCount)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getIbeaconInfo(Subscriber<IbeaconListBean> subscriber, String pageNo, String pageSize) {
        signService.getIbeaconInfo(pageNo, pageSize)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
