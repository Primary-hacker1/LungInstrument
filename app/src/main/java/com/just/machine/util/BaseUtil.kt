package com.just.machine.util

object BaseUtil {
    private const val DOUBLE_CLICK_TIME_DELTA = 500L // 双击间隔阈值，单位：毫秒

    private var lastClickTime = 0L

    /**
     * 判断是否是双击事件
     */
    fun isDoubleClick(): Boolean {
        val now = System.currentTimeMillis()
        val timeSinceLastClick = now - lastClickTime
        lastClickTime = now
        return timeSinceLastClick < DOUBLE_CLICK_TIME_DELTA
    }

}