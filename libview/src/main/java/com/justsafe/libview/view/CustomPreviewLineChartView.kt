package com.justsafe.libview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.justsafe.libview.R
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.AxisValue
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.model.Viewport
import lecho.lib.hellocharts.view.PreviewLineChartView


class CustomPreviewLineChartView(context: Context, attrs: AttributeSet?) :
    PreviewLineChartView(context, attrs) {

    private var yAxisTitle1: String = ""

    private var yAxisTitle2: String = ""

    private var topPadding: Float = -50f // 顶部偏移量

    private var entries: MutableList<PointValue> = ArrayList()


    /**
     *@param data 折线图数据
     *@param title1 左上方标题1
     *@param title2 左上方标题2
     * */
    fun setData(data: MutableList<PointValue>, title1: String? = "Vol", title2: String? = "[L/S]") {
        setYAxisTitles(title1.toString(), title2.toString())
        entries.clear()
        entries.addAll(data)
        setupLineChart()
    }

    private fun setupLineChart() {

        val line = Line(entries)

        line.color = ContextCompat.getColor(context, R.color.colorPrimary) // 设置线条颜色

        line.strokeWidth = 2 // 设置线条粗细

        line.pointRadius = 1 // 设置点的半径大小

        line.isCubic = true // 设置线条为曲线

        val lines: MutableList<Line> = ArrayList()

        val data = LineChartData(lines)

        lines.add(line)

        data.lines = lines

        // 创建X轴
        val axisX = Axis()

        axisX.setTextColor(Color.BLACK) // 设置字体颜色

        axisX.textSize = 8

//        axisX.setName("X Axis") // 设置轴名称

        axisX.setMaxLabelChars(7) //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length

        // 创建Y轴
        val axisY: Axis = Axis().setHasLines(true)

        axisY.setTextColor(Color.BLACK)

//        axisY.setName("Y Axis")

        axisY.textSize = 8

        data.axisXBottom = axisX

        data.axisYLeft = axisY

        axisY.setMaxLabelChars(32) // 最大字符数，用于计算轴的空间

        val yAxisMin = 1
        val yAxisMax = 32
        axisY.setHasLines(true) // 显示网格线

        val yAxisValues: MutableList<AxisValue> = ArrayList()
        for (i in yAxisMin..yAxisMax) {
            yAxisValues.add(AxisValue(i.toFloat()).setLabel(i.toString()))
        }
        axisY.setValues(yAxisValues)
        data.axisYLeft = axisY


        lineChartData = data

        zoomType = ZoomType.HORIZONTAL // 限制为水平缩放

        // 自动计算视口
        val viewport = Viewport(maximumViewport)
        viewport.top = 110f // 为顶部增加一些额外空间

        maximumViewport = viewport
        currentViewport = viewport

        // 假设你已经初始化了图表数据
        // 设置最大视口和初始视口
        val maxViewport = Viewport(maximumViewport)
        maximumViewport = maxViewport


        val newViewport = Viewport(maximumViewport)
        // 确定你希望的视口宽度，例如，始终显示最大视口宽度的10%
        val desiredWidth = maxViewport.width() * 0.05f

        // 计算中心点
        val midPointX = newViewport.centerX()

        // 设置新的视口，以中心点为中心，左右扩展 desiredWidth / 2
        val modifiedViewport = Viewport(
            midPointX - desiredWidth / 2,
            newViewport.top,
            midPointX + desiredWidth / 2,
            newViewport.bottom
        )

        currentViewport = modifiedViewport
    }

    fun setTopPadding(padding: Float) {
        topPadding = padding
        invalidate() // 重新绘制视图以更新偏移量
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // 绘制 Y 轴标题
        drawYAxisTitles(canvas)
    }

    private fun drawYAxisTitles(canvas: Canvas?) {
        // 设置标题文字画笔
        val titlePaint = Paint()
        titlePaint.textSize = 20f
        titlePaint.color = Color.BLACK

        // 获取 Y 轴的位置信息
        val chartWidth = width - paddingLeft - paddingRight
        val chartHeight = height - paddingTop - paddingBottom

        // 设置标题1的位置
        val x1 = paddingLeft.toFloat() + 20
        val y1 = paddingTop.toFloat() + topPadding + 40 // 考虑顶部偏移量

        // 绘制标题1
        canvas?.drawText(yAxisTitle1, x1, y1, titlePaint)

        // 设置标题2的位置
        val x2 = paddingLeft.toFloat() + 20
        val y2 = paddingTop.toFloat() + topPadding + 60 // 考虑顶部偏移量

        // 绘制标题2
        canvas?.drawText(yAxisTitle2, x2, y2, titlePaint)
    }

    fun setYAxisTitles(title1: String, title2: String) {
        this.yAxisTitle1 = title1
        this.yAxisTitle2 = title2
        invalidate() // 重新绘制视图以更新标题
    }
}



