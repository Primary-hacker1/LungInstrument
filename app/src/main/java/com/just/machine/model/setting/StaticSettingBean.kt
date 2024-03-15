package com.just.machine.model.setting


/**
 *create by 2024/3/15
 *@author zt
 */
data class StaticSettingBean(

    var xTimeSvc: String? = "",
    var yTimeUpSvc: String? = "",
    var yTimeDownSvc: String? = "",

    var xTimeFvc: String? = "",
    var yTimeUpFvc: String? = "",
    var yTimeDownFvc: String? = "",

    var xTimeMvv: String? = "",
    var yTimeUpMvv: String? = "",
    var yTimeDownMvv: String? = "",

    var radioVt: Boolean? = true,//是否显示vt
    var radioVideoAutoplaySvc: Boolean? = true,//是否自动播放
    var radioPredictionRing: Boolean? = true,//预测值环是否显示
    var radioVideoAutoplayFvc: Boolean? = true,//是否自动播放
    var radioStartRuler: Boolean? = true,//开始结算标尺是否显示
    var radioVentilationCurve: Boolean? = true,//通气曲线是否显示
    var radioVideoAutoplayMvv: Boolean? = true,//是否自动播放


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

