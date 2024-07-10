package com.just.machine.model.calibrate

object Definition {
    const val Sample_rate = 100

    var ZeroADC_LDES: Float = 0.1035F // Remove the comment
    var ZeroADC_HDIM: Long = 0

    // High range exhalation calibration coefficient
    var Cur_ADC_OUT_HDIM: Float = 0F

    // High range inhalation calibration coefficient
    var Cur_ADC_IN_HDIM: Float = 0F

    // Low range exhalation calibration coefficient
    var Cur_ADC_OUT_LDES: Float = 0F

    // Low range inhalation calibration coefficient
    var Cur_ADC_IN_LDES: Float = 0F

    var Cur_ADCSMALL_OUT_LDES: Float = 0F // Metabolic low range exhalation calibration coefficient

    var Cur_ADCSMALL_IN_LDES: Float = 0F // Metabolic low range inhalation calibration coefficient

    // Automatic calibration coefficient for the current ADC
    var Cur_ADC_Auto: Float = 0F

    // Noise_AD value
    var Noise_AD: Int = 6

    // Low range sensor upper limit value
    var Pa500_ADC: Int = 0

    // DataRecord value
    var DataRecord: Double = 0.0

    // Ask_Stop flag
    var Ask_Stop: Boolean = false

    // Internal pressure differential sensor intermediate value
    var fzeroAuto: Float = 8212F

    // Low value of the internal pressure differential sensor
    var fzeroLow: Float = 8192F

    // High value of the internal pressure differential sensor
    var fzeroHigh: Float = 8192F

    // Low limit value of ADC
    var LowLimit_AD: Int = -6554

    // High limit value of ADC
    var HighLimit_AD: Int = 6554

    // LDES_AD value
    var LDES_AD: Int = 100

    var sttsumFlow: Float = 0F
}