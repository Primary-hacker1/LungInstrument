package com.just.machine.ui.activity

import android.hardware.usb.UsbManager
import com.common.base.CommonBaseActivity
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.just.machine.util.USBTransferUtil
import com.just.machine.util.USBTransferUtil.OnUSBDateReceive
import com.just.news.databinding.ActivitySixMinBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SixMinActivity  : CommonBaseActivity<ActivitySixMinBinding>() {

    private lateinit var usbTransferUtil: USBTransferUtil

    override fun initView() {
        usbTransferUtil = USBTransferUtil.getInstance()
        usbTransferUtil.init(this)

        usbTransferUtil.setOnUSBDateReceive(OnUSBDateReceive {
            runOnUiThread {

            }
        })
        usbTransferUtil.connect()
    }

    override fun getViewBinding() = ActivitySixMinBinding.inflate(layoutInflater)
}