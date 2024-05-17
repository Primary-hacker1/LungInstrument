package com.just.machine.ui.fragment.sixmin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.print.PrintAttributes
import android.print.PrintManager
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
import com.aspose.words.Document
import com.aspose.words.SaveFormat
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.viewmodel.LiveDataEvent
import com.deepoove.poi.XWPFTemplate
import com.deepoove.poi.config.Configure
import com.deepoove.poi.data.PictureRenderData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.just.machine.model.BloodOxyLineEntryBean
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.model.SixMinReportInfoAndEvaluation
import com.just.machine.model.SixMinReportItemBean
import com.just.machine.ui.activity.SixMinDetectActivity
import com.just.machine.ui.adapter.PdfDocumentAdapter
import com.just.machine.ui.dialog.LoadingDialogFragment
import com.just.machine.ui.dialog.SixMinPrintReportOptionsDialogFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.FragmentSixminReportBinding
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

/**
 * 6分钟报告界面
 */
@AndroidEntryPoint
class SixMinReportFragment : CommonBaseFragment<FragmentSixminReportBinding>() {

    private var mDisposable3s: Disposable? = null
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var mActivity: SixMinDetectActivity
    private var reportRowList = mutableListOf<SixMinReportItemBean>()
    private var reportEvaluationList = mutableListOf<SixMinReportInfoAndEvaluation>()
    private var pngSavePath = "" //报告图片保存路劲
    private lateinit var usbTransferUtil: USBTransferUtil
    private var sixMinRecordsBean: SixMinRecordsBean = SixMinRecordsBean()//6分钟报告信息
    private lateinit var bloodOxyDataSet: LineDataSet
    private lateinit var heartBeatDataSet: LineDataSet
    private lateinit var breathingDataSet: LineDataSet
    private lateinit var stepsDataSet: LineDataSet
    private lateinit var startLoadingDialogFragment:LoadingDialogFragment

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
        viewModel.getSixMinReportEvaluationById(mActivity.sixMinPatientId)
        lifecycleScope.launch {
            kotlinx.coroutines.delay(100L)
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
        binding.sixminReportLlExportReport.setNoRepeatListener {
            startLoadingDialogFragment =
                LoadingDialogFragment.startLoadingDialogFragment(
                    mActivity.supportFragmentManager,
                    "导出报告中..."
                )
            val templateName = "templates/报告模板-无截图.docx"

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
                        pngSavePath + File.separator + "imageSteps.png"
                    )
                } else {
                    File(
                        mActivity.getExternalFilesDir("")?.absolutePath,
                        pngSavePath + File.separator + "imageBreathing.png"
                    )

                }

            val filePath = File(
                mActivity.getExternalFilesDir("")?.absolutePath,
                File.separator + "sixminreport" + File.separator + sixMinRecordsBean.infoBean.reportNo
                        + File.separator + "六分钟步行试验检测报告.doc"
            )

            val pdfFilePath = File(
                mActivity.getExternalFilesDir("")?.absolutePath,
                File.separator + "sixminreport" + File.separator + sixMinRecordsBean.infoBean.reportNo
                        + File.separator + "六分钟步行试验检测报告.pdf"
            )

            if (filePath.parentFile?.exists() == false) {
                filePath.parentFile?.mkdirs()
            }

            val root = mutableMapOf<String, Any>()
            dealPageOne(root)
            dealPageTow(root, bloodPng, heartPng, hsHxlPng)

            lifecycleScope.launch(Dispatchers.IO) {
                val generateWord =
                    generateWord(
                        root,
                        templateName,
                        filePath.absolutePath
                    )
                if (!generateWord) {
                    mActivity.runOnUiThread {
                        mActivity.showMsg("生成word文档失败")
                    }
                } else {
                    // 加载Word文档
                    val doc = Document(filePath.absolutePath)
                    // 保存文档为PDF格式
                    doc.save(pdfFilePath.absolutePath, SaveFormat.PDF)
                    mActivity.runOnUiThread {
                        mActivity.showMsg("导出报告成功")
                        if(startLoadingDialogFragment.isVisible){
                            startLoadingDialogFragment.dismiss()
                        }
                    }
                }
            }
        }

        binding.sixminReportLlPrintReport.setNoRepeatListener {
            val dialogFragment =
                SixMinPrintReportOptionsDialogFragment.startSixMinPrintReportOptionsDialogFragment(
                    mActivity.supportFragmentManager
                )
            dialogFragment.setPrintReportOptionsClickListener(object :
                SixMinPrintReportOptionsDialogFragment.PrintReportOptionsClickListener {
                override fun onConfirmClick(
                    ecgOptions: String,
                    evaluationOptions: String,
                    prescriptionOptions: String
                ) {
                    try {
                        if (evaluationOptions == "0" && prescriptionOptions == "0") {
                            mActivity.showMsg("综合评估和处方建议不可都不打印")
                            return
                        }
                        dialogFragment.dismiss()
                        startLoadingDialogFragment =
                            LoadingDialogFragment.startLoadingDialogFragment(
                                mActivity.supportFragmentManager,
                                "加载报告中..."
                            )
                        var templateName = "templates/报告模板-无截图.docx"
                        if (evaluationOptions == "0") {
                            templateName = "templates/报告模板-无截图-无评估.docx"
                        }
                        if (prescriptionOptions == "0") {
                            templateName = "templates/报告模板-无截图-无处方.docx"
                        }
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
                                    pngSavePath + File.separator + "imageSteps.png"
                                )
                            } else {
                                File(
                                    mActivity.getExternalFilesDir("")?.absolutePath,
                                    pngSavePath + File.separator + "imageBreathing.png"
                                )

                            }

                        val filePath = File(
                            mActivity.getExternalFilesDir("")?.absolutePath,
                            File.separator + "sixminreport" + File.separator + sixMinRecordsBean.infoBean.reportNo
                                    + File.separator + "六分钟步行试验检测报告.doc"
                        )

                        val pdfFilePath = File(
                            mActivity.getExternalFilesDir("")?.absolutePath,
                            File.separator + "sixminreport" + File.separator + sixMinRecordsBean.infoBean.reportNo
                                    + File.separator + "六分钟步行试验检测报告.pdf"
                        )

                        if (filePath.parentFile?.exists() == false) {
                            filePath.parentFile?.mkdirs()
                        }

                        val root = mutableMapOf<String, Any>()
                        dealPageOne(root)
                        dealPageTow(root, bloodPng, heartPng, hsHxlPng)

                        lifecycleScope.launch(Dispatchers.IO) {
                            val generateWord =
                                generateWord(
                                    root,
                                    templateName,
                                    filePath.absolutePath
                                )
                            if (!generateWord) {
                                mActivity.runOnUiThread {
                                    mActivity.showMsg("生成word文档失败")
                                }
                            } else {
                                // 加载Word文档
                                val doc = Document(filePath.absolutePath)
                                // 保存文档为PDF格式
                                doc.save(pdfFilePath.absolutePath, SaveFormat.PDF)
                                mActivity.runOnUiThread {
                                    mActivity.showMsg("生成pdf文档成功")
                                    if(startLoadingDialogFragment.isVisible){
                                        startLoadingDialogFragment.dismiss()
                                    }
                                }
                                onPrintPdf(pdfFilePath.absolutePath)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onCancelClick() {

                }
            })

        }
    }

    /**
     * 生成word文档
     */
    private fun generateWord(
        params: MutableMap<String, Any>,
        templateName: String,
        savePath: String
    ): Boolean {
        val open = mActivity.assets.open(templateName)
        val template = XWPFTemplate.compile(open)
        params.let {
            template.render(params)
        }
        var outputStream: FileOutputStream? = null

        try {
            outputStream = FileOutputStream(savePath)
            if (outputStream == null) {
//                LogUtil.w(TAG, "world path:$path is null,check permiss")
//                Toast.show(mActivity.getString(R.string.save_faild_cannot_writ))
                return false
            }
            template.write(outputStream)
            outputStream.flush()
//            Toast.show("save success,path:$path")
            return true
        } catch (e: IOException) {
//            LogUtil.e(TAG, "world write to output stream io exception:" +
//                    LogUtil.objToString(e))
//            Toast.show(mActivity.getString(R.string.save_faild_cannot_writ))
            return false
        } finally {
            try {
                template.close()
                outputStream?.close()
            } finally {
//                Log.i(TAG, "write world to $savePath output stream over")
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
        root["disBefore"] = if (getLastDistance() != "/") "${getLastDistance()}米" else "/"
        root["toWalk"] = sixMinRecordsBean.evaluationBean[0].totalWalk + "步"
        var befoBreathingLevel: String = sixMinRecordsBean.evaluationBean[0].befoBreathingLevel
        if (befoBreathingLevel.isNotEmpty()) {
            befoBreathingLevel += "级"
        }
        root["breathLevel"] =
            befoBreathingLevel + "/" + sixMinRecordsBean.evaluationBean[0].breathingLevel + "级";
        root["toDistance"] = sixMinRecordsBean.evaluationBean[0].totalDistance + "米"

        var strideAverageStr = "/"
        var heartRestoreStr = "/"
        if (sixMinRecordsBean.prescriptionBean[0].prescripState == "1") {
            strideAverageStr = sixMinRecordsBean.strideBean[0].strideAverage + "米/分"
            heartRestoreStr = sixMinRecordsBean.heartBeatBean[0].heartRestore
        }
        root["striAvg"] = strideAverageStr
        root["metabEqu"] = sixMinRecordsBean.evaluationBean[0].metabEquivalent + "METs"
        root["accounted"] = sixMinRecordsBean.evaluationBean[0].accounted + "%"
        root["stHeart"] = sixMinRecordsBean.heartBeatBean[0].heartStop

        var gardLevel: String = sixMinRecordsBean.evaluationBean[0].cardiopuLevel
        gardLevel = when (gardLevel) {
            "一" -> {
                "I"
            }

            "二" -> {
                "II"
            }

            "三" -> {
                "III"
            }

            else -> {
                "IV"
            }
        }
        root["gardLevel"] = gardLevel + "级"
        var degreeStr = ""
        if (sixMinRecordsBean.evaluationBean[0].cardiopuDegree == "0") {
            degreeStr =
                mActivity.usbTransferUtil.dealCardiopuDegree(BigDecimal(sixMinRecordsBean.evaluationBean[0].totalDistance))
            degreeStr = degreeStr.substring(0, degreeStr.length - 1)
        } else {
            when (sixMinRecordsBean.evaluationBean[0].cardiopuDegree) {
                "1" -> {
                    degreeStr = "重度"
                }

                "2" -> {
                    degreeStr = "中度"
                }

                "3" -> {
                    degreeStr = "轻度"
                }
            }
        }
        root["cDegreeStr"] = degreeStr
        root["hRestore"] = heartRestoreStr
        var check4 = ""
        //提前完成了
        if (sixMinRecordsBean.otherBean[0].stopOr == "1") {
            var stopReason = ""
            stopReason = if (sixMinRecordsBean.otherBean[0].stopReason.isEmpty()) {
                "无"
            } else {
                sixMinRecordsBean.otherBean[0].stopReason
            }
            check4 =
                "步行了" + sixMinRecordsBean.otherBean[0].stopTime + "，停止原因：" + stopReason + "。"
        } else if (sixMinRecordsBean.otherBean[0].stopOr == "0") {
            //自动完成了
            check4 = if (sixMinRecordsBean.otherBean[0].badSymptoms.isEmpty()) {
                "完成六分钟试验，未出现不良症状。"
            } else {
                "完成六分钟试验，不良症状：" + sixMinRecordsBean.otherBean[0].badSymptoms + "。"
            }
        }
        if (sixMinRecordsBean.infoBean.restDuration != "-1") {
            check4 += "中途停留了" + sixMinRecordsBean.infoBean.restDuration + "秒。"
        }
        root["badSymptoms"] = check4
        if (sixMinRecordsBean.heartBeatBean[0].heartConclusion.isNotEmpty()) {
            root["heartConclusionStr"] =
                "心电结论：" + sixMinRecordsBean.heartBeatBean[0].heartConclusion + "。"
        }

        //运动处方建议
        val checkPng = File(
            mActivity.getExternalFilesDir("")?.absolutePath,
            "templates" + File.separator + "check.png"
        )
        val unCheckPng = File(
            mActivity.getExternalFilesDir("")?.absolutePath,
            "templates" + File.separator +"uncheck.png"
        )
        if (sixMinRecordsBean.prescriptionBean[0].movementWay == "0") {
            root["checkcf1"] = PictureRenderData(14,14,checkPng.absolutePath)
            root["checkcf2"] = PictureRenderData(16,16,unCheckPng.absolutePath)
        } else if (sixMinRecordsBean.prescriptionBean[0].movementWay == "1") {
            root["checkcf2"] = PictureRenderData(14,14,checkPng.absolutePath)
            root["checkcf1"] = PictureRenderData(16,16,unCheckPng.absolutePath)
        }
        var strideTitStrs = "/"
        var strideStrs = "/"
        var movDistanceTitStrs = "运动距离"
        var movDistanceStrs: String = sixMinRecordsBean.prescriptionBean[0].movementDistance + "米"
        var heartrateRateTitStr = "/"
        var heartrateRateStr = "/"
        var metabMetTitStr = "/"
        var metabMetStr = "/"
        var strTit45 = "自觉疲劳程度"
        var str45: String = sixMinRecordsBean.prescriptionBean[0].pilaoControl
        if (sixMinRecordsBean.prescriptionBean[0].pilaoControl.isEmpty()) {
            str45 = "有点疲劳-疲劳(4-6级)"
        }
        //运动步速版本
        if (sixMinRecordsBean.prescriptionBean[0].prescripState == "1") {
            if (sixMinRecordsBean.prescriptionBean[0].distanceState.isEmpty() || sixMinRecordsBean.prescriptionBean[0].distanceState == "1") {
                strideTitStrs = "运动步速";
                strideStrs =
                    sixMinRecordsBean.prescriptionBean[0].strideBefore + "-" + sixMinRecordsBean.prescriptionBean[0].strideAfter + "米/分钟";
                movDistanceTitStrs = "运动距离";
                movDistanceStrs =
                    sixMinRecordsBean.prescriptionBean[0].movementDistance + "-" + sixMinRecordsBean.prescriptionBean[0].movementDistanceAfter + "米";
            } else {
                movDistanceTitStrs = "/";
                movDistanceStrs = "/";
            }
            if (sixMinRecordsBean.prescriptionBean[0].heartrateState.isEmpty() || sixMinRecordsBean.prescriptionBean[0].heartrateState == "1") {
                heartrateRateTitStr = "运动心率";
                heartrateRateStr = sixMinRecordsBean.prescriptionBean[0].heartrateRate + "bpm";
            }
            if (sixMinRecordsBean.prescriptionBean[0].metabState.isEmpty() || sixMinRecordsBean.prescriptionBean[0].metabState == "1") {
                metabMetTitStr = "代谢当量";
                metabMetStr = sixMinRecordsBean.prescriptionBean[0].metabMet + "METs";
            }
            if (sixMinRecordsBean.prescriptionBean[0].pllevState == "2") {
                strTit45 = "/";
                str45 = "/";
            }
            root["strideTitStrs"] = strideTitStrs;
            root["strideStrs"] = strideStrs;
            root["movDisTitStrs"] = movDistanceTitStrs;
            root["movDisStrs"] = movDistanceStrs;
            root["movTime"] = sixMinRecordsBean.prescriptionBean[0].movementTime + "分钟"
            root["heartrateRateTitStr"] = heartrateRateTitStr;
            root["rateStr"] = heartrateRateStr;
            root["metabMetTitStr"] = metabMetTitStr;
            root["metabStr"] = metabMetStr;
            root["strTit45"] = strTit45;
            root["str45"] = str45;

            var movementStr: String =
                sixMinRecordsBean.prescriptionBean[0].movementWeeklyNumber + "次/周，" + sixMinRecordsBean.prescriptionBean[0].movementCycle
            var checkcf2 = ""
            when (sixMinRecordsBean.prescriptionBean[0].cycleUnit) {
                "0" -> {
                    checkcf2 = "周"
                }

                "1" -> {
                    checkcf2 = "月"
                }

                "2" -> {
                    checkcf2 = "年"
                }
            }
            movementStr += checkcf2;
            root["moveStr"] = movementStr;
            var remarke: String = sixMinRecordsBean.prescriptionBean[0].remarke
            if (remarke.isNotEmpty()) {
                remarke = remarke.replace("\n".toRegex(), "<w:br/>")
            }
            root["remarke"] = remarke
            var jyysStr: String = sixMinRecordsBean.prescriptionBean[0].remarkeName
            if (jyysStr.length > 9) {
                jyysStr = jyysStr.substring(0, 7)
                jyysStr += "..."
            }
            root["jyysStr"] = jyysStr
            val jysjStr: String = sixMinRecordsBean.infoBean.addTime
            root["jysjStr"] = jysjStr
        }
    }

    private fun dealPageTable(root: MutableMap<String, Any>) {
        root["xin0"] = sixMinRecordsBean.heartBeatBean[0].heartStop.ifEmpty { "0" }
        root["xin1"] = sixMinRecordsBean.heartBeatBean[0].heartOne.ifEmpty { "0" }
        root["xin2"] = sixMinRecordsBean.heartBeatBean[0].heartTwo.ifEmpty { "0" }
        root["xin3"] = sixMinRecordsBean.heartBeatBean[0].heartThree.ifEmpty { "0" }
        root["xin4"] = sixMinRecordsBean.heartBeatBean[0].heartFour.ifEmpty { "0" }
        root["xin5"] = sixMinRecordsBean.heartBeatBean[0].heartFive.ifEmpty { "0" }
        root["xin6"] = sixMinRecordsBean.heartBeatBean[0].heartSix.ifEmpty { "0" }
        root["xin7"] = sixMinRecordsBean.heartBeatBean[0].heartBig.ifEmpty { "0" }
        root["xin8"] = sixMinRecordsBean.heartBeatBean[0].heartSmall.ifEmpty { "0" }
        root["xin9"] = sixMinRecordsBean.heartBeatBean[0].heartAverage.ifEmpty { "0" }
        //血氧
        root["ya0"] = sixMinRecordsBean.bloodOxyBean[0].bloodStop
        root["ya1"] = sixMinRecordsBean.bloodOxyBean[0].bloodOne
        root["ya2"] = sixMinRecordsBean.bloodOxyBean[0].bloodTwo
        root["ya3"] = sixMinRecordsBean.bloodOxyBean[0].bloodThree
        root["ya4"] = sixMinRecordsBean.bloodOxyBean[0].bloodFour
        root["ya5"] = sixMinRecordsBean.bloodOxyBean[0].bloodFive
        root["ya6"] = sixMinRecordsBean.bloodOxyBean[0].bloodSix
        root["ya7"] = sixMinRecordsBean.bloodOxyBean[0].bloodBig
        root["ya8"] = sixMinRecordsBean.bloodOxyBean[0].bloodSmall
        root["ya9"] = sixMinRecordsBean.bloodOxyBean[0].bloodAverage
        //呼吸率/步数
        if (sixMinRecordsBean.infoBean.bsHxl == "1") {
            root["hxOrBs"] = "呼吸率"
            root["hB0"] = sixMinRecordsBean.breathingBean[0].breathingStop
            root["hB1"] = sixMinRecordsBean.breathingBean[0].breathingOne
            root["hB2"] = sixMinRecordsBean.breathingBean[0].breathingTwo
            root["hB3"] = sixMinRecordsBean.breathingBean[0].breathingThree
            root["hB4"] = sixMinRecordsBean.breathingBean[0].breathingFour
            root["hB5"] = sixMinRecordsBean.breathingBean[0].breathingFive
            root["hB6"] = sixMinRecordsBean.breathingBean[0].breathingSix
            root["hB7"] = sixMinRecordsBean.breathingBean[0].breathingBig
            root["hB8"] = sixMinRecordsBean.breathingBean[0].breathingSmall
            root["hB9"] = sixMinRecordsBean.breathingBean[0].breathingAverage
        } else if (sixMinRecordsBean.infoBean.bsHxl == "0" || sixMinRecordsBean.infoBean.bsHxl.isEmpty()) {
            root["hxOrBs"] = "步数"
            root["hB0"] = sixMinRecordsBean.walkBean[0].walkStop
            root["hB1"] = sixMinRecordsBean.walkBean[0].walkOne
            root["hB2"] = sixMinRecordsBean.walkBean[0].walkTwo
            root["hB3"] = sixMinRecordsBean.walkBean[0].walkThree
            root["hB4"] = sixMinRecordsBean.walkBean[0].walkFour
            root["hB5"] = sixMinRecordsBean.walkBean[0].walkFive
            root["hB6"] = sixMinRecordsBean.walkBean[0].walkSix
            root["hB7"] = sixMinRecordsBean.walkBean[0].walkBig
            root["hB8"] = sixMinRecordsBean.walkBean[0].waklSmall
            root["hB9"] = sixMinRecordsBean.walkBean[0].walkAverage
        }
        var startPressure = "/"
        if (sixMinRecordsBean.otherBean[0].startHighPressure != "0") {
            startPressure =
                sixMinRecordsBean.otherBean[0].startHighPressure + "/" + sixMinRecordsBean.otherBean[0].startLowPressure
        }
        root["xy1"] = startPressure
        var stopPressure = "/"
        if (sixMinRecordsBean.otherBean[0].stopHighPressure != "0") {
            stopPressure =
                sixMinRecordsBean.otherBean[0].stopHighPressure + "/" + sixMinRecordsBean.otherBean[0].stopLowPressure
        }
        root["xy2"] = stopPressure
    }

    private fun dealPageTow(
        root: MutableMap<String, Any>, bloodPng: File, heartPng: File, hsHxlPng: File
    ) {
//        val base64Blood: String = CommonUtil.imageTobase64(bloodPng.absolutePath)
        root["imageBlood"] = PictureRenderData(750, 200, bloodPng.absolutePath)

        //心率
//        val base64Hreat: String = CommonUtil.imageTobase64(heartPng.absolutePath)
        root["imageHeart"] = PictureRenderData(750, 200, heartPng.absolutePath)
        var qushiStr = "呼吸率趋势"
        if (sixMinRecordsBean.infoBean.bsHxl == "0") {
            qushiStr = "步数趋势"
        }
        root["qushi"] = qushiStr
//        val base64WalkAndHxl: String = CommonUtil.imageTobase64(hsHxlPng.absolutePath)
        root["imageWalkAndHxl"] = PictureRenderData(750, 200, hsHxlPng.absolutePath)
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
                        val bean = datas[num] as SixMinReportInfoAndEvaluation
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
                val lastDistance = if (getLastDistance() != "/") "${getLastDistance()}米" else "/"
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

    @SuppressLint("AutoDispose")
    private fun onPrintPdf(path: String) {
        val printManager = mActivity.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val builder = PrintAttributes.Builder()
        builder.setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME)
        val mPdfDocumentAdapter = PdfDocumentAdapter(mActivity, path)
        val printReport = printManager.print("PrintReport", mPdfDocumentAdapter, builder.build())
        mPdfDocumentAdapter.setOnPrintStatue {
            if (printReport.isCancelled) {
                mActivity.runOnUiThread {
                    Log.e("打印报告回调======", "====打印被取消了")
                    mActivity.showMsg("打印被取消了")
                }
            }
            if (printReport.isBlocked) {
                mActivity.runOnUiThread {
                    Log.e("打印报告回调======", "====打印机器被锁定了(卡了或者没有纸张了)")
                    mActivity.showMsg("打印机器被锁定了(卡了或者没有纸张了)")
                }
            }
            binding.sixminReportLlPrintReport.postDelayed(Runnable {
                if (printReport.isStarted) {
                    mActivity.showMsg("开始打印")
                    mDisposable3s = Observable
                        .interval(3, TimeUnit.SECONDS) //定时器操作符，这里三秒打印一个log
                        //取消任务时取消定时唤醒
                        .doOnDispose {}
                        .subscribe { count ->
                            val completed = printReport.isCompleted //完成的回调
                            val failed = printReport.isFailed //失败的回调
                            if (completed) {
                                mActivity.runOnUiThread {
                                    Log.e("打印报告回调======", "====打印完成")
                                    mActivity.showMsg("打印完成")
                                }
                                if (null != mDisposable3s) {
                                    mDisposable3s?.dispose()
                                    mDisposable3s = null
                                }
                            }
                            if (failed) {
                                mActivity.runOnUiThread {
                                    Log.e("打印报告回调======", "====打印失败")
                                    mActivity.showMsg("打印失败")
                                }
                                if (null != mDisposable3s) {
                                    mDisposable3s?.dispose()
                                    mDisposable3s = null
                                }
                            }
                            if (!completed && count.toInt() == 40) {  //2分钟还未打印成功,默认打印失败
                                mActivity.runOnUiThread {
                                    Log.e("打印报告回调======", "====打印失败")
                                    mActivity.showMsg("打印失败")
                                }
                                if (null != mDisposable3s) {
                                    mDisposable3s?.dispose()
                                    mDisposable3s = null
                                }
                            }
                        }
                }
            }, 1000)
        }
    }
}
