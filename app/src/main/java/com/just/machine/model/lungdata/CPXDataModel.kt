//package com.just.machine.model.lungdata
//
//import kotlin.math.abs
//
//object CPXDataModel {
//    var INDEX = 0
//    const val arrayLength = 20
//    var Last: CPXDataModel? = null
//    var acthird_co2 = 0.0
//    val co2history = DoubleArray(arrayLength)
//    val o2history = DoubleArray(arrayLength)
//    val flowhistory = IntArray(arrayLength)
//}
//
//class CPXModel(
////    type: CPXDataModelType,
//    data: ByteArray
//) {
//    var index = 0
//    var T = 0.0
//    var H = 0.0
//    var P = 0.0
//    var breathOut = false
//    var door1Open = false
//    var door2Open = false
//    var flowori = 0.0
//    var co2ori = 0.0
//    var o2ori = 0.0
//    var flow = 0.0
//    var sumFlowCopy = 0.0
//    var co2 = 0.0
//    var o2 = 0.0
//    var co2_ac1 = 0.0
//    var co2_ac2 = 0.0
//    var co2_ac3 = 0.0
//    var o2_ac1 = 0.0
//    var o2_ac2 = 0.0
//    var o2_ac3 = 0.0
//    var analysis_flow = 0
//    var analysis_pressure = 0.0
//    var spo2 = 0
//    var sttlungData: SttlungDataModel? = null
//    var analysis_temp = 0.0
//
//    init {
//        when (type) {
//            CPXDataModelType.dy -> {
//                index = ++CPXDataModel.INDEX
//                sttlungData = SttlungDataModel(CPXDataModelType.dy, data)
//                T = TestDataCashe.T
//                H = TestDataCashe.H
//                P = TestDataCashe.P
//                flow = -sttlungData!!.flow
//                analysis_flow = data[16] * 256 + data[17]
//                val dTemp = (data[22].toInt() * 256 + data[23]).toDouble()
//                if (dTemp < 1500) {
//                    CPXDataModel.temphistory[index % CPXDataModel.arrayLength] = dTemp / 10
//                    analysis_temp = if (index + 1 >= CPXDataModel.arrayLength) {
//                        CPXDataModel.temphistory.average()
//                    } else {
//                        var sum = 0.0
//                        for (i in 0 until index + 1) {
//                            sum += CPXDataModel.temphistory[i]
//                        }
//                        sum / (index + 1)
//                    }
//                }
//                spo2 = (data[20] * 256 + data[21]).toInt()
//                if (flow != 0.0) breathOut = flow > 0.0
//
//                val num0 = (data[14] * 256 + data[15]).toDouble()
//                val dyoriCO2 = (data[12] * 256 + data[13]).toDouble()
//                val num1 = CPXCalBase.ATP_ATPD(num0 - (17 * analysis_temp) + 450, P, T.toInt(), H)
//                val num2 = CPXCalBase.ATP_ATPD(dyoriCO2, P, T.toInt(), H)
//
//                CPXDataModel.co2history[index % CPXDataModel.arrayLength] = num2
//                CPXDataModel.o2history[index % CPXDataModel.arrayLength] = num1
//
//                co2ori = if (index + 1 >= CPXDataModel.arrayLength) {
//                    CPXDataModel.co2history.average()
//                } else {
//                    var sum = 0.0
//                    for (i in 0 until index + 1) {
//                        sum += CPXDataModel.co2history[i]
//                    }
//                    sum / (index + 1)
//                }
//
//                o2ori = if (index + 1 >= CPXDataModel.arrayLength) {
//                    CPXDataModel.o2history.average()
//                } else {
//                    var sum = 0.0
//                    for (i in 0 until index + 1) {
//                        sum += CPXDataModel.o2history[i]
//                    }
//                    sum / (index + 1)
//                }
//
//                val tempKCO2 = ingredientCoeff.KCO2.coerceAtLeast(0.009)
//                co2 = tempKCO2 * co2_ac3 + ingredientCoeff.BCO2
//                if (co2 < 0) co2 = 0.0
//                o2 = ingredientCoeff.KO2 * o2_ac3 + ingredientCoeff.BO2
//                if (o2 < 0) o2 = 0.0
//
//                if (TestDataCashe.beforeDyDataModel == null) {
//                    co2 = tempKCO2 * co2_ac3 + ingredientCoeff.BCO2
//                    if (co2 < 0) co2 = 0.0
//                    co2_ac1 = co2ori
//                    co2_ac2 = co2ori
//                    co2_ac3 = co2ori
//                    o2 = ingredientCoeff.KO2 * o2_ac3 + ingredientCoeff.BO2
//                    o2_ac1 = o2ori
//                    o2_ac2 = o2ori
//                    o2_ac3 = o2ori
//                } else {
//                    val beforeDyDataModel = TestDataCashe.beforeDyDataModel!!
//                    co2_ac1 = AccelerateWave_CO2(
//                        beforeDyDataModel.co2ori,
//                        beforeDyDataModel.co2_ac1,
//                        co2ori,
//                        1
//                    )
//                    co2_ac2 = AccelerateWave_CO2(
//                        beforeDyDataModel.co2_ac1,
//                        beforeDyDataModel.co2_ac2,
//                        co2_ac1,
//                        2
//                    )
//                    co2_ac3 = AccelerateWave_CO2(
//                        beforeDyDataModel.co2_ac2,
//                        beforeDyDataModel.co2_ac3,
//                        co2_ac2,
//                        3
//                    )
//                    co2ori = co2_ac3
//                    co2 = tempKCO2 * co2ori + ingredientCoeff.BCO2
//                    if (co2 < 0) co2 = 0.0
//                    o2_ac1 = AccelerateWave_O2(
//                        beforeDyDataModel.o2ori,
//                        beforeDyDataModel.o2_ac1,
//                        o2ori,
//                        1
//                    )
//                    o2_ac2 = AccelerateWave_O2(
//                        beforeDyDataModel.o2_ac1,
//                        beforeDyDataModel.o2_ac2,
//                        o2_ac1,
//                        2
//                    )
//                    o2_ac3 = AccelerateWave_O2(
//                        beforeDyDataModel.o2_ac2,
//                        beforeDyDataModel.o2_ac3,
//                        o2_ac2,
//                        3
//                    )
//                    o2ori = o2_ac3
//                    o2 = ingredientCoeff.KO2 * o2ori + ingredientCoeff.BO2
//                }
//
//                InstanceBase<TestDataCashe>.Instance.beforeDyDataModel = this
//            }
//
//            CPXDataModel.Last = this
//        }
//    }
//
//    companion object {
//        fun Clear() {
//            CPXDataModel.Last = null
//            CPXDataModel.INDEX = 0
//            CPXDataModel.co2history.fill(0.0)
//            CPXDataModel.o2history.fill(0.0)
//            TestDataCashe.beforeDyDataModel = null
//            TestDataCashe.dysumFlow = 0.0
//            CPXDataModel.temphistory.fill(0.0)
//        }
//
//        private fun AccelerateWave_O2(
//            previousbefore: Double,
//            previous: Double,
//            before: Double,
//            accNum: Int
//        ): Double {
//            val num1: Double
//            val num2: Double
//            when (accNum) {
//                1 -> {
//                    num1 = acfirst1_o2
//                    num2 = acfirst2_o2
//                }
//
//                2 -> {
//                    num1 = acsecond1_o2
//                    num2 = acsecond2_o2
//                }
//
//                3 -> {
//                    num1 = acthird1_o2
//                    num2 = acthird2_o2
//                }
//
//                else -> throw IllegalArgumentException("Invalid accNum value: $accNum")
//            }
//            return previous * (1.0 - 10.0 / num1) + before * 10.0 / num1 + (before - previousbefore) * num2
//        }
//
//        private fun AccelerateWave_CO2(
//            previousbefore: Double,
//            previous: Double,
//            before: Double,
//            accNum: Int
//        ): Double {
//            val num1: Double
//            val num2: Double
//            when (accNum) {
//                1 -> {
//                    num1 = acfirst1_co2
//                    num2 = acfirst2_co2
//                }
//
//                2 -> {
//                    num1 = acsecond1_co2
//                    num2 = acsecond2_co2
//                }
//
//                3 -> {
//                    num1 = acthird1_co2
//                    num2 = acthird2_co2
//                }
//
//                else -> throw IllegalArgumentException("Invalid accNum value: $accNum")
//            }
//            return previous * (1.0 - 10.0 / num1) + before * 10.0 / num1 + (before - previousbefore) * num2
//        }
//    }
//}
