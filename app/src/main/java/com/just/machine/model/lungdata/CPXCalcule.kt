package com.just.machine.model.lungdata

//import com.just.machine.model.lungdata.Cashe
import com.common.network.LogUtils
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.dao.setting.AllSettingBean
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
        val breathData = CPXBreathInOutData()

        breathData.VTin = abs(dataBase.TempiTotal / 1000.0)
        breathData.FiO2 = dataBase.TempiO2 / dataBase.TempiTotal * 100.0
        breathData.FiCO2 = dataBase.TempiCO2 / dataBase.TempiTotal * 100.0
        breathData.VTex = abs(dataBase.TempeTotal / 1000.0)
        breathData.FeO2 = dataBase.TempeO2 / dataBase.TempeTotal * 100.0
        breathData.FeCO2 = dataBase.TempeCO2 / dataBase.TempeTotal * 100.0
        val p2Kpa = dataBase.P
        breathData.PaO2 = p2Kpa * breathData.FeO2!! / 100.0
        breathData.PaCO2 = p2Kpa * breathData.FeCO2!! / 100.0
        breathData.PiO2 = p2Kpa * breathData.FiO2!! / 100.0
        breathData.PiCO2 = p2Kpa * breathData.FiCO2!! / 100.0
        breathData.PeTO2 = p2Kpa * dataBase.FeTO2 / 100.0
        breathData.PeTCO2 = p2Kpa * dataBase.FeTCO2 / 100.0

//        dataBase.Tin = 2.0
//        dataBase.Tex = 2.0

        breathData.Ttot = dataBase.Tin + dataBase.Tex //总时间
        breathData.Tin_div_Ttot = dataBase.Tin / breathData.Ttot!! * 100.0
        breathData.BF = 60.0 / breathData.Ttot!!
        breathData.VE = breathData.VTex!! * breathData.BF!!

        val allSettingBean = AllSettingBean()//这里要获取到设置数据

        val physicalVD = CPXCalBase.atpBtpsBreathOut(
            (if (allSettingBean.testDeadSpace == 0) 10 else allSettingBean.testDeadSpace) * 0.001,
            dataBase.T.toInt(),
            dataBase.P
        )
        val paCO2 = 0.05 * (breathData.PiO2!! - breathData.PaO2!!) + breathData.PeTCO2!!
        breathData.VD =
            breathData.VTex!! * (paCO2 - breathData.PaCO2!!) / paCO2 - physicalVD //不用物理死腔
        breathData.VD_div_VT = breathData.VD!! / breathData.VTex!! * 100.0

//        LogUtils.d("BreathData" + "VTex: ${breathData.VTex}")
//        LogUtils.d("BreathData" + "paCO2: ${paCO2}")
//        LogUtils.d("BreathData" + "breathData.PaCO2: ${breathData.PaCO2}")
//
//        LogUtils.d("BreathData" + "physicalVD: ${physicalVD}")
//        LogUtils.d("BreathData" + "VdO2: ${breathData.VdO2}")
//        LogUtils.d("BreathData" + "VD: ${breathData.VD}")

        if (beforeDyBreathInOutData != null) {
            breathData.VD =
                breathData.VTex!! * (dataBase.FeTCO2 - breathData.FeCO2!!) / (dataBase.FeTCO2
                        - (if (breathData.FiCO2!! < 0.0) 0.0 else breathData.FiCO2)!!)
            LogUtils.d("BreathData" + "VTex: ${breathData.VTex}")
            LogUtils.d("BreathData" + "FeTCO2: ${dataBase.FeTCO2}")
            LogUtils.d("BreathData" + "FeCO2: ${breathData.FeCO2}")
            LogUtils.d("BreathData" + "FiCO2: ${breathData.FiCO2}")
            LogUtils.d("BreathData" + "vd1: ${breathData.VD}")
        } else {
            breathData.VD = 0.33 * breathData.VTex!!
        }
        if (breathData.VD!! < 0.2 || breathData.VD!! >= 1) {
            breathData.VD = 0.33 * breathData.VTex!!
        }
        LogUtils.d("BreathData" + "VD: ${breathData.VD}")
        breathData.VD_div_VT = breathData.VD!! / breathData.VTex!! - physicalVD

        breathData.VdO2 =
            if (beforeDyBreathInOutData == null) 0.0 else (breathData.VD!! + physicalVD) * abs(
                dataBase.FeTO2 - dataBase.FeTO2
            ) / 100 * breathData.BF!!

//        LogUtils.d("BreathData" + "physicalVD: ${physicalVD}")
//        LogUtils.d("BreathData" + "FeTO2: ${dataBase.FeTO2}")
//        LogUtils.d("BreathData" + "FeTO2: ${dataBase.FeTO2}")
//        LogUtils.d("BreathData" + "BF: ${breathData.BF}")
//        LogUtils.d("BreathData" + "VdO2: ${breathData.VdO2}")

        breathData.VdCO2 =
            if (beforeDyBreathInOutData == null) 0.0 else (breathData.VD!! + physicalVD) * abs(
                dataBase.FeTCO2 - dataBase.FeTCO2
            ) / 100 * breathData.BF!!
        breathData.VI =
            breathData.VE!! * (100.0 - breathData.FeCO2!! - breathData.FeO2!!) / (100.0 - breathData.FiO2!! - breathData.FiCO2!!)
        val v1 =
            breathData.VI!! * breathData.FiO2!! / 100 - breathData.VE!! * breathData.FeO2!! / 100 + breathData.VdO2!!

        val aa = log10(breathData.BF!! / 12) * 100
        breathData.VO2 = CPXCalBase.stpd(
            if (v1 + 0.02 - aa * v1 / 1000 < 0.03) 0.0 else v1 + 0.02 - aa * v1 / 1000,
            dataBase.P
        ) * 1000
        if (dys.HR > 0) {
            breathData.VO2_div_HR = breathData.VO2!! / dys.HR
        }
        val v2 =
            breathData.VE!! * breathData.FeCO2!! / 100 - breathData.VI!! * breathData.FiCO2!! / 100 + breathData.VdCO2!!
        breathData.VCO2 = CPXCalBase.stpd(v2, dataBase.P) * 1000
        breathData.RER = if (breathData.VO2 == 0.0) 0.0 else breathData.VCO2!! / breathData.VO2!!
        breathData.VO2_div_KG = breathData.VO2!! / 60

        breathData.BR = 60.0 / breathData.Ttot!!
        breathData.VE_div_VO2 =
            if (breathData.VO2!! < 50) 0.0 else breathData.VE!! / breathData.VO2!! * 100.0

        breathData.VE_div_VCO2 =
            if (breathData.VCO2!! < 50) 0.0 else (breathData.VE!! - 0.5 * breathData.BF!!) / breathData.VCO2!! * 1000.0
        beforeDyBreathInOutData = breathData
        return breathData
    }
}