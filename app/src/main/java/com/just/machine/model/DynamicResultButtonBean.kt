package com.just.machine.model

import android.graphics.drawable.Drawable

/**
 * 表示动态结果按钮的数据类。
 *
 * @property resultBtnIcon 按钮的图标，默认为 null。
 * @property resultBtnName 按钮的名称，默认为空字符串 ""。
 * @property isClick 按钮是否被选中，默认为 false。
 * @property isVisible 按钮是否显示，默认为 true。
 */
data class DynamicResultButtonBean(
    var resultBtnIcon: Drawable? = null, // 按钮的图标
    var resultBtnName: String? = "", // 按钮的名称
    var isClick: Boolean? = false, // 按钮是否被选中
    var isVisible: Boolean? = true // 按钮是否显示
)
