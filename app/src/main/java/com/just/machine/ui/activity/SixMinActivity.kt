package com.just.machine.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
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
import com.google.gson.Gson
import androidx.activity.viewModels
import com.just.machine.model.BloodOxyLineEntryBean
import com.just.machine.model.Constants
import com.just.machine.model.PatientInfoBean
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.UsbSerialData
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinHeartEcg
import com.just.machine.model.sixminreport.SixMinReportBreathing
import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.model.sixminreport.SixMinReportHeartBeat
import com.just.machine.model.sixminreport.SixMinReportInfo
import com.just.machine.model.sixminreport.SixMinReportOther
import com.just.machine.model.sixminreport.SixMinReportPrescription
import com.just.machine.model.sixminreport.SixMinReportStride
import com.just.machine.model.sixminreport.SixMinReportWalk
import com.just.machine.model.systemsetting.SixMinSysSettingBean
import com.just.machine.ui.dialog.CommonDialogFragment
import com.just.machine.ui.dialog.SixMinCollectRestoreEcgDialogFragment
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
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class SixMinActivity : CommonBaseActivity<ActivitySixMinBinding>(), TextToSpeech.OnInitListener {

    private val TAG = "SixMinActivity"
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var usbTransferUtil: USBTransferUtil
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var mCountDownTime: FixCountDownTime
    private lateinit var mCountDownTimeThree: CountDownTimer//每3秒一个值获取心率，血氧
    private lateinit var mStartTestCountDownTime: FixCountDownTime//6分钟试验倒计时
    private lateinit var mGetSportHeartEcgCountDownTime: FixCountDownTime//采集运动心率倒计时
    private lateinit var bloodOxyDataSet: LineDataSet
    private lateinit var patientBean: PatientInfoBean//患者信息
    private lateinit var sysSettingBean: SixMinSysSettingBean//系统设置
    private var colorList = mutableListOf(0)
    private var notShowAnymore = false
    private lateinit var usbSerialData: UsbSerialData
    private var exitType = "0" //0是返回键 1是跳转系统设置
    private var measureBloodUtteranceId = "" //测量血压语音播报标识
    private var startTestUtteranceId = "" //开始训练语音播报标识
    private var stepsAndCircleUtteranceId = "" //开始步行和计圈语音播报标识
    private var bloodBeforeUtteranceId = "" //运动前血压语音播报标识
    private var bloodEndUtteranceId = "" //运动后血压语音播报标识
    private var defaultUtteranceId = ""//语音播报开始标识
    private var sixMinReportBloodOxy: SixMinBloodOxygen = SixMinBloodOxygen()//6分钟血氧
    private var sixMinReportBloodOther: SixMinReportOther = SixMinReportOther()//6分钟其它信息
    private var sixMinReportBloodHeart: SixMinReportHeartBeat = SixMinReportHeartBeat()//6分钟心率
    private var sixMinReportBloodHeartEcg: SixMinHeartEcg = SixMinHeartEcg()//6分钟心电
    private var sixMinReportWalk: SixMinReportWalk = SixMinReportWalk()//6分钟步数
    private var sixMinReportStride: SixMinReportStride = SixMinReportStride()//6分钟步速
    private var sixMinReportEvaluation: SixMinReportEvaluation = SixMinReportEvaluation()//6分钟综合评估
    private var sixMinReportInfo: SixMinReportInfo = SixMinReportInfo()//6分钟报告信息
    private var sixMinReportPrescription: SixMinReportPrescription =
        SixMinReportPrescription()//6分钟报告处方信息
    private var sixMinReportBreathing: SixMinReportBreathing = SixMinReportBreathing()//6分钟报告呼吸率信息
    private var selfCheckSelection = "" //试验前疲劳和呼吸量级
    private var testPatientId = "" //试验的患者id
    private lateinit var startRestoreEcgDialogFragment: SixMinCollectRestoreEcgDialogFragment
    private var timeRemain = "" //采集恢复心电倒计时

    private fun addEntryData(entryData: Float, times: Int) {

        val decimalFormat = DecimalFormat("#.00")
        val index: Float = (times.toFloat() / 60)
        bloodOxyDataSet.addEntry(
            Entry(
                ((18.00 - decimalFormat.format(index).toFloat()).toFloat()), entryData
            )
        )
        usbTransferUtil.bloodOxyLineData.add(
            BloodOxyLineEntryBean(
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
        binding.sixminIvIgnoreBlood.isEnabled = false
        binding.sixminRlStart.isEnabled = false
        binding.sixminRlMeasureBlood.isEnabled = false
        selfCheckSelection =
            intent.extras?.getString(Constants.sixMinSelfCheckViewSelection).toString()
        testPatientId =
            intent.extras?.getString(Constants.sixMinPatientInfo).toString()
        initSysInfo()
        initCountDownTimerExt()
        copyAssetsFilesToSD()
        initLineChart(binding.sixminLineChartBloodOxygen, 1)
        initLineChart(binding.sixminLineChartHeartBeat, 2)
        usbTransferUtil = USBTransferUtil.getInstance()
        usbTransferUtil.init(this)
        usbTransferUtil.connect()
        usbTransferUtil.bloodOxyLineData.clear()
        textToSpeech = TextToSpeech(applicationContext, this)
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
        viewModel.getSixMinReportInfo()
    }

    private fun initPatientInfo() {
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
        val reportNo = dateFormat.format(date)
        sixMinReportInfo.patientId = patientBean.infoBean.patientId
        sixMinReportInfo.reportNo = reportNo
        sixMinReportInfo.patientName = patientBean.infoBean.name.toString()
        sixMinReportInfo.patientHeight = patientBean.infoBean.height.toString()
        sixMinReportInfo.patientWeight = patientBean.infoBean.weight.toString()
        sixMinReportInfo.patientSix = patientBean.infoBean.sex.toString()
        sixMinReportInfo.patientAge = patientBean.infoBean.age.toString()
        sixMinReportInfo.patientBmi = patientBean.infoBean.BMI.toString()
        sixMinReportInfo.patientStride = patientBean.infoBean.stride.toString()
        sixMinReportInfo.clinicalDiagnosis = patientBean.infoBean.clinicalDiagnosis.toString()
        sixMinReportInfo.medicineUse = patientBean.infoBean.currentMedications.toString()
        sixMinReportInfo.predictionDistance = patientBean.infoBean.predictDistances.toString()
        sixMinReportInfo.medicalNo = patientBean.infoBean.medicalRecordNumber.toString()
        sixMinReportInfo.medicalHistory = patientBean.infoBean.diseaseHistory.toString()
        sixMinReportInfo.bsHxl = sysSettingBean.sysOther.stepsOrBreath

        sixMinReportBloodOther.reportId = sixMinReportInfo.reportNo
        sixMinReportBloodOther.useName = sysSettingBean.sysOther.useOrg
        sixMinReportBloodOther.ecgType = sysSettingBean.sysOther.ectType

        sixMinReportBloodOxy.reportId = sixMinReportInfo.reportNo

        sixMinReportBloodHeart.reportId = sixMinReportInfo.reportNo

        sixMinReportBloodHeartEcg.reportId = sixMinReportInfo.reportNo

        sixMinReportEvaluation.reportId = sixMinReportInfo.reportNo
        if (selfCheckSelection != "") {
            val split = selfCheckSelection.split("&")
            if (split.size > 1) {
                sixMinReportEvaluation.befoFatigueLevel = split[1]
                sixMinReportEvaluation.befoBreathingLevel = split[0]
            }
        }

        sixMinReportPrescription.reportId = sixMinReportInfo.reportNo

        sixMinReportBreathing.reportId = sixMinReportInfo.reportNo

        sixMinReportStride.reportId = sixMinReportInfo.reportNo

        sixMinReportWalk.reportId = sixMinReportInfo.reportNo
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            SystemUtil.immersive(this, true)
        }
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
        if (usbTransferUtil.isConnectUSB) {
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
                            startTest()
                        }
                    })
                } else {
                    startTest()
                }
            } else {
                startTest()
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

        LiveDataBus.get().with("simMinTest").observe(this, Observer {
            try {
                usbSerialData = Gson().fromJson(it.toString(), UsbSerialData::class.java)
                if (usbTransferUtil.ecgConnection) {
                    binding.sixminIvEcg.setImageResource(R.mipmap.xinlvyes)
                } else {
                    binding.sixminIvEcg.setImageResource(R.mipmap.xinlvno)
                }
                if (usbTransferUtil.bloodPressureConnection) {
                    binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyayes)
                } else {
                    binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyano)
                }
                if (usbTransferUtil.bloodOxygenConnection) {
                    binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangyes)
                } else {
                    binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangno)
                }
                //电池状态
                when (usbTransferUtil.batteryLevel) {
                    1 -> {
                        binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi00)
                    }

                    2 -> {
                        binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi1)
                    }

                    3 -> {
                        binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi2)
                    }

                    4 -> {
                        binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi3)
                    }

                    5 -> {
                        binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi4)
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
                        binding.sixminRlStart.isEnabled = true
                        binding.sixminIvIgnoreBlood.isEnabled = true
                        if (usbTransferUtil.bloodType == 2 && usbTransferUtil.ignoreBlood) {
                            val startCommonDialogFragment =
                                CommonDialogFragment.startCommonDialogFragment(
                                    supportFragmentManager,
                                    "", "1", "1"
                                )
                            startCommonDialogFragment.setCommonDialogOnClickListener(
                                object :
                                    CommonDialogFragment.CommonDialogClickListener {
                                    override fun onPositiveClick() {

                                    }

                                    override fun onNegativeClick() {

                                    }

                                    override fun onStopNegativeClick(stopReason: String) {
                                        usbTransferUtil.isBegin = false
                                        usbTransferUtil.testType = 0
                                        SixMinCmdUtils.closeQSAndBS()
                                        binding.sixminTvStart.text =
                                            getString(R.string.sixmin_start)
                                        binding.sixminTvTestStatus.text =
                                            getString(R.string.sixmin_test_title)
//                                        binding.sixminTvStartMin.text = "0"
//                                        binding.sixminTvStartSec1.text = "0"
//                                        binding.sixminTvStartSec2.text = "0"
                                        mCountDownTime.cancel()
                                        mCountDownTime.setmTimes(360)

                                        sixMinReportBloodOther.badOr = "1"
                                        sixMinReportBloodOther.badSymptoms = stopReason

                                        generateReportData(
                                            usbTransferUtil.min,
                                            usbTransferUtil.sec1 + usbTransferUtil.sec2,
                                            0,
                                            0,
                                            0
                                        )
                                    }
                                })
                            usbTransferUtil.ignoreBlood = false
                        }
                    }

                    "测量血压成功" -> {
                        binding.sixminTvMeasureBlood.text = getString(R.string.sixmin_measure_blood)
                        if (usbSerialData.bloodHigh.toInt() > sysSettingBean.sysAlarm.highPressure.toInt()) {
                            binding.sixminTvBloodPressureHigh.setTextColor(
                                ContextCompat.getColor(
                                    this@SixMinActivity, R.color.red
                                )
                            )
                        } else {
                            binding.sixminTvBloodPressureHigh.setTextColor(
                                ContextCompat.getColor(
                                    this@SixMinActivity, R.color.white
                                )
                            )
                        }
                        if (usbSerialData.bloodLow.toInt() > sysSettingBean.sysAlarm.lowPressure.toInt()) {
                            binding.sixminTvBloodPressureLow.setTextColor(
                                ContextCompat.getColor(
                                    this@SixMinActivity, R.color.red
                                )
                            )
                        } else {
                            binding.sixminTvBloodPressureLow.setTextColor(
                                ContextCompat.getColor(
                                    this@SixMinActivity, R.color.white
                                )
                            )
                        }
                        binding.sixminTvBloodPressureHigh.text = usbSerialData.bloodHigh
                        binding.sixminTvBloodPressureLow.text = usbSerialData.bloodLow
                        binding.sixminTvBloodPressureHighBehind.text =
                            usbSerialData.bloodHighBehind ?: "---"
                        binding.sixminTvBloodPressureLowBehind.text =
                            usbSerialData.bloodLowBehind ?: "---"
                        val str =
                            "您当前的收缩压为，${usbSerialData.bloodHigh},舒张压为，${usbSerialData.bloodLow}"
                        if (usbTransferUtil.bloodType == 0) {
                            binding.sixminTvBloodPressureHighFront.text =
                                usbSerialData.bloodHighFront ?: "---"
                            binding.sixminTvBloodPressureLowFront.text =
                                usbSerialData.bloodLowFront ?: "---"
                            sixMinReportBloodOther.startHighPressure = usbSerialData.bloodHighFront
                            sixMinReportBloodOther.startLowPressure = usbSerialData.bloodLowFront
                            if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                                bloodBeforeUtteranceId = System.currentTimeMillis().toString()
                                speechContent(str, bloodBeforeUtteranceId)
                            } else {
                                val startCommonDialogFragment =
                                    CommonDialogFragment.startCommonDialogFragment(
                                        supportFragmentManager,
                                        getString(R.string.sixmin_test_start_use_this_blood_pressure_front)
                                    )
                                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                                    CommonDialogFragment.CommonDialogClickListener {
                                    override fun onPositiveClick() {
                                        usbTransferUtil.bloodType = 1
                                        if (sysSettingBean.sysOther.autoStart == "1") {
                                            if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                                                startTestUtteranceId =
                                                    System.currentTimeMillis().toString()
                                                speechContent(
                                                    getString(R.string.sixmin_test_start_test),
                                                    startTestUtteranceId
                                                )
                                            } else {
                                                autoStartTest()
                                            }
                                        }
                                    }

                                    override fun onNegativeClick() {
                                        usbTransferUtil.bloodType = 1
                                    }

                                    override fun onStopNegativeClick(stopReason: String) {

                                    }
                                })
                            }
                        } else if (usbTransferUtil.bloodType == 2) {
                            binding.sixminTvBloodPressureHighBehind.text =
                                usbSerialData.bloodHighBehind ?: "---"
                            binding.sixminTvBloodPressureLowBehind.text =
                                usbSerialData.bloodLowBehind ?: "---"
                            sixMinReportBloodOther.stopHighPressure = usbSerialData.bloodHighBehind
                            sixMinReportBloodOther.stopLowPressure = usbSerialData.bloodLowBehind
                            bloodEndUtteranceId = System.currentTimeMillis().toString()
                            if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                                speechContent(str, bloodEndUtteranceId)
                            } else {
                                val startCommonDialogFragment =
                                    CommonDialogFragment.startCommonDialogFragment(
                                        supportFragmentManager,
                                        getString(R.string.sixmin_test_start_use_this_blood_pressure_behind)
                                    )
                                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                                    CommonDialogFragment.CommonDialogClickListener {
                                    override fun onPositiveClick() {
                                        usbTransferUtil.bloodType = 0
                                        sixMinReportBloodOther.stopHighPressure =
                                            usbSerialData.bloodHighBehind
                                        sixMinReportBloodOther.stopLowPressure =
                                            usbSerialData.bloodLowBehind
                                        usbTransferUtil.isBegin = false
                                        usbTransferUtil.testType = 0
                                        SixMinCmdUtils.closeQSAndBS()
                                        binding.sixminTvStart.text =
                                            getString(R.string.sixmin_start)
                                        binding.sixminTvTestStatus.text =
                                            getString(R.string.sixmin_test_title)
//                                        binding.sixminTvStartMin.text = "0"
//                                        binding.sixminTvStartSec1.text = "0"
//                                        binding.sixminTvStartSec2.text = "0"
                                        mCountDownTime.cancel()
                                        mCountDownTime.setmTimes(360)
                                        usbTransferUtil.ignoreBlood = false

                                        sixMinReportBloodOther.badOr = "0"
                                        generateReportData(
                                            usbTransferUtil.min,
                                            usbTransferUtil.sec1 + usbTransferUtil.sec2,
                                            0,
                                            0,
                                            0
                                        )
//                                        if (usbTransferUtil.ignoreBlood) {
//                                            val startCommonDialogFragment2 =
//                                                CommonDialogFragment.startCommonDialogFragment(
//                                                    supportFragmentManager,
//                                                    "", "1", "1"
//                                                )
//                                            startCommonDialogFragment2.setCommonDialogOnClickListener(
//                                                object :
//                                                    CommonDialogFragment.CommonDialogClickListener {
//                                                    override fun onPositiveClick() {
//
//                                                    }
//
//                                                    override fun onNegativeClick() {
//
//                                                    }
//
//                                                    override fun onStopNegativeClick(stopReason: String) {
//                                                        usbTransferUtil.isBegin = false
//                                                        usbTransferUtil.testType = 0
//                                                        SixMinCmdUtils.closeQSAndBS()
//                                                        binding.sixminTvStart.text =
//                                                            getString(R.string.sixmin_start)
//                                                        binding.sixminTvTestStatus.text =
//                                                            getString(R.string.sixmin_test_title)
//                                                        binding.sixminTvStartMin.text = "5"
//                                                        binding.sixminTvStartSec1.text = "5"
//                                                        binding.sixminTvStartSec2.text = "9"
//                                                        mCountDownTime.cancel()
//                                                        mCountDownTime.setmTimes(360)
//                                                        usbTransferUtil.ignoreBlood = false
//
//                                                        sixMinReportBloodOther.badOr = "0"
//                                                        usbTransferUtil.dealWalk(sixMinReportWalk)
//                                                        usbTransferUtil.dealBlood(
//                                                            sixMinReportBloodOxy,
//                                                            Gson().toJson(binding.sixminLineChartBloodOxygen)
//                                                        )
//                                                        if (sysSettingBean.sysOther.circleCountType == "0") {
//                                                            usbTransferUtil.dealPreption(
//                                                                sysSettingBean,
//                                                                sixMinReportEvaluation,
//                                                                "5",
//                                                                "59",
//                                                                0,
//                                                                0,
//                                                                0
//                                                            )
//                                                        } else {
//                                                            usbTransferUtil.dealPreptionSD(
//                                                                sixMinReportEvaluation,
//                                                                sysSettingBean,
//                                                                "5",
//                                                                "59",
//                                                                0,
//                                                                0,
//                                                                0
//                                                            )
//                                                        }
//                                                        viewModel.setSixMinReportBloodOxyData(
//                                                            sixMinReportBloodOxy
//                                                        )
//                                                        viewModel.setSixMinReportWalkData(
//                                                            sixMinReportWalk
//                                                        )
//
//                                                        startActivity(
//                                                            Intent(
//                                                                this@SixMinActivity,
//                                                                SixMinPreReportActivity::class.java
//                                                            )
//                                                        )
//                                                        finish()
//                                                    }
//                                                })
//                                        }
                                    }

                                    override fun onNegativeClick() {
                                        usbTransferUtil.bloodType = 0
                                    }

                                    override fun onStopNegativeClick(stopReason: String) {

                                    }
                                })
                            }
                        }
                    }
                }
                //血氧数据
                if (usbSerialData.bloodOxygen != null && usbSerialData.bloodOxygen != "" && usbSerialData.bloodOxygen != "--" && usbSerialData.bloodOxygen != "---") {
                    if (usbSerialData.bloodOxygen.toInt() > sysSettingBean.sysAlarm.bloodOxy.toInt()) {
                        binding.sixminTvBloodOxygen.setTextColor(
                            ContextCompat.getColor(
                                this@SixMinActivity, R.color.red
                            )
                        )
                    } else {
                        binding.sixminTvBloodOxygen.setTextColor(
                            ContextCompat.getColor(
                                this@SixMinActivity, R.color.colorWhite
                            )
                        )
                    }
                    binding.sixminTvBloodOxygen.text = usbSerialData.bloodOxygen
                } else {
                    binding.sixminTvBloodOxygen.text = "--"
                    if (usbTransferUtil.bloodOxygenConnection) {
                        binding.sixminTvBloodOxygen.setTextColor(
                            ContextCompat.getColor(
                                this@SixMinActivity, R.color.red
                            )
                        )
                    } else {
                        binding.sixminTvBloodOxygen.setTextColor(
                            ContextCompat.getColor(
                                this@SixMinActivity, R.color.colorWhite
                            )
                        )
                    }
                }
                //圈数数据
                if (usbTransferUtil.isBegin) {
                    binding.sixminTvCircleCount.text = usbTransferUtil.circleCount.toString()
                } else {
                    binding.sixminTvCircleCount.text = "- -"
                }

                Log.d("SixMinActivity", usbSerialData.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        binding.sixminRlMeasureBlood.setNoRepeatListener {
            if (usbTransferUtil.isConnectUSB) {
                if (usbTransferUtil.bloodPressureConnection) {
                    if (binding.sixminTvMeasureBlood.text == getString(R.string.sixmin_measure_blood)) {
                        SixMinCmdUtils.measureBloodPressure()
                    } else {
                        showMsg(getString(R.string.sixmin_test_measuring_blood))
                    }
                } else {
                    showMsg(getString(R.string.sixmin_test_blood_pressure_without_connection))
                }
            } else {
                showMsg(getString(R.string.sixmin_test_device_without_connection))
            }
        }

        binding.sixminRlStart.setNoRepeatListener {
            if (usbTransferUtil.isConnectUSB) {
                if (!usbTransferUtil.isBegin) {
                    startStepAndCircle()
                } else {
                    val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                        supportFragmentManager,
                        getString(R.string.sixmin_test_generate_report_tips)
                    )
                    startCommonDialogFragment.setCommonDialogOnClickListener(object :
                        CommonDialogFragment.CommonDialogClickListener {
                        override fun onPositiveClick() {
                            val startCommonDialogFragment2 =
                                CommonDialogFragment.startCommonDialogFragment(
                                    supportFragmentManager,
                                    "", "1", "1"
                                )
                            startCommonDialogFragment2.setCommonDialogOnClickListener(object :
                                CommonDialogFragment.CommonDialogClickListener {
                                override fun onPositiveClick() {

                                }

                                override fun onNegativeClick() {

                                }

                                override fun onStopNegativeClick(stopReason: String) {
                                    usbTransferUtil.isBegin = false
                                    usbTransferUtil.testType = 0
                                    SixMinCmdUtils.closeQSAndBS()
                                    binding.sixminTvStart.text = getString(R.string.sixmin_start)
                                    binding.sixminTvTestStatus.text =
                                        getString(R.string.sixmin_test_title)
//                                    binding.sixminTvStartMin.text = "0"
//                                    binding.sixminTvStartSec1.text = "0"
//                                    binding.sixminTvStartSec2.text = "0"
                                    mCountDownTime.cancel()
                                    mCountDownTime.setmTimes(360)
                                    usbTransferUtil.ignoreBlood = false

                                    val min = 5 - usbTransferUtil.min.toString().toInt()
                                    val sec =
                                        59 - (usbTransferUtil.sec1 + usbTransferUtil.sec2).toString()
                                            .toInt()
                                    val stopTime = "${min}分${sec}秒"

                                    sixMinReportBloodOther.stopOr = "1"
                                    sixMinReportBloodOther.stopReason = stopReason
                                    sixMinReportBloodOther.stopTime = stopTime

                                    generateReportData(
                                        usbTransferUtil.min,
                                        usbTransferUtil.sec1 + usbTransferUtil.sec2,
                                        1,
                                        min,
                                        sec
                                    )
                                }
                            })
                        }

                        override fun onNegativeClick() {
                            finish()
                        }

                        override fun onStopNegativeClick(stopReason: String) {

                        }
                    })
                }
            } else {
                showMsg(getString(R.string.sixmin_test_device_without_connection))
            }
        }

        binding.sixminIvClose.setNoRepeatListener {
            if (usbTransferUtil.isBegin) {
                exitType = "0"
                val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                    supportFragmentManager,
                    getString(R.string.sixmin_test_start_exit_test_tips)
                )
                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                    CommonDialogFragment.CommonDialogClickListener {
                    override fun onPositiveClick() {
                        if (exitType == "0") {
                            finish()
                        } else {
                            val intent =
                                Intent(this@SixMinActivity, SixMinSystemSettingActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onNegativeClick() {

                    }

                    override fun onStopNegativeClick(stopReason: String) {

                    }
                })
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

        binding.sixminIvSystemSetting.setNoRepeatListener {
            if (!usbTransferUtil.isBegin) {
                val intent = Intent(this, SixMinSystemSettingActivity::class.java)
                startActivity(intent)
            } else {
                exitType = "1"
//                if (!exitTestDialog.isShowing) {
//                    exitTestDialog.show()
//                }
                val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                    supportFragmentManager,
                    getString(R.string.sixmin_test_start_exit_test_tips)
                )
                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                    CommonDialogFragment.CommonDialogClickListener {
                    override fun onPositiveClick() {
                        usbTransferUtil.release()
                        if (exitType == "0") {
                            finish()
                        } else {
                            val intent =
                                Intent(this@SixMinActivity, SixMinSystemSettingActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onNegativeClick() {

                    }

                    override fun onStopNegativeClick(stopReason: String) {

                    }
                })
            }
        }
        binding.sixminIvCircleCountPlus.setNoRepeatListener {
            if (usbTransferUtil.isBegin) {
                usbTransferUtil.circleCount++
                binding.sixminTvCircleCount.text = usbTransferUtil.circleCount.toString()
            }
        }
        binding.sixminIvCircleCountMinus.setNoRepeatListener {
            if (usbTransferUtil.isBegin) {
                if (usbTransferUtil.circleCount == 0) {
                    return@setNoRepeatListener
                }
                usbTransferUtil.circleCount--
                binding.sixminTvCircleCount.text = usbTransferUtil.circleCount.toString()
            }
        }
        binding.sixminIvIgnoreBlood.setNoRepeatListener {
            if (usbTransferUtil.testType == 3) {
                if (!startRestoreEcgDialogFragment.isVisible) {
                    SixMinCollectRestoreEcgDialogFragment.startRestoreEcgDialogFragment(
                        supportFragmentManager,
                        timeRemain
                    )
                }
            } else {
                binding.sixminIvIgnoreBlood.setBackgroundResource(R.drawable.sixmin_ignore_blood_pressure_disable)
                usbTransferUtil.ignoreBlood = true
            }
        }
    }

    /**
     * 插入数据库
     */
    private fun generateReportData(min: String, sec: String, type: Int, min1: Int, sec1: Int) {
        try {
            if(sysSettingBean.sysOther.showResetTime == "1"){
                if(type == 1){
                    sixMinReportInfo.restDuration = (usbTransferUtil.restTime-1).toString()
                }else if(type == 0){
                    sixMinReportInfo.restDuration = usbTransferUtil.restTime.toString()
                }
            }else if(sysSettingBean.sysOther.showResetTime == "0"){
                sixMinReportInfo.restDuration = "-1"
            }
            sixMinReportEvaluation.turnsNumber = usbTransferUtil.circleCount.toString()
            sixMinReportEvaluation.totalWalk = usbTransferUtil.stepsStr

            usbTransferUtil.dealWalk(sixMinReportWalk)
            usbTransferUtil.dealBlood(
                sixMinReportBloodOxy,
                Gson().toJson(usbTransferUtil.bloodOxyLineData)
            )
            if (sysSettingBean.sysOther.circleCountType == "0") {
                usbTransferUtil.dealPreption(
                    sysSettingBean,
                    sixMinReportEvaluation,
                    min,
                    sec,
                    type,
                    min1,
                    sec1
                )
            } else {
                usbTransferUtil.dealPreptionSD(
                    sixMinReportEvaluation,
                    sysSettingBean,
                    min,
                    sec,
                    type,
                    min1,
                    sec1
                )
            }

            usbTransferUtil.dealStride(
                sixMinReportStride,
                BigDecimal(sixMinReportEvaluation.totalDistance),
                if (type == 0) 360 else min1 * 60 + sec1
            )

            //计算实际运动距离占预测的百分比
            val bd: BigDecimal =
                BigDecimal(sixMinReportEvaluation.totalDistance).divide(
                    BigDecimal(if (patientBean.infoBean.predictDistances == "") "1" else patientBean.infoBean.predictDistances),
                    2,
                    RoundingMode.HALF_UP
                )
            val bd2: BigDecimal = bd.multiply(BigDecimal(100)).setScale(0)
            sixMinReportEvaluation.accounted = bd2.toString()

            viewModel.setSixMinReportInfo(sixMinReportInfo)
            viewModel.setSixMinReportBloodOxyData(sixMinReportBloodOxy)
            viewModel.setSixMinReportWalkData(sixMinReportWalk)
            viewModel.setSixMinReportEvaluation(sixMinReportEvaluation)
            viewModel.setSixMinReportOther(sixMinReportBloodOther)
            viewModel.setSixMinReportPrescription(sixMinReportPrescription)
            viewModel.setSixMinReportBreathing(sixMinReportBreathing)
            viewModel.setSixMinReportHeartEcg(sixMinReportBloodHeartEcg)
            viewModel.setSixMinReportHeartBeat(sixMinReportBloodHeart)
            viewModel.setSixMinReportStride(sixMinReportStride)

            usbTransferUtil.release()

            jumpToPreReport()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 跳转预生成报告
     */
    private fun jumpToPreReport() {

        lifecycleScope.launch {
            kotlinx.coroutines.delay(0L)
            val intent = Intent(
                this@SixMinActivity,
                SixMinPreReportActivity::class.java
            )
            val bundle = Bundle()
            bundle.putString(Constants.sixMinPatientInfo, patientBean.infoBean.patientId.toString())
            bundle.putString(Constants.sixMinReportNo, sixMinReportInfo.reportNo)
            bundle.putString(Constants.sixMinReportType, "1")
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }
    }

    private fun startStepAndCircle() {
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val addTime = dateFormat.format(date)
        sixMinReportInfo.addTime = addTime

        usbTransferUtil.bloodType = 1
        usbTransferUtil.isBegin = true
        usbTransferUtil.circleBoolean = true
        usbTransferUtil.testType = 1
        binding.sixminTvStart.text = getString(R.string.sixmin_stop)
        binding.sixminTvTestStatus.text = getString(R.string.sixmin_test_testing)
        binding.sixminTvCircleCount.text = "0"
        binding.sixminIvIgnoreBlood.isEnabled = true
        binding.sixminRlStart.isEnabled = true
        binding.sixminRlMeasureBlood.isEnabled = true
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
                        binding.sixminTvStartSec1.text = second.toString().substring(0, 1)
                        binding.sixminTvStartSec2.text = second.toString().substring(1)
                    }

                    usbTransferUtil.min = binding.sixminTvStartMin.text.toString()
                    usbTransferUtil.sec1 = binding.sixminTvStartSec1.text.toString()
                    usbTransferUtil.sec2 = binding.sixminTvStartSec2.text.toString()

                    //识别每秒的步数，来采集休息时长
                    if (sysSettingBean.sysOther.showResetTime == "1") {
                        val bsStr: String = usbTransferUtil.stepsStr
                        if (bsStr == "0") {
                            ++usbTransferUtil.restTime
                        } else {
                            if (usbTransferUtil.checkBSStr.equals(bsStr)) {
                                ++usbTransferUtil.checkBSInd
                            } else {
                                usbTransferUtil.checkBSInd = 0
                            }
                            if (usbTransferUtil.checkBSInd > 5) {
                                if (usbTransferUtil.checkBSInd == 6) {
                                    usbTransferUtil.restTime = usbTransferUtil.restTime + 5
                                }
                                ++usbTransferUtil.restTime
                            }
                        }
                        usbTransferUtil.checkBSStr = bsStr
                    }
                    if (binding.sixminTvBloodOxygen.text != "---") {
                        usbTransferUtil.bloodListAvg.add(
                            binding.sixminTvBloodOxygen.text.toString().toInt()
                        )
                        usbTransferUtil.bloodAllListAvg.add(
                            binding.sixminTvBloodOxygen.text.toString().toInt()
                        )
                    }
                    if (second == 0) {
                        when (minute) {
                            5 -> {
                                if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                                    speechContent(getString(R.string.sixmin_test_start_count_down_five_tips))
                                }
                                sixMinReportWalk.walkOne = usbTransferUtil.stepsStr
                                usbTransferUtil.dealBloodBe(
                                    sixMinReportBloodOxy,
                                    minute,
                                    usbTransferUtil.bloodListAvg
                                )
                                usbTransferUtil.bloodListAvg.clear()
                            }

                            4 -> {
                                if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                                    speechContent(getString(R.string.sixmin_test_start_count_down_four_tips))
                                }
                                sixMinReportWalk.walkTwo =
                                    (usbTransferUtil.stepsStr.toInt() - sixMinReportWalk.walkOne.toInt()).toString()
                                usbTransferUtil.dealBloodBe(
                                    sixMinReportBloodOxy,
                                    minute,
                                    usbTransferUtil.bloodListAvg
                                )
                                usbTransferUtil.bloodListAvg.clear()
                            }

                            3 -> {
                                if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                                    speechContent(getString(R.string.sixmin_test_start_count_down_three_tips))
                                }
                                sixMinReportWalk.walkThree =
                                    (usbTransferUtil.stepsStr.toInt() - sixMinReportWalk.walkOne.toInt() - sixMinReportWalk.walkTwo.toInt()).toString()
                                usbTransferUtil.dealBloodBe(
                                    sixMinReportBloodOxy,
                                    minute,
                                    usbTransferUtil.bloodListAvg
                                )
                                usbTransferUtil.bloodListAvg.clear()
                            }

                            2 -> {
                                if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                                    speechContent(getString(R.string.sixmin_test_start_count_down_two_tips))
                                }
                                sixMinReportWalk.walkFour =
                                    (usbTransferUtil.stepsStr.toInt() - sixMinReportWalk.walkThree.toInt() - sixMinReportWalk.walkTwo.toInt() - sixMinReportWalk.walkOne.toInt()).toString()
                                usbTransferUtil.dealBloodBe(
                                    sixMinReportBloodOxy,
                                    minute,
                                    usbTransferUtil.bloodListAvg
                                )
                                usbTransferUtil.bloodListAvg.clear()
                            }

                            1 -> {
                                if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                                    speechContent(getString(R.string.sixmin_test_start_count_down_one_tips))
                                }
                                sixMinReportWalk.walkFive =
                                    (usbTransferUtil.stepsStr.toInt() - sixMinReportWalk.walkFour.toInt() - sixMinReportWalk.walkThree.toInt() - sixMinReportWalk.walkTwo.toInt() - sixMinReportWalk.walkOne.toInt()).toString()
                                usbTransferUtil.dealBloodBe(
                                    sixMinReportBloodOxy,
                                    minute,
                                    usbTransferUtil.bloodListAvg
                                )
                                usbTransferUtil.bloodListAvg.clear()
                            }
                        }
                    }

                    if (minute == 0 && second == 16) {
                        if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                            speechContent(getString(R.string.sixmin_test_start_count_down_zero_tips))
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFinish() {
                usbTransferUtil.testType = 2
                sixMinReportWalk.walkSix =
                    (usbTransferUtil.stepsStr.toInt() - sixMinReportWalk.walkFive.toInt() - sixMinReportWalk.walkFour.toInt() - sixMinReportWalk.walkThree.toInt() - sixMinReportWalk.walkTwo.toInt() - sixMinReportWalk.walkOne.toInt()).toString()
                usbTransferUtil.dealBloodBe(
                    sixMinReportBloodOxy,
                    0,
                    usbTransferUtil.bloodListAvg
                )
                usbTransferUtil.bloodListAvg.clear()

                if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                    speechContent(getString(R.string.sixmin_test_start_timeout))
                }
                binding.sixminRlMeasureBlood.isEnabled = false
                binding.sixminRlStart.isEnabled = false
                usbTransferUtil.circleBoolean = false
//                usbTransferUtil.isBegin = false
                usbTransferUtil.bloodType = 2
                SixMinCmdUtils.closeQSAndBS()

//                binding.sixminTvStart.text = getString(R.string.sixmin_start)
                binding.sixminTvTestStatus.text = getString(R.string.sixmin_test_title)
                binding.sixminTvStartMin.text = "0"
                binding.sixminTvStartSec1.text = "0"
                binding.sixminTvStartSec2.text = "0"
                mCountDownTimeThree.cancel()
                mCountDownTime.setmTimes(360)
                mGetSportHeartEcgCountDownTime.start(object : FixCountDownTime.OnTimerCallBack {
                    override fun onStart() {
                        usbTransferUtil.testType = 3
                        startRestoreEcgDialogFragment =
                            SixMinCollectRestoreEcgDialogFragment.startRestoreEcgDialogFragment(
                                supportFragmentManager,
                                timeRemain
                            )
                    }


                    override fun onTick(times: Int) {
                        timeRemain = times.toString()
                        LiveDataBus.get().with("simMinRestore").postValue(timeRemain)
                    }

                    override fun onFinish() {
                        startRestoreEcgDialogFragment.dismiss()
                        if (!usbTransferUtil.ignoreBlood) {
                            if (sysSettingBean.sysOther.autoMeasureBlood == "1") {
                                lifecycleScope.launch {
                                    kotlinx.coroutines.delay(1000L)
                                    binding.sixminTvBloodPressureHigh.text = "---"
                                    binding.sixminTvBloodPressureLow.text = "---"
                                    if (usbTransferUtil.bloodPressureConnection) {
                                        if (binding.sixminTvMeasureBlood.text == getString(R.string.sixmin_measure_blood)) {
                                            SixMinCmdUtils.measureBloodPressure()
                                        } else {
                                            showMsg(getString(R.string.sixmin_test_measuring_blood))
                                        }
                                    } else {
                                        showMsg(getString(R.string.sixmin_test_blood_pressure_without_connection))
                                    }
                                }
                            } else {
                                binding.sixminRlMeasureBlood.isEnabled = true
                                showMsg("请点击测量运动后血压")
                            }
                        } else {
                            val startCommonDialogFragment =
                                CommonDialogFragment.startCommonDialogFragment(
                                    supportFragmentManager,
                                    "", "1", "1"
                                )
                            startCommonDialogFragment.setCommonDialogOnClickListener(
                                object :
                                    CommonDialogFragment.CommonDialogClickListener {
                                    override fun onPositiveClick() {

                                    }

                                    override fun onNegativeClick() {

                                    }

                                    override fun onStopNegativeClick(stopReason: String) {
                                        usbTransferUtil.isBegin = false
                                        usbTransferUtil.testType = 0
                                        SixMinCmdUtils.closeQSAndBS()
                                        binding.sixminTvStart.text =
                                            getString(R.string.sixmin_start)
                                        binding.sixminTvTestStatus.text =
                                            getString(R.string.sixmin_test_title)
//                                        binding.sixminTvStartMin.text = "0"
//                                        binding.sixminTvStartSec1.text = "0"
//                                        binding.sixminTvStartSec2.text = "0"
                                        mCountDownTime.cancel()
                                        mCountDownTime.setmTimes(360)
                                        usbTransferUtil.ignoreBlood = false

                                        sixMinReportBloodOther.badOr = "1"
                                        sixMinReportBloodOther.badSymptoms = stopReason
                                        generateReportData(
                                            usbTransferUtil.min,
                                            usbTransferUtil.sec1 + usbTransferUtil.sec2,
                                            0,
                                            0,
                                            0
                                        )
                                    }
                                })
                        }
                    }
                })
            }
        })
        mCountDownTimeThree.start()
    }

    private fun startTest() {
        if (!usbTransferUtil.isBegin) {
//            if (!selfCheck) {
//                val selfCheckBeforeTestDialogFragment =
//                    SixMinReportSelfCheckBeforeTestFragment.startPatientSelfCheckDialogFragment(
//                        supportFragmentManager, "1", "1"
//                    )
//                selfCheckBeforeTestDialogFragment.setSelfCheckBeforeTestDialogOnClickListener(object :
//                    SixMinReportSelfCheckBeforeTestFragment.SixMinReportSelfCheckBeforeTestDialogListener {
//                    override fun onClickConfirm(befoFatigueLevel: Int, befoBreathingLevel: Int) {
//                        Log.d("tag", "$befoFatigueLevel$befoBreathingLevel")
//                        selfCheck = true
//                        sixMinReportEvaluation.befoFatigueLevel = befoBreathingLevel
//                        sixMinReportEvaluation.befoBreathingLevel = befoBreathingLevel
//                        //是否自动测量血压
//                        if (sysSettingBean.sysOther.autoMeasureBlood == "1") {
//                            //是否播报语音
//                            if (sysSettingBean.sysOther.broadcastVoice == "1") {
//                                measureBloodUtteranceId = System.currentTimeMillis().toString()
//                                speechContent(
//                                    getString(R.string.sixmin_test_start_measure_blood_front),
//                                    measureBloodUtteranceId
//                                )
//                            } else {
//                                if (usbTransferUtil.bloodPressureConnection) {
//                                    if (binding.sixminTvMeasureBlood.text == getString(R.string.sixmin_measure_blood)) {
//                                        SixMinCmdUtils.measureBloodPressure()
//                                    } else {
//                                        Toast.makeText(
//                                            this@SixMinActivity,
//                                            getString(R.string.sixmin_test_measuring_blood),
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                } else {
//                                    Toast.makeText(
//                                        this@SixMinActivity,
//                                        getString(R.string.sixmin_test_blood_pressure_without_connection),
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                            }
//                        } else {
////                if (sysSettingBean.sysOther.broadcastVoice == "1") {
////                    startTestUtteranceId = System.currentTimeMillis().toString()
////                    speechContent(
////                        getString(R.string.sixmin_test_start_test),
////                        startTestUtteranceId
////                    )
////                } else {
////                    startStepAndCircle()
////                }
//                            showMsg("请点击测量运动前血压")
//                        }
//                    }
//
//                    override fun onClickClose() {
//
//                    }
//                })
//            } else {
//                if (sysSettingBean.sysOther.autoMeasureBlood == "1") {
//                    //是否播报语音
//                    if (sysSettingBean.sysOther.broadcastVoice == "1") {
//                        measureBloodUtteranceId = System.currentTimeMillis().toString()
//                        speechContent(
//                            getString(R.string.sixmin_test_start_measure_blood_front),
//                            measureBloodUtteranceId
//                        )
//                    } else {
//                        if (usbTransferUtil.bloodPressureConnection) {
//                            if (binding.sixminTvMeasureBlood.text == getString(R.string.sixmin_measure_blood)) {
//                                SixMinCmdUtils.measureBloodPressure()
//                            } else {
//                                Toast.makeText(
//                                    this@SixMinActivity,
//                                    getString(R.string.sixmin_test_measuring_blood),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        } else {
//                            Toast.makeText(
//                                this@SixMinActivity,
//                                getString(R.string.sixmin_test_blood_pressure_without_connection),
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                } else {
//                    showMsg("请点击测量运动前血压")
//                }
//            }
            if (sysSettingBean.sysOther.autoMeasureBlood == "1") {
                //是否播报语音
                if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                    measureBloodUtteranceId = System.currentTimeMillis().toString()
                    speechContent(
                        getString(R.string.sixmin_test_start_measure_blood_front),
                        measureBloodUtteranceId
                    )
                } else {
                    if (usbTransferUtil.bloodPressureConnection) {
                        if (binding.sixminTvMeasureBlood.text == getString(R.string.sixmin_measure_blood)) {
                            SixMinCmdUtils.measureBloodPressure()
                        } else {
                            showMsg(getString(R.string.sixmin_test_measuring_blood))
                        }
                    } else {
                        binding.sixminRlMeasureBlood.isEnabled = true
                        binding.sixminRlStart.isEnabled = true
                        binding.sixminIvIgnoreBlood.isEnabled = true
                        showMsg(getString(R.string.sixmin_test_blood_pressure_without_connection))
                    }
                }
            } else {
                binding.sixminRlMeasureBlood.isEnabled = true
                binding.sixminRlStart.isEnabled = true
                binding.sixminIvIgnoreBlood.isEnabled = true
                showMsg("请点击测量运动前血压")
            }
        }
    }

    private fun beanQuery(any: Any) {
        try {
            if (any is List<*>) {
                val datas = any as MutableList<*>
                if (testPatientId.isNotEmpty()) {
                    datas.forEach {
                        val patientInfoBean = it as PatientInfoBean
                        if (patientInfoBean.infoBean.patientId.toString() == testPatientId) {
                            patientBean = patientInfoBean
                        }
                    }
                } else {
                    patientBean = datas[0] as PatientInfoBean
                }
                binding.sixminTvTestPatientName.text = patientBean.infoBean.name
                binding.sixminTvTestPatientSex.text = patientBean.infoBean.sex
                binding.sixminTvTestPatientAge.text = patientBean.infoBean.age
                binding.sixminTvTestPatientHight.text = patientBean.infoBean.height
                binding.sixminTvTestPatientWeight.text = patientBean.infoBean.weight
                binding.sixminTvTestPatientBmi.text = patientBean.infoBean.BMI

                initPatientInfo()
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
            description.setPosition((screenWidth / 2).toFloat() + 140, 23f)
            description.textSize = 11f
            description?.apply {
                text =
                    if (type == 1) getString(R.string.sixmin_test_start_blood_oxygen_trend) else getString(
                        R.string.sixmin_test_start_heart_beat_trend
                    )
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
                            showMsg("复制成功")
                        }

                        override fun onFailed(error: String?) {
                            showMsg("复制失败")
                        }
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initCountDownTimerExt() {
        mCountDownTime = object : FixCountDownTime(360, 1000) {}
        mCountDownTimeThree = object : CountDownTimer(1080000, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                try {
                    if (usbTransferUtil.bloodOxygenConnection) {
                        val mapBloodOxygen = usbTransferUtil.mapBloodOxygen
                        if (mapBloodOxygen.isNotEmpty()) {
                            val value = mapBloodOxygen.entries.last().value
                            addEntryData(value.toFloat(), (millisUntilFinished / 1000).toInt())
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFinish() {

            }
        }
        mStartTestCountDownTime = object : FixCountDownTime(5, 1000) {}
        mGetSportHeartEcgCountDownTime = object : FixCountDownTime(60, 1000) {}
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
                            if (usbTransferUtil.bloodPressureConnection) {
                                if (binding.sixminTvMeasureBlood.text == getString(R.string.sixmin_measure_blood)) {
                                    SixMinCmdUtils.measureBloodPressure()
                                } else {
                                    showMsg(getString(R.string.sixmin_test_measuring_blood))
                                }
                                binding.sixminRlStart.isEnabled = true
                                binding.sixminRlMeasureBlood.isEnabled = true
                                binding.sixminIvIgnoreBlood.isEnabled = true
                            } else {
                                showMsg(getString(R.string.sixmin_test_blood_pressure_without_connection))
                            }
                        } else if (startTestUtteranceId == defaultUtteranceId) {
                            autoStartTest()
                        } else if (stepsAndCircleUtteranceId == defaultUtteranceId) {
                            startStepAndCircle()
                        } else if (bloodBeforeUtteranceId == defaultUtteranceId) {
                            val startCommonDialogFragment =
                                CommonDialogFragment.startCommonDialogFragment(
                                    supportFragmentManager,
                                    getString(R.string.sixmin_test_start_use_this_blood_pressure_front)
                                )
                            startCommonDialogFragment.setCommonDialogOnClickListener(object :
                                CommonDialogFragment.CommonDialogClickListener {
                                override fun onPositiveClick() {
                                    usbTransferUtil.bloodType = 1
                                    if (sysSettingBean.sysOther.autoStart == "1") {
                                        if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                                            startTestUtteranceId =
                                                System.currentTimeMillis().toString()
                                            speechContent(
                                                getString(R.string.sixmin_test_start_test),
                                                startTestUtteranceId
                                            )
                                        } else {
                                            autoStartTest()
                                        }
                                    }
                                }

                                override fun onNegativeClick() {
                                    usbTransferUtil.bloodType = 1
                                }

                                override fun onStopNegativeClick(stopReason: String) {

                                }
                            })
                        } else if (bloodEndUtteranceId == defaultUtteranceId) {
                            val startCommonDialogFragment =
                                CommonDialogFragment.startCommonDialogFragment(
                                    supportFragmentManager,
                                    getString(R.string.sixmin_test_start_use_this_blood_pressure_behind)
                                )
                            startCommonDialogFragment.setCommonDialogOnClickListener(object :
                                CommonDialogFragment.CommonDialogClickListener {
                                override fun onPositiveClick() {
                                    usbTransferUtil.bloodType = 0
                                    usbTransferUtil.isBegin = false
                                    usbTransferUtil.testType = 0
                                    SixMinCmdUtils.closeQSAndBS()
                                    binding.sixminTvStart.text =
                                        getString(R.string.sixmin_start)
                                    binding.sixminTvTestStatus.text =
                                        getString(R.string.sixmin_test_title)
//                                    binding.sixminTvStartMin.text = "0"
//                                    binding.sixminTvStartSec1.text = "0"
//                                    binding.sixminTvStartSec2.text = "0"
                                    mCountDownTime.cancel()
                                    mCountDownTime.setmTimes(360)
                                    usbTransferUtil.ignoreBlood = false

                                    generateReportData(
                                        usbTransferUtil.min,
                                        usbTransferUtil.sec1 + usbTransferUtil.sec2,
                                        0,
                                        0,
                                        0
                                    )
                                }

                                override fun onNegativeClick() {
                                    usbTransferUtil.bloodType = 0
                                    usbTransferUtil.isBegin = false
                                    usbTransferUtil.testType = 0
                                    SixMinCmdUtils.closeQSAndBS()
                                    binding.sixminTvStart.text =
                                        getString(R.string.sixmin_start)
                                    binding.sixminTvTestStatus.text =
                                        getString(R.string.sixmin_test_title)
//                                    binding.sixminTvStartMin.text = "0"
//                                    binding.sixminTvStartSec1.text = "0"
//                                    binding.sixminTvStartSec2.text = "0"
                                    mCountDownTime.cancel()
                                    mCountDownTime.setmTimes(360)
                                    usbTransferUtil.ignoreBlood = false

                                    generateReportData(
                                        usbTransferUtil.min,
                                        usbTransferUtil.sec1 + usbTransferUtil.sec2,
                                        0,
                                        0,
                                        0
                                    )
                                }

                                override fun onStopNegativeClick(stopReason: String) {

                                }
                            })
                        }
                    }
                }

                override fun onError(utteranceId: String?) {
                    Log.d(TAG, "onError: $utteranceId")
                }
            })

            showGuideDialog()
        }
    }

    private fun speechContent(
        content: String, utteranceId: String = System.currentTimeMillis().toString()
    ) {
        textToSpeech.speak(content, TextToSpeech.QUEUE_ADD, null, utteranceId)
    }

    /**
     * 测量完运动前血压自动开始试验
     */
    private fun autoStartTest() {
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
                if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                    speechContent(times.toString())
                }
                lifecycleScope.launch {
                    kotlinx.coroutines.delay(if (sysSettingBean.sysOther.broadcastVoice == "1") 1200L else 500L)
                    binding.sixminIvCountdownTime.setImageResource(imageId)
                }
            }

            override fun onFinish() {
                lifecycleScope.launch {
                    kotlinx.coroutines.delay(1000L)
                    binding.sixminIvCountdownTime.visibility = View.GONE
                    if (sysSettingBean.sysOther.broadcastVoice == "1" || sysSettingBean.sysOther.broadcastVoice == "0") {
                        stepsAndCircleUtteranceId =
                            System.currentTimeMillis().toString()
                        speechContent("请开始步行。", stepsAndCircleUtteranceId)
                    } else {
                        startStepAndCircle()
                    }
                }
            }
        })
    }

    override fun onResume() {
        if (!usbTransferUtil.isConnectUSB) {
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
        binding.sixminRlStart.isEnabled = true
        binding.sixminRlMeasureBlood.isEnabled = true
        binding.sixminIvIgnoreBlood.isEnabled = true
        usbTransferUtil.isBegin = false
        usbTransferUtil.testType = 0
        usbTransferUtil.bloodType = 0
        SixMinCmdUtils.closeQSAndBS()
        textToSpeech.stop()
        textToSpeech.shutdown()
        mCountDownTime.cancel()
        mCountDownTimeThree.cancel()
        mStartTestCountDownTime.cancel()
    }

}
