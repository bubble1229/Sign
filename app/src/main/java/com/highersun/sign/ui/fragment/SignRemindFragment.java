package com.highersun.sign.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.highersun.sign.R;
import com.highersun.sign.otto.AppBus;
import com.highersun.sign.otto.ServerTime;
import com.highersun.sign.view.signcricle.SignCricleView;
import com.squareup.otto.Subscribe;


import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author: ShenDanLai on 2016/9/14.
 * Email: 17721129316@163.com
 */

public class SignRemindFragment extends Fragment {
    SignCricleView signView;
    @Bind(R.id.tv_count_down_hour)
    TextView tvHour;
    @Bind(R.id.tv_count_down_minute)
    TextView tvMinute;
    @Bind(R.id.tv_count_down_explain)
    TextView tvExplain;
    boolean isCutDown;
    boolean isAnimation;

    Time t;
    //倒计时
    static int hour = -1;
    static int minute = 10;
    static int second = 10;
    final static String tag = "tag";
    Timer timer;
    TimerTask timerTask;
    Handler handler1;

    public static SignRemindFragment newInstance() {
        return new SignRemindFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remind, null);
        ButterKnife.bind(this, view);
        signView = (SignCricleView) view.findViewById(R.id.sign_view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        signView.startAnim();
        AppBus.getInstance().register(this);

        handler1 = new Handler() {
            public void handleMessage(Message msg) {
                System.out.println("handle!");
                if (msg.what == 0) {
                    if (minute == 0) {
                        if (second == 0) {
                            isCutDown = false;
//                            tvHour.setText("Time out !");
//                            if (timer != null) {
//                                timer.cancel();
//                                timer = null;
//                            }
//                            if (timerTask != null) {
//                                timerTask = null;
//                            }
                        } else {
                            second--;
                            if (isAnimation) {
                                signView.startAnim(second);
                            }
                            if (second >= 10) {
                                tvHour.setText("0" + minute);
                                tvMinute.setText(second + "");
                            } else {
                                tvHour.setText("0" + minute);
                                tvMinute.setText(":0" + second);
                            }
                        }
                    } else {
                        if (second == 0) {
                            second = 59;
                            minute--;
                            if (isAnimation) {
                                signView.startAnim(second);
                            }
                            if (minute >= 10) {
                                tvHour.setText(minute + "");
                                tvMinute.setText(second + "");
                            } else {
                                tvHour.setText("0" + minute);
                                tvMinute.setText(second + "");
                            }
                        } else {
                            second--;
                            if (isAnimation) {
                                signView.startAnim(second);
                            }
                            if (second >= 10) {
                                if (minute >= 10) {
                                    tvHour.setText(minute + "");
                                    tvMinute.setText(second + "");
                                } else {
                                    tvHour.setText("0" + minute);
                                    tvMinute.setText(second + "");
                                }
                            } else {
                                if (minute >= 10) {
                                    tvHour.setText(minute + "");
                                    tvMinute.setText("0" + second);
                                } else {
                                    tvHour.setText("0" + minute);
                                    tvMinute.setText("0" + second);
                                }
                            }
                        }
                    }
                }
                if (msg.what == 1) {
                    if (second == 59) {
                        second = 0;
                        minute++;
                        if (isAnimation) {
                            signView.startAnim(second);
                        }
                    } else {
                        second++;
                        if (isAnimation) {
                            signView.startAnim(second);
                        }
                    }

                    if (second >= 10) {
                        if (minute >= 10) {
                            tvHour.setText(minute + "");
                            tvMinute.setText(second + "");
                        } else {
                            tvHour.setText("0" + minute);
                            tvMinute.setText(second + "");
                        }
                    } else {
                        if (minute >= 10) {
                            tvHour.setText(minute + "");
                            tvMinute.setText("0" + second);
                        } else {
                            tvHour.setText("0" + minute);
                            tvMinute.setText("0" + second);
                        }
                    }
                }
                super.handleMessage(msg);
            }

        };

        //倒计时每分钟
//        minute = t.hour;
//        second = t.minute;
        t = new Time();
        t.setToNow();
        setTime(t.hour, t.minute);
        timerTask = new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                if (isCutDown) {
                    msg.what = 0;
                } else {
                    msg.what = 1;
                }
                handler1.sendMessage(msg);
                Log.e("当前线程1：", Thread.currentThread().getName());//这里打印de结果bu会是main
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 60 * 1000);
    }


    @Override
    public void onPause() {
        super.onPause();
        AppBus.getInstance().unregister(this);
        handler1 = null;
        //销毁倒计时线程
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask = null;
        }
        minute = -1;
        second = -1;
    }

    public void setTime(int rawhour, int rawminute) {
        if ((rawhour >= 0 && rawhour < 8) || (rawhour == 8 && rawminute < 30)) {
            //时间小于8点半

            //距离签到时间
            isCutDown = true;
            isAnimation = false; //不要显示动画
            second = 30 - rawminute;
            if (second < 0) {
                second += 60;
                minute = 9 - rawhour - 1;
            } else {
                minute = 9 - rawhour;
            }
            tvExplain.setText("距离签到时间");


        } else if ((rawhour == 8 && rawminute >= 30) || (rawhour == 9 && rawminute <= 30)) {
            isCutDown = true;
            isAnimation = true; //显示动画
            //8点半到9点半之间
            //距离签到时间
            second = 30 - rawminute;
            if (second < 0) {
                second += 60;
                minute = 9 - rawhour - 1;
            } else {
                minute = 9 - rawhour;
            }
        } else if ((rawhour == 9 && rawminute > 30) || (rawhour > 9 && rawhour < 12) || (rawhour == 12 && rawminute <= 30)) {
            //9点半到12点半之间

            //签到迟到
            second = rawminute - 30;
            if (second < 0) {
                second += 60;
                minute = rawhour - 9 - 1;
            } else {
                minute = rawhour - 9;
            }
            tvExplain.setText("签到迟到");
            isCutDown = false;
            isAnimation = false; //不要显示动画

        } else if ((rawhour == 12 && rawminute > 30) || (rawhour >= 13 && rawhour < 17)) {
            //12点半到17点之间
            //签退早退
            isCutDown = true;
            isAnimation = false; //不要显示动画
            second = 0 - rawminute;
            if (second < 0) {
                second += 60;
                minute = 18 - rawhour - 1;
            } else {
                minute = 18 - rawhour;
            }
            tvExplain.setText("距离签退时间");

        } else if (rawhour == 17) {
            isCutDown = true;
            isAnimation = true;
            second = 0 - rawminute;
            if (second < 0) {
                second += 60;
                minute = 18 - rawhour - 1;
            } else {
                minute = 18 - rawhour;
            }
            tvExplain.setText("距离签退时间");
            //17点到18点之间
        } else if (rawhour >= 18) {
            //18点以后
            //过了签退时间，
            // 正计时
            isCutDown = false;
            isAnimation = false;
            second = rawminute;
            minute = rawhour - 18;
            tvExplain.setText("超过签退时间");
        }
    }

    @Subscribe
    public void onServerTime(ServerTime serverTime) {
        Log.e("Test the time", serverTime.getHour() + "---" + serverTime.getMinute());
//        setTime(serverTime.getHour(), serverTime.getMinute());
    }
}
