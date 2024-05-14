package com.just.machine.model

import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.model.sixminreport.SixMinReportInfo

data class SixMinReportInfoAndEvaluation(
    val id: Long = 0,
    var reportNo: String = "",//报告编号
    var patientId: Long = 0,//患者id
    var patientName: String = "",//患者姓名
    var patientSix: String = "",//患者性别
    var patientAge: String = "",//患者年龄
    var patientHeight: String = "",//患者身高
    var patientWeight: String = "",//患者体重
    var patientBmi: String = "",//患者BMI
    var patientStride: String = "",//患者步幅
    var clinicalDiagnosis: String = "",//临床诊断
    var medicineUse: String = "",//目前用药
    var predictionDistance: String = "",//预测距离
    var medicalNo: String = "",//病历号
    var medicalHistory: String = "",//病史
    var bsHxl: String = "",//步数/呼吸率，0为步数，1为呼吸率
    var ecgEdition: String = "",//心电版本
    var restDuration: String = "",//休息时长
    var addTime: String = "",//检验日期
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
