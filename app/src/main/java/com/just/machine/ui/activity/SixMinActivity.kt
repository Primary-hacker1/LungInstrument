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
        binding.sixminLlDevicesStatus.setBackgroundColor(Color.rgb(109, 188, 246))
        binding.sixminLlPatientInfo.setBackgroundColor(Color.rgb(109, 188, 246))
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
            binding.sixminIvEcg.setImageResource(R.mipmap.xinlvno)
            binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyangno)
            binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangno)
        }
        LiveDataBus.get().with("111").observe(this, Observer {
            try {
                val usbSerialData = Gson().fromJson(it.toString(), UsbSerialData::class.java)
                if (usbSerialData.ecgState == "心电已连接") {
                    binding.sixminIvEcg.setImageResource(R.mipmap.xinlvyes)
                } else {
                    binding.sixminIvEcg.setImageResource(R.mipmap.xinlvno)
                }
                if (usbSerialData.bloodPressureState == "血压已连接") {
                    binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyayes)
                } else {
                    binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyangno)
                }
                if (usbSerialData.bloodOxyState == "血氧已连接") {
                    binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangyes)
                } else {
                    binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangno)
                }
                when (usbSerialData.batteryLevel) {
                    1 -> {
                        binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi1)
                    }

                    2 -> {
                        binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi2)
                    }

                    3 -> {
                        binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi3)
                    }

                    4 -> {
                        binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi4)
                    }

                    5 -> {
                        binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi)
                    }

                    else -> {
                        binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi00)
                    }
                }
                when (usbSerialData.bloodState) {
                    "未测量血压" -> {
                        binding.sixminTvMeasureBlood.text = getString(R.string.sixmin_measure_blood)
                        binding.sixminTvBloodPressureHigh.text = "- - -"
                        binding.sixminTvBloodPressureLow.text = "- - -"
                    }

                    "测量血压中" -> {
                        binding.sixminTvMeasureBlood.text =
                            getString(R.string.sixmin_measuring_blood)
                        binding.sixminTvBloodPressureHigh.text = usbSerialData.bloodHigh ?: "- - -"
                        binding.sixminTvBloodPressureLow.text = usbSerialData.bloodLow ?: "- - -"
                    }

                    "测量血压失败" -> {
                        binding.sixminTvMeasureBlood.text = getString(R.string.sixmin_measure_blood)
                        binding.sixminTvBloodPressureHigh.text = "- - -"
                        binding.sixminTvBloodPressureLow.text = "- - -"
                    }

                    "测量血压成功" -> {
                        binding.sixminTvMeasureBlood.text = getString(R.string.sixmin_measure_blood)
                        binding.sixminTvBloodPressureHighFront.text = usbSerialData.bloodHighFront ?: "- - -"
                        binding.sixminTvBloodPressureLowFront.text = usbSerialData.bloodLowFront ?: "- - -"
                        binding.sixminTvBloodPressureHighBehind.text = usbSerialData.bloodHighBehind ?: "- - -"
                        binding.sixminTvBloodPressureLowBehind.text = usbSerialData.bloodLowBehind ?: "- - -"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        binding.sixminRlMeasureBlood.setNoRepeatListener {
            if (binding.sixminTvMeasureBlood.text == getString(R.string.sixmin_measure_blood)) {
                SixMinCmdUtils.measureBloodPressure()
            } else {
                Toast.makeText(this, "正在测量血压中...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.sixminRlStart.setNoRepeatListener {
            if (!isBegin) {
                binding.sixminTvStart.text = "停止"
                mCountDownTime.start(object : FixCountDownTime.OnTimerCallBack {
                    override fun onStart() {

                    }

                    override fun onTick(times: Int) {
                        val minute = times / 60 % 60
                        val second = times % 60
                        binding.sixminTvStartMin.text = minute.toString()
                        binding.sixminTvStartSec1.text = second.toString().substring(0, 1)
                        binding.sixminTvStartSec2.text = second.toString().substring(1)
//                    Toast.makeText(this@SixMinActivity, "$times===", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFinish() {
                        binding.sixminTvStartMin.text = "5"
                        binding.sixminTvStartSec1.text = "5"
                        binding.sixminTvStartSec2.text = "9"
                    }
                })
            } else {
                binding.sixminTvStart.text = "开始"
                binding.sixminTvStartMin.text = "5"
                binding.sixminTvStartSec1.text = "5"
                binding.sixminTvStartSec2.text = "9"
                mCountDownTime.cancel()
                mCountDownTime.setmTimes(360)
            }
            isBegin = !isBegin
        }

        binding.sixminIvClose.setNoRepeatListener {
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