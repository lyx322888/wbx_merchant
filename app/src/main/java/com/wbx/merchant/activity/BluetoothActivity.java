package com.wbx.merchant.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.DevicesArrayAdapter;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.DevicesBean;
import com.wbx.merchant.utils.PrintUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;

//接入蓝牙
public class BluetoothActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_saomiao)
    Button btnSaomiao;
    @Bind(R.id.rv_bluetooth)
    RecyclerView rvBluetooth;
    @Bind(R.id.btn_dayin)
    Button btnDayin;
    private Set<BluetoothDevice> pairedDevices;
    private DevicesArrayAdapter devicesArrayAdapter;
    private static final int REQUEST_ENABLE_BT = 1234;
    private BluetoothAdapter mBluetoothAdapter;//蓝牙适配器
    //广播接收器，当远程蓝牙设备被发现时，回调函数onReceiver()会被执行
    private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (action) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    Log.d("dfdf", "开始扫描...");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.d("dfdf", "结束扫描...");
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    Log.d("dfdf", "发现设备...");
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    Log.d("dfdf", "设备绑定状态改变...");
                    break;
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_bluetooth;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initAdapter();
        initIntentFilter();
        initBluetooth();
    }

    private void initBluetooth() {
        //蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //判断设备是否支持蓝牙，如果mBluetoothAdapter为空则不支持，否则支持
        if (mBluetoothAdapter == null) {
            showShortToast("这台设备不支持蓝牙");
        } else {
            // If BT is not on, request that it be enabled.
            // setupChat() will then be called during onActivityResult
            //判断蓝牙是否开启，如果蓝牙没有打开则打开蓝牙
            if (!mBluetoothAdapter.isEnabled()) {
                //请求用户开启
                Intent enableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            } else {
                getDeviceList();
            }
        }
    }

    private void initIntentFilter() {
        //广播
        //搜索开始的过滤器
        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//搜索结束的过滤器
        IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//寻找到设备的过滤器
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//绑定状态改变
        IntentFilter filter4 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//配对请求
        IntentFilter filter5 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(mFindBlueToothReceiver, filter1);
        registerReceiver(mFindBlueToothReceiver, filter2);
        registerReceiver(mFindBlueToothReceiver, filter3);
        registerReceiver(mFindBlueToothReceiver, filter4);
        registerReceiver(mFindBlueToothReceiver, filter5);
    }

    private void initAdapter() {
        devicesArrayAdapter = new DevicesArrayAdapter(R.layout.bluetooth_device_name_item, new ArrayList<DevicesBean>());
        rvBluetooth.setLayoutManager(new LinearLayoutManager(mContext));
        rvBluetooth.setAdapter(devicesArrayAdapter);
        devicesArrayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("dfdf", "onItemClick: 开始连接");
                connect(devicesArrayAdapter.getData().get(position).device, new ConnectBlueCallBack() {
                    @Override
                    public void onStartConnect() {

                    }

                    @Override
                    public void onConnectSuccess(BluetoothDevice device, BluetoothSocket bluetoothSocket) {
                        //连接成功
                        try {
                            PrintUtils.setOutputStream(bluetoothSocket.getOutputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConnectFail(BluetoothDevice device, String string) {

                    }
                });
            }
        });
    }


    /**
     * 配对蓝牙设备
     */
    private void pinTargetDevice(int position) {
        //在配对之前，停止搜索
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        //获取要匹配的BluetoothDevice对象，后边的deviceList是你本地存的所有对象
        BluetoothDevice device = devicesArrayAdapter.getData().get(position).device;
        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {//没配对才配对
            try {
                Log.d("dfdf", "开始配对...");
                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                Boolean returnValue = (Boolean) createBondMethod.invoke(device);
                if (returnValue) {
                    Log.d("dfdf", "配对成功...");
                    showShortToast("配对成功");
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                // bluetooth is opened
                getDeviceList();
            } else {
                // bluetooth is not open
                showShortToast("蓝牙没有开启");
            }
        }
    }

    //获取设备列表
    protected void getDeviceList() {
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        List<DevicesBean> stringList = new ArrayList<>();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                stringList.add(new DevicesBean(device.getName() + "\n"
                        + device.getAddress(), device));
            }
        }
        devicesArrayAdapter.setNewData(stringList);
    }


    @OnClick({R.id.btn_saomiao, R.id.btn_dayin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_saomiao:
                break;
            case R.id.btn_dayin:
                // 打印照片
                PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
                PrintUtils.printText("炯阳是逗比");
                PrintUtils.selectCommand(PrintUtils.RESET);
                PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
                PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
                PrintUtils.printText("微百姓\n\n");
                PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
                PrintUtils.printText("桌号：1号桌\n\n");
                PrintUtils.selectCommand(PrintUtils.NORMAL);
                PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
                PrintUtils.printText(PrintUtils.printTwoData("订单编号", "201507161515\n"));
                PrintUtils.printText(PrintUtils.printTwoData("点菜时间", "2016-02-16 10:46\n"));
                PrintUtils.printText(PrintUtils.printTwoData("上菜时间", "2016-02-16 11:46\n"));
                PrintUtils.printText(PrintUtils.printTwoData("人数：2人", "收银员：张三\n"));
                PrintUtils.printText("--------------------------------\n");
                PrintUtils.selectCommand(PrintUtils.BOLD);
                PrintUtils.printText("--------------------------------\n");
                PrintUtils.selectCommand(PrintUtils.BOLD_CANCEL);
                PrintUtils.printText("--------------------------------\n");
                PrintUtils.printText(PrintUtils.printTwoData("合计", "53.50\n"));
                PrintUtils.printText(PrintUtils.printTwoData("抹零", "3.50\n"));
                PrintUtils.printText("--------------------------------\n");
                PrintUtils.printText(PrintUtils.printTwoData("应收", "50.00\n"));
                PrintUtils.printText("--------------------------------\n");
                PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
                PrintUtils.printText("备注：不要辣、不要香菜");
                PrintUtils.printText("\n\n\n\n\n");
                break;
        }
    }

    /**
     * 蓝牙连接线程
     */
    @SuppressLint("StaticFieldLeak")
    public class ConnectBlueTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket> {
        private BluetoothDevice bluetoothDevice;
        private ConnectBlueCallBack callBack;

        public ConnectBlueTask(ConnectBlueCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... bluetoothDevices) {
            bluetoothDevice = bluetoothDevices[0];
            BluetoothSocket socket = null;
            try {
                Log.d("dfdf", "开始连接socket,uuid:00001101-0000-1000-8000-00805F9B34FB");
                socket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                if (socket != null && !socket.isConnected()) {
                    socket.connect();
                }
            } catch (IOException e) {
                Log.e("dfdf", "socket连接失败");
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Log.e("dfdf", "socket关闭失败");
                }
            }
            return socket;
        }

        @Override
        protected void onPreExecute() {
            Log.d("dfdf", "开始连接");
            if (callBack != null) callBack.onStartConnect();
        }

        @Override
        protected void onPostExecute(BluetoothSocket bluetoothSocket) {
            if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
                Log.d("dfdf", "连接成功");
                if (callBack != null) callBack.onConnectSuccess(bluetoothDevice, bluetoothSocket);
            } else {
                Log.d("dfdf", "连接失败");
                if (callBack != null) callBack.onConnectFail(bluetoothDevice, "连接失败");
            }
        }
    }


    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }



    /**
     * 连接 （在配对之后调用）
     *
     * @param device
     */
    public void connect(BluetoothDevice device, ConnectBlueCallBack callBack) {
        //连接之前把扫描关闭
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        new ConnectBlueTask(callBack).execute(device);
    }

    public interface ConnectBlueCallBack {
        void onStartConnect();

        void onConnectSuccess(BluetoothDevice device, BluetoothSocket bluetoothSocket);

        void onConnectFail(BluetoothDevice device, String string);
    }
}
