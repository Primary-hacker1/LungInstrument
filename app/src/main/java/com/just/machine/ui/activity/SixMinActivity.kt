package com.just.machine.ui.activity

import android.graphics.Color
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.lifecycle.Observer
import com.common.base.CommonBaseActivity
import com.common.base.setNoRepeatListener
import com.google.gson.Gson
import com.just.machine.model.UsbSerialData
import com.just.machine.util.FixCountDownTime
import com.just.machine.util.LiveDataBus
import com.just.machine.util.SixMinCmdUtils
import com.just.machine.util.USBTransferUtil
import com.just.machine.util.USBTransferUtil.OnUSBDateReceive
import com.just.news.R
import com.just.news.databinding.ActivitySixMinBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class SixMinActivity : CommonBaseActivity<ActivitySixMinBinding>(), TextToSpeech.OnInitListener {

    private lateinit var usbTransferUtil: USBTransferUtil
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var mCountDownTime: FixCountDownTime
    private var isBegin = false

    override fun initView() {
        binding.llDevicesStatus.setBackgroundColor(Color.rgb(109, 188, 246))
        binding.llPatientInfo.setBackgroundColor(Color.rgb(109, 188, 246))
        initCountDownTimerExt()
        usbTransferUtil = USBTransferUtil.getInstance()
        usbTransferUtil.init(this)
        textToSpeech = TextToSpeech(applicationContext, this)
        usbTransferUtil.setOnUSBDateReceive(OnUSBDateReceive {
            runOnUiThread {

            }
        })
        usbTransferUtil.connect()
        if (!USBTransferUtil.isConnectUSB) {
            binding.ivEcg.setImageResource(R.mipmap.xinlvno)
            binding.ivBloodPressure.setImageResource(R.mipmap.xueyangno)
            binding.ivBloodOxygen.setImageResource(R.mipmap.xueyangno)
        }
        LiveDataBus.get().with("111").observe(this, Observer {
//            Toast.makeText(this@SixMinActivity,it.toString(),Toast.LENGTH_SHORT).show()
//            val bytes = CRC16Util.hexStringToBytes(it.toString())
            try {
                val usbSerialData = Gson().fromJson(it.toString(), UsbSerialData::class.java)
                if (usbSerialData.ecg == "心电已连接") {
                    binding.ivEcg.setImageResource(R.mipmap.xinlvyes)
                } else {
                    binding.ivEcg.setImageResource(R.mipmap.xinlvno)
                }
                if (usbSerialData.bloodPressure == "血压已连接") {
                    binding.ivBloodPressure.setImageResource(R.mipmap.xueyayes)
                } else {
                    binding.ivBloodPressure.setImageResource(R.mipmap.xueyangno)
                }
                if (usbSerialData.bloodOxy == "血氧已连接") {
                    binding.ivBloodOxygen.setImageResource(R.mipmap.xueyangyes)
                } else {
                    binding.ivBloodOxygen.setImageResource(R.mipmap.xueyangno)
                }
                when (usbSerialData.batteryLevel) {
                    1 -> {
                        binding.ivBatteryStatus.setImageResource(R.mipmap.dianchi1)
                    }

                    2 -> {
                        binding.ivBatteryStatus.setImageResource(R.mipmap.dianchi2)
                    }

                    3 -> {
                        binding.ivBatteryStatus.setImageResource(R.mipmap.dianchi3)
                    }

                    4 -> {
                        binding.ivBatteryStatus.setImageResource(R.mipmap.dianchi4)
                    }

                    5 -> {
                        binding.ivBatteryStatus.setImageResource(R.mipmap.dianchi)
                    }

                    else -> {
                        binding.ivBatteryStatus.setImageResource(R.mipmap.dianchi00)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        binding.rlMeasureBlood.setNoRepeatListener {
            if (binding.tvMeasureBlood.text == getString(R.string.measure_blood)) {
                usbTransferUtil.bloodState = 1
                binding.tvMeasureBlood.text = getString(R.string.measuring_blood)
                SixMinCmdUtils.measureBloodPressure()
            }
        }

        binding.rlStart.setNoRepeatListener {
            isBegin = true
            mCountDownTime.start(object : FixCountDownTime.OnTimerCallBack {
                override fun onStart() {

                }

                override fun onTick(times: Int) {
                    val minute = times / 60 % 60
                    val second = times % 60
                    binding.tvStartMin.text = minute.toString()
                    binding.tvStartSec1.text = second.toString().substring(0, 1)
                    binding.tvStartSec2.text = second.toString().substring(1)
//                    Toast.makeText(this@SixMinActivity, "$times===", Toast.LENGTH_SHORT).show()
                }

                override fun onFinish() {

                }
            })
        }

        binding.ivClose.setNoRepeatListener {
            if (isBegin) {

            } else {
                finish()
            }
        }

        binding.btnSpeechText.setNoRepeatListener {
            textToSpeech.speak(
                "试验即将结束，当提示时间到的时候，您不要突然停下来，而是放慢速度继续向前走。",
                TextToSpeech.QUEUE_ADD,
                null,
                null
            )
        }
    }

    private fun initCountDownTimerExt() {
        mCountDownTime = object : FixCountDownTime(360, 1000) {}
    }

    override fun getViewBinding() = ActivitySixMinBinding.inflate(layoutInflater)

    override fun onInit(status: Int) {
        //判断是否转化成功
        if (status == TextToSpeech.SUCCESS) {
            //设置语言为中文
            textToSpeech.language = Locale.CHINA
            //设置音调,值越大声音越尖（女生），值越小则变成男声,1.0是常规
            textToSpeech.setPitch(0.6f)
            //设置语速
            textToSpeech.setSpeechRate(0.2f)
            //在onInIt方法里直接调用tts的播报功能
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        usbTransferUtil.disconnect()
        textToSpeech.stop()
        textToSpeech.shutdown()
        mCountDownTime.cancel()
    }

}