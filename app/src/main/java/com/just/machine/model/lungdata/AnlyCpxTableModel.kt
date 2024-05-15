package com.just.machine.model.lungdata


data class AnlyCpxTableModel(
    var time: String = "",
    var load: Int = 0,
    var speed: Int = 0,
    var grade: Int = 0,
    var HR: Int = 0,
    var VO2: String = "",
    var VCO2: String = "",
    var VO2_div_kg: String = "",
    var RER: String  = "",
    var VE: String  = "",
    var BF: Double = 0.0,
    var psys: Int = 0,
    var pdia: Int = 0,
    var RPE: Double = 0.0,
    var EE: String = "",
    var prot: String = "",
    var cho_kal: String = "",
    var fat_kal: String = "",
    var cho_g: String = "",
    var fat_g: String = "",
    var mets: String = "",
)

class AnlyCpxTableModelNew {
    var colname: String = ""
    var parvalue: String = ""
}
