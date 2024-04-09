package com.just.machine.model.sixminreport

data class SixMinReportHeartBeat(
    val heartBeatStop: String = "",//静止
    val heartBeatOne: String = "",//1分钟时的心率值
    val heartBeatTwo: String = "",//2分钟时的心率值
    val heartBeatThree: String = "",//3分钟时的心率值
    val heartBeatFour: String = "",//4分钟时的心率值
    val heartBeatFive: String = "",//5分钟时的心率值
    val heartBeatSix: String = "",//6分钟时的心率值
    val heartBeatBig: String = "",//心率最大值
    val heartBeatSmall: String = "",//心率最小值
    val heartBeatAverage: String = "",//心率平均值
    val heartBeatAll: String = "",//心率，每秒一个值，6分钟的集合
    val heartBeatAllData: String = "",//6分钟所有的心率集合
    val heartBeatRestore: String = "",//运动心率恢复值
    val heartBeatConclusion: String = "",//心电结论
)