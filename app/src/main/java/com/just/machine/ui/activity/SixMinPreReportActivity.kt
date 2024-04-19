package com.just.machine.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseActivity
import com.common.base.setNoRepeatListener
import com.just.machine.model.SixMinReportEditBloodPressure
import com.just.machine.model.SixMinReportItemBean
import com.just.machine.model.SixMinReportPatientSelfBean
import com.just.machine.model.SixMinReportPatientSelfItemBean
import com.just.machine.model.sixminreport.SixMinReportWalk
import com.just.machine.ui.adapter.SixMinReportPatientSelfAdapter
import com.just.machine.ui.dialog.SixMinReportEditBloodPressureFragment
import com.just.machine.ui.dialog.SixMinReportPrescriptionFragment
import com.just.machine.ui.dialog.SixMinReportSelfCheckBeforeTestFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.ActivitySixMinPreReportBinding
import com.justsafe.libview.util.SystemUtil
import dagger.hilt.android.AndroidEntryPoint

/**
 * 6分钟预生成报告
 */
@AndroidEntryPoint
class SixMinPreReportActivity: CommonBaseActivity<ActivitySixMinPreReportBinding>()   {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var generateReportDialog: AlertDialog
    private lateinit var exitPreReportDialog: AlertDialog
    private var reportRowList = mutableListOf<SixMinReportItemBean>()

    override fun getViewBinding() = ActivitySixMinPreReportBinding.inflate(layoutInflater)

    override fun initView() {
        val patientSelfList = mutableListOf<SixMinReportPatientSelfBean>()
        val patientBreathSelfItemList = mutableListOf<SixMinReportPatientSelfItemBean>()
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
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("0级", "没有"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("0.5级", "非常轻松"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("1级", "轻松"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("2级", "很轻"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("3级", "中度"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("4级", "有点疲劳"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("5-6级", "疲劳"))
        patientTiredSelfItemList.add(SixMinReportPatientSelfItemBean("7-9级", "非常疲劳"))
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "10级",
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
        binding.sixminRbSportTypeWalk.isChecked = true
        binding.sixminTvFinishCircle.text = Html.fromHtml(String.format(getString(R.string.sixmin_test_report_finish_circles),20))
        binding.sixminTvUnfinishCircle.text = Html.fromHtml(String.format(getString(R.string.sixmin_test_report_unfinish_circles),60))
        binding.sixminTvTotalDistance.text = Html.fromHtml(String.format(getString(R.string.sixmin_test_report_total_distance),100))
        binding.sixminTvDataStatistics.text = String.format(getString(R.string.sixmin_test_report_data_statistics),10,20,1.4,"重度","一级",144)
        binding.sixminTvStride.text = Html.fromHtml(String.format(getString(R.string.sixmin_test_report_stride),20))
        binding.sixminTvSingleSportTimeAndDistance.text = Html.fromHtml(String.format(getString(R.string.sixmin_test_report_single_sport_time_and_distance),6,30))
        binding.sixminTvSportHeartbeatAndMetabAndTiredControl.text = Html.fromHtml(String.format(getString(R.string.sixmin_test_report_heartbeat_and_metab_and_tired_control),108,1.5,2))
        binding.sixminTvRecommendTimeWeekly.text = Html.fromHtml(String.format(getString(R.string.sixmin_test_report_recommend_time_weekly),3))
        binding.sixminRbPrescriptionCycleWeek.isChecked = true
        binding.sixminTvPrescriptionConclusion.text = "本次未能完成六分钟试验，运动了0分11秒，停止原因：心脏周围的组织和体液都能导电，因此可将人体看成为一个具有长、宽、厚三度空间的容积导体。"
        binding.sixminTvReportNote.text = "本次未能完成六分钟试验，运动了0分11秒，停止原因:心脏周围的组织和体液都能导电，因此可将人体看成为一个具有长、宽、厚三度空间的容积导体。"
        initTable()
        initClickListener()
        initGenerateReportDialog()
        initExitPreReportDialog()
    }

    private fun initClickListener() {
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
                        if (reportRowList.isNotEmpty()) {
                            val sixMinReportItem = reportRowList[reportRowList.size - 1]
                            sixMinReportItem.stillnessValue =
                                "${bean.highBloodPressureBefore}/${bean.lowBloodPressureBefore}"
                            sixMinReportItem.sixMinValue =
                                "${bean.highBloodPressureAfter}/${bean.lowBloodPressureAfter}"
                            binding.sixminReportTlPreTable.removeAllViews()
                            initTable()
                        }
                    }
                })
            }
        }
        binding.sixminReportTvSelfCheckBeforeTest.setNoRepeatListener {
            val selfCheckBeforeTestDialogFragment =
                SixMinReportSelfCheckBeforeTestFragment.startPatientSelfCheckDialogFragment(
                    supportFragmentManager,
                    "0"
                )
            selfCheckBeforeTestDialogFragment.setSelfCheckBeforeTestDialogOnClickListener(object:
                SixMinReportSelfCheckBeforeTestFragment.SixMinReportSelfCheckBeforeTestDialogListener{
                override fun onClickConfirm(
                    befoFatigueLevel: Int,
                    befoBreathingLevel: Int,
                    befoFatigueLevelStr: String,
                    befoBreathingLevelStr: String
                ) {
                    Log.d("tag","$befoFatigueLevel$befoBreathingLevel")
                }

                override fun onClickClose() {

                }
            })
        }
        binding.sixminReportTvPrescription.setNoRepeatListener {
            val prescriptionFragment =
                SixMinReportPrescriptionFragment.startPrescriptionDialogFragment(
                    supportFragmentManager
                )
            prescriptionFragment.setPrescriptionDialogOnClickListener(object:
                SixMinReportPrescriptionFragment.SixMinReportPrescriptionDialogListener{
                override fun onClickConfirm(
                    stride: String,
                    distance: String,
                    heart: String,
                    metab: String,
                    borg: String
                ) {

                }

                override fun onClickClose() {

                }

            })
        }
        binding.sixminReportIvGenerateReport.setNoRepeatListener {
            val reportWalk = SixMinReportWalk()
            reportWalk.reportId = "123456789"
            reportWalk.walkOne = "60"
            reportWalk.walkTwo = "70"
            reportWalk.walkThree = "80"
            reportWalk.walkFour = "90"
            reportWalk.walkFive = "100"
            reportWalk.walkSix = "110"
            reportWalk.walkBig = "110"
            reportWalk.waklSmall = "60"
            reportWalk.walkAverage = "85"
            reportWalk.delFlag = "0"
            viewModel.setSixMinReportWalkData(reportWalk)
            //生成报告
            startActivity(Intent(this,SixMinReportActivity::class.java))
            finish()
        }
        binding.sixminReportIvClose.setNoRepeatListener {
            if(!exitPreReportDialog.isShowing){
                exitPreReportDialog.show()
            }
        }
    }

    private fun initExitPreReportDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage("退出将视为放弃生成报告，是否确定?")
        builder.setPositiveButton(getString(R.string.sixmin_system_setting_check_yes)) { _, _ ->
            finish()
        }
        builder.setNegativeButton(getString(R.string.sixmin_system_setting_check_no)) { _, _ ->

        }
        exitPreReportDialog = builder.create()
        exitPreReportDialog.setCanceledOnTouchOutside(false)
        exitPreReportDialog.setCancelable(false)
    }

    /**
     * 是否生成报告
     */
    private fun initGenerateReportDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage(getString(R.string.sixmin_test_generate_report_tips))
        builder.setPositiveButton(getString(R.string.sixmin_system_setting_check_yes)) { _, _ ->

        }
        builder.setNegativeButton(getString(R.string.sixmin_system_setting_check_no)) { _, _ ->

        }
        generateReportDialog = builder.create()
        generateReportDialog.setCanceledOnTouchOutside(false)
        generateReportDialog.setCancelable(false)
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
                "80",
                "80",
                "80",
                "80",
                "80",
                "80",
                "80",
                "80",
                "80",
                "80"
            )
        )
        reportRowList.add(
            SixMinReportItemBean(
                "步数",
                "70",
                "70",
                "70",
                "70",
                "70",
                "70",
                "70",
                "70",
                "70",
                "70"
            )
        )
        reportRowList.add(
            SixMinReportItemBean(
                "血压(mmHg)",
                "105/68",
                "/",
                "/",
                "/",
                "/",
                "/",
                "/",
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
                tvNo.textSize = dip2px( 7.0f).toFloat()
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
                dip2px( 6.0f),
                dip2px( 3.0f),
                dip2px( 6.0f),
                dip2px( 3.0f)
            )
            newRow.addView(linearLayout)
            binding.sixminReportTlPreTable.addView(newRow)
        }
    }

    private fun dip2px(dpValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.displayMetrics)
            .toInt()
    }

    override fun onResume() {
        SystemUtil.immersive(this,true)
        super.onResume()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            SystemUtil.immersive(this, true)
        }
    }
}