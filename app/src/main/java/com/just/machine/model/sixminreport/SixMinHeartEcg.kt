package com.just.machine.model.sixminreport

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.just.machine.dao.ChatItemConverter

/**
 * 6分钟报告心电数据
 */
@Entity(tableName = "sixmin_report_heart_ecg")
@TypeConverters(ChatItemConverter::class)
data class SixMinHeartEcg(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Long = 0,
    var reportId: String = "",//报告主id
    var bigHreatTime: String = "",//最快心率时间
    var bigHreatCountdown: String = "",//最快心率正计时
    var bigHreatEcg: String = "",//最快心率的心电数据
    var bigHreat: String = "",//最快心率值
    var smallHreatTime: String = "",//最慢心率时间
    var smallHreatCountdown: String = "",//最慢心率正计时
    var smallHreatEcg: String = "",//最慢心率的心电数据
    var smallHreat: String = "",//最慢心率值
    var jietuOr: String = "",//是否截图，0为未截图，1为已截图
    var hreatTime: String = "",//心率异常/截图时间
    var hreatEcg: String = "",//心率异常/截图心电数据
    var bigHreatLong: String = "",//心率异常/截图心电数据
    var smallHreatLong: String = "",//心率异常/截图心电数据
    var hreatLong: String = "",//心率异常/截图心电数据
    var hreatRate: String = "",//截取心率值
    var delFlag: String = "",//删除标记
)
