package com.just.machine.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.common.base.CommonBaseActivity
import com.common.base.setNoRepeatListener
import com.google.gson.Gson
import com.just.machine.model.UsbSerialData
import com.just.machine.util.FileUtil
import com.just.machine.util.FixCountDownTime
import com.just.machine.util.LiveDataBus
import com.just.machine.util.SixMinCmdUtils
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.ActivitySixMinBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileInputStream
import java.util.Locale


@AndroidEntryPoint
class SixMinActivity : CommonBaseActivity<ActivitySixMinBinding>(), TextToSpeech.OnInitListener {

    private lateinit var usbTransferUtil: USBTransferUtil
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var mCountDownTime: FixCountDownTime
    private lateinit var exitTestDialog: AlertDialog

    override fun initView() {
        binding.sixminLlDevicesStatus.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))
        binding.sixminLlPatientInfo.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))
        initCountDownTimerExt()
        initExitAlertDialog()
        copyAssetsFilesToSD()
        usbTransferUtil = USBTransferUtil.getInstance()
        usbTransferUtil.init(this)
        textToSpeech = TextToSpeech(applicationContext, this)
        usbTransferUtil.connect()
        usbTransferUtil.setOnUSBDateReceive {
            runOnUiThread {
                if (it.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                    //usb设备拔出
                    binding.sixminIvEcg.setImageResource(R.mipmap.xinlvno)
                    binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyangno)
                    binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangno)
                    binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi00)
                }
            }
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
                        binding.sixminTvBloodPressureHigh.text = usbSerialData.bloodHigh
                        binding.sixminTvBloodPressureLow.text = usbSerialData.bloodLow
                        binding.sixminTvBloodPressureHighFront.text =
                            usbSerialData.bloodHighFront ?: "- - -"
                        binding.sixminTvBloodPressureLowFront.text =
                            usbSerialData.bloodLowFront ?: "- - -"
                        binding.sixminTvBloodPressureHighBehind.text =
                            usbSerialData.bloodHighBehind ?: "- - -"
                        binding.sixminTvBloodPressureLowBehind.text =
                            usbSerialData.bloodLowBehind ?: "- - -"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        binding.sixminRlMeasureBlood.setNoRepeatListener {
            if (USBTransferUtil.isConnectUSB) {
                if (binding.sixminTvMeasureBlood.text == getString(R.string.sixmin_measure_blood)) {
                    SixMinCmdUtils.measureBloodPressure()
                } else {
                    Toast.makeText(this, "正在测量血压中...", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "请先接入设备", Toast.LENGTH_SHORT).show()
            }
        }

        binding.sixminRlStart.setNoRepeatListener {
            if (USBTransferUtil.isConnectUSB) {
                if (!usbTransferUtil.isBegin) {
                    binding.sixminTvStart.text = "停止"
                    mCountDownTime.start(object : FixCountDownTime.OnTimerCallBack {
                        override fun onStart() {

                        }

                        override fun onTick(times: Int) {
                            try {
                                val minute = times / 60 % 60
                                val second = times % 60
                                binding.sixminTvStartMin.text = minute.toString()
                                binding.sixminTvStartSec1.text = second.toString().substring(0, 1)
                                binding.sixminTvStartSec2.text = second.toString().substring(1)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onFinish() {
                            binding.sixminTvStartMin.text = "0"
                            binding.sixminTvStartSec1.text = "0"
                            binding.sixminTvStartSec2.text = "0"
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
                usbTransferUtil.isBegin = !usbTransferUtil.isBegin
            } else {
                Toast.makeText(this, "请先接入设备", Toast.LENGTH_SHORT).show()
            }
        }

        binding.sixminIvClose.setNoRepeatListener {
            if (usbTransferUtil.isBegin) {
//                Toast.makeText(this@SixMinActivity, "正在试验中...", Toast.LENGTH_SHORT).show()
                if (!exitTestDialog.isShowing) {
                    exitTestDialog.show()
                }
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

        binding.sixminIvSystemSetting.setOnClickListener {
            val intent = Intent(this, SixMinSystemSettingActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 将assets文件复制到内存卡
     */
    private fun copyAssetsFilesToSD() {
        try {
            val stringNames = assets.list("templates")
            var srcFolderSize: Long = 0
            stringNames?.forEach { name ->
                val length = assets.open("templates/$name").available()
                srcFolderSize += length
            }
            val dstFolderSize = FileUtil.getInstance(this)
                .getFolderSize(getExternalFilesDir("")?.absolutePath + File.separator + "templates")
            if (srcFolderSize != dstFolderSize) {
                val file = File(getExternalFilesDir("templates"), "报告模板3页-无截图.xml")
                val fi = FileInputStream(file)
                fi.available()
                FileUtil.getInstance(this).copyAssetsToSD("templates", "templates")
                    .setFileOperateCallback(object : FileUtil.FileOperateCallback {
                        override fun onSuccess() {
                            Toast.makeText(this@SixMinActivity, "复制成功", Toast.LENGTH_SHORT)
                                .show()
                        }

                        override fun onFailed(error: String?) {
                            Toast.makeText(this@SixMinActivity, "复制失败", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initExitAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("提示")
        builder.setMessage("是否确定退出试验？")
        builder.setPositiveButton("是") { _, _ ->
        }
        builder.setNegativeButton("否") { _, _ ->
        }
        exitTestDialog = builder.create()
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

    override fun onResume() {
        if (!USBTransferUtil.isConnectUSB) {
            binding.sixminIvEcg.setImageResource(R.mipmap.xinlvno)
            binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyangno)
            binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangno)
        }
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        usbTransferUtil.disconnect()
        textToSpeech.stop()
        textToSpeech.shutdown()
        mCountDownTime.cancel()
    }

}