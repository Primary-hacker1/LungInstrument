package com.justsafe.libview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import lecho.lib.hellocharts.view.PreviewLineChartView

class CustomPreviewLineChartView(context: Context, attrs: AttributeSet?) : PreviewLineChartView(context, attrs) {

    private var yAxisTitle1: String = ""
    private var yAxisTitle2: String = ""
    private var topPadding: Float = 0f // 顶部偏移量

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



