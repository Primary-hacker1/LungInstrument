package com.just.machine.model.sixminreport

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.just.machine.dao.ChatItemConverter

/**
 * 6分钟报告其他信息
 */
@Entity(tableName = "sixmin_report_other")
@TypeConverters(ChatItemConverter::class)
data class SixMinReportOther(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Long = 0,
    var reportId: String = "",//报告主id
    var startHighPressure: String = "",//开始高压
    var startLowPressure: String = "",//开始低压
    var stopHighPressure: String = "",//结束高压
    var stopLowPressure: String = "",//结束低压
    var stopOr: String = "",//是否提前停止试验，0为否，1为是
    var stopTime: String = "",//停止时间
    var stopReason: String = "",//停止原因(症状描述)
    var badOr: String = "",//自动结束后是否有不良症状，0为否，1为是
    var badSymptoms: String = "",//自动结束后的不良症状描述
    var useName: String = "",//使用单位名称
    var ecgType: String = "",//导联数量
    var delFlag: String = "0",//删除标记
)
