package com.just.machine.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
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
import com.just.machine.ui.dialog.PatientDialogFragment
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
import java.io.File
import java.io.FileInputStream
import java.text.DecimalFormat
import java.util.Locale
import java.util.Random


@AndroidEntryPoint
class SixMinActivity : CommonBaseActivity<ActivitySixMinBinding>(), TextToSpeech.OnInitListener {

    private lateinit var usbTransferUtil: USBTransferUtil
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var mCountDownTime: FixCountDownTime
    private lateinit var mCountDownTimeThree: CountDownTimer
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
    private var exitType = "0"

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
        initCountDownTimerExt()
        initExitAlertDialog()
        initConfirmBloodFrontDialog()
        initConfirmBloodEndDialog()
        copyAssetsFilesToSD()
        initLineChart(binding.sixminLineChartBloodOxygen, 1)
        initLineChart(binding.sixminLineChartHeartBeat, 2)
        sysSettingBean = SixMinSysSettingBean()
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

    private fun showGuideDialog() {
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
                        binding.sixminTvBloodPressureHighFront.text =
                            usbSerialData.bloodHighFront ?: "---"
                        binding.sixminTvBloodPressureLowFront.text =
                            usbSerialData.bloodLowFront ?: "---"
                        binding.sixminTvBloodPressureHighBehind.text =
                            usbSerialData.bloodHighBehind ?: "---"
                        binding.sixminTvBloodPressureLowBehind.text =
                            usbSerialData.bloodLowBehind ?: "---"
                        if(usbTransferUtil.bloodType == 0){

                        }
                        startStepAndCircle()
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
                if (usbSerialData.circleCount != null && !usbSerialData.circleCount.isEmpty()) {
                    binding.sixminTvCircleCount.text = usbSerialData.circleCount
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
            }else{
                exitType = "1"
                if (!exitTestDialog.isShowing) {
                    exitTestDialog.show()
                }
            }
        }
        binding.sixminIvCircleCountPlus.setOnClickListener {
            if (usbTransferUtil.isBegin) {
                usbTransferUtil.circleCount++
                if (usbSerialData.circleCount == null) {
                    usbSerialData.circleCount = "1"
                } else {
                    usbSerialData.circleCount = (usbSerialData.circleCount.toInt() + 1).toString()
                }
                binding.sixminTvCircleCount.text = usbSerialData.circleCount
            }
        }
        binding.sixminIvCircleCountMinus.setOnClickListener {
            if (usbTransferUtil.isBegin) {
                if (usbTransferUtil.circleCount == 0) {
                    return@setOnClickListener
                }
                usbTransferUtil.circleCount--
                if (usbSerialData.circleCount == null || usbSerialData.circleCount == "0") {
                    return@setOnClickListener
                }
                usbSerialData.circleCount = (usbSerialData.circleCount.toInt() - 1).toString()
                binding.sixminTvCircleCount.text = usbSerialData.circleCount
            }
        }
        binding.sixminIvIgnoreBlood.setOnClickListener {
            usbTransferUtil.isIgnoreBlood = true
            binding.sixminIvIgnoreBlood.visibility = View.INVISIBLE
        }
    }

    private fun startStepAndCircle() {
        if (!usbTransferUtil.isBegin) {
            usbTransferUtil.testType = 1
            binding.sixminTvStart.text = getString(R.string.sixmin_stop)
            binding.sixminTvTestStatus.text = getString(R.string.sixmin_test_testing)
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
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFinish() {
                    usbTransferUtil.testType = 0
                    SixMinCmdUtils.closeQSAndBS()
                    binding.sixminTvStart.text = getString(R.string.sixmin_start)
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
        } else {
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
        usbTransferUtil.isBegin = !usbTransferUtil.isBegin

    }

    private fun startTest() {
        if (!usbTransferUtil.isBegin) {
            if (sysSettingBean.sysOther.autoMeasureBlood == "1") {
                SixMinCmdUtils.measureBloodPressure()
            } else {
                usbTransferUtil.isBegin = true
                usbTransferUtil.testType = 1
                binding.sixminTvStart.text = getString(R.string.sixmin_stop)
                binding.sixminTvTestStatus.text = getString(R.string.sixmin_test_testing)
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
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFinish() {
                        usbTransferUtil.testType = 0
                        SixMinCmdUtils.closeQSAndBS()
                        binding.sixminTvStart.text = getString(R.string.sixmin_start)
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
                text = if (type == 1) "血氧趋势" else "心率趋势"
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
        builder.setMessage("是否确定退出试验？")
        builder.setPositiveButton("是") { _, _ ->
            if(exitType == "0"){
                finish()
            }else{
                val intent = Intent(this, SixMinSystemSettingActivity::class.java)
                startActivity(intent)
            }
        }
        builder.setNegativeButton("否") { _, _ ->
        }
        exitTestDialog = builder.create()
    }

    /**
     * 确认运动前血压弹窗
     */
    private fun initConfirmBloodFrontDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage("是否使用此次运动前血压？")
        builder.setPositiveButton("是") { _, _ ->
            startTest()
        }
        builder.setNegativeButton("否") { _, _ ->
            startTest()
        }
        confirmBloodFrontDialog = builder.create()
    }

    /**
     * 确认运动后血压弹窗
     */
    private fun initConfirmBloodEndDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage("是否使用此次运动后血压？")
        builder.setPositiveButton("是") { _, _ ->
        }
        builder.setNegativeButton("否") { _, _ ->
        }
        confirmBloodEndDialog = builder.create()
    }

    private fun initCountDownTimerExt() {
        mCountDownTime = object : FixCountDownTime(360, 1000) {}
        mCountDownTimeThree = object : CountDownTimer(1080000, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                if(usbSerialData != null && usbSerialData.bloodOxyState != null && usbSerialData.bloodOxyState == "血氧已连接"){
                    val mapBloodOxygen = usbTransferUtil.mapBloodOxygen
//                usbTransferUtil.addOxygenData()
                    val value = mapBloodOxygen.entries.lastOrNull()?.value ?: "98"
                    addEntryData(value.toFloat(), (millisUntilFinished / 1000).toInt())
                }
            }

            override fun onFinish() {

            }
        }
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
        SystemUtil.immersive(this, true)
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
