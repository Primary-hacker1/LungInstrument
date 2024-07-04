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
            rpe = 5.0,  // 设定 RPE 默认值为 5.0，可以根据需要调整
            SPO2 = lungTestData.bloodOxygen!!,  // 映射血氧数据
            Pdia = 80.0,  // 设定舒张压默认值为 80.0，可以根据需要调整
            Psys = 120.0,  // 设定收缩压默认值为 120.0，可以根据需要调整
            Grade = 1.0,  // 设定坡度默认值为 1.0，可以根据需要调整
            Speed = 5.0,  // 设定速度默认值为 5.0，可以根据需要调整
            Load = 50.0,  // 设定负载默认值为 50.0，可以根据需要调整
            rpm = lungTestData.highRangeFlowSensorData.toDouble(),  // 映射气体流速传感器数据
            vol = lungTestData.lowRangeFlowSensorData.toDouble(),  // 映射低量程流量传感器数据
            hrr = 10.0,  // 设定 HRR 默认值为 10.0，可以根据需要调整
            HR = 75.0,  // 设定心率默认值为 75.0，可以根据需要调整
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

    fun createAnlyCpxTableModels(): List<AnlyCpxTableModel>? {//运动评估数据
        val cpxTableModelList = mutableListOf<AnlyCpxTableModel>()
        breathData ?: return null

        val cpxTableModel = AnlyCpxTableModel(
            time = String.format(
                "%02d:%02d",
                (index * 0.005f / 60).toInt(),
                (index * 0.005f % 60).toInt()
            ),
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

    fun createAnlyCpxTableModel(data: CPXSerializeData): AnlyCpxTableModel {//运动评估数据
        val cpxTableModel = AnlyCpxTableModel(
            time = String.format(
                "%02d:%02d",
                (index * 0.005f / 60).toInt(),
                (index * 0.005f % 60).toInt()
            ),
            load = Load.toInt(),
            speed = Speed.toInt(),
            grade = Grade.toInt(),
            HR = HR.toInt(),
            VO2 = "%.2f".format(data.breathData!!.VO2),
            VCO2 = "%.2f".format(data.breathData!!.VCO2),
            VO2_div_kg = "%.2f".format(data.breathData!!.VO2_div_KG),
            VE = "%.2f".format(data.breathData!!.VE),
            RER = "%.2f".format(data.breathData!!.RER),
            BF = data.breathData!!.BF!!,
            psys = data.Psys.toInt(),
            pdia = data.Pdia.toInt(),
            EE = "%.2f".format(data.breathData!!.EE),
            fat_g = "%.2f".format(data.breathData!!.FAT),
            fat_kal = "%.2f".format(data.breathData!!.FAT),
            cho_g = "%.2f".format(data.breathData!!.CHO),
            cho_kal = "%.2f".format(data.breathData!!.CHO),
            prot = "%.2f".format(data.breathData!!.PROT),
            mets = "%.2f".format(data.breathData!!.METS)
        )
        return cpxTableModel
    }

}
