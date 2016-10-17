package com.highersun.sign.ui.status;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.highersun.sign.R;
import com.highersun.sign.bean.MonthDataBean;
import com.highersun.sign.otto.AppBus;
import com.highersun.sign.otto.SignEvent;
import com.highersun.sign.ui.MainActivity;
import com.highersun.sign.view.MyWheel.MyDateTime;
import com.highersun.sign.view.MyWheel.TimePopupWindow;
import com.highersun.sign.view.calendarview.CalendarView;
import com.highersun.sign.view.calendarview.ICalendarView;
import com.highersun.sign.view.timepicker.MyTimePickerView;
import com.highersun.sign.view.timepicker.TimePickerView;
import com.squareup.otto.Subscribe;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.bigkoo.pickerview.R.id.month;

/**
 * Author: ShenDanLai on 2016/9/8.
 * Email: 17721129316@163.com
 */

public class StatusActivity extends Activity implements View.OnClickListener, StatusContract.View {

    //    private MyTimePickerView timePickerView;
    TimePopupWindow timePopupWindow;
    StatusPresenter statusPresenter;
    String userId;
    MonthDataBean monthDataBean;
    boolean isOpened;
    private int[] signStatus = new int[32];
    private static String[] MONTH_NAME = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    @Bind(R.id.prev)
    Button prev;
    @Bind(R.id.year_month_textview)
    TextView yearMonthTextView;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.calendarView)
    CalendarView calendarView;
    @Bind(R.id.tv_checkInTime)
    TextView tvCheckInTime;
    @Bind(R.id.tv_checkOutTime)
    TextView tvCheckOutTime;
    @Bind(R.id.tv_detail_date)
    TextView tvDetailDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getIntent().getStringExtra("userId");
        setContentView(R.layout.activity_status);
        ButterKnife.bind(this);

        statusPresenter = new StatusPresenter();
        statusPresenter.attachView(this);
//        statusPresenter.getMonthStatus(userId,"2016-05-10","2");
        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        //时间选择
        timePopupWindow = new TimePopupWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY, TimePopupWindow.Type2.SUN);
        timePopupWindow.setTime(new Date());
        timePopupWindow.setIsSun(true);
        timePopupWindow.setOutsideTouchable(false);
        timePopupWindow.setOnDismissed(new TimePopupWindow.OnDismissed() {
            @Override
            public void onDismissed() {
//                iv_top.setImageResource(R.drawable.buttom);
            }
        });
        timePopupWindow.setOnStatuChanged(new TimePopupWindow.OnStatuChanged() {
            @Override
            public void open(boolean isOpen) {
//                T.showShort(CalendarActivity.this, isOpen + "");
                isOpened = isOpen;
            }
        });
        //时间选择后回调
        timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(MyDateTime date) {

                int year = date.getYearSun();
                int month = date.getMonthLunar() + 1;

                Log.e("Test the timePick", "year:" + year + "month:" + month);


                calendarView.refresh0(year, month);
//                calendarView.status = signStatus;
                calendarView.invalidate();
//                myDate = date.getDate();
//                final DateTime myda = new DateTime(date.getCalendar());
//                time_top.setText(myda.getYear() + "年" + (myda.getMonth() + 1) + "月");
//                month.goDay(myda);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        goToday(myda);
//
//                    }
//                }, 358);
            }
        });


//        timePickerView = new MyTimePickerView(StatusActivity.this);
//        timePickerView.setTitle("年/月");
//        timePickerView.setCyclic(true);
//        timePickerView.setCancelable(false);
//        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date) {
//                Calendar c = Calendar.getInstance();
//                c.setTime(date);
//                int year = c.get(Calendar.YEAR)
//                int month = c.get(Calendar.MONTH) + 1;
//                calendarView.refresh0(year, month);
//                calendarView.invalidate();
//            }
//        });

        //月份显示控件
        yearMonthTextView.setText(getYearMonthText(calendarView.getYear(), calendarView.getMonth()));
        yearMonthTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                timePickerView.setTime(calendarView.getCalendar().getTime());
//                timePickerView.show();
                if (timePopupWindow == null) {
                    return;
                }

                if (!isOpened) {
                    timePopupWindow.showAtLocation(yearMonthTextView, Gravity.BOTTOM, 0, 0, calendarView.getCalendar().getTime());
                    timePopupWindow.setCyclic(true);
//                    iv_top.setImageResource(R.drawable.arrow_up);
                } else {
                    timePopupWindow.dismiss();
                }
            }
        });


        //日历显示控件

        statusPresenter.getMonthStatus(userId, calendarView.getYear() + "-" + calendarView.getMonth() + "-01", "1");


        calendarView.setWeekTextStyle(1);
        calendarView.setOnRefreshListener(new ICalendarView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                yearMonthTextView.setText(getYearMonthText(calendarView.getYear(), calendarView.getMonth()));
            }
        });

        calendarView.setOnItemClickListener(new ICalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(int day) {
                int year = calendarView.getYear();
                int month = calendarView.getMonth();
                tvDetailDate.setText(year + "-" + month + "-" + day);
                if (monthDataBean != null && monthDataBean.getData().get(0).size() > day) {
                    String signInTime = monthDataBean.getData().get(0).get(day - 1).getSignInTime();
                    String signOutTime = monthDataBean.getData().get(0).get(day - 1).getSignOutTime();
                    if (!TextUtils.isEmpty(signInTime)) {
                        tvCheckInTime.setText(signInTime);
                        if (isLate(signInTime)) {
                            tvCheckInTime.setTextColor(Color.parseColor("#FF0000"));
                        } else {
                            tvCheckInTime.setTextColor(Color.parseColor("#000000"));
                        }
                    } else {
                        tvCheckInTime.setText("未签到");
                        tvCheckInTime.setTextColor(Color.parseColor("#FF0000"));
                    }
                    if (!TextUtils.isEmpty(signOutTime)) {
                        tvCheckOutTime.setText(signOutTime);
                        if (isLeaveEarly(signOutTime)) {
                            tvCheckOutTime.setTextColor(Color.parseColor("#FF0000"));
                        } else {
                            tvCheckOutTime.setTextColor(Color.parseColor("#000000"));
                        }
                    } else {
                        tvCheckOutTime.setText("未签退");
                        tvCheckOutTime.setTextColor(Color.parseColor("#FF0000"));
                    }
                }


//                Toast.makeText(StatusActivity.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void turnLeftRight(int direction) {
                if (direction == 0) {
                    onPrevious();
                } else if (direction == 1) {
                    onNext();
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册到bus事件总线中
        AppBus.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        AppBus.getInstance().unregister(this);
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        statusPresenter.getMonthStatus(userId, calendarView.getYear() + "-" + calendarView.getMonth() + "-01", "1");
//    }

    private String getYearMonthText(int year, int month) {
        return new StringBuilder().append(MONTH_NAME[month - 1]).append(", ").append(year).toString();
    }

    public void onPrevious() {
        Calendar c = calendarView.getCalendar();
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
        calendarView.refresh0(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
        statusPresenter.getMonthStatus(userId, calendarView.getYear() + "-" + calendarView.getMonth() + "-01", "1");
    }

    public void onNext() {
        Calendar c = calendarView.getCalendar();
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
        calendarView.refresh0(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
        statusPresenter.getMonthStatus(userId, calendarView.getYear() + "-" + calendarView.getMonth() + "-01", "1");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prev:
                onPrevious();
                break;
            case R.id.next:
                onNext();
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void addMonthStatus(MonthDataBean monthDataBean) {
//        calendarView.statusList = monthDataBean.getData().get(0);
        for (int i = 0; i < monthDataBean.getData().get(0).size(); i++) {
            calendarView.status[i] = monthDataBean.getData().get(0).get(i).getStatus();
        }
        this.monthDataBean = monthDataBean;
        calendarView.invalidate();
    }

    public boolean isLate(String signInTime) {
        String[] time = signInTime.split(":");
        int hour = Integer.valueOf(time[0]);
        int minute = Integer.valueOf(time[1]);
        if (hour >= 10 || (hour == 9 && minute > 30)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLeaveEarly(String signOutTime) {
        String[] time = signOutTime.split(":");
        int hour = Integer.valueOf(time[0]);
        int minute = Integer.valueOf(time[1]);
        if (hour <= 17 || (hour == 18 && minute < 30)) {
            return true;
        } else {
            return false;
        }
    }

    @Subscribe
    public void onSignEvent(SignEvent event) {
        statusPresenter.getMonthStatus(userId, calendarView.getYear() + "-" + calendarView.getMonth() + "-01", "1");
        if ("signOut".equals(event.getSingMode())) {
            tvCheckOutTime.setText(event.getSignTime().substring(11, 16));
        }
    }
}
