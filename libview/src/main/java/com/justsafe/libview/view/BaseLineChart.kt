package com.justsafe.libview.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.justsafe.libview.R

class BaseLineChart(context: Context, attrs: AttributeSet?) : LineChart(context, attrs) {

    val entries = arrayListOf<Entry>()// 创建折线图的样本数据

    /**
     * @param entriesList 数据
     * @param title2 标题2y轴单位
     * */
    fun setLineChartFlow(
        entriesList : MutableList<LineDataSet>?=ArrayList(),
        yAxisMinimum: Float? = 0f,//y轴最小值
        yAxisMaximum: Float? = 0f,//y轴最大值
        count: Int? = 0,//y轴的个数
        title2: String? = "[L/S]"
    ) {

        for (index in 0..30) {
            entries.add(Entry(index.toFloat(), index.toFloat() / 6 ))
        }

        val dataSet1 = LineDataSet(entries, "Data Set 1")
        dataSet1.color = ContextCompat.getColor(context, R.color.colorPrimary) // 设置曲线颜色
        dataSet1.setCircleColor(Color.BLUE) // 设置曲线上的数据点颜色
        dataSet1.lineWidth = 2f // 设置曲线宽度
        dataSet1.circleRadius = 3f // 设置曲线上的数据点半径
        dataSet1.valueTextSize = 10f // 设置数据点值的字体大小
        dataSet1.valueTextColor = ContextCompat.getColor(context, R.color.Indigo_colorPrimary) // 设置曲线颜色
        dataSet1.setDrawValues(false) // 设置是否绘制数据点的值
        dataSet1.mode = LineDataSet.Mode.CUBIC_BEZIER // 设置曲线模式为三次贝塞尔曲线

        entriesList?.add(dataSet1)

        val lineDataSets = mutableListOf<ILineDataSet>()

        entriesList?.let { lineDataSets.addAll(it) }

        val lineData = LineData(lineDataSets)
        data = lineData

        xAxis.granularity = 1f     //这个很重要

        xAxis.position = XAxis.XAxisPosition.BOTTOM

        xAxis.setLabelCount(entries.size, true)//x刻度多少个

        // 禁用X轴的刻度线
        xAxis.setDrawGridLines(false)

        // 设置标题
        description.text = title2
        description.setPosition(80f, 22f)
        description.textSize = 9f
        description.textColor = Color.BLACK

        // 设置 Y 轴的最小值和最大值，以确保包含所有单数刻度
        if (yAxisMinimum != null) {
            axisLeft.axisMinimum = yAxisMinimum
        } // 设置 Y 轴最小值
        if (yAxisMaximum != null) {
            axisLeft.axisMaximum = yAxisMaximum
        } // 设置 Y 轴最大值
        if (yAxisMinimum != null) {
            axisRight.axisMinimum = yAxisMinimum
        } // 设置 Y 轴最小值
        if (yAxisMaximum != null) {
            axisRight.axisMaximum = yAxisMaximum
        } // 设置 Y 轴最大值

        setExtraOffsets(0f, 0f, 0f, 0f) // 上、左、下、右边距

        // 设置 LineChart 可以拖动
        isDragEnabled = true

        // 设置 LineChart 的缩放
        setScaleEnabled(false)

        setPinchZoom(false)

        count?.let {
            axisLeft.setLabelCount(
                it, true
            )
        } // y轴刻度多少个
        if (count != null) {
            axisRight.setLabelCount(
                count, true
            )
        } // y轴刻度多少个

        // 刷新图表
        invalidate()
    }
}