package com.just.machine.ui.fragment.serial

import com.common.network.LogUtils
import com.just.machine.model.LungTestData
import com.just.machine.util.BaseUtil
import java.util.zip.CRC32
import kotlin.experimental.and

object MudbusProtocol {

    private var tag: String = MudbusProtocol::class.java.name


    // 包头和包尾
    const val PACKET_HEADER: Short = 0xAAAA.toShort()
    const val PACKET_FOOTER: Byte = 0xED.toByte()

    // 上位机握手命令数据格式
    val HANDSHAKE_COMMAND: ByteArray = byteArrayOf(
        (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
        0x06, // 数据长度
        0x01, // 功能码
        0x00, 0x00, 0x00, 0x00, // 占位的 CRC 校验码
        PACKET_FOOTER // 包尾
    )

    // 主控板握手应答命令数据格式
    val HANDSHAKE_RESPONSE: ByteArray = byteArrayOf(
        (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
        0x06, // 数据长度
        0x99.toByte(), // 功能码
        0x00, 0x00, 0x00, 0x00, // 占位的 CRC32 校验码
        PACKET_FOOTER // 包尾
    )

    // 上位机设备状态信息读取命令数据格式
    val DEVICE_STATUS_COMMAND: ByteArray = byteArrayOf(
        (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
        0x06, // 数据长度
        0x02, // 功能码
        0x00, 0x00, 0x00, 0x00, // 占位的 CRC32 校验码
        PACKET_FOOTER // 包尾
    )

    fun isHandshakeResponseValid(response: ByteArray): Boolean {
        // 检查包头和包尾
        if (response.size != 9 || response[0] != PACKET_HEADER.toByte() || response[1] != PACKET_HEADER.toByte() ||
            response[8] != PACKET_FOOTER
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
            PACKET_FOOTER // 包尾
        )
    }


    // 上位机环境定标命令数据格式
    val ENVIRONMENT_CALIBRATION_COMMAND: ByteArray = byteArrayOf(
        (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
        0x06, // 数据长度
        0x03, // 功能码
        0x00, 0x00, 0x00, 0x00, // 占位的 CRC32 校验码
        PACKET_FOOTER // 包尾
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
    data class EnvironmentData(val temperature: Float, val humidity: Float, val pressure: Int)

    fun parseEnvironmentData(response: ByteArray): EnvironmentData? {
        // 检查数据长度是否符合规范
        if (response.size != 17) {
            // 数据长度不正确，返回空
            LogUtils.e(tag + "环境定标数据长度不正确")
            return null
        }

        // 检查包头和包尾
        if (response[0] != 0xAA.toByte() || response[1] != 0xAA.toByte() || response[16] != 0xED.toByte()) {
            // 包头或包尾不正确，返回空
            LogUtils.e(tag + "环境定标包头或包尾不正确")
            return null
        }

        // 解析温度数据
        val temperature = ((response[4].toInt() shl 8) or response[5].toInt()).toFloat() / 100

        // 解析湿度数据
        val humidity = ((response[6].toInt() shl 8) or response[7].toInt()).toFloat() / 100

        // 解析大气压力数据
        val pressure = (response[8].toInt() shl 24) or (response[9].toInt() shl 16) or
                (response[10].toInt() shl 8) or response[11].toInt()

        // 计算 CRC 校验码
        val crcValue =
            crc32ToByteArray(
                calculateCRC32(response.sliceArray(3 until 12))
            )

        // 提取接收到的 CRC 校验码
        val receivedCRC = response.sliceArray(12 until 16)

        if (!crcValue.contentEquals(receivedCRC)) {
            // CRC 校验失败，返回空
            LogUtils.e(tag + "CRC 校验失败==" + crcValue)
            return null
        }

        // 返回解析后的环境数据
        return EnvironmentData(temperature, humidity, pressure)
    }


    // 上位机流量定标命令数据格式
    val FLOW_CALIBRATION_COMMAND: ByteArray = byteArrayOf(
        (PACKET_HEADER.toInt() ushr 8).toByte(), PACKET_HEADER.toByte(), // 包头
        0x06, // 数据长度
        0x0A, // 功能码
        0x00, 0x00, 0x00, 0x00, // 占位的 CRC32 校验码
        PACKET_FOOTER // 包尾
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
        PACKET_FOOTER // 包尾
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
            (lungTestData.gasFlowSpeedSensorData shr 8).toByte(), // 分析气体流速高字节
            lungTestData.gasFlowSpeedSensorData.toByte(), // 分析气体流速低字节
            (lungTestData.gasPressureSensorData shr 8).toByte(), // 分析气体压力高字节
            lungTestData.gasPressureSensorData.toByte(), // 分析气体压力低字节
            (lungTestData.bloodOxygen shr 8).toByte(), // 血氧高字节
            lungTestData.bloodOxygen.toByte(), // 血氧低字节
            (lungTestData.temperature shr 8).toByte(), // 温度数据高字节
            lungTestData.temperature.toByte(), // 温度数据低字节
            (lungTestData.batteryLevel shr 8).toByte(), // 电量数据高字节
            lungTestData.batteryLevel.toByte() // 电量数据低字节
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
    fun parseLungTestData(response: ByteArray): LungTestData? {
        // 检查数据长度是否正确
        if (response.size != 35) {
            LogUtils.e("动态肺测试返回数据长度不正确")
            return null
        }

        // 检查包头和包尾
        if (response[0] != 0xAA.toByte() || response[1] != 0xAA.toByte() || response[34] != 0xED.toByte()) {
            LogUtils.e("动态肺测试返回包头或包尾不正确")
            return null
        }

        // 解析数据
        val temperature =
            (response[4].toInt() and 0xFF shl 8) or (response[5].toInt() and 0xFF)  // 温度数据
        val humidity =
            (response[6].toInt() and 0xFF shl 8) or (response[7].toInt() and 0xFF)  // 湿度数据
        val atmosphericPressure =
            ((response[8].toLong() and 0xFF shl 24) or
                    (response[9].toLong() and 0xFF shl 16) or
                    (response[10].toLong() and 0xFF shl 8) or
                    (response[11].toLong() and 0xFF)).toFloat() / 100.0f  // 大气压力数据
        val highRangeFlowSensorData =
            (response[12].toInt() and 0xFF shl 8) or (response[13].toInt() and 0xFF)  // 高量程流量传感器数据
        val lowRangeFlowSensorData =
            (response[14].toInt() and 0xFF shl 8) or (response[15].toInt() and 0xFF)  // 低量程流量传感器数据
        val co2SensorData =
            (response[16].toInt() and 0xFF shl 8) or (response[17].toInt() and 0xFF)  // CO2传感器数据
        val o2SensorData =
            (response[18].toInt() and 0xFF shl 8) or (response[19].toInt() and 0xFF)  // O2传感器数据
        val gasFlowSpeedSensorData =
            (response[20].toInt() and 0xFF shl 8) or (response[21].toInt() and 0xFF)  // 分析气体流速传感器数据
        val gasPressureSensorData =
            (response[22].toInt() and 0xFF shl 8) or (response[23].toInt() and 0xFF)  // 分析气体压力传感器数据
        val bloodOxygen =
            (response[24].toInt() and 0xFF shl 8) or (response[25].toInt() and 0xFF)  // 血氧数据
        val batteryTemperature =
            (response[26].toInt() and 0xFF shl 8) or (response[27].toInt() and 0xFF)  // 温度数据
        val batteryLevel =
            (response[28].toInt() and 0xFF shl 8) or (response[29].toInt() and 0xFF)  // 电池数据

        // 解析 CRC 校验码
        val crcValue = response.sliceArray(30 until 34)
        val calculatedCRC = crc32ToByteArray(calculateCRC32(response.sliceArray(4 until 30)))

        // 验证 CRC 校验码
//        if (!crcValue.contentEquals(calculatedCRC)) {
//            LogUtils.e(
//                "动态肺测试CRC校验失败----" + crcValue.joinToString(" ") { "%02X".format(it) } + "!=calculatedCRC----"
//                        + calculatedCRC.joinToString(" ") { "%02X".format(it) }
//            )
//            return null
//        }

        // 返回解析后的数据
        return LungTestData(
            0x07,
            temperature,
            humidity,
            atmosphericPressure,
            highRangeFlowSensorData,
            lowRangeFlowSensorData,
            co2SensorData,
            o2SensorData,
            gasFlowSpeedSensorData,
            gasPressureSensorData,
            bloodOxygen,
            batteryLevel
        )
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
}


