package com.highersun.sign.ui.sign1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.highersun.sign.R;
import com.highersun.sign.bean.IbeaconBean;
import com.highersun.sign.otto.AppBus;
import com.highersun.sign.otto.ServerTime;
import com.highersun.sign.ui.dialog.BlurDialog;
import com.highersun.sign.ui.fragment.SignRemindFragment;
import com.highersun.sign.ui.fragment.SignTimeFragment;
import com.highersun.sign.utils.IbeanconUtils;
import com.highersun.sign.utils.ToastUtil;


import butterknife.Bind;
import butterknife.ButterKnife;

import static com.highersun.sign.R.id.rl_daka;

/**
 * Author: ShenDanLai on 2016/9/13.
 * Email: 17721129316@163.com
 */

public class Sign1Activity extends AppCompatActivity implements Sign1Contract.View {
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.dot_layout)
    LinearLayout mDotLayout;
    @Bind(R.id.content)
    RelativeLayout content;
    @Bind(R.id.iv_shenpi)
    ImageView ivShenpi;
    @Bind(rl_daka)
    RelativeLayout rlDaka;
    @Bind(R.id.iv_dingwei)
    ImageView ivDingwei;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_sign_enable)
    ImageView ivSignEnable;
    @Bind(R.id.tv_sing_enable)
    TextView tvSignEnable;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothAdapter.LeScanCallback mLeScanCallback;
    String userId;
    String userName;
    String greeting;
    Time t;
    String ibeaconId = "fda50693-a4e2-4fb1-afcf-c6eb07647825";
    //
    Sign1Presenter mPresenter;


    boolean isInLocation;
    Handler handler;
    Runnable runnable;

    private Fragment[] fragments = new Fragment[]{SignRemindFragment.newInstance(), SignTimeFragment.newInstance()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign1);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");


        rlDaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInLocation) {
                    showDialog();
                } else {
                    ToastUtil.showToast("不在考勤范围内");
                }

            }
        });
        mPresenter = new Sign1Presenter();
        mPresenter.attachView(this);
        mPresenter.getIbeaconInfo("1", "1");


        //
        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateIntroAndDot();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //title 的显示
        t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        setGreeting();
        tvTitle.setText(greeting + "," + userName);
//蓝牙扫描
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//            mBluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter = bluetoothManager.getAdapter();
//

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mBluetoothAdapter.enable();
        // Device scan callback.
        mLeScanCallback =
                new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

                        Log.e("Test ", "Test the scan");
                        final IbeaconBean ibeacon = IbeanconUtils.fromScanData(device, rssi, scanRecord);
                        if (ibeacon != null) {
                            if (ibeaconId.equals(ibeacon.getProximityUuid())) {
                                //在范围内可以打卡
                                isInLocation = true;
                                ivSignEnable.setImageResource(R.drawable.icon_sign_enable);
                                tvSignEnable.setText("在考勤范围内");
                            }
                        }
                    }
                };
        mBluetoothAdapter.
                startLeScan(mLeScanCallback);
        //定时清空
        //定时清空
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //清空操作
                isInLocation = false;
//                ivSignEnable.setImageDrawable(getResources().getDrawable(R.drawable.icon_sign_disable));
                ivSignEnable.setImageResource(R.drawable.icon_sign_disable);
                tvSignEnable.setText("不在考勤范围内");
                handler.postDelayed(this, 20000);// 50ms后执行this，即runable
            }
        };
        handler.postDelayed(runnable, 20000);// 打开定时器，50ms后执行runnable操作

        initDots();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getCurrent();
        setGreeting();
        tvTitle.setText(greeting + "," + userName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
    }
    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }


    private static class PagerAdapter extends FragmentStatePagerAdapter {

        private Fragment[] fragments;

        public PagerAdapter(FragmentManager fm, Fragment[] fragments) {

            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {

            return fragments[position];
        }

        @Override
        public int getCount() {

            return fragments.length;
        }
    }

    /**
     * 初始化dot
     */
    private void initDots() {
        for (int i = 0; i < fragments.length; i++) {
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            if (i != 0) {//第一个点不需要左边距
                params.leftMargin = 100;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.selector_dot);
            mDotLayout.addView(view);
        }
    }

    /**
     * 更新dot
     */
    private void updateIntroAndDot() {
        int currentPage = mViewPager.getCurrentItem();
        for (int i = 0; i < mDotLayout.getChildCount(); i++) {
            mDotLayout.getChildAt(i).setEnabled(i == currentPage);//设置setEnabled为true的话 在选择器里面就会对应的使用白色颜色
        }
    }

    private void showDialog() {
        BlurDialog dialog = new BlurDialog(this, mPresenter, userId, "Android", ibeaconId) {
            @Override
            protected void onCreateDialog() {
            }
        };
        dialog.show();
    }

    public void setGreeting() {
        t.setToNow(); // 取得系统时间。
        if (0 <= t.hour && t.hour < 6) {
            greeting = "凌晨好";
        }
        if (6 <= t.hour && t.hour < 12) {
            greeting = "上午好";
        }
        if (12 <= t.hour && t.hour < 19) {
            greeting = "下午好";
        }
        if (19 <= t.hour && t.hour < 24) {
            greeting = "晚上好";
        }
    }

    @Override
    public void refreshTime(int hour, int minute) {

        AppBus.getInstance().post(new ServerTime(hour,minute));

    }

    @Override
    public void setIbeconId(String ibeconId) {

//        this.ibeaconId = ibeconId;  //为防止周围的IbeaconId干扰，这个地方先注释掉

    }

}