package com.just.machine.dao.calibration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient")
data class IngredientBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val ingredientId: Long = 0,
    val createTime: String? = "",
    val userId: Long,
    var inspiratoryIndicators: String? = "",//浓度
    var calibratedValue: String? = "",//标定值
    var actual: String? = "",//实际值
    var errorRate: String? = "",//误差率
    var calibrationResults: String? = "",//标定结果
    var o2t90: String? = "",//O2T90（ms）
    var offset : String? = "",
)
