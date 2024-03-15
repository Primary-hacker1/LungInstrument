package com.just.machine.model.setting

/**
 *create by 2024/3/15
 *@author zt
 */
data class StaticSettingBean(
    val svc: String,
    val vitalCapacity: String,//呼吸肺活量
    val unit: String,//单位
    var isSelected: Boolean = false,//是否显示
)

data class SvcSettingBean(
    val svc: String? = "",
    val vitalCapacity: String? = "",//呼吸肺活量
    val unit: String? = "",//单位
    var isSelected: Boolean? = false,//是否显示
)

data class FvcSettingBean(
    val svc: String? = "",
    val vitalCapacity: String? = "",//呼吸肺活量
    val unit: String? = "",//单位
    var isSelected: Boolean? = false,//是否显示
)

data class MvvSettingBean(
    val svc: String? = "",
    val vitalCapacity: String? = "",//呼吸肺活量
    val unit: String? = "",//单位
    var isSelected: Boolean? = false,//是否显示
)

