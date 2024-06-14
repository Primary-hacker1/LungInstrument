package com.just.machine.model.lungdata

//import com.just.machine.model.lungdata.Cashe
import com.common.network.LogUtils
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.dao.setting.AllSettingBean
import com.just.machine.util.Algorithm
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.log10

/**
 * 动态肺测试参数计算方法
 */
object CPXCalcule {
    var beforeDyBreathInOutData: CPXBreathInOutData? = null

    fun clear() {
        beforeDyBreathInOutData = null
    }

    /**
     * @param dys 原始数据
     * CPXBreathInOutData计算返回的动态肺数据
     * */
    fun calDyBreathInOutData(
        dys: CPXSerializeData,
        dataBase: CPXBreathInOutDataBase
    ): CPXBreathInOutData {

        val df = DecimalFormat("#.##")

        val breathData = CPXBreathInOutData()

        breathData.VTin = df.format(abs(dataBase.TempiTotal / 1000.0)).toDouble()
        breathData.VTex = df.format(abs(dataBase.TempeTotal / 1000.0)).toDouble()

        breathData.FiO2 = df.format(dataBase.TempiO2 / dataBase.TempiTotal * 100.0).toDouble()
        breathData.FeO2 = df.format(dataBase.TempeO2 / dataBase.TempeTotal * 100.0).toDouble()

        breathData.FiCO2 = df.format(dataBase.TempiCO2 / dataBase.TempiTotal * 100.0).toDouble()
        breathData.FeCO2 = df.format(dataBase.TempeCO2 / dataBase.TempeTotal * 100.0).toDouble()

        val p2Kpa = dataBase.P
        breathData.PaO2 = df.format(p2Kpa * breathData.FeO2!! / 100.0).toDouble()
        breathData.PaCO2 = df.format(p2Kpa * breathData.FeCO2!! / 100.0).toDouble()
        breathData.PiO2 = df.format(p2Kpa * breathData.FiO2!! / 100.0).toDouble()
        breathData.PiCO2 = df.format(p2Kpa * breathData.FiCO2!! / 100.0).toDouble()
        breathData.PeTO2 = df.format(p2Kpa * dataBase.FeTO2 / 100.0).toDouble()
        breathData.PeTCO2 = df.format(p2Kpa * dataBase.FeTCO2 / 100.0).toDouble()

//        dataBase.Tin = 2.0
//        dataBase.Tex = 2.0

        breathData.Ttot = df.format(dataBase.Tin + dataBase.Tex).toDouble() //总时间
        breathData.Tin_div_Ttot = df.format(dataBase.Tin / breathData.Ttot!! * 100.0).toDouble()
        breathData.BF = 60.0 / breathData.Ttot!!
        breathData.VE = df.format(breathData.VTex!! * breathData.BF!!).toDouble()


        if (breathData.VE!! > 0.0) {
            LogUtils.d("BreathData" + "TempiCO2: ${dataBase.TempiCO2}")
            LogUtils.d("BreathData" + "TempeCO2: ${dataBase.TempeCO2}")
            LogUtils.d("BreathData" + "TempiTotal: ${dataBase.TempiTotal}")
            LogUtils.d("BreathData" + "TempeTotal: ${dataBase.TempeTotal}")
            LogUtils.d("BreathData" + "FeCO2: ${breathData.FeCO2}")
            LogUtils.d("BreathData" + "FiCO2: ${breathData.FiCO2}")
        }


        val allSettingBean = AllSettingBean()//这里要获取到设置数据

        val physicalVD = df.format(
            CPXCalBase.atpBtpsBreathOut(
                (if (allSettingBean.testDeadSpace == 0) 10 else allSettingBean.testDeadSpace) * 0.001,
                dataBase.T.toInt(),
                dataBase.P
            )
        ).toDouble()
//        val paCO2 = 0.05 * (breathData.PiO2!! - breathData.PaO2!!) + breathData.PeTCO2!!
//        breathData.VD =
//            breathData.VTex!! * (paCO2 - breathData.PaCO2!!) / paCO2 - physicalVD //不用物理死腔
//        breathData.VD_div_VT = breathData.VD!! / breathData.VTex!! * 100.0

        if (beforeDyBreathInOutData != null) {
            breathData.VD =
                breathData.VTex!! * (dataBase.FeTCO2 - breathData.FeCO2!!) / (dataBase.FeTCO2
                        - (if (breathData.FiCO2!! < 0.0) 0.0 else breathData.FiCO2)!!)
//            LogUtils.d("BreathData/" + "VTex: ${breathData.VTex}")
//            LogUtils.d("BreathData/" + "FeTCO2: ${dataBase.FeTCO2}")
//            LogUtils.d("BreathData/" + "FeCO2: ${breathData.FeCO2}")
//            LogUtils.d("BreathData/" + "FiCO2: ${breathData.FiCO2}")
//            LogUtils.d("BreathData/" + "vd1: ${breathData.VD}")
        } else {
            breathData.VD = 0.33 * breathData.VTex!!
        }
        if (breathData.VD!! < 0.2 || breathData.VD!! >= 1) {
            breathData.VD = 0.33 * breathData.VTex!!
        }
        breathData.VD_div_VT =
            df.format(breathData.VD!! / breathData.VTex!! - physicalVD).toDouble()

        breathData.VdO2 =
            df.format(
                if (beforeDyBreathInOutData == null) 0.0 else (breathData.VD!! + physicalVD) * abs(
                    dataBase.FeTO2 - dataBase.FeTO2
                ) / 100 * breathData.BF!!
            ).toDouble()

//        LogUtils.d("BreathData" + "physicalVD: ${physicalVD}")
//        LogUtils.d("BreathData" + "FeTO2: ${dataBase.FeTO2}")
//        LogUtils.d("BreathData" + "FeTO2: ${dataBase.FeTO2}")
//        LogUtils.d("BreathData" + "BF: ${breathData.BF}")
//        LogUtils.d("BreathData" + "VdO2: ${breathData.VdO2}")

        breathData.VdCO2 =
            df.format(
                if (beforeDyBreathInOutData == null) 0.0 else (breathData.VD!! + physicalVD) * abs(
                    dataBase.FeTCO2 - dataBase.FeTCO2
                ) / 100 * breathData.BF!!
            ).toDouble()
        breathData.VI =
            df.format(
                breathData.VE!! * (100.0 - breathData.FeCO2!! - breathData.FeO2!!) / (100.0 - breathData.FiO2!! - breathData.FiCO2!!)
            ).toDouble()
        val v1 =
            df.format(
                breathData.VI!! * breathData.FiO2!! / 100 - breathData.VE!! * breathData.FeO2!! / 100 + breathData.VdO2!!
            ).toDouble()
        val aa =
            log10(breathData.BF!! / 12) * 100

        breathData.VO2 = df.format(
            CPXCalBase.stpd(
                if (v1 + 0.02 - aa * v1 / 1000 < 0.03) 0.0 else v1 + 0.02 - aa * v1 / 1000,
                dataBase.P
            ) * 1000
        ).toDouble()

        if (dys.HR > 0) {
            breathData.VO2_div_HR = breathData.VO2!! / dys.HR
        }

        var V2 =
            df.format(
                breathData.VE!! * breathData.FeCO2!! / 100 - breathData.VI!! * breathData.FiCO2!! / 100 + breathData.VdCO2!!
            ).toDouble()
        val vext =
            df.format(
                breathData.BF!! * V2 * log10(breathData.BF!! / 12) / 1000
            ).toDouble()

        V2 =
            df.format(
                if ((V2 + vext) < 0.03) 0.0 else (V2 + vext)
            ).toDouble()
//        if (breathData.VE!! > 0.0) {
//            LogUtils.d("BreathData" + "TempiCO2: ${dataBase.TempiCO2}")
//            LogUtils.d("BreathData" + "TempeCO2: ${dataBase.TempeCO2}")
//            LogUtils.d("BreathData" + "TempiTotal: ${dataBase.TempiTotal}")
//            LogUtils.d("BreathData" + "p2Kpa: ${p2Kpa}")
//            LogUtils.d("BreathData" + "VE: ${breathData.VE}")
//            LogUtils.d("BreathData" + "FeCO2: ${breathData.FeCO2}")
//            LogUtils.d("BreathData" + "VI: ${breathData.VI}")
//            LogUtils.d("BreathData" + "FiCO2: ${breathData.FiCO2}")
//            LogUtils.d("BreathData" + "VdCO2: ${breathData.VdCO2}")
//            LogUtils.d("BreathData" + "vext: ${vext}")
//            LogUtils.d("BreathData" + "BF: ${breathData.BF}")
//            LogUtils.d("BreathData" + "V2: ${V2}")
//        }

        breathData.VCO2 = df.format(CPXCalBase.stpd(V2, dataBase.P) * 1000).toDouble()

        breathData.RER =
            df.format(if (breathData.VO2 == 0.0) 0.0 else breathData.VCO2!! / breathData.VO2!!)
                .toDouble()

        breathData.VO2_div_KG = df.format(breathData.VO2!! / 60).toDouble()

        breathData.BR = 60.0 / breathData.Ttot!!

        breathData.VE_div_VO2 =
            if (breathData.VO2!! < 50) 0.0 else (breathData.VE!! - physicalVD * breathData.BF!!) / breathData.VO2!! * 1000.0
        if (breathData.VE_div_VO2!! < 0) {
            breathData.VE_div_VO2 = 0.0
        }
        breathData.VE_div_VCO2 =
            if (breathData.VCO2!! < 50) 0.0 else (breathData.VE!! - physicalVD * breathData.BF!!) / breathData.VCO2!! * 1000.0
        if (breathData.VE_div_VCO2!! < 0) {
            breathData.VE_div_VCO2 = 0.0
        }
        breathData.REE = df.format(Algorithm.getREE(breathData.VO2!!, breathData.VCO2!!)).toDouble()
        val num3 = breathData.VCO2!! * 60.0 * 24.0 / 1000.0
        val num4 = breathData.VO2!! * 60.0 * 24.0 / 1000.0 - 15 * 0.95 * 6.25
        breathData.NPRQ =
            df.format(Algorithm.getNPRQ(breathData.VO2!!, breathData.VCO2!!, 15)).toDouble()
        breathData.PROT = 15 * 6.25
        breathData.PROT = df.format((15 * 6.25 * 4.3) / breathData.REE!!).toDouble()
        breathData.EE = df.format(breathData.EE?.div(4.1858518)).toDouble()

        return withNoWaterref(breathData, dataBase)
    }

    private fun withNoWaterref(
        breathData: CPXBreathInOutData?,
        dataBase: CPXBreathInOutDataBase
    ): CPXBreathInOutData {
        if (breathData == null) return CPXBreathInOutData()

        val num = dataBase.P / (dataBase.P - 47.08)

        breathData.PeTO2 = breathData.PeTO2?.times(num)
        breathData.PeTCO2 = breathData.PeTCO2?.times(num)
        breathData.FiO2 = breathData.FiO2?.times(num)
        breathData.FiCO2 = breathData.FiCO2?.times(num)
        breathData.FeO2 = breathData.FeO2?.times(num)
        breathData.FeCO2 = breathData.FeCO2?.times(num)
        breathData.PiO2 = breathData.PiO2?.times(num)
        breathData.PiCO2 = breathData.PiCO2?.times(num)
        breathData.PaO2 = breathData.PaO2?.times(num)
        breathData.PaCO2 = breathData.PaCO2?.times(num)

        return breathData
    }


}
