package com.just.machine.model.sixminreport

/**
 * 6分钟报告心电数据
 */
data class SixMinHeartEcg(
    val bigHeartEcgTime: String = "",//最快心率时间
    val bigHeartEcgCountdown: String = "",//最快心率正计时
    val bigHeartEcg: String = "",//最快心率的心电数据
    val bigHeart: String = "",//最快心率值
    val smallHeartEcgTime: String = "",//最慢心率时间
    val smallHeartEcgCountdown: String = "",//最慢心率正计时
    val smallHeartEcg: String = "",//最慢心率的心电数据
    val smallHeart: String = "",//最慢心率值
    val jietuOr: String = "",//是否截图，0为未截图，1为已截图
    val heartTime: String = "",//心率异常/截图时间
    val heartEcg: String = "",//心率异常/截图心电数据
    val bigHeartLong: String = "",//心率异常/截图心电数据
    val smallHeartLong: String = "",//心率异常/截图心电数据
    val heartLong: String = "",//心率异常/截图心电数据
    val heartRate: String = "",//截取心率值
)
