package com.highersun.sign.api.login;

import com.highersun.sign.bean.LoginDataBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2016/8/12.
 */

public interface LoginService {
    @FormUrlEncoded @POST("user/login")
    Observable<LoginDataBean>login(
            @FieldMap Map<String, String> params);
}
