package com.just.machine.model.sixminreport

/**
 * 6分钟报告步速
 */
data class SixMinReportStride(
    val strideStop: String = "",//静止
    val strideOne: String = "",//1分钟时的步速值
    val strideTwo: String = "",//2分钟时的步速值
    val strideThree: String = "",//3分钟时的步速值
    val strideFour: String = "",//4分钟时的步速值
    val strideFive: String = "",//5分钟时的步速值
    val strideSix: String = "",//6分钟时的步速值
    val strideBig: String = "",//步速最大值
    val strideSmall: String = "",//步速最小值
    val strideAverage: String = ""//步速平均值
)
