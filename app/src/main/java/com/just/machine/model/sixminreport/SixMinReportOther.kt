package com.just.machine.model.sixminreport

/**
 * 6分钟报告其他信息
 */
data class SixMinReportOther(
    val startHighPressure: String = "",//开始高压
    val startLowPressure: String = "",//开始低压
    val stopHighPressure: String = "",//结束高压
    val stopLowPressure: String = "",//结束低压
    val stopOr: String = "",//是否提前停止试验，0为否，1为是
    val stopTime: String = "",//停止时间
    val stopReason: String = "",//停止原因(症状描述)
    val badOr: String = "",//自动结束后是否有不良症状，0为否，1为是
    val badSymptoms: String = "",//自动结束后的不良症状描述
    val useName: String = "",//使用单位名称
    val ecgType: String = "",//导联数量
)
