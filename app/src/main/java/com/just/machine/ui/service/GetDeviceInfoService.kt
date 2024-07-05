package com.just.machine.ui.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.just.machine.ui.dialog.LungCommonDialogFragment
import com.just.machine.ui.fragment.serial.ModbusProtocol
import com.just.machine.util.USBTransferUtil
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

class GetDeviceInfoService: Service(){

    private var timer = Timer()
    private val usbTransferUtil: USBTransferUtil by lazy {
        USBTransferUtil.getInstance()
    }

    override fun onCreate() {
        super.onCreate()
        usbTransferUtil.init(this)
        usbTransferUtil.connect()
        timer = fixedRateTimer("", false, 0, 1000) {
            usbTransferUtil.write(ModbusProtocol.readDevice)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}