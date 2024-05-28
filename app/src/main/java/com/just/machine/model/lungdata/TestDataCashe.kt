package com.just.machine.model.lungdata

import com.just.machine.dao.lung.CPXBreathInOutData
import java.lang.Math

object TestDataCashe {

    var T = 250.0
    var H = 20.0
    var P = 760.0
    var ko2 = 1.0
    var kco2 = 1.0
    var T90Or = 50.0
    private val saturationPressure = doubleArrayOf(
        4.58,
        4.62,
        4.65,
        4.68,
        4.72,
        4.75,
        4.79,
        4.82,
        4.86,
        4.89,
        4.93,
        4.96,
        5.0,
        5.03,
        5.07,
        5.11,
        5.14,
        5.18,
        5.22,
        5.25,
        5.29,
        5.33,
        5.37,
        5.41,
        5.45,
        5.48,
        5.52,
        5.56,
        5.6,
        5.64,
        5.68,
        5.72,
        5.76,
        5.8,
        5.85,
        5.89,
        5.93,
        5.94,
        6.01,
        6.06,
        6.1,
        6.14,
        6.18,
        6.23,
        6.27,
        6.32,
        6.36,
        6.4,
        6.45,
        6.5,
        6.54,
        6.59,
        6.63,
        6.68,
        6.73,
        6.77,
        6.82,
        6.87,
        6.91,
        6.96,
        7.01,
        7.06,
        7.11,
        7.16,
        7.21,
        7.26,
        7.31,
        7.36,
        7.41,
        7.46,
        7.51,
        7.56,
        7.61,
        7.67,
        7.72,
        7.77,
        7.83,
        7.88,
        7.93,
        7.99,
        8.1,
        8.15,
        8.21,
        8.26,
        8.32,
        8.38,
        8.43,
        8.49,
        8.55,
        8.61,
        8.67,
        8.72,
        8.78,
        8.84,
        8.9,
        8.96,
        9.02,
        9.08,
        9.14,
        9.21,
        9.27,
        9.33,
        9.39,
        9.45,
        9.52,
        9.58,
        9.65,
        9.71,
        9.78,
        9.84,
        9.91,
        9.97,
        10.04,
        10.11,
        10.17,
        10.24,
        10.31,
        10.38,
        10.45,
        10.51,
        10.58,
        10.65,
        10.72,
        10.79,
        10.87,
        10.94,
        11.01,
        11.08,
        11.15,
        11.23,
        11.3,
        11.38,
        11.45,
        11.53,
        11.6,
        11.68,
        11.75,
        11.83,
        11.91,
        11.98,
        12.06,
        12.14,
        12.22,
        12.3,
        12.38,
        12.46,
        12.54,
        12.62,
        12.7,
        12.78,
        12.87,
        12.95,
        13.03,
        13.12,
        13.2,
        13.29,
        13.37,
        13.46,
        13.54,
        13.63,
        13.72,
        13.81,
        13.89,
        13.98,
        14.07,
        14.16,
        14.25,
        14.34,
        14.44,
        14.53,
        14.62,
        14.71,
        14.81,
        14.9,
        14.99,
        15.09,
        15.18,
        15.28,
        15.38,
        15.47,
        15.57,
        15.67,
        15.77,
        15.87,
        15.97,
        16.07,
        16.17,
        16.27,
        16.37,
        16.47,
        16.58,
        16.68,
        16.79,
        16.89,
        17.0,
        17.1,
        17.21,
        17.32,
        17.42,
        17.53,
        17.64,
        17.75,
        17.86,
        17.97,
        18.08,
        18.19,
        18.31,
        18.42,
        18.53,
        18.65,
        18.76,
        18.88,
        18.99,
        19.11,
        19.23,
        19.35,
        19.46,
        19.58,
        19.7,
        19.82,
        19.95,
        20.07,
        20.19,
        20.31,
        20.44,
        20.56,
        20.69,
        20.81,
        20.94,
        21.07,
        21.19,
        21.32,
        21.45,
        21.58,
        21.71,
        21.84,
        21.98,
        22.11,
        22.24,
        22.38,
        22.51,
        22.65,
        22.78,
        22.92,
        23.06,
        23.19,
        23.33,
        23.47,
        23.61,
        23.76,
        23.9,
        24.04,
        24.18,
        24.33,
        24.47,
        24.62,
        24.76,
        24.91,
        25.06,
        25.21,
        25.36,
        25.51,
        25.66,
        25.81,
        25.96,
        26.12,
        26.27,
        26.43,
        26.58,
        26.74,
        26.9,
        27.06,
        27.21,
        27.37,
        27.53,
        27.7,
        27.86,
        28.02,
        28.18,
        28.35,
        28.52,
        28.68,
        28.85,
        29.02,
        29.19,
        29.36,
        29.53,
        29.7,
        29.87,
        30.04,
        30.22,
        30.39,
        30.57,
        30.75,
        30.92,
        31.1,
        31.28,
        31.46,
        31.64,
        31.83,
        32.01,
        32.19,
        32.38,
        32.56,
        32.75,
        32.94,
        33.13,
        33.32,
        33.51,
        33.7,
        33.89,
        34.08,
        34.28,
        34.47,
        34.67,
        34.87,
        35.07,
        35.27,
        35.47,
        35.67,
        35.87,
        36.07,
        36.28,
        36.48,
        36.69,
        36.89,
        37.1,
        37.31,
        37.52,
        37.73,
        37.95,
        38.16,
        38.37,
        38.59,
        38.81,
        39.02,
        39.24,
        39.46,
        39.68,
        39.9,
        40.13,
        40.35,
        40.58,
        40.8,
        41.03,
        41.26,
        41.49,
        41.72,
        41.95,
        42.18,
        42.42,
        42.65,
        42.89,
        43.12,
        43.36,
        43.6,
        43.84,
        44.08,
        44.33,
        44.57,
        44.82,
        45.06,
        45.31,
        45.56,
        45.81,
        46.06,
        46.31,
        46.57,
        46.82,
        47.08,
        47.33,
        47.59,
        47.85,
        48.11,
        48.37,
        48.64,
        48.9,
        49.17,
        49.43,
        49.7,
        49.97,
        50.24,
        50.51,
        50.79,
        51.06,
        51.34,
        51.62,
        51.89,
        52.17,
        52.5,
        52.74,
        53.02,
        53.31,
        53.59,
        53.88,
        54.17,
        54.46,
        54.75,
        55.04,
        55.34,
        55.63,
        55.93,
        56.23,
        56.53,
        56.83,
        57.13,
        57.44,
        57.74,
        58.05
    )
    var UserEnvironmentSettingT = 0
    var UserEnvironmentSettingH = 0
    var UserEnvironmentSettingP = 0
    var beforeDyDataModel: CPXDataModel? = null
    var o2high = 0.0
    var co2high = 0.0
    var o2low = 0.0
    var co2low = 0.0
    lateinit var beforeDyBreathInOutData: CPXBreathInOutData
    var dysumFlow = 0.0
    var sttsumFlow = 0.0f
    var isErgoConect = false

//    fun updateEnvironmentSetting() {
//        if (UseUserEnvironmentSettings) {
//            T = UserEnvironmentSettingT.toDouble()
//            H = UserEnvironmentSettingH.toDouble()
//            P = UserEnvironmentSettingP.toDouble()
//        } else {
//            T = 250.0
//            H = 50.0
//            P = 760.0
//        }
//    }
//
//    fun getHistoryEnvironment() {
//        // Assuming DBHelper is a placeholder for a database utility class and GetList is a method to retrieve data
//        val ds = DBHelper<t_environment>.getList({ it.id != -1 }, { it.time })
//        T = ds[0].temperature.toDouble()
//        H = ds[0].humidity.toDouble()
//        P = ds[0].pressure.toDouble()
//    }

    fun getTP(currentTemprate: Int): Double {
        return when {
            currentTemprate <= -20 -> saturationPressure[0] / 1000
            currentTemprate <= -15 -> saturationPressure[1] / 1000
            currentTemprate <= -10 -> saturationPressure[2] / 1000
            currentTemprate <= -5 -> saturationPressure[3] / 1000
            currentTemprate <= 0 -> saturationPressure[4] / 1000
            currentTemprate >= 49 -> saturationPressure[53] / 1000
            else -> saturationPressure[currentTemprate] / 1000
        }
    }

//    fun setUseUserEnvironmentSettings(use: Boolean = true, t: Int = 250, h: Int = 50, p: Int = 765) {
//        val tempT = t * 10
//        UseUserEnvironmentSettings = use
//        if (use) {
//            UserEnvironmentSettingT = tempT
//            UserEnvironmentSettingH = h
//            UserEnvironmentSettingP = p
//        }
//        updateEnvironmentSetting()
//    }

    fun getP2Kpa(): Double {
        return P / 760.0 * 101.325
    }

    fun getP2mmHg(p: Int): Int {
        return (p * 0.0075).toInt()
    }

    fun getP2Kpa(p: Double): Double {
        return p / 760.0 * 101.325
    }

//    fun calculeCyclelistOffset(): IntArray {
//        return if (co2offset > o2offset) {
//            intArrayOf(0, co2offset - o2offset, co2offset)
//        } else {
//            intArrayOf(o2offset - co2offset, 0, o2offset)
//        }
//    }
//
//    fun getRealO2(o23: Double): Double {
//        return ko2 * o23 + bo2
//    }
//
//    fun getRealCO2(co23: Double): Double {
//        return kco2 * co23 + bco2
//    }

    fun getP0(currentTemprate: Int): Double {
        return if (currentTemprate >= 410) saturationPressure[409] else saturationPressure[currentTemprate]
    }

    fun accelerateWave(first: Double, Second: Double, Third: Double, t90: Double): Double {
        val t = t90 * 0.005 / (Math.log(0.9) - Math.log(0.1))
        val K1 = t / 4
        val K2 = t / 5
        val D1 = (Third - first) / 2 / 0.005
        val D2 = (((Third - Second) / 0.005 - (Second - first) / 0.005)) / 0.005
        return first + (K1 + K2) * D1 + K1 * K2 * D2
    }

    fun newAccelerateWave(testValue: Double, D1: Double, D2: Double, t90: Double): Double {
        val t = t90 * 0.005 / (Math.log(0.9) - Math.log(0.1))
        val K1 = t / 3
        val K2 = t / 3
        return testValue + (K1 + K2) * D1 + K1 * K2 * D2
    }

    fun newO2AccelerateWave(testValue: Double, D1: Double, D2: Double, t90: Double): Double {
        val t = t90 * 0.005 / (Math.log(0.9) - Math.log(0.1))
        val K1 = t / 3
        val K2 = t / 5
        return testValue + (K1 + K2) * D1 + K1 * K2 * D2
    }
}
