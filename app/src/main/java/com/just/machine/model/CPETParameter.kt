package com.just.machine.model

data class CPETParameter(
    var id: Int = 0,
    var parameterName: String? = null,
    var parameterNameCH: String? = null,
    var parameterType: String? = null,
    var lowRange: Double? = null,
    var highRange: Double? = null,
    var unit: String? = null,
    var isShow: Boolean? = null,
    var isReport: Boolean? = null,
    var remark: String? = null
)