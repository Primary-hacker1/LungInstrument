package com.just.machine.ui.activity

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.common.base.CommonBaseActivity
import com.just.machine.model.SixMinReportItemBean
import com.just.news.R
import com.just.news.databinding.ActivitySixMinReportBinding
import com.justsafe.libview.util.SystemUtil

/**
 * 6分钟报告
 */
class SixMinReportActivity: CommonBaseActivity<ActivitySixMinReportBinding>()  {

    private var reportRowList = mutableListOf<SixMinReportItemBean>()

    override fun getViewBinding() = ActivitySixMinReportBinding.inflate(layoutInflater)

    override fun initView() {
        initTable()
        initClickListener()
    }

    private fun initClickListener() {
        binding.sixminReportIvClose.setOnClickListener {
            PatientActivity.startPatientActivity(this,"finishSixMinTest")
            finish()
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