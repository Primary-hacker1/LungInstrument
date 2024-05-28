package com.just.machine.model.lungdata


data class IngredientCalibraCoefficient(
    var ko2: Double = 0.0,
    var bo2: Double = 11376.0,
    var ko2_17: Double = -2.0,
    var bo2_17: Double = 155824.0,
    var kco2: Double = 0.0,
    var bco2: Double = 63760.0,
    var kco2_5: Double = 0.0,
    var bco2_5: Double = 63760.0,
    var co2Offset: Int = 0,
    var o2Offset: Int = 0,
    var o2T90: Int = 0,
    var co2T90: Int = 0
) {
    fun updateTestDataCache(model: TIngredientCalibrate?) {
        if (model == null) return

        ko2 = model.ko2_21
        bo2 = model.bo2_21
        ko2_17 = model.ko2_17
        bo2_17 = model.bo2_17
        kco2 = model.kco2_0
        bco2 = model.bco2_0
        kco2_5 = model.kco2_5
        bco2_5 = model.bco2_5
        co2Offset = model.co2Offset
        o2Offset = model.o2Offset
        o2T90 = model.o2T90
        co2T90 = model.co2T90
    }
}

// Assuming TIngredientCalibrate class is defined somewhere in your project
data class TIngredientCalibrate(
    val ko2_21: Double,
    val bo2_21: Double,
    val ko2_17: Double,
    val bo2_17: Double,
    val kco2_0: Double,
    val bco2_0: Double,
    val kco2_5: Double,
    val bco2_5: Double,
    val co2Offset: Int,
    val o2Offset: Int,
    val o2T90: Int,
    val co2T90: Int
)
