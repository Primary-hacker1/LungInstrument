package com.just.machine.model.calibration

data class ResultBean(
    var calibrationTime: String? = "",//定标时间
    var inspirationError: String? = "",//吸气准确度误差
    var inhalationError: String? = "",//吸气精确度误差
    var expiratoryAccuracyError: String? = "",//呼气准确度误差
    var expiratoryPrecisionError: String? = "",//呼气精确度误差
    var calibrationResults: String? = "",//定标结果
)
