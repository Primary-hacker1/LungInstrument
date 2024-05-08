package com.just.machine.ui.activity

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.common.base.CommonBaseActivity
import com.common.viewmodel.LiveDataEvent
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.just.machine.model.BloodOxyLineEntryBean
import com.just.machine.model.Constants
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.model.SixMinReportItemBean
import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.ActivitySixMinReportBinding
import com.justsafe.libview.util.SystemUtil
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
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
    private var sixMinReportType = "" //跳转类型 1新增 2编辑
    private var pngSavePath = "" //报告图片保存路劲
    private lateinit var usbTransferUtil: USBTransferUtil
    private var sixMinRecordsBean: SixMinRecordsBean = SixMinRecordsBean()//6分钟报告信息
    private lateinit var bloodOxyDataSet: LineDataSet
    private lateinit var heartBeatDataSet: LineDataSet
    private lateinit var breathingDataSet: LineDataSet
    private lateinit var stepsDataSet: LineDataSet

    override fun getViewBinding() = ActivitySixMinReportBinding.inflate(layoutInflater)

    override fun initView() {
        usbTransferUtil = USBTransferUtil.getInstance()
        sixMinPatientId = intent.extras?.getString(Constants.sixMinPatientInfo).toString()
        sixMinReportNo = intent.extras?.getString(Constants.sixMinReportNo).toString()
        sixMinReportType = intent.extras?.getString(Constants.sixMinReportType).toString()
        initLineChart(binding.sixminReportLineChartBloodOxygen, 1)
        initLineChart(binding.sixminReportLineChartHeartBeat, 2)
        initLineChart(binding.sixminReportLineChartBreathing, 3)
        initLineChart(binding.sixminReportLineChartSteps, 4)
        viewModel.getSixMinReportEvaluation()
        lifecycleScope.launch {
            kotlinx.coroutines.delay(200L)
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
            if (sixMinReportType == "1") {
                PatientActivity.startPatientActivity(this, "finishSixMinTest")
                finish()
            } else {
                finish()
            }
        }

        binding.sixminReportLlExportReport.setOnClickListener {

        }

        binding.sixminReportLlPrintReport.setOnClickListener {
            val bloodPng = File(
                getExternalFilesDir("")?.absolutePath,
                pngSavePath + File.separator + "imageBlood.png"
            )
            val heartPng = File(
                getExternalFilesDir("")?.absolutePath,
                pngSavePath + File.separator + "imageHeart.png"
            )
            val hsHxlPng = if(sixMinRecordsBean.infoBean.bsHxl.isEmpty() || sixMinRecordsBean.infoBean.bsHxl == "0"){
                File(
                    getExternalFilesDir("")?.absolutePath,
                    pngSavePath + File.separator + "imageBreathing.png"
                )
            }else{
                File(
                    getExternalFilesDir("")?.absolutePath,
                    pngSavePath + File.separator + "imageSteps.png"
                )
            }
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
                    "病  历  号: ${sixMinRecordsBean.infoBean.medicalNo}"
                binding.sixminReportTvPatientHeight.text =
                    "身高:${sixMinRecordsBean.infoBean.patientHeight}cm"
                binding.sixminReportTvPatientWeight.text =
                    "体重:${sixMinRecordsBean.infoBean.patientWeight}kg"
                binding.sixminReportTvPatientBmi.text =
                    "BMI :${sixMinRecordsBean.infoBean.patientBmi}"
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

                    else -> {
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
                if (stopOr == "1") {
                    if (sixMinRecordsBean.otherBean[0].stopReason.isEmpty()) {
                        sb.append("步行了" + sixMinRecordsBean.otherBean[0].stopTime + "，停止原因：无。")
                    } else {
                        sb.append("步行了" + sixMinRecordsBean.otherBean[0].stopTime + "，停止原因：" + sixMinRecordsBean.otherBean[0].stopReason + "。")
                    }
                } else {
                    if (sixMinRecordsBean.otherBean[0].badSymptoms.isEmpty()) {
                        sb.append("完成六分钟试验，未出现不良症状。")
                    } else {
                        sb.append("完成六分钟试验，不良症状：" + sixMinRecordsBean.otherBean[0].badSymptoms + "。")
                    }
                }

                if (sixMinRecordsBean.infoBean.restDuration != "-1") {
                    sb.append("中途休息了" + sixMinRecordsBean.infoBean.restDuration + "秒。")
                }

                binding.sixminReportTvConclusion.text = sb.toString()

                if (sixMinRecordsBean.prescriptionBean[0].distanceState.isEmpty() || sixMinRecordsBean.prescriptionBean[0].distanceState == "1") {
                    binding.sixminReportTvSportStride.text =
                        "${sixMinRecordsBean.prescriptionBean[0].strideBefore}~${sixMinRecordsBean.prescriptionBean[0].strideAfter}米/分钟"
                    binding.sixminReportTvSportDistance.text =
                        "${sixMinRecordsBean.prescriptionBean[0].movementDistance}~${sixMinRecordsBean.prescriptionBean[0].movementDistanceAfter}米"
                } else {
                    binding.sixminReportTvSportStride.text = "/"
                    binding.sixminReportTvSportDistance.text = "/"
                }

                if (sixMinRecordsBean.prescriptionBean[0].heartrateState.isEmpty() || sixMinRecordsBean.prescriptionBean[0].heartrateState == "1") {
                    binding.sixminReportTvPrescriptionHeartBeat.text =
                        "${sixMinRecordsBean.prescriptionBean[0].heartrateRate}bmp"
                } else {
                    binding.sixminReportTvPrescriptionHeartBeat.text = "/"
                }
                binding.sixminReportTvSportTime.text =
                    "${sixMinRecordsBean.prescriptionBean[0].movementTime}分钟"

                var unit = ""
                unit = when (sixMinRecordsBean.prescriptionBean[0].cycleUnit) {
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
                binding.sixminReportTvSportPeroid.text =
                    "${sixMinRecordsBean.prescriptionBean[0].movementWeeklyNumber}次/周，${sixMinRecordsBean.prescriptionBean[0].movementCycle}$unit"
                if (sixMinRecordsBean.prescriptionBean[0].pllevState.isEmpty() || sixMinRecordsBean.prescriptionBean[0].pllevState == "1") {
                    binding.sixminReportTvPrescriptionTiredLevel.text =
                        sixMinRecordsBean.prescriptionBean[0].pilaoControl
                } else {
                    binding.sixminReportTvPrescriptionTiredLevel.text = "/"
                }

                if (sixMinRecordsBean.prescriptionBean[0].metabState.isEmpty() || sixMinRecordsBean.prescriptionBean[0].metabState == "1") {
                    binding.sixminReportTvPrescriptionMetab.text =
                        "${sixMinRecordsBean.prescriptionBean[0].metabMet}METs"
                } else {
                    binding.sixminReportTvPrescriptionMetab.text = "/"
                }

                binding.sixminReportTvNote.text = sixMinRecordsBean.prescriptionBean[0].remarke
                binding.sixminReportTvCheckDoctor.text =
                    "检验医生：${sixMinRecordsBean.prescriptionBean[0].remarkeName}"
                binding.sixminReportTvTestDate.text = sixMinRecordsBean.infoBean.addTime

                //第二页
                binding.sixminReportTvUseNameSec.text = sixMinRecordsBean.otherBean[0].useName
                binding.sixminReportTvReportNoSec.text =
                    "编号:${sixMinRecordsBean.infoBean.reportNo}"

                if (sixMinRecordsBean.infoBean.bsHxl == "0") {
                    binding.sixminReportLlLineChartSteps.visibility = View.VISIBLE
                    binding.sixminReportLlLineChartBreathing.visibility = View.GONE
                } else {
                    binding.sixminReportLlLineChartSteps.visibility = View.GONE
                    binding.sixminReportLlLineChartBreathing.visibility = View.VISIBLE
                }

                val bloodAll = sixMinRecordsBean.bloodOxyBean[0].bloodAll
                val listType = object : TypeToken<List<BloodOxyLineEntryBean>>() {}.type
                val bloodOxyList: List<BloodOxyLineEntryBean> = Gson().fromJson(bloodAll, listType)
                if (bloodOxyList.isNotEmpty()) {
                    bloodOxyList.forEach {
                        bloodOxyDataSet.addEntry(Entry(it.bloodX, it.bloodY))
                    }
                }
                binding.sixminReportLineChartBloodOxygen.lineData.addDataSet(
                    bloodOxyDataSet
                )

                binding.sixminReportLineChartBloodOxygen.lineData.notifyDataChanged()
                binding.sixminReportLineChartBloodOxygen.notifyDataSetChanged()
                binding.sixminReportLineChartBloodOxygen.invalidate()

                pngSavePath =
                    File.separator + "sixminreportpng" + File.separator + sixMinRecordsBean.infoBean.reportNo
                val file = File(getExternalFilesDir("")?.absolutePath, pngSavePath)
                if (!file.exists()) {
                    file.mkdirs()
                }

                binding.sixminReportLineChartBloodOxygen.saveToPath(this, "imageBlood", pngSavePath)
                binding.sixminReportLineChartHeartBeat.saveToPath(this, "imageHeart", pngSavePath)
                binding.sixminReportLineChartBreathing.saveToPath(
                    this,
                    "imageBreathing",
                    pngSavePath
                )
                binding.sixminReportLineChartSteps.saveToPath(this, "imageSteps", pngSavePath)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getLastDistance(): String {
        var distance = "/"
        if (reportEvaluationList.size > 1) {
            for (i in 0 until reportEvaluationList.size) {
                val evaluation = reportEvaluationList[i]
                if (evaluation.reportId == sixMinRecordsBean.infoBean.reportNo) {
                    if (i == 0) {
                        break
                    } else {
                        val infoBe = reportEvaluationList[i - 1]
                        distance = infoBe.totalDistance
                    }
                }
            }
        }
        return distance
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
            description.text = ""
            xAxis?.apply {
                textSize = 10f
                textColor = ContextCompat.getColor(this@SixMinReportActivity, R.color.text3)
                //X轴最大值和最小值
                axisMaximum = 6F
                axisMinimum = 0F
                offsetLeftAndRight(0)
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
                textColor = ContextCompat.getColor(this@SixMinReportActivity, R.color.text3)
                //左侧Y轴的最大值和最小值
                axisMaximum = if (type == 1) 100f else if (type == 2) 180f else 60f
                axisMinimum = if (type == 1) 80f else if (type == 2) 30f else 10f
                setLabelCount(6, true)
                //绘制网格线(样式虚线)
                enableGridDashedLine(2f, 2f, 0f)
                gridColor = ContextCompat.getColor(this@SixMinReportActivity, R.color.text3)
                setDrawGridLines(true)
                setDrawAxisLine(false) //绘制左边Y轴是否显示
                setDrawZeroLine(false) //是否开启0线
                isGranularityEnabled = true
            }

            legend?.apply {
                setDrawInside(false)
                formSize = 0f
            }

            when (type) {
                1 -> {
                    bloodOxyDataSet = LineDataSet(null, "")
                    bloodOxyDataSet.lineWidth = 1.0f
                    bloodOxyDataSet.color =
                        ContextCompat.getColor(this@SixMinReportActivity, R.color.text3)
                    bloodOxyDataSet.setDrawValues(false)
                    bloodOxyDataSet.setDrawCircles(false)
                    bloodOxyDataSet.setDrawCircleHole(false)
                    bloodOxyDataSet.setDrawFilled(false)
                    bloodOxyDataSet.mode = LineDataSet.Mode.LINEAR
                    val lineData = LineData(bloodOxyDataSet)
                    data = lineData
                }

                2 -> {
                    heartBeatDataSet = LineDataSet(null, "")
                    heartBeatDataSet.lineWidth = 1.0f
                    heartBeatDataSet.color =
                        ContextCompat.getColor(this@SixMinReportActivity, R.color.text3)
                    heartBeatDataSet.setDrawValues(false)
                    heartBeatDataSet.setDrawCircles(false)
                    heartBeatDataSet.setDrawCircleHole(false)
                    heartBeatDataSet.setDrawFilled(false)
                    heartBeatDataSet.mode = LineDataSet.Mode.LINEAR
                    val lineData = LineData(heartBeatDataSet)
                    data = lineData
                }

                3 -> {
                    breathingDataSet = LineDataSet(null, "")
                    breathingDataSet.lineWidth = 1.0f
                    breathingDataSet.color =
                        ContextCompat.getColor(this@SixMinReportActivity, R.color.text3)
                    breathingDataSet.setDrawValues(false)
                    breathingDataSet.setDrawCircles(false)
                    breathingDataSet.setDrawCircleHole(false)
                    breathingDataSet.setDrawFilled(false)
                    breathingDataSet.mode = LineDataSet.Mode.LINEAR
                    val lineData = LineData(breathingDataSet)
                    data = lineData
                }

                else -> {
                    stepsDataSet = LineDataSet(null, "")
                    stepsDataSet.lineWidth = 1.0f
                    stepsDataSet.color =
                        ContextCompat.getColor(this@SixMinReportActivity, R.color.text3)
                    stepsDataSet.setDrawValues(false)
                    stepsDataSet.setDrawCircles(false)
                    stepsDataSet.setDrawCircleHole(false)
                    stepsDataSet.setDrawFilled(false)
                    stepsDataSet.mode = LineDataSet.Mode.LINEAR
                    val lineData = LineData(stepsDataSet)
                    data = lineData
                }
            }
        }
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