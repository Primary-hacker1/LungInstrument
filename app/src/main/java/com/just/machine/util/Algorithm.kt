package com.just.machine.util

object Algorithm {
    fun STPD(value: Double, pressure: Double): Double {
        // 假设实现是基于某些公式的转换
        // 这里提供一个简单的示例逻辑，实际实现可能不同
        return value * pressure / 101.3 // 示例公式
    }
    fun getREE(vo2: Double, vco2: Double): Double {
        // 假设的 REE 计算公式，实际公式可能不同
        // REE 是指静息能量消耗，通常基于 VO2 和 VCO2 的某些比例计算
        return 3.9 * vo2 + 1.1 * vco2 // 示例公式
    }
    fun getNPRQ(vo2: Double, vco2: Double, someParameter: Int): Double {
        // NPRQ 是指营养呼吸商，通常是 VCO2 和 VO2 的比值
        return vco2 / vo2 // 示例公式
    }
}