package com.just.machine.model.sixminreport

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.just.machine.dao.ChatItemConverter

/**
 * 6分钟报告呼吸率
 */
@Entity(tableName = "sixmin_report_breathing")
@TypeConverters(ChatItemConverter::class)
data class SixMinReportBreathing(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Long = 0,
    var reportId: String = "",//报告主id
    var breathingStop: String = "",//静止
    var breathingOne: String = "",//1分钟时的呼吸频率值
    var breathingTwo: String = "",//2分钟时的呼吸频率值
    var breathingThree: String = "",//3分钟时的呼吸频率值
    var breathingFour: String = "",//4分钟时的呼吸频率值
    var breathingFive: String = "",//5分钟时的呼吸频率值
    var breathingSix: String = "",//6分钟时的呼吸频率值
    var breathingBig: String = "",//呼吸频率最大值
    var breathingSmall: String = "",//呼吸频率最小值
    var breathingAverage: String = "",//呼吸频率平均值
    var breathingAll: String = "",//呼吸，每3秒一个值，6分钟的集合
    var delFlag: String = "",//删除标记
)
