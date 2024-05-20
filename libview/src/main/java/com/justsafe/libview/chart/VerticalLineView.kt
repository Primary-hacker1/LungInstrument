package com.justsafe.libview.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import com.justsafe.libview.R

class VerticalLineView(context: Context) : View(context) {
    private val paint = Paint()
    private val circlePaint = Paint()
    private var circleRadius = 10f
    private var circleCenterX = 0f

    init {
        paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        paint.strokeWidth = 2f

        circlePaint.color = ContextCompat.getColor(context,R.color.colorPrimary)
        circlePaint.style = Paint.Style.STROKE // 设置为画线模式
        circlePaint.strokeWidth = 4f // 设置圆圈线条的宽度
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawLine(circleCenterX, 0f, circleCenterX, height.toFloat(), paint)
        canvas?.drawCircle(circleCenterX, height / 2.toFloat(), circleRadius, circlePaint) // 绘制空心圆
    }

    fun setXPosition(x: Float) {
        circleCenterX = x
        invalidate()
    }
}
