package com.wbx.merchant.bean;

import android.bluetooth.BluetoothDevice;

public class DevicesBean {
    public String content;
    public BluetoothDevice device;

    public DevicesBean(String content, BluetoothDevice device) {
        this.content = content;
        this.device = device;
    }
}
