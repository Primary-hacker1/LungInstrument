package com.just.machine.model.sixminreport

/**
 * 6分钟报告心率失常
 */
data class SixMinReportDisorder(
    val disorderTime:String = "",//失常时间
    val disorderValue:String = "",//失常值
)
