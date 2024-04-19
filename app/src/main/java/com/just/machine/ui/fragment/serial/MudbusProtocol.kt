package com.just.machine.ui.fragment.serial

import com.common.network.LogUtils
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
        val length = byteArrayOf(14.toByte()) // 不包括包头和包尾的数据长度
        val funcCode = byteArrayOf(0x03.toByte()) // 功能码为返回环境定标数据
        val tempBytes = temperature.toBytes() // 温度数据转换为字节
        val humidityBytes = humidity.toBytes() // 湿度数据转换为字节
        val pressureBytes = pressure.toBytes() // 大气压力数据转换为字节

        val crc =
            crc32ToByteArray(
                calculateCRC32(length + funcCode + tempBytes + humidityBytes + pressureBytes) // 计算CRC校验码
            )

        val packet =
            header + length + funcCode + tempBytes + humidityBytes + pressureBytes + crc + 0xED.toByte() // 构建完整数据包

        return packet
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
                calculateCRC32(response.sliceArray(2 until 12))
            )

        // 提取接收到的 CRC 校验码
        val receivedCRC = response.sliceArray(12 until 16)

        // 验证 CRC 校验码
        if (!crcValue.contentEquals(receivedCRC)) {
            // CRC 校验失败，返回空
            return null
        }

        // 返回解析后的环境数据
        return EnvironmentData(temperature, humidity, pressure)
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
        result[0] = (crcValue shr 24).toByte()
        result[1] = (crcValue shr 16).toByte()
        result[2] = (crcValue shr 8).toByte()
        result[3] = crcValue.toByte()
        return result
    }

}


