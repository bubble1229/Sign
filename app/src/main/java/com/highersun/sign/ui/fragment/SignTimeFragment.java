package com.highersun.sign.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.highersun.sign.R;
import com.highersun.sign.otto.AppBus;
import com.highersun.sign.otto.SignEvent;
import com.highersun.sign.utils.AppTools;
import com.highersun.sign.utils.ToastUtil;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author: ShenDanLai on 2016/9/14.
 * Email: 17721129316@163.com
 */

public class SignTimeFragment extends Fragment {

    @Bind(R.id.tv_checkInTime)
    TextView tvCheckInTime;
    @Bind(R.id.tv_checkOutTime)
    TextView tvCheckOutTime;

    public static SignTimeFragment newInstance() {
        return new SignTimeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, null);
        ButterKnife.bind(this, view);
//        AppBus.getInstance().register(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppBus.getInstance().register(this);
        Time t = new Time();
        t.setToNow();
        Log.e("test the time",t.monthDay+"-"+AppTools.getSignInTime(getActivity()));
        if (!TextUtils.isEmpty(AppTools.getSignInTime(getActivity()))) {
            Log.e("test the time",t.monthDay+"-"+AppTools.getSignInTime(getActivity()).substring(8,10));
            if (String.valueOf(t.monthDay).equals(AppTools.getSignInTime(getActivity()).substring(8, 10))) {
                tvCheckInTime.setText(AppTools.getSignInTime(getActivity()).substring(11, 16));
            }
        }
        if (!TextUtils.isEmpty(AppTools.getSignOutTime(getActivity()))) {
            if (String.valueOf(t.monthDay).equals(AppTools.getSignOutTime(getActivity()).substring(8, 10))) {
                tvCheckOutTime.setText(AppTools.getSignOutTime(getActivity()).substring(11, 16));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        AppBus.getInstance().unregister(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    @Subscribe
    public void onSignEvent(SignEvent event) {
        if ("signOut".equals(event.getSingMode())) {
            tvCheckOutTime.setText(event.getSignTime().substring(11, 16));
            AppTools.setSignOutTime(getActivity(), event.getSignTime());
            ToastUtil.showToast("签退成功");
        }
        if ("signIn".equals(event.getSingMode())) {
            tvCheckInTime.setText(event.getSignTime().substring(11, 16));
            AppTools.setSignInTime(getActivity(), event.getSignTime());
            ToastUtil.showToast("签到成功");
        }
    }
}
