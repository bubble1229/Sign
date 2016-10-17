package com.highersun.sign.api.sign;

import com.highersun.sign.bean.BaseDataBean;
import com.highersun.sign.bean.IbeaconListBean;
import com.highersun.sign.bean.MonthDataBean;
import com.highersun.sign.bean.SignDataBean;
import com.highersun.sign.bean.SignSuccessData;
import com.highersun.sign.bean.StringBaseDataBean;


import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by admin on 2016/8/12.
 */

public interface SignService {
    /**
     * 1、 获取当前时间
     */
    @GET("user/current")
    Observable<StringBaseDataBean> getCurrent();


    /**
     * 2、 提交考勤
     */
    @FormUrlEncoded
    @POST("attendance/record/add")
    Observable<SignSuccessData> postSign(
            @FieldMap Map<String, String> params);


    /**
     * 3、获取考勤信息
     */
    @GET("attendance/record/get")
    Observable<SignDataBean> getSignInfo(
            @Query("userId") String userId, @Query("iBeaconId") String iBeaconId, @Query("signTime") String signTime);

    /**
     * 4、获取mCount个月的考勤数据
     */
    @GET("attendance/record/getMonthRecord")
    Observable<MonthDataBean> getMonthRecord(
            @Query("userId") String userId, @Query("dateTime") String dateTime, @Query("mCount") String mCount);

    /**
     * 5、获取考勤的ibeacon信息
     */
    @GET("ibeacon/list")
    Observable<IbeaconListBean> getIbeaconInfo(
            @Query("pageNo") String PageNo, @Query("pageSize") String pageSize
    );
}
