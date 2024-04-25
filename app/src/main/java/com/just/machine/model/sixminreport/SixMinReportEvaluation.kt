package com.just.machine.model.sixminreport

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.just.machine.dao.ChatItemConverter

/**
 * 6分钟报告综合评估
 */
@Entity(tableName = "sixmin_report_evaluation")
@TypeConverters(ChatItemConverter::class)
data class SixMinReportEvaluation(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Long = 0,
    var reportId: String = "",//报告主id
    var befoFatigueLevel:String = "0",//步行前，疲劳量级
    var befoBreathingLevel:String = "0",//步行前，呼吸量级
    var fatigueLevel:String = "",//疲劳量级
    var breathingLevel:String = "",//呼吸量级
    var turnsNumber:String = "0",//运动圈数
    var unfinishedDistance:String = "0",//最后一圈还剩的距离(米)
    var totalWalk:String = "0",//总步数
    var totalDistance:String = "0",//总距离(米)
    var accounted:String = "",//实际运动距离占预测的百分比
    var metabEquivalent:String = "",//   * 代谢当量 公式：(4.928 + 0.023 *距离) / 3.5
    var cardiopuLevel:String = "",// * 心肺功能等级 等级标准： 1级：<=299.9m 2级：300~375m 3级：375.1~450m 4级：>=450.1m
    var cardiopuDegree:String = "",//    * 心功能严重程度,1=重度，2=中度，3=轻度 重度：<=149.9m 中度：150~450m 轻度：>=450.1m
    var fieldLength:String = "",//场地长度
    var delFlag: String = "0",//删除标记
)
