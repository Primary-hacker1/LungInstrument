package com.just.machine.model.dynmicdata

data class CPXBreathInOutData(
    var VTin: Double = 0.0,
    var VCO2: Double = 0.0,
    var RER: Double = 0.0,
    var VO2_div_HR: Double = 0.0,
    var VO2_div_KG: Double = 0.0,
    var BR: Double = 0.0,
    var VE_div_VO2: Double = 0.0,
    var VE_div_VCO2: Double = 0.0,
    var VO2: Double = 0.0,
    var Qtc: Double = 0.0,
    var PROT: Double = 0.0,
    var EE: Double = 0.0,
    var CHO: Double = 0.0,
    var FAT: Double = 0.0,
    var METS: Double = 0.0,
    var ND: Double = 0.0,
    var NPRQ: Double = 0.0,
    var SVc: Double = 0.0,
    var VdCO2: Double = 0.0,
    var VI: Double = 0.0,
    var PiO2: Double = 0.0,
    var FiO2: Double = 0.0,
    var FiCO2: Double = 0.0,
    var VTex: Double = 0.0,
    var FeO2: Double = 0.0,
    var FeCO2: Double = 0.0,
    var PaO2: Double = 0.0,
    var PaCO2: Double = 0.0,
    var VdO2: Double = 0.0,
    var PiCO2: Double = 0.0,
    var PeTO2: Double = 0.0,
    var PeTCO2: Double = 0.0,
    var Ttot: Double = 0.0,
    var Tin_div_Ttot: Double = 0.0,
    var BF: Double = 0.0,
    var VE: Double = 0.0,
    var VD_div_VT: Double = 0.0,
    var VD: Double = 0.0
) {


//    fun copy(): CPXBreathInOutData {
//        return clone() as CPXBreathInOutData
//    }

    fun isDataOk(): Boolean {
        return VO2 in 0.0..15000.0 &&
                (VCO2 in 0.0..15000.0) &&
                (VE_div_VO2 in 0.0..1000.0 && VE_div_VCO2 >= 0.0) &&
                VE_div_VCO2 <= 1000.0
    }
}
