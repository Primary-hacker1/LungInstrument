package com.justsafe.libview.chart

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView

class CustomHorizontalScrollView : HorizontalScrollView {
    private var lastX: Float = 0.0f
    private var isScrolling: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.x
                // 让父级视图不拦截触摸事件
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = ev.x - lastX
                if (!isScrolling && Math.abs(deltaX) > 50) {
                    // 如果水平滑动距离大于50像素，则认为是水平滑动，禁止父级视图拦截触摸事件
                    parent.requestDisallowInterceptTouchEvent(true)
                    isScrolling = true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 恢复父级视图的触摸事件拦截
                parent.requestDisallowInterceptTouchEvent(false)
                isScrolling = false
            }
        }
        return super.onTouchEvent(ev)
    }
}
