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
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.utils.MPPointD
import com.justsafe.libview.R
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/*
* 运动评估专用散点图
* */
class ResultScatterChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScatterChart(context, attrs, defStyleAttr) {

    private val tag = ResultScatterChart::class.java.name

    private val yAxis2: YAxis = YAxis(YAxis.AxisDependency.RIGHT)

    private val entries1: MutableList<Entry> = mutableListOf()

    private val entries2: MutableList<Entry> = mutableListOf()

    private lateinit var dataSet1: ScatterDataSet

    private lateinit var dataSet2: ScatterDataSet

    private lateinit var limitLine: LimitLine

    private var onEntrySelectedListener: ((Map<IScatterDataSet, List<Entry>>) -> Unit)? = null

    data class ResultChartBean(
        var titleOneL: String? = "VO2",
        var titleTwoL: String? = "[L/S]",

        var titleOneR: String? = "VCO2",
        var titleTwoR: String? = "[L/S]",
        var titleContent: String? = "居中标题",
    )

    private var resultChartBean = ResultChartBean()

    init {
        setupChart()
        setExtraOffsets(20f, 20f, 20f, 2f) // 设置左、顶部、右、底部的偏移量
    }

    fun setTitle(
        resultChartBean: ResultChartBean
    ) {
        this.resultChartBean = resultChartBean
    }

    private fun setupChart() {
        // 隐藏描述
        description.isEnabled = false

        // 启用触摸和缩放
        setTouchEnabled(true)
        isDragEnabled = true
        setPinchZoom(true)

        // 设置 X 轴在底部显示
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false) // 禁用 X 轴的轴线
        xAxis.setDrawGridLines(false) // 禁用 X 轴的网格线
        xAxis.setDrawLabels(true) // 启用 X 轴的标签

        // 设置左侧 Y 轴
        axisLeft.apply {
            textColor = Color.RED
        }

        // 设置右侧 Y 轴
        axisRight.isEnabled = true // 启用右侧 Y 轴
        axisRight.textColor = Color.BLUE // 设置轴标签颜色
        axisRight.setDrawLabels(true) // 绘制轴标签
        axisRight.setDrawAxisLine(false) // 绘制轴线
        axisRight.setDrawGridLines(false) // 绘制网格线

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

        dataSet1 = ScatterDataSet(entries1, "数据集1").apply {
            color = Color.RED
            scatterShapeSize = 8f
            axisDependency = YAxis.AxisDependency.LEFT
        }

        dataSet2 = ScatterDataSet(entries2, "数据集2").apply {
            color = Color.BLUE
            scatterShapeSize = 8f
            axisDependency = YAxis.AxisDependency.RIGHT
        }

        val scatterData = ScatterData(
            dataSet1,
        )
        data = scatterData

        invalidate()
    }

    //动态评估设置双轴
    fun setDynamicAxis() {
        val scatterData = ScatterData(
            dataSet1,
            dataSet2
        )
        data = scatterData
        invalidate()
    }

    // 实时更新数据集的方法
    private fun updateData(newValue1: Float, newValue2: Float) {
        val xValue1 = entries1.size.toFloat()
        val xValue2 = entries2.size.toFloat()
        entries1.add(Entry(xValue1, newValue1))
        entries2.add(Entry(xValue2, newValue2))

        dataSet1.notifyDataSetChanged()
        dataSet2.notifyDataSetChanged()
        data.notifyDataChanged()
        notifyDataSetChanged()
        invalidate()
    }

    /**
     * 模拟散点图，真实的要改
     * */
    fun startUpdatingData() {
        var job: Job? = null
        var currentValue1 = 0f
        var currentValue2 = 0.5f

        job = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(1000) // 每秒钟执行一次

                currentValue1 += Random.nextFloat() * 0.1f // 生成一个随机数用于数据点变化
                currentValue2 += Random.nextFloat() * 1f + 0.05f // 生成一个随机数用于数据点变化

                // 添加新数据点
                updateData(currentValue1, currentValue2)

                // 刷新图表
                invalidate()
            }
        }
    }

    private val mGridPaint: Paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    /**
     * 绘制双轴
     * */
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

        mAnimator.phaseY

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
                canvas?.drawLine(
                    clipRect.left,
                    positions[1],
                    clipRect.right,
                    positions[1],
                    mGridPaint
                )
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 转换 dp 为像素
        val titleTextSizePx = dpToPx(8f, context) // 标题文本的大小
        val contentTextSizePx = dpToPx(18f, context) // 文本的大小

        // 绘制左边标题文本
        val titlePaintLeft = Paint().apply {
            textSize = titleTextSizePx
            color = Color.BLACK
            textAlign = Paint.Align.LEFT
        }
        val totalTitleHeight = titlePaintLeft.fontSpacing * 2 // 两行标题文本的总高度
        val titleStartXLeft = 40f // 左边留出一定的空白
        val titleStartY = totalTitleHeight - 26 // 留出与标题文本相同的高度
        canvas.drawText(resultChartBean.titleOneL.toString(), titleStartXLeft, titleStartY, titlePaintLeft)
        canvas.drawText(resultChartBean.titleTwoL.toString(), titleStartXLeft, titleStartY + titlePaintLeft.fontSpacing, titlePaintLeft)

        // 绘制右边标题文本
        val titlePaintRight = Paint().apply {
            textSize = titleTextSizePx
            color = Color.BLACK
            textAlign = Paint.Align.RIGHT
        }
        val titleStartXRight = width - 40f // 右边留出一定的空白
        canvas.drawText(resultChartBean.titleOneR.toString(), titleStartXRight, titleStartY, titlePaintRight)
        canvas.drawText(resultChartBean.titleTwoR.toString(), titleStartXRight, titleStartY + titlePaintRight.fontSpacing, titlePaintRight)

        // 绘制垂直居中的文本
        val verticalText = resultChartBean.titleContent.toString()
        val verticalTextPaint = Paint().apply {
            textSize = contentTextSizePx
            color = ContextCompat.getColor(context, R.color.c888888)
            textAlign = Paint.Align.CENTER
        }

        val verticalTextBounds = Rect()
        verticalTextPaint.getTextBounds(verticalText, 0, verticalText.length, verticalTextBounds)
        val verticalTextHeight = verticalTextBounds.height()
        val viewWidth = width
        val centerX = (viewWidth - verticalTextBounds.width()) / 2 + verticalTextPaint.measureText(
            verticalText
        ) / 2 // 文本的水平居中位置
        val centerY = verticalTextHeight // 文本的顶部位置
        canvas.drawText(verticalText, centerX, centerY.toFloat(), verticalTextPaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setDynamicDragLine(onPositionChanged: (Float) -> Unit) {
        val xAxis = xAxis
        val initialPosition = 10f // 初始位置
        limitLine = LimitLine(initialPosition, "拖拽线")
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

                    // 通知外部回调
                    onPositionChanged(convertToSharedCoordinate(xValue))

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
                            // 打印数据集的标签、X值和Y值
                            println("DataSet: ${dataSet.label}, X value: ${entry.x}, Y value: $yValue")
                        }
                    }

                    // 回调返回相交的数据点
                    onEntrySelectedListener?.invoke(entriesMap)

                    invalidate() // 刷新图表以显示新位置的 LimitLine

                    MPPointD.recycleInstance(outputPoint) // 回收 MPPointD 实例
                    true
                }

                else -> false
            }
        }
    }

    // 更新拖拽线位置的方法
    fun updateDragLine(sharedPosition: Float) {
        val localPosition = convertFromSharedCoordinate(sharedPosition)
        limitLine.limit = localPosition
        invalidate() // 刷新图表以显示新位置的 LimitLine
    }

    // 将本地X轴坐标转换为共享的逻辑坐标
    private fun convertToSharedCoordinate(localX: Float): Float {
        val xMin = xAxis.axisMinimum
        val xMax = xAxis.axisMaximum
        return (localX - xMin) / (xMax - xMin)
    }

    // 将共享的逻辑坐标转换为本地X轴坐标
    private fun convertFromSharedCoordinate(sharedX: Float): Float {
        val xMin = xAxis.axisMinimum
        val xMax = xAxis.axisMaximum
        return sharedX * (xMax - xMin) + xMin
    }

    // 设置相交数据点回调的方法
    fun setOnEntrySelectedListener(listener: (Map<IScatterDataSet, List<Entry>>) -> Unit) {
        onEntrySelectedListener = listener
    }

    fun dpToPx(dp: Float, context: Context): Float {
        return dp * context.resources.displayMetrics.density
    }
}



