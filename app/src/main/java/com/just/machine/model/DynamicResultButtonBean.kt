package com.just.machine.model

import android.graphics.drawable.Drawable

data class DynamicResultButtonBean(
    var resultBtnIcon: Drawable? = null,//图标
    var resultBtnName: String? = "",//名称
    var isClick: Boolean? = false,//是否选中
    var isVisible: Boolean? = true//是否显示
)