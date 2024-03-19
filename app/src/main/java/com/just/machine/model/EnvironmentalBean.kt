package com.just.machine.model

data class EnvironmentalBean(
    var calibrationTime: String? = "",
    var conventionalVentilation: String? = "",

    var humidity: String? = "",
    var atmosphericPressure: String? = "",
    var calibrationMethod: String? = "",
)
