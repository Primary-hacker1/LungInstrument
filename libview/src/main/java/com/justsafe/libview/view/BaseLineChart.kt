package com.justsafe.libview.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.justsafe.libview.R

/**
 * 自定义的基础折线图控件，继承自 LineChart。
 * 提供了设置折线图基本属性和数据的方法。
 */
class BaseLineChart(context: Context, attrs: AttributeSet?) : LineChart(context, attrs) {

    private val tag = BaseLineChart::class.java.name

    // 折线数据集合
    private val entriesList: MutableList<LineDataSet> = ArrayList()

    // 记录原始的 X 轴最大值
    private var originalXAxisMaximum: Float = 30f

    init {
        setLineChartFlow(-5f, 5f, 30f, 11)
    }

    /**
     * 设置折线图的基本属性。
     * @param yAxisMinimum Y 轴的最小值
     * @param yAxisMaximum Y 轴的最大值
     * @param countX X 轴的刻度数
     * @param countY Y 轴的刻度数
     * @param title2 Y 轴的标题
     */
    fun setLineChartFlow(
        yAxisMinimum: Float? = 0f,
        yAxisMaximum: Float? = 0f,
        countX: Float = originalXAxisMaximum,
        countY: Int? = 0,
        title2: String? = "[L/S]"
    ) {
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        countX.let { xAxis.setLabelCount(it.toInt(), true) }
        countX.let { xAxis.axisMaximum = (it / 1) }
        xAxis.setDrawGridLines(false)
        description.text = title2
        description.setPosition(80f, 22f)
        description.textSize = 9f
        description.textColor = Color.BLACK
        if (yAxisMinimum != null) {
            axisLeft.axisMinimum = yAxisMinimum
            axisRight.axisMinimum = yAxisMinimum
        }
        if (yAxisMaximum != null) {
            axisLeft.axisMaximum = yAxisMaximum
            axisRight.axisMaximum = yAxisMaximum
        }
        setExtraOffsets(0f, 0f, 0f, 0f)
        isDragEnabled = true
        setScaleEnabled(false)
        setPinchZoom(false)
        countY?.let {
            axisLeft.setLabelCount(
                it, true
            )
            axisRight.setLabelCount(
                it, true
            )
        }
        invalidate()
    }

    /**
     * 设置折线图的数据。
     * @param dataSetList 折线图数据集合
     * @param dataSetColors 折线颜色
     * @param valueTextColor 折线点颜色
     */
    fun setLineDataSetData(
        dataSetList: MutableList<LineDataSet>? = mutableListOf(),
        dataSetColors: List<Int>? = listOf(
            R.color.colorPrimary,
            R.color.colorAccent,
            R.color.cardview_dark_background,
            R.color.c238E23
        ),
        valueTextColor: Int? = R.color.Indigo_colorPrimary
    ) {
        entriesList.clear()

        if (dataSetList?.isEmpty() == true) {
            val entries1 = mutableListOf<Entry>()

            // 模拟十个数据点
            for (i in 0 until 10) {
                // 在 x 轴上以递增的方式设置数据点的 x 值，y 值可以根据需要设置
                val entry = Entry(i.toFloat(), i.toFloat() * 2)
                entries1.add(entry)
            }

            val entries2 = mutableListOf<Entry>()

            // 模拟十个数据点
            for (i in 0 until 10) {
                // 在 x 轴上以递增的方式设置数据点的 x 值，y 值可以根据需要设置
                val entry = Entry(i.toFloat(), i.toFloat())
                entries2.add(entry)
            }

            val entries3 = mutableListOf<Entry>()

            // 模拟十个数据点
            for (i in 0 until 10) {
                // 在 x 轴上以递增的方式设置数据点的 x 值，y 值可以根据需要设置
                val entry = Entry(i.toFloat(), i.toFloat() / 1)
                entries3.add(entry)
            }

            val entries4 = mutableListOf<Entry>()

            // 模拟十个数据点
            for (i in 0 until 10) {
                // 在 x 轴上以递增的方式设置数据点的 x 值，y 值可以根据需要设置
                val entry = Entry(i.toFloat(), i.toFloat() / 3)
                entries4.add(entry)
            }

            // 创建多个 LineDataSet 对象
            val dataSet1 = LineDataSet(entries1, "Data Set 1")
            val dataSet2 = LineDataSet(entries2, "Data Set 2")
            val dataSet3 = LineDataSet(entries3, "Data Set 3")
            val dataSet4 = LineDataSet(entries4, "Data Set 3")

            // 将 LineDataSet 对象添加到一个列表中
            val list = listOf(dataSet1, dataSet2, dataSet3, dataSet4)

            dataSetList.addAll(list)
        }


        if (dataSetList != null) {
            for ((index, dataSet) in dataSetList.withIndex()) {

                // 确保颜色列表不为空，且颜色足够多以覆盖所有的 LineDataSet
                val color = dataSetColors?.get(index) ?:R.color.colorPrimary

                dataSet.color = ContextCompat.getColor(context,color)

                Log.e(tag, "$index-----$color")

                dataSet.setCircleColor(Color.BLUE)

                dataSet.lineWidth = 2f

                dataSet.circleRadius = 2f

                dataSet.valueTextSize = 10f

                if (valueTextColor != null) {
                    dataSet.setCircleColor(valueTextColor)
                }
                dataSet.setDrawValues(false)

                dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            }
        }

        dataSetList?.let { entriesList.addAll(it) }

        val lineDataSets = mutableListOf<ILineDataSet>()

        entriesList.let { lineDataSets.addAll(it) }

        val lineData = LineData(lineDataSets)

        data = lineData

        xAxis.setLabelCount(originalXAxisMaximum.toInt(), true)

        xAxis.axisMaximum = (originalXAxisMaximum / 1)

        invalidate()
    }


}

