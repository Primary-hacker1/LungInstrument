package com.just.machine.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseActivity
import com.common.base.setNoRepeatListener
import com.common.viewmodel.LiveDataEvent
import com.google.gson.Gson
import com.just.machine.model.Constants
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.model.SixMinReportEditBloodPressure
import com.just.machine.model.SixMinReportItemBean
import com.just.machine.model.SixMinReportPatientSelfBean
import com.just.machine.model.SixMinReportPatientSelfItemBean
import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.model.sixminreport.SixMinReportHeartBeat
import com.just.machine.model.sixminreport.SixMinReportInfo
import com.just.machine.model.sixminreport.SixMinReportOther
import com.just.machine.model.sixminreport.SixMinReportPrescription
import com.just.machine.model.sixminreport.SixMinReportStride
import com.just.machine.ui.adapter.SixMinReportPatientSelfAdapter
import com.just.machine.ui.dialog.CommonDialogFragment
import com.just.machine.ui.dialog.SixMinReportEditBloodPressureFragment
import com.just.machine.ui.dialog.SixMinReportPrescriptionFragment
import com.just.machine.ui.dialog.SixMinReportSelfCheckBeforeTestFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.CommonUtil
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.ActivitySixMinPreReportBinding
import com.justsafe.libview.util.SystemUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.math.BigDecimal


/**
 * 6分钟预生成报告
 */
@AndroidEntryPoint
class SixMinPreReportActivity : CommonBaseActivity<ActivitySixMinPreReportBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private var reportRowList = mutableListOf<SixMinReportItemBean>()
    private var sixMinPatientId = "" //患者id
    private var sixMinReportNo = "" //报告id
    private var sixMinReportType = "" //跳转类型 1新增 2编辑
    private lateinit var usbTransferUtil: USBTransferUtil
    private var sixMinRecordsBean: SixMinRecordsBean = SixMinRecordsBean()//6分钟报告信息
    private var patientSelfList = mutableListOf<SixMinReportPatientSelfBean>()
    private var selectStrList = mutableListOf<String>()
    private var sixMinReportPrescription: SixMinReportPrescription =
        SixMinReportPrescription()//6分钟报告处方信息
    private var sixMinReportBloodOther: SixMinReportOther = SixMinReportOther()//6分钟其它信息
    private var sixMinReportStride: SixMinReportStride = SixMinReportStride()//6分钟步速信息
    private var sixMinReportEvaluation: SixMinReportEvaluation = SixMinReportEvaluation()//6分钟综合评估信息
    private var sixMinReportHeartBeat: SixMinReportHeartBeat = SixMinReportHeartBeat()//6分钟综合心率信息
    private var sixMinReportInfo: SixMinReportInfo = SixMinReportInfo()//6分钟患者信息
    private var strideAvg: BigDecimal = BigDecimal(0.00)
    private var isFirst = true

    override fun getViewBinding() = ActivitySixMinPreReportBinding.inflate(layoutInflater)

    override fun initView() {
        usbTransferUtil = USBTransferUtil.getInstance()
        sixMinPatientId = intent.extras?.getString(Constants.sixMinPatientInfo).toString()
        sixMinReportNo = intent.extras?.getString(Constants.sixMinReportNo).toString()
        sixMinReportType = intent.extras?.getString(Constants.sixMinReportType).toString()
        lifecycleScope.launch {
            kotlinx.coroutines.delay(0L)
            viewModel.getSixMinReportInfoById(sixMinPatientId.toLong(), sixMinReportNo)
        }
        initClickListener()
    }

    private fun initSelfCheck() {
        val fatigueLevel = sixMinReportEvaluation.fatigueLevel
        val breathingLevel = sixMinReportEvaluation.breathingLevel

        patientSelfList.clear()
        val patientBreathSelfItemList = mutableListOf<SixMinReportPatientSelfItemBean>()
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0级", "没有", if (breathingLevel == "0") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0.5级", "非常非常轻", if (breathingLevel == "0.5") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "1级", "非常轻", if (breathingLevel == "1") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "2级", "很轻", if (breathingLevel == "2") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "3级", "中度", if (breathingLevel == "3") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "4级", "较严重", if (breathingLevel == "4") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "5-6级", "严重", if (breathingLevel == "5-6") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "7-9级", "非常严重", if (breathingLevel == "7-9") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "10级", "非常非常严重", if (breathingLevel == "10") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientSelfList.add(
            SixMinReportPatientSelfBean(
                "呼吸状况等级", "1", patientBreathSelfItemList,
                usbTransferUtil.dealSelfCheckBreathingLevel(breathingLevel)
            )
        )
        val patientTiredSelfItemList = mutableListOf<SixMinReportPatientSelfItemBean>()
        patientTiredSelfItemList.clear()
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0级", "没有", if (fatigueLevel == "0") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "1级", "非常轻松", if (fatigueLevel == "1") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "2级", "轻松", if (fatigueLevel == "2") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "3级", "中度", if (fatigueLevel == "3") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "4级", "有点疲劳", if (fatigueLevel == "4") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "5-6级", "疲劳", if (fatigueLevel == "5-6") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "7-8级", "非常疲劳", if (fatigueLevel == "7-8") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "9-10级", "非常非常疲劳(几乎到极限)", if (fatigueLevel == "10") "1" else "0",
                if (sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientSelfList.add(
            SixMinReportPatientSelfBean(
                "疲劳状况等级", "2", patientTiredSelfItemList,
                usbTransferUtil.dealSelfCheckFatigueLevel(fatigueLevel)
            )
        )
        binding.sixminRvPatientSelfCheck.layoutManager = LinearLayoutManager(this)
        val patientSelfItemAdapter = SixMinReportPatientSelfAdapter(this)
        patientSelfItemAdapter.setItemsBean(patientSelfList)
        binding.sixminRvPatientSelfCheck.adapter = patientSelfItemAdapter
    }

    /**
     * 运动时长变化
     */
    private fun dealTimeSportTimeChange(value: Int) {
        if (sixMinReportPrescription.distanceState == "1") {
            var percentLow = ""
            var percentHigh = ""
            if (sixMinReportPrescription.distanceFormula == "0") {
                percentLow = "0.5"
                percentHigh = "0.6"
            } else {
                percentLow = "0.7"
                percentHigh = "0.8"
            }
            binding.sixminPreEtDistanceLow.setText(
                usbTransferUtil.dealtjjlStrs(
                    percentLow,
                    BigDecimal(if (sixMinReportStride.strideAverage == "") "0.00" else sixMinReportStride.strideAverage),
                    value
                )
            )
            binding.sixminPreEtDistanceHigh.setText(
                usbTransferUtil.dealtjjlStrs(
                    percentHigh,
                    BigDecimal(if (sixMinReportStride.strideAverage == "") "0.00" else sixMinReportStride.strideAverage),
                    value
                )
            )
        }
    }

    private fun initClickListener() {
        viewModel.mEventHub.observe(this) {
            if (isFirst) {
                when (it.action) {
                    LiveDataEvent.QuerySixMinReportInfoSuccess -> {
                        it.any?.let { it1 -> beanQuery(it1) }
                    }
                }
                isFirst = false
            }
        }
        binding.sixminReportTvEditBloodPressure.setOnClickListener {
            if (reportRowList.isNotEmpty()) {
                val sixMinReportItemBean = reportRowList[reportRowList.size - 1]
                val stillnessValue = sixMinReportItemBean.stillnessValue
                val bean = SixMinReportEditBloodPressure()
                val before = stillnessValue.split("/")
                if (before.size > 1) {
                    bean.highBloodPressureBefore = before[0]
                    bean.lowBloodPressureBefore = before[1]
                }
                val after = sixMinReportItemBean.sixMinValue.split("/")
                if (after.size > 1) {
                    bean.highBloodPressureAfter = after[0]
                    bean.lowBloodPressureAfter = after[1]
                }
                val startEditBloodDialogFragment =
                    SixMinReportEditBloodPressureFragment.startEditBloodDialogFragment(
                        supportFragmentManager, bean
                    )
                startEditBloodDialogFragment.setEditBloodDialogOnClickListener(object :
                    SixMinReportEditBloodPressureFragment.SixMinReportEditBloodDialogListener {
                    override fun onClickConfirm(bean: SixMinReportEditBloodPressure) {
                        val highBloodPressureBefore = bean.highBloodPressureBefore
                        val lowBloodPressureBefore = bean.lowBloodPressureBefore
                        val highBloodPressureAfter = bean.highBloodPressureAfter
                        val lowBloodPressureAfter = bean.lowBloodPressureAfter

                        if (highBloodPressureBefore?.isNotEmpty() == true) {
                            if (highBloodPressureBefore.toInt() == 0 || highBloodPressureBefore.toInt() > 999) {
                                showMsg("请检查试验前 收缩压值")
                                return
                            }
                            if (lowBloodPressureBefore?.isEmpty() == true) {
                                showMsg("请检查试验前 舒张压值")
                                return
                            }
                        }
                        if (lowBloodPressureBefore?.isNotEmpty() == true) {
                            if (lowBloodPressureBefore.toInt() == 0 || lowBloodPressureBefore.toInt() > 999) {
                                showMsg("请检查试验前 舒张压值")
                                return
                            }
                            if (highBloodPressureBefore?.isEmpty() == true) {
                                showMsg("请检查试验前 收缩压值")
                                return
                            }
                        }

                        if (highBloodPressureAfter?.isNotEmpty() == true) {
                            if (highBloodPressureAfter.toInt() == 0 || highBloodPressureAfter.toInt() > 999) {
                                showMsg("请检查试验后 收缩压值")
                                return
                            }
                            if (lowBloodPressureAfter?.isEmpty() == true) {
                                showMsg("请检查试验后 舒张压值")
                                return
                            }
                        }

                        if (lowBloodPressureAfter?.isNotEmpty() == true) {
                            if (lowBloodPressureAfter.toInt() == 0 || lowBloodPressureAfter.toInt() > 999) {
                                showMsg("请检查试验后 舒张压值")
                                return
                            }
                            if (highBloodPressureAfter?.isEmpty() == true) {
                                showMsg("请检查试验后 收缩压值")
                                return
                            }
                        }
                        startEditBloodDialogFragment.dismiss()
                        viewModel.updateSixMinReportOther(
                            sixMinRecordsBean.infoBean.reportNo,
                            bean.highBloodPressureBefore.toString(),
                            bean.lowBloodPressureBefore.toString(),
                            bean.highBloodPressureAfter.toString(),
                            bean.lowBloodPressureAfter.toString()
                        )
                        viewModel.getSixMinReportInfoById(sixMinPatientId.toLong(), sixMinReportNo)
                        binding.sixminReportTlPreTable.removeAllViews()
                    }
                })
            }
        }
        binding.sixminReportTvSelfCheckBeforeTest.setNoRepeatListener {
            var selfCheckSelection = ""
            if (sixMinRecordsBean.evaluationBean[0].befoFatigueLevel != "" && sixMinRecordsBean.evaluationBean[0].befoBreathingLevel != "") {
                selfCheckSelection =
                    "${sixMinRecordsBean.evaluationBean[0].befoBreathingLevel}&${sixMinRecordsBean.evaluationBean[0].befoFatigueLevel}"
            }
            val selfCheckBeforeTestDialogFragment =
                SixMinReportSelfCheckBeforeTestFragment.startPatientSelfCheckDialogFragment(
                    supportFragmentManager, "0", "", selfCheckSelection
                )
            selfCheckBeforeTestDialogFragment.setSelfCheckBeforeTestDialogOnClickListener(object :
                SixMinReportSelfCheckBeforeTestFragment.SixMinReportSelfCheckBeforeTestDialogListener {
                override fun onClickConfirm(
                    befoFatigueLevel: Int,
                    befoBreathingLevel: Int,
                    befoFatigueLevelStr: String,
                    befoBreathingLevelStr: String
                ) {
                    Log.d("tag", "$befoFatigueLevel$befoBreathingLevel")
                }

                override fun onClickClose() {

                }
            })
        }
        binding.sixminReportTvPrescription.setNoRepeatListener {
            val prescriptionFragment =
                SixMinReportPrescriptionFragment.startPrescriptionDialogFragment(
                    supportFragmentManager,
                    sixMinReportPrescription,
                )
            prescriptionFragment.setPrescriptionDialogOnClickListener(object :
                SixMinReportPrescriptionFragment.SixMinReportPrescriptionDialogListener {
                override fun onClickConfirm(
                    stride: String,
                    distance: String,
                    heart: String,
                    metab: String,
                    borg: String,
                    strideFormula: String,
                    distanceFormula: String
                ) {
                    if (stride == "出具") {
                        sixMinReportPrescription.distanceState = "1"
                    } else {
                        sixMinReportPrescription.distanceState = "2"
                    }
                    if (heart == "出具") {
                        sixMinReportPrescription.heartrateState = "1"
                    } else {
                        sixMinReportPrescription.heartrateState = "2"
                    }
                    if (metab == "出具") {
                        sixMinReportPrescription.metabState = "1"
                    } else {
                        sixMinReportPrescription.metabState = "2"
                    }
                    if (borg == "出具") {
                        sixMinReportPrescription.pllevState = "1"
                    } else {
                        sixMinReportPrescription.pllevState = "2"
                    }
                    sixMinReportPrescription.strideFormula = strideFormula
                    sixMinReportPrescription.distanceFormula = distanceFormula
                    dealSelectPrescriptionElement()
                }

                override fun onClickClose() {

                }

            })
        }
        binding.sixminReportTvGenerateReport.setNoRepeatListener {
            try {
                val text = binding.sixminReportTvGenerateReport.text
                if (text == "生成报告") {
                    //生成报告
                    selectStrList.clear()
                    patientSelfList.forEach {
                        it.itemList.forEach { it1 ->
                            if (it1.itemCheck == "1") {
                                val index = it1.itemName.indexOf("级")
                                selectStrList.add(
                                    "${it.itemName}&${
                                        it1.itemName.substring(
                                            0,
                                            index
                                        )
                                    }"
                                )
                            }
                        }
                    }
                    if (selectStrList.isNotEmpty()) {
                        if (selectStrList.size > 1) {
//                            viewModel.updateSixMinReportEvaluation(
//                                sixMinRecordsBean.evaluationBean[0].reportId,
//                                selectStrList[1].split("&")[1],
//                                selectStrList[0].split("&")[1]
//                            )
                            sixMinReportEvaluation.breathingLevel = selectStrList[0].split("&")[1]
                            sixMinReportEvaluation.fatigueLevel = selectStrList[1].split("&")[1]
                        } else {
                            val split = selectStrList[0].split("&")
                            if (split[0] == "呼吸状况等级") {
                                showMsg("请进行患者自评疲劳量级")
                            } else {
                                showMsg("请进行患者自评呼吸量级")
                            }
                            return@setNoRepeatListener
                        }
                    } else {
                        showMsg("请进行患者自评呼吸和疲劳量级")
                        return@setNoRepeatListener
                    }

                    val circleNum = binding.sixminEtFinishCircle.text.toString()
                    if (circleNum.isEmpty() || circleNum.length > 2) {
                        showMsg("请检查圈数值")
                        return@setNoRepeatListener
                    }
                    sixMinReportEvaluation.turnsNumber = circleNum

                    var wqsBoolen = true
                    if (binding.sixminEtUnfinishCircle.text.toString().isNotEmpty()) {
                        val wqsDec: BigDecimal =
                            BigDecimal(binding.sixminEtUnfinishCircle.text.toString())
                        val totoLength: Int = sixMinReportEvaluation.fieldLength.toInt() * 2
                        if (wqsDec.compareTo(BigDecimal(totoLength)) == 1) {
                            wqsBoolen = false
                        }
                    }
                    if (binding.sixminEtUnfinishCircle.text.toString().isEmpty() || !wqsBoolen) {
                        showMsg("请检查未走完的距离值")
                        return@setNoRepeatListener
                    }
                    sixMinReportEvaluation.unfinishedDistance =
                        binding.sixminEtUnfinishCircle.text.toString()

                    if (sixMinReportInfo.restDuration != "-1") {
                        if (binding.sixminPreEtResetTime.text.toString().isEmpty()) {
                            showMsg("中途休息值不能为空")
                            return@setNoRepeatListener
                        }
                        if (binding.sixminPreEtResetTime.text.toString().toInt() > 360) {
                            showMsg("中途休息值不能大于360")
                            return@setNoRepeatListener
                        }
                        //提前停止
                        if (sixMinReportBloodOther.stopOr == "1") {
                            val stopTimeStr: String = sixMinReportBloodOther.stopTime
                            val minStr = stopTimeStr.substring(0, 1)
                            val stopTimeStrings =
                                stopTimeStr.split("分".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()
                            if (stopTimeStrings.size == 2) {
                                val stopTimeSecStr = stopTimeStrings[1]
                                val secStr = stopTimeSecStr.substring(0, stopTimeSecStr.length - 1)
                                val minInt = minStr.toInt()
                                val secInt = secStr.toInt()
                                val stopTimeInt = minInt * 60 + secInt
                                if (binding.sixminPreEtResetTime.text.toString()
                                        .toInt() > stopTimeInt
                                ) {
                                    showMsg("中途休息值不能大于步行时间")
                                    return@setNoRepeatListener
                                }
                            }
                        }
                    }
                    sixMinReportInfo.restDuration = binding.sixminPreEtResetTime.text.toString()

                    val heartBeatConclusion = binding.sixminEtHeartBeatConclusion.text.toString()
                    if (heartBeatConclusion.isNotEmpty() && heartBeatConclusion.length > 44) {
                        showMsg("请检查心电结论，最大长度为44")
                        return@setNoRepeatListener
                    }
                    sixMinReportHeartBeat.heartConclusion = heartBeatConclusion

                    //运动处方
                    if (sixMinReportPrescription.distanceState.isEmpty() || sixMinReportPrescription.distanceState == "1") {
                        if (binding.sixminPreEtStrideLow.text.toString()
                                .isEmpty() || binding.sixminPreEtStrideHigh.text.toString()
                                .isEmpty()
                        ) {
                            showMsg("请检查运动步速值")
                            return@setNoRepeatListener
                        }
                        if (!CommonUtil.checkNum(
                                binding.sixminPreEtStrideLow.text.toString(),
                                "0+"
                            ) || !CommonUtil.checkNum(
                                binding.sixminPreEtStrideHigh.text.toString(),
                                "0+"
                            )
                        ) {
                            showMsg("请检查运动步速值，最多精确到1位")
                            return@setNoRepeatListener
                        }
                        if (BigDecimal(binding.sixminPreEtStrideLow.text.toString()).compareTo(
                                BigDecimal("1000")
                            ) != -1 ||
                            BigDecimal(binding.sixminPreEtStrideHigh.text.toString()).compareTo(
                                BigDecimal("1000")
                            ) != -1
                        ) {
                            showMsg("请检查运动步速值，最大值为999.9")
                            return@setNoRepeatListener
                        }
                        //前者不可大于后者
                        if (BigDecimal(binding.sixminPreEtStrideLow.text.toString()) > BigDecimal(
                                binding.sixminPreEtStrideHigh.text.toString()
                            )
                        ) {
                            showMsg("请检查运动步速值,起始值不可大于终止值")
                            return@setNoRepeatListener
                        }

                        if (binding.sixminPreEtDistanceLow.text.toString()
                                .isEmpty() || binding.sixminPreEtDistanceHigh.text.toString()
                                .isEmpty()
                        ) {
                            showMsg("请检查运动距离值")
                            return@setNoRepeatListener
                        }
                        if (!CommonUtil.checkNum(
                                binding.sixminPreEtDistanceLow.text.toString(),
                                "0+"
                            ) || !CommonUtil.checkNum(
                                binding.sixminPreEtDistanceHigh.text.toString(),
                                "0+"
                            )
                        ) {
                            showMsg("请检查运动距离值，最多精确到1位")
                            return@setNoRepeatListener
                        }
                        if (BigDecimal(binding.sixminPreEtDistanceLow.text.toString()).compareTo(
                                BigDecimal("10000")
                            ) != -1 ||
                            BigDecimal(binding.sixminPreEtDistanceHigh.text.toString()).compareTo(
                                BigDecimal("10000")
                            ) != -1
                        ) {
                            showMsg("请检查运动距离值，最大值为9999.9")
                            return@setNoRepeatListener
                        }
                        //前者不可大于后者
                        if (BigDecimal(binding.sixminPreEtDistanceLow.text.toString()) > BigDecimal(
                                binding.sixminPreEtDistanceHigh.text.toString()
                            )
                        ) {
                            showMsg("请检查运动距离值,起始值不可大于终止值")
                            return@setNoRepeatListener
                        }
                        sixMinReportPrescription.strideBefore =
                            binding.sixminPreEtStrideLow.text.toString()
                        sixMinReportPrescription.strideAfter =
                            binding.sixminPreEtStrideHigh.text.toString()
                        sixMinReportPrescription.movementDistance =
                            binding.sixminPreEtDistanceLow.text.toString()
                        sixMinReportPrescription.movementDistanceAfter =
                            binding.sixminPreEtDistanceHigh.text.toString()
                    } else {
                        sixMinReportPrescription.strideBefore = "0"
                        sixMinReportPrescription.strideAfter = "0"
                        sixMinReportPrescription.movementDistance = "0"
                        sixMinReportPrescription.movementDistanceAfter = "0"
                    }

                    //运动时长
                    sixMinReportPrescription.movementTime =
                        binding.sixminPreSpSportTime.value.toString()
                    if (sixMinReportPrescription.heartrateState.isEmpty() || sixMinReportPrescription.heartrateState == "1") {
                        val heartEcg = binding.sixminPreEtSportEcg.text.toString()
                        if (heartEcg.isNotEmpty() && heartEcg.length > 30) {
                            showMsg("请检查运动心率值")
                            return@setNoRepeatListener
                        }
                    }
                    sixMinReportPrescription.heartrateRate =
                        binding.sixminPreEtSportEcg.text.toString()

                    if (sixMinReportPrescription.metabState.isEmpty() || sixMinReportPrescription.metabState == "1") {
                        val metab = binding.sixminPreEtMetab.text.toString()
                        if (metab.isNotEmpty() && metab.length > 30) {
                            showMsg("请检查代谢当量值")
                            return@setNoRepeatListener
                        }
                    }
                    sixMinReportPrescription.metabMet = binding.sixminPreEtMetab.text.toString()

                    if (sixMinReportPrescription.pllevState.isEmpty() || sixMinReportPrescription.pllevState == "1") {
                        val tireLow = binding.sixminPreEtTiredControlLow.text.toString()
                        val tireHigh = binding.sixminPreEtTiredControlHigh.text.toString()
                        if (tireLow.isEmpty() || tireHigh.isEmpty()) {
                            showMsg("请检查疲劳程度控制")
                            return@setNoRepeatListener
                        }
                        if (tireLow.toInt() < 0 || tireLow.toInt() > 10 || tireHigh.toInt() < 0 || tireHigh.toInt() > 10) {
                            showMsg("请检查疲劳程度值，最大值为10")
                            return@setNoRepeatListener
                        }

                        if (tireLow.toInt() > tireHigh.toInt()) {
                            showMsg("请检查疲劳程度控制，起始值不可大于终止值")
                            return@setNoRepeatListener
                        }
                    }
                    sixMinReportPrescription.pllevBefore =
                        binding.sixminPreEtTiredControlLow.text.toString()
                    sixMinReportPrescription.pllevAfter =
                        binding.sixminPreEtTiredControlHigh.text.toString()
                    if (sixMinReportPrescription.pllevState == "1" || sixMinReportPrescription.pllevState.isEmpty()) {
                        sixMinReportPrescription.pilaoControl = usbTransferUtil.dealPiLaoKZ(
                            binding.sixminPreEtTiredControlLow.text.toString().toInt(),
                            binding.sixminPreEtTiredControlHigh.text.toString().toInt()
                        )
                    }

                    val doctorName = binding.sixminEtRecommendDoctor.text.toString()
                    if (doctorName.isEmpty() || doctorName.length > 5) {
                        showMsg("建议医生，长度不能大于5")
                        return@setNoRepeatListener
                    }
                    sixMinReportPrescription.remarkeName =
                        binding.sixminEtRecommendDoctor.text.toString()
                    sixMinReportPrescription.movementWay =
                        if (binding.sixminRbSportTypeWalk.isChecked) "0" else "1"
                    sixMinReportPrescription.movementWeeklyNumber =
                        binding.sixminPreSpWeeklyCount.value.toString()
                    sixMinReportPrescription.movementCycle =
                        binding.sixminPreSpPrescriptionPeriod.value.toString()
                    sixMinReportPrescription.cycleUnit =
                        if (binding.sixminRbPrescriptionCycleWeek.isChecked) "0" else if (binding.sixminRbPrescriptionCycleMonth.isChecked) "1" else "2"

                    viewModel.updateSixMinReportPrescription(sixMinReportPrescription)
                    viewModel.updateSixMinReportEvaluation(sixMinReportEvaluation)
                    viewModel.updateSixMinReportInfo(sixMinReportInfo)

                    val intent = Intent(
                        this@SixMinPreReportActivity,
                        SixMinReportActivity::class.java
                    )
                    val bundle = Bundle()
                    bundle.putString(Constants.sixMinPatientInfo, sixMinPatientId)
                    bundle.putString(Constants.sixMinReportNo, sixMinReportNo)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                } else {
                    //保存报告

                    val heartBeatConclusion = binding.sixminEtHeartBeatConclusion.text.toString()
                    if (heartBeatConclusion.isNotEmpty() && heartBeatConclusion.length > 44) {
                        showMsg("请检查心电结论，最大长度为44")
                        return@setNoRepeatListener
                    }
                    sixMinReportHeartBeat.heartConclusion = heartBeatConclusion

                    //运动处方
                    if (sixMinReportPrescription.distanceState.isEmpty() || sixMinReportPrescription.distanceState == "1") {
                        if (binding.sixminPreEtStrideLow.text.toString()
                                .isEmpty() || binding.sixminPreEtStrideHigh.text.toString()
                                .isEmpty()
                        ) {
                            showMsg("请检查运动步速值")
                            return@setNoRepeatListener
                        }
                        if (!CommonUtil.checkNum(
                                binding.sixminPreEtStrideLow.text.toString(),
                                "0+"
                            ) || !CommonUtil.checkNum(
                                binding.sixminPreEtStrideHigh.text.toString(),
                                "0+"
                            )
                        ) {
                            showMsg("请检查运动步速值，最多精确到1位")
                            return@setNoRepeatListener
                        }
                        if (BigDecimal(binding.sixminPreEtStrideLow.text.toString()).compareTo(
                                BigDecimal("1000")
                            ) != -1 ||
                            BigDecimal(binding.sixminPreEtStrideHigh.text.toString()).compareTo(
                                BigDecimal("1000")
                            ) != -1
                        ) {
                            showMsg("请检查运动步速值，最大值为999.9")
                            return@setNoRepeatListener
                        }
                        //前者不可大于后者
                        if (BigDecimal(binding.sixminPreEtStrideLow.text.toString()) > BigDecimal(
                                binding.sixminPreEtStrideHigh.text.toString()
                            )
                        ) {
                            showMsg("请检查运动步速值,起始值不可大于终止值")
                            return@setNoRepeatListener
                        }

                        if (binding.sixminPreEtDistanceLow.text.toString()
                                .isEmpty() || binding.sixminPreEtDistanceHigh.text.toString()
                                .isEmpty()
                        ) {
                            showMsg("请检查运动距离值")
                            return@setNoRepeatListener
                        }
                        if (!CommonUtil.checkNum(
                                binding.sixminPreEtDistanceLow.text.toString(),
                                "0+"
                            ) || !CommonUtil.checkNum(
                                binding.sixminPreEtDistanceHigh.text.toString(),
                                "0+"
                            )
                        ) {
                            showMsg("请检查运动距离值，最多精确到1位")
                            return@setNoRepeatListener
                        }
                        if (BigDecimal(binding.sixminPreEtDistanceLow.text.toString()).compareTo(
                                BigDecimal("10000")
                            ) != -1 ||
                            BigDecimal(binding.sixminPreEtDistanceHigh.text.toString()).compareTo(
                                BigDecimal("10000")
                            ) != -1
                        ) {
                            showMsg("请检查运动距离值，最大值为9999.9")
                            return@setNoRepeatListener
                        }
                        //前者不可大于后者
                        if (BigDecimal(binding.sixminPreEtDistanceLow.text.toString()) > BigDecimal(
                                binding.sixminPreEtDistanceHigh.text.toString()
                            )
                        ) {
                            showMsg("请检查运动距离值,起始值不可大于终止值")
                            return@setNoRepeatListener
                        }
                        sixMinReportPrescription.strideBefore =
                            binding.sixminPreEtStrideLow.text.toString()
                        sixMinReportPrescription.strideAfter =
                            binding.sixminPreEtStrideHigh.text.toString()
                        sixMinReportPrescription.movementDistance =
                            binding.sixminPreEtDistanceLow.text.toString()
                        sixMinReportPrescription.movementDistanceAfter =
                            binding.sixminPreEtDistanceHigh.text.toString()
                    } else {
                        sixMinReportPrescription.strideBefore = "0"
                        sixMinReportPrescription.strideAfter = "0"
                        sixMinReportPrescription.movementDistance = "0"
                        sixMinReportPrescription.movementDistanceAfter = "0"
                    }

                    //运动时长
                    sixMinReportPrescription.movementTime =
                        binding.sixminPreSpSportTime.value.toString()
                    if (sixMinReportPrescription.heartrateState.isEmpty() || sixMinReportPrescription.heartrateState == "1") {
                        val heartEcg = binding.sixminPreEtSportEcg.text.toString()
                        if (heartEcg.isNotEmpty() && heartEcg.length > 30) {
                            showMsg("请检查运动心率值")
                            return@setNoRepeatListener
                        }
                    }
                    sixMinReportPrescription.heartrateRate =
                        binding.sixminPreEtSportEcg.text.toString()

                    if (sixMinReportPrescription.metabState.isEmpty() || sixMinReportPrescription.metabState == "1") {
                        val metab = binding.sixminPreEtMetab.text.toString()
                        if (metab.isNotEmpty() && metab.length > 30) {
                            showMsg("请检查代谢当量值")
                            return@setNoRepeatListener
                        }
                    }
                    sixMinReportPrescription.metabMet = binding.sixminPreEtMetab.text.toString()

                    if (sixMinReportPrescription.pllevState.isEmpty() || sixMinReportPrescription.pllevState == "1") {
                        val tireLow = binding.sixminPreEtTiredControlLow.text.toString()
                        val tireHigh = binding.sixminPreEtTiredControlHigh.text.toString()
                        if (tireLow.isEmpty() || tireHigh.isEmpty()) {
                            showMsg("请检查疲劳程度控制")
                            return@setNoRepeatListener
                        }
                        if (tireLow.toInt() < 0 || tireLow.toInt() > 10 || tireHigh.toInt() < 0 || tireHigh.toInt() > 10) {
                            showMsg("请检查疲劳程度值，最大值为10")
                            return@setNoRepeatListener
                        }

                        if (tireLow.toInt() > tireHigh.toInt()) {
                            showMsg("请检查疲劳程度控制，起始值不可大于终止值")
                            return@setNoRepeatListener
                        }
                    }
                    sixMinReportPrescription.pllevBefore =
                        binding.sixminPreEtTiredControlLow.text.toString()
                    sixMinReportPrescription.pllevAfter =
                        binding.sixminPreEtTiredControlHigh.text.toString()
                    if (sixMinReportPrescription.pllevState == "1" || sixMinReportPrescription.pllevState.isEmpty()) {
                        sixMinReportPrescription.pilaoControl = usbTransferUtil.dealPiLaoKZ(
                            binding.sixminPreEtTiredControlLow.text.toString().toInt(),
                            binding.sixminPreEtTiredControlHigh.text.toString().toInt()
                        )
                    }

                    val doctorName = binding.sixminEtRecommendDoctor.text.toString()
                    if (doctorName.isEmpty() || doctorName.length > 5) {
                        showMsg("建议医生，长度不能大于5")
                        return@setNoRepeatListener
                    }
                    sixMinReportPrescription.remarkeName =
                        binding.sixminEtRecommendDoctor.text.toString()
                    sixMinReportPrescription.movementWay =
                        if (binding.sixminRbSportTypeWalk.isChecked) "0" else "1"
                    sixMinReportPrescription.movementWeeklyNumber =
                        binding.sixminPreSpWeeklyCount.value.toString()
                    sixMinReportPrescription.movementCycle =
                        binding.sixminPreSpPrescriptionPeriod.value.toString()
                    sixMinReportPrescription.cycleUnit =
                        if (binding.sixminRbPrescriptionCycleWeek.isChecked) "0" else if (binding.sixminRbPrescriptionCycleMonth.isChecked) "1" else "2"

                    viewModel.updateSixMinReportPrescription(sixMinReportPrescription)
                    viewModel.updateSixMinReportEvaluation(sixMinReportEvaluation)
                    viewModel.updateSixMinReportInfo(sixMinReportInfo)

                    val intent = Intent(
                        this@SixMinPreReportActivity,
                        SixMinReportActivity::class.java
                    )
                    val bundle = Bundle()
                    bundle.putString(Constants.sixMinPatientInfo, sixMinPatientId)
                    bundle.putString(Constants.sixMinReportNo, sixMinReportNo)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.sixminReportIvClose.setNoRepeatListener {
            if (sixMinReportPrescription.movementWay.isEmpty()) {
                val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                    supportFragmentManager, "退出将视为放弃生成报告，是否确定?"
                )
                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                    CommonDialogFragment.CommonDialogClickListener {
                    override fun onPositiveClick() {
                        viewModel.deleteSixMinReportInfoReal(sixMinReportPrescription.reportId)
                        finish()
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

        binding.sixminPreSpSportTime.setOnValueChangeListener { _, value ->
            dealTimeSportTimeChange(value)
        }
    }

    /**
     * 选择处方参数
     */
    private fun dealSelectPrescriptionElement() {
        if (sixMinReportPrescription.distanceState == "1" || sixMinReportPrescription.distanceState == "") {
            if (sixMinReportPrescription.strideFormula == "0" || sixMinReportPrescription.strideFormula == "") {
                binding.sixminPreEtStrideLow.setText(usbTransferUtil.dealYdbsStrs("0.5", strideAvg))
                binding.sixminPreEtStrideHigh.setText(
                    usbTransferUtil.dealYdbsStrs(
                        "0.6", strideAvg
                    )
                )
            } else {
                binding.sixminPreEtStrideLow.setText(usbTransferUtil.dealYdbsStrs("0.7", strideAvg))
                binding.sixminPreEtStrideHigh.setText(
                    usbTransferUtil.dealYdbsStrs(
                        "0.8", strideAvg
                    )
                )
            }
            if (sixMinReportPrescription.distanceFormula == "0") {
                binding.sixminPreEtDistanceLow.setText(
                    usbTransferUtil.dealtjjlStrs(
                        "0.5",
                        BigDecimal(if (sixMinReportStride.strideAverage == "") "0.00" else sixMinReportStride.strideAverage),
                        binding.sixminPreSpSportTime.value
                    )
                )
                binding.sixminPreEtDistanceHigh.setText(
                    usbTransferUtil.dealtjjlStrs(
                        "0.6",
                        BigDecimal(if (sixMinReportStride.strideAverage == "") "0.00" else sixMinReportStride.strideAverage),
                        binding.sixminPreSpSportTime.value
                    )
                )
            } else {
                binding.sixminPreEtDistanceLow.setText(
                    usbTransferUtil.dealtjjlStrs(
                        "0.7",
                        BigDecimal(if (sixMinReportStride.strideAverage == "") "0.00" else sixMinReportStride.strideAverage),
                        binding.sixminPreSpSportTime.value
                    )
                )
                binding.sixminPreEtDistanceHigh.setText(
                    usbTransferUtil.dealtjjlStrs(
                        "0.8",
                        BigDecimal(if (sixMinReportStride.strideAverage == "") "0.00" else sixMinReportStride.strideAverage),
                        binding.sixminPreSpSportTime.value
                    )
                )
            }
            binding.sixminPreEtStrideLow.isEnabled = true
            binding.sixminPreEtStrideHigh.isEnabled = true
            binding.sixminPreEtDistanceLow.isEnabled = true
            binding.sixminPreEtDistanceHigh.isEnabled = true
        } else {
            binding.sixminPreEtStrideLow.setText("\\")
            binding.sixminPreEtStrideHigh.setText("\\")
            binding.sixminPreEtDistanceLow.setText("\\")
            binding.sixminPreEtDistanceHigh.setText("\\")

            binding.sixminPreEtStrideLow.isEnabled = false
            binding.sixminPreEtStrideHigh.isEnabled = false
            binding.sixminPreEtDistanceLow.isEnabled = false
            binding.sixminPreEtDistanceHigh.isEnabled = false
        }

        if (sixMinReportPrescription.heartrateState == "1" || sixMinReportPrescription.heartrateState == "") {
            binding.sixminPreEtSportEcg.setText(sixMinReportPrescription.heartrateRate)
            binding.sixminPreEtSportEcg.isEnabled = true
        } else {
            binding.sixminPreEtSportEcg.setText("\\")
            binding.sixminPreEtSportEcg.isEnabled = false
        }

        if (sixMinReportPrescription.metabState == "1" || sixMinReportPrescription.metabState == "") {
            binding.sixminPreEtMetab.setText(sixMinReportPrescription.metabMet)
            binding.sixminPreEtMetab.isEnabled = true
        } else {
            binding.sixminPreEtMetab.setText("\\")
            binding.sixminPreEtMetab.isEnabled = false
        }

        if (sixMinReportPrescription.pllevState == "1" || sixMinReportPrescription.pllevState == "") {
            binding.sixminPreEtTiredControlLow.setText(sixMinReportPrescription.pllevBefore)
            binding.sixminPreEtTiredControlHigh.setText(sixMinReportPrescription.pllevAfter)

            binding.sixminPreEtTiredControlLow.isEnabled = true
            binding.sixminPreEtTiredControlHigh.isEnabled = true
        } else {
            binding.sixminPreEtTiredControlLow.setText("\\")
            binding.sixminPreEtTiredControlHigh.setText("\\")

            binding.sixminPreEtTiredControlLow.isEnabled = false
            binding.sixminPreEtTiredControlHigh.isEnabled = false
        }
    }

    private fun beanQuery(any: Any) {
        try {
            if (any is List<*>) {
                val datas = any as MutableList<*>
                sixMinRecordsBean = datas[0] as SixMinRecordsBean

                sixMinReportPrescription = sixMinRecordsBean.prescriptionBean[0]
                sixMinReportPrescription.prescripState = "1"
                sixMinReportBloodOther = sixMinRecordsBean.otherBean[0]
                sixMinReportStride = sixMinRecordsBean.strideBean[0]
                sixMinReportEvaluation = sixMinRecordsBean.evaluationBean[0]
                sixMinReportHeartBeat = sixMinRecordsBean.heartBeatBean[0]
                sixMinReportInfo = sixMinRecordsBean.infoBean

                initTable()
                initSelfCheck()

                Log.d("sixMinRecordsBean", Gson().toJson(sixMinRecordsBean))

                binding.sixminEtFinishCircle.setText(sixMinRecordsBean.evaluationBean[0].turnsNumber)
                binding.sixminEtFinishCircle.isEnabled =
                    sixMinReportPrescription.movementWay.isEmpty()

                binding.sixminEtUnfinishCircle.setText(sixMinRecordsBean.evaluationBean[0].unfinishedDistance)
                binding.sixminEtUnfinishCircle.isEnabled =
                    sixMinReportPrescription.movementWay.isEmpty()

                binding.sixminTvTotalDistance.text = Html.fromHtml(
                    String.format(
                        getString(R.string.sixmin_test_report_total_distance),
                        sixMinRecordsBean.evaluationBean[0].totalDistance
                    )
                )
                val stopTime = sixMinRecordsBean.otherBean[0].stopTime
                val type = sixMinRecordsBean.otherBean[0].stopOr
                strideAvg = usbTransferUtil.dealStrideAvg(
                    BigDecimal(sixMinRecordsBean.evaluationBean[0].totalDistance),
                    type.toInt(),
                    stopTime
                )
                val cardiopuDegreeStr = when (sixMinRecordsBean.evaluationBean[0].cardiopuDegree) {
                    "1" -> "重度"
                    "2" -> "中度"
                    else -> "轻度"
                }
                binding.sixminTvDataStatistics.text = Html.fromHtml(
                    String.format(
                        getString(R.string.sixmin_test_report_data_statistics),
                        sixMinRecordsBean.evaluationBean[0].totalDistance,
                        strideAvg,
                        sixMinRecordsBean.evaluationBean[0].metabEquivalent,
                        cardiopuDegreeStr,
                        sixMinRecordsBean.evaluationBean[0].cardiopuLevel,
                    )
                )
                binding.sixminPreEtResetTime.isEnabled =
                    sixMinReportPrescription.movementWay.isEmpty() || sixMinReportPrescription.movementWay == "0"
                if (sixMinReportInfo.restDuration != "-1") {
                    binding.sixminPreLlResetTime.visibility = View.VISIBLE
                    binding.sixminPreEtResetTime.setText(sixMinReportInfo.restDuration)
                } else {
                    binding.sixminPreLlResetTime.visibility = View.GONE
                }
                if (sixMinReportPrescription.movementWay.isEmpty() || sixMinReportPrescription.movementWay == "0") {
                    binding.sixminRbSportTypeWalk.isChecked = true
                } else {
                    binding.sixminRbSportTypeRun.isChecked = true
                }

                if (sixMinReportPrescription.movementTime != "") {
                    binding.sixminPreSpSportTime.value =
                        sixMinReportPrescription.movementTime.toInt()
                }

                if (sixMinReportPrescription.movementWay.isEmpty()) {
                    binding.sixminReportTvGenerateReport.text = "生成报告"
                } else {
                    binding.sixminReportTvGenerateReport.text = "保存"
                }

                updatePrescriptionView()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updatePrescriptionView() {
        try {
            //运动步速
            var ydbsStr1 = ""
            var ydbsStr2 = ""
            if (sixMinReportPrescription.movementWay.isEmpty()) {
                ydbsStr1 = usbTransferUtil.dealYdbsStrs(
                    "0.5",
                    BigDecimal(if (sixMinReportStride.strideAverage == "") "0.00" else sixMinReportStride.strideAverage)
                )
                ydbsStr2 = usbTransferUtil.dealYdbsStrs(
                    "0.6",
                    BigDecimal(if (sixMinReportStride.strideAverage == "") "0.00" else sixMinReportStride.strideAverage)
                )
            } else {
                //强度版本
                if (sixMinReportPrescription.prescripState == "0") {
                    ydbsStr1 = usbTransferUtil.dealYdbsStrs("0.5", strideAvg)
                    ydbsStr2 = usbTransferUtil.dealYdbsStrs("0.6", strideAvg)
                } else if (sixMinReportPrescription.prescripState == "1") {
                    //验证是否出具
                    if (sixMinReportPrescription.distanceState == "1" || sixMinReportPrescription.distanceState.isEmpty()) {
                        ydbsStr1 = sixMinReportPrescription.strideBefore
                        ydbsStr2 = sixMinReportPrescription.strideAfter
                    } else if (sixMinReportPrescription.distanceState == "2") {
                        ydbsStr1 = "/"
                        ydbsStr2 = "/"
                    }
                }
            }

            binding.sixminPreEtStrideLow.setText(ydbsStr1)
            binding.sixminPreEtStrideHigh.setText(ydbsStr2)

            //运动周期
            var zhouqiStrs: Array<String>? = arrayOf()
            zhouqiStrs = if (sixMinReportPrescription.movementWay.isEmpty()) {
                val cf3qiangduStrs: Array<String> =
                    usbTransferUtil.dealQiangdu(BigDecimal(sixMinReportEvaluation.metabEquivalent))
                usbTransferUtil.dealzhouqi(cf3qiangduStrs)
            } else {
                val sj: String = sixMinReportPrescription.movementTime
                val mn: String = sixMinReportPrescription.movementWeeklyNumber
                arrayOf(sj, mn)
            }

            if (!zhouqiStrs.isNullOrEmpty()) {
                binding.sixminPreSpSportTime.value = zhouqiStrs[0].toInt()
                binding.sixminPreSpWeeklyCount.value = zhouqiStrs[1].toInt()
            }

            //运动距离
            var ydjlStr1 = ""
            var ydjlStr2 = ""
            if (sixMinReportPrescription.movementWay.isEmpty()) {
                ydjlStr1 = usbTransferUtil.dealtjjlStrs(
                    "0.5",
                    BigDecimal(if (sixMinReportStride.strideAverage == "") "0.00" else sixMinReportStride.strideAverage),
                    zhouqiStrs!![0].toInt()
                )
                ydjlStr2 = usbTransferUtil.dealtjjlStrs(
                    "0.6",
                    BigDecimal(if (sixMinReportStride.strideAverage == "") "0.00" else sixMinReportStride.strideAverage),
                    zhouqiStrs[0].toInt()
                )
            } else {
                //强度版本
                if (sixMinReportPrescription.prescripState == "0") {
                    ydjlStr1 = sixMinReportPrescription.movementDistance
                    ydjlStr2 = sixMinReportPrescription.movementDistance
                } else if (sixMinReportPrescription.prescripState == "1") {
                    //验证是否出具
                    if (sixMinReportPrescription.distanceState == "1" || sixMinReportPrescription.distanceState.isEmpty()) {
                        ydjlStr1 = sixMinReportPrescription.movementDistance
                        ydjlStr2 = sixMinReportPrescription.movementDistanceAfter
                    } else if (sixMinReportPrescription.distanceState == "2") {
                        ydjlStr1 = "/"
                        ydjlStr2 = "/"
                    }
                }
            }
            binding.sixminPreEtDistanceLow.setText(ydjlStr1)
            binding.sixminPreEtDistanceHigh.setText(ydjlStr2)

            //运动心率
            var ydxlStr = ""
            if (sixMinReportPrescription.movementWay.isNotEmpty()) {
                //验证是否出具
                if (sixMinReportPrescription.heartrateState == "1" || sixMinReportPrescription.heartrateState.isEmpty()) {
                    ydxlStr = sixMinReportPrescription.heartrateRate
                } else if (sixMinReportPrescription.heartrateState == "2") {
                    ydxlStr = "/"
                }
            }
            binding.sixminPreEtSportEcg.setText(ydxlStr)

            //代谢当量
            var dxdlStr = ""
            if (sixMinReportPrescription.movementWay.isNotEmpty()) {
                //验证是否出具
                if (sixMinReportPrescription.metabState == "1" || sixMinReportPrescription.metabState.isEmpty()) {
                    dxdlStr = sixMinReportPrescription.metabMet
                } else if (sixMinReportPrescription.metabState == "2") {
                    dxdlStr = "/"
                }
            }
            binding.sixminPreEtMetab.setText(dxdlStr)

            //疲劳控制
            var plzhi1Str = "4"
            var plzhi2Str = "6"
            if (sixMinReportPrescription.movementWay.isNotEmpty()) {
                //验证是否出具
                if (sixMinReportPrescription.pllevState == "1" || sixMinReportPrescription.pllevState.isEmpty()) {
                    plzhi1Str = sixMinReportPrescription.pllevBefore
                    plzhi2Str = sixMinReportPrescription.pllevAfter
                } else if (sixMinReportPrescription.metabState == "2") {
                    plzhi1Str = "/"
                    plzhi2Str = "/"
                }
            }
            binding.sixminPreEtTiredControlLow.setText(plzhi1Str)
            binding.sixminPreEtTiredControlHigh.setText(plzhi2Str)

            //处方周期
            var prescriptionPeriod = "8"
            if (sixMinReportPrescription.movementWay.isNotEmpty()) {
                if (sixMinReportPrescription.movementCycle.isNotEmpty()) {
                    prescriptionPeriod = sixMinReportPrescription.movementCycle
                }
            }
            binding.sixminPreSpPrescriptionPeriod.value = prescriptionPeriod.toInt()

            when (sixMinReportPrescription.cycleUnit) {
                "0" -> {
                    binding.sixminRbPrescriptionCycleWeek.isChecked = true
                }

                "1" -> {
                    binding.sixminRbPrescriptionCycleMonth.isChecked = true
                }

                else -> {
                    binding.sixminRbPrescriptionCycleYear.isChecked = true
                }
            }

            //试验结论
            if (sixMinReportBloodOther.stopOr == "1") {
                var reasonStr: String = sixMinReportBloodOther.stopReason
                if (reasonStr.isEmpty()) {
                    reasonStr = "无"
                }
                binding.sixminTvPrescriptionConclusion.text = Html.fromHtml(
                    String.format(
                        getString(R.string.sixmin_test_report_test_conclusion_unfinish),
                        sixMinReportBloodOther.stopTime,
                        reasonStr
                    )
                )
            } else if (sixMinReportBloodOther.stopOr == "0") {
                var badSymptomsStr: String = sixMinReportBloodOther.badSymptoms
                if (badSymptomsStr.isEmpty()) {
                    badSymptomsStr = "无"
                }
                binding.sixminTvPrescriptionConclusion.text = Html.fromHtml(
                    String.format(
                        getString(R.string.sixmin_test_report_test_conclusion_finish),
                        badSymptomsStr
                    )
                )
            }

            //注意事项
            if (sixMinReportPrescription.movementWay.isNotEmpty() && sixMinReportPrescription.remarke.isNotEmpty()) {
                binding.sixminEtReportNote.setText(sixMinReportPrescription.remarke)
            }

            binding.sixminEtRecommendDoctor.setText(sixMinReportPrescription.remarkeName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initTable() {
        binding.sixminReportTlPreTable.removeAllViews()
        reportRowList.clear()
        reportRowList.add(
            SixMinReportItemBean(
                "时间(min)", "静止", "1", "2", "3", "4", "5", "6", "最大值", "最小值", "平均值"
            )
        )
        reportRowList.add(
            SixMinReportItemBean(
                "心率(bpm)", "60", "60", "60", "60", "60", "60", "60", "60", "60", "60"
            )
        )
        reportRowList.add(
            SixMinReportItemBean(
                "血氧(%)",
                sixMinRecordsBean.bloodOxyBean[0].bloodStop,
                sixMinRecordsBean.bloodOxyBean[0].bloodOne,
                sixMinRecordsBean.bloodOxyBean[0].bloodTwo,
                sixMinRecordsBean.bloodOxyBean[0].bloodThree,
                sixMinRecordsBean.bloodOxyBean[0].bloodFour,
                sixMinRecordsBean.bloodOxyBean[0].bloodFive,
                sixMinRecordsBean.bloodOxyBean[0].bloodSix,
                sixMinRecordsBean.bloodOxyBean[0].bloodBig,
                sixMinRecordsBean.bloodOxyBean[0].bloodSmall,
                sixMinRecordsBean.bloodOxyBean[0].bloodAverage
            )
        )
        if (sixMinRecordsBean.infoBean.bsHxl == "0") {
            reportRowList.add(
                SixMinReportItemBean(
                    "步数",
                    sixMinRecordsBean.walkBean[0].walkStop,
                    sixMinRecordsBean.walkBean[0].walkOne,
                    sixMinRecordsBean.walkBean[0].walkTwo,
                    sixMinRecordsBean.walkBean[0].walkThree,
                    sixMinRecordsBean.walkBean[0].walkFour,
                    sixMinRecordsBean.walkBean[0].walkFive,
                    sixMinRecordsBean.walkBean[0].walkSix,
                    sixMinRecordsBean.walkBean[0].walkBig,
                    sixMinRecordsBean.walkBean[0].waklSmall,
                    sixMinRecordsBean.walkBean[0].walkAverage
                )
            )
        } else {
            reportRowList.add(
                SixMinReportItemBean(
                    "呼吸率",
                    sixMinRecordsBean.breathingBean[0].breathingStop,
                    sixMinRecordsBean.breathingBean[0].breathingOne,
                    sixMinRecordsBean.breathingBean[0].breathingTwo,
                    sixMinRecordsBean.breathingBean[0].breathingThree,
                    sixMinRecordsBean.breathingBean[0].breathingFour,
                    sixMinRecordsBean.breathingBean[0].breathingFive,
                    sixMinRecordsBean.breathingBean[0].breathingSix,
                    sixMinRecordsBean.breathingBean[0].breathingBig,
                    sixMinRecordsBean.breathingBean[0].breathingSmall,
                    sixMinRecordsBean.breathingBean[0].breathingAverage
                )
            )
        }
        val startHighBlood = sixMinRecordsBean.otherBean[0].startHighPressure
        val startLowBlood = sixMinRecordsBean.otherBean[0].startLowPressure
        val endHighBlood = sixMinRecordsBean.otherBean[0].stopHighPressure
        val endLowBlood = sixMinRecordsBean.otherBean[0].stopLowPressure
        reportRowList.add(
            SixMinReportItemBean(
                "血压(mmHg)",
                "$startHighBlood/$startLowBlood",
                "/",
                "/",
                "/",
                "/",
                "/",
                "$endHighBlood/$endLowBlood",
                "/",
                "/",
                "/"
            )
        )
        val padding = dip2px(1.0f)
        for (i in 0 until reportRowList.size) {
            val sixMinReportItemBean = reportRowList[i]
            val newRow = TableRow(applicationContext)
            val layoutParams = TableRow.LayoutParams()
            newRow.layoutParams = layoutParams

            val linearLayout = LinearLayout(
                applicationContext
            )
            linearLayout.orientation = LinearLayout.HORIZONTAL

            for (j in 0..10) {
                val tvNo = TextView(applicationContext)
                tvNo.textSize = dip2px(7.0f).toFloat()
                // 设置文字居中
                tvNo.gravity = if (j == 0) Gravity.START else Gravity.CENTER
                tvNo.setTextColor(ContextCompat.getColor(this, R.color.text3))
                // 设置表格中的数据不自动换行
                tvNo.setSingleLine()
                // 设置边框和weight
                val lpNo = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    if (j == 0) 4.2f else if (j == 1 || j == 7 || j == 8 || j == 9 || j == 10) 3f else 2f
                )
                lpNo.setMargins(
                    0, 0, dip2px(2.0f), 0
                )
                tvNo.layoutParams = lpNo
                // 设置padding和背景颜色
                tvNo.setPadding(padding, padding, padding, padding)
                // 填充文字数据
                tvNo.text = when (j) {
                    0 -> {
                        sixMinReportItemBean.itemName
                    }

                    1 -> {
                        sixMinReportItemBean.stillnessValue
                    }

                    2 -> {
                        sixMinReportItemBean.oneMinValue
                    }

                    3 -> {
                        sixMinReportItemBean.twoMinValue
                    }

                    4 -> {
                        sixMinReportItemBean.threeMinValue
                    }

                    5 -> {
                        sixMinReportItemBean.fourMinValue
                    }

                    6 -> {
                        sixMinReportItemBean.fiveMinValue
                    }

                    7 -> {
                        sixMinReportItemBean.sixMinValue
                    }

                    8 -> {
                        sixMinReportItemBean.maxValue
                    }

                    9 -> {
                        sixMinReportItemBean.minMinValue
                    }

                    else -> {
                        sixMinReportItemBean.avgMinValue
                    }
                }
                linearLayout.addView(tvNo)
            }
            newRow.setPadding(
                dip2px(6.0f), dip2px(3.0f), dip2px(6.0f), dip2px(3.0f)
            )
            newRow.addView(linearLayout)
            binding.sixminReportTlPreTable.addView(newRow)

//            // 创建分割线View
//            val divider = View(this)
//            divider.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1)
//            divider.setBackgroundColor(Color.GRAY) // 设置分割线颜色
//            // 添加分割线到TableLayout
//            val row = TableRow(this)
//            row.layoutParams = TableLayout.LayoutParams(
//                TableLayout.LayoutParams.MATCH_PARENT,
//                TableLayout.LayoutParams.WRAP_CONTENT
//            )
//            row.addView(divider)
//            binding.sixminReportTlPreTable.addView(row)
        }
    }

    private fun dip2px(dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.displayMetrics
        ).toInt()
    }

    override fun onResume() {
        SystemUtil.immersive(this, true)
        super.onResume()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            SystemUtil.immersive(this, true)
        }
    }
}