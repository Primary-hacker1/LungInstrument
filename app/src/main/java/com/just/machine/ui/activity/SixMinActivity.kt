package com.just.machine.ui.activity

import android.graphics.Color
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.lifecycle.Observer
import com.common.base.CommonBaseActivity
import com.just.machine.util.CRC16Util
import com.just.machine.util.LiveDataBus
import com.just.machine.util.SixMinCmdUtils
import com.just.machine.util.USBTransferUtil
import com.just.machine.util.USBTransferUtil.OnUSBDateReceive
import com.just.news.databinding.ActivitySixMinBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class SixMinActivity : CommonBaseActivity<ActivitySixMinBinding>(), TextToSpeech.OnInitListener {

    private lateinit var usbTransferUtil: USBTransferUtil
    private lateinit var textToSpeech: TextToSpeech

    override fun initView() {
        binding.llDevicesStatus.setBackgroundColor(Color.rgb(109, 188, 246))
        binding.llPatientInfo.setBackgroundColor(Color.rgb(109, 188, 246))
        usbTransferUtil = USBTransferUtil.getInstance()
        usbTransferUtil.init(this)
        textToSpeech = TextToSpeech(applicationContext, this)
        usbTransferUtil.setOnUSBDateReceive(OnUSBDateReceive {
            runOnUiThread {

            }
        })
        usbTransferUtil.connect()
        LiveDataBus.get().with("111").observe(this, Observer {
//            Toast.makeText(this@SixMinActivity,it.toString(),Toast.LENGTH_SHORT).show()
//            val bytes = CRC16Util.hexStringToBytes(it.toString())
//            if (bytes[13] != 0x00.toByte() && bytes[14] != 0x00.toByte()) {
//                val b = byteArrayOf(bytes[13])
//                val ss = Integer.valueOf(CRC16Util.bytesToHexString(b), 16).toString()
//                if (bytes[13] !== 0xFF.toByte() && bytes[14] !== 0xFF.toByte()) {
//                    val c = byteArrayOf(bytes[14])
//                    val sz = Integer.valueOf(CRC16Util.bytesToHexString(c), 16).toString()
//                    val str = "您当前的收缩压为，$ss,舒张压为，$sz"
//                    Toast.makeText(this@SixMinActivity, str, Toast.LENGTH_SHORT).show()
//                }
//            }
        })

        binding.btnMeasureBlood.setOnClickListener {
            SixMinCmdUtils.measureBloodPressure()
        }

        binding.btnSpeechText.setOnClickListener {
            textToSpeech.speak(
                "试验即将结束，当提示时间到的时候，您不要突然停下来，而是放慢速度继续向前走。",
                TextToSpeech.QUEUE_ADD,
                null,
                null
            )
        }
    }

    override fun getViewBinding() = ActivitySixMinBinding.inflate(layoutInflater)

    override fun onDestroy() {
        super.onDestroy()
        usbTransferUtil.disconnect()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    override fun onInit(status: Int) {
        //判断是否转化成功
        if (status == TextToSpeech.SUCCESS) {
            //设置语言为中文
            textToSpeech.language = Locale.CHINA
            //设置音调,值越大声音越尖（女生），值越小则变成男声,1.0是常规
            textToSpeech.setPitch(1.0f);
            //设置语速
            textToSpeech.setSpeechRate(0.5f);
            //在onInIt方法里直接调用tts的播报功能
        }
    }
}