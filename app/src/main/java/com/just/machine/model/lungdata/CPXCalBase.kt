package com.just.machine.model.lungdata

import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

object CPXCalBase {

    // 获取与给定值最接近的列表项
    inline fun <reified T2> getNearlist(compare: Double, list: List<T2>, func: (T2) -> Double): T2? {
        if (list.isEmpty()) return null // 如果列表为空，返回null
        var closestItem: T2? = null // 最接近的项
        var minDifference = Double.MAX_VALUE // 最小差值，初始值为最大双精度值
        for (item in list) { // 遍历列表
            val difference = abs(func(item) - compare) // 计算当前项与目标值的差值
            if (difference < minDifference) { // 如果差值更小
                minDifference = difference // 更新最小差值
                closestItem = item // 更新最接近的项
            }
        }
        return closestItem // 返回最接近的项
    }

    // 计算吸气流量
    fun calcFlowIn(F: Int, p: Double, T: Int, H: Double, K: Double): Double {
        val a = sqrt(F.toDouble()) * K // 计算流量系数
        return atpBtpsBreathIn(a, T, p, H) // 转换为标准温压状态下的流量
    }

    // 计算呼气流量
    fun calcFlowOut(F: Int, p: Double, T: Int, K: Double): Double {
        val a = sqrt(F.toDouble()) * K // 计算流量系数
        return atpBtpsBreathOut(a, T, p) // 转换为标准温压状态下的流量
    }

    // 将流量从大气压条件（ATP）转换为干燥条件（ATPD）
    fun atpAtpd(F: Double, p: Double, T: Int, H: Double): Double {
        return F * (p / (p - TestDataCashe.getP0(T) * H / 100.0))
    }

    // 将流量从大气压条件（ATP）转换为标准温压干燥条件（ATPD-BTPS）
    fun atpAtpdBtps(F: Double, p: Double): Double {
        return F * (p - 47.08) / p
    }

    // 将流量从大气压条件（ATP）转换为吸气条件（ATPD-BTPS-In）
    fun atpAtpdBtpsIn(F: Double, p: Double, T: Int): Double {
        return F * (p - TestDataCashe.getP0(T)) / p
    }

    // 将流量从大气压条件（ATP）转换为吸气条件下的标准温压状态（BTPS-Breath-In）
    fun atpBtpsBreathIn(F: Double, T: Int, P: Double, H: Double = 100.0): Double {
        val t = TestDataCashe.getP0(T)
        return F * 310.15 / (273.15 + T / 10.0) * (P - t * H / 100.0) / (P - 47.08)
    }

    // 将流量从大气压条件（ATP）转换为CPX吸气条件下的标准温压状态（BTPS-Breath-Cpx-In）
    fun atpBtpsBreathCpxIn(F: Double, T: Int, P: Double, H: Double = 100.0): Double {
        val t = TestDataCashe.getP0(T)
        return F * 310.15 / (273.15 + T) * (P - TestDataCashe.getP0(T) * H) / (P - 47.08)
    }

    // 将流量从大气压条件（ATP）转换为呼气条件下的标准温压状态（BTPS-Breath-Out）
    fun atpBtpsBreathOut(F: Double, T: Int, P: Double): Double {
        val a = 370.0 - (370.0 - T) / 3.0
        return F * 310.15 / (273.15 + a / 10.0) * (P - TestDataCashe.getP0(a.roundToInt())) / (P - 47.08)
    }

    // 将流量从标准温压状态（STPD）转换为呼吸条件下的体积流量
    fun stpd(V: Double, P: Double): Double {
        return V * 273.15 / 310.15 * P / 760.0
    }
}
