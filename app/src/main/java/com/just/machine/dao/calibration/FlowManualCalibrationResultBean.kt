package com.just.machine.dao.calibration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 流速手动定标结果实体类
 */
@Entity(tableName = "flowManualCalibrationResult")
data class FlowManualCalibrationResultBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val flowId: Long = 0,
    var calibrationTime: String? = "",//定标时间
    var inFluctuation: String? = "",//吸入波动
    var inError: String? = "",//吸入误差
    var outFluctuation: String? = "",//呼出波动
    var outError: String? = "",//呼出误差
    var calibrationResult: String? = "",//定标结果 0未通过1通过
)
