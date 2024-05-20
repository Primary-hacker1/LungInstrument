package com.just.machine.model.lungdata

import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

object CPXCalBase {

    inline fun <reified T2> getNearlist(compare: Double, list: List<T2>, func: (T2) -> Double): T2? {
        if (list.isEmpty()) return null
        var obj1: T2? = null
        var num = Double.MAX_VALUE
        for (obj2 in list) {
            if (abs(func(obj2) - compare) < num) {
                num = abs(func(obj2) - compare)
                obj1 = obj2
            }
        }
        return obj1
    }

    fun calcFlowIn(F: Int, p: Double, T: Int, H: Double, K: Double): Double {
        val a = sqrt(F.toDouble()) * K
        return atpBtpsBreathIn(a, T, p, H)
    }

    fun calcFlowOut(F: Int, p: Double, T: Int, K: Double): Double {
        val a = sqrt(F.toDouble()) * K
        return atpBtpsBreathOut(a, T, p)
    }

    fun atpAtpd(F: Double, p: Double, T: Int, H: Double): Double {
        return F * (p / (p - TestDataCashe.getP0(T) * H / 100.0))
    }

    fun atpAtpdBtps(F: Double, p: Double): Double {
        return F * (p - 47.08) / p
    }

    fun atpAtpdBtpsIn(F: Double, p: Double, T: Int): Double {
        return F * (p - TestDataCashe.getP0(T)) / p
    }

    fun atpBtpsBreathIn(F: Double, T: Int, P: Double, H: Double = 100.0): Double {
        val t = TestDataCashe.getP0(T)
        return F * 310.15 / (273.15 + T / 10.0) * (P - t * H / 100.0) / (P - 47.08)
    }

    fun atpBtpsBreathCpxIn(F: Double, T: Int, P: Double, H: Double = 100.0): Double {
        val t = TestDataCashe.getP0(T)
        return F * 310.15 / (273.15 + T) * (P - TestDataCashe.getP0(T) * H) / (P - 47.08)
    }

    fun atpBtpsBreathOut(F: Double, T: Int, P: Double): Double {
        val a = 370.0 - (370.0 - T) / 3.0
        return F * 310.15 / (273.15 + a / 10.0) * (P - TestDataCashe.getP0(a.roundToInt())) / (P - 47.08)
    }

    fun stpd(V: Double, P: Double): Double {
        return V * 273.15 / 310.15 * P / 760.0
    }
}
