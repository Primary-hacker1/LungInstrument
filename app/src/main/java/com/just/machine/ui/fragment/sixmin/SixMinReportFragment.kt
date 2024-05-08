package com.just.machine.ui.fragment.sixmin

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.common.base.CommonBaseFragment
import com.common.viewmodel.LiveDataEvent
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.just.machine.model.BloodOxyLineEntryBean
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.model.SixMinReportItemBean
import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.ui.activity.SixMinDetectActivity
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.CommonUtil
import com.just.machine.util.USBTransferUtil
import com.just.machine.util.WordUtil
import com.just.news.R
import com.just.news.databinding.FragmentSixminReportBinding
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigDecimal


/**
 *create by 2020/6/19
 *@author zt
 */
@AndroidEntryPoint
class SixMinReportFragment : CommonBaseFragment<FragmentSixminReportBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var mActivity: SixMinDetectActivity
    private var reportRowList = mutableListOf<SixMinReportItemBean>()
    private var reportEvaluationList = mutableListOf<SixMinReportEvaluation>()
    private var pngSavePath = "" //报告图片保存路劲
    private lateinit var usbTransferUtil: USBTransferUtil
    private var sixMinRecordsBean: SixMinRecordsBean = SixMinRecordsBean()//6分钟报告信息
    private lateinit var bloodOxyDataSet: LineDataSet
    private lateinit var heartBeatDataSet: LineDataSet
    private lateinit var breathingDataSet: LineDataSet
    private lateinit var stepsDataSet: LineDataSet

    override fun loadData() {//懒加载

    }


    override fun initView() {
        if (activity is SixMinDetectActivity) {
            mActivity = activity as SixMinDetectActivity
        }
        initLineChart(binding.sixminReportLineChartBloodOxygen, 1)
        initLineChart(binding.sixminReportLineChartHeartBeat, 2)
        initLineChart(binding.sixminReportLineChartBreathing, 3)
        initLineChart(binding.sixminReportLineChartSteps, 4)
        viewModel.getSixMinReportEvaluation()
        lifecycleScope.launch {
            kotlinx.coroutines.delay(1000L)
            viewModel.getSixMinReportInfoById(
                mActivity.sixMinPatientId.toLong(), mActivity.sixMinReportNo
            )
        }
    }

    override fun initListener() {
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
        binding.sixminReportLlExportReport.setOnClickListener {

        }

        binding.sixminReportLlPrintReport.setOnClickListener {
            try {
                val bloodPng = File(
                    mActivity.getExternalFilesDir("")?.absolutePath,
                    pngSavePath + File.separator + "imageBlood.png"
                )
                val heartPng = File(
                    mActivity.getExternalFilesDir("")?.absolutePath,
                    pngSavePath + File.separator + "imageHeart.png"
                )
                val hsHxlPng =
                    if (sixMinRecordsBean.infoBean.bsHxl.isEmpty() || sixMinRecordsBean.infoBean.bsHxl == "0") {
                        File(
                            mActivity.getExternalFilesDir("")?.absolutePath,
                            pngSavePath + File.separator + "imageBreathing.png"
                        )
                    } else {
                        File(
                            mActivity.getExternalFilesDir("")?.absolutePath,
                            pngSavePath + File.separator + "imageSteps.png"
                        )

                    }

                val templatePath = File.separator+"templates" + File.separator + "报告模板3页-无截图.xml"
                var pageNum = 0
                val root = mutableMapOf<String, Any>()
                dealPageOne(root)
                dealPageTow(root, bloodPng, heartPng, hsHxlPng)
                pageNum = 2
                root["pageNum"] = pageNum
                val byteArrayOutputStream = WordUtil.process(root, templatePath)

                var fileOutputStream: FileOutputStream? = null
                val docPath =
                    mActivity.getExternalFilesDir("")?.absolutePath + File.separator + "sixminreport" + File.separator + "六分钟步行试验检测报告.doc"
                try {
                    fileOutputStream = FileOutputStream(docPath)
                    fileOutputStream.write(byteArrayOutputStream.toByteArray())
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        fileOutputStream?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun dealPageOne(root: MutableMap<String, Any>) {
        var patientName = sixMinRecordsBean.infoBean.patientName
        if (patientName.length > 9) {
            patientName = patientName.substring(0, 7)
            patientName += "..."
        }
        root["patientName"] = patientName
        root["xingbieStr"] = sixMinRecordsBean.infoBean.patientSix
        root["patientAge"] = sixMinRecordsBean.infoBean.patientAge
        root["patientHeight"] = sixMinRecordsBean.infoBean.patientHeight
        root["patientWeight"] = sixMinRecordsBean.infoBean.patientWeight
        root["patientBmi"] = sixMinRecordsBean.infoBean.patientBmi
        root["medicalNo"] = sixMinRecordsBean.infoBean.medicalNo
        root["predictionDistance"] = sixMinRecordsBean.infoBean.predictionDistance
        root["medicalHistory"] = sixMinRecordsBean.infoBean.medicalHistory
        root["clinicalDiagnosis"] = sixMinRecordsBean.infoBean.clinicalDiagnosis
        root["medicineUse"] = sixMinRecordsBean.infoBean.medicineUse
        var bianhaoStr: String = sixMinRecordsBean.infoBean.reportNo
        if (bianhaoStr.length >= 12) {
            val newBianhao = bianhaoStr.substring(6, 12)
            bianhaoStr = bianhaoStr.replace(newBianhao.toRegex(), "******")
        }
        root["reportNo"] = bianhaoStr
        root["useName"] = mActivity.sysSettingBean.sysOther.useOrg
        dealPageTable(root)
        //综合评估
        var befoFatigueLevel: String = sixMinRecordsBean.evaluationBean[0].befoFatigueLevel
        if (befoFatigueLevel.isNotEmpty()) {
            befoFatigueLevel += "级"
        }
        root["fatigueLevel"] =
            befoFatigueLevel + "/" + sixMinRecordsBean.evaluationBean[0].fatigueLevel + "级"
        root["totalDistanceBefore"] = getLastDistance()
        root["totalWalk"] = sixMinRecordsBean.evaluationBean[0].totalWalk
        var befoBreathingLevel: String = sixMinRecordsBean.evaluationBean[0].befoBreathingLevel
        if (befoBreathingLevel.isNotEmpty()) {
            befoBreathingLevel += "级"
        }
        root["breathingLevel"] =
            befoBreathingLevel + "/" + sixMinRecordsBean.evaluationBean[0].breathingLevel + "级";
    }

    private fun dealPageTable(root: MutableMap<String, Any>) {
        root["xinlv0"] = sixMinRecordsBean.heartBeatBean[0].heartStop
        root["xinlv1"] = sixMinRecordsBean.heartBeatBean[0].heartOne
        root["xinlv2"] = sixMinRecordsBean.heartBeatBean[0].heartTwo
        root["xinlv3"] = sixMinRecordsBean.heartBeatBean[0].heartThree
        root["xinlv4"] = sixMinRecordsBean.heartBeatBean[0].heartFour
        root["xinlv5"] = sixMinRecordsBean.heartBeatBean[0].heartFive
        root["xinlv6"] = sixMinRecordsBean.heartBeatBean[0].heartSix
        root["xinlv7"] = sixMinRecordsBean.heartBeatBean[0].heartBig
        root["xinlv8"] = sixMinRecordsBean.heartBeatBean[0].heartSmall
        root["xinlv9"] = sixMinRecordsBean.heartBeatBean[0].heartAverage
        //血氧
        root["xueyang0"] = sixMinRecordsBean.bloodOxyBean[0].bloodStop
        root["xueyang1"] = sixMinRecordsBean.bloodOxyBean[0].bloodOne
        root["xueyang2"] = sixMinRecordsBean.bloodOxyBean[0].bloodTwo
        root["xueyang3"] = sixMinRecordsBean.bloodOxyBean[0].bloodThree
        root["xueyang4"] = sixMinRecordsBean.bloodOxyBean[0].bloodFour
        root["xueyang5"] = sixMinRecordsBean.bloodOxyBean[0].bloodFive
        root["xueyang6"] = sixMinRecordsBean.bloodOxyBean[0].bloodSix
        root["xueyang7"] = sixMinRecordsBean.bloodOxyBean[0].bloodBig
        root["xueyang8"] = sixMinRecordsBean.bloodOxyBean[0].bloodSmall
        root["xueyang9"] = sixMinRecordsBean.bloodOxyBean[0].bloodAverage
        //呼吸率/步数
        if (sixMinRecordsBean.infoBean.bsHxl === "1") {
            root["hxOrBs"] = "呼吸率"
            root["hxOrBs0"] = sixMinRecordsBean.breathingBean[0].breathingStop
            root["hxOrBs1"] = sixMinRecordsBean.breathingBean[0].breathingOne
            root["hxOrBs2"] = sixMinRecordsBean.breathingBean[0].breathingTwo
            root["hxOrBs3"] = sixMinRecordsBean.breathingBean[0].breathingThree
            root["hxOrBs4"] = sixMinRecordsBean.breathingBean[0].breathingFour
            root["hxOrBs5"] = sixMinRecordsBean.breathingBean[0].breathingFive
            root["hxOrBs6"] = sixMinRecordsBean.breathingBean[0].breathingSix
            root["hxOrBs7"] = sixMinRecordsBean.breathingBean[0].breathingBig
            root["hxOrBs8"] = sixMinRecordsBean.breathingBean[0].breathingSmall
            root["hxOrBs9"] = sixMinRecordsBean.breathingBean[0].breathingAverage
        } else if (sixMinRecordsBean.infoBean.bsHxl === "0") {
            root["hxOrBs"] = "步数"
            root["hxOrBs0"] = sixMinRecordsBean.walkBean[0].walkStop
            root["hxOrBs1"] = sixMinRecordsBean.walkBean[0].walkOne
            root["hxOrBs2"] = sixMinRecordsBean.walkBean[0].walkTwo
            root["hxOrBs3"] = sixMinRecordsBean.walkBean[0].walkThree
            root["hxOrBs4"] = sixMinRecordsBean.walkBean[0].walkFour
            root["hxOrBs5"] = sixMinRecordsBean.walkBean[0].walkFive
            root["hxOrBs6"] = sixMinRecordsBean.walkBean[0].walkSix
            root["hxOrBs7"] = sixMinRecordsBean.walkBean[0].walkBig
            root["hxOrBs8"] = sixMinRecordsBean.walkBean[0].waklSmall
            root["hxOrBs9"] = sixMinRecordsBean.walkBean[0].walkAverage
        }
        var startPressure = "/"
        if (sixMinRecordsBean.otherBean[0].startHighPressure != "0") {
            startPressure =
                sixMinRecordsBean.otherBean[0].startHighPressure + "/" + sixMinRecordsBean.otherBean[0].startLowPressure
        }
        root["xueya1"] = startPressure
        var stopPressure = "/"
        if (sixMinRecordsBean.otherBean[0].stopHighPressure != "0") {
            stopPressure =
                sixMinRecordsBean.otherBean[0].stopHighPressure + "/" + sixMinRecordsBean.otherBean[0].stopLowPressure
        }
        root["xueya2"] = stopPressure
    }

    private fun dealPageTow(
        root: MutableMap<String, Any>, bloodPng: File, heartPng: File, hsHxlPng: File
    ) {
        //血氧
        val base64Blood: String = CommonUtil.imageTobase64(bloodPng.absolutePath)
        root["imageBlood"] = base64Blood
        //心率
        val base64Hreat: String = CommonUtil.imageTobase64(heartPng.absolutePath)
        root["imageHreat"] = base64Hreat
        var qushiStr = "呼吸率趋势"
        if (sixMinRecordsBean.infoBean.bsHxl == "0") {
            qushiStr = "步数趋势"
        }
        root["qushi"] = qushiStr
        val base64WalkAndHxl: String = CommonUtil.imageTobase64(hsHxlPng.absolutePath)
        root["imageWalkAndHxl"] = base64WalkAndHxl
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSixminReportBinding.inflate(inflater, container, false)


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
                "时间(min)", "静止", "1", "2", "3", "4", "5", "6", "最大值", "最小值", "平均值"
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
        val padding = dip2px(mActivity.applicationContext, 1)
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
                tvNo.textSize = dip2px(mActivity.applicationContext, 7).toFloat()
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
                    0, 0, dip2px(mActivity.applicationContext, 2), 0
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
                dip2px(mActivity, 6),
                dip2px(mActivity, 3),
                dip2px(mActivity, 6),
                dip2px(mActivity, 3)
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
                    if (sixMinRecordsBean.heartBeatBean[0].heartStop.isNotEmpty()) "${sixMinRecordsBean.heartBeatBean[0].heartStop}bmp" else "0bmp"
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

                if (sixMinRecordsBean.otherBean[0].stopOr == "0") {
                    stepsDataSet.addEntry(
                        Entry(
                            0f, sixMinRecordsBean.walkBean[0].walkStop.toFloat()
                        )
                    )
                    stepsDataSet.addEntry(
                        Entry(
                            1f, sixMinRecordsBean.walkBean[0].walkOne.toFloat()
                        )
                    )
                    stepsDataSet.addEntry(
                        Entry(
                            2f, sixMinRecordsBean.walkBean[0].walkTwo.toFloat()
                        )
                    )
                    stepsDataSet.addEntry(
                        Entry(
                            3f, sixMinRecordsBean.walkBean[0].walkThree.toFloat()
                        )
                    )
                    stepsDataSet.addEntry(
                        Entry(
                            4f, sixMinRecordsBean.walkBean[0].walkFour.toFloat()
                        )
                    )
                    stepsDataSet.addEntry(
                        Entry(
                            5f, sixMinRecordsBean.walkBean[0].walkFive.toFloat()
                        )
                    )
                    stepsDataSet.addEntry(
                        Entry(
                            6f, sixMinRecordsBean.walkBean[0].walkSix.toFloat()
                        )
                    )
                } else {
                    val stopTime = sixMinRecordsBean.otherBean[0].stopTime
                    val times = stopTime.split("分")
                    if (times.size > 1) {
                        when (times[0]) {
                            "1" -> {
                                stepsDataSet.addEntry(
                                    Entry(
                                        0f, sixMinRecordsBean.walkBean[0].walkStop.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        1f, sixMinRecordsBean.walkBean[0].walkOne.toFloat()
                                    )
                                )
                            }

                            "2" -> {
                                stepsDataSet.addEntry(
                                    Entry(
                                        0f, sixMinRecordsBean.walkBean[0].walkStop.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        1f, sixMinRecordsBean.walkBean[0].walkOne.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        2f, sixMinRecordsBean.walkBean[0].walkTwo.toFloat()
                                    )
                                )
                            }

                            "3" -> {
                                stepsDataSet.addEntry(
                                    Entry(
                                        0f, sixMinRecordsBean.walkBean[0].walkStop.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        1f, sixMinRecordsBean.walkBean[0].walkOne.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        2f, sixMinRecordsBean.walkBean[0].walkTwo.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        3f, sixMinRecordsBean.walkBean[0].walkThree.toFloat()
                                    )
                                )
                            }

                            "4" -> {
                                stepsDataSet.addEntry(
                                    Entry(
                                        0f, sixMinRecordsBean.walkBean[0].walkStop.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        1f, sixMinRecordsBean.walkBean[0].walkOne.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        2f, sixMinRecordsBean.walkBean[0].walkTwo.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        3f, sixMinRecordsBean.walkBean[0].walkThree.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        4f, sixMinRecordsBean.walkBean[0].walkFour.toFloat()
                                    )
                                )
                            }

                            "5" -> {
                                stepsDataSet.addEntry(
                                    Entry(
                                        0f, sixMinRecordsBean.walkBean[0].walkStop.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        1f, sixMinRecordsBean.walkBean[0].walkOne.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        2f, sixMinRecordsBean.walkBean[0].walkTwo.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        3f, sixMinRecordsBean.walkBean[0].walkThree.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        4f, sixMinRecordsBean.walkBean[0].walkFour.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        5f, sixMinRecordsBean.walkBean[0].walkFive.toFloat()
                                    )
                                )
                            }

                            "6" -> {
                                stepsDataSet.addEntry(
                                    Entry(
                                        0f, sixMinRecordsBean.walkBean[0].walkStop.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        1f, sixMinRecordsBean.walkBean[0].walkOne.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        2f, sixMinRecordsBean.walkBean[0].walkTwo.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        3f, sixMinRecordsBean.walkBean[0].walkThree.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        4f, sixMinRecordsBean.walkBean[0].walkFour.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        5f, sixMinRecordsBean.walkBean[0].walkFive.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        6f, sixMinRecordsBean.walkBean[0].walkSix.toFloat()
                                    )
                                )
                            }

                            else -> {
                                stepsDataSet.addEntry(
                                    Entry(
                                        0f, sixMinRecordsBean.walkBean[0].walkStop.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        1f, sixMinRecordsBean.walkBean[0].walkOne.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        2f, sixMinRecordsBean.walkBean[0].walkTwo.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        3f, sixMinRecordsBean.walkBean[0].walkThree.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        4f, sixMinRecordsBean.walkBean[0].walkFour.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        5f, sixMinRecordsBean.walkBean[0].walkFive.toFloat()
                                    )
                                )
                                stepsDataSet.addEntry(
                                    Entry(
                                        6f, sixMinRecordsBean.walkBean[0].walkSix.toFloat()
                                    )
                                )
                            }
                        }
                    }
                }
                binding.sixminReportLineChartSteps.lineData.addDataSet(
                    stepsDataSet
                )
                binding.sixminReportLineChartSteps.lineData.notifyDataChanged()
                binding.sixminReportLineChartSteps.notifyDataSetChanged()
                binding.sixminReportLineChartSteps.invalidate()

                binding.sixminReportLineChartBloodOxygen.lineData.notifyDataChanged()
                binding.sixminReportLineChartBloodOxygen.notifyDataSetChanged()
                binding.sixminReportLineChartBloodOxygen.invalidate()

                pngSavePath =
                    File.separator + "sixminreportpng" + File.separator + sixMinRecordsBean.infoBean.reportNo
                val file = File(mActivity.getExternalFilesDir("")?.absolutePath, pngSavePath)
                if (!file.exists()) {
                    file.mkdirs()
                }

                binding.sixminReportLineChartBloodOxygen.saveToPath(
                    mActivity, "imageBlood", pngSavePath
                )
                binding.sixminReportLineChartHeartBeat.saveToPath(
                    mActivity, "imageHeart", pngSavePath
                )
                binding.sixminReportLineChartBreathing.saveToPath(
                    mActivity, "imageBreathing", pngSavePath
                )
                binding.sixminReportLineChartSteps.saveToPath(mActivity, "imageSteps", pngSavePath)
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
                textColor = ContextCompat.getColor(mActivity, R.color.text3)
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
                textColor = ContextCompat.getColor(mActivity, R.color.text3)
                //左侧Y轴的最大值和最小值
                axisMaximum =
                    if (type == 1) 100f else if (type == 2) 180f else if (type == 3) 60f else 200f
                axisMinimum =
                    if (type == 1) 80f else if (type == 2) 30f else if (type == 3) 10f else 0f
                setLabelCount(6, true)
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

            when (type) {
                1 -> {
                    bloodOxyDataSet = LineDataSet(null, "")
                    bloodOxyDataSet.lineWidth = 1.0f
                    bloodOxyDataSet.color = ContextCompat.getColor(mActivity, R.color.text3)
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
                    heartBeatDataSet.color = ContextCompat.getColor(mActivity, R.color.text3)
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
                    breathingDataSet.color = ContextCompat.getColor(mActivity, R.color.text3)
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
                    stepsDataSet.color = ContextCompat.getColor(mActivity, R.color.text3)
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

}
