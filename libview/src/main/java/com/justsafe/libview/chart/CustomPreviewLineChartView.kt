package com.justsafe.libview.chart

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
import java.math.BigDecimal


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
     *@param maxY y轴最大值
     *@param maxX x轴最大值
     *@param averageY y轴平均多少个
     * */
    fun setData(
        data: MutableList<PointValue>, title1: String? = "Vol",
        title2: String? = "[L/S]",
        maxY: Int? = 5,
        minY: Int? = -5,
        maxX: Int? = 30,
        averageY: Int? = 5,
        averageX: Float? = 1f
    ) {
        setYAxisTitles(title1.toString(), title2.toString())
        entries.clear()
        entries.addAll(data)
        if (maxY != null && averageY != null && maxX != null && minY != null && averageX != null) {
            setupLineChart(maxY, averageY, maxX, averageX, minY)
        }
    }

    private fun setupLineChart(maxY: Int, averageY: Int, maxX: Int, averageX: Float, minY: Int) {
        // 创建折线
        val line = Line(entries)
        line.color = ContextCompat.getColor(context, R.color.colorPrimary) // 设置线条颜色
        line.strokeWidth = 2 // 设置线条粗细
        line.pointRadius = 1 // 设置点的半径大小
        line.isCubic = true // 设置线条为曲线

        val lines: MutableList<Line> = ArrayList()
        lines.add(line)

        val data = LineChartData(lines)

        // 创建X轴
        val axisX = Axis()
        axisX.setTextColor(Color.BLACK) // 设置字体颜色
        axisX.textSize = 8


        val xAxisValues: MutableList<AxisValue> = ArrayList()

        val averageXBigDecimal = BigDecimal.valueOf(averageX.toDouble())
        val maxXMultiplied = BigDecimal.valueOf(maxX.toDouble()).multiply(BigDecimal.TEN)

        for (i in 0 until maxXMultiplied.toInt() step (averageXBigDecimal.multiply(BigDecimal.TEN)).toInt()) {
            val actualValue = BigDecimal(i).divide(BigDecimal.TEN)
            xAxisValues.add(AxisValue(actualValue.toFloat()).setLabel(actualValue.toString()))
        }

        axisX.setValues(xAxisValues)


        // 创建Y轴
        val axisY: Axis = Axis().setHasLines(true)
        axisY.setTextColor(Color.BLACK)
        axisY.textSize = 8

        data.axisXBottom = axisX

        data.axisYLeft = axisY

        // 设置Y轴刻度
        val yAxisValues: MutableList<AxisValue> = ArrayList()
        val step = maxOf(1, maxY / averageY) // 确保步长至少为1，且为正数
        val startValue = -5 // 您可以根据需要调整这个起始值

        for (i in startValue..maxY step step) {
            yAxisValues.add(AxisValue(i.toFloat()).setLabel(i.toString()))
        }
        axisY.setValues(yAxisValues)


        lineChartData = data

        zoomType = ZoomType.HORIZONTAL // 限制为水平缩放

        // 自动计算视口
        val viewport = Viewport(maximumViewport)
        viewport.top = (maxY + 3).toFloat() // 为顶部增加一些额外空间
        viewport.right = maxX.toFloat() // 设置 x 轴的最大值
        viewport.bottom = minY.toFloat() // 设置 Y 轴的最小值

        // 设置最大视口和初始视口
        maximumViewport = viewport
        currentViewport = viewport

        // 确定你希望的视口宽度，例如，始终显示最大视口宽度的10%
        val desiredWidth = maximumViewport.width() * 0.05f
        // 计算中心点
        val midPointX = currentViewport.centerX()
        // 设置新的视口，以中心点为中心，左右扩展 desiredWidth / 2
        val modifiedViewport = Viewport(
            midPointX - desiredWidth / 2,
            currentViewport.top,
            midPointX + desiredWidth / 2,
            currentViewport.bottom
        )

        //y轴显示不全四位数
//        currentViewport.left = modifiedViewport.left + 20f
        currentViewport.left = 20f

        // 应用修改后的视口
        currentViewport = modifiedViewport

        invalidate()
    }


    fun setTopPadding(padding: Float) {
        topPadding = padding
        invalidate() // 重新绘制视图以更新偏移量
    }


    override fun onDraw(canvas: Canvas) {
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

    private fun setYAxisTitles(title1: String, title2: String) {
        this.yAxisTitle1 = title1
        this.yAxisTitle2 = title2
        invalidate() // 重新绘制视图以更新标题
    }

}



