package com.just.machine.ui.fragment.serial

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.common.network.LogUtils
import com.just.machine.model.Constants
import com.common.base.BaseUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import world.shanya.serialport.SerialPort
import world.shanya.serialport.SerialPortBuilder

object SerialPortManager {
    private val tag = SerialPortManager::class.java.name

    @SuppressLint("StaticFieldLeak")
    var serialPort: SerialPort? = null

    private var isStart: Boolean = false

    fun initialize(context: Context) {
        val stringBuilder = StringBuilder()
        serialPort =
            SerialPortBuilder
                .setReceivedDataCallback {
                    MainScope().launch {
                        stringBuilder.append(it)
                        val byteArray = stringBuilder.toString().toByteArray()

                        isStart = ModbusProtocol.isHandshakeResponseValid(byteArray)//握手指令ture成功

                        if (isStart) {//握手成功后的操作
//                            LiveDataBus.get().with(Constants.serialCallback).value = byteArray
                        }
                    }
                }
                .isDebug(Constants.isDebug)
                .setConnectionStatusCallback { status, bluetoothDevice ->
                    MainScope().launch {
                        if (status) {
                            if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.BLUETOOTH_CONNECT
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return@launch
                            }

                            LogUtils.d(
                                tag + "设备名称:\t${bluetoothDevice?.name}\n" +
                                        "设备地址:\t${bluetoothDevice?.address}\n" +
                                        "设备类型:\t${bluetoothDevice?.type}"
                            )
                        } else {
                            LogUtils.d(tag + "设备:暂无链接设备")
                        }
                    }
                }
                .build(context)

        serialPort?.connectDevice("B8:C3:85:93:29:FD")//连接设备

//        serialPort?.openDiscoveryActivity()
    }

    fun sendMessage(message: ByteArray) {
        LogUtils.e(
            BaseUtil.generateLogEntryWithRectangleBorder(
                "发送串口延时消息：" + BaseUtil.bytes2HexStr(message)
            )
        )
        serialPort?.let { SendMsg(message.toString(), it) }
        //serialPort?.sendData(message)
    }

    fun cleanSerial() {//关闭串口
        serialPort?.disconnect() //关闭蓝牙串口
        isStart = false
    }
}
