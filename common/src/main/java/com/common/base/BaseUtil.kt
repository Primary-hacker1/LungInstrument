package com.common.base

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

    fun generateLogEntryWithRectangleBorder(log: String, width: Int = 50, horizontalBorder: String = "—", verticalBorder: String = "|", fill: String = " "): String {
        val horizontalLine = horizontalBorder.repeat(width) // 生成横向边框线
        val verticalSpace = "$verticalBorder${fill.repeat(width - 2)}$verticalBorder" // 生成纵向边框间的填充
        val filledLog = "$verticalBorder${fill.repeat((width - 2 - log.length) / 2)}$log${fill.repeat((width - 1 - log.length) / 2)}$verticalBorder" // 添加填充后的日志字符串
        return "$horizontalLine\n$verticalSpace\n$filledLog\n$verticalSpace\n$horizontalLine" // 组合成完整的带矩形边框的日志条目
    }

    fun bytes2HexStr(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = hexChar(v ushr 4)
            hexChars[i * 2 + 1] = hexChar(v and 0x0F)
        }
        return String(hexChars)
    }

    private fun hexChar(nibble: Int): Char {
        return if (nibble < 10) {
            ('0'.toInt() + nibble).toChar()
        } else {
            ('A'.toInt() - 10 + nibble).toChar()
        }
    }



}