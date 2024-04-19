package com.just.machine.model.sixminreport

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.just.machine.dao.ChatItemConverter

/**
 * 6分钟报告步数
 */
@Entity(tableName = "sixmin_report_walk")
@TypeConverters(ChatItemConverter::class)
data class SixMinReportWalk(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Long = 0,
    var reportId: String = "",//报告主id
    var walkStop: String = "0",//静止
    var walkOne: String = "0",//1分钟时的步数值
    var walkTwo: String = "0",//2分钟时的步数值
    var walkThree: String = "0",//3分钟时的步数值
    var walkFour: String = "0",//4分钟时的步数值
    var walkFive: String = "0",//5分钟时的步数值
    var walkSix: String = "0",//6分钟时的步数值
    var walkBig: String = "0",//步数最大值
    var waklSmall: String = "0",//步数最小值
    var walkAverage: String = "0",//步数平均值
    var delFlag: String = "0",//删除标记
)
