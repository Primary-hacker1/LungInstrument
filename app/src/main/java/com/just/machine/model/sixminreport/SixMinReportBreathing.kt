package com.just.machine.model.sixminreport

/**
 * 6分钟报告呼吸率
 */
data class SixMinReportBreathing(
    val breathingStop: String = "",//静止
    val breathingOne: String = "",//1分钟时的呼吸频率值
    val breathingTwo: String = "",//2分钟时的呼吸频率值
    val breathingThree: String = "",//3分钟时的呼吸频率值
    val breathingFour: String = "",//4分钟时的呼吸频率值
    val breathingFive: String = "",//5分钟时的呼吸频率值
    val breathingSix: String = "",//6分钟时的呼吸频率值
    val breathingBig: String = "",//呼吸频率最大值
    val breathingSmall: String = "",//呼吸频率最小值
    val breathingAverage: String = "",//呼吸频率平均值
    val breathingAll: String = "",//呼吸，每3秒一个值，6分钟的集合
)
