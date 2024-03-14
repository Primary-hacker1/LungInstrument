package com.justsafe.libview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2


/**
 * 斜率计算曲线
 * Created by zhang on 2024/3/14
 */
class CustomDrawView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val pointPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val linePaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }

    private val linePath = Path()

    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f
    private var angle = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制两个点
        canvas.drawCircle(startX, startY, 20f, pointPaint)
        canvas.drawCircle(endX, endY, 20f, pointPaint)

        // 绘制斜线
        linePath.reset()
        linePath.moveTo(startX, startY)
        linePath.lineTo(endX, endY)
        canvas.drawPath(linePath, linePaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 手指按下时，更新起始点位置
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                // 手指移动时，更新结束点位置
                endX = event.x
                endY = event.y
                // 计算斜线的角度
                angle = atan2(endY - startY, endX - startX)
                // 重新绘制
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                // 手指抬起时，重置位置
                startX = 0f
                startY = 0f
                endX = 0f
                endY = 0f
                invalidate()
            }
        }
        return true
    }

    // 获取斜线的角度
    fun getAngle(): Float {
        return angle
    }
}