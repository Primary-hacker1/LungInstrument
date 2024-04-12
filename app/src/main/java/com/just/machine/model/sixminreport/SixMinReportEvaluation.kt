package com.just.machine.model.sixminreport

/**
 * 6分钟报告综合评估
 */
data class SixMinReportEvaluation(
    val fatigueLevelBefore:String = "",//步行前，疲劳量级
    val breathingLevelBefore:String = "",//步行前，呼吸量级
    val fatigueLevel:String = "",//疲劳量级
    val breathingLeve:String = "",//呼吸量级
    val circleNumber:String = "",//运动圈数
    val unfinishedDistance:String = "",//最后一圈还剩的距离(米)
    val totalWalk:String = "",//总步数
    val totalDistance:String = "",//总距离(米)
    val accounted:String = "",//实际运动距离占预测的百分比
    val metabEquivalent:String = "",//   * 代谢当量 公式：(4.928 + 0.023 *距离) / 3.5
    val cardiopuLevel:String = "",// * 心肺功能等级 等级标准： 1级：<=299.9m 2级：300~375m 3级：375.1~450m 4级：>=450.1m
    val cardiopuDegree:String = "",//    * 心功能严重程度,1=重度，2=中度，3=轻度 重度：<=149.9m 中度：150~450m 轻度：>=450.1m
)
