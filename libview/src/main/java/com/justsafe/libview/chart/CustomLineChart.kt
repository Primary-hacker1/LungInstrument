package com.justsafe.libview.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.utils.MPPointD
import com.justsafe.libview.R
import com.xxmassdeveloper.mpchartexample.ValueFormatter


class CustomScatterChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScatterChart(context, attrs, defStyleAttr) {

    private val yAxis2: YAxis = YAxis(YAxis.AxisDependency.RIGHT)

    init {
        setupChart()
        setExtraOffsets(20f, 0f, 60f, 0f) // 设置左、顶部、右、底部的偏移量
        setDynamicDragLine()
    }

    private fun setupChart() {
        // 隐藏描述
        description.isEnabled = false

        // 启用触摸和缩放
        setTouchEnabled(true)
        isDragEnabled = true
        setPinchZoom(true)

        // 设置左侧 Y 轴
        axisLeft.apply {
            textColor = Color.RED
        }

        // 设置右侧 Y 轴
        axisRight.isEnabled = true // 启用右侧 Y 轴
        axisRight.textColor = Color.BLUE // 设置轴标签颜色
        axisRight.setDrawLabels(true) // 绘制轴标签
        axisRight.setDrawAxisLine(true) // 绘制轴线
        axisRight.setDrawGridLines(true) // 绘制网格线

        // 自定义右侧 Y 轴标签格式化器
        axisRight.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return when (value.toInt()) {
                    0 -> "0"
                    100 -> "100"
                    else -> ""
                }
            }
        }

        // 设置数据
        val values1 = listOf(30f, 40f, 25f, 50f, 45f)
        val values2 = listOf(0.5f, 0.8f, 0.6f, 0.9f, 0.7f)

        val entries1 = values1.mapIndexed { index, value -> Entry(index.toFloat(), value) }
        val dataSet1 = ScatterDataSet(entries1, "数据集1").apply {
            color = Color.RED
            scatterShapeSize = 12f
            axisDependency = YAxis.AxisDependency.LEFT
        }

        val entries2 = values2.mapIndexed { index, value -> Entry(index.toFloat(), value) }
        val dataSet2 = ScatterDataSet(entries2, "数据集2").apply {
            color = Color.BLUE
            scatterShapeSize = 12f
            axisDependency = YAxis.AxisDependency.RIGHT
        }

        val scatterData = ScatterData(dataSet1, dataSet2)
        data = scatterData

        // 刷新图表
        invalidate()
    }

    private val mGridPaint: Paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    override fun drawGridBackground(canvas: Canvas?) {
        super.drawGridBackground(canvas)

        // 绘制右侧 Y 轴的网格线
        if (data == null || !yAxis2.isEnabled || !yAxis2.isDrawGridLinesEnabled) return

        val clipRect = mViewPortHandler.contentRect
        val trans = mRightAxisTransformer

        mGridPaint.color = yAxis2.gridColor
        mGridPaint.strokeWidth = yAxis2.gridLineWidth

        val positions = FloatArray(2)
        positions[0] = mViewPortHandler.contentRight() // 右侧 Y 轴的位置，根据实际情况调整

        val phaseY = mAnimator.phaseY

        // 获取 Y 轴的值范围（这里假设您的 yAxis2 是右侧 Y 轴对象）
        val min = yAxis2.axisMinimum
        val max = yAxis2.axisMaximum
        val labelCount = yAxis2.labelCount

        // 计算网格线的间隔
        val interval = (max - min) / (labelCount - 1)

        // 绘制网格线
        for (i in 0 until labelCount) {
            positions[1] = max - i * interval // 计算 Y 值位置

            trans.pointValuesToPixel(positions)

            // 检查 Y 值位置是否在绘制区域内
            if (positions[1] >= clipRect.top && positions[1] <= clipRect.bottom) {
                canvas?.drawLine(clipRect.left, positions[1], clipRect.right, positions[1], mGridPaint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setDynamicDragLine() {
        val xAxis = xAxis
        val initialPosition = 10f // 初始位置
        val limitLine = LimitLine(initialPosition, "拖拽线")
        limitLine.lineColor = ContextCompat.getColor(context, R.color.colorPrimary)
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
                    getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(
                        event.x,
                        event.y,
                        outputPoint
                    )

                    // 从转换后的结果中获取 x 轴的数据值
                    val xValue = outputPoint.x.toFloat() // 将 double 转换为 float

                    // 更新 LimitLine 的位置到新的 x 坐标值
                    limitLine.limit = xValue

                    // 获取与垂直线相交的数据点
                    val entriesMap = mutableMapOf<IScatterDataSet, MutableList<Entry>>()
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
}



