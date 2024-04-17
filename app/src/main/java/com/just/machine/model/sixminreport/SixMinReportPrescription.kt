package com.just.machine.model.sixminreport

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.just.machine.dao.ChatItemConverter

/**
 * 6分钟报告运动处方
 */
@Entity(tableName = "sixmin_report_prescription")
@TypeConverters(ChatItemConverter::class)
data class SixMinReportPrescription(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Long = 0,
    var reportId: String = "",//报告主id
    var movementWay: String = "0",//运动方式，0为步行，1为跑步
    var movementTime: String = "",//运动时间
    var movementDistance: String = "",//运动距离(米)
    var movementSteps: String = "",//运动步数
    var movementWeeklyNumber: String = "",//每周运动次数
    var movementCycle: String = "",//运动周期
    var cycleUnit: String = "",//运动周期单位，0为周，1为月，2为年
    var heartBigg: String = "",//最大心率
    var heartBefore: String = "",//最佳心率区间，前
    var heartAfter: String = "",//最佳心率区间，后
    var pilaoControl: String = "",//疲劳控制
    var qiangduBefore: String = "",//运动强度区间 前
    var qiangduAfter: String = "",//运动强度区间 后
    var remarke: String = "",//医生建议
    var remarkeName: String = "",//建议医生（者）
    var remarkeNameYs: String = "",// 建议医师（者）
    var pllevBefore: String = "",//疲劳程度控制 前
    var pllevAfter: String = "",//疲劳程度控制 后
    var strideBefore: String = "",//运动步速区间，前
    var strideAfter: String = "",//运动步速区间，后
    var strideFormula: String = "",//运动步速计算公式，0=50~60%，1=70~80%
    var movementDistanceAfter: String = "",//运动距离，后
    var distanceFormula: String = "",//推荐距离公式，0=50~60%，1=70~80%
    var distanceState: String = "",//运动步速和推荐距离的状态，1=出具，2=不出具
    var heartrateRate: String = "",// 运动心率
    var heartrateState: String = "",// 运动心率的状态，1=出具，2=不出具
    var metabMet: String = "",//代谢当量
    var metabState: String = "",//代谢当量的状态，1=出具，2=不出具
    var pllevState: String = "",//borg疲劳的状态，1=出具，2=不出具
    var prescripState: String = "",//处方状态，0=强度版本，1=运动步速版本
    var delFlag: String = "",//删除标记
)