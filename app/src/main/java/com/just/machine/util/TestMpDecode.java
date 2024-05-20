package com.just.machine.util;

import android.util.Log;

import com.mlnx.mlnxdevicelibrary.service.ecg_analysis.EcgAnalysis;
import com.mlnx.mptp.ResponseCode;
import com.mlnx.mptp.exception.InvalidPacketException;
import com.mlnx.mptp.model.ecg.ECGChannelType;
import com.mlnx.mptp.model.ecg.ECGDeviceRunMode;
import com.mlnx.mptp.mptp.MpPacket;
import com.mlnx.mptp.mptp.VersionManager;
import com.mlnx.mptp.mptp.body.Body;
import com.mlnx.mptp.mptp.head.Header;
import com.mlnx.mptp.mptp.head.QoS;
import com.mlnx.mptp.utils.ByteUtils;
import com.mlnx.pojo.device.DeviceType;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * @ClassName: TestMpDecode
 * @Description: 处理心电算法工具类
 * @author: chenjianhua
 * @Copyright: ©2020 南京知心健 版权所有
 * @version: 1.0
 */
public class TestMpDecode {

    enum State {
        HEAD, VERSION, LEN, CONTANT
    }

    private State state = State.HEAD;
    private Integer hreatTYpe = 0;
    private int matchHeadIndex;
    private int length;
    private String head = "A831";
    private String deviceId = "";
    private EcgAnalysis ecgAnalysis;
    private Integer fileType = 0;
    private Integer xinlvType = 1;
    private Map<Long, byte[]> mapFile = new TreeMap<>();
    //private List<byte[]> byteList = new ArrayList<>();
    private Long ecgTime = null;

    private List<Long> bigTimeList = new ArrayList<>();
    private List<byte[]> bigHeartEcg = new ArrayList<>();

    private List<Long> smallTimeList = new ArrayList<>();
    private List<byte[]> smallHeartEcg = new ArrayList<>();

    private Long hreatTime = null;
    private ECGChannelType ecgChannelType = ECGChannelType.CHAN_3;
    private ECGChannelType ECGChannelTypeJar;
    private Integer checkInit = 1;
    private Integer ecgPanel = 2;
    private Map<Long, Integer> heartAllData = new TreeMap<>();
    private String rrStrs = "";
    //试验状态，0=初始，1=试验界面且还未开始计时，2=其他，3=计时刚结束
    private Integer goTestType = 0;
    //静息血氧集合
    private String xyBloods = "";
    //静息心率集合
    private String ecgHreats = "";
    //运动心率恢复值集合
    private String ecgHreatsTh = "";


    /**
     * 处理心电数据
     *
     * @param mapXinlv
     * @param byteBuffer
     * @throws Exception
     */
    public void decode(Map<Long, String> mapXinlv, ByteBuffer byteBuffer) throws Exception {
        switch (state) {
            case HEAD:
                if (matchHead(byteBuffer)) {
                    state = State.VERSION;
                } else {
                    break;
                }
            case VERSION:
                if (byteBuffer.hasRemaining()) {
                    byte code = byteBuffer.get();
                    if (VersionManager.isSupport(code)) {
                        state = State.LEN;
                    } else {
                        int mainCode = (code >> 4) & 0x0000000f;
                        int subCode = code & 0x0f;

                        Log.e("TestMpDecode",String
                                .format("不支持的协议版本%d.%d", mainCode, subCode));
                        state = State.HEAD;
                        break;
                    }
                } else {
                    break;
                }
            case LEN:
                length = getPacketLength(byteBuffer);
                if (length > Header.MaxLength) {
                    Log.w("TestMpDecode",String.format("数据包长度过长  实际包长度为%d", length));
                    state = State.HEAD;
                    break;
                } else if (length >= 0) {
                    state = State.CONTANT;
                } else {
                    break;
                }
            case CONTANT:
                if (byteBuffer.remaining() >= length) {
                    byte[] bytes = new byte[length];
                    byteBuffer.get(bytes);
                    ByteBuffer bf = ByteBuffer.allocate(length);
                    bf.flip();
                    ByteBuffUtils.addBytes(bf, bytes);
                    byte[] emptyFileData = null;
                    // boolean checkEcg = true;
                    try {
                        MpPacket mpPacket = new MpPacket();
                        mpPacket.init();
                        try {
                            mpPacket.decode(bf);
                        } catch (InvalidPacketException e) {
                            //System.out.println("丢包了");
//                            emptyFileData = realEcgPanel.getEmptyFilterData();
                        }
                        //当前时间戳
                        ecgTime = System.currentTimeMillis();
                        //向设备发送注册包等操作
                        register(mpPacket, mapXinlv);
                        if (null == emptyFileData) {
                            //得到完整的加密心电数据
                            byte[] realutEcgData = mpPacket.getBody().getEcgBody().getEcgData().getEncrySuccessionData();
//                            if (null != realutEcgData) {
//                                if (checkInit == 1) {
//                                    ecgAnalysis.init();
//                                    ecgAnalysis.setBpmCalcLead((byte) 0);//切换心率通道
//                                    ecgAnalysis.setAnalysisCallback(new AnalysisCallback() {
//                                        @Override
//                                        public void renewHeart(String deviceId, int heart) {
//                                            if (0 != heart) {
//                                                if (goTestType == 1) {
//                                                    ecgHreats = ecgHreats + heart + ",";
//                                                }
//                                                if (goTestType == 3) {
//                                                    ecgHreatsTh = ecgHreatsTh + heart + ",";
//                                                }
//                                                if (xinlvType == 1) {
//                                                    //系统时间戳
//                                                    Long time = System.currentTimeMillis();
//                                                    while (true) {
//                                                        if (mapXinlv.containsKey(time)) {
//                                                            time = time + 1;
//                                                        } else {
//                                                            mapXinlv.put(time, String.valueOf(heart));
//                                                            break;
//                                                        }
//                                                    }
//                                                    if (fileType == 1) {
//                                                        //将心率和心电数据的时间戳对应起来
//                                                        Long ecgTimeLo = ecgTime;
//                                                        while (true) {
//                                                            if (heartAllData.containsKey(ecgTimeLo)) {
//                                                                ecgTimeLo = ecgTimeLo + 1;
//                                                            } else {
//                                                                heartAllData.put(ecgTimeLo, heart);
//                                                                break;
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
////                        System.out.println("心率：" + heart);
//                                        }
//
//                                        @Override
//                                        public void renewSts(String deviceId, int[] sts) {
//                                            //System.out.println("ST：" + JSON.toJSONString(sts));
//                                        }
//
//                                        @Override
//                                        public void renewHeartResult(String deviceId,
//                                                                     HeartResult heartResult) {
////                                        if (xinlvType == 1) {
////                                            if (null != heartResult && heartResult.getCode() != 0 && heartResult.getCode() != 20 && heartResult.getCode() != 22) {
////                                                if (hreatTYpe == 0) {
////                                                    new Thread(new Runnable() {
////                                                        @Override
////                                                        public void run() {
////                                                            SwitchSwing.customLogAuto("心律异常：" + heartResult.getTitle(), 2);
////                                                            hreatTYpe = 1;
////                                                            try {
////                                                                Thread.sleep(5000);
////                                                            } catch (InterruptedException e) {
////                                                            }
////                                                            hreatTYpe = 0;
////                                                        }
////                                                    }).start();
////                                                }
////                                                if(fileType == 1){
////                                                    hreatTime = ecgTime;
////                                                }
////                                            }
////                                        }
//
//                                            // System.out.println("心律异常：" + JSON.toJSONString(heartResult));
//                                        }
//                                    });
//                                    checkInit = 0;
//                                }
//
//                                RealEcgAnalysisResult realEcgResult = ecgAnalysis.realAnalysis(ECGChannelTypeJar, realutEcgData, deviceId);
//                                if (fileType == 1) {
//                                    //将rr值缓存起来
//                                    int rr = realEcgResult.getRr();
//                                    if (rr > 300 && rr <= 2000) {
//                                        rrStrs += String.valueOf(rr) + ",";
//                                    }
//                                    //将心电数据先缓存起来
//                                    Long time = ecgTime;
//                                    if (mapFile.containsKey(time)) {
//                                        while (true) {
//                                            if (mapFile.containsKey(time)) {
//                                                time = time + 1;
//                                            } else {
//                                                mapFile.put(time, realEcgResult.getFilterEcgData());
//                                                break;
//                                            }
//                                        }
//                                    } else {
//                                        mapFile.put(time, realEcgResult.getFilterEcgData());
//                                    }
//                                    //采集开始1-8秒的心电波形
//                                    if (bigTimeList.size() < 16 && jLabelMin.getText().equals("5") && jLabelSec1.getText().equals("5")) {
//                                        String secStr = jLabelSec1.getText() + jLabelSec2.getText();
//                                        Integer SecInt = Integer.valueOf(secStr);
//                                        if (SecInt >= 50) {
//                                            bigTimeList.add(time);
//                                            bigHeartEcg.add(realEcgResult.getFilterEcgData());
//                                        }
////                                        System.out.println("bigTimeList=="+bigTimeList.size());
//                                    }
//                                    //采集结束352-360秒的心电波形
//                                    if (smallTimeList.size() < 16 && jLabelMin.getText().equals("0") && jLabelSec1.getText().equals("0")) {
//                                        String secStr = jLabelSec1.getText() + jLabelSec2.getText();
//                                        Integer SecInt = Integer.valueOf(secStr);
//                                        if (SecInt <= 9) {
//                                            smallTimeList.add(time);
//                                            smallHeartEcg.add(realEcgResult.getFilterEcgData());
//                                        }
////                                        System.out.println("smallTimeList=="+smallTimeList.size());
//                                    }
//                                    //byteList.add(filteData);
//                                }
////                                threadEcgPanel.getRealEcgPanel().inputEcgData(realEcgResult.getFilterEcgData());
//                                if (ecgPanel != 0) {
//                                    ecgPanel = 1;
//                                }
//                            }
//                            else {
//                                checkEcg = false;
//                            }
//                        } else {
//                            checkEcg = false;
//                        }
//                        if (!checkEcg) {
//                            if (null == emptyFileData) {
//                                emptyFileData = realEcgPanel.getEmptyFilterData();
//                            }
//                            if (fileType == 1) {
//                                //将心电数据先缓存起来
//                                if(mapFile.containsKey(ecgTime)){
//                                    Long time = ecgTime;
//                                    while (true){
//                                        if(mapFile.containsKey(time)){
//                                            time = ecgTime+1;
//                                        }else{
//                                            mapFile.put(time, emptyFileData);
//                                            break;
//                                        }
//                                    }
//                                } else {
//                                    mapFile.put(ecgTime, emptyFileData);
//                                }
//                            }
//                            realEcgPanel.inputEcgData(emptyFileData);
//                            if (ecgPanel != 0) {
//                                ecgPanel = 1;
//                            }
                        }
                        if (ecgPanel == 1) {
                            //绘制心电线程
//                            threadEcgPanel.start();
                            ecgPanel = 0;
                        }
                    } catch (InvalidPacketException e) {
                        e.printStackTrace();
                    } finally {
                        state = State.HEAD;
                    }
                }
                break;
            default:
        }
    }

    /**
     * 心电算法
     *
     * @param buf
     * @return
     */
    private boolean matchHead(ByteBuffer buf) {
        while (buf.hasRemaining()) {
            if (matchHeadIndex == Header.Heads.length) {
                matchHeadIndex = 0;
                return true;
            }
            byte b = buf.get();
            if (b == Header.Heads[matchHeadIndex]) {
                matchHeadIndex++;
            } else if (b == Header.Heads[0]) {
                matchHeadIndex = 1;
            } else {
                matchHeadIndex = 0;
            }
        }
        return false;
    }

    /**
     * 获取心电包长度
     *
     * @param buf
     * @return
     */
    private int getPacketLength(ByteBuffer buf) {
        if (buf.remaining() > Header.LengthByteSize) {
            byte[] dst = new byte[Header.LengthByteSize];
            buf.get(dst);
            int length = ByteUtils.bytesToInt(dst, Header.LengthByteSize);
            return length;
        } else {
            return -1;
        }
    }

    /**
     * 向设备发送注册包等操作
     *
     * @param mpPacket
     */
    public void register(MpPacket mpPacket, Map<Long, String> mapXinlv) {
        Body body = mpPacket.getBody();
        switch (mpPacket.getHeader().getPacketType()) {
            case REGISTER:
//                if (body.getMcuId() != null) {
////                    ecgAnalysis = new EcgAnalysisIml(deviceId, body.getMcuId());
//                    ecgAnalysis = new EcgAnalysis();
//                    ecgAnalysis.setGpu8AcId(body.getMcuId());
//                }
//                ecgAnalysis.init();
//                ecgAnalysis.setBpmCalcLead((byte) 0);//切换心率通道
//                ecgAnalysis.setAnalysisCallback(new AnalysisCallback() {
//                    @Override
//                    public void renewHeart(String deviceId, int heart) {
//                        if (0 != heart) {
//                            if (goTestType == 1) {
//                                ecgHreats = ecgHreats + heart + ",";
//                            }
//                            if (goTestType == 3) {
//                                ecgHreatsTh = ecgHreatsTh + heart + ",";
//                            }
//                            if (xinlvType == 1) {
//                                //系统时间戳
//                                Long time = System.currentTimeMillis();
//                                while (true) {
//                                    if (mapXinlv.containsKey(time)) {
//                                        time = time + 1;
//                                    } else {
//                                        mapXinlv.put(time, String.valueOf(heart));
//                                        break;
//                                    }
//                                }
//                                if (fileType == 1) {
//                                    //将心率和心电数据的时间戳对应起来
//                                    Long ecgTimeLo = ecgTime;
//                                    while (true) {
//                                        if (heartAllData.containsKey(ecgTimeLo)) {
//                                            ecgTimeLo = ecgTimeLo + 1;
//                                        } else {
//                                            heartAllData.put(ecgTimeLo, heart);
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//                        }
////                        System.out.println("心率：" + heart);
//                    }
//
//                    @Override
//                    public void renewSts(String deviceId, int[] sts) {
//                        //System.out.println("ST：" + JSON.toJSONString(sts));
//                    }
//
//                    @Override
//                    public void renewHeartResult(String deviceId,
//                                                 HeartResult heartResult) {
//                    }
//                });
                checkInit = 0;
                // 注册包 需要向设备回复注册回复的包
                mpPacket.registerAck(ecgChannelType,
                        ECGDeviceRunMode.DIAGNOSIS_MODE, DeviceType.SERVER,
                        1, ResponseCode.SUCESS);
                byte[] bytess = mpPacket.encode();
                //CRC校验
                if (null != bytess) {
                    //发送
                    bytess = CRC16Util.checkCrcTo(bytess, head);
                    //System.out.println("1发送给设备的数据为：" + CRC16Util.bytesToHexString(bytess));
//                    SerialPortUtil.sendData(serialPort, bytess);
                    USBTransferUtil.getInstance().write(bytess);
                }
                break;
            case PUBLISH:
                // push包里面有心电数据， 向设备回复PUBLISH_ACK 包
                if (QoS.LEAST_ONE.equals(mpPacket.getHeader().getQoS())) {
                    byte[] bytes = mpPacket.pushAck(DeviceType.SERVER, mpPacket.getBody().getMessageId(), ResponseCode.SUCESS).encode();
                    //发送
                    //byte[] bytes = mpPacket1.encode();
                    if (null != bytes) {
                        bytes = CRC16Util.checkCrcTo(bytes, head);
                        //System.out.println("2发送给设备的数据为：" + CRC16Util.bytesToHexString(bytes));
//                        SerialPortUtil.sendData(serialPort, bytes);
                        USBTransferUtil.getInstance().write(bytes);
                    }
                }
                break;
            case PUBLISH_ACK:
                break;
            case PINGREQ:
                // ping包 向设备回复 PINGRESP
                mpPacket = new MpPacket().pong(DeviceType.SERVER);
                //发送
                byte[] bytes = mpPacket.encode();
                if (null != bytes) {
                    bytes = CRC16Util.checkCrcTo(bytes, head);
                    // System.out.println("3发送给设备的数据为：" + CRC16Util.bytesToHexString(bytes));
                    USBTransferUtil.getInstance().write(bytes);
                }
                break;
            case PINGRESP:
                break;
            default:
        }
    }
}
