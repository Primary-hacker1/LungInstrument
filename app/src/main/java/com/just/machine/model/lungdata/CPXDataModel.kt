package com.just.machine.model.lungdata

import com.just.machine.model.lungdata.CPXDataModel.Last
import com.just.machine.model.lungdata.SttlungDataModel.CPXDataModelType

object CPXDataModel {
    var INDEX = 0 // 静态索引，用于跟踪数据模型实例的数量
    const val arrayLength = 20 // 固定数组长度，用于存储历史数据
    var Last: CPXDataModel? = null // 保存上一个CPXDataModel实例
    var acthird_co2 = 0.0 // 静态变量，用于保存第三个加速度波形的CO2值
    val co2history = DoubleArray(arrayLength) // CO2历史记录数组
    val o2history = DoubleArray(arrayLength) // O2历史记录数组
    val flowhistory = IntArray(arrayLength) // 流量历史记录数组

    // 初始化氧气和二氧化碳加速度波形的静态变量
    var acfirst1_o2: Double = 10.0
    var acfirst2_o2: Double = 0.0
    var acsecond1_o2: Double = 10.0
    var acsecond2_o2: Double = 0.0
    var acthird1_o2: Double = 10.0
    var acthird2_o2: Double = 0.0
    var acfirst1_co2: Double = 10.0
    var acfirst2_co2: Double = 0.0
    var acsecond1_co2: Double = 10.0
    var acsecond2_co2: Double = 0.0
    var acthird1_co2: Double = 10.0
    var acthird2_co2: Double = 0.0
    var temphistory: DoubleArray = DoubleArray(arrayLength) // 温度历史记录数组
}

class CPXModel(
    type: CPXDataModelType,
    data: ByteArray
) {
    var index = 0
    var T = 0.0 // 温度
    var H = 0.0 // 湿度
    var P = 0.0 // 压力
    var breathOut = false // 是否呼气
    var door1Open = false // 门1是否打开
    var door2Open = false // 门2是否打开
    var flowori = 0.0 // 原始流量
    var co2ori = 0.0 // 原始二氧化碳
    var o2ori = 0.0 // 原始氧气
    var flow = 0.0 // 流量
    var sumFlowCopy = 0.0 // 流量总和副本
    var co2 = 0.0 // 二氧化碳
    var o2 = 0.0 // 氧气
    var co2_ac1 = 0.0 // 第一次加速度波形的CO2值
    var co2_ac2 = 0.0 // 第二次加速度波形的CO2值
    var co2_ac3 = 0.0 // 第三次加速度波形的CO2值
    var o2_ac1 = 0.0 // 第一次加速度波形的O2值
    var o2_ac2 = 0.0 // 第二次加速度波形的O2值
    var o2_ac3 = 0.0 // 第三次加速度波形的O2值
    var analysis_flow = 0 // 分析流量
    var analysis_pressure = 0.0 // 分析压力
    var spo2 = 0 // 血氧饱和度
    var sttlungData: SttlungDataModel? = null // Sttlung数据模型
    var analysis_temp = 0.0 // 分析温度
    var ingredientCoeff = IngredientCalibraCoefficient() // 成分校准系数

    init {
        when (type) {
            CPXDataModelType.DY -> {
                index = ++CPXDataModel.INDEX // 更新静态索引
                sttlungData = SttlungDataModel(CPXDataModelType.DY, data) // 创建Sttlung数据模型
                T = TestDataCashe.T // 从缓存中获取温度
                H = TestDataCashe.H // 从缓存中获取湿度
                P = TestDataCashe.P // 从缓存中获取压力
                flow = (-sttlungData!!.flow).toDouble() // 计算流量
                analysis_flow = data[16] * 256 + data[17] // 分析流量数据
                val dTemp = (data[22].toInt() * 256 + data[23]).toDouble() // 计算温度

                if (dTemp < 1500) {
                    // 更新温度历史记录数组
                    CPXDataModel.temphistory[index % CPXDataModel.arrayLength] = dTemp / 10
                    analysis_temp = if (index + 1 >= CPXDataModel.arrayLength) {
                        CPXDataModel.temphistory.average() // 计算温度平均值
                    } else {
                        var sum = 0.0
                        for (i in 0 until index + 1) {
                            sum += CPXDataModel.temphistory[i]
                        }
                        sum / (index + 1) // 计算温度平均值
                    }
                }
                spo2 = (data[20] * 256 + data[21]) // 获取血氧饱和度
                if (flow != 0.0) breathOut = flow > 0.0 // 判断是否呼气

                // 处理CO2和O2历史数据
                val num0 = (data[14] * 256 + data[15]).toDouble()
                val dyoriCO2 = (data[12] * 256 + data[13]).toDouble()
                val num1 = CPXCalBase.atpAtpd(num0 - (17 * analysis_temp) + 450, P, T.toInt(), H)
                val num2 = CPXCalBase.atpAtpd(dyoriCO2, P, T.toInt(), H)

                CPXDataModel.co2history[index % CPXDataModel.arrayLength] = num2
                CPXDataModel.o2history[index % CPXDataModel.arrayLength] = num1

                co2ori = if (index + 1 >= CPXDataModel.arrayLength) {
                    CPXDataModel.co2history.average()
                } else {
                    var sum = 0.0
                    for (i in 0 until index + 1) {
                        sum += CPXDataModel.co2history[i]
                    }
                    sum / (index + 1)
                }

                o2ori = if (index + 1 >= CPXDataModel.arrayLength) {
                    CPXDataModel.o2history.average()
                } else {
                    var sum = 0.0
                    for (i in 0 until index + 1) {
                        sum += CPXDataModel.o2history[i]
                    }
                    sum / (index + 1)
                }

                val tempKCO2 = ingredientCoeff.kco2.coerceAtLeast(0.009)
                co2 = tempKCO2 * co2_ac3 + ingredientCoeff.bco2
                if (co2 < 0) co2 = 0.0
                o2 = ingredientCoeff.ko2 * o2_ac3 + ingredientCoeff.bo2
                if (o2 < 0) o2 = 0.0

                if (TestDataCashe.beforeDyDataModel == null) {
                    co2 = tempKCO2 * co2_ac3 + ingredientCoeff.bco2
                    if (co2 < 0) co2 = 0.0
                    co2_ac1 = co2ori
                    co2_ac2 = co2ori
                    co2_ac3 = co2ori
                    o2 = ingredientCoeff.ko2 * o2_ac3 + ingredientCoeff.bo2
                    o2_ac1 = o2ori
                    o2_ac2 = o2ori
                    o2_ac3 = o2ori
                } else {
                    val beforeDyDataModel = TestDataCashe.beforeDyDataModel!!
                    co2_ac1 = AccelerateWave_CO2(
                        co2ori,
                        co2_ac1,
                        co2ori,
                        1
                    )
                    co2_ac2 = AccelerateWave_CO2(
                        co2_ac1,
                        co2_ac2,
                        co2_ac1,
                        2
                    )
                    co2_ac3 = AccelerateWave_CO2(
                        co2_ac2,
                        co2_ac3,
                        co2_ac2,
                        3
                    )
                    co2ori = co2_ac3
                    co2 = tempKCO2 * co2ori + ingredientCoeff.bco2
                    if (co2 < 0) co2 = 0.0
                    o2_ac1 = AccelerateWave_O2(
                        o2ori,
                        o2_ac1,
                        o2ori,
                        1
                    )
                    o2_ac2 = AccelerateWave_O2(
                        o2_ac1,
                        o2_ac2,
                        o2_ac1,
                        2
                    )
                    o2_ac3 = AccelerateWave_O2(
                        o2_ac2,
                        o2_ac3,
                        o2_ac2,
                        3
                    )
                    o2ori = o2_ac3
                    o2 = ingredientCoeff.ko2 * o2ori + ingredientCoeff.bo2
                }

                // 保存当前数据模型实例到缓存中
                // InstanceBase<TestDataCashe>.Instance.beforeDyDataModel = this
            }

            // 将当前实例设置为最后一个实例
            // Last = this
        }
    }

    companion object {
        fun Clear() {
            Last = null // 清除最后一个实例
            CPXDataModel.INDEX = 0 // 重置静态索引
            CPXDataModel.co2history.fill(0.0) // 清空CO2历史记录
            CPXDataModel.o2history.fill(0.0) // 清空O2历史记录
            TestDataCashe.beforeDyDataModel = null // 清除缓存中的数据模型
            TestDataCashe.dysumFlow = 0.0 // 重置总流量
            CPXDataModel.temphistory.fill(0.0) // 清空温度历史记录
        }

        // 计算氧气加速度波形
        private fun AccelerateWave_O2(
            previousbefore: Double,
            previous: Double,
            before: Double,
            accNum: Int
        ): Double {
            val num1: Double
            val num2: Double
            when (accNum) {
                1 -> {
                    num1 = CPXDataModel.acfirst1_o2
                    num2 = CPXDataModel.acfirst2_o2
                }

                2 -> {
                    num1 = CPXDataModel.acsecond1_o2
                    num2 = CPXDataModel.acsecond2_o2
                }

                3 -> {
                    num1 = CPXDataModel.acthird1_o2
                    num2 = CPXDataModel.acthird2_o2
                }

                else -> throw IllegalArgumentException("Invalid accNum value: $accNum")
            }
            return previous * (1.0 - 10.0 / num1) + before * 10.0 / num1 + (before - previousbefore) * num2
        }

        // 计算二氧化碳加速度波形
        private fun AccelerateWave_CO2(
            previousbefore: Double,
            previous: Double,
            before: Double,
            accNum: Int
        ): Double {
            val num1: Double
            val num2: Double
            when (accNum) {
                1 -> {
                    num1 = CPXDataModel.acfirst1_co2
                    num2 = CPXDataModel.acfirst2_co2
                }

                2 -> {
                    num1 = CPXDataModel.acsecond1_co2
                    num2 = CPXDataModel.acsecond2_co2
                }

                3 -> {
                    num1 = CPXDataModel.acthird1_co2
                    num2 = CPXDataModel.acthird2_co2
                }

                else -> throw IllegalArgumentException("Invalid accNum value: $accNum")
            }
            return previous * (1.0 - 10.0 / num1) + before * 10.0 / num1 + (before - previousbefore) * num2
        }
    }
}
