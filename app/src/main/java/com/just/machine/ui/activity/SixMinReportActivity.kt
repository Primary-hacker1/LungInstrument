package com.just.machine.ui.activity

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.common.base.CommonBaseActivity
import com.common.viewmodel.LiveDataEvent
import com.google.gson.Gson
import com.just.machine.model.Constants
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.model.SixMinReportItemBean
import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.ActivitySixMinReportBinding
import com.justsafe.libview.util.SystemUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.math.BigDecimal

/**
 * 6分钟报告
 */
@AndroidEntryPoint
class SixMinReportActivity : CommonBaseActivity<ActivitySixMinReportBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private var reportRowList = mutableListOf<SixMinReportItemBean>()
    private var reportEvaluationList = mutableListOf<SixMinReportEvaluation>()
    private var sixMinPatientId = "" //患者id
    private var sixMinReportNo = "" //报告id
    private lateinit var usbTransferUtil: USBTransferUtil
    private var sixMinRecordsBean: SixMinRecordsBean = SixMinRecordsBean()//6分钟报告信息
    private var strideAvg: BigDecimal = BigDecimal(0.00)

    override fun getViewBinding() = ActivitySixMinReportBinding.inflate(layoutInflater)

    override fun initView() {
        usbTransferUtil = USBTransferUtil.getInstance()
        sixMinPatientId = intent.extras?.getString(Constants.sixMinPatientInfo).toString()
        sixMinReportNo = intent.extras?.getString(Constants.sixMinReportNo).toString()
        lifecycleScope.launch {
            kotlinx.coroutines.delay(0L)
            viewModel.getSixMinReportEvaluation()
            viewModel.getSixMinReportInfoById(sixMinPatientId.toLong(), sixMinReportNo)
        }
        initClickListener()
    }

    private fun initClickListener() {
        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.QuerySixMinReportEvaluationSuccess -> {
                    it.any?.let { it1 -> beanQueryEvaluation(it1) }
                }
                LiveDataEvent.QuerySixMinReportInfoSuccess -> {
                    it.any?.let { it1 -> beanQuery(it1) }
                }
            }
        }
        binding.sixminReportIvClose.setOnClickListener {
            PatientActivity.startPatientActivity(this, "finishSixMinTest")
            finish()
        }
    }

    private fun beanQueryEvaluation(any: Any) {
        try {
            if (any is List<*>) {
                reportEvaluationList.clear()
                val datas = any as MutableList<*>
                if (datas.isNotEmpty()) {
                    for (num in 0 until datas.size) {
                        val bean = datas[num] as SixMinReportEvaluation
                        reportEvaluationList.add(bean)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initTable() {
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
                if (sixMinRecordsBean.heartBeatBean[0].heartStop == "") "0" else sixMinRecordsBean.heartBeatBean[0].heartStop,
                if (sixMinRecordsBean.heartBeatBean[0].heartOne == "") "0" else sixMinRecordsBean.heartBeatBean[0].heartOne,
                if (sixMinRecordsBean.heartBeatBean[0].heartTwo == "") "0" else sixMinRecordsBean.heartBeatBean[0].heartTwo,
                if (sixMinRecordsBean.heartBeatBean[0].heartThree == "") "0" else sixMinRecordsBean.heartBeatBean[0].heartThree,
                if (sixMinRecordsBean.heartBeatBean[0].heartFour == "") "0" else sixMinRecordsBean.heartBeatBean[0].heartFour,
                if (sixMinRecordsBean.heartBeatBean[0].heartFive == "") "0" else sixMinRecordsBean.heartBeatBean[0].heartFive,
                if (sixMinRecordsBean.heartBeatBean[0].heartSix == "") "0" else sixMinRecordsBean.heartBeatBean[0].heartSix,
                if (sixMinRecordsBean.heartBeatBean[0].heartBig == "") "0" else sixMinRecordsBean.heartBeatBean[0].heartBig,
                if (sixMinRecordsBean.heartBeatBean[0].heartSmall == "") "0" else sixMinRecordsBean.heartBeatBean[0].heartSmall,
                if (sixMinRecordsBean.heartBeatBean[0].heartAverage == "") "0" else sixMinRecordsBean.heartBeatBean[0].heartAverage
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
                    if (sixMinRecordsBean.breathingBean[0].breathingStop == "") "0" else sixMinRecordsBean.breathingBean[0].breathingStop,
                    if (sixMinRecordsBean.breathingBean[0].breathingOne == "") "0" else sixMinRecordsBean.breathingBean[0].breathingOne,
                    if (sixMinRecordsBean.breathingBean[0].breathingTwo == "") "0" else sixMinRecordsBean.breathingBean[0].breathingTwo,
                    if (sixMinRecordsBean.breathingBean[0].breathingThree == "") "0" else sixMinRecordsBean.breathingBean[0].breathingThree,
                    if (sixMinRecordsBean.breathingBean[0].breathingFour == "") "0" else sixMinRecordsBean.breathingBean[0].breathingFour,
                    if (sixMinRecordsBean.breathingBean[0].breathingFive == "") "0" else sixMinRecordsBean.breathingBean[0].breathingFive,
                    if (sixMinRecordsBean.breathingBean[0].breathingSix == "") "0" else sixMinRecordsBean.breathingBean[0].breathingSix,
                    if (sixMinRecordsBean.breathingBean[0].breathingBig == "") "0" else sixMinRecordsBean.breathingBean[0].breathingBig,
                    if (sixMinRecordsBean.breathingBean[0].breathingSmall == "") "0" else sixMinRecordsBean.breathingBean[0].breathingSmall,
                    if (sixMinRecordsBean.breathingBean[0].breathingAverage == "") "0" else sixMinRecordsBean.breathingBean[0].breathingAverage
                )
            )
        }

        reportRowList.add(
            SixMinReportItemBean(
                "血压(mmHg)",
                "${sixMinRecordsBean.otherBean[0].startHighPressure}/${sixMinRecordsBean.otherBean[0].startLowPressure}",
                "/",
                "/",
                "/",
                "/",
                "/",
                "${sixMinRecordsBean.otherBean[0].stopHighPressure}/${sixMinRecordsBean.otherBean[0].stopLowPressure}",
                "/",
                "/",
                "/"
            )
        )
        val padding = dip2px(applicationContext, 1)
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
                tvNo.textSize = dip2px(applicationContext, 7).toFloat()
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
                    0, 0, dip2px(applicationContext, 2), 0
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
                dip2px(this, 6),
                dip2px(this, 3),
                dip2px(this, 6),
                dip2px(this, 3)
            )
            newRow.addView(linearLayout)
            binding.sixminReportTable.addView(newRow)
        }
    }

    private fun beanQuery(any: Any) {
        try {
            if (any is List<*>) {
                val datas = any as MutableList<*>
                sixMinRecordsBean = datas[0] as SixMinRecordsBean

                binding.sixminReportTvUseName.text = sixMinRecordsBean.otherBean[0].useName
                binding.sixminReportTvReportNo.text = "编号:${sixMinRecordsBean.infoBean.reportNo}"
                binding.sixminReportTvPatientName.text =
                    "姓名:${sixMinRecordsBean.infoBean.patientName}"
                binding.sixminReportTvPatientSex.text =
                    "性别:${sixMinRecordsBean.infoBean.patientSix}"
                binding.sixminReportTvPatientAge.text =
                    "年龄:${sixMinRecordsBean.infoBean.patientAge}岁"
                binding.sixminReportTvPatientNum.text =
                    "病历号:${sixMinRecordsBean.infoBean.medicalNo}"
                binding.sixminReportTvPatientHeight.text =
                    "身高:${sixMinRecordsBean.infoBean.patientHeight}cm"
                binding.sixminReportTvPatientWeight.text =
                    "体重:${sixMinRecordsBean.infoBean.patientWeight}kg"
                binding.sixminReportTvPatientBmi.text =
                    "BMI:${sixMinRecordsBean.infoBean.patientBmi}"
                binding.sixminReportTvPatientPredictDistance.text =
                    "预测距离:${sixMinRecordsBean.infoBean.predictionDistance}米"
                binding.sixminReportTvPatientDiseaseHistory.text =
                    "病史:${sixMinRecordsBean.infoBean.medicalHistory}"
                binding.sixminReportTvPatientClinicalDiagnosis.text =
                    "临床诊断:${sixMinRecordsBean.infoBean.clinicalDiagnosis}"
                binding.sixminReportTvPatientCurrentMedications.text =
                    "目前用药:${sixMinRecordsBean.infoBean.medicineUse}"

                initTable()

                //综合评估
                binding.sixminReportTvEvaluationTiredLevel.text =
                    "${sixMinRecordsBean.evaluationBean[0].befoFatigueLevel}级/${sixMinRecordsBean.evaluationBean[0].fatigueLevel}级"
                binding.sixminReportTvBreathingLevel.text =
                    "${sixMinRecordsBean.evaluationBean[0].befoBreathingLevel}级/${sixMinRecordsBean.evaluationBean[0].breathingLevel}级"
                val lastDistance = getLastDistance()
                binding.sixminReportTvLastTestDistance.text = lastDistance
                binding.sixminReportTvTotalSteps.text =
                    "${sixMinRecordsBean.evaluationBean[0].totalWalk}步"
                binding.sixminReportTvThisTestDistance.text =
                    "${sixMinRecordsBean.evaluationBean[0].totalDistance}米"
                binding.sixminReportTvAverageStride.text =
                    "${sixMinRecordsBean.strideBean[0].strideAverage}米/分钟"
                binding.sixminReportTvEvaluationMetab.text =
                    "${sixMinRecordsBean.evaluationBean[0].metabEquivalent}METs"
                binding.sixminReportTvAccounted.text =
                    "${sixMinRecordsBean.evaluationBean[0].accounted}%"
                binding.sixminReportTvStopHeartBeat.text =
                    if (sixMinRecordsBean.heartBeatBean[0].heartStop.isNotEmpty())
                        "${sixMinRecordsBean.heartBeatBean[0].heartStop}bmp" else "0bmp"
                //心肺功能
                var cardiopuLevel: String = ""
                when (sixMinRecordsBean.evaluationBean[0].cardiopuLevel) {
                    "一" -> {
                        cardiopuLevel = "I"
                    }

                    "二" -> {
                        cardiopuLevel = "II"
                    }

                    "三" -> {
                        cardiopuLevel = "III"
                    }

                    "四" -> {
                        cardiopuLevel = "IV"
                    }
                    else ->{
                        cardiopuLevel = "I"
                    }
                }
                binding.sixminReportTvCardiopulmonaryLevel.text = "${cardiopuLevel}级"
                //心功能严重程度
                var cardiacFunctionLeve = ""
                when (sixMinRecordsBean.evaluationBean[0].cardiopuDegree) {

                    "1" -> {
                        cardiacFunctionLeve = "重度"
                    }

                    "2" -> {
                        cardiacFunctionLeve = "中度"
                    }

                    "3" -> {
                        cardiacFunctionLeve = "轻度"
                    }

                    else -> {
                        cardiacFunctionLeve =
                            usbTransferUtil.dealCardiopuDegree(BigDecimal(sixMinRecordsBean.evaluationBean[0].totalDistance))
                        cardiacFunctionLeve =
                            cardiacFunctionLeve.substring(0, cardiacFunctionLeve.length - 1)
                    }
                }
                binding.sixminReportTvCardiacFunctionLevel.text = cardiacFunctionLeve

                //处方建议
                if (sixMinRecordsBean.prescriptionBean[0].movementWay == "0" || sixMinRecordsBean.prescriptionBean[0].movementWay == "") {
                    binding.sixminReportRbWalk.isChecked = true
                } else {
                    binding.sixminReportRbRun.isChecked = true
                }

                Log.d("sixMinRecordsBean", Gson().toJson(sixMinRecordsBean))

                val stopOr = sixMinRecordsBean.otherBean[0].stopOr
                val sb = StringBuilder()
                if(stopOr == "1"){
                    if(sixMinRecordsBean.otherBean[0].stopReason.isEmpty()){
                        sb.append("步行了" + sixMinRecordsBean.otherBean[0].stopTime + "，停止原因：无。")
                    }else{
                        sb.append("步行了" + sixMinRecordsBean.otherBean[0].stopTime + "，停止原因：" + sixMinRecordsBean.otherBean[0].stopReason + "。")
                    }
                }else{
                    if(sixMinRecordsBean.otherBean[0].badSymptoms.isEmpty()){
                        sb.append("完成六分钟试验，未出现不良症状。")
                    }else{
                        sb.append("完成六分钟试验，不良症状：" + sixMinRecordsBean.otherBean[0].badSymptoms + "。")
                    }
                }

                if(sixMinRecordsBean.infoBean.restDuration != "-1"){
                    sb.append("中途休息了"+sixMinRecordsBean.infoBean.restDuration+"秒。")
                }

                binding.sixminReportTvConclusion.text = sb.toString()

                binding.sixminReportTvSportStride.text = "${sixMinRecordsBean.prescriptionBean[0].strideBefore}~${sixMinRecordsBean.prescriptionBean[0].strideAfter}米/分钟"
                binding.sixminReportTvSportDistance.text = "${sixMinRecordsBean.prescriptionBean[0].movementDistance}~${sixMinRecordsBean.prescriptionBean[0].movementDistanceAfter}米"
                binding.sixminReportTvSportTime.text = "${sixMinRecordsBean.prescriptionBean[0].movementTime}分钟"
                binding.sixminReportTvPrescriptionHeartBeat.text = "${sixMinRecordsBean.prescriptionBean[0].heartrateRate}bmp"
                var unit = ""
                unit = when(sixMinRecordsBean.prescriptionBean[0].cycleUnit){
                    "0" -> {
                        "周"
                    }

                    "1" -> {
                        "月"
                    }

                    else -> {
                        "年"
                    }
                }
                binding.sixminReportTvSportPeroid.text = "${sixMinRecordsBean.prescriptionBean[0].movementWeeklyNumber}次/周，${sixMinRecordsBean.prescriptionBean[0].movementCycle}$unit"
                binding.sixminReportTvPrescriptionTiredLevel.text = sixMinRecordsBean.prescriptionBean[0].pilaoControl
                binding.sixminReportTvPrescriptionMetab.text = "${sixMinRecordsBean.prescriptionBean[0].metabMet}METs"
                binding.sixminReportTvNote.text = sixMinRecordsBean.prescriptionBean[0].remarke
                binding.sixminReportTvCheckDoctor.text = "检验医生：${sixMinRecordsBean.prescriptionBean[0].remarkeName}"
                binding.sixminReportTvTestDate.text = sixMinRecordsBean.infoBean.addTime
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getLastDistance():String {
        var distance = "/"
        if (reportEvaluationList.size > 1) {
            for (i in 0 until reportEvaluationList.size) {
                val evaluation = reportEvaluationList[i]
                if (evaluation.reportId == sixMinRecordsBean.infoBean.reportNo) {
                    if (i == 0) {
                        break
                    } else {
                        val infoBe = reportEvaluationList[i - 1]
                        distance =  infoBe.totalDistance
                    }
                }
            }
        }
        return distance
    }

    private fun dip2px(context: Context, dpValue: Int): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
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