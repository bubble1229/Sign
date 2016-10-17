package com.highersun.sign.view.MyWheel;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/11/27 0027.
 */
public class MyDateTime implements Serializable {
    private int yearSun;
    private int monthSun;
    private int daySun;
    private int hSun;
    private int mSun;
    private int yearLunar;
    private int monthLunar;
    private int dayLunar;
    private int hLunar;
    private int mLunar;
    private boolean isRun;
    private boolean isSun;

    public String toStringSun(){
        return yearSun + "-" + ((monthSun)<10?("0"+(monthSun)):(monthSun+"")) + "-" + (daySun<10?("0"+daySun):(daySun+""))+" "+hSun +":"+mSun+":00";
    }
    public String toStringSun2(){
        return yearSun + "-" + ((monthSun)<10?("0"+(monthSun)):(monthSun+"")) + "-" + (daySun<10?("0"+daySun):(daySun+""));
    }
    public String toStringSun3(){
        return yearSun + "-" + ((monthSun)<10?("0"+(monthSun)):(monthSun+"")) + "-" + (daySun<10?("0"+daySun):(daySun+""))+" "+hSun +":"+mSun;
    }
    public String toStringLunar(){
        return "";
    }
    public MyDateTime(){
        isRun=true;
        Calendar calendar= Calendar.getInstance();
        yearSun=calendar.get(Calendar.YEAR);
        monthSun=calendar.get(Calendar.MONTH)+1;
        daySun=calendar.get(Calendar.DAY_OF_MONTH);
        hSun=calendar.get(Calendar.HOUR_OF_DAY);
        mSun=calendar.get(Calendar.MINUTE);
        Lunar lunar=new Lunar(calendar);
        yearLunar=lunar.getLunarYear();
        monthLunar=lunar.getLunarMonth();
        dayLunar=lunar.getLunarDay();
        hLunar=calendar.get(Calendar.HOUR_OF_DAY);
        mLunar=calendar.get(Calendar.MINUTE);
        isRun=lunar.getLeap();
    }
    public MyDateTime(Calendar calendar,boolean isSun){
        this.isSun=isSun;
        yearSun=calendar.get(Calendar.YEAR);
        monthSun=calendar.get(Calendar.MONTH)+1;
        daySun=calendar.get(Calendar.DAY_OF_MONTH);
        hSun=calendar.get(Calendar.HOUR_OF_DAY);
        mSun=calendar.get(Calendar.MINUTE);
        Lunar lunar=new Lunar(calendar);
        yearLunar=lunar.getLunarYear();
        monthLunar=lunar.getLunarMonth();
        dayLunar=lunar.getLunarDay();
        hLunar=calendar.get(Calendar.HOUR_OF_DAY);
        mLunar=calendar.get(Calendar.MINUTE);
        isRun=lunar.getLeap();
    }

    public Calendar getCalendar(){
        Calendar calendar= Calendar.getInstance();
        calendar.set(yearSun,monthSun-1,daySun,hSun,mSun);
        return calendar;
    }

    public Date getDate(){
        Date date=new Date(getCalendar().getTimeInMillis());
        return date;
    }

    public int getYearSun() {
        return yearSun;
    }

    public void setYearSun(int yearSun) {
        this.yearSun = yearSun;
    }

    public int getMonthSun() {
        return monthSun;
    }


    public boolean isSun() {
        return isSun;
    }

    public void setIsSun(boolean isSun) {
        this.isSun = isSun;
    }

    public void setMonthSun(int monthSun) {
        this.monthSun = monthSun;
    }

    public int getDaySun() {
        return daySun;
    }

    public void setDaySun(int daySun) {
        this.daySun = daySun;
    }

    public int gethSun() {
        return hSun;
    }

    public void sethSun(int hSun) {
        this.hSun = hSun;
    }

    public int getmSun() {
        return mSun;
    }

    public void setmSun(int mSun) {
        this.mSun = mSun;
    }

    public int getYearLunar() {
        return yearLunar;
    }

    public void setYearLunar(int yearLunar) {
        this.yearLunar = yearLunar;
    }

    public int getMonthLunar() {
        return monthLunar;
    }

    public void setMonthLunar(int monthLunar) {
        this.monthLunar = monthLunar;
    }

    public int getDayLunar() {
        return dayLunar;
    }

    public void setDayLunar(int dayLunar) {
        this.dayLunar = dayLunar;
    }

    public int gethLunar() {
        return hLunar;
    }

    public void sethLunar(int hLunar) {
        this.hLunar = hLunar;
    }

    public int getmLunar() {
        return mLunar;
    }

    public void setmLunar(int mLunar) {
        this.mLunar = mLunar;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setIsRun(boolean isRun) {
        this.isRun = isRun;
    }

}
