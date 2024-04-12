package com.just.machine.model.sixminreport

/**
 * 6分钟报告运动处方
 */
data class SixMinReportPrescription(
    val movementWay: String = "0",//运动方式，0为步行，1为跑步
    val movementTime: String = "",//运动时间
    val movementDistance: String = "",//运动距离(米)
    val movementSteps: String = "",//运动步数
    val movementWeeklyNumber: String = "",//每周运动次数
    val movementCycle: String = "",//运动周期
    val cycleUnit: String = "",//运动周期单位，0为周，1为月，2为年
    val heartBig: String = "",//最大心率
    val heartBefore: String = "",//最佳心率区间，前
    val heartAfter: String = "",//最佳心率区间，后
    val tiredControl: String = "",//疲劳控制
    val strengthBefore: String = "",//运动强度区间 前
    val strengthAfter: String = "",//运动强度区间 后
    val remark: String = "",//医生建议
    val remarkName: String = "",//建议医生（者）
    val remarkDoctorName: String = "",// 建议医师（者）
    val tiredControlBefore: String = "",//疲劳程度控制 前
    val tiredControlAfter: String = "",//疲劳程度控制 后
    val strideBefore: String = "",//运动步速区间，前
    val strideAfter: String = "",//运动步速区间，后
    val strideFormula: String = "",//运动步速计算公式，0=50~60%，1=70~80%
    val movementDistanceAfter: String = "",//运动距离，后
    val distanceFormula: String = "",//推荐距离公式，0=50~60%，1=70~80%
    val distanceState: String = "",//运动步速和推荐距离的状态，1=出具，2=不出具
    val heartBeat: String = "",// 运动心率
    val heartBeatState: String = "",// 运动心率的状态，1=出具，2=不出具
    val metabMet: String = "",//代谢当量
    val metabState: String = "",//代谢当量的状态，1=出具，2=不出具
    val tiredLevelState: String = "",//borg疲劳的状态，1=出具，2=不出具
    val prescriptionState: String = "",//处方状态，0=强度版本，1=运动步速版本
)