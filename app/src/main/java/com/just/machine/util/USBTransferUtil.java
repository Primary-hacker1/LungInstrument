package com.just.machine.util;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.just.machine.model.UsbSerialData;
import com.just.machine.model.systemsetting.SixMinSysSettingBean;
import com.just.news.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * usb 数据传输工具
 */
public class USBTransferUtil {

    private String afterStr = "EDFFEDFF";//包尾
    private String headEcgStr1 = "A88B3055";//心电包抬头
    private String headEcgStr2 = "A88B3155";//注册包抬头
    private String headEcgStr3 = "A88B3255";//心电信息包抬头
    private String headDataStr = "A88A3214";//设备信息包抬头
    private byte bytesnull = (byte) 0x00;

    private String TAG = "USBTransferUtil";
    public static boolean isConnectUSB = false;  // 连接标识
    private Context my_context;
    private UsbManager manager;  // usb管理器

    private BroadcastReceiver usbReceiver;  // 广播监听：判断usb设备授权操作
    private static final String INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".INTENT_ACTION_GRANT_USB";  // usb权限请求标识
    private final String IDENTIFICATION = " USB-Serial Controller D";  // 目标设备标识

    private Map<Long, byte[]> map = new HashMap<>();//接收数据
    private Map<Long, byte[]> mapNew = new TreeMap<>();
    private Map<Long, String> mapBloodOxygen = new TreeMap<>();//血氧数据
    private String byteStr = "";
    private UsbSerialData usbSerialData = new UsbSerialData();
    private boolean isBegin = false;//是否开始试验
    private int bloodState = 0;
    private int bloodType = 0; //1运动前血压 2运动后血压
    private boolean xueyangType = true;
    private boolean circleBoolean = true;
    private boolean autoCircleBoolean = true;//自动计圈
    private int circleCount = 0;
    private int testType = 0;//0初始状态 1开始 2结束
    private boolean ignoreBlood = false;//是否忽略测量血压

    // 顺序： manager - availableDrivers（所有可用设备） - UsbSerialDriver（目标设备对象） - UsbDeviceConnection（设备连接对象） - UsbSerialPort（设备的端口，一般只有1个）
    private List<UsbSerialDriver> availableDrivers = new ArrayList<>();  // 所有可用设备
    private UsbSerialDriver usbSerialDriver;  // 当前连接的设备
    private UsbDeviceConnection usbDeviceConnection;  // 连接对象
    private UsbSerialPort usbSerialPort;  // 设备端口对象，通过这个读写数据
    private SerialInputOutputManager inputOutputManager;  // 数据输入输出流管理器

    // 连接参数，按需求自行修改，一般情况下改变的参数只有波特率，数据位、停止位、奇偶校验都是固定的8/1/none ---------------------
    private int baudRate = 460800;  // 波特率
    private int dataBits = 8;  // 数据位
    private int stopBits = UsbSerialPort.STOPBITS_1;  // 停止位
    private int parity = UsbSerialPort.PARITY_NONE;// 奇偶校验

    // 单例 -------------------------
    private static USBTransferUtil usbTransferUtil;

    public int getBloodState() {
        return bloodState;
    }

    public void setBloodState(int bloodState) {
        this.bloodState = bloodState;
    }

    public boolean isBegin() {
        return isBegin;
    }

    public void setBegin(boolean begin) {
        isBegin = begin;
    }

    public boolean isIgnoreBlood() {
        return ignoreBlood;
    }

    public void setIgnoreBlood(boolean ignoreBlood) {
        this.ignoreBlood = ignoreBlood;
    }


    public Map<Long, String> getMapBloodOxygen() {
        return mapBloodOxygen;
    }

    public void setMapBloodOxygen(Map<Long, String> mapBloodOxygen) {
        this.mapBloodOxygen = mapBloodOxygen;
    }

    public int getBloodType() {
        return bloodType;
    }

    public void setBloodType(int bloodType) {
        this.bloodType = bloodType;
    }

    public int getTestType() {
        return testType;
    }

    public void setTestType(int testType) {
        this.testType = testType;
    }

    public int getCircleCount() {
        return circleCount;
    }

    public void setCircleCount(int circleCount) {
        this.circleCount = circleCount;
    }

    public static USBTransferUtil getInstance() {
        if (usbTransferUtil == null) {
            usbTransferUtil = new USBTransferUtil();
        }
        return usbTransferUtil;
    }

    public void addOxygenData() {
        Random random =new Random();
        int data = random.nextInt(10) + 90;
        mapBloodOxygen.put(System.currentTimeMillis(),String.valueOf(data));
    }

    // 接口 -------------------------
    public interface OnUSBDateReceive {
        void onReceive(String data_str);
    }

    private OnUSBDateReceive onUSBDateReceive;

    public void setOnUSBDateReceive(OnUSBDateReceive onUSBDateReceive) {
        this.onUSBDateReceive = onUSBDateReceive;
    }

    public void init(Context context) {
        my_context = context;
        manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public void connect() {
        if (!isConnectUSB) {
            registerReceiver();  // 注册广播监听
            refreshDevice();  // 拿到已连接的usb设备列表
            connectDevice();  // 建立连接
        }
    }

    // 注册usb授权监听广播
    public void registerReceiver() {
        usbReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "onReceive: " + intent.getAction());
                if (INTENT_ACTION_GRANT_USB.equals(intent.getAction())) {
                    // 授权操作完成，连接
//                    boolean granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);  // 不知为何获取到的永远都是 false 因此无法判断授权还是拒绝
                    connectDevice();
                    //usb插入
                } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(intent.getAction())) {
                    connect();
                } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(intent.getAction())) {
                    isConnectUSB = false;
                    inputOutputManager = null;
                    if (onUSBDateReceive != null) {
                        onUSBDateReceive.onReceive(intent.getAction());
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_ACTION_GRANT_USB);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        my_context.registerReceiver(usbReceiver, filter);
    }

    // 刷新当前可用 usb设备
    public void refreshDevice() {
        availableDrivers.clear();
        availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        Log.e(TAG, "当前可用 usb 设备数量: " + availableDrivers.size());
        // 有设备可以连接
        if (availableDrivers.size() != 0) {
            // 当时开发用的是定制平板电脑有 2 个usb口，所以搜索到两个
            if (availableDrivers.size() > 1) {
                for (int i = 0; i < availableDrivers.size(); i++) {
                    UsbSerialDriver availableDriver = availableDrivers.get(i);
                    String productName = availableDriver.getDevice().getProductName();
                    Log.e(TAG, "productName: " + productName);
                    // 我是通过 ProductName 这个参数来识别我要连接的设备
                    if (productName.equals(IDENTIFICATION)) {
                        usbSerialDriver = availableDriver;
                    }
                }
            }
            // 通常手机只有充电口 1 个
            else {
                usbSerialDriver = availableDrivers.get(0);
            }
            usbSerialPort = usbSerialDriver.getPorts().get(0);  // 一般设备的端口都只有一个，具体要参考设备的说明文档
            // 同时申请设备权限
            if (!manager.hasPermission(usbSerialDriver.getDevice())) {
                int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0;
                PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(my_context, 0, new Intent(INTENT_ACTION_GRANT_USB), flags);
                manager.requestPermission(usbSerialDriver.getDevice(), usbPermissionIntent);
            }
        }
        // 没有设备
        else {
            Toast.makeText(my_context, "请先接入设备", Toast.LENGTH_SHORT).show();
        }
    }

    // 连接设备
    public void connectDevice() {
        if (usbSerialDriver == null || inputOutputManager != null) {
            return;
        }
        // 判断是否拥有权限
        boolean hasPermission = manager.hasPermission(usbSerialDriver.getDevice());
        if (hasPermission) {
            usbDeviceConnection = manager.openDevice(usbSerialDriver.getDevice());  // 拿到连接对象
            if (usbSerialPort == null) {
                return;
            }
            try {
                usbSerialPort.open(usbDeviceConnection);  // 打开串口
                usbSerialPort.setParameters(baudRate, dataBits, stopBits, parity);  // 设置串口参数：波特率 - 115200 ， 数据位 - 8 ， 停止位 - 1 ， 奇偶校验 - 无
                startReceiveData();  // 开启数据监听
//                init_device();  // 下发初始化指令
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(my_context, "请先授予权限再连接", Toast.LENGTH_SHORT).show();
        }
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private byte[] readBuffer = new byte[1024 * 2];  // 缓冲区

    // 开启数据接收监听
    public void startReceiveData() {
        if (usbSerialPort == null || !usbSerialPort.isOpen()) {
            return;
        }
        inputOutputManager = new SerialInputOutputManager(usbSerialPort, new SerialInputOutputManager.Listener() {
            @Override
            public void onNewData(byte[] data) {
                // 在这里处理接收到的 usb 数据 -------------------------------
                // 按照结尾标识符处理
//                baos.write(data,0,data.length);
//                readBuffer = baos.toByteArray();
//                if (readBuffer.length >= 2 && readBuffer[readBuffer.length - 2] == (byte)'\r' && readBuffer[readBuffer.length - 1] == (byte)'\n') {
//                    String data_str = bytes2string(readBuffer);
//                    Log.i(TAG, "收到 usb 数据: " + data_str);
//                    if(onUSBDateReceive!=null){onUSBDateReceive.onReceive(data_str);}
//                    baos.reset();  // 重置
//                }
                // 直接处理
                String data_str = CRC16Util.bytes2Hex(data);
                Log.i(TAG, "收到 usb 数据: " + data_str);
                try {
                    Long time = System.currentTimeMillis();
                    if (map.containsKey(time)) {
                        time = time + 1;
                        if (!map.containsKey(time)) {
                            map.put(time, data);
                        }
                    } else {
                        map.put(time, data);
                    }
                    parseData(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRunError(Exception e) {
                Log.e(TAG, "usb 断开了");
                isConnectUSB = false;
                e.printStackTrace();
            }
        });
        inputOutputManager.start();
        isConnectUSB = true;  // 修改连接标识
        Toast.makeText(my_context, "连接成功", Toast.LENGTH_SHORT).show();
    }

    // 下发数据：建议使用线程池
    public void write(byte[] data_bytes) {
        if (usbSerialPort != null) {
            Log.e(TAG, "当前usb状态: isOpen-" + usbSerialPort.isOpen());
            // 当串口打开时再下发
            if (usbSerialPort.isOpen()) {
                if (data_bytes == null || data_bytes.length == 0) return;
                try {
                    usbSerialPort.write(data_bytes, 0);  // 写入数据，延迟设置太大的话如果下发间隔太小可能报错
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "write: usb 未连接");
            }
        }
    }


    // 断开连接
    public void disconnect() {
        try {
            // 停止数据接收监听
            if (inputOutputManager != null) {
                inputOutputManager.stop();
                inputOutputManager = null;
            }
            // 关闭端口
            if (usbSerialPort != null) {
                usbSerialPort.close();
                usbSerialPort = null;
            }
            // 关闭连接
            if (usbDeviceConnection != null) {
                usbDeviceConnection.close();
                usbDeviceConnection = null;
            }
            // 清除设备
            if (usbSerialDriver != null) {
                usbSerialDriver = null;
            }
            // 清空设备列表
            availableDrivers.clear();
            // 注销广播监听
            if (usbReceiver != null) {
                my_context.unregisterReceiver(usbReceiver);
            }
            if (isConnectUSB) {
                isConnectUSB = false;  // 修改标识
            }
            byteStr = "";
            Log.e(TAG, "断开连接");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 下发设备初始化指令
    public void init_device() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        write(CRC16Util.hex2bytes("6861686168610D0A"));  // 查询 IC 信息
    }

    /**
     * 数据解析
     *
     * @param map
     */
    private void parseData(Map<Long, byte[]> map) {
        if (map.size() > 0) {
            if (mapNew.size() > 0) {
                mapNew.clear();
            }
            try {
                Set<Long> set = map.keySet();
                Iterator<Long> iterator = set.iterator();
                while (iterator.hasNext()) {
                    Long mapLongKey = iterator.next();
                    mapNew.put(mapLongKey, map.get(mapLongKey));
                }
            } catch (ConcurrentModificationException cc) {
                cc.printStackTrace();
            }
            for (Long key : mapNew.keySet()) {
                byteStr = (CRC16Util.bytesToHexString(Objects.requireNonNull(mapNew.get(key))))+byteStr;
                map.remove(key);
            }
        }

        if (!TextUtils.isEmpty(byteStr) && byteStr.length() >= 8) {
            String titBT = byteStr.substring(0, 8);
            //验证包头
            if (titBT.equals(headEcgStr1) || titBT.equals(headEcgStr2) || titBT.equals(headEcgStr3)
                    || titBT.equals(headDataStr)) {
                if (byteStr.contains(afterStr)) {
                    String[] strings = byteStr.split(afterStr);
                    String dataStr = strings[0];
                    //验证此帧中是否含有数据包
                    if (!dataStr.contains(headDataStr)) {
                        boolean forEachState = false;
                        if (strings.length > 1) {
                            for (int dd = 1; dd < strings.length; dd++) {
                                dataStr = dataStr + afterStr + strings[dd];
                                if (dataStr.contains(headDataStr)) {
                                    forEachState = true;
                                    break;
                                }
                            }
                        }
                        if (!forEachState) {
                            return;
                        }
                    }
                    //8为以包尾分组时，漏掉的包尾长度
                    int start = dataStr.length() + 8;
                    if (start > byteStr.length()) {
                        start = byteStr.length();
                    }
                    String dataDaStr = "";
                    String headStr = "";
                    if (dataStr.length() >= 8) {
                        headStr = dataStr.substring(0, 8);
                    }

                    //心电包
                    if (headStr.equals(headEcgStr1)) {
                        //区分心电数据和数据包数据
                        String[] strs = dataStr.split(headDataStr);
                        //44=，52-8(分组时漏掉的包头长
                        if (strs.length == 2 && strs[1].length() == 44) {
                            dataDaStr = headDataStr + strs[1];
                        }
                    } else if (headStr.equals(headEcgStr2) || headStr.equals(headEcgStr3)) {
                        //心电注册包
                        String[] strs = dataStr.split(headDataStr);
                        if (strs.length == 2 && strs[1].length() == 44) {
                            dataDaStr = headDataStr + strs[1];
                        }
                    } else if (headStr.equals(headDataStr) && dataStr.length() == 52) {
                        //单独数据包
                        dataDaStr = dataStr;
                    }

                    if (!TextUtils.isEmpty(dataDaStr)) {
                        byte[] bytes = CRC16Util.hexStringToBytes(dataDaStr);
                        if (CRC16Util.checkCrcGet(bytes)) {
                            // 心电连接状态
                            if (bytes[4] == (byte) 0x11) {
                                //连接
                                usbSerialData.setEcgState("心电已连接");
                            } else if (bytes[4] == (byte) 0x10) {
                                //断开
                                usbSerialData.setEcgState("心电未连接");
                            }

                            //血氧连接状态
                            if (bytes[5] == (byte) 0x21) {
                                //连接
                                usbSerialData.setBloodOxyState("血氧已连接");
                            } else if (bytes[5] == (byte) 0x20) {
                                //断开
                                usbSerialData.setBloodOxyState("血氧未连接");
                            }

                            //血压连接状态
                            if (bytes[6] == (byte) 0x31) {
                                usbSerialData.setBloodPressureState("血压已连接");
                            } else if (bytes[6] == (byte) 0x30) {
                                usbSerialData.setBloodPressureState("血压未连接");
                            }

                            //电池等级
                            String dcLevelStr = CRC16Util.bytesToHexString(new byte[]{bytes[8]});
                            switch (dcLevelStr) {
                                case "55":
                                    usbSerialData.setBatteryLevel(5);
                                    break;
                                case "54":
                                    usbSerialData.setBatteryLevel(4);
                                    break;
                                case "53":
                                    usbSerialData.setBatteryLevel(3);
                                    break;
                                case "52":
                                    usbSerialData.setBatteryLevel(2);
                                    break;
                                case "51":
                                case "50":
                                    usbSerialData.setBatteryLevel(1);
                                    break;
                            }
                            //血压数据
                            if (bloodState == 0) {
                                if (bytes[13] == bytesnull && bytes[14] == bytesnull) {
                                    usbSerialData.setBloodState("未测量血压");
                                }

                                if (bytes[13] != bytesnull && bytes[14] != bytesnull) {
                                    byte[] bloodByte = {bytes[13]};
                                    String bloodValue = Integer.valueOf(CRC16Util.bytesToHexString(bloodByte), 16).toString();
                                    //测量血压中
                                    if (bytes[13] != (byte) 0xFF && bytes[14] == (byte) 0xFF) {
                                        usbSerialData.setBloodState("测量血压中");
                                        usbSerialData.setBloodHigh(bloodValue);
                                        //测量失败
                                    } else if ((bytes[13] == (byte) 0xFF && bytes[14] == (byte) 0xFF)
                                            || bytes[6] == (byte) 0x30) {
                                        usbSerialData.setBloodState("测量血压失败");
                                        SixMinCmdUtils.Companion.failMeasureBloodPressure();
                                        //测量成功
                                    } else if ((bytes[13] != (byte) 0xFF && bytes[14] != (byte) 0xFF)) {
                                        byte[] bloodBehindByte = {bytes[14]};
                                        String bloodBehindValue = Integer.valueOf(CRC16Util.bytesToHexString(bloodBehindByte), 16).toString();
                                        usbSerialData.setBloodHigh(bloodValue);
                                        usbSerialData.setBloodLow(bloodBehindValue);
                                        SixMinCmdUtils.Companion.resetMeasureBloodPressure();
                                        //运动前
                                        if (bloodType == 0) {
                                            usbSerialData.setBloodHighFront(bloodValue);
                                            usbSerialData.setBloodLowFront(bloodBehindValue);
                                        }
                                        usbSerialData.setBloodState("测量血压成功");
                                    }
                                }
                            }

                            //血氧数据
                            if (bytes[10] == (byte) 0x61 && bytes[11] == (byte) 0x71) {
                                //系统时间戳
                                Long time = System.currentTimeMillis();
                                byte[] b = {bytes[12]};
                                int oxygen = Integer.valueOf(CRC16Util.bytesToHexString(b), 16);
                                if (oxygen > 99) {
                                    oxygen = 99;
                                }
                                String str = Integer.toString(oxygen);
                                mapBloodOxygen.put(time, str);
                                usbSerialData.setBloodOxygen(String.valueOf(oxygen));
                            }else{
                                usbSerialData.setBloodOxygen("--");
                            }

                            //步数数据
                            if (testType == 1 && (bytes[15] != bytesnull || bytes[16] != bytesnull)) {
                                byte[] bytesBS = {bytes[15], bytes[16]};
                                String bsStr = CRC16Util.bytesToHexString(bytesBS);
                                Integer steps = Integer.valueOf(bsStr, 16);
                            }
                            //圈数数据
                            if (testType == 1 && bytes[17] != bytesnull) {
                                //圈数
                                byte[] b = {bytes[17]};
                                Integer qsInt = Integer.valueOf(CRC16Util.bytesToHexString(b), 16);
                                SixMinSysSettingBean bean = new SixMinSysSettingBean();
                                if (circleBoolean && bean.getSysOther().getCircleCountType().equals("0") && qsInt == 1) {
                                    ++circleCount;
                                   usbSerialData.setCircleCount(String.valueOf(circleCount));
                                }
                            }
                        }
                    }

                    LiveDataBus.get().with("111").postValue(new Gson().toJson(usbSerialData));
                }
            }else{
                byteStr = byteStr.substring(2, byteStr.length());
            }
        }
    }
}
