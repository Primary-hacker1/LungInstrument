package com.just.machine.ui.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aspose.words.Document
import com.aspose.words.ImportFormatMode
import com.aspose.words.SaveFormat
import com.common.base.CommonBaseActivity
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.deepoove.poi.XWPFTemplate
import com.deepoove.poi.data.PictureRenderData
import com.google.gson.Gson
import com.just.machine.dao.PatientBean
import com.just.machine.model.Constants
import com.just.machine.model.PatientInfoBean
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.model.SixMinReportInfoAndEvaluation
import com.just.machine.model.sixminreport.SixMinReportInfo
import com.just.machine.model.systemsetting.SixMinSysSettingBean
import com.just.machine.ui.adapter.CardiopulAdapter
import com.just.machine.ui.adapter.PatientsAdapter
import com.just.machine.ui.adapter.SixMinAdapter
import com.just.machine.ui.dialog.CommonDialogFragment
import com.just.machine.ui.dialog.LoadingDialogFragment
import com.just.machine.ui.dialog.PatientDialogFragment
import com.just.machine.ui.dialog.SelectActionDialogFragment
import com.just.machine.ui.dialog.SixMinPermissionDialogFragment
import com.just.machine.ui.dialog.SixMinReportSelfCheckBeforeTestFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.ActivityPatientBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigDecimal


/**
 *create by 2024/2/27
 * 患者信息
 *@author zt
 */
@AndroidEntryPoint
class PatientActivity : CommonBaseActivity<ActivityPatientBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var usbTransferUtil: USBTransferUtil
    private var hasPassPermission = false //删除6分钟试验记录授权
    private lateinit var sysSettingBean: SixMinSysSettingBean//6分钟系统设置

    companion object {
        /**
         * @param context context
         */
        fun startPatientActivity(context: Context?, jumpFlag: String?) {
            val intent = Intent(context, PatientActivity::class.java)
            intent.putExtra(Constants.finishSixMinTest, jumpFlag)
            context?.startActivity(intent)
        }
    }

    private var sixMinAdapter: SixMinAdapter = SixMinAdapter(this)

    private var cardiopulmonaryAdapter: CardiopulAdapter = CardiopulAdapter(this)

    private var bean: PatientBean? = null

    val beans: MutableList<PatientBean> = ArrayList()
    private val patientInfoBeans: MutableList<PatientInfoBean> = ArrayList()

    private val adapter: PatientsAdapter = PatientsAdapter(this)

    private var jumpFlag: String? = null
    private lateinit var startLoadingDialogFragment: LoadingDialogFragment
    private var reportEvaluationList = mutableListOf<SixMinReportInfoAndEvaluation>()


    private fun initToolbar() {
        binding.toolbar.title = Constants.patientInformation//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.visible()
        binding.toolbar.ivTitleBack.setNoRepeatListener {
            finish()
        }
    }

    override fun initView() {
        initToolbar()
        usbTransferUtil = USBTransferUtil.getInstance()

        viewModel.getPatients()//查询数据库

        binding.rvList.layoutManager = LinearLayoutManager(this)

        binding.rvSixTest.layoutManager = LinearLayoutManager(this)

        binding.rvCardiopulmonaryTest.layoutManager = LinearLayoutManager(this)

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.SUCCESS -> {
                    if (it.any is Int) LogUtils.e(tag + it.any)
                }

                LiveDataEvent.QueryPatient, LiveDataEvent.QueryPatientNull -> {//查询患者
                    it.any?.let { it1 -> queryPatient(it1) }
                }

                LiveDataEvent.QueryNameId, LiveDataEvent.QuerySuccess -> {
                    it.any?.let { it1 -> beanQuery(it1) }
                }

                LiveDataEvent.QuerySixMinReportEvaluationSuccess -> {
                    it.any?.let { it1 -> beanQueryEvaluation(it1) }
                }

                LiveDataEvent.QuerySixMinReportInfoSuccess -> {
                    it.any?.let { it1 -> querySixMinReportInfo(it1) }
                }
            }
        }

        initOnClick()
        initSystemInfo()
        jumpFlag = intent.getStringExtra(Constants.finishSixMinTest)
        if (jumpFlag != null && jumpFlag == "finishSixMinTest") {
            setButtonStyle(
                binding.btnCardiopulmonary,
                binding.btnSixMin,
                binding.rvCardiopulmonaryTest,
                binding.rvSixTest
            )
            binding.llSixMin.visible()
            binding.llCardiopulmonary.gone()
        }
    }

    private fun queryPatient(any: Any) {

        if (any is PatientBean) {
            this.bean = any
        } else {
            bean = PatientBean()
        }

        LogUtils.d(tag + bean.toString())

        if (patientInfoBeans.isNotEmpty()) {
            patientInfoBeans.forEach { it ->
                if (bean?.patientId == it.infoBean.patientId) {
                    it.sixMinReportInfo.sortByDescending { it.addTime }
                    val filter = it.sixMinReportInfo.filter { it.delFlag == "0" }
                    sixMinAdapter.setItemsBean(filter as MutableList<SixMinReportInfo>)
                }
            }
        }

//        sixMinAdapter = SixMinAdapter(this)

//        bean?.sixMinRecordsBean?.let { it1 -> sixMinAdapter.setItemsBean(it1) }

//        binding.rvSixTest.adapter = sixMinAdapter


        cardiopulmonaryAdapter = CardiopulAdapter(this)

        bean?.testRecordsBean?.let { it1 -> cardiopulmonaryAdapter.setItemsBean(it1) }

        binding.rvCardiopulmonaryTest.adapter = cardiopulmonaryAdapter

    }

    private fun beanQuery(any: Any) {
        if (any is List<*>) {
            beans.clear()
            patientInfoBeans.clear()

            val datas = any as MutableList<*>

            for (num in 0 until datas.size) {
                val bean = datas[num] as PatientInfoBean
                beans.add(bean.infoBean)
                patientInfoBeans.add(bean)
            }

            adapter.setItemsBean(beans)

            LogUtils.d(tag + beans.toString())

            binding.rvList.adapter = adapter

            if (patientInfoBeans.isNotEmpty()) {

                binding.rvSixTest.layoutManager = LinearLayoutManager(this)

                sixMinAdapter = SixMinAdapter(this)

                patientInfoBeans[0].sixMinReportInfo.sortByDescending { it.addTime }

                val filter = patientInfoBeans[0].sixMinReportInfo.filter { it.delFlag == "0" }

                filter.let { it1 -> sixMinAdapter.setItemsBean(it1 as MutableList<SixMinReportInfo>) }

                binding.rvSixTest.adapter = sixMinAdapter

                sixMinAdapter.setItemClickListener { item, position ->
                    sixMinAdapter.toggleItemBackground(position)
                    val startSelectActionDialogFragment =
                        SelectActionDialogFragment.startSelectActionDialogFragment(
                            supportFragmentManager,
                            "report"
                        )
                    startSelectActionDialogFragment.setSelectReportActionDialogListener(object :
                        SelectActionDialogFragment.SelectReportActionDialogListener {
                        override fun onClickView() {
                            val intent = Intent(
                                this@PatientActivity, SixMinDetectActivity::class.java
                            )
                            val bundle = Bundle()
                            bundle.putString(
                                Constants.sixMinPatientInfo,
                                item.patientId.toString()
                            )
                            bundle.putString(Constants.sixMinReportNo, item.reportNo)
                            bundle.putString(Constants.sixMinReportType, "3")
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }

                        override fun onClickEdit() {
                            val intent = Intent(
                                this@PatientActivity, SixMinDetectActivity::class.java
                            )
                            val bundle = Bundle()
                            bundle.putString(
                                Constants.sixMinPatientInfo,
                                item.patientId.toString()
                            )
                            bundle.putString(Constants.sixMinReportNo, item.reportNo)
                            bundle.putString(Constants.sixMinReportType, "2")
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }

                        override fun onClickExport() {

                        }

                        override fun onClickDelete() {
                            val gson = Gson()
                            var sysSettingBean = SixMinSysSettingBean()
                            val sixMinSysSetting = SharedPreferencesUtils.instance.sixMinSysSetting
                            if (sixMinSysSetting != null && sixMinSysSetting != "") {
                                sysSettingBean = gson.fromJson(
                                    sixMinSysSetting, SixMinSysSettingBean::class.java
                                )
                            }
                            if (!hasPassPermission) {
                                val startPermissionDialogFragment =
                                    SixMinPermissionDialogFragment.startPermissionDialogFragment(supportFragmentManager)
                                startPermissionDialogFragment.setOnConfirmClickListener(object :
                                    SixMinPermissionDialogFragment.SixMinPermissionDialogListener {
                                    override fun onClickConfirm(pwd: String) {
                                        if (pwd.isEmpty()) {
                                            showMsg("权限密码不能为空")
                                            return
                                        }
                                        if (sysSettingBean.sysPwd.exportPwd == pwd) {
                                            startPermissionDialogFragment.dismiss()
                                            hasPassPermission = true
                                        } else {
                                            showMsg("权限密码错误，请重新输入")
                                            return
                                        }
                                    }
                                })
                                return
                            }

                            val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                                supportFragmentManager, "确认删除该试验记录吗?"
                            )
                            startCommonDialogFragment.setCommonDialogOnClickListener(object :
                                CommonDialogFragment.CommonDialogClickListener {
                                override fun onPositiveClick() {
                                    hasPassPermission = false
                                    viewModel.deleteSixMinReportInfo(item.reportNo)
                                    viewModel.getPatients()//查询数据库
                                }

                                override fun onNegativeClick() {
                                    hasPassPermission = false
                                }

                                override fun onStopNegativeClick(stopReason: String) {

                                }
                            })
                        }
                    })
                }

                sixMinAdapter.setItemOnClickListener(object : SixMinAdapter.SixMinReportListener {
                    override fun onExportItem(bean: SixMinReportInfo) {
                        // 导出6分钟报告
                        lifecycleScope.launch {
                            viewModel.getSixMinReportEvaluationById(bean.patientId.toString())
                            kotlinx.coroutines.delay(100L)
                            viewModel.getSixMinReportInfoById(
                                bean.patientId, bean.reportNo
                            )
                        }
                    }

                    override fun onUpdateItem(bean: SixMinReportInfo) {
                        //心电回放
                        val intent = Intent(
                            this@PatientActivity, SixMinDetectActivity::class.java
                        )
                        val bundle = Bundle()
                        bundle.putString(Constants.sixMinReportType, "4")
                        bundle.putString(Constants.sixMinReportNo, bean.reportNo)
                        bundle.putString(Constants.sixMinPatientInfo, bean.patientId.toString())
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    override fun onCheckItem(bean: SixMinReportInfo) {
                        //操作记录类型

                    }
                })
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
                        val bean = datas[num] as SixMinReportInfoAndEvaluation
                        reportEvaluationList.add(bean)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun querySixMinReportInfo(any: Any) {
        if (any is List<*>) {
            val datas = any as MutableList<*>
            val sixMinRecordsBean = datas[0] as SixMinRecordsBean
            exportReport(sixMinRecordsBean)
        }
    }

    /**
     * 导出报告
     */
    private fun exportReport(sixMinRecordsBean: SixMinRecordsBean) {
        val pngSavePath =
            File.separator + "sixmin/sixminreportpng" + File.separator + sixMinRecordsBean.infoBean.reportNo

        startLoadingDialogFragment =
            LoadingDialogFragment.startLoadingDialogFragment(
                supportFragmentManager,
                "导出报告中..."
            )
        val templateName = "templates/报告模板-无截图.docx"

        val bloodPng = File(
            getExternalFilesDir("")?.absolutePath,
            pngSavePath + File.separator + "imageBlood.png"
        )
        val heartPng = File(
            getExternalFilesDir("")?.absolutePath,
            pngSavePath + File.separator + "imageHeart.png"
        )
        val hsHxlPng =
            if (sixMinRecordsBean.infoBean.bsHxl.isEmpty() || sixMinRecordsBean.infoBean.bsHxl == "0") {
                File(
                    getExternalFilesDir("")?.absolutePath,
                    pngSavePath + File.separator + "imageSteps.png"
                )
            } else {
                File(
                    getExternalFilesDir("")?.absolutePath,
                    pngSavePath + File.separator + "imageBreathing.png"
                )

            }

        val filePath = File(
            getExternalFilesDir("")?.absolutePath,
            File.separator + "sixmin/sixminreport" + File.separator + sixMinRecordsBean.infoBean.reportNo
                    + File.separator + "六分钟步行试验检测报告.doc"
        )

        val pdfFilePath = File(
            getExternalFilesDir("")?.absolutePath,
            File.separator + "sixmin/sixminreport" + File.separator + sixMinRecordsBean.infoBean.reportNo
                    + File.separator + "六分钟步行试验检测报告.pdf"
        )

        if (filePath.parentFile?.exists() == false) {
            filePath.parentFile?.mkdirs()
        }

        val root = mutableMapOf<String, Any>()
        dealPageOne(root,sixMinRecordsBean)
        dealPageTow(root, bloodPng, heartPng, hsHxlPng,sixMinRecordsBean)

        lifecycleScope.launch(Dispatchers.IO) {
            val generateWord =
                generateWord(
                    root,
                    templateName,
                    filePath.absolutePath
                )
            if (!generateWord) {
                withContext(Dispatchers.Main) {
                    showMsg("生成word文档失败")
                }
            } else {
                // 加载Word文档
                val doc = Document(filePath.absolutePath)
                val document = Document()
                document.removeAllChildren()
                document.appendDocument(doc, ImportFormatMode.USE_DESTINATION_STYLES)
                val format = doc.styles.defaultParagraphFormat
                format.clearFormatting()
                // 保存文档为PDF格式
                document.save(pdfFilePath.absolutePath, SaveFormat.PDF)
                withContext(Dispatchers.Main) {
                    showMsg("导出报告成功")
                    if(startLoadingDialogFragment.isVisible){
                        startLoadingDialogFragment.dismiss()
                    }
                }
            }
        }
    }

    private fun dealPageOne(root: MutableMap<String, Any>,sixMinRecordsBean: SixMinRecordsBean) {
        var patientName = sixMinRecordsBean.infoBean.patientName
        if (patientName.length > 9) {
            patientName = patientName.substring(0, 7)
            patientName += "..."
        }
        root["patientName"] = patientName
        root["xingbieStr"] = sixMinRecordsBean.infoBean.patientSix
        root["patientAge"] = sixMinRecordsBean.infoBean.patientAge
        root["patientHeigh"] = sixMinRecordsBean.infoBean.patientHeight
        root["patientWeight"] = sixMinRecordsBean.infoBean.patientWeight
        root["patientBmi"] = sixMinRecordsBean.infoBean.patientBmi
        root["medicalNo"] = sixMinRecordsBean.infoBean.medicalNo
        root["pDistance"] = sixMinRecordsBean.infoBean.predictionDistance
        root["medicalHistory"] = sixMinRecordsBean.infoBean.medicalHistory
        root["clinicalDiagnosis"] = sixMinRecordsBean.infoBean.clinicalDiagnosis
        root["medicineUse"] = sixMinRecordsBean.infoBean.medicineUse
        var bianhaoStr: String = sixMinRecordsBean.infoBean.reportNo
        if (bianhaoStr.length >= 12) {
            val newBianhao = bianhaoStr.substring(6, 12)
            bianhaoStr = bianhaoStr.replace(newBianhao.toRegex(), "******")
        }
        root["reportNo"] = bianhaoStr
        root["useName"] = sixMinRecordsBean.otherBean[0].useName
        dealPageTable(root,sixMinRecordsBean)
        //综合评估
        var befoFatigueLevel: String = sixMinRecordsBean.evaluationBean[0].befoFatigueLevel
        if (befoFatigueLevel.isNotEmpty()) {
            befoFatigueLevel += "级"
        }
        root["fatigueLevel"] =
            befoFatigueLevel + "/" + sixMinRecordsBean.evaluationBean[0].fatigueLevel + "级"
        root["disBefore"] = if (getLastDistance(sixMinRecordsBean) != "/") "${getLastDistance(sixMinRecordsBean)}米" else "/"
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
        if (sixMinRecordsBean.prescriptionBean[0].prescripState == "1" || sixMinRecordsBean.prescriptionBean[0].prescripState.isEmpty()) {
            strideAverageStr = sixMinRecordsBean.strideBean[0].strideAverage + "米/分"
            heartRestoreStr = sixMinRecordsBean.heartBeatBean[0].heartRestore.ifEmpty { "0" }
        }
        root["striAvg"] = strideAverageStr
        root["metabEqu"] = sixMinRecordsBean.evaluationBean[0].metabEquivalent + "METs"
        root["accounted"] = sixMinRecordsBean.evaluationBean[0].accounted + "%"
        root["stHeart"] = if(sixMinRecordsBean.heartBeatBean[0].heartStop.isEmpty()) "0bmp" else "${sixMinRecordsBean.heartBeatBean[0].heartStop}bmp"

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
                usbTransferUtil.dealCardiopuDegree(BigDecimal(sixMinRecordsBean.evaluationBean[0].totalDistance))
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
        if (sysSettingBean.sysOther.showResetTime == "1") {
            check4 += "中途停留了" + sixMinRecordsBean.infoBean.restDuration + "秒。"
        }
        root["badSymptoms"] = check4
        if (sixMinRecordsBean.heartBeatBean[0].heartConclusion.isNotEmpty()) {
            root["heartConclusionStr"] =
                "心电结论：" + sixMinRecordsBean.heartBeatBean[0].heartConclusion + "。"
        }

        //运动处方建议
        val checkPng = File(
            getExternalFilesDir("")?.absolutePath,
            "templates" + File.separator + "check.png"
        )
        val unCheckPng = File(
            getExternalFilesDir("")?.absolutePath,
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
        if (sixMinRecordsBean.prescriptionBean[0].prescripState == "1" || sixMinRecordsBean.prescriptionBean[0].prescripState.isEmpty()) {
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

    private fun dealPageTable(root: MutableMap<String, Any>,sixMinRecordsBean: SixMinRecordsBean) {
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
        root: MutableMap<String, Any>, bloodPng: File, heartPng: File, hsHxlPng: File,sixMinRecordsBean: SixMinRecordsBean
    ) {
        root["imageBlood"] = PictureRenderData(750, 200, bloodPng.absolutePath)

        //心率
        root["imageHeart"] = PictureRenderData(750, 200, heartPng.absolutePath)
        var qushiStr = "呼吸率趋势"
        if (sixMinRecordsBean.infoBean.bsHxl == "0") {
            qushiStr = "步数趋势"
        }
        root["qushi"] = qushiStr
        root["imageWalkAndHxl"] = PictureRenderData(750, 200, hsHxlPng.absolutePath)
    }

    private fun getLastDistance(sixMinRecordsBean: SixMinRecordsBean): String {
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

    /**
     * 生成word文档
     */
    private fun generateWord(
        params: MutableMap<String, Any>,
        templateName: String,
        savePath: String
    ): Boolean {
        val open = assets.open(templateName)
        val template = XWPFTemplate.compile(open)
        params.let {
            template.render(params)
        }
        var outputStream: FileOutputStream? = null

        try {
            outputStream = FileOutputStream(savePath)
            if (outputStream == null) {
                return false
            }
            template.write(outputStream)
            outputStream.flush()
            return true
        } catch (e: IOException) {
            return false
        } finally {
            try {
                template.close()
                outputStream?.close()
            } finally {
            }
        }
    }

    private fun initOnClick() {

        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (beans.size > 0) {
                    beans.clear()
                }

                val select = binding.editSearch.text.toString().trim()

                if (select.isEmpty()) {
                    viewModel.getPatients()//查询数据库
                    return
                }
                viewModel.getNameOrId(select)//模糊查询数据库
            }

            override fun afterTextChanged(s: Editable) {}
        })

        adapter.setItemOnClickListener(object : PatientsAdapter.PatientListener {
            override fun onDeleteItem(bean: PatientBean) {
                val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                    supportFragmentManager, "确认删除该患者吗?"
                )
                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                    CommonDialogFragment.CommonDialogClickListener {
                    override fun onPositiveClick() {
                        viewModel.deletePatient(bean.patientId)
                        viewModel.deleteSixMinReportInfoById(bean.patientId.toString())
                    }

                    override fun onNegativeClick() {

                    }

                    override fun onStopNegativeClick(stopReason: String) {

                    }
                })
            }

            override fun onUpdateItem(bean: PatientBean) {
                PatientDialogFragment.startPatientDialogFragment(
                    supportFragmentManager, bean
                )//修改患者信息
            }

            override fun onActionItem(bean: PatientBean) {
                val startSelectActionDialogFragment =
                    SelectActionDialogFragment.startSelectActionDialogFragment(
                        supportFragmentManager
                    )
                startSelectActionDialogFragment.setSelectActionDialogListener(object :
                    SelectActionDialogFragment.SelectActionDialogListener {
                    override fun onClickConfirm(actionType: Int) {
                        //0 心肺测试  1 6分钟测试
                        if (actionType == 0) {

                        } else {
                            if (usbTransferUtil.isConnectUSB && usbTransferUtil.bloodOxygenConnection && usbTransferUtil.ecgConnection && usbTransferUtil.bloodPressureConnection) {
                                val selfCheckBeforeTestDialogFragment =
                                    SixMinReportSelfCheckBeforeTestFragment.startPatientSelfCheckDialogFragment(
                                        supportFragmentManager, "1", "1"
                                    )
                                selfCheckBeforeTestDialogFragment.setSelfCheckBeforeTestDialogOnClickListener(
                                    object :
                                        SixMinReportSelfCheckBeforeTestFragment.SixMinReportSelfCheckBeforeTestDialogListener {
                                        override fun onClickConfirm(
                                            befoFatigueLevel: Int,
                                            befoBreathingLevel: Int,
                                            befoFatigueLevelStr: String,
                                            befoBreathingLevelStr: String,
                                            faceMaskStr:String
                                        ) {
                                            val intent = Intent(
                                                this@PatientActivity,
                                                SixMinDetectActivity::class.java
                                            )
                                            val bundle = Bundle()
                                            bundle.putString(
                                                Constants.sixMinSelfCheckViewSelection,
                                                "$befoFatigueLevelStr&$befoBreathingLevelStr"
                                            )
                                            bundle.putString(
                                                Constants.sixMinPatientInfo,
                                                bean.patientId.toString()
                                            )
                                            intent.putExtras(bundle)
                                            startActivity(intent)
                                        }

                                        override fun onClickClose() {

                                        }
                                    })
                            } else {
                                val startCommonDialogFragment =
                                    CommonDialogFragment.startCommonDialogFragment(
                                        supportFragmentManager,
                                        getString(R.string.sixmin_test_enter_test_without_device_connection)
                                    )
                                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                                    CommonDialogFragment.CommonDialogClickListener {
                                    override fun onPositiveClick() {
                                        val selfCheckBeforeTestDialogFragment =
                                            SixMinReportSelfCheckBeforeTestFragment.startPatientSelfCheckDialogFragment(
                                                supportFragmentManager, "1", "1"
                                            )
                                        selfCheckBeforeTestDialogFragment.setSelfCheckBeforeTestDialogOnClickListener(
                                            object :
                                                SixMinReportSelfCheckBeforeTestFragment.SixMinReportSelfCheckBeforeTestDialogListener {
                                                override fun onClickConfirm(
                                                    befoFatigueLevel: Int,
                                                    befoBreathingLevel: Int,
                                                    befoFatigueLevelStr: String,
                                                    befoBreathingLevelStr: String,
                                                    faceMaskStr:String
                                                ) {
                                                    val intent = Intent(
                                                        this@PatientActivity,
                                                        SixMinDetectActivity::class.java
                                                    )
                                                    val bundle = Bundle()
                                                    bundle.putString(
                                                        Constants.sixMinSelfCheckViewSelection,
                                                        "$befoFatigueLevelStr&$befoBreathingLevelStr"
                                                    )
                                                    bundle.putString(
                                                        Constants.sixMinPatientInfo,
                                                        bean.patientId.toString()
                                                    )
                                                    intent.putExtras(bundle)
                                                    startActivity(intent, bundle)
                                                }

                                                override fun onClickClose() {

                                                }
                                            })
                                    }

                                    override fun onNegativeClick() {

                                    }

                                    override fun onStopNegativeClick(stopReason: String) {

                                    }
                                })
                            }
                        }
                    }
                })
            }
        })

        adapter.setItemClickListener { item, position ->
            viewModel.getPatient(item.patientId)//查询数据库
            adapter.toggleItemBackground(position)
        }

        cardiopulmonaryAdapter.setItemClickListener { item, position ->
            cardiopulmonaryAdapter.toggleItemBackground(position)
            LogUtils.d(tag + item.toString())
        }

        binding.btnAdd.setNoRepeatListener {
            val patientDialogFragment =
                PatientDialogFragment.startPatientDialogFragment(supportFragmentManager)//添加患者修改患者信息
            patientDialogFragment.setDialogOnClickListener(object :
                PatientDialogFragment.PatientDialogListener {
                override fun onClickConfirmBtn(patientId: String) {//确认

                }

                override fun onClickCleanBtn() {//取消

                }
            })
        }

        binding.btnSixMin.setNoRepeatListener {
            setButtonStyle(
                binding.btnCardiopulmonary,
                binding.btnSixMin,
                binding.rvCardiopulmonaryTest,
                binding.rvSixTest
            )
            binding.llSixMin.visible()
            binding.llCardiopulmonary.gone()
        }

        binding.btnCardiopulmonary.setNoRepeatListener {
            setButtonStyle(
                binding.btnSixMin,
                binding.btnCardiopulmonary,
                binding.rvSixTest,
                binding.rvCardiopulmonaryTest
            )
            binding.llSixMin.gone()
            binding.llCardiopulmonary.visible()
        }
    }

    private fun setButtonStyle(
        textView1: TextView,
        textView2: TextView,
        showRecyclerView: RecyclerView,
        hideRecyclerView: RecyclerView
    ) {// 设置按钮的样式

        textView1.setTextColor(ContextCompat.getColor(this, R.color.cD9D9D9))
        textView2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        textView1.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        textView2.background = ContextCompat.getDrawable(this, R.drawable.super_edittext_bg)
        showRecyclerView.gone()
        hideRecyclerView.visible()
    }

    fun initSystemInfo() {
        val gson = Gson()
        sysSettingBean = SixMinSysSettingBean()
        val sixMinSysSetting = SharedPreferencesUtils.instance.sixMinSysSetting
        if (sixMinSysSetting != null && sixMinSysSetting != "") {
            sysSettingBean = gson.fromJson(
                sixMinSysSetting, SixMinSysSettingBean::class.java
            )
        }
    }

    override fun getViewBinding() = ActivityPatientBinding.inflate(layoutInflater)
}