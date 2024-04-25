package com.just.machine.model.sixminreport

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.just.machine.dao.ChatItemConverter

/**
 * 6分钟报告信息
 */
@Entity(tableName = "sixmin_report_info")
@TypeConverters(ChatItemConverter::class)
data class SixMinReportInfo(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Long = 0,
    var reportNo: String = "",//报告编号
    var patientId: Long = 0,//患者id
    var patientName: String = "",//患者姓名
    var patientSix: String = "",//患者性别
    var patientAge: String = "",//患者年龄
    var patientHeight: String = "",//患者身高
    var patientWeight: String = "",//患者体重
    var patientBmi: String = "",//患者BMI
    var patientStride: String = "",//患者步幅
    var clinicalDiagnosis: String = "",//临床诊断
    var medicineUse: String = "",//目前用药
    var predictionDistance: String = "",//预测距离
    var medicalNo: String = "",//病历号
    var medicalHistory: String = "",//病史
    var bsHxl: String = "",//步数/呼吸率，0为步数，1为呼吸率
    var ecgEdition: String = "",//心电版本
    var restDuration: String = "",//休息时长
    var addTime: String = "",//检验日期
    var delFlag: String = "0"//删除标记
)