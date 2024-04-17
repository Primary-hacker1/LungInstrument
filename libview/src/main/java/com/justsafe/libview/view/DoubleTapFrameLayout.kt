package com.justsafe.libview.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import kotlin.math.abs

class DoubleTapFrameLayout : FrameLayout {
    private var onDoubleTapListener: OnDoubleTapListener? = null

    private var lastClickTime: Long = 0
    private var lastClickX: Float = 0f
    private var lastClickY: Float = 0f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val clickTime = System.currentTimeMillis()
                val clickX = event.x
                val clickY = event.y

                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA &&
                    abs(clickX - lastClickX) < DOUBLE_CLICK_MAX_DISTANCE &&
                    abs(clickY - lastClickY) < DOUBLE_CLICK_MAX_DISTANCE
                ) {
                    // 双击事件触发
                   return onDoubleTap()
                }

                lastClickTime = clickTime
                lastClickX = clickX
                lastClickY = clickY
            }
        }
        return super.onTouchEvent(event)
    }

    private fun onDoubleTap() : Boolean {
        // 执行双击事件操作
        // 这里可以添加你的双击事件处理逻辑
        onDoubleTapListener?.onDoubleTap()
        return true
    }

    companion object {
        private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 // 双击时间间隔
        private const val DOUBLE_CLICK_MAX_DISTANCE: Float = 100f // 最大点击距离
    }

    fun setOnDoubleTapListener(listener: OnDoubleTapListener) {

        onDoubleTapListener = listener
    }

    // 定义双击事件监听器接口
    interface OnDoubleTapListener {
        fun onDoubleTap()
    }
}
