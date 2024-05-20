package com.justsafe.libview.chart

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler


class CustomLineChartRenderer(chart: LineChart, animator: ChartAnimator, viewPortHandler: ViewPortHandler) :
    LineChartRenderer(chart, animator, viewPortHandler) {

    private val mPaintLineSegment: Paint = Paint()

    init {
        // 初始化绘制线段的画笔
        mPaintLineSegment.style = Paint.Style.STROKE
        mPaintLineSegment.strokeWidth = 5f
    }

    override fun drawLinear(c: Canvas?, dataSet: ILineDataSet?) {
        // 获取数据集中的点
        val trans: Transformer = mChart.getTransformer(dataSet?.axisDependency)
        val entryCount = dataSet?.entryCount

        // 循环绘制线段
        if (entryCount != null) {
            for (i in 0 until entryCount - 1) {
                val startEntry = dataSet.getEntryForIndex(i)
                val endEntry = dataSet.getEntryForIndex(i + 1)

                // 获取当前点的像素坐标
                val start = trans.getPixelForValues(startEntry?.x!!, startEntry.y)
                val end = trans.getPixelForValues(endEntry?.x!!, endEntry.y)

                // 设置线段颜色
                mPaintLineSegment.color = getColorForValue(startEntry.y)

                // 绘制线段
                c?.drawLine(start.x.toFloat(), start.y.toFloat(),
                    end.x.toFloat(), end.y.toFloat(), mPaintLineSegment)
            }
        }
    }

    // 根据数值返回对应的颜色
    private fun getColorForValue(value: Float): Int {
        return when {
            value < 8 -> Color.RED
            value < 15 -> Color.YELLOW
            else -> Color.GREEN
        }
    }
}



