package com.just.machine.dao.lung

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.just.machine.dao.PatientBean

/**
 * 动态肺数据bean
 * 用于存储数据库并绑定患者
 */
@Entity(
    tableName = "cpx_breath",
    foreignKeys = [androidx.room.ForeignKey(//表关联患者
        entity = PatientBean::class,
        parentColumns = ["id"],
        childColumns = ["patientId"],
        onDelete = androidx.room.ForeignKey.CASCADE
    )],
    indices = [Index("patientId")]
)

data class CPXBreathInOutData(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var dynmicId: Long = 0,

    var patientId: Long = 0,

    var createTime: String? = "",
    var VTin: Double? = 0.0,
    var VCO2: Double? = 0.0,
    var RER: Double? = 0.0,
    var VO2_div_HR: Double? = 0.0,
    var VO2_div_KG: Double? = 0.0,
    var BR: Double? = 0.0,
    var VE_div_VO2: Double? = 0.0,
    var VE_div_VCO2: Double? = 0.0,
    var VO2: Double? = 0.0,
    var Qtc: Double? = 0.0,
    var PROT: Double? = 0.0,
    var EE: Double? = 0.0,
    var CHO: Double? = 0.0,
    var FAT: Double? = 0.0,
    var METS: Double? = 0.0,
    var ND: Double? = 0.0,
    var NPRQ: Double? = 0.0,
    var SVc: Double? = 0.0,
    var VdCO2: Double? = 0.0,
    var VI: Double? = 0.0,
    var PiO2: Double? = 0.0,
    var FiO2: Double? = 0.0,
    var FiCO2: Double? = 0.0,
    var VTex: Double? = 0.0,
    var FeO2: Double? = 0.0,
    var FeCO2: Double? = 0.0,
    var PaO2: Double? = 0.0,
    var PaCO2: Double? = 0.0,
    var VdO2: Double? = 0.0,
    var PiCO2: Double? = 0.0,
    var PeTO2: Double? = 0.0,
    var PeTCO2: Double? = 0.0,
    var Ttot: Double? = 0.0,
    var Tin_div_Ttot: Double? = 0.0,
    var BF: Double? = 0.0,
    var VE: Double? = 0.0,
    var VD_div_VT: Double? = 0.0,
    var VD: Double? = 0.0,
    var REE: Double? = 0.0,
) {

    fun toMutableList(): MutableList<Pair<String, Any?>> {
        return mutableListOf(
            "createTime" to createTime,
            "VTin" to VTin,
            "VCO2" to VCO2,
            "RER" to RER,
            "VO2_div_HR" to VO2_div_HR,
            "VO2_div_KG" to VO2_div_KG,
            "BR" to BR,
            "VE_div_VO2" to VE_div_VO2,
            "VE_div_VCO2" to VE_div_VCO2,
            "VO2" to VO2,
            "Qtc" to Qtc,
            "PROT" to PROT,
            "EE" to EE,
            "CHO" to CHO,
            "FAT" to FAT,
            "METS" to METS,
            "ND" to ND,
            "NPRQ" to NPRQ,
            "SVc" to SVc,
            "VdCO2" to VdCO2,
            "VI" to VI,
            "PiO2" to PiO2,
            "FiO2" to FiO2,
            "FiCO2" to FiCO2,
            "VTex" to VTex,
            "FeO2" to FeO2,
            "FeCO2" to FeCO2,
            "PaO2" to PaO2,
            "PaCO2" to PaCO2,
            "VdO2" to VdO2,
            "PiCO2" to PiCO2,
            "PeTO2" to PeTO2,
            "PeTCO2" to PeTCO2,
            "Ttot" to Ttot,
            "Tin_div_Ttot" to Tin_div_Ttot,
            "BF" to BF,
            "VE" to VE,
            "VD_div_VT" to VD_div_VT,
            "VD" to VD,
            "REE" to REE
        )
    }

//    fun isDataOk(): Boolean {
//        return VO2 in 0.0..15000.0 &&
//                (VCO2 in 0.0..15000.0) &&
//                (VE_div_VO2 in 0.0..1000.0 && VE_div_VCO2 >= 0.0) &&
//                VE_div_VCO2 <= 1000.0
//    }
}
