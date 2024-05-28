package com.just.machine.model.lungdata

import java.io.Serializable
import kotlin.math.sqrt

class SttlungDataModel : Serializable {

    companion object {
        var INDEX = 0
        var beforeflow = 0f
        const val arrayLength = 20
        var flowavg = FloatArray(arrayLength)
        var flownewavg = FloatArray(arrayLength)
        var flowavgHigh = FloatArray(arrayLength)

        fun clear() {
            INDEX = 0
            beforeflow = 0f
            flownewavg.fill(0f)
            flowavg.fill(0f)
            flowavgHigh.fill(0f)
        }
    }

    var breathOut: Boolean = false
    var door1Open: Boolean = false
    var door2Open: Boolean = false
    var flow: Float = 0f
    var flowori: Float = 0f
    var H: Int = 0
    var index: Int = 0
    var P: Int = 0
    var signalhigh: Int = 0
    var signallow: Int = 0
    var T: Int = 0
    var sumFlowCopy: Float = 0f

    constructor()

    constructor(stttype: CPXDataModelType, data: ByteArray) {
        var d = 0f
        var dh = 0f
        this.index = INDEX++
        when (stttype) {
            CPXDataModelType.FLOWCALIBRATE -> {
                this.signallow = data[1].toInt() shl 8 or (data[0].toInt() and 0xFF)
                this.signalhigh = data[2].toInt() * 256 + data[3].toInt()
            }
            CPXDataModelType.STT -> {
                this.T = data[0].toInt() * 256 + data[1].toInt()
                this.H = (data[2].toInt() * 256 + data[3].toInt()) / 10
                this.P = TestDataCashe.instance.getP2mmHg((data[4].toInt() * 256 * 256 * 256 + data[5].toInt() * 256 * 256 + data[6].toInt() * 256 + data[7].toInt()) * 10)
                if (this.P < 500 || this.P > 1000 || this.T <= 0 || this.T > 500 || this.H <= 0 || this.H > 100) {
                    TestDataCashe.instance.getHistoryEnvironment()
                    this.T = TestDataCashe.instance.T.toInt()
                    this.H = TestDataCashe.instance.H.toInt()
                    this.P = TestDataCashe.instance.P.toInt()
                }
                this.signallow = data[9].toInt() shl 8 or (data[8].toInt() and 0xFF)
                this.signalhigh = data[10].toInt() * 256 + data[11].toInt()
                d = this.signallow.toFloat() - Definition.fzeroLow
                dh = this.signalhigh.toFloat() - Definition.fzeroHigh
            }
            CPXDataModelType.DY -> {
                this.T = data[0].toInt() * 256 + data[1].toInt()
                this.H = (data[2].toInt() * 256 + data[3].toInt()) / 10
                this.P = TestDataCashe.instance.getP2mmHg((data[4].toInt() * 256 * 256 * 256 + data[5].toInt() * 256 * 256 + data[6].toInt() * 256 + data[7].toInt()) * 10)
                if (this.P < 500 || this.P > 1000 || this.T <= 0 || this.T > 500 || this.H <= 0 || this.H > 100) {
                    TestDataCashe.instance.getHistoryEnvironment()
                    this.T = TestDataCashe.instance.T.toInt()
                    this.H = TestDataCashe.instance.H.toInt()
                    this.P = TestDataCashe.instance.P.toInt()
                }
                this.signallow = data[9].toInt() shl 8 or (data[8].toInt() and 0xFF)
                this.signalhigh = data[10].toInt() * 256 + data[11].toInt()
                d = this.signallow.toFloat() - Definition.fzeroLow
                dh = this.signalhigh.toFloat() - Definition.fzeroHigh
            }
            CPXDataModelType.AUTOFLOW -> {
                this.flowori = (data[1].toInt() shl 8 or (data[0].toInt() and 0xFF)).toFloat()
                this.signallow = data[5].toInt() shl 8 or (data[4].toInt() and 0xFF)
                this.signalhigh = data[6].toInt() * 256 + data[7].toInt()
            }
        }

        var dtemp = d
        var dtemphigh = dh
        val lastbreath = breathOut

        flownewavg[index % arrayLength] = dtemp

        if (this.index + 1 >= arrayLength) {
            dtemp = if (flownewavg.maxOrNull()!! < 65 && flownewavg.minOrNull()!! > -65) {
                flownewavg.sorted().subList(6, 14).average().toFloat()
            } else {
                flownewavg.average().toFloat()
            }
        } else {
            dtemp = flownewavg.sum() / (this.index + 1)
        }

        flowavgHigh[index % arrayLength] = dtemphigh

        if (this.index + 1 >= arrayLength) {
            dtemphigh = flowavgHigh.sorted().subList(6, 14).average().toFloat()
        } else {
            dtemphigh = flowavgHigh.sum() / (this.index + 1)
        }

        if (Definition.LowLimit_AD < dtemp && dtemp < Definition.HighLimit_AD) {
            flow = when {
                dtemp <= Definition.LDES_AD && dtemp > Definition.Noise_AD -> sqrt(dtemp) * Definition.Cur_ADC_IN_LDES
                dtemp > Definition.LDES_AD -> sqrt(dtemp) * Definition.Cur_ADC_IN_LDES
                dtemp > -Definition.LDES_AD && dtemp < -Definition.Noise_AD -> -sqrt(-dtemp) * Definition.Cur_ADC_OUT_LDES
                dtemp < -Definition.LDES_AD -> -sqrt(-dtemp) * Definition.Cur_ADC_OUT_LDES
                else -> 0f
            }
            breathOut = dtemp > 0
        } else {
            flow = when {
                dtemphigh > 30 -> sqrt(dtemphigh) * Definition.Cur_ADC_IN_HDIM * 1.03f
                dtemphigh < -30 -> -sqrt(-dtemphigh) * Definition.Cur_ADC_OUT_HDIM * 1.03f
                else -> 0f
            }
        }

        flowavg[index % arrayLength] = flow

        flow = if (this.index + 1 >= arrayLength) {
            flowavg.average().toFloat()
        } else {
            flowavg.sum() / (this.index + 1)
        }

        when (stttype) {
            CPXDataModelType.FLOWCALIBRATE, CPXDataModelType.STT -> {
                this.flow *= 1000
                this.flowori = this.flow
                this.breathOut = !this.breathOut
                this.flow = if (this.breathOut) {
                    CPXCalBase.ATP_BTPS_BreathIn(this.flow.toDouble(), this.T, this.P.toDouble()).toFloat()
                } else {
                    CPXCalBase.ATP_BTPS_BreathOut(this.flow.toDouble(), this.T, this.P.toDouble()).toFloat()
                }
            }
            CPXDataModelType.DY -> {
                this.flow *= 1000
                this.breathOut = !this.breathOut
                this.flow = if (!this.breathOut) {
                    CPXCalBase.ATP_BTPS_BreathIn(this.flow.toDouble(), this.T, this.P.toDouble()).toFloat()
                } else {
                    CPXCalBase.ATP_BTPS_BreathOut(this.flow.toDouble(), this.T, this.P.toDouble()).toFloat()
                }
            }
        }

        TestDataCashe.instance.sttsumFlow += this.flow * 0.005f / 1000
        this.sumFlowCopy = TestDataCashe.instance.sttsumFlow.toFloat()
    }

    fun getCopy(): SttlungDataModel {
        return SttlungDataModel().also {
            it.index = this.index
            it.breathOut = this.breathOut
            it.sumFlowCopy = this.sumFlowCopy
            it.flow = this.flow
            it.flowori = this.flowori
        }
    }

    enum class CPXDataModelType {
        FLOWCALIBRATE, STT, DY, AUTOFLOW
    }

    object Definition {
        const val fzeroLow = 0f
        const val fzeroHigh = 0f
        const val LowLimit_AD = 0f
        const val HighLimit_AD = 0f
        const val LDES_AD = 0f
        const val Noise_AD = 0f
        const val Cur_ADC_IN_LDES = 0f
        const val Cur_ADC_OUT_LDES = 0f
        const val Cur_ADC_IN_HDIM = 0f
        const val Cur_ADC_OUT_HDIM = 0f
    }

    object TestDataCashe {
        val instance = this

        var T: Float = 0f
        var H: Float = 0f
        var P: Float = 0f
        var sttsumFlow: Float = 0f

        fun getP2mmHg(p: Int): Int {
            // Implement your logic
            return p
        }

        fun getHistoryEnvironment() {
            // Implement your logic
        }
    }

    object CPXCalBase {
        fun ATP_BTPS_BreathIn(flow: Double, T: Int, P: Double): Double {
            // Implement your logic
            return flow
        }

        fun ATP_BTPS_BreathOut(flow: Double, T: Int, P: Double): Double {
            // Implement your logic
            return flow
        }
    }
}
