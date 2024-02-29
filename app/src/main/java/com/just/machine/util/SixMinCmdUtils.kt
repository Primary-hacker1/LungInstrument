package com.just.machine.util

class SixMinCmdUtils {

    companion object {

        /**
         * 下发蓝牙设备mac地址
         */
        fun dealBluetooth(ecgStr: String, pressureStr: String, oxygenStr: String) {
            val head = "A804"
            val bluetoothStr = ecgStr + oxygenStr + pressureStr
            val byteBluetooth = CRC16Util.hexStringToBytes(bluetoothStr)
            val checkCrcToCmd = CRC16Util.checkCrcTo(byteBluetooth, head)
            USBTransferUtil.getInstance().write(checkCrcToCmd)
        }

        /**
         * 开始测量血压指令
         */
        fun measureBloodPressure() {
            val bytes = byteArrayOf(
                0xA8.toByte(),
                0x11.toByte(),
                0x01.toByte(),
                0x81.toByte(),
                0xB1.toByte(),
                0xB1.toByte()
            )
            USBTransferUtil.getInstance().write(bytes)
        }

        /**
         * 测量血压失败指令
         */
        fun failMeasureBloodPressure() {
            val bytes = byteArrayOf(
                0xA8.toByte(),
                0x11.toByte(),
                0x01.toByte(),
                0x80.toByte(),
                0x70.toByte(),
                0x71.toByte()
            )
            //System.out.println("发送给血压设备的启动指令为：" + CRC16Util.bytesToHexString(bytes));
            USBTransferUtil.getInstance().write(bytes)
        }

        /**
         * 测量血压重置指令
         */
        fun resetMeasureBloodPressure() {
            //发送测量血压指令A8 11 01 82 F1 B0
            val bytes = byteArrayOf(
                0xA8.toByte(),
                0x11.toByte(),
                0x01.toByte(),
                0x82.toByte(),
                0xF1.toByte(),
                0xB0.toByte()
            )
            //System.out.println("发送给血压设备的启动指令为：" + CRC16Util.bytesToHexString(bytes));
            USBTransferUtil.getInstance().write(bytes)
        }

        /**
         * 开启计圈和记步设备
         */
        fun openQSAndBS() {
            val bytes = byteArrayOf(
                0xA8.toByte(),
                0x03.toByte(),
                0x01.toByte(),
                0x81.toByte(),
                0x11.toByte(),
                0xB4.toByte()
            )
            //System.out.println("开始计圈和记步设备指令为：" + CRC16Util.bytesToHexString(bytes));
            USBTransferUtil.getInstance().write(bytes)
        }

        /**
         * 关闭计圈和记步设备
         */
        fun closeQSAndBS() {
            val bytes = byteArrayOf(
                0xA8.toByte(),
                0x03.toByte(),
                0x01.toByte(),
                0x80.toByte(),
                0xD0.toByte(),
                0x74.toByte()
            )
            //System.out.println("关闭计圈和记步设备指令为：" + CRC16Util.bytesToHexString(bytes));
            USBTransferUtil.getInstance().write(bytes)
        }
    }
}