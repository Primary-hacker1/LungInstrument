package com.justsafe.libview.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CustomDrawView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path: Path = Path()
    private val point1: PointF = PointF(100f, 300f)
    private val point2: PointF = PointF(500f, 300f)
    private var movingPoint: PointF? = null

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制两个点
        canvas.drawCircle(point1.x, point1.y, 20f, paint)
        canvas.drawCircle(point2.x, point2.y, 20f, paint)

        // 绘制两个垂直线
        canvas.drawLine(point1.x, 0f, point1.x, point1.y - 200, paint)
        canvas.drawLine(point2.x, 0f, point2.x, point2.y - 200, paint)

        // 绘制曲线
        path.reset()
        path.moveTo(point1.x, point1.y)
        path.quadTo((point1.x + point2.x) / 2, 0f, point2.x, point2.y)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isTouchingPoint(point1, event.x, event.y)) {
                    movingPoint = point1
                } else if (isTouchingPoint(point2, event.x, event.y)) {
                    movingPoint = point2
                }
            }
            MotionEvent.ACTION_MOVE -> {
                movingPoint?.apply {
                    x = event.x
                    y = event.y
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                movingPoint = null
            }
        }
        return true
    }

    private fun isTouchingPoint(point: PointF, x: Float, y: Float): Boolean {
        val dx = x - point.x
        val dy = y - point.y
        return dx * dx + dy * dy <= 400 // 点的点击范围为以其为中心的半径为20的圆形区域
    }
}
