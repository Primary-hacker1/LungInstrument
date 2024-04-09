package com.just.machine.model.sixminreport

/**
 * 6分钟报告报警
 */
data class SixMinReportAlarm(
    val alarmTime: String = "",// 报警时间
    val alarmType: String = "",// 报警属性1.心率，2.血氧
    val alarmValue: String = "",// 报警值
)
