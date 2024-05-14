package com.just.machine.model.dynmicdata


import java.io.Serializable

data class CPXBreathInOutDataBase(
    var EndRealIndex: Int = 0,
    var Tin: Double = 0.0,
    var TempiTotal: Double = 0.0,
    var TempiO2: Double = 0.0,
    var TempiCO2: Double = 0.0,
    var FiTO2: Double = 0.0,
    var Tex: Double = 0.0,
    var TempeTotal: Double = 0.0,
    var TempeO2: Double = 0.0,
    var TempeCO2: Double = 0.0,
    var FeTO2: Double = 0.0,
    var FeTCO2: Double = 0.0,
    var T: Double = 0.0,
    var H: Double = 0.0,
    var P: Double = 0.0
)
