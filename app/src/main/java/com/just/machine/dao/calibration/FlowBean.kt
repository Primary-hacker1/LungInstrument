package com.just.machine.dao.calibration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "flow")
data class FlowBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val flowId: Long = 0,
    val createTime: String? = "",
    val userId: Long,
    var inspiratoryIndicators: String? = "",//吸气标准指标
    var calibratedValue: String? = "",//标定值
    var actual: String? = "",//实际值
    var errorRate: String? = "",//误差率
    var calibrationResults: String? = "",//标定结果
)
