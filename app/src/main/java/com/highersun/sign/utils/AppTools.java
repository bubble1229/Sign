package com.highersun.sign.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.widget.ImageView;


import com.highersun.sign.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * @author WC
 * @date 创建时间：2016年1月21日 下午2:40:27
 * @description 工具管理类
 */
public class AppTools {
    public static final String TAG = "AppTools";

    private AppTools() {
        throw new AssertionError();
    }


    /**
     * 获取登录状态
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                Constant.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean("is_login", false);
    }

    /**
     * 保存是否登录状态
     *
     * @param context
     */
    public static void setIsLogin(Context context, boolean isLogin) {
        SharedPreferences sp = context.getSharedPreferences(
                Constant.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("is_login", isLogin);
        editor.commit();
    }

    /**
     *
     * 保存登录用户的userId
     * @param context
     * @param userId
     */
    public static void setUserId(Context context, String userId) {
        SharedPreferences sp = context.getSharedPreferences(
                Constant.USER_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  =sp.edit();
        editor.putString("user_id",userId);
        editor.commit();
    }
    public static String getUserId(Context context){
        SharedPreferences sp = context.getSharedPreferences(
                Constant.USER_ID,Context.MODE_PRIVATE);
        return sp.getString("user_id","");
    }
    /**
     *
     * 保存登录用户的userName
     * @param context
     * @param userId
     */
    public static void setUserName(Context context, String userId) {
        SharedPreferences sp = context.getSharedPreferences(
                Constant.USER_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  =sp.edit();
        editor.putString("user_name",userId);
        editor.commit();
    }
    public static String getUserName(Context context){
        SharedPreferences sp = context.getSharedPreferences(
                Constant.USER_ID,Context.MODE_PRIVATE);
        return sp.getString("user_name","");
    }
    /**
     *　保存当天签到时间
     * @param context
     * @param signTime
     */
    public static void setSignInTime(Context context,String signTime){
        SharedPreferences sp = context.getSharedPreferences(
                Constant.USER_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  =sp.edit();
        editor.putString("sign_in_time",signTime);
        editor.commit();
    }
    public static String getSignInTime(Context context){
        SharedPreferences sp = context.getSharedPreferences(
                Constant.USER_ID,Context.MODE_PRIVATE);
        return sp.getString("sign_in_time","");
    }

    /**
     *　保存当天签到时间
     * @param context
     * @param signTime
     */
    public static void setSignOutTime(Context context,String signTime){
        SharedPreferences sp = context.getSharedPreferences(
                Constant.USER_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  =sp.edit();
        editor.putString("sign_out_time",signTime);
        editor.commit();
    }
    public static String getSignOutTime(Context context){
        SharedPreferences sp = context.getSharedPreferences(
                Constant.USER_ID,Context.MODE_PRIVATE);
        return sp.getString("sign_out_time","");
    }


}
