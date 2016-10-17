package com.highersun.sign.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.highersun.sign.bean.IbeaconBean;
import com.highersun.sign.utils.IbeanconUtils;

/**
 * Created by admin on 2016/8/23.
 */

public class ScanService extends Service {
    private final String TAG = ScanService.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    ScanListener scanListener;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "BLE is not support", Toast.LENGTH_SHORT).show();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE is not support", Toast.LENGTH_SHORT).show();
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "BlueTooth is not support", Toast.LENGTH_SHORT).show();
            return;
        }
        //开启蓝牙
        mBluetoothAdapter.enable();


        mLeScanCallback =
                new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

                        final IbeaconBean ibeacon = IbeanconUtils.fromScanData(device, rssi, scanRecord);
                        if (ibeacon != null) {
                            scanListener.scanResult(ibeacon);
                            Log.e("The ibeacon service ", ibeacon.toString());
                        }
                    }
                };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
    }

    public void setScanListener(ScanListener scanListener) {
        this.scanListener = scanListener;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ScanBinder();
    }

    public class ScanBinder extends Binder {

        public ScanService getService() {
            return ScanService.this;
        }

    }

    public interface ScanListener {
        void scanResult(IbeaconBean iBeacon);
    }



}
