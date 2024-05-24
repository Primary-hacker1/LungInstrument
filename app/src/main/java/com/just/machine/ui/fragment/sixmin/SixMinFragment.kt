package com.just.machine.ui.fragment.sixmin

import android.graphics.Color
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.viewmodel.LiveDataEvent
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.just.machine.model.BloodOxyLineEntryBean
import com.just.machine.model.Constants
import com.just.machine.model.PatientInfoBean
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.UsbSerialData
import com.just.machine.model.systemsetting.SixMinSysSettingBean
import com.just.machine.ui.activity.SixMinDetectActivity
import com.just.machine.ui.dialog.CommonDialogFragment
import com.just.machine.ui.dialog.SixMinCollectRestoreEcgDialogFragment
import com.just.machine.ui.dialog.SixMinGuideDialogFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.ECGDataParse
import com.just.machine.util.FixCountDownTime
import com.just.machine.util.LiveDataBus
import com.just.machine.util.MyLineChartRenderer
import com.just.machine.util.ScreenUtils
import com.just.machine.util.SixMinCmdUtils
import com.just.news.R
import com.just.news.databinding.FragmentSixminBinding
import com.seeker.luckychart.annotation.UIMode
import com.seeker.luckychart.model.ECGPointValue
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * 6分钟试验界面
 */
@AndroidEntryPoint
class SixMinFragment : CommonBaseFragment<FragmentSixminBinding>(), TextToSpeech.OnInitListener {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var mActivity: SixMinDetectActivity
    private lateinit var mCountDownTime: FixCountDownTime //试验6分钟倒计时
    private lateinit var mCountDownTimeThree: CountDownTimer//每3秒一个值获取心率，血氧
    private lateinit var mStartTestCountDownTime: FixCountDownTime//6分钟试验倒计时
    private lateinit var mGetSportHeartEcgCountDownTime: FixCountDownTime//采集运动心率倒计时
    private lateinit var bloodOxyDataSet: LineDataSet
    private lateinit var heartBeatDataSet: LineDataSet

    private lateinit var usbSerialData: UsbSerialData
    private var notShowAnymore = false
    private lateinit var startRestoreEcgDialogFragment: SixMinCollectRestoreEcgDialogFragment
    private var timeRemain = "" //采集恢复心电倒计时

    private var measureBloodUtteranceId = "" //测量血压语音播报标识
    private var startTestUtteranceId = "" //开始训练语音播报标识
    private var stepsAndCircleUtteranceId = "" //开始步行和计圈语音播报标识
    private var bloodBeforeUtteranceId = "" //运动前血压语音播报标识
    private var bloodEndUtteranceId = "" //运动后血压语音播报标识
    private var defaultUtteranceId = ""//语音播报开始标识

    private lateinit var mValues: Array<Array<ECGPointValue>?>
    private var index = 0
    private var ready = false

    override fun loadData() {//懒加载

    }


    override fun initView() {
        if (activity is SixMinDetectActivity) {
            mActivity = activity as SixMinDetectActivity
        }
        textToSpeech = TextToSpeech(mActivity.applicationContext, this)
        binding.sixminIvIgnoreBlood.isEnabled = false
        binding.sixminRlStart.isEnabled = false
        binding.sixminRlMeasureBlood.isEnabled = false
        initLineChart(binding.sixminLineChartBloodOxygen, 1)
        initLineChart(binding.sixminLineChartHeartBeat, 2)
        initCountDownTimerExt()
        initEcgChart()
        viewModel.getPatients()
    }

    private fun initEcgChart() {
        try {
            binding.sixminEcg.setMode(UIMode.ERASE)
            binding.sixminEcg.initDefaultChartData(false, false)
            binding.sixminEcg.setFrameRenderCallback {
                if (!ready || binding.sixminEcg.chartData == null) {
                    return@setFrameRenderCallback
                }
                val count = 4
                if (index + count < mValues[0]!!.size) {
                    val lineCount: Int = binding.sixminEcg.ecgRenderStrategy.ecgLineCount
                    val values: Array<Array<ECGPointValue>?> = arrayOfNulls(lineCount)
                    for (i in 0 until lineCount) {
                        val value =
                            mValues[i]!!.copyOfRange(index, index + count)
                        values[i] = value
                    }
                    binding.sixminEcg.updatePointsToRender(*values)
                    index += count
                }
            }
            lifecycleScope.launch(Dispatchers.Default) {
                val count: Int = binding.sixminEcg.ecgRenderStrategy.ecgLineCount
                mValues = arrayOfNulls(count)
                for (i in 0 until count) {
                    val dataParse = ECGDataParse(mActivity)
                    mValues[i] = dataParse.values
                }
                ready = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun initListener() {
        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.QuerySuccess -> {
                    it.any?.let { it1 -> beanQuery(it1) }
                }
            }
        }

        LiveDataBus.get().with(Constants.sixMinLiveDataBusKey).observe(this, Observer {
            try {
                usbSerialData = Gson().fromJson(it.toString(), UsbSerialData::class.java)
                if (mActivity.usbTransferUtil.ecgConnection) {
                    mActivity.setEcgStatus(R.mipmap.xinlvyes)
                } else {
                    mActivity.setEcgStatus(R.mipmap.xinlvno)
                }
                if (mActivity.usbTransferUtil.bloodPressureConnection) {
                    mActivity.setBloodPressureStatus(R.mipmap.xueyayes)
                } else {
                    mActivity.setBloodPressureStatus(R.mipmap.xueyano)
                }
                if (mActivity.usbTransferUtil.bloodOxygenConnection) {
                    mActivity.setBloodOxyStatus(R.mipmap.xueyangyes)
                } else {
                    mActivity.setBloodOxyStatus(R.mipmap.xueyangno)
                }
                //电池状态
                when (mActivity.usbTransferUtil.batteryLevel) {
                    1 -> {
                        mActivity.setBatterStatus(R.mipmap.dianchi00)
                    }

                    2 -> {
                        mActivity.setBatterStatus(R.mipmap.dianchi1)
                    }

                    3 -> {
                        mActivity.setBatterStatus(R.mipmap.dianchi2)
                    }

                    4 -> {
                        mActivity.setBatterStatus(R.mipmap.dianchi3)
                    }

                    5 -> {
                        mActivity.setBatterStatus(R.mipmap.dianchi4)
                    }

                    else -> {
                        mActivity.setBatterStatus(R.mipmap.dianchi00)
                    }
                }
                //血压数据
                when (usbSerialData.bloodState) {
                    "未测量血压" -> {
                        binding.sixminTvMeasureBlood.text = getString(R.string.sixmin_measure_blood)
                    }

                    "测量血压中" -> {
                        binding.sixminTvBloodPressureHigh.setTextColor(
                            ContextCompat.getColor(
                                mActivity, R.color.white
                            )
                        )
                        binding.sixminTvBloodPressureLow.setTextColor(
                            ContextCompat.getColor(
                                mActivity, R.color.white
                            )
                        )
                        usbSerialData.bloodLow = "---"
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
                        if (mActivity.usbTransferUtil.bloodType == 2 && mActivity.usbTransferUtil.ignoreBlood) {
                            val startCommonDialogFragment =
                                CommonDialogFragment.startCommonDialogFragment(
                                    mActivity.supportFragmentManager,
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
                                        mActivity.usbTransferUtil.isBegin = false
                                        mActivity.usbTransferUtil.testType = 0
                                        SixMinCmdUtils.closeQSAndBS()
                                        binding.sixminTvStart.text =
                                            getString(R.string.sixmin_start)
                                        mActivity.setToolbarTitle(getString(R.string.sixmin_test_title))
                                        mCountDownTime.cancel()
                                        mCountDownTime.setmTimes(360)

                                        mActivity.sixMinReportBloodOther.badOr = "1"
                                        mActivity.sixMinReportBloodOther.badSymptoms = stopReason

                                        generateReportData(
                                            mActivity.usbTransferUtil.min,
                                            mActivity.usbTransferUtil.sec1 + mActivity.usbTransferUtil.sec2,
                                            0,
                                            0,
                                            0
                                        )
                                    }
                                })
                            mActivity.usbTransferUtil.ignoreBlood = false
                        }
                    }

                    "测量血压成功" -> {
                        binding.sixminTvMeasureBlood.text = getString(R.string.sixmin_measure_blood)
                        if (usbSerialData.bloodHigh.toInt() > mActivity.sysSettingBean.sysAlarm.highPressure.toInt()) {
                            binding.sixminTvBloodPressureHigh.setTextColor(
                                ContextCompat.getColor(
                                    mActivity, R.color.red
                                )
                            )
                        } else {
                            binding.sixminTvBloodPressureHigh.setTextColor(
                                ContextCompat.getColor(
                                    mActivity, R.color.white
                                )
                            )
                        }
                        if (usbSerialData.bloodLow.toInt() > mActivity.sysSettingBean.sysAlarm.lowPressure.toInt()) {
                            binding.sixminTvBloodPressureLow.setTextColor(
                                ContextCompat.getColor(
                                    mActivity, R.color.red
                                )
                            )
                        } else {
                            binding.sixminTvBloodPressureLow.setTextColor(
                                ContextCompat.getColor(
                                    mActivity, R.color.white
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
                        if (mActivity.usbTransferUtil.bloodType == 0) {
                            binding.sixminTvBloodPressureHighFront.text =
                                usbSerialData.bloodHighFront ?: "---"
                            binding.sixminTvBloodPressureLowFront.text =
                                usbSerialData.bloodLowFront ?: "---"
                            mActivity.sixMinReportBloodOther.startHighPressure =
                                usbSerialData.bloodHighFront
                            mActivity.sixMinReportBloodOther.startLowPressure =
                                usbSerialData.bloodLowFront
                            if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
                                bloodBeforeUtteranceId = System.currentTimeMillis().toString()
                                speechContent(str, bloodBeforeUtteranceId)
                            } else {
                                val startCommonDialogFragment =
                                    CommonDialogFragment.startCommonDialogFragment(
                                        mActivity.supportFragmentManager,
                                        getString(R.string.sixmin_test_start_use_this_blood_pressure_front)
                                    )
                                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                                    CommonDialogFragment.CommonDialogClickListener {
                                    override fun onPositiveClick() {
                                        mActivity.usbTransferUtil.bloodType = 1
                                        if (mActivity.sysSettingBean.sysOther.autoStart == "1") {
                                            if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
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
                                        mActivity.usbTransferUtil.bloodType = 1
                                    }

                                    override fun onStopNegativeClick(stopReason: String) {

                                    }
                                })
                            }
                        } else if (mActivity.usbTransferUtil.bloodType == 2) {
                            binding.sixminTvBloodPressureHighBehind.text =
                                usbSerialData.bloodHighBehind ?: "---"
                            binding.sixminTvBloodPressureLowBehind.text =
                                usbSerialData.bloodLowBehind ?: "---"
                            mActivity.sixMinReportBloodOther.stopHighPressure =
                                usbSerialData.bloodHighBehind
                            mActivity.sixMinReportBloodOther.stopLowPressure =
                                usbSerialData.bloodLowBehind
                            bloodEndUtteranceId = System.currentTimeMillis().toString()
                            if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
                                speechContent(str, bloodEndUtteranceId)
                            } else {
                                val startCommonDialogFragment =
                                    CommonDialogFragment.startCommonDialogFragment(
                                        mActivity.supportFragmentManager,
                                        getString(R.string.sixmin_test_start_use_this_blood_pressure_behind)
                                    )
                                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                                    CommonDialogFragment.CommonDialogClickListener {
                                    override fun onPositiveClick() {
                                        mActivity.usbTransferUtil.bloodType = 0
                                        mActivity.sixMinReportBloodOther.stopHighPressure =
                                            usbSerialData.bloodHighBehind
                                        mActivity.sixMinReportBloodOther.stopLowPressure =
                                            usbSerialData.bloodLowBehind
                                        mActivity.usbTransferUtil.isBegin = false
                                        mActivity.usbTransferUtil.testType = 0
                                        SixMinCmdUtils.closeQSAndBS()
                                        binding.sixminTvStart.text =
                                            getString(R.string.sixmin_start)
                                        mActivity.setToolbarTitle(getString(R.string.sixmin_test_title))
//                                        binding.sixminTvStartMin.text = "0"
//                                        binding.sixminTvStartSec1.text = "0"
//                                        binding.sixminTvStartSec2.text = "0"
                                        mCountDownTime.cancel()
                                        mCountDownTime.setmTimes(360)
                                        mActivity.usbTransferUtil.ignoreBlood = false

                                        mActivity.sixMinReportBloodOther.badOr = "0"
                                        generateReportData(
                                            mActivity.usbTransferUtil.min,
                                            mActivity.usbTransferUtil.sec1 + mActivity.usbTransferUtil.sec2,
                                            0,
                                            0,
                                            0
                                        )
                                    }

                                    override fun onNegativeClick() {
                                        mActivity.usbTransferUtil.bloodType = 0
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
                    if (usbSerialData.bloodOxygen.toInt() < mActivity.sysSettingBean.sysAlarm.bloodOxy.toInt()) {
                        binding.sixminTvBloodOxygen.setTextColor(
                            ContextCompat.getColor(
                                mActivity, R.color.red
                            )
                        )
                    } else {
                        binding.sixminTvBloodOxygen.setTextColor(
                            ContextCompat.getColor(
                                mActivity, R.color.colorWhite
                            )
                        )
                    }
                    binding.sixminTvBloodOxygen.text = usbSerialData.bloodOxygen
                } else {
                    binding.sixminTvBloodOxygen.text = "--"
                    if (mActivity.usbTransferUtil.bloodOxygenConnection) {
                        binding.sixminTvBloodOxygen.setTextColor(
                            ContextCompat.getColor(
                                mActivity, R.color.red
                            )
                        )
                    } else {
                        binding.sixminTvBloodOxygen.setTextColor(
                            ContextCompat.getColor(
                                mActivity, R.color.colorWhite
                            )
                        )
                    }
                }
                //圈数数据
                if (mActivity.usbTransferUtil.isBegin) {
                    binding.sixminTvCircleCount.text =
                        mActivity.usbTransferUtil.circleCount.toString()
                } else {
                    binding.sixminTvCircleCount.text = "- -"
                }

                Log.d("SixMinActivity", usbSerialData.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        binding.sixminIvSystemSetting.setOnClickListener {
            if (!mActivity.usbTransferUtil.isBegin) {
                navigate(it, R.id.sixMinSystemSettingFragment)//fragment跳转
            } else {
                val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                    mActivity.supportFragmentManager,
                    getString(R.string.sixmin_test_start_exit_test_tips)
                )
                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                    CommonDialogFragment.CommonDialogClickListener {
                    override fun onPositiveClick() {
                        mActivity.usbTransferUtil.release()
                        navigate(it, R.id.sixMinSystemSettingFragment)//fragment跳转
                    }

                    override fun onNegativeClick() {

                    }

                    override fun onStopNegativeClick(stopReason: String) {

                    }
                })
            }
        }

        binding.sixminRlMeasureBlood.setNoRepeatListener {
            if (mActivity.usbTransferUtil.isConnectUSB && mActivity.usbTransferUtil.ecgConnection && mActivity.usbTransferUtil.bloodOxygenConnection) {
                if (mActivity.usbTransferUtil.bloodPressureConnection) {
                    if (binding.sixminTvMeasureBlood.text == getString(R.string.sixmin_measure_blood)) {
                        SixMinCmdUtils.measureBloodPressure()
                    } else {
                        mActivity.showMsg(getString(R.string.sixmin_test_measuring_blood))
                    }
                } else {
                    binding.sixminRlMeasureBlood.isEnabled = true
                    mActivity.showMsg(getString(R.string.sixmin_test_blood_pressure_without_connection))
                }
            } else {
                mActivity.showMsg(getString(R.string.sixmin_test_device_without_connection))
            }
        }

        binding.sixminRlStart.setNoRepeatListener {
            if (mActivity.usbTransferUtil.isConnectUSB && mActivity.usbTransferUtil.ecgConnection && mActivity.usbTransferUtil.bloodOxygenConnection && mActivity.usbTransferUtil.bloodPressureConnection) {
                if (!mActivity.usbTransferUtil.isBegin) {
                    if (mActivity.sysSettingBean.sysOther.autoMeasureBlood == "0") {
                        if (binding.sixminTvMeasureBlood.text.toString()
                                .trim() == getString(R.string.sixmin_measuring_blood)
                        ) {
                            mActivity.showMsg(getString(R.string.sixmin_test_measuring_blood))
                        } else {
                            if (mActivity.usbTransferUtil.bloodType == 1) {
                                autoStartTest()
                            } else {
                                val startCommonDialogFragment =
                                    CommonDialogFragment.startCommonDialogFragment(
                                        mActivity.supportFragmentManager,
                                        "还未测量运动前血压，是否开始试验？"
                                    )
                                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                                    CommonDialogFragment.CommonDialogClickListener {
                                    override fun onPositiveClick() {
                                        autoStartTest()
                                    }

                                    override fun onNegativeClick() {

                                    }

                                    override fun onStopNegativeClick(stopReason: String) {

                                    }
                                })
                            }
                        }
                    }
                } else {
                    val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                        mActivity.supportFragmentManager,
                        getString(R.string.sixmin_test_generate_report_tips)
                    )
                    startCommonDialogFragment.setCommonDialogOnClickListener(object :
                        CommonDialogFragment.CommonDialogClickListener {
                        override fun onPositiveClick() {
                            val startCommonDialogFragment2 =
                                CommonDialogFragment.startCommonDialogFragment(
                                    mActivity.supportFragmentManager,
                                    "", "1", "1"
                                )
                            startCommonDialogFragment2.setCommonDialogOnClickListener(object :
                                CommonDialogFragment.CommonDialogClickListener {
                                override fun onPositiveClick() {

                                }

                                override fun onNegativeClick() {

                                }

                                override fun onStopNegativeClick(stopReason: String) {
                                    mActivity.usbTransferUtil.isBegin = false
                                    mActivity.usbTransferUtil.testType = 0
                                    SixMinCmdUtils.closeQSAndBS()
                                    binding.sixminTvStart.text = getString(R.string.sixmin_start)
                                    mActivity.setToolbarTitle(getString(R.string.sixmin_test_title))
                                    mCountDownTime.cancel()
                                    mCountDownTime.setmTimes(360)
                                    mActivity.usbTransferUtil.ignoreBlood = false

                                    val min = 5 - mActivity.usbTransferUtil.min.toString().toInt()
                                    val sec =
                                        59 - (mActivity.usbTransferUtil.sec1 + mActivity.usbTransferUtil.sec2).toString()
                                            .toInt()
                                    val stopTime = "${min}分${sec}秒"

                                    mActivity.sixMinReportBloodOther.stopOr = "1"
                                    mActivity.sixMinReportBloodOther.stopReason = stopReason
                                    mActivity.sixMinReportBloodOther.stopTime = stopTime

                                    generateReportData(
                                        mActivity.usbTransferUtil.min,
                                        mActivity.usbTransferUtil.sec1 + mActivity.usbTransferUtil.sec2,
                                        1,
                                        min,
                                        sec
                                    )
                                }
                            })
                        }

                        override fun onNegativeClick() {
                            mActivity.finish()
                        }

                        override fun onStopNegativeClick(stopReason: String) {

                        }
                    })
                }
            } else {
                mActivity.showMsg(getString(R.string.sixmin_test_device_without_connection))
            }
        }

        binding.sixminIvCircleCountPlus.setNoRepeatListener {
            if (mActivity.usbTransferUtil.isBegin) {
                mActivity.usbTransferUtil.circleCount++
                binding.sixminTvCircleCount.text = mActivity.usbTransferUtil.circleCount.toString()
            } else {
                mActivity.showMsg("试验未开始!")
            }
        }
        binding.sixminIvCircleCountMinus.setNoRepeatListener {
            if (mActivity.usbTransferUtil.isBegin) {
                if (mActivity.usbTransferUtil.circleCount == 0) {
                    return@setNoRepeatListener
                }
                mActivity.usbTransferUtil.circleCount--
                binding.sixminTvCircleCount.text = mActivity.usbTransferUtil.circleCount.toString()
            } else {
                mActivity.showMsg("试验未开始!")
            }
        }
        binding.sixminIvIgnoreBlood.setNoRepeatListener {
            if (mActivity.usbTransferUtil.testType == 3) {
                if (!startRestoreEcgDialogFragment.isVisible) {
                    SixMinCollectRestoreEcgDialogFragment.startRestoreEcgDialogFragment(
                        mActivity.supportFragmentManager,
                        timeRemain
                    )
                }
            } else {
                binding.sixminIvIgnoreBlood.setBackgroundResource(R.drawable.sixmin_ignore_blood_pressure_disable)
                mActivity.usbTransferUtil.ignoreBlood = true
            }
        }

        binding.sixminTvToggleCardiopulmonary.setNoRepeatListener {
            if (binding.sixminTvToggleCardiopulmonary.text == "隐藏心肺参数") {
                binding.sixminTvToggleCardiopulmonary.text = "显示心肺参数"
                binding.sixminLlCardiopulmonary.visibility = View.GONE
            } else {
                binding.sixminTvToggleCardiopulmonary.text = "隐藏心肺参数"
                binding.sixminLlCardiopulmonary.visibility = View.VISIBLE
            }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSixminBinding.inflate(inflater, container, false)


    /**
     * 初始化系统设置信息
     */
    private fun initSysInfo() {
        val gson = Gson()
        mActivity.sysSettingBean = SixMinSysSettingBean()
        val sixMinSysSetting = SharedPreferencesUtils.instance.sixMinSysSetting
        if (sixMinSysSetting != null && sixMinSysSetting != "") {
            mActivity.sysSettingBean = gson.fromJson(
                sixMinSysSetting, SixMinSysSettingBean::class.java
            )
        }
    }

    private fun initCountDownTimerExt() {
        mCountDownTime = object : FixCountDownTime(360, 1000) {}
        mCountDownTimeThree = object : CountDownTimer(1080000, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                try {
                    if (mActivity.usbTransferUtil.bloodOxygenConnection) {
                        val mapBloodOxygen = mActivity.usbTransferUtil.mapBloodOxygen
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
        mGetSportHeartEcgCountDownTime = object : FixCountDownTime(10, 1000) {}
    }

    private fun addEntryData(entryData: Float, times: Int) {
        val bloodOxyAlarm = mActivity.sysSettingBean.sysAlarm.bloodOxy.toInt()
        val decimalFormat = DecimalFormat("#.00")
        val index: Float = (times.toFloat() / 60)

        bloodOxyDataSet.addEntry(
            Entry(
                ((18.00 - decimalFormat.format(index).toFloat()).toFloat()), entryData
            )
        )
        binding.sixminLineChartBloodOxygen.lineData.addDataSet(
            bloodOxyDataSet
        )

        mActivity.usbTransferUtil.bloodOxyLineData.add(
            BloodOxyLineEntryBean(
                ((18.00 - decimalFormat.format(index).toFloat()).toFloat()),
                entryData
            )
        )

        binding.sixminLineChartBloodOxygen.lineData.notifyDataChanged()
        binding.sixminLineChartBloodOxygen.notifyDataSetChanged()
        binding.sixminLineChartBloodOxygen.invalidate()
    }

    private fun beanQuery(any: Any) {
        try {
            if (any is List<*>) {
                val datas = any as MutableList<*>
                if (mActivity.sixMinPatientId.isNotEmpty()) {
                    datas.forEach {
                        val patientInfoBean = it as PatientInfoBean
                        if (patientInfoBean.infoBean.patientId.toString() == mActivity.sixMinPatientId) {
                            mActivity.patientBean = patientInfoBean
                        }
                    }
                } else {
                    mActivity.patientBean = datas[0] as PatientInfoBean
                }
                binding.sixminTvTestPatientName.text = mActivity.patientBean.infoBean.name
                binding.sixminTvTestPatientSex.text = mActivity.patientBean.infoBean.sex
                binding.sixminTvTestPatientAge.text = mActivity.patientBean.infoBean.age
                binding.sixminTvTestPatientHight.text = mActivity.patientBean.infoBean.height
                binding.sixminTvTestPatientWeight.text = mActivity.patientBean.infoBean.weight
                binding.sixminTvTestPatientBmi.text = mActivity.patientBean.infoBean.BMI

                initPatientInfo()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initPatientInfo() {
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
        val reportNo = dateFormat.format(date)
        mActivity.sixMinReportInfo.patientId = mActivity.patientBean.infoBean.patientId
        mActivity.sixMinReportInfo.reportNo = reportNo
        mActivity.sixMinReportInfo.patientName = mActivity.patientBean.infoBean.name.toString()
        mActivity.sixMinReportInfo.patientHeight = mActivity.patientBean.infoBean.height.toString()
        mActivity.sixMinReportInfo.patientWeight = mActivity.patientBean.infoBean.weight.toString()
        mActivity.sixMinReportInfo.patientSix = mActivity.patientBean.infoBean.sex.toString()
        mActivity.sixMinReportInfo.patientAge = mActivity.patientBean.infoBean.age.toString()
        mActivity.sixMinReportInfo.patientBmi = mActivity.patientBean.infoBean.BMI.toString()
        mActivity.sixMinReportInfo.patientStride = mActivity.patientBean.infoBean.stride.toString()
        mActivity.sixMinReportInfo.clinicalDiagnosis =
            mActivity.patientBean.infoBean.clinicalDiagnosis.toString()
        mActivity.sixMinReportInfo.medicineUse =
            mActivity.patientBean.infoBean.currentMedications.toString()
        mActivity.sixMinReportInfo.predictionDistance =
            mActivity.patientBean.infoBean.predictDistances.toString()
        mActivity.sixMinReportInfo.medicalNo =
            mActivity.patientBean.infoBean.medicalRecordNumber.toString()
        mActivity.sixMinReportInfo.medicalHistory =
            mActivity.patientBean.infoBean.diseaseHistory.toString()
        mActivity.sixMinReportInfo.bsHxl = mActivity.sysSettingBean.sysOther.stepsOrBreath

        mActivity.sixMinReportBloodOther.reportId = mActivity.sixMinReportInfo.reportNo
        mActivity.sixMinReportBloodOther.useName = mActivity.sysSettingBean.sysOther.useOrg
        mActivity.sixMinReportBloodOther.ecgType = mActivity.sysSettingBean.sysOther.ectType

        mActivity.sixMinReportBloodOxy.reportId = mActivity.sixMinReportInfo.reportNo

        mActivity.sixMinReportBloodHeart.reportId = mActivity.sixMinReportInfo.reportNo

        mActivity.sixMinReportBloodHeartEcg.reportId = mActivity.sixMinReportInfo.reportNo

        mActivity.sixMinReportEvaluation.reportId = mActivity.sixMinReportInfo.reportNo
        if (mActivity.selfCheckSelection != "") {
            val split = mActivity.selfCheckSelection.split("&")
            if (split.size > 1) {
                mActivity.sixMinReportEvaluation.befoFatigueLevel = split[1]
                mActivity.sixMinReportEvaluation.befoBreathingLevel = split[0]
            }
        }

        mActivity.sixMinReportPrescription.reportId = mActivity.sixMinReportInfo.reportNo

        mActivity.sixMinReportBreathing.reportId = mActivity.sixMinReportInfo.reportNo

        mActivity.sixMinReportStride.reportId = mActivity.sixMinReportInfo.reportNo

        mActivity.sixMinReportWalk.reportId = mActivity.sixMinReportInfo.reportNo
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
            val screenWidth = ScreenUtils.getScreenWidth(mActivity)
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
                textColor = ContextCompat.getColor(mActivity, R.color.text3)
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
                textColor = ContextCompat.getColor(mActivity, R.color.text3)
                //左侧Y轴的最大值和最小值
                axisMaximum = if (type == 1) 99f else 170f
                axisMinimum = if (type == 1) 85f else 30f
                setLabelCount(8, true)
                //绘制网格线(样式虚线)
                enableGridDashedLine(2f, 2f, 0f)
                gridColor = ContextCompat.getColor(mActivity, R.color.text3)
                setDrawGridLines(true)
                setDrawAxisLine(false) //绘制左边Y轴是否显示
                setDrawZeroLine(false) //是否开启0线
                isGranularityEnabled = true
            }

            legend?.apply {
                setDrawInside(false)
                formSize = 0f
            }

            if (type == 1) {
                bloodOxyDataSet = LineDataSet(null, "")
                bloodOxyDataSet.lineWidth = 1.0f
                bloodOxyDataSet.color = ContextCompat.getColor(mActivity, R.color.red)
                bloodOxyDataSet.setDrawValues(false)
                bloodOxyDataSet.setDrawCircles(false)
                bloodOxyDataSet.setDrawCircleHole(false)
                bloodOxyDataSet.setDrawFilled(false)
                bloodOxyDataSet.mode = LineDataSet.Mode.LINEAR
                val bloodOxyLineData = LineData(bloodOxyDataSet)
                data = bloodOxyLineData

                val mtRenderer = MyLineChartRenderer(this, animator, viewPortHandler)
                val colors = IntArray(4)
                colors[0] = Color.parseColor("#333333")
                colors[1] = Color.parseColor("#333333")
                colors[2] = Color.parseColor("#ff0000")
                colors[3] = Color.parseColor("#ff0000")
                mtRenderer.setHeartLine(
                    90,
                    100,
                    mActivity.sysSettingBean.sysAlarm.bloodOxy.toInt(),
                    colors
                )
                renderer = mtRenderer
            } else {
                heartBeatDataSet = LineDataSet(null, "")
                heartBeatDataSet.lineWidth = 1.0f
                heartBeatDataSet.color = ContextCompat.getColor(mActivity, R.color.text3)
                heartBeatDataSet.setDrawValues(false)
                heartBeatDataSet.setDrawCircles(false)
                heartBeatDataSet.setDrawCircleHole(false)
                heartBeatDataSet.setDrawFilled(false)
                heartBeatDataSet.mode = LineDataSet.Mode.LINEAR
                val lineData = LineData(heartBeatDataSet)
                data = lineData
            }
        }
    }

    /**
     * 显示引导弹窗
     */
    private fun showGuideDialog() {
        if (mActivity.usbTransferUtil.isConnectUSB && mActivity.usbTransferUtil.ecgConnection && mActivity.usbTransferUtil.bloodOxygenConnection && mActivity.usbTransferUtil.bloodPressureConnection) {
            if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1") {
                val sixMinGuide = SharedPreferencesUtils.instance.sixMinGuide
                if (sixMinGuide == null || sixMinGuide == "") {
                    val sixMinGuideDialogFragment =
                        SixMinGuideDialogFragment.startGuideDialogFragment(mActivity.supportFragmentManager)//添加患者修改患者信息
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

                        override fun onClickCancelTest() {
                            mActivity.finish()
                        }
                    })
                } else {
                    startTest()
                }
            } else {
                startTest()
            }
        } else {
            mActivity.showMsg(getString(R.string.sixmin_test_device_without_connection))
        }
    }

    private fun startTest() {
        if (!mActivity.usbTransferUtil.isBegin) {
            if (mActivity.sysSettingBean.sysOther.autoMeasureBlood == "1") {
                //是否播报语音
                if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
                    measureBloodUtteranceId = System.currentTimeMillis().toString()
                    speechContent(
                        getString(R.string.sixmin_test_start_measure_blood_front),
                        measureBloodUtteranceId
                    )
                } else {
                    if (mActivity.usbTransferUtil.bloodPressureConnection) {
                        if (binding.sixminTvMeasureBlood.text == getString(R.string.sixmin_measure_blood)) {
                            SixMinCmdUtils.measureBloodPressure()
                        } else {
                            mActivity.showMsg(getString(R.string.sixmin_test_measuring_blood))
                        }
                    } else {
                        binding.sixminRlMeasureBlood.isEnabled = true
                        binding.sixminRlStart.isEnabled = true
                        binding.sixminIvIgnoreBlood.isEnabled = true
                        mActivity.showMsg(getString(R.string.sixmin_test_blood_pressure_without_connection))
                    }
                }
            } else {
                binding.sixminRlMeasureBlood.isEnabled = true
                binding.sixminRlStart.isEnabled = true
                binding.sixminIvIgnoreBlood.isEnabled = true
                mActivity.showMsg("请点击测量运动前血压")
            }
        }
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
                lifecycleScope.launch(Dispatchers.Main) {
                    Log.d("SixMinFragment","倒计时===$times")
                    if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
                        speechContent(times.toString())
                    }
                    kotlinx.coroutines.delay(1500L)
                    binding.sixminIvCountdownTime.setImageResource(imageId)
                }
            }

            override fun onFinish() {
                Log.d("SixMinFragment","倒计时结束")
                lifecycleScope.launch {
                    kotlinx.coroutines.delay(1000L)
                    binding.sixminIvCountdownTime.visibility = View.GONE
                    if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
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

    private fun startStepAndCircle() {
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val addTime = dateFormat.format(date)
        mActivity.sixMinReportInfo.addTime = addTime

        mActivity.usbTransferUtil.bloodType = 1
        mActivity.usbTransferUtil.isBegin = true
        mActivity.usbTransferUtil.circleBoolean = true
        mActivity.usbTransferUtil.testType = 1
        binding.sixminTvStart.text = getString(R.string.sixmin_stop)
        mActivity.setToolbarTitle(getString(R.string.sixmin_test_testing))
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

                    mActivity.usbTransferUtil.min = binding.sixminTvStartMin.text.toString()
                    mActivity.usbTransferUtil.sec1 = binding.sixminTvStartSec1.text.toString()
                    mActivity.usbTransferUtil.sec2 = binding.sixminTvStartSec2.text.toString()

                    //识别每秒的步数，来采集休息时长
                    if (mActivity.sysSettingBean.sysOther.showResetTime == "1") {
                        val bsStr: String = mActivity.usbTransferUtil.stepsStr
                        if (bsStr == "0") {
                            ++mActivity.usbTransferUtil.restTime
                        } else {
                            if (mActivity.usbTransferUtil.checkBSStr.equals(bsStr)) {
                                ++mActivity.usbTransferUtil.checkBSInd
                            } else {
                                mActivity.usbTransferUtil.checkBSInd = 0
                            }
                            if (mActivity.usbTransferUtil.checkBSInd > 5) {
                                if (mActivity.usbTransferUtil.checkBSInd == 6) {
                                    mActivity.usbTransferUtil.restTime =
                                        mActivity.usbTransferUtil.restTime + 5
                                }
                                ++mActivity.usbTransferUtil.restTime
                            }
                        }
                        mActivity.usbTransferUtil.checkBSStr = bsStr
                    }
                    if (binding.sixminTvBloodOxygen.text != "---" && binding.sixminTvBloodOxygen.text != "--") {
                        mActivity.usbTransferUtil.bloodListAvg.add(
                            binding.sixminTvBloodOxygen.text.toString().toInt()
                        )
                        mActivity.usbTransferUtil.bloodAllListAvg.add(
                            binding.sixminTvBloodOxygen.text.toString().toInt()
                        )
                    }
                    if (second == 0) {
                        when (minute) {
                            5 -> {
                                if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
                                    speechContent(getString(R.string.sixmin_test_start_count_down_five_tips))
                                }
                                mActivity.sixMinReportWalk.walkOne =
                                    mActivity.usbTransferUtil.stepsStr
                                mActivity.usbTransferUtil.dealBloodBe(
                                    mActivity.sixMinReportBloodOxy,
                                    minute,
                                    mActivity.usbTransferUtil.bloodListAvg
                                )
                                mActivity.usbTransferUtil.bloodListAvg.clear()
                            }

                            4 -> {
                                if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
                                    speechContent(getString(R.string.sixmin_test_start_count_down_four_tips))
                                }
                                mActivity.sixMinReportWalk.walkTwo =
                                    (mActivity.usbTransferUtil.stepsStr.toInt() - mActivity.sixMinReportWalk.walkOne.toInt()).toString()
                                mActivity.usbTransferUtil.dealBloodBe(
                                    mActivity.sixMinReportBloodOxy,
                                    minute,
                                    mActivity.usbTransferUtil.bloodListAvg
                                )
                                mActivity.usbTransferUtil.bloodListAvg.clear()
                            }

                            3 -> {
                                if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
                                    speechContent(getString(R.string.sixmin_test_start_count_down_three_tips))
                                }
                                mActivity.sixMinReportWalk.walkThree =
                                    (mActivity.usbTransferUtil.stepsStr.toInt() - mActivity.sixMinReportWalk.walkOne.toInt() - mActivity.sixMinReportWalk.walkTwo.toInt()).toString()
                                mActivity.usbTransferUtil.dealBloodBe(
                                    mActivity.sixMinReportBloodOxy,
                                    minute,
                                    mActivity.usbTransferUtil.bloodListAvg
                                )
                                mActivity.usbTransferUtil.bloodListAvg.clear()
                            }

                            2 -> {
                                if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
                                    speechContent(getString(R.string.sixmin_test_start_count_down_two_tips))
                                }
                                mActivity.sixMinReportWalk.walkFour =
                                    (mActivity.usbTransferUtil.stepsStr.toInt() - mActivity.sixMinReportWalk.walkThree.toInt() - mActivity.sixMinReportWalk.walkTwo.toInt() - mActivity.sixMinReportWalk.walkOne.toInt()).toString()
                                mActivity.usbTransferUtil.dealBloodBe(
                                    mActivity.sixMinReportBloodOxy,
                                    minute,
                                    mActivity.usbTransferUtil.bloodListAvg
                                )
                                mActivity.usbTransferUtil.bloodListAvg.clear()
                            }

                            1 -> {
                                if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
                                    speechContent(getString(R.string.sixmin_test_start_count_down_one_tips))
                                }
                                mActivity.sixMinReportWalk.walkFive =
                                    (mActivity.usbTransferUtil.stepsStr.toInt() - mActivity.sixMinReportWalk.walkFour.toInt() - mActivity.sixMinReportWalk.walkThree.toInt() - mActivity.sixMinReportWalk.walkTwo.toInt() - mActivity.sixMinReportWalk.walkOne.toInt()).toString()
                                mActivity.usbTransferUtil.dealBloodBe(
                                    mActivity.sixMinReportBloodOxy,
                                    minute,
                                    mActivity.usbTransferUtil.bloodListAvg
                                )
                                mActivity.usbTransferUtil.bloodListAvg.clear()
                            }
                        }
                    }

                    if (minute == 0 && second == 16) {
                        if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
                            speechContent(getString(R.string.sixmin_test_start_count_down_zero_tips))
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFinish() {
                mActivity.usbTransferUtil.testType = 2
                mActivity.sixMinReportWalk.walkSix =
                    (mActivity.usbTransferUtil.stepsStr.toInt() - mActivity.sixMinReportWalk.walkFive.toInt() - mActivity.sixMinReportWalk.walkFour.toInt() - mActivity.sixMinReportWalk.walkThree.toInt() - mActivity.sixMinReportWalk.walkTwo.toInt() - mActivity.sixMinReportWalk.walkOne.toInt()).toString()
                mActivity.usbTransferUtil.dealBloodBe(
                    mActivity.sixMinReportBloodOxy,
                    0,
                    mActivity.usbTransferUtil.bloodListAvg
                )
                mActivity.usbTransferUtil.bloodListAvg.clear()

                if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
                    speechContent(getString(R.string.sixmin_test_start_timeout))
                }
                binding.sixminRlMeasureBlood.isEnabled = false
                binding.sixminRlStart.isEnabled = false
                mActivity.usbTransferUtil.circleBoolean = false
//                mActivity.usbTransferUtil.isBegin = false
                mActivity.usbTransferUtil.bloodType = 2
                SixMinCmdUtils.closeQSAndBS()
                mActivity.setToolbarTitle(getString(R.string.sixmin_test_title))
                binding.sixminTvStartMin.text = "0"
                binding.sixminTvStartSec1.text = "0"
                binding.sixminTvStartSec2.text = "0"
                mCountDownTimeThree.cancel()
                mCountDownTime.setmTimes(360)

                lifecycleScope.launch {
                    kotlinx.coroutines.delay(2000L)
                    mGetSportHeartEcgCountDownTime.start(object : FixCountDownTime.OnTimerCallBack {
                        override fun onStart() {
                            mActivity.usbTransferUtil.testType = 3
                            startRestoreEcgDialogFragment =
                                SixMinCollectRestoreEcgDialogFragment.startRestoreEcgDialogFragment(
                                    mActivity.supportFragmentManager,
                                    timeRemain
                                )
                        }


                        override fun onTick(times: Int) {
                            timeRemain = times.toString()
                            LiveDataBus.get().with("simMinRestore").postValue(timeRemain)
                        }

                        override fun onFinish() {
                            startRestoreEcgDialogFragment.dismiss()
                            binding.sixminIvIgnoreBlood.isEnabled = false
                            if (!mActivity.usbTransferUtil.ignoreBlood) {
                                if (mActivity.sysSettingBean.sysOther.autoMeasureBlood == "1") {
                                    lifecycleScope.launch {
                                        kotlinx.coroutines.delay(1000L)
                                        binding.sixminTvBloodPressureHigh.text = "---"
                                        binding.sixminTvBloodPressureLow.text = "---"
                                        if (mActivity.usbTransferUtil.bloodPressureConnection) {
                                            if (binding.sixminTvMeasureBlood.text == getString(R.string.sixmin_measure_blood)) {
                                                SixMinCmdUtils.measureBloodPressure()
                                            } else {
                                                mActivity.showMsg(getString(R.string.sixmin_test_measuring_blood))
                                            }
                                        } else {
                                            binding.sixminRlMeasureBlood.isEnabled = true
                                            mActivity.showMsg(getString(R.string.sixmin_test_blood_pressure_without_connection))
                                        }
                                    }
                                } else {
                                    binding.sixminRlMeasureBlood.isEnabled = true
                                    mActivity.showMsg("请点击测量运动后血压")
                                }
                            } else {
                                val startCommonDialogFragment =
                                    CommonDialogFragment.startCommonDialogFragment(
                                        mActivity.supportFragmentManager,
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
                                            mActivity.usbTransferUtil.isBegin = false
                                            mActivity.usbTransferUtil.testType = 0
                                            SixMinCmdUtils.closeQSAndBS()
                                            binding.sixminTvStart.text =
                                                getString(R.string.sixmin_start)
                                            mActivity.setToolbarTitle(getString(R.string.sixmin_test_title))
                                            mCountDownTime.cancel()
                                            mCountDownTime.setmTimes(360)
                                            mActivity.usbTransferUtil.ignoreBlood = false

                                            mActivity.sixMinReportBloodOther.badOr = "1"
                                            mActivity.sixMinReportBloodOther.badSymptoms =
                                                stopReason
                                            generateReportData(
                                                mActivity.usbTransferUtil.min,
                                                mActivity.usbTransferUtil.sec1 + mActivity.usbTransferUtil.sec2,
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
            }
        })
        mCountDownTimeThree.start()
    }

    /**
     * 插入数据库
     */
    private fun generateReportData(min: String, sec: String, type: Int, min1: Int, sec1: Int) {
        try {
            if (mActivity.sysSettingBean.sysOther.showResetTime == "1") {
                if (type == 1) {
                    mActivity.sixMinReportInfo.restDuration =
                        (mActivity.usbTransferUtil.restTime - 1).toString()
                } else if (type == 0) {
                    mActivity.sixMinReportInfo.restDuration =
                        mActivity.usbTransferUtil.restTime.toString()
                }
            } else if (mActivity.sysSettingBean.sysOther.showResetTime == "0") {
                mActivity.sixMinReportInfo.restDuration = "-1"
            }
            mActivity.sixMinReportEvaluation.turnsNumber =
                mActivity.usbTransferUtil.circleCount.toString()
            mActivity.sixMinReportEvaluation.totalWalk = mActivity.usbTransferUtil.stepsStr

            mActivity.usbTransferUtil.dealWalk(mActivity.sixMinReportWalk)
            mActivity.usbTransferUtil.dealBlood(
                mActivity.sixMinReportBloodOxy,
                Gson().toJson(mActivity.usbTransferUtil.bloodOxyLineData)
            )
            if (mActivity.sysSettingBean.sysOther.circleCountType == "0") {
                mActivity.usbTransferUtil.dealPreption(
                    mActivity.sysSettingBean,
                    mActivity.sixMinReportEvaluation,
                    min,
                    sec,
                    type,
                    min1,
                    sec1
                )
            } else {
                mActivity.usbTransferUtil.dealPreptionSD(
                    mActivity.sixMinReportEvaluation,
                    mActivity.sysSettingBean,
                    min,
                    sec,
                    type,
                    min1,
                    sec1
                )
            }

            val avgStride = mActivity.usbTransferUtil.getAvgStride(
                BigDecimal(mActivity.sixMinReportEvaluation.totalDistance),
                if (type == 0) 360 else min1 * 60 + sec1
            )
            mActivity.sixMinReportStride.strideAverage = avgStride

            //计算实际运动距离占预测的百分比
            val bd: BigDecimal =
                BigDecimal(mActivity.sixMinReportEvaluation.totalDistance).divide(
                    BigDecimal(if (mActivity.patientBean.infoBean.predictDistances == "") "1" else mActivity.patientBean.infoBean.predictDistances),
                    2,
                    RoundingMode.HALF_UP
                )
            val bd2: BigDecimal = bd.multiply(BigDecimal(100)).setScale(0)
            mActivity.sixMinReportEvaluation.accounted = bd2.toString()

            mActivity.usbTransferUtil.release()

            jumpToPreReport()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun jumpToPreReport() {
        navigate(binding.sixminIvSystemSetting, R.id.sixMinPreReportFragment)
    }

    private fun speechContent(
        content: String, utteranceId: String = System.currentTimeMillis().toString()
    ) {
        textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

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
                    mActivity.runOnUiThread {
                        Log.d(TAG, "onDone: $utteranceId")
                        if (measureBloodUtteranceId == defaultUtteranceId) {
                            if (mActivity.usbTransferUtil.bloodPressureConnection) {
                                if (binding.sixminTvMeasureBlood.text == getString(R.string.sixmin_measure_blood)) {
                                    SixMinCmdUtils.measureBloodPressure()
                                } else {
                                    mActivity.showMsg(getString(R.string.sixmin_test_measuring_blood))
                                }
//                                binding.sixminRlStart.isEnabled = true
                                binding.sixminRlMeasureBlood.isEnabled = true
//                                binding.sixminIvIgnoreBlood.isEnabled = true
                            } else {
                                binding.sixminRlMeasureBlood.isEnabled = true
                                mActivity.showMsg(getString(R.string.sixmin_test_blood_pressure_without_connection))
                            }
                        } else if (startTestUtteranceId == defaultUtteranceId) {
                            autoStartTest()
                        } else if (stepsAndCircleUtteranceId == defaultUtteranceId) {
                            startStepAndCircle()
                        } else if (bloodBeforeUtteranceId == defaultUtteranceId) {
                            val startCommonDialogFragment =
                                CommonDialogFragment.startCommonDialogFragment(
                                    mActivity.supportFragmentManager,
                                    getString(R.string.sixmin_test_start_use_this_blood_pressure_front)
                                )
                            startCommonDialogFragment.setCommonDialogOnClickListener(object :
                                CommonDialogFragment.CommonDialogClickListener {
                                override fun onPositiveClick() {
                                    mActivity.usbTransferUtil.bloodType = 1
                                    if (mActivity.sysSettingBean.sysOther.autoStart == "1") {
                                        if (mActivity.sysSettingBean.sysOther.broadcastVoice == "1" || mActivity.sysSettingBean.sysOther.broadcastVoice == "0") {
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
                                    mActivity.usbTransferUtil.bloodType = 1
                                }

                                override fun onStopNegativeClick(stopReason: String) {

                                }
                            })
                        } else if (bloodEndUtteranceId == defaultUtteranceId) {
                            val startCommonDialogFragment =
                                CommonDialogFragment.startCommonDialogFragment(
                                    mActivity.supportFragmentManager,
                                    getString(R.string.sixmin_test_start_use_this_blood_pressure_behind)
                                )
                            startCommonDialogFragment.setCommonDialogOnClickListener(object :
                                CommonDialogFragment.CommonDialogClickListener {
                                override fun onPositiveClick() {
                                    mActivity.usbTransferUtil.bloodType = 0
                                    mActivity.usbTransferUtil.isBegin = false
                                    mActivity.usbTransferUtil.testType = 0
                                    SixMinCmdUtils.closeQSAndBS()
                                    binding.sixminTvStart.text =
                                        getString(R.string.sixmin_start)
                                    mActivity.setToolbarTitle(getString(R.string.sixmin_test_title))
//                                    binding.sixminTvStartMin.text = "0"
//                                    binding.sixminTvStartSec1.text = "0"
//                                    binding.sixminTvStartSec2.text = "0"
                                    mCountDownTime.cancel()
                                    mCountDownTime.setmTimes(360)
                                    mActivity.usbTransferUtil.ignoreBlood = false

                                    generateReportData(
                                        mActivity.usbTransferUtil.min,
                                        mActivity.usbTransferUtil.sec1 + mActivity.usbTransferUtil.sec2,
                                        0,
                                        0,
                                        0
                                    )
                                }

                                override fun onNegativeClick() {
                                    mActivity.usbTransferUtil.bloodType = 0
                                    mActivity.usbTransferUtil.isBegin = false
                                    mActivity.usbTransferUtil.testType = 0
                                    SixMinCmdUtils.closeQSAndBS()
                                    binding.sixminTvStart.text =
                                        getString(R.string.sixmin_start)
                                    mActivity.setToolbarTitle(getString(R.string.sixmin_test_title))
//                                    binding.sixminTvStartMin.text = "0"
//                                    binding.sixminTvStartSec1.text = "0"
//                                    binding.sixminTvStartSec2.text = "0"
                                    mCountDownTime.cancel()
                                    mCountDownTime.setmTimes(360)
                                    mActivity.usbTransferUtil.ignoreBlood = false

                                    generateReportData(
                                        mActivity.usbTransferUtil.min,
                                        mActivity.usbTransferUtil.sec1 + mActivity.usbTransferUtil.sec2,
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

    override fun onResume() {
        initSysInfo()
        mActivity.setToolbarTitle(getString(R.string.sixmin_test_title))
        binding.sixminEcg.onResume()
        super.onResume()
    }

    override fun onPause() {
        binding.sixminEcg.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding.sixminRlStart.isEnabled = true
        binding.sixminRlMeasureBlood.isEnabled = true
        binding.sixminIvIgnoreBlood.isEnabled = true
        mActivity.usbTransferUtil.isBegin = false
        mActivity.usbTransferUtil.testType = 0
        mActivity.usbTransferUtil.bloodType = 0
        SixMinCmdUtils.closeQSAndBS()
        textToSpeech.stop()
        textToSpeech.shutdown()
        mCountDownTime.cancel()
        mCountDownTimeThree.cancel()
        mStartTestCountDownTime.cancel()
        super.onDestroyView()
    }
}
