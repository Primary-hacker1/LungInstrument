package com.just.machine.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class SixMinRecordsBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var reportId: String? = "0",//报告主id
    var reportNo: String? = "",//报告编号
    var testTime: String? = "",//测试日期
    var movementWay: String? = "0",//运动方式，0为步行，1为跑步
    var movementTime: String? = "",//运动时间
    var movementDistance: String? = "0.0",//运动距离
    var movementSteps: String? = "0",//运动步数
    var movementNumber: String? = "0",//每周运动次数
    var movementCycle: String? = "0",//运动周期
    var cycleUnit: String? = "0",//运动周期单位，0为周，1为月，2为年
    var heartBig: String? = "0",//运动周期单位，0为周，1为月，2为年
    var heartBefore: String? = "0", //'最佳心率区间，前
    var heartAfter: String? = "0", //最佳心率区间，后
    var tiredControl: String? = "", //疲劳控制
    var strengthBefore: String? = "0", //'运动强度区间 前
    var strengthAfter: String? = "0", //运动强度区间 后
    var recommend: String? = "", //'医生建议'
    var recommendName: String? = "", //建议医生（者）
    var recommendNameYs: String? = "", //建议医师（者）
    var tiredLevelBefore: String? = "4", //疲劳程度控制 前,
    var tiredLevelAfter: String? = "6", //'疲劳程度控制 后,
    var systoBloodBefore: String? = "90", //收缩压，前,
    var systoBloodAfter: String? = "140", //收缩压，后,
    var diastBloodBefore: String? = "60", //舒张压，前,
    var diastBloodAfter: String? = "90", //舒张压，后,
    var oxygen: String? = "95",//血氧
    var strengthVar: String? = "", //运动强度控制(自编辑)
    var heart: String? = "",//最佳心率(自编辑)
    var tiredLevel: String? = "",//自觉疲劳程度(自编辑)
    var periodic: String? = "", //运动节律(自编辑),
    var cycle: String? = "", //运动周期(自编辑)
    var remarkDown: String? = "",//注意事项下面(自编辑),
    var updateState: String? = "0", //'编辑状态，0=未编辑，1=编辑,
    var strideBefore: String? = "0.0",// 运动步速区间，前,
    var strideAfter: String? = "0.0",//运动步速区间，后,
    var strideFormula: String? = "0",// 运动步速计算公式，0=50~60%，1=70~80%,
    var movementDistanceAfter: String? = "0.0",//运动距离，后,
    var distanceFormula: String? = "0", //推荐距离公式，0=50~60%，1=70~80%,
    var distanceState: String? = "1", //运动步速和推荐距离的状态，1=出具，2=不出具,
    var heartRate: String? = "",//运动心率,
    var heartRateState: String? = "1",//运动心率的状态，1=出具，2=不出具,
    var metabMet: String? = "", // 代谢当量,
    var metabState: String? = "1", // 代谢当量的状态，1=出具，2=不出具,
    var tiredState: String? = "1",// borg疲劳的状态，1=出具，2=不出具,
    var prescripState: String? = "0",//处方状态，0=强度版本，1=运动步速版本,
    var delFlag: String? = "0", // 删除标记,
    var reserveOne: String? = "0", // 预留字段,
    var reserveTwo: String? = "0",// 预留字段,
    var reserveThree: String? = "0", // 预留字段,
    var reserveFour: String? = "0", // 预留字段,
    var reserveFive: String? = "0" // 预留字段,
)
