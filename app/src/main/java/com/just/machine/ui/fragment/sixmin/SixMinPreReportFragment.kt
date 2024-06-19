package com.just.machine.ui.fragment.sixmin

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Editable
import android.text.Html
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.viewmodel.LiveDataEvent
import com.google.gson.Gson
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.model.SixMinReportEditBloodPressure
import com.just.machine.model.SixMinReportItemBean
import com.just.machine.model.SixMinReportPatientSelfBean
import com.just.machine.model.SixMinReportPatientSelfItemBean
import com.just.machine.ui.activity.SixMinDetectActivity
import com.just.machine.ui.adapter.SixMinReportPatientSelfAdapter
import com.just.machine.ui.dialog.SixMinReportEditBloodPressureFragment
import com.just.machine.ui.dialog.SixMinReportPrescriptionFragment
import com.just.machine.ui.dialog.SixMinReportSelfCheckBeforeTestFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.CommonUtil
import com.just.news.R
import com.just.news.databinding.FragmentSixminPreReportBinding
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

/**
 * 6分钟预生成报告界面
 */
@AndroidEntryPoint
class SixMinPreReportFragment : CommonBaseFragment<FragmentSixminPreReportBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var mActivity: SixMinDetectActivity
    private var reportRowList = mutableListOf<SixMinReportItemBean>()
    private var sixMinRecordsBean: SixMinRecordsBean = SixMinRecordsBean()//6分钟报告信息
    private var patientSelfList = mutableListOf<SixMinReportPatientSelfBean>()
    private var selectStrList = mutableListOf<String>()
    private var strideAvg: BigDecimal = BigDecimal(0.00)
    private var isFirst = true

    override fun loadData() {//懒加载

    }


    override fun initView() {
        if (activity is SixMinDetectActivity) {
            mActivity = activity as SixMinDetectActivity
        }
        binding.sixminEtReportNote.filters = arrayOf(InputFilter.LengthFilter(208))
        binding.sixminEtHeartBeatConclusion.filters = arrayOf(InputFilter.LengthFilter(44))
        binding.sixminEtRecommendDoctor.filters = arrayOf(InputFilter.LengthFilter(5))
        if (mActivity.sixMinReportType.isEmpty() || mActivity.sixMinReportType == "1") {
            showData(null)
        } else {
            viewModel.getSixMinReportInfoById(
                mActivity.sixMinPatientId.toLong(),
                mActivity.sixMinReportNo
            )
        }
        binding.sixminEtReportNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                binding.sixminPreTvNoteCount.text = "(${s.toString().trim().length}/208)"
            }
        })
        binding.sixminEtReportNote.setOnEditorActionListener { _, _, event -> (event.keyCode == KeyEvent.KEYCODE_ENTER); }
        binding.sixminEtHeartBeatConclusion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                binding.sixminPreTvConclusionCount.text = "(${s.toString().trim().length}/44)"
            }
        })
        binding.sixminEtHeartBeatConclusion.setOnEditorActionListener { _, _, event -> (event.keyCode == KeyEvent.KEYCODE_ENTER); }
        binding.sixminEtRecommendDoctor.setOnEditorActionListener { _, _, event -> (event.keyCode == KeyEvent.KEYCODE_ENTER); }
    }

    override fun initListener() {
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
                        mActivity.supportFragmentManager, bean
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
                                mActivity.showMsg("请检查试验前 收缩压值")
                                return
                            }
                            if (lowBloodPressureBefore?.isEmpty() == true) {
                                mActivity.showMsg("请检查试验前 舒张压值")
                                return
                            }
                        }
                        if (lowBloodPressureBefore?.isNotEmpty() == true) {
                            if (lowBloodPressureBefore.toInt() == 0 || lowBloodPressureBefore.toInt() > 999) {
                                mActivity.showMsg("请检查试验前 舒张压值")
                                return
                            }
                            if (highBloodPressureBefore?.isEmpty() == true) {
                                mActivity.showMsg("请检查试验前 收缩压值")
                                return
                            }
                        }

                        if (highBloodPressureAfter?.isNotEmpty() == true) {
                            if (highBloodPressureAfter.toInt() == 0 || highBloodPressureAfter.toInt() > 999) {
                                mActivity.showMsg("请检查试验后 收缩压值")
                                return
                            }
                            if (lowBloodPressureAfter?.isEmpty() == true) {
                                mActivity.showMsg("请检查试验后 舒张压值")
                                return
                            }
                        }

                        if (lowBloodPressureAfter?.isNotEmpty() == true) {
                            if (lowBloodPressureAfter.toInt() == 0 || lowBloodPressureAfter.toInt() > 999) {
                                mActivity.showMsg("请检查试验后 舒张压值")
                                return
                            }
                            if (highBloodPressureAfter?.isEmpty() == true) {
                                mActivity.showMsg("请检查试验后 收缩压值")
                                return
                            }
                        }
                        startEditBloodDialogFragment.dismiss()
                        if (mActivity.sixMinReportType.isEmpty() || mActivity.sixMinReportType == "1") {
                            mActivity.sixMinReportBloodOther.startHighPressure =
                                bean.highBloodPressureBefore.toString()
                            mActivity.sixMinReportBloodOther.startLowPressure =
                                bean.lowBloodPressureBefore.toString()
                            mActivity.sixMinReportBloodOther.stopHighPressure =
                                bean.highBloodPressureAfter.toString()
                            mActivity.sixMinReportBloodOther.stopLowPressure =
                                bean.lowBloodPressureAfter.toString()
                            initTable()
                        } else {
                            viewModel.updateSixMinReportOther(
                                sixMinRecordsBean.infoBean.reportNo,
                                bean.highBloodPressureBefore.toString(),
                                bean.lowBloodPressureBefore.toString(),
                                bean.highBloodPressureAfter.toString(),
                                bean.lowBloodPressureAfter.toString()
                            )
                            isFirst = true
                            viewModel.getSixMinReportInfoById(
                                mActivity.sixMinPatientId.toLong(),
                                mActivity.sixMinReportNo
                            )
                        }
                        binding.sixminReportTlPreTable.removeAllViews()
                    }
                })
            }
        }
        binding.sixminReportTvSelfCheckBeforeTest.setNoRepeatListener {
            var selfCheckSelection = ""
            if (mActivity.sixMinReportEvaluation.befoFatigueLevel != "" && mActivity.sixMinReportEvaluation.befoBreathingLevel != "") {
                selfCheckSelection =
                    "${mActivity.sixMinReportEvaluation.befoBreathingLevel}&${mActivity.sixMinReportEvaluation.befoFatigueLevel}"
            }
            val selfCheckBeforeTestDialogFragment =
                SixMinReportSelfCheckBeforeTestFragment.startPatientSelfCheckDialogFragment(
                    mActivity.supportFragmentManager, "0", "", selfCheckSelection
                )
            selfCheckBeforeTestDialogFragment.setSelfCheckBeforeTestDialogOnClickListener(object :
                SixMinReportSelfCheckBeforeTestFragment.SixMinReportSelfCheckBeforeTestDialogListener {
                override fun onClickConfirm(
                    befoFatigueLevel: Int,
                    befoBreathingLevel: Int,
                    befoFatigueLevelStr: String,
                    befoBreathingLevelStr: String,
                    faceMask: String
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
                    mActivity.supportFragmentManager,
                    mActivity.sixMinReportPrescription,
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
                        mActivity.sixMinReportPrescription.distanceState = "1"
                    } else {
                        mActivity.sixMinReportPrescription.distanceState = "2"
                    }
                    if (heart == "出具") {
                        mActivity.sixMinReportPrescription.heartrateState = "1"
                    } else {
                        mActivity.sixMinReportPrescription.heartrateState = "2"
                    }
                    if (metab == "出具") {
                        mActivity.sixMinReportPrescription.metabState = "1"
                    } else {
                        mActivity.sixMinReportPrescription.metabState = "2"
                    }
                    if (borg == "出具") {
                        mActivity.sixMinReportPrescription.pllevState = "1"
                    } else {
                        mActivity.sixMinReportPrescription.pllevState = "2"
                    }
                    mActivity.sixMinReportPrescription.strideFormula = strideFormula
                    mActivity.sixMinReportPrescription.distanceFormula = distanceFormula
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
                            mActivity.sixMinReportEvaluation.breathingLevel =
                                selectStrList[0].split("&")[1]
                            mActivity.sixMinReportEvaluation.fatigueLevel =
                                selectStrList[1].split("&")[1]
                        } else {
                            val split = selectStrList[0].split("&")
                            if (split[0] == "呼吸状况等级") {
                                mActivity.showMsg("请进行患者自评疲劳量级")
                            } else {
                                mActivity.showMsg("请进行患者自评呼吸量级")
                            }
                            return@setNoRepeatListener
                        }
                    } else {
                        mActivity.showMsg("请进行患者自评呼吸和疲劳量级")
                        return@setNoRepeatListener
                    }

                    val circleNum = binding.sixminEtFinishCircle.text.toString().trim()
                    if (circleNum.isEmpty() || circleNum.length > 2) {
                        mActivity.showMsg("请检查圈数值")
                        return@setNoRepeatListener
                    }
                    mActivity.sixMinReportEvaluation.turnsNumber = circleNum

                    var wqsBoolen = true
                    if (binding.sixminEtUnfinishCircle.text.toString().isNotEmpty()) {
                        val wqsDec: BigDecimal =
                            BigDecimal(binding.sixminEtUnfinishCircle.text.toString().trim())
                        val totoLength: Int =
                            mActivity.sixMinReportEvaluation.fieldLength.toInt() * 2
                        if (wqsDec.compareTo(BigDecimal(totoLength)) == 1) {
                            wqsBoolen = false
                        }
                    }
                    if (binding.sixminEtUnfinishCircle.text.toString().trim()
                            .isEmpty() || !wqsBoolen
                    ) {
                        mActivity.showMsg("请检查未走完的距离值")
                        return@setNoRepeatListener
                    }
                    mActivity.sixMinReportEvaluation.unfinishedDistance =
                        binding.sixminEtUnfinishCircle.text.toString()

                    if (mActivity.sysSettingBean.sysOther.showResetTime == "1") {
                        if (binding.sixminPreEtResetTime.text.toString().trim().isEmpty()) {
                            mActivity.showMsg("中途休息值不能为空")
                            return@setNoRepeatListener
                        }
                        if (binding.sixminPreEtResetTime.text.toString().trim().toInt() > 360) {
                            mActivity.showMsg("中途休息值不能大于360")
                            return@setNoRepeatListener
                        }
                        //提前停止
                        if (mActivity.sixMinReportBloodOther.stopOr == "1") {
                            val stopTimeStr: String = mActivity.sixMinReportBloodOther.stopTime
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
                                if (binding.sixminPreEtResetTime.text.toString().trim()
                                        .toInt() > stopTimeInt
                                ) {
                                    mActivity.showMsg("中途休息值不能大于步行时间")
                                    return@setNoRepeatListener
                                }
                            }
                        }
                    }
                    mActivity.sixMinReportInfo.restDuration =
                        binding.sixminPreEtResetTime.text.toString().trim()

                    val heartBeatConclusion =
                        binding.sixminEtHeartBeatConclusion.text.toString().trim()
                    if (heartBeatConclusion.isNotEmpty() && heartBeatConclusion.length > 44) {
                        mActivity.showMsg("请检查心电结论，最大长度为44")
                        return@setNoRepeatListener
                    }
                    mActivity.sixMinReportBloodHeart.heartConclusion = heartBeatConclusion

                    //运动处方
                    if (mActivity.sixMinReportPrescription.distanceState.isEmpty() || mActivity.sixMinReportPrescription.distanceState == "1") {
                        if (binding.sixminPreEtStrideLow.text.toString()
                                .isEmpty() || binding.sixminPreEtStrideHigh.text.toString()
                                .isEmpty()
                        ) {
                            mActivity.showMsg("请检查运动步速值")
                            return@setNoRepeatListener
                        }
                        if (!CommonUtil.checkNum(
                                binding.sixminPreEtStrideLow.text.toString().trim(),
                                "0+"
                            ) || !CommonUtil.checkNum(
                                binding.sixminPreEtStrideHigh.text.toString().trim(),
                                "0+"
                            )
                        ) {
                            mActivity.showMsg("请检查运动步速值，最多精确到1位")
                            return@setNoRepeatListener
                        }
                        if (BigDecimal(
                                binding.sixminPreEtStrideLow.text.toString().trim()
                            ).compareTo(
                                BigDecimal("1000")
                            ) != -1 ||
                            BigDecimal(
                                binding.sixminPreEtStrideHigh.text.toString().trim()
                            ).compareTo(
                                BigDecimal("1000")
                            ) != -1
                        ) {
                            mActivity.showMsg("请检查运动步速值，最大值为999.9")
                            return@setNoRepeatListener
                        }
                        //前者不可大于后者
                        if (BigDecimal(
                                binding.sixminPreEtStrideLow.text.toString().trim()
                            ) > BigDecimal(
                                binding.sixminPreEtStrideHigh.text.toString().trim()
                            )
                        ) {
                            mActivity.showMsg("请检查运动步速值,起始值不可大于终止值")
                            return@setNoRepeatListener
                        }

                        if (binding.sixminPreEtDistanceLow.text.toString().trim()
                                .isEmpty() || binding.sixminPreEtDistanceHigh.text.toString().trim()
                                .isEmpty()
                        ) {
                            mActivity.showMsg("请检查运动距离值")
                            return@setNoRepeatListener
                        }
                        if (!CommonUtil.checkNum(
                                binding.sixminPreEtDistanceLow.text.toString().trim(),
                                "0+"
                            ) || !CommonUtil.checkNum(
                                binding.sixminPreEtDistanceHigh.text.toString().trim(),
                                "0+"
                            )
                        ) {
                            mActivity.showMsg("请检查运动距离值，最多精确到1位")
                            return@setNoRepeatListener
                        }
                        if (BigDecimal(
                                binding.sixminPreEtDistanceLow.text.toString().trim()
                            ).compareTo(
                                BigDecimal("10000")
                            ) != -1 ||
                            BigDecimal(
                                binding.sixminPreEtDistanceHigh.text.toString().trim()
                            ).compareTo(
                                BigDecimal("10000")
                            ) != -1
                        ) {
                            mActivity.showMsg("请检查运动距离值，最大值为9999.9")
                            return@setNoRepeatListener
                        }
                        //前者不可大于后者
                        if (BigDecimal(
                                binding.sixminPreEtDistanceLow.text.toString().trim()
                            ) > BigDecimal(
                                binding.sixminPreEtDistanceHigh.text.toString().trim()
                            )
                        ) {
                            mActivity.showMsg("请检查运动距离值,起始值不可大于终止值")
                            return@setNoRepeatListener
                        }
                        mActivity.sixMinReportPrescription.strideBefore =
                            binding.sixminPreEtStrideLow.text.toString().trim()
                        mActivity.sixMinReportPrescription.strideAfter =
                            binding.sixminPreEtStrideHigh.text.toString().trim()
                        mActivity.sixMinReportPrescription.movementDistance =
                            binding.sixminPreEtDistanceLow.text.toString().trim()
                        mActivity.sixMinReportPrescription.movementDistanceAfter =
                            binding.sixminPreEtDistanceHigh.text.toString().trim()
                    } else {
                        mActivity.sixMinReportPrescription.strideBefore = "0"
                        mActivity.sixMinReportPrescription.strideAfter = "0"
                        mActivity.sixMinReportPrescription.movementDistance = "0"
                        mActivity.sixMinReportPrescription.movementDistanceAfter = "0"
                    }

                    //运动时长
                    mActivity.sixMinReportPrescription.movementTime =
                        binding.sixminPreSpSportTime.value.toString()
                    if (mActivity.sixMinReportPrescription.heartrateState.isEmpty() || mActivity.sixMinReportPrescription.heartrateState == "1") {
                        val heartEcg = binding.sixminPreEtSportEcg.text.toString().trim()
                        if (heartEcg.isEmpty() || heartEcg.length > 30) {
                            mActivity.showMsg("请检查运动心率值")
                            return@setNoRepeatListener
                        }
                        mActivity.sixMinReportPrescription.heartrateRate =
                            binding.sixminPreEtSportEcg.text.toString()
                    }

                    if (mActivity.sixMinReportPrescription.metabState.isEmpty() || mActivity.sixMinReportPrescription.metabState == "1") {
                        val metab = binding.sixminPreEtMetab.text.toString().trim()
                        if (metab.isEmpty() || metab.length > 30) {
                            mActivity.showMsg("请检查代谢当量值")
                            return@setNoRepeatListener
                        }
                        mActivity.sixMinReportPrescription.metabMet =
                            binding.sixminPreEtMetab.text.toString().trim()
                    }

                    if (mActivity.sixMinReportPrescription.pllevState.isEmpty() || mActivity.sixMinReportPrescription.pllevState == "1") {
                        val tireLow = binding.sixminPreEtTiredControlLow.text.toString().trim()
                        val tireHigh = binding.sixminPreEtTiredControlHigh.text.toString().trim()
                        if (tireLow.isEmpty() || tireHigh.isEmpty()) {
                            mActivity.showMsg("请检查疲劳程度控制")
                            return@setNoRepeatListener
                        }
                        if (tireLow.toInt() < 0 || tireLow.toInt() > 10 || tireHigh.toInt() < 0 || tireHigh.toInt() > 10) {
                            mActivity.showMsg("请检查疲劳程度值，最大值为10")
                            return@setNoRepeatListener
                        }

                        if (tireLow.toInt() > tireHigh.toInt()) {
                            mActivity.showMsg("请检查疲劳程度控制，起始值不可大于终止值")
                            return@setNoRepeatListener
                        }

                        mActivity.sixMinReportPrescription.pllevBefore =
                            tireLow
                        mActivity.sixMinReportPrescription.pllevAfter =
                            tireHigh
                        mActivity.sixMinReportPrescription.pilaoControl =
                            mActivity.usbTransferUtil.dealPiLaoKZ(
                                tireLow.toInt(),
                                tireHigh.toInt()
                            )
                    }

                    val doctorName = binding.sixminEtRecommendDoctor.text.toString().trim()
                    if (doctorName.isEmpty()) {
                        mActivity.showMsg("请检查建议医生")
                        return@setNoRepeatListener
                    }
                    if (doctorName.length > 5) {
                        mActivity.showMsg("建议医生，长度不能大于5")
                        return@setNoRepeatListener
                    }

                    val reportNote = binding.sixminEtReportNote.text.toString().trim()
                    if (reportNote.isNotEmpty()) {
                        if (reportNote.length > 208) {
                            mActivity.showMsg("运动注意事项，长度不能大于208")
                            return@setNoRepeatListener
                        }
                        if (reportNote.lines().size > 5) {
                            mActivity.showMsg("运动注意事项，行数不能大于5")
                            return@setNoRepeatListener
                        }
                    }

                    mActivity.sixMinReportPrescription.remarke =
                        binding.sixminEtReportNote.text.toString().trim()
                    mActivity.sixMinReportPrescription.remarkeName =
                        binding.sixminEtRecommendDoctor.text.toString().trim()
                    mActivity.sixMinReportPrescription.movementWay =
                        if (binding.sixminRbSportTypeWalk.isChecked) "0" else "1"
                    mActivity.sixMinReportPrescription.movementWeeklyNumber =
                        binding.sixminPreSpWeeklyCount.value.toString().trim()
                    mActivity.sixMinReportPrescription.movementCycle =
                        binding.sixminPreSpPrescriptionPeriod.value.toString().trim()
                    mActivity.sixMinReportPrescription.cycleUnit =
                        if (binding.sixminRbPrescriptionCycleWeek.isChecked) "0" else if (binding.sixminRbPrescriptionCycleMonth.isChecked) "1" else "2"

                    viewModel.setSixMinReportInfo(mActivity.sixMinReportInfo)
                    viewModel.setSixMinReportBloodOxyData(mActivity.sixMinReportBloodOxy)
                    viewModel.setSixMinReportWalkData(mActivity.sixMinReportWalk)
                    viewModel.setSixMinReportEvaluation(mActivity.sixMinReportEvaluation)
                    viewModel.setSixMinReportOther(mActivity.sixMinReportBloodOther)
                    viewModel.setSixMinReportPrescription(mActivity.sixMinReportPrescription)
                    viewModel.setSixMinReportBreathing(mActivity.sixMinReportBreathing)
                    viewModel.setSixMinReportHeartEcg(mActivity.sixMinReportBloodHeartEcg)
                    viewModel.setSixMinReportHeartBeat(mActivity.sixMinReportBloodHeart)
                    viewModel.setSixMinReportStride(mActivity.sixMinReportStride)

                    mActivity.sixMinPatientId =
                        mActivity.sixMinReportInfo.patientId.toString().trim()
                    mActivity.sixMinReportNo = mActivity.sixMinReportInfo.reportNo

                    //保存心电数据到本地
//                    val ecgPath =
//                        File.separator + "sixmin/sixminreportecgdata" + File.separator + mActivity.sixMinReportInfo.reportNo
//                    val file = File(
//                        mActivity.getExternalFilesDir("")?.absolutePath,
//                        ecgPath + File.separator + "ecgData.json"
//                    )
//                    if (!file.exists()) {
//                        file.mkdirs()
//                    }
//                    val data = mutableMapOf<Long,ByteArray>()
//                    FileUtil.writeEcg(data,file.absolutePath)
                    popBackStack()
                    navigate(binding.sixminLlPreReport, R.id.sixMinReportFragment)

                } else {
                    //保存报告

                    val heartBeatConclusion =
                        binding.sixminEtHeartBeatConclusion.text.toString().trim()
                    if (heartBeatConclusion.isNotEmpty() && heartBeatConclusion.length > 44) {
                        mActivity.showMsg("请检查心电结论，最大长度为44")
                        return@setNoRepeatListener
                    }
                    mActivity.sixMinReportBloodHeart.heartConclusion = heartBeatConclusion

                    //运动处方
                    if (mActivity.sixMinReportPrescription.distanceState.isEmpty() || mActivity.sixMinReportPrescription.distanceState == "1") {
                        if (binding.sixminPreEtStrideLow.text.toString().trim()
                                .isEmpty() || binding.sixminPreEtStrideHigh.text.toString().trim()
                                .isEmpty()
                        ) {
                            mActivity.showMsg("请检查运动步速值")
                            return@setNoRepeatListener
                        }
                        if (!CommonUtil.checkNum(
                                binding.sixminPreEtStrideLow.text.toString().trim(),
                                "0+"
                            ) || !CommonUtil.checkNum(
                                binding.sixminPreEtStrideHigh.text.toString().trim(),
                                "0+"
                            )
                        ) {
                            mActivity.showMsg("请检查运动步速值，最多精确到1位")
                            return@setNoRepeatListener
                        }
                        if (BigDecimal(
                                binding.sixminPreEtStrideLow.text.toString().trim()
                            ).compareTo(
                                BigDecimal("1000")
                            ) != -1 ||
                            BigDecimal(
                                binding.sixminPreEtStrideHigh.text.toString().trim()
                            ).compareTo(
                                BigDecimal("1000")
                            ) != -1
                        ) {
                            mActivity.showMsg("请检查运动步速值，最大值为999.9")
                            return@setNoRepeatListener
                        }
                        //前者不可大于后者
                        if (BigDecimal(
                                binding.sixminPreEtStrideLow.text.toString().trim()
                            ) > BigDecimal(
                                binding.sixminPreEtStrideHigh.text.toString().trim()
                            )
                        ) {
                            mActivity.showMsg("请检查运动步速值,起始值不可大于终止值")
                            return@setNoRepeatListener
                        }

                        if (binding.sixminPreEtDistanceLow.text.toString()
                                .isEmpty() || binding.sixminPreEtDistanceHigh.text.toString().trim()
                                .isEmpty()
                        ) {
                            mActivity.showMsg("请检查运动距离值")
                            return@setNoRepeatListener
                        }
                        if (!CommonUtil.checkNum(
                                binding.sixminPreEtDistanceLow.text.toString().trim(),
                                "0+"
                            ) || !CommonUtil.checkNum(
                                binding.sixminPreEtDistanceHigh.text.toString().trim(),
                                "0+"
                            )
                        ) {
                            mActivity.showMsg("请检查运动距离值，最多精确到1位")
                            return@setNoRepeatListener
                        }
                        if (BigDecimal(
                                binding.sixminPreEtDistanceLow.text.toString().trim()
                            ).compareTo(
                                BigDecimal("10000")
                            ) != -1 ||
                            BigDecimal(
                                binding.sixminPreEtDistanceHigh.text.toString().trim()
                            ).compareTo(
                                BigDecimal("10000")
                            ) != -1
                        ) {
                            mActivity.showMsg("请检查运动距离值，最大值为9999.9")
                            return@setNoRepeatListener
                        }
                        //前者不可大于后者
                        if (BigDecimal(binding.sixminPreEtDistanceLow.text.toString()) > BigDecimal(
                                binding.sixminPreEtDistanceHigh.text.toString()
                            )
                        ) {
                            mActivity.showMsg("请检查运动距离值,起始值不可大于终止值")
                            return@setNoRepeatListener
                        }
                        mActivity.sixMinReportPrescription.strideBefore =
                            binding.sixminPreEtStrideLow.text.toString().trim()
                        mActivity.sixMinReportPrescription.strideAfter =
                            binding.sixminPreEtStrideHigh.text.toString().trim()
                        mActivity.sixMinReportPrescription.movementDistance =
                            binding.sixminPreEtDistanceLow.text.toString().trim()
                        mActivity.sixMinReportPrescription.movementDistanceAfter =
                            binding.sixminPreEtDistanceHigh.text.toString().trim()
                    } else {
                        mActivity.sixMinReportPrescription.strideBefore = "0"
                        mActivity.sixMinReportPrescription.strideAfter = "0"
                        mActivity.sixMinReportPrescription.movementDistance = "0"
                        mActivity.sixMinReportPrescription.movementDistanceAfter = "0"
                    }

                    //运动时长
                    mActivity.sixMinReportPrescription.movementTime =
                        binding.sixminPreSpSportTime.value.toString().trim()
                    if (mActivity.sixMinReportPrescription.heartrateState.isEmpty() || mActivity.sixMinReportPrescription.heartrateState == "1") {
                        val heartEcg = binding.sixminPreEtSportEcg.text.toString()
                        if (heartEcg.isEmpty() || heartEcg.length > 30) {
                            mActivity.showMsg("请检查运动心率值")
                            return@setNoRepeatListener
                        }
                        mActivity.sixMinReportPrescription.heartrateRate =
                            binding.sixminPreEtSportEcg.text.toString()
                    }

                    if (mActivity.sixMinReportPrescription.metabState.isEmpty() || mActivity.sixMinReportPrescription.metabState == "1") {
                        val metab = binding.sixminPreEtMetab.text.toString()
                        if (metab.isEmpty() || metab.length > 30) {
                            mActivity.showMsg("请检查代谢当量值")
                            return@setNoRepeatListener
                        }

                        mActivity.sixMinReportPrescription.metabMet =
                            binding.sixminPreEtMetab.text.toString()
                    }

                    if (mActivity.sixMinReportPrescription.pllevState.isEmpty() || mActivity.sixMinReportPrescription.pllevState == "1") {
                        val tireLow = binding.sixminPreEtTiredControlLow.text.toString()
                        val tireHigh = binding.sixminPreEtTiredControlHigh.text.toString()
                        if (tireLow.isEmpty() || tireHigh.isEmpty()) {
                            mActivity.showMsg("请检查疲劳程度控制")
                            return@setNoRepeatListener
                        }
                        if (tireLow.toInt() < 0 || tireLow.toInt() > 10 || tireHigh.toInt() < 0 || tireHigh.toInt() > 10) {
                            mActivity.showMsg("请检查疲劳程度值，最大值为10")
                            return@setNoRepeatListener
                        }

                        if (tireLow.toInt() > tireHigh.toInt()) {
                            mActivity.showMsg("请检查疲劳程度控制，起始值不可大于终止值")
                            return@setNoRepeatListener
                        }

                        mActivity.sixMinReportPrescription.pllevBefore =
                            binding.sixminPreEtTiredControlLow.text.toString().trim()
                        mActivity.sixMinReportPrescription.pllevAfter =
                            binding.sixminPreEtTiredControlHigh.text.toString().trim()
                        mActivity.sixMinReportPrescription.pilaoControl =
                            mActivity.usbTransferUtil.dealPiLaoKZ(
                                binding.sixminPreEtTiredControlLow.text.toString().trim().toInt(),
                                binding.sixminPreEtTiredControlHigh.text.toString().trim().toInt()
                            )
                    }

                    val doctorName = binding.sixminEtRecommendDoctor.text.toString().trim()
                    if (doctorName.isEmpty()) {
                        mActivity.showMsg("请检查建议医生")
                        return@setNoRepeatListener
                    }
                    if (doctorName.length > 5) {
                        mActivity.showMsg("建议医生，长度不能大于5")
                        return@setNoRepeatListener
                    }
                    mActivity.sixMinReportPrescription.remarke =
                        binding.sixminEtReportNote.text.toString().trim()
                    mActivity.sixMinReportPrescription.remarkeName =
                        binding.sixminEtRecommendDoctor.text.toString().trim()
                    mActivity.sixMinReportPrescription.movementWay =
                        if (binding.sixminRbSportTypeWalk.isChecked) "0" else "1"
                    mActivity.sixMinReportPrescription.movementWeeklyNumber =
                        binding.sixminPreSpWeeklyCount.value.toString().trim()
                    mActivity.sixMinReportPrescription.movementCycle =
                        binding.sixminPreSpPrescriptionPeriod.value.toString().trim()
                    mActivity.sixMinReportPrescription.cycleUnit =
                        if (binding.sixminRbPrescriptionCycleWeek.isChecked) "0" else if (binding.sixminRbPrescriptionCycleMonth.isChecked) "1" else "2"

                    viewModel.updateSixMinReportPrescription(mActivity.sixMinReportPrescription)
                    viewModel.updateSixMinReportEvaluation(mActivity.sixMinReportEvaluation)
                    viewModel.updateSixMinReportInfo(mActivity.sixMinReportInfo)
                    viewModel.setSixMinReportHeartBeat(mActivity.sixMinReportBloodHeart)

                    popBackStack()
                    navigate(binding.sixminLlPreReport, R.id.sixMinReportFragment)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.sixminPreSpSportTime.setOnValueChangeListener { _, value ->
            dealTimeSportTimeChange(value)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSixminPreReportBinding.inflate(inflater, container, false)

    private fun beanQuery(any: Any) {
        try {
            if (any is List<*>) {
                val datas = any as MutableList<*>
                sixMinRecordsBean = datas[0] as SixMinRecordsBean

                showData(sixMinRecordsBean)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showData(sixMinRecordsBean: SixMinRecordsBean?) {
        if (sixMinRecordsBean != null) {
            mActivity.sixMinReportPrescription = sixMinRecordsBean.prescriptionBean[0]
            mActivity.sixMinReportPrescription.prescripState = "1"
            mActivity.sixMinReportBloodOther = sixMinRecordsBean.otherBean[0]
            mActivity.sixMinReportStride = sixMinRecordsBean.strideBean[0]
            mActivity.sixMinReportEvaluation = sixMinRecordsBean.evaluationBean[0]
            mActivity.sixMinReportBloodHeart = sixMinRecordsBean.heartBeatBean[0]
            mActivity.sixMinReportInfo = sixMinRecordsBean.infoBean
            mActivity.sixMinReportBloodHeart = sixMinRecordsBean.heartBeatBean[0]
            mActivity.sixMinReportBloodHeartEcg = sixMinRecordsBean.heartEcgBean[0]
            mActivity.sixMinReportWalk = sixMinRecordsBean.walkBean[0]
        }

        Log.d("sixMinRecordsBean", Gson().toJson(this.sixMinRecordsBean))

        mActivity.sixMinReportBloodOther.useName = mActivity.sysSettingBean.sysOther.useOrg
        mActivity.sixMinReportInfo.bsHxl = mActivity.sysSettingBean.sysOther.stepsOrBreath

        initTable()
        initSelfCheck()

        binding.sixminEtFinishCircle.setText(mActivity.sixMinReportEvaluation.turnsNumber)
        binding.sixminEtFinishCircle.isEnabled =
            mActivity.sixMinReportPrescription.movementWay.isEmpty()

        binding.sixminEtUnfinishCircle.setText(mActivity.sixMinReportEvaluation.unfinishedDistance)
        binding.sixminEtUnfinishCircle.isEnabled =
            mActivity.sixMinReportPrescription.movementWay.isEmpty()

        binding.sixminTvTotalDistance.text = mActivity.sixMinReportEvaluation.totalDistance

        if (mActivity.sixMinReportInfo.bsHxl == "0") {
            binding.sixminPreLlTotalSteps.visibility = View.VISIBLE
            binding.sixminTvTotalSteps.text = mActivity.sixMinReportEvaluation.totalWalk
        } else {
            binding.sixminPreLlTotalSteps.visibility = View.GONE
        }

        val stopTime = mActivity.sixMinReportBloodOther.stopTime
        val type = mActivity.sixMinReportBloodOther.stopOr
        strideAvg = mActivity.usbTransferUtil.dealStrideAvg(
            BigDecimal(mActivity.sixMinReportEvaluation.totalDistance),
            type.toInt(),
            stopTime
        )
        val cardiopuDegreeStr = when (mActivity.sixMinReportEvaluation.cardiopuDegree) {
            "1" -> "重度"
            "2" -> "中度"
            else -> "轻度"
        }
        binding.sixminTvDataStatistics.text = Html.fromHtml(
            String.format(
                getString(R.string.sixmin_test_report_data_statistics),
                mActivity.sixMinReportEvaluation.totalDistance,
                strideAvg,
                mActivity.sixMinReportEvaluation.metabEquivalent,
                cardiopuDegreeStr,
                mActivity.sixMinReportEvaluation.cardiopuLevel,
            )
        )
        binding.sixminPreEtResetTime.isEnabled =
            mActivity.sixMinReportPrescription.movementWay.isEmpty()
        if (mActivity.sysSettingBean.sysOther.showResetTime == "1") {
            binding.sixminPreLlResetTime.visibility = View.VISIBLE
            binding.sixminPreEtResetTime.setText(mActivity.sixMinReportInfo.restDuration)
        } else {
            binding.sixminPreLlResetTime.visibility = View.GONE
        }
        if (mActivity.sixMinReportPrescription.movementWay.isEmpty() || mActivity.sixMinReportPrescription.movementWay == "0") {
            binding.sixminRbSportTypeWalk.isChecked = true
        } else {
            binding.sixminRbSportTypeRun.isChecked = true
        }

        if (mActivity.sixMinReportPrescription.movementTime != "") {
            binding.sixminPreSpSportTime.value =
                mActivity.sixMinReportPrescription.movementTime.toInt()
        }

        binding.sixminEtHeartBeatConclusion.setText(mActivity.sixMinReportBloodHeart.heartConclusion)

        if (mActivity.sixMinReportPrescription.movementWay.isEmpty()) {
            binding.sixminReportTvGenerateReport.text = "生成报告"
        } else {
            binding.sixminReportTvGenerateReport.text = "保存"
        }

        updatePrescriptionView()
    }

    private fun updatePrescriptionView() {
        try {
            //运动步速
            var ydbsStr1 = ""
            var ydbsStr2 = ""
            if (mActivity.sixMinReportPrescription.movementWay.isEmpty()) {
                ydbsStr1 = mActivity.usbTransferUtil.dealYdbsStrs(
                    "0.5",
                    BigDecimal(if (mActivity.sixMinReportStride.strideAverage == "") "0.00" else mActivity.sixMinReportStride.strideAverage)
                )
                ydbsStr2 = mActivity.usbTransferUtil.dealYdbsStrs(
                    "0.6",
                    BigDecimal(if (mActivity.sixMinReportStride.strideAverage == "") "0.00" else mActivity.sixMinReportStride.strideAverage)
                )
            } else {
                //强度版本
                if (mActivity.sixMinReportPrescription.prescripState == "0") {
                    ydbsStr1 = mActivity.usbTransferUtil.dealYdbsStrs("0.5", strideAvg)
                    ydbsStr2 = mActivity.usbTransferUtil.dealYdbsStrs("0.6", strideAvg)
                } else if (mActivity.sixMinReportPrescription.prescripState == "1") {
                    //验证是否出具
                    if (mActivity.sixMinReportPrescription.distanceState == "1" || mActivity.sixMinReportPrescription.distanceState.isEmpty()) {
                        ydbsStr1 = mActivity.sixMinReportPrescription.strideBefore
                        ydbsStr2 = mActivity.sixMinReportPrescription.strideAfter
                    } else if (mActivity.sixMinReportPrescription.distanceState == "2") {
                        ydbsStr1 = "\\"
                        ydbsStr2 = "\\"
                    }
                }
            }

            binding.sixminPreEtStrideLow.setText(ydbsStr1)
            binding.sixminPreEtStrideHigh.setText(ydbsStr2)

            //运动周期
            var zhouqiStrs: Array<String>? = arrayOf()
            zhouqiStrs = if (mActivity.sixMinReportPrescription.movementWay.isEmpty()) {
                val cf3qiangduStrs: Array<String> =
                    mActivity.usbTransferUtil.dealQiangdu(BigDecimal(mActivity.sixMinReportEvaluation.metabEquivalent))
                mActivity.usbTransferUtil.dealzhouqi(cf3qiangduStrs)
            } else {
                val sj: String = mActivity.sixMinReportPrescription.movementTime
                val mn: String = mActivity.sixMinReportPrescription.movementWeeklyNumber
                arrayOf(sj, mn)
            }

            if (!zhouqiStrs.isNullOrEmpty()) {
                binding.sixminPreSpSportTime.value = zhouqiStrs[0].toInt()
                binding.sixminPreSpWeeklyCount.value = zhouqiStrs[1].toInt()
            }

            //运动距离
            var ydjlStr1 = ""
            var ydjlStr2 = ""
            if (mActivity.sixMinReportPrescription.movementWay.isEmpty()) {
                ydjlStr1 = mActivity.usbTransferUtil.dealtjjlStrs(
                    "0.5",
                    BigDecimal(if (mActivity.sixMinReportStride.strideAverage == "") "0.00" else mActivity.sixMinReportStride.strideAverage),
                    zhouqiStrs!![0].toInt()
                )
                ydjlStr2 = mActivity.usbTransferUtil.dealtjjlStrs(
                    "0.6",
                    BigDecimal(if (mActivity.sixMinReportStride.strideAverage == "") "0.00" else mActivity.sixMinReportStride.strideAverage),
                    zhouqiStrs[0].toInt()
                )
            } else {
                //强度版本
                if (mActivity.sixMinReportPrescription.prescripState == "0") {
                    ydjlStr1 = mActivity.sixMinReportPrescription.movementDistance
                    ydjlStr2 = mActivity.sixMinReportPrescription.movementDistance
                } else if (mActivity.sixMinReportPrescription.prescripState == "1") {
                    //验证是否出具
                    if (mActivity.sixMinReportPrescription.distanceState == "1" || mActivity.sixMinReportPrescription.distanceState.isEmpty()) {
                        ydjlStr1 = mActivity.sixMinReportPrescription.movementDistance
                        ydjlStr2 = mActivity.sixMinReportPrescription.movementDistanceAfter
                    } else if (mActivity.sixMinReportPrescription.distanceState == "2") {
                        ydjlStr1 = "\\"
                        ydjlStr2 = "\\"
                    }
                }
            }
            binding.sixminPreEtDistanceLow.setText(ydjlStr1)
            binding.sixminPreEtDistanceHigh.setText(ydjlStr2)

            //运动心率
            var ydxlStr = ""
            if (mActivity.sixMinReportPrescription.movementWay.isNotEmpty()) {
                //验证是否出具
                if (mActivity.sixMinReportPrescription.heartrateState == "1" || mActivity.sixMinReportPrescription.heartrateState.isEmpty()) {
                    ydxlStr = mActivity.sixMinReportPrescription.heartrateRate
                } else if (mActivity.sixMinReportPrescription.heartrateState == "2") {
                    ydxlStr = "\\"
                }
            }
            binding.sixminPreEtSportEcg.setText(ydxlStr)

            //代谢当量
            var dxdlStr = ""
            if (mActivity.sixMinReportPrescription.movementWay.isNotEmpty()) {
                //验证是否出具
                if (mActivity.sixMinReportPrescription.metabState == "1" || mActivity.sixMinReportPrescription.metabState.isEmpty()) {
                    dxdlStr = mActivity.sixMinReportPrescription.metabMet
                } else if (mActivity.sixMinReportPrescription.metabState == "2") {
                    dxdlStr = "\\"
                }
            }
            binding.sixminPreEtMetab.setText(dxdlStr)

            //疲劳控制
            var plzhi1Str = "4"
            var plzhi2Str = "6"
            if (mActivity.sixMinReportPrescription.movementWay.isNotEmpty()) {
                //验证是否出具
                if (mActivity.sixMinReportPrescription.pllevState == "1" || mActivity.sixMinReportPrescription.pllevState.isEmpty()) {
                    plzhi1Str = mActivity.sixMinReportPrescription.pllevBefore
                    plzhi2Str = mActivity.sixMinReportPrescription.pllevAfter
                } else if (mActivity.sixMinReportPrescription.metabState == "2") {
                    plzhi1Str = "\\"
                    plzhi2Str = "\\"
                }
            }
            binding.sixminPreEtTiredControlLow.setText(plzhi1Str)
            binding.sixminPreEtTiredControlHigh.setText(plzhi2Str)

            //处方周期
            var prescriptionPeriod = "8"
            if (mActivity.sixMinReportPrescription.movementWay.isNotEmpty()) {
                if (mActivity.sixMinReportPrescription.movementCycle.isNotEmpty()) {
                    prescriptionPeriod = mActivity.sixMinReportPrescription.movementCycle
                }
            }
            binding.sixminPreSpPrescriptionPeriod.value = prescriptionPeriod.toInt()

            when (mActivity.sixMinReportPrescription.cycleUnit) {
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
            if (mActivity.sixMinReportBloodOther.stopOr == "1") {
                var reasonStr: String = mActivity.sixMinReportBloodOther.stopReason
                if (reasonStr.isEmpty()) {
                    reasonStr = "无"
                }
                binding.sixminTvPrescriptionConclusion.text = Html.fromHtml(
                    String.format(
                        getString(R.string.sixmin_test_report_test_conclusion_unfinish),
                        mActivity.sixMinReportBloodOther.stopTime,
                        reasonStr
                    )
                )
            } else if (mActivity.sixMinReportBloodOther.stopOr == "0") {
                var badSymptomsStr: String = mActivity.sixMinReportBloodOther.badSymptoms
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
            if (mActivity.sixMinReportPrescription.movementWay.isNotEmpty() && mActivity.sixMinReportPrescription.remarke.isNotEmpty()) {
                binding.sixminEtReportNote.setText(mActivity.sixMinReportPrescription.remarke)
            }

            binding.sixminEtRecommendDoctor.setText(mActivity.sixMinReportPrescription.remarkeName)
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
                "心率(bpm)",
                if (mActivity.sixMinReportBloodHeart.heartStop == "") "0" else mActivity.sixMinReportBloodHeart.heartStop,
                if (mActivity.sixMinReportBloodHeart.heartOne == "") "0" else mActivity.sixMinReportBloodHeart.heartOne,
                if (mActivity.sixMinReportBloodHeart.heartTwo == "") "0" else mActivity.sixMinReportBloodHeart.heartTwo,
                if (mActivity.sixMinReportBloodHeart.heartThree == "") "0" else mActivity.sixMinReportBloodHeart.heartThree,
                if (mActivity.sixMinReportBloodHeart.heartFour == "") "0" else mActivity.sixMinReportBloodHeart.heartFour,
                if (mActivity.sixMinReportBloodHeart.heartFive == "") "0" else mActivity.sixMinReportBloodHeart.heartFive,
                if (mActivity.sixMinReportBloodHeart.heartSix == "") "0" else mActivity.sixMinReportBloodHeart.heartSix,
                if (mActivity.sixMinReportBloodHeart.heartBig == "") "0" else mActivity.sixMinReportBloodHeart.heartBig,
                if (mActivity.sixMinReportBloodHeart.heartSmall == "") "0" else mActivity.sixMinReportBloodHeart.heartSmall,
                if (mActivity.sixMinReportBloodHeart.heartAverage == "") "0" else mActivity.sixMinReportBloodHeart.heartAverage
            )
        )
        reportRowList.add(
            SixMinReportItemBean(
                "血氧(%)",
                if (mActivity.sixMinReportBloodOxy.bloodStop == "") "0" else mActivity.sixMinReportBloodOxy.bloodStop,
                if (mActivity.sixMinReportBloodOxy.bloodOne == "") "0" else mActivity.sixMinReportBloodOxy.bloodOne,
                if (mActivity.sixMinReportBloodOxy.bloodTwo == "") "0" else mActivity.sixMinReportBloodOxy.bloodTwo,
                if (mActivity.sixMinReportBloodOxy.bloodThree == "") "0" else mActivity.sixMinReportBloodOxy.bloodThree,
                if (mActivity.sixMinReportBloodOxy.bloodFour == "") "0" else mActivity.sixMinReportBloodOxy.bloodFour,
                if (mActivity.sixMinReportBloodOxy.bloodFive == "") "0" else mActivity.sixMinReportBloodOxy.bloodFive,
                if (mActivity.sixMinReportBloodOxy.bloodSix == "") "0" else mActivity.sixMinReportBloodOxy.bloodSix,
                if (mActivity.sixMinReportBloodOxy.bloodBig == "") "0" else mActivity.sixMinReportBloodOxy.bloodBig,
                if (mActivity.sixMinReportBloodOxy.bloodSmall == "") "0" else mActivity.sixMinReportBloodOxy.bloodSmall,
                if (mActivity.sixMinReportBloodOxy.bloodAverage == "") "0" else mActivity.sixMinReportBloodOxy.bloodAverage
            )
        )
        if (mActivity.sixMinReportInfo.bsHxl == "0") {
            reportRowList.add(
                SixMinReportItemBean(
                    "步数",
                    if (mActivity.sixMinReportWalk.walkStop == "") "0" else mActivity.sixMinReportWalk.walkStop,
                    if (mActivity.sixMinReportWalk.walkOne == "") "0" else mActivity.sixMinReportWalk.walkOne,
                    if (mActivity.sixMinReportWalk.walkTwo == "") "0" else mActivity.sixMinReportWalk.walkTwo,
                    if (mActivity.sixMinReportWalk.walkThree == "") "0" else mActivity.sixMinReportWalk.walkThree,
                    if (mActivity.sixMinReportWalk.walkFour == "") "0" else mActivity.sixMinReportWalk.walkFour,
                    if (mActivity.sixMinReportWalk.walkFive == "") "0" else mActivity.sixMinReportWalk.walkFive,
                    if (mActivity.sixMinReportWalk.walkSix == "") "0" else mActivity.sixMinReportWalk.walkSix,
                    if (mActivity.sixMinReportWalk.walkBig == "") "0" else mActivity.sixMinReportWalk.walkBig,
                    if (mActivity.sixMinReportWalk.waklSmall == "") "0" else mActivity.sixMinReportWalk.waklSmall,
                    if (mActivity.sixMinReportWalk.walkAverage == "") "0" else mActivity.sixMinReportWalk.walkAverage
                )
            )
        } else {
            reportRowList.add(
                SixMinReportItemBean(
                    "呼吸率",
                    if (mActivity.sixMinReportBreathing.breathingStop == "") "0" else mActivity.sixMinReportBreathing.breathingStop,
                    if (mActivity.sixMinReportBreathing.breathingOne == "") "0" else mActivity.sixMinReportBreathing.breathingOne,
                    if (mActivity.sixMinReportBreathing.breathingTwo == "") "0" else mActivity.sixMinReportBreathing.breathingTwo,
                    if (mActivity.sixMinReportBreathing.breathingThree == "") "0" else mActivity.sixMinReportBreathing.breathingThree,
                    if (mActivity.sixMinReportBreathing.breathingFour == "") "0" else mActivity.sixMinReportBreathing.breathingFour,
                    if (mActivity.sixMinReportBreathing.breathingFive == "") "0" else mActivity.sixMinReportBreathing.breathingFive,
                    if (mActivity.sixMinReportBreathing.breathingSix == "") "0" else mActivity.sixMinReportBreathing.breathingSix,
                    if (mActivity.sixMinReportBreathing.breathingBig == "") "0" else mActivity.sixMinReportBreathing.breathingBig,
                    if (mActivity.sixMinReportBreathing.breathingSmall == "") "0" else mActivity.sixMinReportBreathing.breathingSmall,
                    if (mActivity.sixMinReportBreathing.breathingAverage == "") "0" else mActivity.sixMinReportBreathing.breathingAverage
                )
            )
        }
        val startHighBlood = mActivity.sixMinReportBloodOther.startHighPressure
        val startLowBlood = mActivity.sixMinReportBloodOther.startLowPressure
        val endHighBlood = mActivity.sixMinReportBloodOther.stopHighPressure
        val endLowBlood = mActivity.sixMinReportBloodOther.stopLowPressure
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
            val newRow = TableRow(mActivity.applicationContext)
            val layoutParams = TableRow.LayoutParams()
            newRow.layoutParams = layoutParams

            val linearLayout = LinearLayout(
                mActivity.applicationContext
            )
            linearLayout.orientation = LinearLayout.HORIZONTAL

            for (j in 0..10) {
                val tvNo = TextView(mActivity.applicationContext)
                tvNo.textSize = dip2px(6.0f).toFloat()
                // 设置文字居中
                tvNo.gravity = if (j == 0) Gravity.START else Gravity.CENTER
                tvNo.setTextColor(ContextCompat.getColor(mActivity, R.color.text3))
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

                if(j == 0 || j == 1 || j == 7){
                    // 创建一个新的View，作为竖线
                    val view = View(mActivity)
                    // 设置竖线的宽度（如果需要）
                    val heightInPixels = 50 // 2像素的高度
                    val widthInPixels = 1 // 想要的横向宽度，可以是屏幕宽度
                    // 设置竖线的参数
                    val params = LinearLayout.LayoutParams(
                        widthInPixels,  // 宽度
                        heightInPixels // 高度
                    )
                    // 设置竖线的背景色，这里使用黑色
                    view.setBackgroundColor(Color.BLACK)
                    linearLayout.addView(view,params)
                }
            }
            newRow.setPadding(
                dip2px(6.0f), dip2px(0f), dip2px(6.0f), dip2px(0f)
            )
            newRow.addView(linearLayout)
            binding.sixminReportTlPreTable.addView(newRow)
        }
    }

    private fun dip2px(dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.displayMetrics
        ).toInt()
    }

    private fun initSelfCheck() {
        val fatigueLevel = mActivity.sixMinReportEvaluation.fatigueLevel
        val breathingLevel = mActivity.sixMinReportEvaluation.breathingLevel

        patientSelfList.clear()
        val patientBreathSelfItemList = mutableListOf<SixMinReportPatientSelfItemBean>()
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0级", "没有", if (breathingLevel == "0") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0.5级", "非常非常轻", if (breathingLevel == "0.5") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "1级", "非常轻", if (breathingLevel == "1") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "2级", "很轻", if (breathingLevel == "2") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "3级", "中度", if (breathingLevel == "3") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "4级", "较严重", if (breathingLevel == "4") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "5-6级", "严重", if (breathingLevel == "5-6") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "7-9级", "非常严重", if (breathingLevel == "7-9") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "10级", "非常非常严重", if (breathingLevel == "10") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientSelfList.add(
            SixMinReportPatientSelfBean(
                "呼吸状况等级", "1", patientBreathSelfItemList,
                mActivity.usbTransferUtil.dealSelfCheckBreathingLevel(breathingLevel)
            )
        )
        val patientTiredSelfItemList = mutableListOf<SixMinReportPatientSelfItemBean>()
        patientTiredSelfItemList.clear()
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0级", "没有", if (fatigueLevel == "0") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "1级", "非常轻松", if (fatigueLevel == "1") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "2级", "轻松", if (fatigueLevel == "2") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "3级", "中度", if (fatigueLevel == "3") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "4级", "有点疲劳", if (fatigueLevel == "4") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "5-6级", "疲劳", if (fatigueLevel == "5-6") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "7-8级", "非常疲劳", if (fatigueLevel == "7-8") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "9-10级", "非常非常疲劳(几乎到极限)", if (fatigueLevel == "9-10") "1" else "0",
                if (mActivity.sixMinReportPrescription.movementWay == "") "1" else "0"
            )
        )
        patientSelfList.add(
            SixMinReportPatientSelfBean(
                "疲劳状况等级", "2", patientTiredSelfItemList,
                mActivity.usbTransferUtil.dealSelfCheckFatigueLevel(fatigueLevel)
            )
        )
        binding.sixminRvPatientSelfCheck.layoutManager = LinearLayoutManager(mActivity)
        val patientSelfItemAdapter = SixMinReportPatientSelfAdapter(mActivity)
        patientSelfItemAdapter.setItemsBean(patientSelfList)
        binding.sixminRvPatientSelfCheck.adapter = patientSelfItemAdapter
    }

    /**
     * 运动时长变化
     */
    private fun dealTimeSportTimeChange(value: Int) {
        if (mActivity.sixMinReportPrescription.distanceState.isEmpty() || mActivity.sixMinReportPrescription.distanceState == "1") {
            var percentLow = ""
            var percentHigh = ""
            if (mActivity.sixMinReportPrescription.distanceFormula == "0") {
                percentLow = "0.5"
                percentHigh = "0.6"
            } else {
                percentLow = "0.7"
                percentHigh = "0.8"
            }
            binding.sixminPreEtDistanceLow.setText(
                mActivity.usbTransferUtil.dealtjjlStrs(
                    percentLow,
                    BigDecimal(if (mActivity.sixMinReportStride.strideAverage == "") "0.00" else mActivity.sixMinReportStride.strideAverage),
                    value
                )
            )
            binding.sixminPreEtDistanceHigh.setText(
                mActivity.usbTransferUtil.dealtjjlStrs(
                    percentHigh,
                    BigDecimal(if (mActivity.sixMinReportStride.strideAverage == "") "0.00" else mActivity.sixMinReportStride.strideAverage),
                    value
                )
            )
        }
    }

    /**
     * 选择处方参数
     */
    private fun dealSelectPrescriptionElement() {
        if (mActivity.sixMinReportPrescription.distanceState == "1" || mActivity.sixMinReportPrescription.distanceState == "") {
            if (mActivity.sixMinReportPrescription.strideFormula == "0" || mActivity.sixMinReportPrescription.strideFormula == "") {
                binding.sixminPreEtStrideLow.setText(
                    mActivity.usbTransferUtil.dealYdbsStrs(
                        "0.5",
                        strideAvg
                    )
                )
                binding.sixminPreEtStrideHigh.setText(
                    mActivity.usbTransferUtil.dealYdbsStrs(
                        "0.6", strideAvg
                    )
                )
            } else {
                binding.sixminPreEtStrideLow.setText(
                    mActivity.usbTransferUtil.dealYdbsStrs(
                        "0.7",
                        strideAvg
                    )
                )
                binding.sixminPreEtStrideHigh.setText(
                    mActivity.usbTransferUtil.dealYdbsStrs(
                        "0.8", strideAvg
                    )
                )
            }
            if (mActivity.sixMinReportPrescription.distanceFormula == "0") {
                binding.sixminPreEtDistanceLow.setText(
                    mActivity.usbTransferUtil.dealtjjlStrs(
                        "0.5",
                        BigDecimal(if (mActivity.sixMinReportStride.strideAverage == "") "0.00" else mActivity.sixMinReportStride.strideAverage),
                        binding.sixminPreSpSportTime.value
                    )
                )
                binding.sixminPreEtDistanceHigh.setText(
                    mActivity.usbTransferUtil.dealtjjlStrs(
                        "0.6",
                        BigDecimal(if (mActivity.sixMinReportStride.strideAverage == "") "0.00" else mActivity.sixMinReportStride.strideAverage),
                        binding.sixminPreSpSportTime.value
                    )
                )
            } else {
                binding.sixminPreEtDistanceLow.setText(
                    mActivity.usbTransferUtil.dealtjjlStrs(
                        "0.7",
                        BigDecimal(if (mActivity.sixMinReportStride.strideAverage == "") "0.00" else mActivity.sixMinReportStride.strideAverage),
                        binding.sixminPreSpSportTime.value
                    )
                )
                binding.sixminPreEtDistanceHigh.setText(
                    mActivity.usbTransferUtil.dealtjjlStrs(
                        "0.8",
                        BigDecimal(if (mActivity.sixMinReportStride.strideAverage == "") "0.00" else mActivity.sixMinReportStride.strideAverage),
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

        if (mActivity.sixMinReportPrescription.heartrateState == "1" || mActivity.sixMinReportPrescription.heartrateState == "") {
            binding.sixminPreEtSportEcg.setText(mActivity.sixMinReportPrescription.heartrateRate)
            binding.sixminPreEtSportEcg.isEnabled = true
        } else {
            binding.sixminPreEtSportEcg.setText("\\")
            binding.sixminPreEtSportEcg.isEnabled = false
        }

        if (mActivity.sixMinReportPrescription.metabState == "1" || mActivity.sixMinReportPrescription.metabState == "") {
            binding.sixminPreEtMetab.setText(mActivity.sixMinReportPrescription.metabMet)
            binding.sixminPreEtMetab.isEnabled = true
        } else {
            binding.sixminPreEtMetab.setText("\\")
            binding.sixminPreEtMetab.isEnabled = false
        }

        if (mActivity.sixMinReportPrescription.pllevState == "1" || mActivity.sixMinReportPrescription.pllevState == "") {
            binding.sixminPreEtTiredControlLow.setText(mActivity.sixMinReportPrescription.pllevBefore)
            binding.sixminPreEtTiredControlHigh.setText(mActivity.sixMinReportPrescription.pllevAfter)

            binding.sixminPreEtTiredControlLow.isEnabled = true
            binding.sixminPreEtTiredControlHigh.isEnabled = true
        } else {
            binding.sixminPreEtTiredControlLow.setText("\\")
            binding.sixminPreEtTiredControlHigh.setText("\\")

            binding.sixminPreEtTiredControlLow.isEnabled = false
            binding.sixminPreEtTiredControlHigh.isEnabled = false
        }
    }

    private fun popBackStack() {
        val navController = findNavController()//fragment返回数据处理
        navController.previousBackStackEntry?.savedStateHandle?.set("key", "返回")
        navController.popBackStack()
    }
}
