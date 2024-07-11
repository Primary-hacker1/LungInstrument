package com.just.machine.dao.calibration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 成分定标结果实体类
 */
@Entity(tableName = "ingredientCalibrationResult")
data class IngredientCalibrationResultBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var ingredientId: Long = 0,
    var calibrationTime: String? = "",//定标时间
    var kCO2: String? = "",//二氧化碳Y=kX+b
    var bCO2: String? = "",//二氧化碳Y=kX+b
    var kO2: String? = "",//氧气Y=kX+b
    var bO2: String? = "",//氧气Y=kX+b
    var cO2Offset: String? = "",//二氧化碳时间延迟
    var o2Offset: String? = "",//氧气时间延迟
    var cO2T90: String? = "",//二氧化碳90%时间
    var o2T90: String? = "",//氧气90%时间
    var cO2Error: String? = "",//二氧化碳误差
    var o2Error: String? = "",//氧气误差
    var calibrationResult: String? = "",//定标结果 0未通过1通过
)
