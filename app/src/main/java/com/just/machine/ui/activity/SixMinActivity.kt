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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
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
        binding.sixminLlDevicesStatus.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        binding.sixminLlPatientInfo.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        initCountDownTimerExt()
        initExitAlertDialog()
        copyAssetsFilesToSD()
        initLineChart()
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
//            textToSpeech.speak(
//                "试验即将结束，当提示时间到的时候，您不要突然停下来，而是放慢速度继续向前走。",
//                TextToSpeech.QUEUE_ADD,
//                null,
//                null
//            )
            val entries: MutableList<Entry> = java.util.ArrayList()
            for (i in 0..9) {
                val entry = Entry(0f, (i + 85).toFloat())
                entries.add(entry)
            }
            val lineDataSet = LineDataSet(entries, "")
            lineDataSet.lineWidth = 1.0f
            lineDataSet.color = ContextCompat.getColor(this@SixMinActivity, R.color.colorWhile)
            lineDataSet.setDrawValues(false)
            lineDataSet.setDrawCircles(false)
            val lineData = LineData(lineDataSet)
            binding.sixminLineChartBloodOxygen.data = lineData
            binding.sixminLineChartBloodOxygen.invalidate()
        }

        binding.btnMeasureBlood.setOnClickListener {
            val entries: MutableList<Entry> = java.util.ArrayList()
            for (i in 1..9) {
                val entry = Entry(1f, (i + 85).toFloat())
                entries.add(entry)
            }
            val lineDataSet = LineDataSet(entries, "")
            lineDataSet.lineWidth = 1.0f
            lineDataSet.color = ContextCompat.getColor(this@SixMinActivity, R.color.colorWhile)
            lineDataSet.setDrawValues(false)
            lineDataSet.setDrawCircles(false)
            val lineData = LineData(lineDataSet)
            binding.sixminLineChartBloodOxygen.data = lineData
            binding.sixminLineChartBloodOxygen.invalidate()
        }

        binding.sixminIvSystemSetting.setOnClickListener {
            val intent = Intent(this, SixMinSystemSettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initLineChart() {
        binding.sixminLineChartBloodOxygen.apply {
            dragDecelerationFrictionCoef = 0.9f
            isDragEnabled = false
            //开启缩放功能
            setScaleEnabled(false)
            //绘制网格线的背景
            setDrawGridBackground(false)
            //绘制动画的总时长
            animateX(500)
            //是否开启右边Y轴
            axisRight?.isEnabled = false
            //设置图标的标题
            setNoDataText("")
            setTouchEnabled(false)
            isDragEnabled = false
            description?.apply {
                text = ""
            }
            xAxis?.apply {
                textSize = 9f
                textColor = ContextCompat.getColor(this@SixMinActivity, R.color.colorWhile)
                //X轴最大值和最小值
                axisMaximum = 6F
                axisMinimum = 0F
                offsetLeftAndRight(10)
                //X轴每个值的差值(缩放时可以体现出来)
                granularity = 1f
                //X轴的位置
                position = XAxis.XAxisPosition.BOTTOM
                //是否绘制X轴的网格线(垂直于X轴)
                setDrawGridLines(false)
                //X轴的颜色
                axisLineColor = Color.parseColor("#FFD8E5FA")
                //X轴的宽度
                axisLineWidth = 2f
                //设置X轴显示固定条目,放大缩小都显示这个数
                setLabelCount(7, false)
                //是否绘制X轴
                setDrawAxisLine(false)
                //X轴每个刻度对应的值(展示的值和设置的值不同)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.toInt()}min"
                    }
                }
            }

            axisLeft?.apply {
                textColor = ContextCompat.getColor(this@SixMinActivity, R.color.colorWhile)
                //左侧Y轴的最大值和最小值
                axisMaximum = 99f
                axisMinimum = 85f
                setLabelCount(8, true)
                //绘制网格线(样式虚线)
                enableGridDashedLine(2f, 2f, 0f)
                gridColor = ContextCompat.getColor(this@SixMinActivity, R.color.colorWhile)
                setDrawGridLines(true)
                setDrawAxisLine(false) //绘制左边Y轴是否显示
                setDrawZeroLine(false) //是否开启0线
                isGranularityEnabled = true
            }

            legend?.apply {
                setDrawInside(false)
                formSize = 0f
            }
            val lineDataSet = LineDataSet(null, "")
            lineDataSet.lineWidth = 1.0f
            lineDataSet.color = ContextCompat.getColor(this@SixMinActivity, R.color.colorWhile)
            lineDataSet.setDrawValues(false)
            lineDataSet.setDrawCircles(false)
            lineDataSet.setDrawFilled(true)
            lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER;
            val lineData = LineData(lineDataSet)
            data = lineData

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
            binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyano)
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