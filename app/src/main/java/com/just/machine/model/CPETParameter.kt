package com.just.machine.model

/**
 * 表示心肺运动试验（CPET）参数的数据类。
 *
 * @property id 参数的唯一标识符，默认为 0。
 * @property parameterName 参数名称，默认为 null。
 * @property parameterNameCH 参数中文名称，默认为 null。
 * @property parameterType 参数类型，默认为 null。
 * @property lowRange 参数的低范围值，默认为 0.0。
 * @property highRange 参数的高范围值，默认为 null。
 * @property unit 参数的单位，默认为 null。
 * @property isShow 是否显示该参数，默认为 null。
 * @property isReport 是否在报告中包含该参数，默认为 null。
 * @property remark 参数的备注信息，默认为 null。
 */
data class CPETParameter(
    var id: Int = 0, // 参数的唯一标识符
    var parameterName: String? = null, // 参数名称
    var parameterNameCH: String? = null, // 参数中文名称
    var parameterType: String? = null, // 参数类型
    var lowRange: Double? = 0.0, // 参数的低范围值
    var highRange: Double? = null, // 参数的高范围值
    var unit: String? = null, // 参数的单位
    var isShow: Boolean? = null, // 是否显示该参数
    var isReport: Boolean? = null, // 是否在报告中包含该参数
    var remark: String? = null // 参数的备注信息
)
