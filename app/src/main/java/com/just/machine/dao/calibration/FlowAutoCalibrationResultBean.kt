package com.just.machine.dao.calibration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 流速自动定标结果实体类
 */
@Entity(tableName = "flowAutoCalibrationResult")
data class FlowAutoCalibrationResultBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val flowId: Long = 0,
    var calibrationTime: String? = "",//定标时间
    var ratedHighFlow: String?="",//高流速标定流速
    var measuredHighFlow: String? = "",//高流速实测流速
    var highFlowError: String? = "",//高流速误差
    var ratedLowFlow: String?="",//低流速标定流速
    var measuredLowFlow: String? = "",//低流速实测流速
    var lowFlowError: String? = "",//低流速误差
    var calibrationResult: String? = "",//定标结果 0未通过1通过
)
