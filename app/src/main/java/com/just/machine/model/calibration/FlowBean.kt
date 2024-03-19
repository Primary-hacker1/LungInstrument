package com.just.machine.model.calibration

data class FlowBean(
    var inspiratoryIndicators: String? = "",//吸气标准指标
    var calibratedValue: String? = "",//标定值
    var actual: String? = "",//实际值
    var errorRate: String? = "",//误差率
    var calibrationResults: String? = "",//标定结果
)
