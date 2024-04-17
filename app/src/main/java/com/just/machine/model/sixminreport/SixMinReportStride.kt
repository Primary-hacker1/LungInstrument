package com.just.machine.model.sixminreport

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.just.machine.dao.ChatItemConverter

/**
 * 6分钟报告步速
 */
@Entity(tableName = "sixmin_report_stride")
@TypeConverters(ChatItemConverter::class)
data class SixMinReportStride(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Long = 0,
    var reportId: String = "",//报告主id
    var strideStop: String = "",//静止
    var strideOne: String = "",//1分钟时的步速值
    var strideTwo: String = "",//2分钟时的步速值
    var strideThree: String = "",//3分钟时的步速值
    var strideFour: String = "",//4分钟时的步速值
    var strideFive: String = "",//5分钟时的步速值
    var strideSix: String = "",//6分钟时的步速值
    var strideBig: String = "",//步速最大值
    var strideSmall: String = "",//步速最小值
    var strideAverage: String = "",//步速平均值
    var delFlag: String = "",//删除标记
)
