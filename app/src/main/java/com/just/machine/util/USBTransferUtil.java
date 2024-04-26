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

import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.just.machine.model.BloodOxyLineEntryBean;
import com.just.machine.model.UsbSerialData;
import com.just.machine.model.sixminreport.SixMinBloodOxygen;
import com.just.machine.model.sixminreport.SixMinReportEvaluation;
import com.just.machine.model.sixminreport.SixMinReportWalk;
import com.just.machine.model.systemsetting.SixMinSysSettingBean;
import com.just.news.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public boolean isConnectUSB = false;  // 连接标识
    private Context my_context;
    private UsbManager manager;  // usb管理器

    private BroadcastReceiver usbReceiver;  // 广播监听：判断usb设备授权操作
    private static final String INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".INTENT_ACTION_GRANT_USB";  // usb权限请求标识
    private final String IDENTIFICATION = " USB-Serial Controller D";  // 目标设备标识

    public Map<Long, byte[]> map = new HashMap<>();//接收数据
    public Map<Long, byte[]> mapNew = new TreeMap<>();
    public Map<Long, String> mapBloodOxygen = new TreeMap<>();//血氧数据
    public List<Integer> bloodListAvg = new ArrayList<>();//血氧数据，每秒一个值
    public List<Integer> bloodAllListAvg = new ArrayList<>();//6分钟所有血氧数据
    public List<BloodOxyLineEntryBean> bloodOxyLineData = new ArrayList<>();//6分钟血氧折线图数据
    public String byteStr = "";
    public UsbSerialData usbSerialData = null;
    public boolean isBegin = false;//是否开始试验
    public int bloodState = 0;
    public int bloodType = 0; //1运动前血压 2运动后血压
    public boolean xueyangType = true;
    public boolean circleBoolean = false;
    public boolean autoCircleBoolean = true;//自动计圈
    public int circleCount = 0;
    public int testType = 0;//0初始状态 1开始 2结束 3采集运动后心率
    public boolean ignoreBlood = false;//是否忽略测量血压
    public int updateBluetooth = 0;//
    public boolean ecgConnection = false;//心电连接状态
    public boolean bloodPressureConnection = false;//血压连接状态
    public boolean bloodOxygenConnection = false;//血氧连接状态
    public int batteryLevel = 0;//电池电量
    public String restBloodOxy = "";//静息血氧集合
    public int restTime = 0;//休息时长
    public String stepsStr = "0";//步数
    public int checkBSInd = 0;//检查步数字段
    public String checkBSStr = "0";//检查步数
    public String min = "";//每次计圈记住倒计时分钟
    public String sec1 = "";//每次计圈记住倒计时秒
    public String sec2 = "";//每次计圈记住倒计时钟

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

    public static USBTransferUtil getInstance() {
        if (usbTransferUtil == null) {
            usbTransferUtil = new USBTransferUtil();
        }
        return usbTransferUtil;
    }

    // 接口 -------------------------
    public interface OnUSBDateReceive {
        void onReceive(String data_str);
    }

    private OnUSBDateReceive onUSBDateReceive;

    public void setOnUSBDateReceive(OnUSBDateReceive onUSBDateReceive) {
        this.onUSBDateReceive = onUSBDateReceive;
    }

    public int getCircleCount() {
        return circleCount;
    }

    public void setCircleCount(int circleCount) {
        this.circleCount = circleCount;
    }

    public void init(Context context) {
        my_context = context;
        manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        usbSerialData = new UsbSerialData();
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
                    ecgConnection = false;
                    bloodOxygenConnection = false;
                    bloodPressureConnection = false;
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
                ecgConnection = false;
                bloodOxygenConnection = false;
                bloodPressureConnection = false;
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
                ecgConnection = false;
                bloodOxygenConnection = false;
                bloodPressureConnection = false;
            }
            release();
            Log.e(TAG, "断开连接");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        map.clear();
        mapNew.clear();
        mapBloodOxygen.clear();
        byteStr = null;
        usbSerialData = null;
        isBegin = false;//是否开始试验
        bloodState = 0;
        bloodType = 0; //1运动前血压 2运动后血压
        xueyangType = true;
        circleBoolean = false;
        autoCircleBoolean = true;//自动计圈
        circleCount = 0;
        testType = 0;//0初始状态 1开始 2结束
        ignoreBlood = false;//是否忽略测量血压
        updateBluetooth = 0;//
        ecgConnection = false;//心电连接状态
        bloodPressureConnection = false;//血压连接状态
        bloodOxygenConnection = false;//血氧连接状态
        batteryLevel = 0;//电池电量
        restBloodOxy = null;//静息血氧集合
        restTime = 0;//休息时长
        stepsStr = null;//步数
        checkBSInd = 0;
        checkBSStr = null;
        bloodOxyLineData.clear();
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
                for (Long mapLongKey : set) {
                    mapNew.put(mapLongKey, map.get(mapLongKey));
                }
            } catch (ConcurrentModificationException cc) {
                cc.printStackTrace();
            }
            for (Long key : mapNew.keySet()) {
                byteStr = (CRC16Util.bytesToHexString(Objects.requireNonNull(mapNew.get(key)))) + byteStr;
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
                                ecgConnection = true;
                            } else if (bytes[4] == (byte) 0x10) {
                                //断开
                                ecgConnection = false;
                            }

                            //血氧连接状态
                            if (bytes[5] == (byte) 0x21) {
                                //连接
                                bloodOxygenConnection = true;
                            } else if (bytes[5] == (byte) 0x20) {
                                //断开
                                bloodOxygenConnection = false;
                            }

                            //血压连接状态
                            if (bytes[6] == (byte) 0x31) {
                                bloodPressureConnection = true;
                            } else if (bytes[6] == (byte) 0x30) {
                                bloodPressureConnection = false;
                            }

                            //电池等级
                            String dcLevelStr = CRC16Util.bytesToHexString(new byte[]{bytes[8]});
                            switch (dcLevelStr) {
                                case "55":
                                    batteryLevel = 5;
                                    break;
                                case "54":
                                    batteryLevel = 4;
                                    break;
                                case "53":
                                    batteryLevel = 3;
                                    break;
                                case "52":
                                    batteryLevel = 2;
                                    break;
                                case "51":
                                case "50":
                                    batteryLevel = 1;
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
                                        } else if (bloodType == 2) {
                                            usbSerialData.setBloodHighBehind(bloodValue);
                                            usbSerialData.setBloodLowBehind(bloodBehindValue);
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
                                //静息血氧集合
                                if (testType == 0) {
                                    restBloodOxy = restBloodOxy + str + ",";
                                }
                            } else {
                                usbSerialData.setBloodOxygen("--");
                            }

                            //步数数据
                            if (testType == 1 && (bytes[15] != bytesnull || bytes[16] != bytesnull)) {
                                byte[] bytesBS = {bytes[15], bytes[16]};
                                String stepsHexStr = CRC16Util.bytesToHexString(bytesBS);
                                Integer steps = Integer.valueOf(stepsHexStr, 16);
                                stepsStr = String.valueOf(steps);
                                usbSerialData.setStepsCount(String.valueOf(steps));
                            }
                            //圈数数据
                            if (testType == 1 && bytes[17] != bytesnull) {
                                //圈数
                                byte[] b = {bytes[17]};
                                Integer qsInt = Integer.valueOf(CRC16Util.bytesToHexString(b), 16);
                                SixMinSysSettingBean bean = new SixMinSysSettingBean();
                                if (circleBoolean && bean.getSysOther().getCircleCountType().equals("0") && qsInt == 1) {
                                    ++circleCount;
//                                    usbSerialData.setCircleCount(String.valueOf(circleCount));
                                }
                            }

                            //计圈、记步设备、低功耗工作状态
                            String byteStr = CRC16Util.bytesToHexString(new byte[]{bytes[18]});
                            //System.out.println(dataStr);
                            String dGHStr = byteStr.substring(0, 1);
                            String jqAndJbAndDGhStr = byteStr.substring(1, 2);
                            //验证蓝牙参数是否更新成功
                            if (dGHStr.equals("1")) {
                                updateBluetooth = 1;
                            }
                        }
                    }

                    LiveDataBus.get().with("simMinTest").postValue(new Gson().toJson(usbSerialData));
                }
            } else {
                byteStr = byteStr.substring(2, byteStr.length());
            }
        }
    }

    /**
     * 处理运动步速
     *
     * @param percentStr
     * @param strideAverage
     * @return
     */
    public String dealYdbsStrs(String percentStr, BigDecimal strideAverage) {
        String ydbsStr = (strideAverage.multiply(new BigDecimal(percentStr))
                .setScale(1, BigDecimal.ROUND_HALF_UP)).toString();
        return ydbsStr;
    }

    /**
     * 处理推荐距离
     *
     * @param percentStr
     * @param strideAverage
     * @param shichang
     * @return
     */
    public String dealtjjlStrs(String percentStr, BigDecimal strideAverage, Integer shichang) {
        String tjjlStr = (strideAverage.multiply(new BigDecimal(percentStr)).multiply(new BigDecimal(shichang))
                .setScale(1, BigDecimal.ROUND_HALF_UP)).toString();
        return tjjlStr;
    }

    /**
     * 预生成报告
     * 报告血氧表的处理
     *
     * @return
     */
    public void dealBlood(SixMinBloodOxygen reportBlood, String bloodOxyLineData) {
        //处理静息血氧
        if(!restBloodOxy.isEmpty()){
            String[] strings = restBloodOxy.split(",");
            int bloodTotInt = 0;
            if (strings.length > 0) {
                for (String string : strings) {
                    if(!string.isEmpty()){
                        int bloodInt = Integer.parseInt(string);
                        bloodTotInt = bloodTotInt + bloodInt;
                    }
                }
                BigDecimal bloodDec = new BigDecimal(bloodTotInt).divide(new BigDecimal(strings.length), 0, RoundingMode.HALF_UP);
                int bloodStop = Integer.parseInt(bloodDec.toString());
                reportBlood.setBloodStop(Integer.toString(bloodStop));
            }
        }
        Integer min = 0;
        Integer max = 0;
        if (bloodAllListAvg.size() > 0) {
            min = Collections.min(bloodAllListAvg);
            max = Collections.max(bloodAllListAvg);
        }
        if (null == reportBlood.getBloodStop()) {
            reportBlood.setBloodStop("0");
        }
        if (null == reportBlood.getBloodOne()) {
            reportBlood.setBloodOne("0");
        }
        if (null == reportBlood.getBloodTwo()) {
            reportBlood.setBloodTwo("0");
        }
        if (null == reportBlood.getBloodThree()) {
            reportBlood.setBloodThree("0");
        }
        if (null == reportBlood.getBloodFour()) {
            reportBlood.setBloodFour("0");
        }
        if (null == reportBlood.getBloodFive()) {
            reportBlood.setBloodFive("0");
        }
        if (null == reportBlood.getBloodSix()) {
            reportBlood.setBloodSix("0");
        }
        //平均值，只是以下七个数据
        Integer[] number = {
                Integer.parseInt(reportBlood.getBloodOne()),
                Integer.parseInt(reportBlood.getBloodTwo()),
                Integer.parseInt(reportBlood.getBloodThree()),
                Integer.parseInt(reportBlood.getBloodFour()),
                Integer.parseInt(reportBlood.getBloodFive()),
                Integer.parseInt(reportBlood.getBloodSix())
        };
        Integer start = 0;
        int len = 0;
        for (Integer item : number) {
            if (item != 0) {
                ++len;
            }
            start += item;
        }
        BigDecimal avg = new BigDecimal("0");
        if (len != 0) {
            avg = new BigDecimal(start).divide(new BigDecimal(len), 0, RoundingMode.HALF_UP);
        }
        reportBlood.setBloodSmall(String.valueOf(min));
        reportBlood.setBloodBig(String.valueOf(max));
        reportBlood.setBloodAverage(avg.toString());
        reportBlood.setBloodAll(bloodOxyLineData);
    }

    /**
     * 处理血氧表之前
     *
     * @param reportBlood
     * @param min
     * @param bloodListAvg
     */
    public void dealBloodBe(SixMinBloodOxygen reportBlood, Integer min, List<Integer> bloodListAvg) {
        if (null != reportBlood && bloodListAvg.size() > 0) {
            Integer bloodInt = 0;
            int start = 0;
            for (Integer item : bloodListAvg) {
                start += item;
            }
            BigDecimal avgBlood = new BigDecimal(start).divide(new BigDecimal(bloodListAvg.size()), 0, RoundingMode.HALF_UP);
            bloodInt = Integer.parseInt(avgBlood.toString());
            if (min == 5) {
                reportBlood.setBloodOne(String.valueOf(bloodInt));
            } else if (min == 4) {
                reportBlood.setBloodTwo(String.valueOf(bloodInt));
            } else if (min == 3) {
                reportBlood.setBloodThree(String.valueOf(bloodInt));
            } else if (min == 2) {
                reportBlood.setBloodFour(String.valueOf(bloodInt));
            } else if (min == 1) {
                reportBlood.setBloodFive(String.valueOf(bloodInt));
            } else if (min == 0) {
                reportBlood.setBloodSix(String.valueOf(bloodInt));
            }
        }
    }

    /**
     * 处理步数表
     *
     * @param reportWalk
     * @return
     */
    public void dealWalk(SixMinReportWalk reportWalk) {
        if (null != reportWalk) {
            int numAll = 0;
            int len = 0;
            List<Integer> integers = new ArrayList<>();
            if (!reportWalk.getWalkOne().equals("")) {
                integers.add(Integer.parseInt(reportWalk.getWalkOne()));
                numAll += Integer.parseInt(reportWalk.getWalkOne());
                ++len;
            } else {
                reportWalk.setWalkOne("0");
            }
            if (!reportWalk.getWalkTwo().equals("")) {
                integers.add(Integer.parseInt(reportWalk.getWalkTwo()));
                numAll += Integer.parseInt(reportWalk.getWalkTwo());
                ++len;
            } else {
                reportWalk.setWalkTwo("0");
            }
            if (!reportWalk.getWalkThree().equals("")) {
                integers.add(Integer.parseInt(reportWalk.getWalkThree()));
                numAll += Integer.parseInt(reportWalk.getWalkThree());
                ++len;
            } else {
                reportWalk.setWalkThree("0");
            }
            if (!reportWalk.getWalkFour().equals("")) {
                integers.add(Integer.parseInt(reportWalk.getWalkFour()));
                numAll += Integer.parseInt(reportWalk.getWalkFour());
                ++len;
            } else {
                reportWalk.setWalkFour("0");
            }
            if (!reportWalk.getWalkFive().equals("")) {
                integers.add(Integer.parseInt(reportWalk.getWalkFive()));
                numAll += Integer.parseInt(reportWalk.getWalkFive());
                ++len;
            } else {
                reportWalk.setWalkFive("0");
            }
            if (!reportWalk.getWalkSix().equals("")) {
                integers.add(Integer.parseInt(reportWalk.getWalkSix()));
                numAll += Integer.parseInt(reportWalk.getWalkSix());
                ++len;
            } else {
                reportWalk.setWalkSix("0");
            }
            Integer min = 0;
            Integer max = 0;
            if (integers.size() > 0) {
                min = Collections.min(integers);
                max = Collections.max(integers);
            }
            reportWalk.setWalkBig(max.toString());
            reportWalk.setWaklSmall(min.toString());
            BigDecimal avg = new BigDecimal("0");
            if (len != 0) {
                avg = new BigDecimal(numAll).divide(new BigDecimal(len), 0, RoundingMode.HALF_UP);
            }
            reportWalk.setWalkAverage(avg.toString());
        }
    }

    /**
     * 预生成报告 自动计圈
     * 综合评估表
     *
     * @param etion
     * @param min
     * @param sec
     * @param type    0：正常流程走完，1主动停止
     * @return
     */
    public SixMinReportEvaluation dealPreption(SixMinSysSettingBean systemDto,SixMinReportEvaluation etion, String min, String sec,
                                         Integer type, int min1, int sec1) {
        Integer lenKu = 0;
        Integer tcLocation = 1;
        BigDecimal len = new BigDecimal(0);//步行圈数的距离
        if (null != systemDto) {
            //根据系统设置里的场地长度算出圈的长度
            etion.setFieldLength(systemDto.getSysOther().getAreaLength());
            lenKu = Integer.valueOf(systemDto.getSysOther().getAreaLength());
            //起始摆放
            len = new BigDecimal(lenKu * Integer.parseInt(etion.getTurnsNumber()) * 2);
        }
        //计算未走完的那圈的距离
        //每次计圈的时候记录一下当前的倒计时，取结束的最后一次，
        //用最后一次计圈的倒计时当作时间，把走的圈数的长度当作距离，算出速度
        BigDecimal mainingTime = new BigDecimal(0);//最后一圈的时间
        if (!TextUtils.isEmpty(min) && !TextUtils.isEmpty(sec)) {
            mainingTime = new BigDecimal(min + "." + sec);
        }
        if (mainingTime.compareTo(BigDecimal.ZERO) == 0) {
            mainingTime = new BigDecimal("5.59");
        }
        //圈数用的时间
        BigDecimal withTime = new BigDecimal("5.59").subtract(mainingTime);
        Integer mainingIn = 0;
        //主动停止的，总时长根据实际时长来
        if (type == 1) {
            //停止的时间
            String time = String.valueOf(min1) + "." + String.valueOf(sec1);
            if (time.length() == 3) {
                time = String.valueOf(min1) + ".0" + String.valueOf(sec1);
            }
            BigDecimal syTime = new BigDecimal(time).subtract(withTime);
            String syTimeStr = syTime.toString();
            //把剩下的时间当作时间，上面的是速度，算出未走完那圈的走了的距离
            mainingIn = Integer.valueOf(syTimeStr.substring(0, 1)) * 60
                    + Integer.valueOf(syTimeStr.substring(2, syTimeStr.length()));
        } else {
            //把剩下的时间当作时间，上面的是速度，算出未走完那圈的走了的距离
            mainingIn = Integer.valueOf(min) * 60 + Integer.valueOf(sec);
        }
        String minStr = withTime.toString().substring(0, 1);
        String secStr = withTime.toString().substring(2, withTime.toString().length());
        //将用了的时间换算成秒
        Integer minRe = Integer.valueOf(minStr) * 60;
        Integer timeRe = minRe + Integer.valueOf(secStr);
        BigDecimal speed;
        BigDecimal distance;
        if (withTime.compareTo(BigDecimal.ZERO) == 0) {
            distance = new BigDecimal("0");
        } else {
            speed = len.divide(new BigDecimal(timeRe), 1, RoundingMode.HALF_UP);
            //剩下的时间里走的距离
            distance = new BigDecimal(mainingIn).multiply(speed, new MathContext(3));
        }
        //最后一圈还剩多少距离
        BigDecimal unfinished = new BigDecimal(0);
        if (tcLocation == 1) {
            //起始摆放
            lenKu = lenKu * 2;
        }
        unfinished = (new BigDecimal(lenKu)).subtract(distance);
        //如果剩下时间里走的距离超过了一圈的距离，为负数,往前加一圈，以此类推，剩下的再算
        Integer quanshu = 0;
        //居中摆放
        if (tcLocation == 0) {

        } else if (tcLocation == 1) {
            //起始摆放
            quanshu = Integer.valueOf(etion.getTurnsNumber());
        }
        BigDecimal totalDistance;
        if (unfinished.compareTo(BigDecimal.ZERO) < 0) {
            do {
                ++quanshu;
                len = len.add(new BigDecimal(lenKu));
                unfinished = (new BigDecimal(lenKu)).add(unfinished);
                //大于等于0
            } while (unfinished.compareTo(BigDecimal.ZERO) != 1);
            etion.setTurnsNumber(quanshu.toString());
            BigDecimal dec = new BigDecimal(lenKu).subtract(unfinished);
            dec = len.add(dec);
            totalDistance = dec;
            etion.setTotalDistance(dec.toString());
        } else {
            BigDecimal dec = len.add(distance);
            totalDistance = dec;
            etion.setTotalDistance(dec.toString());
        }
        //居中摆放
        if (tcLocation == 0) {
            boolean check = checkNumEven(quanshu.toString());
            BigDecimal intBigDec = new BigDecimal(lenKu).subtract(unfinished);
            if (check) {
                quanshu = quanshu / 2;
            } else {
                quanshu = (quanshu - 1) / 2;
                intBigDec = new BigDecimal(lenKu).add(intBigDec);
            }
            BigDecimal reDec = intBigDec.add(new BigDecimal(lenKu / 2));
            if (reDec.compareTo(new BigDecimal(lenKu * 2)) > -1) {
                ++quanshu;
                reDec = reDec.subtract(new BigDecimal(lenKu * 2));
            }
            unfinished = new BigDecimal(lenKu * 2).subtract(reDec);
            etion.setTurnsNumber(quanshu.toString());
            etion.setUnfinishedDistance(unfinished.toString());
        } else if (tcLocation == 1) {
            etion.setUnfinishedDistance(unfinished.toString());
        }
        //计算代谢当量 (4.928 + 0.023 *距离) / 3.5
        BigDecimal b3 = dealMetabEquivalent(unfinished);
        etion.setMetabEquivalent(b3.toString());
        //计算心肺等级
        String cardiopuLevel = dealCardiopuLevel(totalDistance);
        etion.setCardiopuLevel(cardiopuLevel);
        Integer degree = dealDegree(totalDistance);
        etion.setCardiopuDegree(degree.toString());
        return etion;
    }

    /**
     * 预生成报告 手动计圈
     * 综合评估表
     *
     * @param etion
     * @param min
     * @param sec
     * @param type  0：正常流程走完，1主动停止
     * @param min1
     * @param sec1
     * @return
     */
    public SixMinReportEvaluation dealPreptionSD(SixMinReportEvaluation etion, SixMinSysSettingBean systemDto, String min, String sec,
                                                 Integer type, int min1, int sec1) {
        int lenKu = 0;
        BigDecimal len = new BigDecimal("0");//步行圈数的距离
        if (null != systemDto) {
            //根据系统设置里的场地长度算出圈的长度
            etion.setFieldLength(systemDto.getSysOther().getAreaLength());
            lenKu = Integer.parseInt(systemDto.getSysOther().getAreaLength());
            len = BigDecimal.valueOf((long) lenKu * Integer.parseInt(etion.getTurnsNumber()) * 2);
        }
        //计算未走完的那圈的距离
        //每次计圈的时候记录一下当前的倒计时，取结束的最后一次，
        //用最后一次计圈的倒计时当作时间，把走的圈数的长度当作距离，算出速度
        BigDecimal mainingTime = new BigDecimal("0");//最后一圈的时间
        if (!TextUtils.isEmpty(min) && !TextUtils.isEmpty(sec)) {
            mainingTime = new BigDecimal(min + "." + sec);
        }
        if (mainingTime.compareTo(BigDecimal.ZERO) == 0) {
            mainingTime = new BigDecimal("5.59");
        }
        //圈数用的时间
        BigDecimal withTime = new BigDecimal("5.59").subtract(mainingTime, new MathContext(3));
        Integer mainingIn = 0;
        //主动停止的，总时长根据实际时长来
        if (type == 1) {
            //停止的时间
            String time = String.valueOf(min1) + "." + String.valueOf(sec1);
            if (time.length() == 3) {
                time = String.valueOf(min1) + ".0" + String.valueOf(sec1);
            }
            BigDecimal syTime = new BigDecimal(time).subtract(withTime, new MathContext(3));
            String syTimeStr = syTime.toString();
            //把剩下的时间当作时间，上面的是速度，算出未走完那圈的走了的距离
            mainingIn = Integer.valueOf(syTimeStr.substring(0, 1)) * 60
                    + Integer.valueOf(syTimeStr.substring(2, syTimeStr.length()));
        } else {
            //把剩下的时间当作时间，上面的是速度，算出未走完那圈的走了的距离
            mainingIn = Integer.valueOf(min) * 60 + Integer.valueOf(sec);
        }
        String minStr = withTime.toString().substring(0, 1);
        String secStr = withTime.toString().substring(2, withTime.toString().length());
        //将用了的时间换算成秒
        Integer minRe = Integer.valueOf(minStr) * 60;
        Integer timeRe = minRe + Integer.valueOf(secStr);
        BigDecimal speed;
        BigDecimal distance;
        if (withTime.compareTo(BigDecimal.ZERO) == 0) {
            distance = new BigDecimal("0");
        } else {
            speed = len.divide(new BigDecimal(timeRe), 1, RoundingMode.HALF_UP);
            //剩下的时间里走的距离
            distance = new BigDecimal(mainingIn).multiply(speed, new MathContext(3));
        }
        //最后一圈还剩多少距离
        BigDecimal unfinished = (new BigDecimal(lenKu * 2)).subtract(distance);
        //如果剩下时间里走的距离超过了一圈的距离，为负数,往前加一圈，以此类推，剩下的再算
        if (unfinished.compareTo(BigDecimal.ZERO) == -1) {
            unfinished = new BigDecimal(0);
        }
        etion.setUnfinishedDistance(unfinished.toString());
        etion.setTotalDistance(String.valueOf(len.add(distance)));
        //计算代谢当量 (4.928 + 0.023 *距离) / 3.5
        BigDecimal b3 = dealMetabEquivalent(len.add(distance));
        etion.setMetabEquivalent(String.valueOf(b3));
        //计算心肺等级
        String cardiopuLevel = dealCardiopuLevel(len.add(distance));
        etion.setCardiopuLevel(cardiopuLevel);
        return etion;
    }


    /**
     * 计算代谢当量 (4.928 + 0.023 *距离) / 3.5
     *
     * @param total
     * @return
     */
    public BigDecimal dealMetabEquivalent(BigDecimal total) {
        BigDecimal b1 = new BigDecimal("0.023").multiply(total);
        BigDecimal b2 = b1.add(new BigDecimal("4.928"));
        BigDecimal b3 = b2.divide(new BigDecimal("3.5"), 1, RoundingMode.HALF_UP);
        return b3;
    }

    /**
     * 心肺功能等级
     * 等级标准：
     * 1级：<=299.9m
     * 2级：300~375m
     * 3级：375.1~450m
     * 4级：>=450.1m
     */
    public String dealCardiopuLevel(BigDecimal total) {
        if (total.compareTo(new BigDecimal("299.9")) < 1) {
            return "I";
        } else if (total.compareTo(new BigDecimal("300")) > -1 &&
                total.compareTo(new BigDecimal("374.9")) < 1) {
            return "II";
        } else if (total.compareTo(new BigDecimal("375")) > -1 &&
                total.compareTo(new BigDecimal("449.9")) < 1) {
            return "III";
        } else if (total.compareTo(new BigDecimal("450")) > -1) {
            return "IV";
        }
        return "";
    }

    /**
     * 心肺程度,1=重度，2=中度，3=轻度
     * 重度：<=149.9m
     * 中度：150~450m
     * 轻度：>=450.1m
     *
     * @param total
     * @return
     */
    public String dealCardiopuDegree(BigDecimal total) {
        if (total.compareTo(new BigDecimal("149.9")) < 1) {
            return "重度。";
        } else if (total.compareTo(new BigDecimal("150")) > -1 &&
                total.compareTo(new BigDecimal("450")) < 1) {
            return "中度。";
        } else if (total.compareTo(new BigDecimal("450.1")) > -1) {
            return "轻度。";
        }
        return "";
    }

    public Integer dealDegree(BigDecimal total) {
        if (total.compareTo(new BigDecimal("149.9")) < 1) {
            return 1;
        } else if (total.compareTo(new BigDecimal("150")) > -1 &&
                total.compareTo(new BigDecimal("450")) < 1) {
            return 2;
        } else if (total.compareTo(new BigDecimal("450.1")) > -1) {
            return 3;
        }
        return 0;
    }

    /**
     * 计算六分钟平均步速
     * 分为试验自动结束和提前停止
     * @param totalDistance
     * @param stopOr
     * @param stopTime
     * @return
     */
    public BigDecimal dealStrideAvg(BigDecimal totalDistance, Integer stopOr, String stopTime) {
        BigDecimal strideAvg = new BigDecimal("0.0");
        //自动完成了
        Integer timeInt = 0;
        if (stopOr == 0) {
            timeInt = 360;
        } else if (stopOr == 1) {
            //截取试验时间
            String[] strings = stopTime.split("分");
            if (strings.length == 2) {
                Integer minInt = Integer.parseInt(strings[0]);
                Integer secInt = Integer.valueOf(strings[1].substring(0, strings[1].length() - 1));
                timeInt = minInt * 60 + secInt;
            }
        }
        if (totalDistance.compareTo(new BigDecimal(0)) == 1 && timeInt > 0) {
            strideAvg = totalDistance.divide(new BigDecimal(timeInt), 3, BigDecimal.ROUND_HALF_UP);
            strideAvg = strideAvg.multiply(new BigDecimal(60)).setScale(1, BigDecimal.ROUND_HALF_UP);
        }
        return strideAvg;
    }

    /**
     * 验证是否为偶数
     *
     * @param num
     * @return
     */
    public static boolean checkNumEven(String num) {
        String str = "^\\d*[02468]$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(num);
        boolean b = m.matches();
        return b;
    }
}
