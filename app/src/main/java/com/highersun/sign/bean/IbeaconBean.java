package com.highersun.sign.bean;

/**
 * Created by admin on 2016/8/23.
 */

public class IbeaconBean {
    public String name;
    public int major;
    public int minor;
    public String proximityUuid;
    public String bluetoothAddress;
    public int txPower;
    public int rssi;
    private String distance;
    private long addTime;

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public String getProximityUuid() {
        return proximityUuid;
    }

    public void setProximityUuid(String proximityUuid) {
        this.proximityUuid = proximityUuid;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public int getTxPower() {
        return txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public String toString() {
        return "iBeacon{" +
                "name='" + name + '\'' +
                ", major=" + major +
                ", minor=" + minor +
                ", proximityUuid='" + proximityUuid + '\'' +
                ", bluetoothAddress='" + bluetoothAddress + '\'' +
                ", txPower=" + txPower +
                ", rssi=" + rssi +
                '}';
    }
}
