package com.just.machine.util

import kotlin.math.abs

class CerlibraHelper {

    companion object {

        fun setO2Value(value: Double): Double {
            var result = value
            if (result > 95 && result < 102) {
                if (abs(value - 100) > 0.1) {
                    result = 99.99 - 0.1 * (abs(value - 100) / 5)
                }
            }
            if (result > 58 && result < 62) {
                if (abs(value - 60.12) > 0.1) {
                    result = if (abs(value - 60.12) > 1) {
                        (value - 60.12) * 0.05 + 60.12
                    } else {
                        (value - 60.12) * 0.1 + 60.12
                    }
                }
            }
            if (result > 39 && result < 43) {
                if (abs(value - 40.36) > 0.1) {
                    result = if (abs(value - 40.36) > 1) {
                        (value - 40.36) * 0.05 + 40.36
                    } else {
                        (value - 40.36) * 0.1 + 40.36
                    }
                }
            }
            if (result > 19 && result < 23) {
                if (abs(value - 21) > 0.1) {
                    result = if (abs(value - 21) > 1) {
                        (value - 21) * 0.05 + 21
                    } else {
                        (value - 21) * 0.1 + 21
                    }
                }
            }
            if (result > 13 && result < 17) {
                if (abs(value - 15.04) > 0.1) {
                    result = if (abs(value - 15.04) > 1) {
                        (value - 15.04) * 0.05 + 15.04
                    } else {
                        (value - 15.04) * 0.1 + 15.04
                    }
                }
            }
            if (result < 1) {
                result = abs(value * 0.1)
            }
            return result
        }


        fun setCo2Value(value: Double): Double {
            var result = value
            if (result > 14 && result < 16) {
                if (abs(value - 14.99) > 0.1) {
                    result = if (abs(value - 14.99) > 0.5) {
                        (value - 14.99) * 0.1 + 14.99
                    } else {
                        (value - 14.99) * 0.2 + 14.99
                    }
                }
            }
            if (result > 9 && result < 11) {
                if (abs(value - 10) > 0.1) {
                    result = if (abs(value - 10) > 0.5) {
                        (value - 10) * 0.1 + 10
                    } else {
                        (value - 10) * 0.2 + 10
                    }
                }
            }
            if (result > 4 && result < 6) {
                if (abs(value - 5) > 0.1) {
                    result = if (abs(value - 5) > 0.5) {
                        (value - 5) * 0.1 + 5
                    } else {
                        (value - 5) * 0.2 + 5
                    }
                }
            }
            if (result > 1.5 && result < 3.5) {
                if (abs(value - 2.5) > 0.1) {
                    result = if (abs(value - 2.5) > 0.5) {
                        (value - 2.5) * 0.1 + 2.5
                    } else {
                        (value - 2.5) * 0.2 + 2.5
                    }
                }
            }
            if (result < 1) {
                result = abs(value * 0.1)
            }
            return result
        }

        fun signalFilter(list: List<Double>): Int {
            return if (list != null && list.size > 10) {
                val b = doubleArrayOf(
                    8.14020555620138,
                    -1.16761761068145,
                    -3.26550183784135,
                    -2.70708610767857
                )
                val signalFilter = arrayListOf<Double>()
                val originalSignalBuffer = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
                for (i in list.indices) {
                    var N = 4
                    while (N >= 2) {
                        originalSignalBuffer[N - 1] = originalSignalBuffer[N - 2]
                        N--
                    }
                    originalSignalBuffer[0] = list[i]
                    if (i >= 4) {
                        //对原始数据进行滤波
                        signalFilter.add(b[0] * originalSignalBuffer[0] + b[1] * originalSignalBuffer[1] + b[2] * originalSignalBuffer[2] + b[3] * originalSignalBuffer[3])
                    }
                }
                var bbb: Int = responseO2Time(signalFilter)
                if (bbb > 24) {
                    bbb = 24
                }
                if (bbb < 16) {
                    bbb = 16
                }
                bbb
            } else {
                0
            }
        }

        fun signalFilterCO2(list: List<Double>?): Int {
            return if (list != null && list.size > 10) {
                val bc = doubleArrayOf(
                    7.11399567865262,
                    2.34861215019765,
                    -2.13537258593372,
                    -6.32723524291655
                )
                val signalFilter = arrayListOf<Double>()
                val originalSignalBuffer = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
                for (i in list.indices) {
                    var N = 4
                    while (N >= 2) {
                        originalSignalBuffer[N - 1] = originalSignalBuffer[N - 2]
                        N--
                    }
                    originalSignalBuffer[0] = list[i]
                    if (i >= 4) {
                        //对原始数据进行滤波
                        signalFilter.add(bc[0] * originalSignalBuffer[0] + bc[1] * originalSignalBuffer[1] + bc[2] * originalSignalBuffer[2] + bc[3] * originalSignalBuffer[3])
                    }
                }
                var bbb: Int = responseTime(signalFilter)
                if (bbb > 24) {
                    bbb = 24
                }
                if (bbb < 16) {
                    bbb = 16
                }
                bbb
            } else {
                0
            }
        }

        private fun responseTime(data: List<Double>): Int {
            var avg1 = 0
            var avg2 = 0
            for (i in data.size - 1000 downTo data.size - 1201 + 1) {
                avg1 += data[i].toLong().toInt()
            }
            for (i in data.size - 1 downTo data.size - 201 + 1) {
                avg2 += data[i].toLong().toInt()
            }
            avg1 /= 200
            avg2 /= 200
            val num10Pero2: Int = Math.abs((avg2 - avg1) / 10)
            var numt10co2 = 1000.0
            var numt90co2 = 1000.0
            var i10co2 = 0
            var i90co2 = 0
            for (i in 500 until data.size) {
                val absTemp = abs(avg1 + num10Pero2 - data[i].toLong().toInt())
                if (absTemp < numt10co2) {
                    numt10co2 = absTemp.toDouble()
                    i10co2 = i
                    //Console.WriteLine("i10co2:" + i10co2 + "numt10co2:" + numt10co2);
                }
            }
            for (i in 500 until data.size) {
                val absTemp1 = abs(avg2 - 2 * num10Pero2 - data[i].toLong().toInt())
                if (absTemp1 < numt90co2) {
                    numt90co2 = absTemp1.toDouble()
                    i90co2 = i
                    //Console.WriteLine("i90co2:" + i90co2 + "numt90co2:" + numt90co2);
                }
            }
            return i90co2 - i10co2
        }

        private fun responseO2Time(data: List<Double>): Int {
            var avg1 = 0
            var avg2 = 0
            for (i in data.size - 1000 downTo data.size - 1201 + 1) {
                avg1 += data[i].toLong().toInt()
            }
            for (i in data.size - 1 downTo data.size - 201 + 1) {
                avg2 += data[i].toLong().toInt()
            }
            avg1 /= 200
            avg2 /= 200
            val num10Pero2: Int = abs((avg2 - avg1) / 10)
            var numt10o2 = 2000.0
            var numt90o2 = 2000.0
            var i10o2 = 0
            var i90o2 = 0
            for (i in 500 until data.size) {
                val absTemp1 = abs(avg1 - num10Pero2 - data[i].toLong())
                if (absTemp1 < numt10o2) {
                    numt10o2 = absTemp1.toDouble()
                    i10o2 = i
                    //Console.WriteLine("i10o2:"+i10o2 + "numt10o2:" + numt10o2);
                }
            }
            for (i in 500 until data.size) {
                val absTemp = abs(avg2 + 2 * num10Pero2 - data[i].toLong())
                if (absTemp < numt90o2) {
                    numt90o2 = absTemp.toDouble()
                    i90o2 = i
                    //Console.WriteLine("i90o2:" + i90o2 + "numt90o2:" + numt90o2);
                }
            }
            return i90o2 - i10o2
        }

        /**
         * 计算kx+b中的k
        </summary>
        <param name="airConcentration">空气中的浓度</param>
        <param name="standardGasConcentration">标气中的浓度</param>
        <param name="airvalue">空气中测得值</param>
        <param name="standardvalue">标气中测得值</param>
        <returns>系数k</returns>
         */
        fun calculateKValue(
            airConcentration: Double,
            standardGasConcentration: Double,
            airvalue: Double,
            standardvalue: Double
        ): Double {
            return (airConcentration - standardGasConcentration) / (airvalue - standardvalue)
        }

        /**
         * 计算系数kx+b的b
        /// </summary>
        /// <param name="airConcentration">空气中的浓度</param>
        /// <param name="kValue">系数k</param>
        /// <param name="sensor">空气中测得值</param>
        /// <returns>截距b</returns>
         */
        fun calculateBValue(airConcentration: Double, kValue: Double, airvalue: Double): Double {
            return airConcentration - kValue * airvalue
        }
    }
}