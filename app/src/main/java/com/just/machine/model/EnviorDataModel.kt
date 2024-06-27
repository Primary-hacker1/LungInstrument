package com.just.machine.model

class EnviorDataModel(data: ByteArray) {

    var T: Double = 0.0 // Temperature
    var H: Double = 0.0 // Humidity
    var P: Double = 0.0 // Pressure

    init {
        if (data.isNotEmpty()) {
            // Temperature calculation
            T = ((data[0].toInt() * 256 + data[1].toInt()) / 10.0).toDouble().round(1)

            // Humidity calculation
            H = ((data[2].toInt() * 256 + data[3].toInt()) / 10.0).toDouble().round(1)

            // Pressure calculation
            val nump =
                (data[4].toInt() * 256 * 256 * 256 + data[5].toInt() * 256 * 256 + data[6].toInt() * 256 + data[7].toInt())
            P = getP2mmHg(nump * 10).toDouble().round(1)
        }
    }

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }

    private fun getP2mmHg(p: Int): Int {
        return (p * 0.0075).toInt()
    }
}