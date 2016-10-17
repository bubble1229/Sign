package com.highersun.sign.ui.sign;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.guo.duoduo.randomtextview.RandomTextView;
import com.highersun.sign.R;
import com.highersun.sign.adapter.ScanResultAdapter;
import com.highersun.sign.bean.IbeaconBean;
import com.highersun.sign.service.ScanService;
import com.highersun.sign.ui.BaseActivity;
import com.highersun.sign.utils.DensityUtil;
import com.highersun.sign.utils.IbeanconUtils;
import com.highersun.sign.view.SpaceItemDecoration;
import com.highersun.sign.view.VerticalSpaceItemMessageDecoration;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2016/8/15.
 */

public class SignActivity extends BaseActivity implements SignContract.View, ScanResultAdapter.OnItemClickListener {// ScanService.ScanListener,


    /**
     * 搜索BLE终端
     */
    private BluetoothAdapter mBluetoothAdapter;
    private MaterialDialog dialog;

    SignContract signContract;
    SignPresenter mPresenter;
    String userId;
    String signModel;
    List<IbeaconBean> ibeaconBeanList = new ArrayList<>();
    ScanService scanService;
    ScanResultAdapter scanResultAdapter;
    ServiceConnection conn;
    String iBeaconId = "iBeaconId";
    String userName;
    String greeting;
    int totalScan = 0;
    Time t;
    boolean isShow;
    Handler handler;
    Runnable runnable;

    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    int mScreenWidth;
    String currentTime;
    @Bind(R.id.random_textview)
    RandomTextView randomTextview;
    @Bind(R.id.btn_check_in)
    Button btnCheckIn;
    @Bind(R.id.btn_check_out)
    Button btnCheckOut;
    @Bind(R.id.layout1)
    LinearLayout layout1;
    @Bind(R.id.btn_cancel_sign)
    Button btnCancelSign;
    @Bind(R.id.btn_check_update)
    Button btnCheckUpdate;
    @Bind(R.id.layout2)
    LinearLayout layout2;
    @Bind(R.id.layoutall)
    LinearLayout layoutall;
    @Bind(R.id.rl_all)
    RelativeLayout rlAll;
    @Bind(R.id.tv_current_time)
    TextView tvCurrentTime;
    @Bind(R.id.tv_current_time2)
    TextView tvCurrentTime2;
    @Bind(R.id.rv_scan_result)
    RecyclerView rvScanResult;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_scan_total)
    TextView tvTotal;
    @Bind(R.id.tv_company_name)
    TextView tvCompany;
    @Bind(R.id.tv_company_name2)
    TextView tvCompany2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        Log.e("userId", userId);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        dialog = new MaterialDialog.Builder(this).content("数据加载中").progress(true, 0).build();
        mScreenWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layout2.getLayoutParams();
        linearParams.width = mScreenWidth - 2 * DensityUtil.dip2px(this, 20);
        layout2.setLayoutParams(linearParams);
        layoutall.post(new Runnable() {
            @Override
            public void run() {
           hidePop(0);
            }
        });

        rlAll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getY() < view.getHeight() - layoutall.getHeight()) {
                    hidePop(500);
                    if (!isShow) {
                        return false;
                    }
                    return true;
                }
                return false; //false;
            }
        });
//        rlAll.dispatchTouchEvent()


        //title 的显示
        t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        setGreeting();
        tvTitle.setText(greeting + "," + userName);

        //recycleView的处理
        rvScanResult.setLayoutManager(new GridLayoutManager(this, 1));
        rvScanResult.addItemDecoration(new SpaceItemDecoration(2));
        scanResultAdapter = new ScanResultAdapter(this, ibeaconBeanList);
        scanResultAdapter.setOnItemClickListener(this);
//        scanResultAdapter.setmHeaderView(LayoutInflater.from(this).inflate(R.layout.));
        rvScanResult.setAdapter(scanResultAdapter);


        //和service的连接
//        conn = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                scanService = ((ScanService.ScanBinder) service).getService();
//                scanService.setScanListener(SignActivity.this);
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//
//            }
//        };
//
//        hidePop();


        //蓝牙
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

                            scanResultAdapter.addDevice(ibeacon);
                            scanResultAdapter.notifyDataSetChanged();
                            randomTextview.addKeyWord(TextUtils.isEmpty(ibeacon.getName()) ? "未知设备" : ibeacon.getName());
                            tvTotal.setText(ibeaconBeanList.size() + "");
                            if (totalScan != ibeaconBeanList.size()) {
                                totalScan = ibeaconBeanList.size();
                                randomTextview.show();
                            }
                        }
                    }
                };
        mBluetoothAdapter.
                startLeScan(mLeScanCallback);


        mPresenter = new SignPresenter();
        mPresenter.attachView(this);

        final RandomTextView randomTextView = (RandomTextView) findViewById(
                R.id.random_textview);
        randomTextView.setOnRippleViewClickListener(
                new RandomTextView.OnRippleViewClickListener() {
                    @Override
                    public void onRippleViewClicked(View view, String keyword) {
                        tvCompany.setText(keyword);
                        tvCompany2.setText(keyword);
                        mPresenter.getCurrent();
                    }
                });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                randomTextView.addKeyWord("海洱森");
//                randomTextView.addKeyWord("电力学院");
//                randomTextView.show();
//            }
//        }, 2 * 1000);
        //定时清空
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //
                for (int i = 0; i <ibeaconBeanList.size() ; i++) {
                    if(System.currentTimeMillis()-ibeaconBeanList.get(i).getAddTime()>2000){
                        randomTextview.removeKeyWord(ibeaconBeanList.get(i).getName()!=null?ibeaconBeanList.get(i).getName():"未知设备");

                        ibeaconBeanList.remove(i);
                    }

                }
                scanResultAdapter.notifyDataSetChanged();
                handler.postDelayed(this, 5000);// 50ms后执行this，即runable
            }
        };
        handler.postDelayed(runnable, 5000);// 打开定时器，50ms后执行runnable操作


        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getSignInfo(userId, iBeaconId, currentTime, "checkIn");

            }
        });
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getSignInfo(userId, iBeaconId, currentTime, "checkOut");
            }
        });
        btnCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("checkIn".equals(signModel)) {
                    mPresenter.postSign(userId, "Android", "0", iBeaconId);
                }
                if ("checkOut".equals(signModel)) {
                    mPresenter.postSign(userId, "Android", "1", iBeaconId);
                }
            }
        });
        btnCancelSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideUpdate();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Intent intent = new Intent(this, ScanService.class);
//        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        setGreeting();
        tvTitle.setText(greeting + "," + userName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
//        unbindService(conn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);

    }

    @Override
    public void showPop() {
//     将Pop左移
//        layoutall.setVisibility(View.VISIBLE);
        horizonTranslater(layout1, 0, 0);
        horizonTranslater(layout2, 0, 0);

        float curTranslationY = layoutall.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(layoutall, "translationY", curTranslationY, 0);
        animator.setDuration(500);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isShow = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        rlAll.setBackgroundColor(Color.parseColor("#88969696"));

    }

    @Override
    public void hidePop(int duration) {
        float curTranslationY = layoutall.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(layoutall, "translationY", curTranslationY, layoutall.getHeight());
        animator.setDuration(duration);
        animator.start();
        rlAll.setBackgroundColor(Color.parseColor("#00969696"));
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isShow = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void showUpdate(String model, String formerTime) {
        signModel = model;
        tvCurrentTime2.setText(formerTime.substring(10, 16));
        horizonTranslater(layout1, -mScreenWidth, 300);
        horizonTranslater(layout2, -mScreenWidth, 300);

    }

    @Override
    public void hideUpdate() {
        horizonTranslater(layout1, 0, 300);
        horizonTranslater(layout2, 0, 300);
    }

    @Override
    public void showToastMessage(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCurrentTime(String currentTime) {
        if (!TextUtils.isEmpty(currentTime)) {
            this.currentTime = currentTime;
            tvCurrentTime.setText(currentTime.substring(10, 16));
        }
    }

    @Override
    public void signAfterCheck(String model) {
//        mPresenter.postSign(userId,model,"iBeaconId","iBeaconId");
        mPresenter.postSign(userId, "Android", model, iBeaconId);
    }

    /**
     * View 的水平移动
     *
     * @param view
     * @param dist
     */
    private void horizonTranslater(View view, int dist, int duration) {
        float curTranslationX = view.getTranslationX();
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", curTranslationX, dist);
        animator.setDuration(duration);
        animator.start();
    }
//
//    @Override
//    public void scanResult(IbeaconBean iBeacon) {
//        scanResultAdapter.addDevice(iBeacon);
//        scanResultAdapter.notifyDataSetChanged();
//        randomTextview.addKeyWord(iBeacon.getName());
//        tvTotal.setText(ibeaconBeanList.size() + "");
//        if (totalScan != ibeaconBeanList.size()) {
//            totalScan = ibeaconBeanList.size();
//            randomTextview.show();
//        }
//
//    }

    @Override
    public void onItemClick(int position, IbeaconBean data) {
        iBeaconId = data.getProximityUuid();
        Log.e("The data of iBeaconId", iBeaconId);
        tvCompany.setText(TextUtils.isEmpty(data.getName()) ? "未知设备" : data.getName());
        tvCompany2.setText(TextUtils.isEmpty(data.getName()) ? "未知设备" : data.getName());
        mPresenter.getCurrent();
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
    public void showLoading() {
        if (!isFinishing() && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (!isFinishing() && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
