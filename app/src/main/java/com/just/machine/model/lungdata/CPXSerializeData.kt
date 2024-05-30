package com.just.machine.model.lungdata

import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.model.LungTestData
import java.io.Serializable

data class CPXSerializeData(
    var index: Int = 0,
    var breathData: CPXBreathInOutData? = null,
    var rpe: Double = 0.0,
    var SPO2: Int = 0,
    var Pdia: Double = 0.0,
    var Psys: Double = 0.0,
    var Grade: Double = 0.0,
    var Speed: Double = 0.0,
    var Load: Double = 0.0,
    var rpm: Double = 0.0,
    var vol: Double = 0.0,
    var hrr: Double = 0.0,
    var HR: Double = 0.0,
    var flow: Double = 0.0,
    var o2: Double = 0.0,
    var co2: Double = 0.0,
    var t_ph: Int = 0,
    var sportpool: String? = null
) : Serializable {

    fun convertLungTestDataToCPXSerializeData(lungTestData: LungTestData): CPXSerializeData {
        return CPXSerializeData(
            index = 0,  // 假设索引是 0，可以根据需要设置
            breathData = null,  // 假设没有呼吸数据
            rpe = 0.0,  // 假设 RPE 默认是 0.0，可以根据需要设置
            SPO2 = lungTestData.bloodOxygen,  // 映射血氧数据
            Pdia = 0.0,  // 假设舒张压默认是 0.0，可以根据需要设置
            Psys = 0.0,  // 假设收缩压默认是 0.0，可以根据需要设置
            Grade = 0.0,  // 假设坡度默认是 0.0，可以根据需要设置
            Speed = 0.0,  // 假设速度默认是 0.0，可以根据需要设置
            Load = 0.0,  // 假设负载默认是 0.0，可以根据需要设置
            rpm = lungTestData.gasFlowSpeedSensorData.toDouble(),  // 映射气体流速传感器数据
            vol = lungTestData.lowRangeFlowSensorData.toDouble(),  // 映射低量程流量传感器数据
            hrr = 0.0,  // 假设 HRR 默认是 0.0，可以根据需要设置
            HR = 0.0,  // 假设心率默认是 0.0，可以根据需要设置
            flow = lungTestData.highRangeFlowSensorData.toDouble(),  // 映射高量程流量传感器数据
            o2 = lungTestData.o2SensorData.toDouble(),  // 映射 O2 传感器数据
            co2 = lungTestData.co2SensorData.toDouble(),  // 映射 CO2 传感器数据
            t_ph = lungTestData.temperature,  // 映射温度数据
            sportpool = null  // 假设运动池默认是 null，可以根据需要设置
        )
    }

    fun copyWithNewBreathData(breathData: CPXBreathInOutData? = CPXBreathInOutData()): CPXSerializeData {
        return this.copy(breathData = breathData)
    }

    fun createAnlyCpxTableModel(): List<AnlyCpxTableModel>? {//运动评估数据
        val cpxTableModelList = mutableListOf<AnlyCpxTableModel>()
        breathData ?: return null

        val cpxTableModel = AnlyCpxTableModel(
            time = String.format("%02d:%02d", (index * 0.005f / 60).toInt(), (index * 0.005f % 60).toInt()),
            load = Load.toInt(),
            speed = Speed.toInt(),
            grade = Grade.toInt(),
            HR = HR.toInt(),
            VO2 = "%.2f".format(breathData!!.VO2),
            VCO2 = "%.2f".format(breathData!!.VCO2),
            VO2_div_kg = "%.2f".format(breathData!!.VO2_div_KG),
            VE = "%.2f".format(breathData!!.VE),
            RER = "%.2f".format(breathData!!.RER),
            BF = breathData!!.BF!!,
            psys = Psys.toInt(),
            pdia = Pdia.toInt(),
            EE = "%.2f".format(breathData!!.EE),
            fat_g = "%.2f".format(breathData!!.FAT),
            fat_kal = "%.2f".format(breathData!!.FAT),
            cho_g = "%.2f".format(breathData!!.CHO),
            cho_kal = "%.2f".format(breathData!!.CHO),
            prot = "%.2f".format(breathData!!.PROT),
            mets = "%.2f".format(breathData!!.METS)
        )
        cpxTableModelList.add(cpxTableModel)
        return cpxTableModelList
    }

}
