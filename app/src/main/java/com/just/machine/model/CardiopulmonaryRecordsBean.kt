package com.just.machine.model

/**
 * 表示心肺记录的数据类。
 *
 * @property createTime 测试时间，默认为 "0"。
 * @property conventionalVentilation 常规通气量，默认为 "0"。
 * @property forcedVitalCapacity 用力肺活量，默认为 "0"。
 * @property maximumVentilation 最大通气量，默认为 "0"。
 * @property exerciseLungTest 运动肺测试结果，默认为 "0"。
 * @property assess 心肺功能评估，默认为 "0"。
 */
data class CardiopulmonaryRecordsBean(
    var createTime: String? = "0", // 测试时间
    var conventionalVentilation: String? = "0", // 常规通气量
    var forcedVitalCapacity: String? = "0", // 用力肺活量
    var maximumVentilation: String? = "0", // 最大通气量
    var exerciseLungTest: String? = "0", // 运动肺测试结果
    var assess: String? = "0", // 心肺功能评估
)
