package com.just.machine.model.sixminreport

/**
 * 6分钟报告血氧
 */
data class SixMinBloodOxygen(
    val bloodOxygenStop: String = "",//静止
    val bloodOxygenOne: String = "",//1分钟时的血氧值
    val bloodOxygenTwo: String = "",//2分钟时的血氧值
    val bloodOxygenThree: String = "",//3分钟时的血氧值
    val bloodOxygenFour: String = "",//4分钟时的血氧值
    val bloodOxygenFive: String = "",//5分钟时的血氧值
    val bloodOxygenSix: String = "",//6分钟时的血氧值
    val bloodOxygenBig: String = "",//血氧最大值
    val bloodOxygenSmall: String = "",//血氧最小值
    val bloodOxygenAverage: String = "",//血氧平均值
    val bloodOxygenAll: String = "",//血氧，每秒一个值，6分钟的集合
)
