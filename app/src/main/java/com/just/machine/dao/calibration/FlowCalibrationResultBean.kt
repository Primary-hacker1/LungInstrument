package com.just.machine.dao.calibration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 流速定标结果实体类
 */
@Entity(tableName = "flowCalibrationResult")
data class FlowCalibrationResultBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val flowId: Long = 0,
    var calibrationTime: String? = "",//定标时间
    var calibrationType: Long = 0,//定标方式 0手动1自动
    var inCoefficient: String? = "",//吸气系数(低)
    var outCoefficient: String? = "",//呼气系数(低)
    var inHighCoefficient: String? = "",//吸气系数(高)
    var outHighCoefficient: String? = "",//呼气系数(高)
    var calibrationResult: String? = "",//定标结果 0未通过1通过
)
