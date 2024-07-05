package com.just.machine.ui.fragment.serial

import com.common.network.LogUtils
import com.just.machine.model.LungTestData
import com.common.base.BaseUtil
import com.just.machine.model.Constants
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.lungdata.BreathState
import com.just.machine.model.lungdata.CPXCalcule
import com.just.machine.model.lungdata.CPXSerializeData
import com.just.machine.model.lungdata.DyCalculeSerializeCore
import com.just.machine.util.CRC16Util
import com.just.machine.util.LiveDataBus
import com.justsafe.libview.util.DateUtils
import java.util.zip.CRC32
import kotlin.experimental.and

object ModbusProtocol {

    private var tag: String = ModbusProtocol::class.java.name

    var isFlowCalibra = false
    var isIngredientCalibra = false
    var isEnvironmentCalibra = false
    var isDeviceConnect = false
    var isWarmup = false
    var batteryLevel = 0
    var warmLeaveSec  = 0
    var hardWareVersion = ""
    var softWareVersion = ""


    // 包头和包尾
    const val PACKET_HEADER: Short = 0x55AA.toShort()

    /**
     * 功能复位
     */
    val reset: ByteArray = byteArrayOf(0x55, 0xAA.toByte(), 0x03, 0x01, 0x01, 0x41, 0x90.toByte())

    /**
     * 读取下位机版本信息
     */
    val readVersion: ByteArray = byteArrayOf(0x55, 0xAA.toByte(), 0x01, 0x00, 0x00, 0x20)

    /**
     * 读取下位机设备信息
     */
    val readDevice: ByteArray = byteArrayOf(0x55, 0xAA.toByte(), 0x02, 0x00, 0x00, 0xD0.toByte())

    /**
     * 允许一类传感器上传(环境温湿度与大气压)
     */
    val allowOneSensor: ByteArray = byteArrayOf(
        0x55, 0xAA.toByte(), 0x11, 0x02, 0x07, 0x00, 0xA6.toByte(), 0xE8.toByte()
    )

    /**
     * 禁止一类传感器上传
     */
    val banOneSensor: ByteArray = byteArrayOf(
        0x55, 0xAA.toByte(), 0x11, 0x02, 0x00, 0x00, 0xA4.toByte(), 0xD8.toByte()
    )

    /**
     * 允许二类传感器上传(上传所有数据)
     */
    val allowTwoSensor: ByteArray = byteArrayOf(
        0x55, 0xAA.toByte(), 0x12, 0x02, 0xFF.toByte(), 0xFF.toByte(), 0xA5.toByte(), 0x2C
    )

    /**
     * 禁止二类传感器上传
     */
    val banTwoSensor: ByteArray = byteArrayOf(
        0x55, 0xAA.toByte(), 0x12, 0x02, 0x00, 0x00, 0xA4.toByte(), 0x9C.toByte()
    )

    /**
     * 设置电磁阀成分定标流程
     */
    val setIngredientSolenoidValve: ByteArray = byteArrayOf(
        0x55, 0xAA.toByte(), 0x22, 0x02, 0x03, 0x03, 0xEB.toByte(), 0x6D
    )

    /**
     * 关闭电磁阀
     */
    val closeSolenoidValve: ByteArray = byteArrayOf(
        0x55, 0xAA.toByte(), 0x22, 0x02, 0x02, 0x02, 0x2B, 0x3D
    )

    /**
     * 开电磁阀1
     */
    val openSolenoid1: ByteArray = byteArrayOf(
        0x55, 0xAA.toByte(), 0x22, 0x02, 0x01, 0x02, 0x2B, 0xCD.toByte()
    )

    /**
     * 开电磁阀2
     */
    val openSolenoid2: ByteArray = byteArrayOf(
        0x55, 0xAA.toByte(), 0x22, 0x02, 0x02, 0x01, 0x6B, 0x3C
    )

    /**
     * 设置风机高流速(600)
     */
    val setBlowerHigh: ByteArray = byteArrayOf(
        0x55, 0xAA.toByte(), 0x32, 0x03, 0x02, 0x02, 0x58,
        0xBC.toByte(), 0xDA.toByte()
    )

    /**
     * 设置风机低流速(100)
     */
    val setBlowerLow: ByteArray = byteArrayOf(
        0x55, 0xAA.toByte(), 0x32, 0x03, 0x02, 0x00, 0x64, 0xBD.toByte(), 0xAB.toByte()
    )

    /**
     * 设置风机自动流量定标流程
     */
    val setAutoFlowBlow: ByteArray = byteArrayOf(
        0x55, 0xAA.toByte(), 0x32, 0x03, 0x04, 0x00, 0x00, 0x5C, 0x41
    )

    /**
     * @param cmd 发送到指令 *
     * @param dataLength 数据长度
     */
    fun cmdSend(cmd: String, dataLength: String = "00"): ByteArray {
        val head = "55AA"
        val data = cmd + dataLength
        val crc = CRC16Util.getCRC16(data)
        val cmdHex = head + data + crc
        return CRC16Util.hexStringToBytes(cmdHex)
    }

    /**
     * @param data 发送数据到串口的方法，参数可以是要发送的数据。
     */
    fun receiveSerialData(data: ByteArray) {
        if (data.size < 3) {
            return
        }
        val hexString = String.format("0x%02X", data[2].toInt() and 0xFF)
//        LogUtils.d(tag + "功能码：" + hexString)

        when (data[2].toInt() and 0xFF) { // data[2] 是功能码
            0x01 -> { // 读取下位机版本信息
                // 处理逻辑
                parseVersionInfo(data)
            }

            0x02 -> { // 读取下位机设备信息
                // 处理逻辑
                parseDeviceInfo(data)
            }

            0x03 -> { // 控制下位机设备
                // 处理逻辑
            }

            0x11 -> { // 允许/禁止上传一类传感器数据
                // 处理逻辑
            }

            0x12 -> { // 允许/禁止上传二类传感器数据
                // 处理逻辑
            }

            0x21 -> { // 读取电磁阀信息
                // 处理逻辑
            }

            0x22 -> { // 控制电磁阀
                // 处理逻辑
            }

            0x31 -> { // 读取风机信息
                // 处理逻辑
            }

            0x32 -> { // 控制风机
                // 处理逻辑
            }

            0x81 -> { // 下位机启动完成通知
                // 处理逻辑
            }

            0x82 -> { // 设备心跳帧
                // 处理逻辑
            }

            0x91 -> { // 上传一类传感器数据
                // 处理逻辑
                parseEnvironmentData(data)
            }

            0x92 -> { // 上传二类传感器数据
                // 检查数据长度是否正确
                if (data.size != 26) {
                    return
                }
                parseLungTestData(data)
            }

            else -> { // 处理未知功能码或其他情况

            }
        }
    }

    /**
     * 解析版本信息
     */
    private fun parseVersionInfo(response: ByteArray) {
        // 检查数据长度是否正确
        if (response.size != 12) {
            LogUtils.e("主控板返回数据长度不正确")
            return
        }

        // 检查包头和包尾
        if (response[0] != 0x55.toByte()) {
            LogUtils.e("环境定标返回包头或包尾不正确")
            return
        }

        // 解析 CRC 校验码
        val crcValue = response.sliceArray(10 until 12)
        val calculatedCRC = CRC16Util.getCRC16Bytes(response.sliceArray(2 until 10))
        // 验证 CRC 校验码
        if (!crcValue.contentEquals(calculatedCRC)) {
            LogUtils.e(
                "获取版本信息CRC校验失败----" + crcValue.joinToString(" ") { "%02X".format(it) } + "!=calculatedCRC----"
                        + calculatedCRC.joinToString(" ") { "%02X".format(it) }
            )
            return
        }

        val bytes2Hex = CRC16Util.bytes2Hex(response)
        hardWareVersion = formatVersion(bytes2Hex.substring(8,12))
        softWareVersion = formatVersion(bytes2Hex.substring(12,16))

        LogUtils.e(
            "版本信息数据 ----hardWareVersion----$hardWareVersion----softWareVersion----$softWareVersion"
        )
    }

    // 解析设备信息数据
    data class DeviceInfoData(val batterLevel: Int, val warmLeaveSec: Int, val connectStatus: Boolean)

    /**
     * 解析设备信息
     */
    private fun parseDeviceInfo(response: ByteArray) {
        // 检查数据长度是否正确
        if (response.size != 11) {
            LogUtils.e("主控板返回数据长度不正确")
            return
        }

        // 检查包头和包尾
        if (response[0] != 0x55.toByte()) {
            LogUtils.e("环境定标返回包头或包尾不正确")
            return
        }

        // 解析 CRC 校验码
        val crcValue = response.sliceArray(9 until 11)
        val calculatedCRC = CRC16Util.getCRC16Bytes(response.sliceArray(2 until 9))

        // 验证 CRC 校验码
        if (!crcValue.contentEquals(calculatedCRC)) {
            LogUtils.e(
                "获取设备信息CRC校验失败----" + crcValue.joinToString(" ") { "%02X".format(it) } + "!=calculatedCRC----"
                        + calculatedCRC.joinToString(" ") { "%02X".format(it) }
            )
            return
        }

        // 解析数据
        val bytes2Hex = CRC16Util.bytes2Hex(response)
        batteryLevel = response[5].toInt() and 0xFF //电量信息
        if(batteryLevel == 255){
            batteryLevel = 100
        }
        val heatSecHex1 = bytes2Hex.substring(12, 14)
        val heatSecHex2 = bytes2Hex.substring(14, 16)
        val heatSexHex = heatSecHex2 +heatSecHex1
        warmLeaveSec = heatSexHex.toInt(16) //热机时间
        isDeviceConnect = true

//        LogUtils.e(
//            "设备信息数据 ----batteryLevel----$batteryLevel----warmLeaveSec----$warmLeaveSec----isDeviceConnect----$isDeviceConnect"
//        )

//        val deviceInfoData = DeviceInfoData(batteryLevel,warmLeaveSec, isConnect)
//
//        LiveDataBus.get().with(Constants.getDevInfoSerialCallback).postValue(deviceInfoData)
    }

    // 上位机设备状态信息读取命令数据格式
    val DEVICE_STATUS_COMMAND: ByteArray = byteArrayOf(
        (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
        0x06, // 数据长度
        0x02, // 功能码
        0x00, 0x00, 0x00, 0x00, // 占位的 CRC32 校验码
    )

    fun isHandshakeResponseValid(response: ByteArray): Boolean {
        // 检查包头和包尾
        if (response.size != 9 || response[0] != PACKET_HEADER.toByte() || response[1] != PACKET_HEADER.toByte()
        ) {
            return false
        }
        // 检查功能码
        if (response[3] != 0x99.toByte()) {
            return false
        }
        // 检查CRC32校验码
        val calculatedCRC = calculateCRC32(response.sliceArray(2 until 8))
        val receivedCRC =
            (response[4].toInt() shl 24) or (response[5].toInt() shl 16) or
                    (response[6].toInt() shl 8) or response[7].toInt()
        return calculatedCRC == receivedCRC.toLong()
    }


    // 主控板设备状态信息数据格式
    fun createDeviceStatusResponse(preheatTime: Short, batteryInfo: Short): ByteArray {
        val crcValue = calculateCRC32(
            byteArrayOf(
                0x02,
                (preheatTime.toInt() ushr 8).toByte(),
                preheatTime.toByte(),
                (batteryInfo.toInt() ushr 8).toByte(),
                batteryInfo.toByte()
            )
        )
        return byteArrayOf(
            (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
            0x0A, // 数据长度
            0x02, // 功能码
            (preheatTime.toInt() ushr 8).toByte(), preheatTime.toByte(), // 预热时间
            (batteryInfo.toInt() ushr 8).toByte(), batteryInfo.toByte(), // 电量信息
            (crcValue.toInt() ushr 24).toByte(), (crcValue.toInt() ushr 16).toByte(), // CRC32校验码
            (crcValue.toInt() ushr 8).toByte(), crcValue.toByte(),
        )
    }


    // 上位机环境定标命令数据格式
    val ENVIRONMENT_CALIBRATION_COMMAND: ByteArray = byteArrayOf(
        (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
        0x06, // 数据长度
        0x03, // 功能码
        0x00, 0x00, 0x00, 0x00, // 占位的 CRC32 校验码
    )

    // 主控板环境定标数据格式
    fun generateSerialCommand(temperature: Short, humidity: Short, pressure: Int): ByteArray {
        // 构建数据包
        val header = byteArrayOf(0xAA.toByte(), 0xAA.toByte())
        val funcCode = byteArrayOf(0x03.toByte()) // 功能码为返回环境定标数据
        val tempBytes = temperature.toBytes() // 温度数据转换为字节
        val humidityBytes = humidity.toBytes() // 湿度数据转换为字节
        val pressureBytes = pressure.toBytes() // 大气压力数据转换为字节

        val dataBody = funcCode + tempBytes + humidityBytes + pressureBytes

        val crc =
            crc32ToByteArray(
                calculateCRC32(dataBody) // 计算CRC校验码
            )

        val length = byteArrayOf((dataBody.size + crc.size + 1).toByte())

        return header + length + dataBody + crc + 0xED.toByte()
    }


    // 解析环境定标数据
    data class EnvironmentData(val temperature: Float, val humidity: Float, val pressure: Float)

    private fun parseEnvironmentData(response: ByteArray) {

        // 检查数据长度是否正确
        if (response.size != 12) {
            LogUtils.e("主控板返回数据长度不正确")
            return
        }

        // 检查包头和包尾
        if (response[0] != 0x55.toByte()) {
            LogUtils.e("环境定标返回包头或包尾不正确")
            return
        }

        // 解析 CRC 校验码
        val crcValue = response.sliceArray(10 until 12)

        val calculatedCRC = CRC16Util.getCRC16Bytes(response.sliceArray(2 until 10))

        // 验证 CRC 校验码
        if (!crcValue.contentEquals(calculatedCRC)) {
            LogUtils.e(
                "环境定标CRC校验失败----" + crcValue.joinToString(" ") { "%02X".format(it) } + "!=calculatedCRC----"
                        + calculatedCRC.joinToString(" ") { "%02X".format(it) }
            )
            return
        }

        // 解析数据
        var temperature =
            ((response[4].toInt() and 0xFF) + (response[5].toInt() and 0xFF)) / 10f  // 环境温度数据
        if (temperature <= 0f || temperature > 500f) {
            temperature = 50f
        }

        var humidity =
            ((response[6].toInt() and 0xFF) + (response[7].toInt() and 0xFF)) / 10f  // 环境湿度数据
        if (humidity <= 0f || humidity > 100f) {
            humidity = 50f
        }

        var atmosphericPressure =
            ((response[8].toInt() and 0xFF) + (response[9].toInt() and 0xFF) * 256).toFloat() * 0.075f  // 大气压数据
        if (atmosphericPressure < 500f || atmosphericPressure > 1000f) {
            atmosphericPressure = 765f
        }

//        LogUtils.e(
//            "环境定标数据 ----temperature----$temperature----humidity----$humidity----atmosphericPressure----$atmosphericPressure"
//        )

        val environmentData = EnvironmentData(temperature, humidity, atmosphericPressure)

        LiveDataBus.get().with(Constants.serialCallback).postValue(environmentData)
    }

    // 上位机流量定标命令数据格式
    val FLOW_CALIBRATION_COMMAND: ByteArray = byteArrayOf(
        (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
        0x06, // 数据长度
        0x04, // 功能码
        0x00, 0x00, 0x00, 0x00, // 占位的 CRC32 校验码
    )

    // 上位机自动流量定标命令数据格式
    val FLOW_AUTO_CALIBRATION_COMMAND: ByteArray = byteArrayOf(
        (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
        0x06, // 数据长度
        0x10, // 功能码
        0x00, 0x00, 0x00, 0x00, // 占位的 CRC32 校验码
    )

    // 上位机停止命令数据格式
    val FLOW_STOP_COMMAND: ByteArray = byteArrayOf(
        (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
        0x06, // 数据长度
        0x08, // 功能码
        0x00, 0x00, 0x00, 0x00, // 占位的 CRC32 校验码
    )

    val EXIT_LOW_POWER_COMMAND: ByteArray = byteArrayOf(
        (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
        0x06, // 数据长度
        0x0B, // 功能码
        0x00, 0x00, 0x00, 0x00, // 占位的 CRC32 校验码
    )


    data class FlowCalibrationData(val smallRangeFlow: Int, val largeRangeFlow: Int)

    fun generateFlowCalibrationCommand(smallRangeFlow: Int, largeRangeFlow: Int): ByteArray {
        val header = byteArrayOf(0xAA.toByte(), 0xAA.toByte())
        val funcCode = byteArrayOf(0x04.toByte())  // 功能码

        // 流量数据转为字节
        val smallRangeBytes = smallRangeFlow.toBytesMin()
        val largeRangeBytes = largeRangeFlow.toBytesMin()

        // 准备数据包主体，用于计算 CRC
        val dataBody = funcCode + smallRangeBytes + largeRangeBytes

        // 计算 CRC 校验码
        val crc = crc32ToByteArray(calculateCRC32(dataBody))
        val tail = byteArrayOf(0xED.toByte())

        // 数据包长度（不包括包头和包尾）
        val length = byteArrayOf((dataBody.size + crc.size + 1).toByte())

        // 构建完整数据包
        return header + length + dataBody + crc + tail
    }


    /**
     * @param response 流量定标解析
     * */
    fun parseFlowCalibrationData(response: ByteArray): FlowCalibrationData? {
        // 检查数据长度是否正确
        if (response.size != 13) {
            LogUtils.e("流量手动定标数据长度不正确")
            return null
        }

        // 检查包头和包尾
        if (response[0] != 0xAA.toByte() || response[1] != 0xAA.toByte() || response[12] != 0xED.toByte()) {
            LogUtils.e("流量手动定标包头或包尾不正确")
            return null
        }

        // 检查功能码
        if (response[3] != 0x04.toByte()) {
            LogUtils.e("功能码不正确")
            return null
        }

        // 解析小量程流量数据
        val smallRangeFlow = (response[4].toInt() and 0xFF shl 8) or (response[5].toInt() and 0xFF)

        // 解析大量程流量数据
        val largeRangeFlow = (response[6].toInt() and 0xFF shl 8) or (response[7].toInt() and 0xFF)

        // 提取接收到的 CRC 校验码
        val crcValue = response.sliceArray(8 until 12)

        // 计算应有的 CRC 校验码（假设有 calculateCRC32 函数）
        val calculatedCRC = crc32ToByteArray(
            calculateCRC32(response.sliceArray(3 until 8))
        )

        // 验证 CRC 校验码
        if (!crcValue.contentEquals(calculatedCRC)) {
            LogUtils.e(
                tag + "CRC 校验失败==" + BaseUtil.bytes2HexStr(crcValue) + "!="
                        + BaseUtil.bytes2HexStr(calculatedCRC)
            )
            return null
        }

        return FlowCalibrationData(smallRangeFlow, largeRangeFlow)
    }

    // 上位机成分定标命令数据格式
    val INGREDIENT_CALIBRATION_COMMAND: ByteArray = byteArrayOf(
        (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
        0x06, // 数据长度
        0x05, // 功能码
        0x00, 0x00, 0x00, 0x00, // 占位的 CRC32 校验码
    )


    /**
     * 生成主控板返回数据
     * @param returnCommand 返回命令
     * @param highRangeFlowSensorData 大量程流量传感器数据
     * @param lowRangeFlowSensorData 小量程流量传感器数据
     * @param co2SensorData CO2传感器数据
     * @param o2SensorData O2传感器数据
     * @param gasFlowSpeedSensorData 分析气体流速传感器数据
     * @param gasPressureSensorData 分析气体压力传感器数据
     * @param temperature 温度数据
     * @param batteryLevel 电量数据
     * @return 生成的主控板返回数据
     */
    data class ControlBoardData(
        val returnCommand: Byte,
        val highRangeFlowSensorData: Int,
        val lowRangeFlowSensorData: Int,
        val co2SensorData: Int,
        val o2SensorData: Int,
        val gasFlowSpeedSensorData: Int,
        val gasPressureSensorData: Int,
        val temperature: Int,
        val batteryLevel: Int
    ) {
        override fun toString(): String {
            return "ControlBoardData(" +
                    "returnCommand=$returnCommand, " +
                    "highRangeFlowSensorData=$highRangeFlowSensorData, " +
                    "lowRangeFlowSensorData=$lowRangeFlowSensorData, " +
                    "co2SensorData=$co2SensorData, " +
                    "o2SensorData=$o2SensorData, " +
                    "gasFlowSpeedSensorData=$gasFlowSpeedSensorData, " +
                    "gasPressureSensorData=$gasPressureSensorData, " +
                    "temperature=$temperature, " +
                    "batteryLevel=$batteryLevel)"
        }
    }


    fun generateControlBoardResponse(
        bean: ControlBoardData
    ): ByteArray {
        val header = byteArrayOf(0xAA.toByte(), 0xAA.toByte()) // 包头
        val returnCommandByte = byteArrayOf(bean.returnCommand) // 返回命令

        // 将传感器数据转换为字节数组
        val highRangeFlowBytes = bean.highRangeFlowSensorData.toBytesMin()
        val lowRangeFlowBytes = bean.lowRangeFlowSensorData.toBytesMin()
        val co2Bytes = bean.co2SensorData.toBytesMin()
        val o2Bytes = bean.o2SensorData.toBytesMin()
        val gasFlowSpeedBytes = bean.gasFlowSpeedSensorData.toBytesMin()
        val gasPressureBytes = bean.gasPressureSensorData.toBytesMin()

        // 温度和电量数据转换为字节数组
        val temperatureBytes = bean.temperature.toBytesMin()
        val batteryLevelBytes = bean.batteryLevel.toBytesMin()

        // 构建数据包主体，用于计算 CRC
        val dataBody = returnCommandByte + highRangeFlowBytes + lowRangeFlowBytes +
                co2Bytes + o2Bytes + gasFlowSpeedBytes + gasPressureBytes +
                temperatureBytes + batteryLevelBytes

        // 计算 CRC 校验码
        val crc = crc32ToByteArray(calculateCRC32(dataBody))

        // 包尾
        val tail = byteArrayOf(0xED.toByte())

        val dataLength = byteArrayOf((dataBody.size + crc.size + 1).toByte()) // 数据长度

        // 构建完整数据包
        return header + dataLength + dataBody + crc + tail
    }

    /**
     * @param response 成分定标解析
     * */
    fun parseControlBoardResponse(response: ByteArray): ControlBoardData? {
        // 检查数据长度是否正确
        if (response.size != 25) {
            LogUtils.e("主控板返回数据长度不正确")
            return null
        }

        // 检查包头和包尾
        if (response[0] != 0xAA.toByte() || response[1] != 0xAA.toByte() || response[24] != 0xED.toByte()) {
            LogUtils.e("主控板返回包头或包尾不正确")
            return null
        }

        // 解析数据
        val returnCommand = response[3] // 返回命令

        // 解析传感器数据
        val highRangeFlowSensorData =
            (response[4].toInt() and 0xFF shl 8) or (response[5].toInt() and 0xFF) // 大量程流量传感器数据
        val lowRangeFlowSensorData =
            (response[6].toInt() and 0xFF shl 8) or (response[7].toInt() and 0xFF) // 小量程流量传感器数据
        val co2SensorData =
            (response[8].toInt() and 0xFF shl 8) or (response[9].toInt() and 0xFF) // CO2传感器数据
        val o2SensorData =
            (response[10].toInt() and 0xFF shl 8) or (response[11].toInt() and 0xFF) // O2传感器数据
        val gasFlowSpeedSensorData =
            (response[12].toInt() and 0xFF shl 8) or (response[13].toInt() and 0xFF) // 分析气体流速传感器数据
        val gasPressureSensorData =
            (response[14].toInt() and 0xFF shl 8) or (response[15].toInt() and 0xFF) // 分析气体压力传感器数据

        // 解析温度和电量数据
        val temperature =
            (response[16].toInt() and 0xFF shl 8) or (response[17].toInt() and 0xFF) // 温度数据
        val batteryLevel =
            (response[18].toInt() and 0xFF shl 8) or (response[19].toInt() and 0xFF) // 电量数据

        // 解析 CRC 校验码
        val crcValue = response.sliceArray(20 until 24)

        // 计算应有的 CRC 校验码（假设有 calculateCRC32 函数）
        val calculatedCRC = crc32ToByteArray(calculateCRC32(response.sliceArray(3 until 20)))

        // 验证 CRC 校验码
        if (!crcValue.contentEquals(calculatedCRC)) {
            LogUtils.e("CRC 校验失败")
            return null
        }

        // 返回解析后的数据
        return ControlBoardData(
            returnCommand,
            highRangeFlowSensorData,
            lowRangeFlowSensorData,
            co2SensorData,
            o2SensorData,
            gasFlowSpeedSensorData,
            gasPressureSensorData,
            temperature,
            batteryLevel
        )
    }

    /**
     * 生成动态肺数据。
     * @return 生成的动态肺测试数据的字节数组。
     */
//    fun generateLungTestData(lungTestData: LungTestData): ByteArray {
//        val header = byteArrayOf(0xAA.toByte(), 0xAA.toByte()) // 包头
//
//        // 计算 CRC 校验码（假设有 calculateCRC16 函数）
//        val dataBody = byteArrayOf(
//            lungTestData.returnCommand,
//            lungTestData.temperature.toByte(), // 温度
//            lungTestData.humidity.toByte(), // 湿度
//            (lungTestData.atmosphericPressure.toInt() shr 8).toByte(), // 大气压高字节
//            lungTestData.atmosphericPressure.toInt().toByte(), // 大气压低字节
//            lungTestData.highRangeFlowSensorData.toByte(), // 高范围流量传感器数据
//            lungTestData.lowRangeFlowSensorData.toByte(), // 低范围流量传感器数据
//            lungTestData.co2SensorData.toByte(), // 二氧化碳传感器数据
//            lungTestData.o2SensorData.toByte(), // 氧气传感器数据
//            lungTestData.gasFlowSpeedSensorData.toByte(), // 气体流速传感器数据
//            lungTestData.gasPressureSensorData.toByte(), // 气体压力传感器数据
//            (lungTestData.bloodOxygen shr 8).toByte(), // 血氧高字节
//            (lungTestData.bloodOxygen and 0xFF).toByte(), // 血氧低字节
//            (lungTestData.batteryLevel shr 8).toByte(), // 电池电量高字节
//            (lungTestData.batteryLevel and 0xFF).toByte() // 电池电量低字节
//        )
//
//        // 计算 CRC 校验码并转换为字节数组
//        val crc = crc32ToByteArray(calculateCRC32(dataBody))
//
//        val dataLength = byteArrayOf((dataBody.size + crc.size + 1).toByte()) // 数据长度
//
//        // 拼接数据包
//        val packet = header + dataLength + dataBody + crc + 0xED.toByte() // 包尾
//
//        return packet
//    }

    fun generateLungTestData(lungTestData: LungTestData): ByteArray {
        val header = byteArrayOf(0xAA.toByte(), 0xAA.toByte()) // 包头
//        val dataLength = byteArrayOf(0x1E) // 数据长度（固定为30字节）
        val returnCommand = byteArrayOf(0x07) // 返回命令（固定为0x07）

        // 大气压数据转换为4字节
        val atmosphericPressure = (lungTestData.atmosphericPressure * 100).toInt()
        val atmosphericPressureBytes = byteArrayOf(
            (atmosphericPressure shr 24).toByte(),
            (atmosphericPressure shr 16).toByte(),
            (atmosphericPressure shr 8).toByte(),
            atmosphericPressure.toByte()
        )

        val dataBody = byteArrayOf(
            0x07,//功能码
            lungTestData.temperature.toByte(), // 温度高字节
            lungTestData.temperature.toByte(), // 温度低字节
            lungTestData.humidity.toByte(), // 湿度高字节
            lungTestData.humidity.toByte(), // 湿度低字节
            atmosphericPressureBytes[0], // 大气压高字节
            atmosphericPressureBytes[1],
            atmosphericPressureBytes[2],
            atmosphericPressureBytes[3], // 大气压低字节
            (lungTestData.highRangeFlowSensorData shr 8).toByte(), // 高量程流量传感器高字节
            lungTestData.highRangeFlowSensorData.toByte(), // 高量程流量传感器低字节
            (lungTestData.lowRangeFlowSensorData shr 8).toByte(), // 低量程流量传感器高字节
            lungTestData.lowRangeFlowSensorData.toByte(), // 低量程流量传感器低字节
            (lungTestData.co2SensorData shr 8).toByte(), // CO2传感器数据高字节
            lungTestData.co2SensorData.toByte(), // CO2传感器数据低字节
            (lungTestData.o2SensorData shr 8).toByte(), // O2传感器数据高字节
            lungTestData.o2SensorData.toByte(), // O2传感器数据低字节
            (lungTestData.bloodOxygen?.shr(8))!!.toByte(), // 血氧高字节
            lungTestData.bloodOxygen.toByte(), // 血氧低字节
            (lungTestData.temperature shr 8).toByte(), // 温度数据高字节
            lungTestData.temperature.toByte(), // 温度数据低字节
//            (lungTestData.batteryLevel shr 8).toByte(), // 电量数据高字节
        )

        // 计算 CRC 校验码并转换为字节数组
        val crc = crc32ToByteArray(calculateCRC32(dataBody))
//        val packet =
//            header + dataLength + returnCommand + dataBody + crc + byteArrayOf(0xED.toByte()) // 包尾

        val dataLength = byteArrayOf((dataBody.size + crc.size + 1).toByte()) // 数据长度

        // 拼接数据包
        val packet = header + dataLength + dataBody + crc + 0xED.toByte() // 包尾

        return packet
    }


    // 解析动态肺测试数据
    private fun parseLungTestData(response: ByteArray) {

        // 检查数据长度是否正确
        if (response.size != 26) {
            LogUtils.e("主控板返回数据长度不正确")
            return
        }

        // 检查包头和包尾
        if (response[0] != 0x55.toByte() || response[1] != 0xAA.toByte()) {
            LogUtils.e("动态肺测试返回包头或包尾不正确")
            return
        }

        // 解析数据
        val temperature =
            (response[4].toInt() and 0xFF) or (response[5].toInt() and 0xFF)  // 环境温度数据

        val humidity =
            (response[6].toInt() and 0xFF) or (response[7].toInt() and 0xFF)  // 环境湿度数据

        var atmosphericPressure =
            ((response[8].toInt() and 0xFF) + (response[9].toInt() and 0xFF) * 256).toFloat()  // 大气压数据

        atmosphericPressure = (atmosphericPressure * 0.075).toFloat()

        if (atmosphericPressure < 500 || atmosphericPressure > 1000) {
            atmosphericPressure = 765f
        }

        val solenoidValve1 =
            (response[10].toInt() and 0xFF)  // 电磁阀1

        val solenoidValve2 =
            (response[11].toInt() and 0xFF)  // 电磁阀2

        val calibratedFlowRate =
            (response[12].toInt() and 0xFF) or (response[13].toInt() and 0xFF) // 定标流量

        val highRangeFlowSensorData =
            (response[14].toInt() and 0xFF) or (response[15].toInt() and 0xFF)  // 高量程流量传感器数据

        val lowRangeFlowSensorData =
            (response[16].toInt() and 0xFF) or (response[17].toInt() and 0xFF)  // 低量程流量传感器数据

        val co2SensorData =
            (response[18].toInt() and 0xFF) or (response[19].toInt() and 0xFF)  // CO2传感器数据

        val o2SensorData =
            (response[20].toInt() and 0xFF) or (response[21].toInt() and 0xFF)  // O2传感器数据

//        val bloodOxygen =
//            (response[24].toInt() and 0xFF shl 8) or (response[25].toInt() and 0xFF)  // 血氧数据

        val batteryTemperature =
            (response[22].toInt() and 0xFF) or (response[23].toInt() and 0xFF)  // O2温度数据

//        val batteryLevel =
//            (response[28].toInt() and 0xFF shl 8) or (response[29].toInt() and 0xFF)  // 电池数据

//        val str = String(response.sliceArray(3 until 24), Charsets.UTF_8)  // 使用指定的字符集（这里使用UTF-8）
//        val crcStirng = CRC16Util.getCRC16(str)
//        LogUtils.d(tag + crcStirng)

        // 解析 CRC 校验码
        val crcValue = response.sliceArray(24 until 26)

        val calculatedCRC = CRC16Util.getCRC16Bytes(response.sliceArray(2 until 24))

        // 验证 CRC 校验码
        if (!crcValue.contentEquals(calculatedCRC)) {
            LogUtils.e(
                "动态肺测试CRC校验失败----" + crcValue.joinToString(" ") { "%02X".format(it) } + "!=calculatedCRC----"
                        + calculatedCRC.joinToString(" ") { "%02X".format(it) }
            )
            return
        }

        val lungTestData = LungTestData(
            // 返回解析后的数据
            0x07,
            temperature,
            humidity,
            atmosphericPressure,
            highRangeFlowSensorData,
            lowRangeFlowSensorData,
            co2SensorData,
            o2SensorData,
        )

//        LogUtils.d(tag + lungTestData)

        val breathInData = CPXSerializeData().convertLungTestDataToCPXSerializeData(lungTestData)

        val dyCalculeSerializeCore = DyCalculeSerializeCore()

        dyCalculeSerializeCore.setBegin(BreathState.None)

        val cpxSerializeData = dyCalculeSerializeCore.enqueDyDataModel(breathInData)

        dyCalculeSerializeCore.caluculeData(dyCalculeSerializeCore.observeBreathModel)

        val cpxBreathInOutData = CPXCalcule.calDyBreathInOutData(
            cpxSerializeData,
            dyCalculeSerializeCore.cpxBreathInOutDataBase
        )

        val patientBean = SharedPreferencesUtils.instance.patientBean

        val id = patientBean?.patientId

        if (id != null) {
            cpxBreathInOutData.patientId = id
        }

        cpxBreathInOutData.createTime = DateUtils.nowMinutesDataString

//        viewModel.insertCPXBreathInOutData(cpxBreathInOutData) // 插入数据库

//        LogUtils.e(tag + cpxBreathInOutData.toString())
        LiveDataBus.get().with("动态心肺测试").postValue(cpxBreathInOutData)


    }


    // 计算CRC32校验码
    private fun calculateCRC32(data: ByteArray): Long {
        val crc32 = CRC32()
        crc32.update(data)
        return crc32.value
    }

    // 扩展函数：Short类型转换为字节数组（大端序）
    private fun Short.toBytes(): ByteArray {
        return byteArrayOf((this.toInt() shr 8).toByte(), (this and 0xFF).toByte())
    }

    private fun Int.toBytesMin(): ByteArray {//小端序
        return byteArrayOf(
            ((this shr 8) and 0xFF).toByte(),
            (this and 0xFF).toByte()
        )
    }

    // 扩展函数：Int类型转换为字节数组（大端序）
    private fun Int.toBytes(): ByteArray {
        return byteArrayOf(
            (this shr 24).toByte(),
            (this shr 16).toByte(),
            (this shr 8).toByte(),
            (this and 0xFF).toByte()
        )
    }

    // 扩展函数：Long类型转换为字节数组（大端序）
    fun Long.toBytes(): ByteArray {
        return byteArrayOf(
            (this shr 56).toByte(),
            (this shr 48).toByte(),
            (this shr 40).toByte(),
            (this shr 32).toByte(),
            (this shr 24).toByte(),
            (this shr 16).toByte(),
            (this shr 8).toByte(),
            (this and 0xFF).toByte()
        )
    }

    fun longToByteArray(value: Long): ByteArray {
        val result = ByteArray(8)
        for (i in 7 downTo 0) {
            result[i] = (value shr (8 * (7 - i))).toByte()
        }
        return result
    }


    fun crc32ToByteArray(crcValue: Long): ByteArray {
        val result = ByteArray(4)
        result[0] = (crcValue ushr 24).toByte()
        result[1] = (crcValue ushr 16).toByte()
        result[2] = (crcValue ushr 8).toByte()
        result[3] = crcValue.toByte()
        return result
    }


    /**
     * @return 获取硬件版本如v 1.0.1。
     */
    fun formatVersion(bitFieldHex: String): String {
        // Step 1: Swap first two and last two characters
        val swappedHex = bitFieldHex.takeLast(2) + bitFieldHex.dropLast(2)

        // Step 2: Convert swapped hex string to a Long
        val bitField = swappedHex.toLong(16)

        // Step 3: Extract fields from the bitField
        val versionType = if ((1.toLong() shl 15 and bitField) != 0L) "B" else "V"
        val versionNumber = (bitField.toInt() ushr 5) and 0x3FF // Extract bits 14-5
        val testNumber = bitField.toInt() and 0x1F // Extract bits 4-0

        // Step 4: Format the string according to the rules
        return if (versionType == "V") {
            val aa = versionNumber / 100
            val b = (versionNumber % 100) / 10
            val c = versionNumber % 10
            "V$aa.$b.$c"
        } else {
            val aa = versionNumber / 100
            val b = (versionNumber % 100) / 10
            val c = versionNumber % 10
            "B$aa.$b.${c}_$testNumber"
        }
    }
}


