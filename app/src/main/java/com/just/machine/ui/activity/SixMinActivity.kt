package com.just.machine.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.common.base.CommonBaseActivity
import com.common.base.setNoRepeatListener
import com.common.viewmodel.LiveDataEvent
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.Gson
import com.just.machine.dao.PatientBean
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.UsbSerialData
import com.just.machine.model.systemsetting.SixMinSysSettingBean
import com.just.machine.ui.dialog.SixMinGuideDialogFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.FileUtil
import com.just.machine.util.FixCountDownTime
import com.just.machine.util.LiveDataBus
import com.just.machine.util.ScreenUtils
import com.just.machine.util.SixMinCmdUtils
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.ActivitySixMinBinding
import com.justsafe.libview.util.SystemUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.text.DecimalFormat
import java.util.Locale


@AndroidEntryPoint
class SixMinActivity : CommonBaseActivity<ActivitySixMinBinding>(), TextToSpeech.OnInitListener {

    private val TAG = "SixMinActivity"
    private lateinit var usbTransferUtil: USBTransferUtil
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var mCountDownTime: FixCountDownTime
    private lateinit var mCountDownTimeThree: CountDownTimer
    private lateinit var mStartTestCountDownTime: FixCountDownTime
    private lateinit var exitTestDialog: AlertDialog
    private lateinit var confirmBloodFrontDialog: AlertDialog
    private lateinit var confirmBloodEndDialog: AlertDialog
    private lateinit var bloodOxyDataSet: LineDataSet
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var patientBean: PatientBean
    private lateinit var sysSettingBean: SixMinSysSettingBean
    private var colorList = mutableListOf(0)
    private var notShowAnymore = false
    private lateinit var usbSerialData: UsbSerialData
    private var exitType = "0" //0是返回键 1是跳转系统设置
    private var measureBloodUtteranceId = "" //测量血压语音播报标识
    private var startTestUtteranceId = "" //开始训练语音播报标识
    private var stepsAndCircleUtteranceId = "" //开始步行和计圈语音播报标识
    private var defaultUtteranceId = ""//语音播报开始标识
    private fun addEntryData(entryData: Float, times: Int) {

        val decimalFormat = DecimalFormat("#.00")
        val index: Float = (times.toFloat() / 60)
        bloodOxyDataSet.addEntry(
            Entry(
                ((18.00 - decimalFormat.format(index).toFloat()).toFloat()),
                entryData
            )
        )
        binding.sixminLineChartBloodOxygen.lineData.addDataSet(
            bloodOxyDataSet
        )

        binding.sixminLineChartBloodOxygen.lineData.notifyDataChanged()
        binding.sixminLineChartBloodOxygen.notifyDataSetChanged()
        binding.sixminLineChartBloodOxygen.invalidate()
    }

    override fun initView() {
        initSysInfo()
        initCountDownTimerExt()
        initExitAlertDialog()
        initConfirmBloodFrontDialog()
        initConfirmBloodEndDialog()
        copyAssetsFilesToSD()
        initLineChart(binding.sixminLineChartBloodOxygen, 1)
        initLineChart(binding.sixminLineChartHeartBeat, 2)
        usbTransferUtil = USBTransferUtil.getInstance()
        usbTransferUtil.init(this)
        textToSpeech = TextToSpeech(applicationContext, this)
        usbTransferUtil.connect()
        usbTransferUtil.setOnUSBDateReceive {
            runOnUiThread {
                if (it.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                    //usb设备拔出
                    binding.sixminIvEcg.setImageResource(R.mipmap.xinlvno)
                    binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyano)
                    binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangno)
                    binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi00)
                }
            }
        }

        viewModel.getPatients()
        initClickListener()
        showGuideDialog()
    }

    /**
     * 初始化系统设置信息
     */
    private fun initSysInfo() {
        val gson = Gson()
        sysSettingBean = SixMinSysSettingBean()
        val sixMinSysSetting = SharedPreferencesUtils.instance.sixMinSysSetting
        if (sixMinSysSetting != null && sixMinSysSetting != "") {
            sysSettingBean = gson.fromJson(
                sixMinSysSetting, SixMinSysSettingBean::class.java
            )
        }
    }

    /**
     * 显示引导弹窗
     */
    private fun showGuideDialog() {
        if (sysSettingBean.sysOther.broadcastVoice == "1") {
            val sixMinGuide = SharedPreferencesUtils.instance.sixMinGuide
            if (sixMinGuide == null || sixMinGuide == "") {
                val sixMinGuideDialogFragment =
                    SixMinGuideDialogFragment.startGuideDialogFragment(supportFragmentManager)//添加患者修改患者信息
                sixMinGuideDialogFragment.setDialogOnClickListener(object :
                    SixMinGuideDialogFragment.SixMinGuideDialogListener {
                    override fun onSelectNotSeeAnyMore(checked: Boolean) {
                        notShowAnymore = checked
                    }

                    override fun onClickStartTest() {
                        if (notShowAnymore) {
                            SharedPreferencesUtils.instance.sixMinGuide = "1"
                        }
                        sixMinGuideDialogFragment.dismiss()
                        startTest()
                    }
                })
            }
        }
    }

    private fun initClickListener() {
        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.QuerySuccess -> {
                    it.any?.let { it1 -> beanQuery(it1) }
                }
            }
        }

        LiveDataBus.get().with("111").observe(this, Observer {
            try {
                usbSerialData = Gson().fromJson(it.toString(), UsbSerialData::class.java)
                if (usbSerialData.ecgState == "心电已连接") {
                    binding.sixminIvEcg.setImageResource(R.mipmap.xinlvyes)
                } else {
                    binding.sixminIvEcg.setImageResource(R.mipmap.xinlvno)
                }
                if (usbSerialData.bloodPressureState == "血压已连接") {
                    binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyayes)
                } else {
                    binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyano)
                }
                if (usbSerialData.bloodOxyState == "血氧已连接") {
                    binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangyes)
                } else {
                    binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangno)
                }
                //电池状态
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
                //血压数据
                when (usbSerialData.bloodState) {
                    "未测量血压" -> {
                        binding.sixminTvMeasureBlood.text = getString(R.string.sixmin_measure_blood)
                    }

                    "测量血压中" -> {
                        binding.sixminTvMeasureBlood.text =
                            getString(R.string.sixmin_measuring_blood)
                        binding.sixminTvBloodPressureHigh.text = usbSerialData.bloodHigh ?: "---"
                        binding.sixminTvBloodPressureLow.text = usbSerialData.bloodLow ?: "---"
                    }

                    "测量血压失败" -> {
                        binding.sixminTvMeasureBlood.text = getString(R.string.sixmin_measure_blood)
                        binding.sixminTvBloodPressureHigh.text = "---"
                        binding.sixminTvBloodPressureLow.text = "---"
                    }

                    "测量血压成功" -> {
                        binding.sixminTvMeasureBlood.text = getString(R.string.sixmin_measure_blood)
                        binding.sixminTvBloodPressureHigh.text = usbSerialData.bloodHigh
                        binding.sixminTvBloodPressureLow.text = usbSerialData.bloodLow
                        binding.sixminTvBloodPressureHighBehind.text =
                            usbSerialData.bloodHighBehind ?: "---"
                        binding.sixminTvBloodPressureLowBehind.text =
                            usbSerialData.bloodLowBehind ?: "---"
                        val str =
                            "您当前的收缩压为，${usbSerialData.bloodHigh},舒张压为，${usbSerialData.bloodLow}"
                        speechContent(str)
                        if (usbTransferUtil.bloodType == 0) {
                            if (!confirmBloodFrontDialog.isShowing) {
                                confirmBloodFrontDialog.show()
                            }
                        } else if (usbTransferUtil.bloodType == 2) {
                            if (!confirmBloodEndDialog.isShowing) {
                                confirmBloodEndDialog.show()
                            }
                        }
                    }
                }
                //血氧数据
                if (usbSerialData.bloodOxygen != null && usbSerialData.bloodOxygen != "" && usbSerialData.bloodOxygen != "--") {
                    if (usbSerialData.bloodOxygen.toInt() > sysSettingBean.sysAlarm.bloodOxy.toInt()) {
                        binding.sixminTvBloodOxygen.setTextColor(
                            ContextCompat.getColor(
                                this@SixMinActivity,
                                R.color.red
                            )
                        )
                    } else {
                        binding.sixminTvBloodOxygen.setTextColor(
                            ContextCompat.getColor(
                                this@SixMinActivity,
                                R.color.colorWhite
                            )
                        )
                    }
                    binding.sixminTvBloodOxygen.text = usbSerialData.bloodOxygen
                } else {
                    binding.sixminTvBloodOxygen.text = "--"
                    if (usbSerialData.bloodOxyState == "血氧已连接") {
                        binding.sixminTvBloodOxygen.setTextColor(
                            ContextCompat.getColor(
                                this@SixMinActivity,
                                R.color.red
                            )
                        )
                    } else {
                        binding.sixminTvBloodOxygen.setTextColor(
                            ContextCompat.getColor(
                                this@SixMinActivity,
                                R.color.colorWhite
                            )
                        )
                    }
                }
                //圈数数据
                if (usbSerialData.circleCount != null && usbSerialData.circleCount.isNotEmpty()) {
                    binding.sixminTvCircleCount.text = usbSerialData.circleCount
                }
                Log.d("SixMinActivity", usbSerialData.toString())
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
                startTest()
            } else {
                binding.sixminLlLineChartBloodOxygen.visibility = View.VISIBLE
                Toast.makeText(this, "请先接入设备", Toast.LENGTH_SHORT).show()
            }
        }

        binding.sixminIvClose.setNoRepeatListener {
            if (usbTransferUtil.isBegin) {
                exitType = "0"
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
        }

        binding.sixminIvSystemSetting.setOnClickListener {
            if (!usbTransferUtil.isBegin) {
                val intent = Intent(this, SixMinSystemSettingActivity::class.java)
                startActivity(intent)
            } else {
                exitType = "1"
                if (!exitTestDialog.isShowing) {
                    exitTestDialog.show()
                }
            }
        }
        binding.sixminIvCircleCountPlus.setOnClickListener {
            if (usbTransferUtil.isBegin) {
                if (usbSerialData.circleCount == null) {
                    usbSerialData.circleCount = "0"
                }
                usbSerialData.circleCount = (usbSerialData.circleCount.toInt() + 1).toString()
                usbTransferUtil.circleCount = usbSerialData.circleCount.toInt()
                binding.sixminTvCircleCount.text = usbSerialData.circleCount
            }
        }
        binding.sixminIvCircleCountMinus.setOnClickListener {
            if (usbTransferUtil.isBegin) {
                if (usbSerialData.circleCount == null || usbSerialData.circleCount == "0") {
                    return@setOnClickListener
                }
                usbSerialData.circleCount = (usbSerialData.circleCount.toInt() - 1).toString()
                usbTransferUtil.circleCount = usbSerialData.circleCount.toInt()
                binding.sixminTvCircleCount.text = usbSerialData.circleCount
            }
        }
        binding.sixminIvIgnoreBlood.setOnClickListener {
            usbTransferUtil.isIgnoreBlood = true
            binding.sixminIvIgnoreBlood.visibility = View.INVISIBLE
        }
    }

    private fun startStepAndCircle() {
        usbTransferUtil.isBegin = true
        usbTransferUtil.testType = 1
        binding.sixminTvStart.text = getString(R.string.sixmin_stop)
        binding.sixminTvTestStatus.text = getString(R.string.sixmin_test_testing)
        usbSerialData.circleCount = "0"
        binding.sixminTvCircleCount.text = "0"
        SixMinCmdUtils.openQSAndBS()
        mCountDownTime.start(object : FixCountDownTime.OnTimerCallBack {
            override fun onStart() {

            }

            override fun onTick(times: Int) {
                try {
                    val minute = times / 60 % 60
                    val second = times % 60
                    binding.sixminTvStartMin.text = minute.toString()
                    if (second.toString().length == 1) {
                        binding.sixminTvStartSec1.text = "0"
                        binding.sixminTvStartSec2.text = second.toString()
                    } else {
                        binding.sixminTvStartSec1.text =
                            second.toString().substring(0, 1)
                        binding.sixminTvStartSec2.text =
                            second.toString().substring(1)
                    }

                    if(second == 0){
                        when (minute) {
                            5 -> {
                                speechContent(getString(R.string.sixmin_test_start_count_down_five_tips))
                            }
                            4 -> {
                                speechContent(getString(R.string.sixmin_test_start_count_down_four_tips))
                            }
                            3 -> {
                                speechContent(getString(R.string.sixmin_test_start_count_down_three_tips))
                            }
                            2 -> {
                                speechContent(getString(R.string.sixmin_test_start_count_down_two_tips))
                            }
                            1 -> {
                                speechContent(getString(R.string.sixmin_test_start_count_down_one_tips))
                            }
                        }
                    }

                    if(minute == 0 && second == 16){
                        speechContent(getString(R.string.sixmin_test_start_count_down_zero_tips))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFinish() {
                speechContent(getString(R.string.sixmin_test_start_timeout))
                usbTransferUtil.isBegin = false
                usbTransferUtil.testType = 0
                usbTransferUtil.bloodType = 2
                SixMinCmdUtils.closeQSAndBS()
                lifecycleScope.launch {
                    kotlinx.coroutines.delay(1000L)
                    if(sysSettingBean.sysOther.autoMeasureBlood == "1"){
                        binding.sixminTvBloodPressureHigh.text = "---"
                        binding.sixminTvBloodPressureLow.text = "---"
                        SixMinCmdUtils.measureBloodPressure()
                    }
                }
                binding.sixminTvStart.text = getString(R.string.sixmin_start)
                binding.sixminTvTestStatus.text = getString(R.string.sixmin_test_title)
                binding.sixminTvStartMin.text = "0"
                binding.sixminTvStartSec1.text = "0"
                binding.sixminTvStartSec2.text = "0"
                binding.sixminIvIgnoreBlood.visibility = View.VISIBLE
                usbTransferUtil.isIgnoreBlood = false
                mCountDownTimeThree.cancel()
                mCountDownTime.setmTimes(360)
            }
        })
        mCountDownTimeThree.start()
    }

    private fun startTest() {
        if (!usbTransferUtil.isBegin) {
            //是否自动测量血压
            if (sysSettingBean.sysOther.autoMeasureBlood == "1") {
                //是否播报语音
                if (sysSettingBean.sysOther.broadcastVoice == "1") {
                    measureBloodUtteranceId = System.currentTimeMillis().toString()
                    speechContent(
                        getString(R.string.sixmin_test_start_measure_blood_front),
                        measureBloodUtteranceId
                    )
                } else {
                    SixMinCmdUtils.measureBloodPressure()
                }
            } else {
                if (sysSettingBean.sysOther.broadcastVoice == "1") {
                    startTestUtteranceId = System.currentTimeMillis().toString()
                    speechContent(
                        getString(R.string.sixmin_test_start_test),
                        startTestUtteranceId
                    )
                } else {
                    startStepAndCircle()
                }
            }
        } else {
            usbTransferUtil.isBegin = false
            usbTransferUtil.testType = 0
            SixMinCmdUtils.closeQSAndBS()
            binding.sixminTvStart.text = getString(R.string.sixmin_start)
            binding.sixminTvTestStatus.text = getString(R.string.sixmin_test_title)
            binding.sixminTvStartMin.text = "5"
            binding.sixminTvStartSec1.text = "5"
            binding.sixminTvStartSec2.text = "9"
            mCountDownTime.cancel()
            mCountDownTime.setmTimes(360)
            binding.sixminIvIgnoreBlood.visibility = View.VISIBLE
            usbTransferUtil.isIgnoreBlood = false
        }
    }

    private fun beanQuery(any: Any) {
        try {
            if (any is List<*>) {
                val datas = any as MutableList<*>
                patientBean = datas[0] as PatientBean
                binding.sixminTvTestPatientName.text = patientBean.name
                binding.sixminTvTestPatientSex.text = patientBean.sex
                binding.sixminTvTestPatientAge.text = patientBean.age
                binding.sixminTvTestPatientHight.text = patientBean.height
                binding.sixminTvTestPatientWeight.text = patientBean.weight
                binding.sixminTvTestPatientBmi.text = patientBean.BMI
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initLineChart(lineChart: LineChart, type: Int) {
        lineChart.apply {
            dragDecelerationFrictionCoef = 0.9f
            isDragEnabled = false
            //开启缩放功能
            setScaleEnabled(false)
            clearAnimation()
            //绘制网格线的背景
            setDrawGridBackground(false)
            //绘制动画的总时长
//            animateX(500)
            //是否开启右边Y轴
            axisRight?.isEnabled = false
            //设置图标的标题
            setNoDataText("")
            setTouchEnabled(false)
            isDragEnabled = false
            val screenWidth = ScreenUtils.getScreenWidth(this@SixMinActivity)
            description.setPosition((screenWidth / 2).toFloat() + 140, 20f)
            description.textSize = 11f
            description?.apply {
                text = if (type == 1) getString(R.string.sixmin_test_start_blood_oxygen_trend) else getString(R.string.sixmin_test_start_heart_beat_trend)
            }
            xAxis?.apply {
                textSize = 10f
                textColor = ContextCompat.getColor(this@SixMinActivity, R.color.text3)
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
                textColor = ContextCompat.getColor(this@SixMinActivity, R.color.text3)
                //左侧Y轴的最大值和最小值
                axisMaximum = if (type == 1) 99f else 170f
                axisMinimum = if (type == 1) 85f else 30f
                setLabelCount(8, true)
                //绘制网格线(样式虚线)
                enableGridDashedLine(2f, 2f, 0f)
                gridColor = ContextCompat.getColor(this@SixMinActivity, R.color.text3)
                setDrawGridLines(true)
                setDrawAxisLine(false) //绘制左边Y轴是否显示
                setDrawZeroLine(false) //是否开启0线
                isGranularityEnabled = true
            }

            legend?.apply {
                setDrawInside(false)
                formSize = 0f
            }
            bloodOxyDataSet = LineDataSet(null, "")
            bloodOxyDataSet.lineWidth = 1.0f
            bloodOxyDataSet.color = ContextCompat.getColor(this@SixMinActivity, R.color.text3)
            bloodOxyDataSet.setDrawValues(false)
            bloodOxyDataSet.setDrawCircles(false)
            bloodOxyDataSet.setDrawCircleHole(false)
            bloodOxyDataSet.setDrawFilled(false)
            bloodOxyDataSet.mode = LineDataSet.Mode.LINEAR
            val lineData = LineData(bloodOxyDataSet)
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

    /**
     * 退出试验弹窗
     */
    private fun initExitAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage(getString(R.string.sixmin_test_start_exit_test_tips))
        builder.setPositiveButton(getString(R.string.sixmin_system_setting_check_yes)) { _, _ ->
            if (exitType == "0") {
                finish()
            } else {
                val intent = Intent(this, SixMinSystemSettingActivity::class.java)
                startActivity(intent)
            }
        }
        builder.setNegativeButton(getString(R.string.sixmin_system_setting_check_no)) { _, _ ->
        }
        exitTestDialog = builder.create()
        exitTestDialog.setCanceledOnTouchOutside(false)
        exitTestDialog.setCancelable(false)
    }

    /**
     * 确认运动前血压弹窗
     */
    private fun initConfirmBloodFrontDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage(getString(R.string.sixmin_test_start_use_this_blood_pressure_front))
        builder.setPositiveButton(getString(R.string.sixmin_system_setting_check_yes)) { _, _ ->
            binding.sixminTvBloodPressureHighFront.text =
                usbSerialData.bloodHighFront ?: "---"
            binding.sixminTvBloodPressureLowFront.text =
                usbSerialData.bloodLowFront ?: "---"
            usbTransferUtil.bloodType = 1
            if (sysSettingBean.sysOther.broadcastVoice == "1") {
                startTestUtteranceId = System.currentTimeMillis().toString()
                speechContent(getString(R.string.sixmin_test_start_test), startTestUtteranceId)
            }
        }
        builder.setNegativeButton(getString(R.string.sixmin_system_setting_check_no)) { _, _ ->
            usbTransferUtil.bloodType = 1
            if (sysSettingBean.sysOther.broadcastVoice == "1") {
                startTestUtteranceId = System.currentTimeMillis().toString()
                speechContent(getString(R.string.sixmin_test_start_test), startTestUtteranceId)
            }
        }
        confirmBloodFrontDialog = builder.create()
        confirmBloodFrontDialog.setCanceledOnTouchOutside(false)
        confirmBloodFrontDialog.setCancelable(false)
    }

    /**
     * 确认运动后血压弹窗
     */
    private fun initConfirmBloodEndDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage(getString(R.string.sixmin_test_start_use_this_blood_pressure_behind))
        builder.setPositiveButton(getString(R.string.sixmin_system_setting_check_yes)) { _, _ ->
            binding.sixminTvBloodPressureHighBehind.text =
                usbSerialData.bloodHighBehind ?: "---"
            binding.sixminTvBloodPressureLowBehind.text =
                usbSerialData.bloodLowBehind ?: "---"
            usbTransferUtil.bloodType = 0
        }
        builder.setNegativeButton(getString(R.string.sixmin_system_setting_check_no)) { _, _ ->
            usbTransferUtil.bloodType = 0
        }
        confirmBloodEndDialog = builder.create()
        confirmBloodEndDialog.setCanceledOnTouchOutside(false)
        confirmBloodEndDialog.setCancelable(false)
    }

    private fun initCountDownTimerExt() {
        mCountDownTime = object : FixCountDownTime(360, 1000) {}
        mCountDownTimeThree = object : CountDownTimer(1080000, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                if (usbSerialData != null && usbSerialData.bloodOxyState != null && usbSerialData.bloodOxyState == "血氧已连接") {
                    val mapBloodOxygen = usbTransferUtil.mapBloodOxygen
//                usbTransferUtil.addOxygenData()
                    val value = mapBloodOxygen.entries.lastOrNull()?.value ?: "98"
                    addEntryData(value.toFloat(), (millisUntilFinished / 1000).toInt())
                }
            }

            override fun onFinish() {

            }
        }
        mStartTestCountDownTime = object : FixCountDownTime(5, 1000) {}
    }

    override fun getViewBinding() = ActivitySixMinBinding.inflate(layoutInflater)

    override fun onInit(status: Int) {
        //判断是否转化成功
        if (status == TextToSpeech.SUCCESS) {
            //设置语言为中文
            textToSpeech.language = Locale.CHINA
            //设置音调,值越大声音越尖（女生），值越小则变成男声,1.0是常规
            textToSpeech.setPitch(1f)
            //设置语速
            textToSpeech.setSpeechRate(1f)
            //在onInIt方法里直接调用tts的播报功能
            textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    Log.d(TAG, "onStart: $utteranceId")
                    defaultUtteranceId = utteranceId!!
                }

                override fun onDone(utteranceId: String?) {
                    runOnUiThread {
                        Log.d(TAG, "onDone: $utteranceId")
                        if (measureBloodUtteranceId == defaultUtteranceId) {
                            SixMinCmdUtils.measureBloodPressure()
                        } else if (startTestUtteranceId == defaultUtteranceId) {
                            mStartTestCountDownTime.start(object :
                                FixCountDownTime.OnTimerCallBack {
                                override fun onStart() {
                                    binding.sixminIvCountdownTime.visibility = View.VISIBLE
                                }

                                override fun onTick(times: Int) {
                                    var imageId = -1
                                    when (times) {
                                        5 -> {
                                            imageId = R.mipmap.five
                                        }

                                        4 -> {
                                            imageId = R.mipmap.four
                                        }

                                        3 -> {
                                            imageId = R.mipmap.three
                                        }

                                        2 -> {
                                            imageId = R.mipmap.two
                                        }

                                        1 -> {
                                            imageId = R.mipmap.one
                                        }
                                    }
                                    speechContent(times.toString())
                                    lifecycleScope.launch {
                                        kotlinx.coroutines.delay(1200L)
                                        binding.sixminIvCountdownTime.setImageResource(imageId)
                                    }
                                }

                                override fun onFinish() {
                                    lifecycleScope.launch {
                                        kotlinx.coroutines.delay(1000L)
                                        binding.sixminIvCountdownTime.visibility = View.GONE
                                        stepsAndCircleUtteranceId =
                                            System.currentTimeMillis().toString()
                                        speechContent("请开始步行。", stepsAndCircleUtteranceId)
                                    }
                                }
                            })
                        } else if (stepsAndCircleUtteranceId == defaultUtteranceId) {
                            startStepAndCircle()
                        }
                    }
                }

                override fun onError(utteranceId: String?) {
                    Log.d(TAG, "onError: $utteranceId")
                }
            })
        }
    }

    private fun speechContent(
        content: String,
        utteranceId: String = System.currentTimeMillis().toString()
    ) {
        textToSpeech.speak(content, TextToSpeech.QUEUE_ADD, null, utteranceId)
    }

    override fun onResume() {
        if (!USBTransferUtil.isConnectUSB) {
            binding.sixminIvEcg.setImageResource(R.mipmap.xinlvno)
            binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyano)
            binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangno)
        }
        initSysInfo()
        SystemUtil.immersive(this, true)
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        usbTransferUtil.isBegin = false
        usbTransferUtil.testType = 0
        usbTransferUtil.bloodType = 0
        SixMinCmdUtils.closeQSAndBS()
        usbTransferUtil.disconnect()
        textToSpeech.stop()
        textToSpeech.shutdown()
        mCountDownTime.cancel()
        mCountDownTimeThree.cancel()
        mStartTestCountDownTime.cancel()
    }

}
