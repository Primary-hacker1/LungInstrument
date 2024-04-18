package com.just.machine.model.sixminreport

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.just.machine.dao.ChatItemConverter

/**
 * 6分钟心率
 */
@Entity(tableName = "sixmin_report_heart")
@TypeConverters(ChatItemConverter::class)
data class SixMinReportHeartBeat(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Long = 0,
    var reportId: String = "",//报告主id
    var heartStop: String = "",//静止
    var heartOne: String = "",//1分钟时的心率值
    var heartTwo: String = "",//2分钟时的心率值
    var heartThree: String = "",//3分钟时的心率值
    var heartFour: String = "",//4分钟时的心率值
    var heartFive: String = "",//5分钟时的心率值
    var heartSix: String = "",//6分钟时的心率值
    var heartBig: String = "",//心率最大值
    var heartSmall: String = "",//心率最小值
    var heartAverage: String = "",//心率平均值
    var heartAll: String = "",//心率，每秒一个值，6分钟的集合
    var heartAllData: String = "",//6分钟所有的心率集合
    var heartRestore: String = "",//运动心率恢复值
    var heartRr: String = "",//六分钟的rr值集合
    var heartRrRemarke: String = "",//直方图的描述
    var heartState: String = "",//心率变异分析状态，0为没有，1为有
    var heartVarying: String = "",//心脏变时指数
    var heartConclusion: String = "",//心电结论
    var delFlag: String = "0",//删除标记
)