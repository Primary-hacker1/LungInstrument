package com.just.machine.model.sixminreport

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.just.machine.dao.ChatItemConverter

/**
 * 6分钟报告血氧
 */
@Entity(tableName = "sixmin_report_blood")
@TypeConverters(ChatItemConverter::class)
data class SixMinBloodOxygen(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Long = 0,
    var reportId: String = "",//报告主id
    var bloodStop: String = "",//静止
    var bloodOne: String = "",//1分钟时的血氧值
    var bloodTwo: String = "",//2分钟时的血氧值
    var bloodThree: String = "",//3分钟时的血氧值
    var bloodFour: String = "",//4分钟时的血氧值
    var bloodFive: String = "",//5分钟时的血氧值
    var bloodSix: String = "",//6分钟时的血氧值
    var bloodBig: String = "",//血氧最大值
    var bloodSmall: String = "",//血氧最小值
    var bloodAverage: String = "",//血氧平均值
    var bloodAll: String = "",//血氧，每秒一个值，6分钟的集合
    var delFlag: String = "0",//删除标记
)
