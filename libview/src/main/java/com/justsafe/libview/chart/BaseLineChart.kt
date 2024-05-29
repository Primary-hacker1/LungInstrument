package com.justsafe.libview.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.MPPointD
import com.justsafe.libview.R
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import kotlin.math.ceil


/**
 * 自定义的基础折线图控件，继承自 LineChart。
 * 提供了设置折线图基本属性和数据的方法。
 */
class BaseLineChart(context: Context, attrs: AttributeSet?) : LineChart(context, attrs) {

    private val tag = BaseLineChart::class.java.simpleName

    // 折线数据集合
    private val entriesList: MutableList<LineDataSet> = ArrayList()

    var title1 = ""
    var title2 = ""
    var titleCentent = "居中标题"

    // 记录原始的 X 轴最大值
    private var originalXAxisMaximum: Float = 30f

    init {
//        setLineChartFlow(-5f, 5f, originalXAxisMaximum, 11)
    }

    /**
     * 设置折线图的基本属性。
     * @param yAxisMinimum Y 轴的最小值
     * @param yAxisMaximum Y 轴的最大值
     * @param countMaxX X 轴的刻度数
     * @param granularityY Y 轴的刻度数
     * @param title2 Y 轴的标题
     */
    fun setLineChartFlow(
        yAxisMinimum: Float? = 0f,
        yAxisMaximum: Float? = 0f,
        countMaxX: Float? = 0f,
        granularityY: Float? = 1.5f, // Y 轴每个标签的间隔
        granularityX: Float? = 1f, // X 轴每个标签的间隔（由X轴刻度数和X轴标签数量决定）
        title1: String? = "[Vol]",
        title2: String? = "[L/S]",
        titleCentent: String? = ""
    ) {
        // 设置标题
        this.title1 = title1.toString()
        this.title2 = title2.toString()
        this.titleCentent = titleCentent.toString()
        // 创建自定义格式化器实例
        val customYAxisFormatter = CustomYAxisValueFormatter()

        // 设置 X 轴属性
//        xAxis.valueFormatter = CustomYAxisValueFormatter()
        xAxis.granularity = granularityX!!  // 每个标签间隔1
        xAxis.axisMinimum = 0f  // X轴最小值设置为0（如果需要）
        xAxis.axisMaximum = countMaxX!!  // 你已经有这一步
        axisLeft.valueFormatter = customYAxisFormatter
        xAxis.setLabelCount(((xAxis.axisMaximum - xAxis.axisMinimum) / granularityX + 1).toInt(), true)
        xAxis.setDrawGridLines(false)

        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // xAxis.setCenterAxisLabels(true)

        // 设置描述
        description.text = ""
        description.setPosition(80f, 22f)
        description.textSize = 9f
        description.textColor = Color.BLACK

        // 设置 Y 轴最小值和最大值
        if (yAxisMinimum != null && yAxisMaximum != null) {
            axisLeft.axisMinimum = yAxisMinimum
            axisRight.axisMinimum = yAxisMinimum
            axisLeft.axisMaximum = yAxisMaximum
            axisRight.axisMaximum = yAxisMaximum

            val range = yAxisMaximum - yAxisMinimum

            val numY = ceil((range / granularityY!!).toDouble()).toInt()  // 使用Math.ceil确保覆盖全部范围

            // 应用自定义的ValueFormatter
            axisLeft.valueFormatter = CustomYAxisValueFormatter()
            axisRight.valueFormatter = CustomYAxisValueFormatter()

            axisLeft.granularity = granularityY
            axisRight.granularity = granularityY

            // 根据实际数据调整labelCount
            axisLeft.setLabelCount(
                ((axisLeft.axisMaximum - axisLeft.axisMinimum) / axisLeft.granularity + 1).toInt(),
                true
            )
            axisRight.setLabelCount(
                ((axisRight.axisMaximum - axisRight.axisMinimum) / axisRight.granularity + 1).toInt(),
                true
            )


            // 设置到Y轴
            axisLeft.valueFormatter = customYAxisFormatter
            axisRight.valueFormatter = customYAxisFormatter

            Log.e(tag, "$numY-----y轴的个数")
        }

        xAxis.textSize = 8f
        axisLeft.textSize = 8f
        axisRight.textSize = 8f

        // 设置其他图表属性
        setExtraOffsets(0f, 25f, 0f, 0f)
        isDragEnabled = true
        setScaleEnabled(false)
        setPinchZoom(false)


        notifyDataSetChanged()  // 通知数据变更
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setDynamicDragLine(){
        val xAxis = xAxis
        val initialPosition = 10f // 初始位置
        val limitLine = LimitLine(initialPosition, "拖拽线")
        limitLine.lineColor = ContextCompat.getColor(context,R.color.colorPrimary)
        limitLine.lineWidth = 2f
        xAxis.addLimitLine(limitLine)
        invalidate() // 刷新图表

        // 创建 VerticalLineView 实例
        val verticalLineView = VerticalLineView(context)

        // 设置 VerticalLineView 的布局参数，例如宽度和高度
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        verticalLineView.layoutParams = layoutParams

        // 将 VerticalLineView 添加到你的布局中
        addView(verticalLineView)

        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    // 获取触摸点的 x 坐标
                    val x = event.x

                    // 更新 VerticalLineView 的位置
                    verticalLineView.setXPosition(x)

                    val outputPoint = MPPointD.getInstance(0.0, 0.0)
                    getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(event.x, event.y, outputPoint)

                    // 从转换后的结果中获取 x 轴的数据值
                    val xValue = outputPoint.x.toFloat() // 将 double 转换为 float

                    // 更新 LimitLine 的位置到新的 x 坐标值
                    limitLine.limit = xValue

                    // 获取与垂直线相交的数据点
                    val entriesMap = mutableMapOf<ILineDataSet, MutableList<Entry>>()
                    val dataSets = data.dataSets
                    for (dataSet in dataSets) {
                        val entries = mutableListOf<Entry>()
                        val entryForXValue = dataSet.getEntryForXValue(xValue, Float.NaN)
                        entryForXValue?.let { entries.add(it) }
                        if (entries.isNotEmpty()) {
                            entriesMap[dataSet] = entries
                        }
                    }

                    // 这里的 entriesMap 包含了每个数据集与垂直线相交的数据点列表
                    for ((dataSet, entries) in entriesMap) {
                        for (entry in entries) {
                            val yValue = entry.y
                            // 这里处理 yValue，例如打印或存储
                            println("DataSet: ${dataSet.label}, Y value: $yValue")
                        }
                    }

                    invalidate() // 刷新图表以显示新位置的 LimitLine

                    MPPointD.recycleInstance(outputPoint) // 回收 MPPointD 实例
                    true
                }
                else -> false
            }
        }
    }

    /**
     * 设置折线图的数据。
     * @param dataSetList 折线图数据集合
     */
    fun setLineDataSetData(
        dataSetList: MutableList<LineDataSet>? = mutableListOf(),
    ) {
        entriesList.clear()

        dataSetList?.let { entriesList.addAll(it) }

        val lineDataSets = mutableListOf<ILineDataSet>()

        entriesList.let { lineDataSets.addAll(it) }

        val lineData = LineData(lineDataSets)

        data = lineData

    }


    /**
     * 设置折线图的数据。
     * @param dataSetColors 折线颜色
     * @param valueTextColor 折线点颜色
     */
    fun flowDataSetList(
        valueTextColor: Int? = R.color.Indigo_colorPrimary,
        dataSetColors: List<Int>? = listOf(
            R.color.cFF5B5B,
            R.color.gray,
            R.color.wheel_title_bar_ok_color,
            R.color.attend_bg
        ),
    ): MutableList<LineDataSet> {

        val dataSetEntriesMap = mutableMapOf<String, MutableList<Entry>>()

        val entries1 = mutableListOf<Entry>()
        val entries2 = mutableListOf<Entry>()
        val entries3 = mutableListOf<Entry>()
        val entries4 = mutableListOf<Entry>()

        // 模拟十个数据点
        for (i in 0 until 10) {
            // 在 x 轴上以递增的方式设置数据点的 x 值，y 值可以根据需要设置
            val entry1 = Entry(i.toFloat(), i.toFloat() * 2)
            entries1.add(entry1)

            val entry2 = Entry(i.toFloat(), i.toFloat() / 1)
            entries2.add(entry2)
        }

        // 模拟二十个数据点
        for (i in 0 until 20) {
            // 在 x 轴上以递增的方式设置数据点的 x 值，y 值可以根据需要设置
            val entry3 = Entry(i.toFloat(), i.toFloat() / 2)
            entries3.add(entry3)

            val entry4 = Entry(i.toFloat(), i.toFloat() / 4)
            entries4.add(entry4)
        }

        dataSetEntriesMap["1"] = entries1
        dataSetEntriesMap["2"] = entries2
        dataSetEntriesMap["3"] = entries3
        dataSetEntriesMap["4"] = entries4

        // 创建多个 LineDataSet 对象
        val dataSet1 = LineDataSet(dataSetEntriesMap["1"], "1")
        val dataSet2 = LineDataSet(dataSetEntriesMap["2"], "2")
        val dataSet3 = LineDataSet(dataSetEntriesMap["3"], "3")
        val dataSet4 = LineDataSet(dataSetEntriesMap["4"], "4")

        // 将 LineDataSet 对象添加到一个列表中
        val list = mutableListOf(dataSet1, dataSet2, dataSet3, dataSet4)

        for ((index, dataSet) in list.withIndex()) {

            // 确保颜色列表不为空，且颜色足够多以覆盖所有的 LineDataSet
            val color = dataSetColors?.get(index) ?: R.color.colorPrimary

            dataSet.color = ContextCompat.getColor(context, color)

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

        // 4. 创建自定义渲染器的实例，并将其应用到 LineChart 中
        val customRenderer = CustomLineChartRenderer(this, animator, viewPortHandler)
        renderer = customRenderer

        return list
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 转换 dp 为像素
        val titleTextSizePx = dpToPx(6f, context) // 标题文本的大小
        val contentTextSizePx = dpToPx(15f, context) // 文本的大小

        // 绘制标题文本
        val titlePaint = Paint().apply {
            textSize = titleTextSizePx
            color = Color.BLACK
            textAlign = Paint.Align.CENTER
        }
        val totalTitleHeight = titlePaint.fontSpacing * 2 // 两行标题文本的总高度
        val titleStartX = 30f // 左边留出一定的空白
        val titleStartY = totalTitleHeight - 26 // 留出与标题文本相同的高度
        canvas.drawText(title1, titleStartX, titleStartY, titlePaint)
        canvas.drawText(title2, titleStartX, titleStartY + titlePaint.fontSpacing, titlePaint)

        // 绘制垂直居中的文本
        val verticalText = titleCentent
        val verticalTextPaint = Paint().apply {
            textSize = contentTextSizePx
            color = ContextCompat.getColor(context, R.color.c888888)
            textAlign = Paint.Align.CENTER
        }

        val verticalTextBounds = Rect()
        verticalTextPaint.getTextBounds(verticalText, 0, verticalText.length, verticalTextBounds)
        val verticalTextHeight = verticalTextBounds.height()
        val viewWidth = width
        val centerX = (viewWidth - verticalTextBounds.width()) / 2 + verticalTextPaint.measureText(verticalText) / 2 // 文本的水平居中位置
        val centerY = verticalTextHeight // 文本的顶部位置
        canvas?.drawText(verticalText, centerX, centerY.toFloat(), verticalTextPaint)
    }

    // 自定义y轴左右坐标
    class CustomYAxisValueFormatter : ValueFormatter() {
        @SuppressLint("DefaultLocale")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return String.format("%.1f", value)
        }

        override fun getFormattedValue(value: Float): String {
            // 你可以根据 value 返回任何格式的字符串
            return "$value"
        }
    }

    // 自定义y轴左右坐标
    class DynamicLeftValueFormatter : ValueFormatter() {
        @SuppressLint("DefaultLocale")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return String.format("%.2f", value)
        }

        override fun getFormattedValue(value: Float): String {
            // 你可以根据 value 返回任何格式的字符串
            return "$value"
        }
    }

    // 自定义y轴左右坐标
    class DynamicRightValueFormatter : ValueFormatter() {
        @SuppressLint("DefaultLocale")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return String.format("%.1f", value)
        }

        override fun getFormattedValue(value: Float): String {
            // 你可以根据 value 返回任何格式的字符串
            return "$value"
        }
    }

    fun dpToPx(dp: Float, context: Context): Float {
        return dp * context.resources.displayMetrics.density
    }

}

