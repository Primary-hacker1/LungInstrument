package com.just.machine.model.sixminreport

/**
 * 6分钟报告步数
 */
data class SixMinReportWalk(
    val walkStop: String = "",//静止
    val walkOne: String = "",//1分钟时的步数值
    val walkTwo: String = "",//2分钟时的步数值
    val walkThree: String = "",//3分钟时的步数值
    val walkFour: String = "",//4分钟时的步数值
    val walkFive: String = "",//5分钟时的步数值
    val walkSix: String = "",//6分钟时的步数值
    val walkBig: String = "",//步数最大值
    val walkSmall: String = "",//步数最小值
    val walkAverage: String = ""//步数平均值
)
