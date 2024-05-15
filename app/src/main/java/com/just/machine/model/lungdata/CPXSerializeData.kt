package com.just.machine.model.lungdata

import com.just.machine.dao.lung.CPXBreathInOutData
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

    fun copyWithNewBreathData(breathData: CPXBreathInOutData? = CPXBreathInOutData()): CPXSerializeData {
        return this.copy(breathData = breathData)
    }

    fun createAnlyCpxTableModel(): List<AnlyCpxTableModel>? {
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
            BF = breathData!!.BF,
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
