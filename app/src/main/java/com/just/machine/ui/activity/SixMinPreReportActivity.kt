package com.just.machine.ui.activity

import android.content.Intent
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
import com.just.machine.model.sixminreport.SixMinReportOther
import com.just.machine.model.sixminreport.SixMinReportPrescription
import com.just.machine.model.sixminreport.SixMinReportStride
import com.just.machine.ui.adapter.SixMinReportPatientSelfAdapter
import com.just.machine.ui.dialog.CommonDialogFragment
import com.just.machine.ui.dialog.SixMinReportEditBloodPressureFragment
import com.just.machine.ui.dialog.SixMinReportPrescriptionFragment
import com.just.machine.ui.dialog.SixMinReportSelfCheckBeforeTestFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.SnappingStepper
import com.just.machine.util.SpinnerHelper
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
    private var strideAvg: BigDecimal = BigDecimal(0.00)

    override fun getViewBinding() = ActivitySixMinPreReportBinding.inflate(layoutInflater)

    override fun initView() {
        usbTransferUtil = USBTransferUtil.getInstance()
        sixMinPatientId =
            intent.extras?.getString(Constants.sixMinPatientInfo).toString()
        sixMinReportNo =
            intent.extras?.getString(Constants.sixMinReportNo).toString()
        sixMinReportType =
            intent.extras?.getString(Constants.sixMinReportType).toString()
        patientSelfList.clear()
        val patientBreathSelfItemList = mutableListOf<SixMinReportPatientSelfItemBean>()
        patientBreathSelfItemList.clear()
        patientBreathSelfItemList.add(SixMinReportPatientSelfItemBean("0级", "没有"))
        patientBreathSelfItemList.add(SixMinReportPatientSelfItemBean("0.5级", "非常非常轻"))
        patientBreathSelfItemList.add(SixMinReportPatientSelfItemBean("1级", "非常轻"))
        patientBreathSelfItemList.add(SixMinReportPatientSelfItemBean("2级", "很轻"))
        patientBreathSelfItemList.add(SixMinReportPatientSelfItemBean("3级", "中度"))
        patientBreathSelfItemList.add(SixMinReportPatientSelfItemBean("4级", "较严重"))
        patientBreathSelfItemList.add(SixMinReportPatientSelfItemBean("5-6级", "严重"))
        patientBreathSelfItemList.add(SixMinReportPatientSelfItemBean("7-9级", "非常严重"))
        patientBreathSelfItemList.add(SixMinReportPatientSelfItemBean("10级", "非常非常严重"))
        patientSelfList.add(
            SixMinReportPatientSelfBean(
                "呼吸状况等级",
                "1",
                patientBreathSelfItemList
            )
        )
        val patientTiredSelfItemList = mutableListOf<SixMinReportPatientSelfItemBean>()
        patientTiredSelfItemList.clear()
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("0级", "没有"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("1级", "非常轻松"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("2级", "轻松"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("3级", "中度"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("4级", "有点疲劳"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("5-6级", "疲劳"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("7-8级", "非常疲劳"))
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "9-10级",
                "非常非常疲劳(几乎到极限)"
            )
        )
        patientSelfList.add(
            SixMinReportPatientSelfBean(
                "疲劳状况等级",
                "2",
                patientTiredSelfItemList
            )
        )
        binding.sixminRvPatientSelfCheck.layoutManager = LinearLayoutManager(this)
        val patientSelfItemAdapter = SixMinReportPatientSelfAdapter(this)
        patientSelfItemAdapter.setItemsBean(patientSelfList)
        binding.sixminRvPatientSelfCheck.adapter = patientSelfItemAdapter

        initClickListener()
        initValueChangeListener()

        lifecycleScope.launch {
            kotlinx.coroutines.delay(500L)
            viewModel.getSixMinReportInfoById(sixMinPatientId.toLong(), sixMinReportNo)
        }
    }

    private fun initValueChangeListener() {
        binding.sixminPreSpSportTime.setOnValueChangeListener { _, value ->
            sixMinReportPrescription.movementTime = value.toString()
            updatePrescriptionView()
        }

        binding.sixminPreSpWeeklyCount.setOnValueChangeListener(object:
            SnappingStepper.SnappingStepperValueChangeListener{
            override fun onValueChange(view: View?, value: Int) {

            }
        })

        binding.sixminPreSpPrescriptionPeriod.setOnValueChangeListener(object:
            SnappingStepper.SnappingStepperValueChangeListener{
            override fun onValueChange(view: View?, value: Int) {

            }
        })
    }

    private fun initClickListener() {
        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.QuerySixMinReportInfoSuccess -> {
                    it.any?.let { it1 -> beanQuery(it1) }
                }
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
                        supportFragmentManager,
                        bean
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
                    supportFragmentManager,
                    "0", "", selfCheckSelection
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
                    borg: String
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
                        sixMinReportPrescription.prescripState = "1"
                    } else {
                        sixMinReportPrescription.prescripState = "2"
                    }
                    updatePrescriptionView()
                }

                override fun onClickClose() {

                }

            })
        }
        binding.sixminReportIvGenerateReport.setNoRepeatListener {
            selectStrList.clear()
            patientSelfList.forEach {
                it.itemList.forEach { it1 ->
                    if (it1.itemCheck == "1") {
                        val index = it1.itemName.indexOf("级")
                        selectStrList.add("${it.itemName}&${it1.itemName.substring(0, index)}")
                    }
                }
            }
            if (selectStrList.isNotEmpty()) {
                if (selectStrList.size > 1) {
                    viewModel.updateSixMinReportEvaluation(
                        sixMinRecordsBean.evaluationBean[0].reportId,
                        selectStrList[1].split("&")[1],
                        selectStrList[0].split("&")[1]
                    )
                } else {
                    val split = selectStrList[0].split("&")
                    if (split[0] == "呼吸状况等级") {
                        showMsg("请选择疲劳状况等级")
                    } else {
                        showMsg("请选择呼吸状况等级")
                    }
                    return@setNoRepeatListener
                }
            } else {
                showMsg("请选择呼吸和疲劳状况等级")
                return@setNoRepeatListener
            }
            //生成报告
            startActivity(Intent(this, SixMinReportActivity::class.java))
            finish()
        }
        binding.sixminReportIvClose.setNoRepeatListener {
            val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                supportFragmentManager,
                "退出将视为放弃生成报告，是否确定?"
            )
            startCommonDialogFragment.setCommonDialogOnClickListener(object :
                CommonDialogFragment.CommonDialogClickListener {
                override fun onPositiveClick() {
                    finish()
                }

                override fun onNegativeClick() {

                }

                override fun onStopNegativeClick(stopReason: String) {

                }
            })
        }
        binding.sixminRbSportTypeWalk.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                sixMinReportPrescription.movementWay = "0"
            }
        }
        binding.sixminRbSportTypeRun.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                sixMinReportPrescription.movementWay = "1"
            }
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

                initTable()

                Log.d("sixMinRecordsBean", Gson().toJson(sixMinRecordsBean))

                binding.sixminTvFinishCircle.setText(sixMinRecordsBean.evaluationBean[0].turnsNumber)

                binding.sixminTvUnfinishCircle.setText(sixMinRecordsBean.evaluationBean[0].unfinishedDistance)

                binding.sixminTvTotalDistance.text =
                    Html.fromHtml(
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
                        sixMinRecordsBean.infoBean.restDuration
                    )
                )
                if (sixMinReportPrescription.movementWay == "" || sixMinReportPrescription.movementWay == "0") {
                    binding.sixminRbSportTypeWalk.isChecked = true
                } else {
                    binding.sixminRbSportTypeRun.isChecked = true
                }

                if(sixMinReportPrescription.movementTime != ""){
                    binding.sixminPreSpSportTime.value = sixMinReportPrescription.movementTime.toInt()
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
                    if (sixMinReportPrescription.distanceState == "1") {
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
                    if (sixMinReportPrescription.distanceState == "1") {
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
                if (sixMinReportPrescription.heartrateState == "1") {
                    ydxlStr = sixMinReportPrescription.heartrateRate
                } else if (sixMinReportPrescription.distanceState == "2") {
                    ydxlStr = "/"
                }
            }
            binding.sixminPreEtSportEcg.setText(ydxlStr)

            //代谢当量
            var dxdlStr = ""
            if (sixMinReportPrescription.movementWay.isNotEmpty()) {
                //验证是否出具
                if (sixMinReportPrescription.metabState == "1") {
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
                if (sixMinReportPrescription.pllevState == "1") {
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
            if (sixMinReportPrescription.movementWay.isEmpty()) {
                binding.sixminPreSpPrescriptionPeriod.value = sixMinReportPrescription.movementCycle.toInt()
                binding.sixminRbPrescriptionCycleWeek.isChecked = true
            } else {
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
            if(sixMinReportPrescription.movementWay.isNotEmpty() && sixMinReportPrescription.remarke.isNotEmpty()){
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
                "时间(min)",
                "静止",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "最大值",
                "最小值",
                "平均值"
            )
        )
        reportRowList.add(
            SixMinReportItemBean(
                "心率(bpm)",
                "60",
                "60",
                "60",
                "60",
                "60",
                "60",
                "60",
                "60",
                "60",
                "60"
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
                dip2px(6.0f),
                dip2px(3.0f),
                dip2px(6.0f),
                dip2px(3.0f)
            )
            newRow.addView(linearLayout)
            binding.sixminReportTlPreTable.addView(newRow)
        }
    }

    private fun dip2px(dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            resources.displayMetrics
        )
            .toInt()
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