package com.just.machine.dao.setting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "dynamic_setting")
data class DynamicSettingBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var dynamicId: Long = 0,
    var respiratoryOrderMean: String? = "",//呼吸次数平均
    var isSingularity: Boolean? = false,//奇异点消除
    var isAutomatiFlow: Boolean? = false,//自动流速容量环

    var nineDiagramEstimates: String? = "",//九图预计值显示
    var lungWidth: String? = "",//运动肺图形宽度
    var reasonForTermination: Boolean? = true,//终止原因
    var isExtremum: Boolean? = true,//运动极值分析
    var isOxygen: Boolean? = true,//无氧阈分析
    var isRpe: Boolean? = true,//呼吸代偿点分析
    var isDynamicTrafficAnalysis: Boolean? = true,//动态流量分析

    var isExercisePrescriptionOptions: Boolean? = false,//运动处方选择
)