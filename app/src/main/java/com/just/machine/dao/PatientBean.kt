package com.just.machine.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Calendar.DAY_OF_YEAR

/*
基本信息：姓名、性别、身高、体重、身份证、生日、年龄、BMI
病例信息：病历号、预测距离、病史、目前用药、临床诊断、备注
字段限制
3.1姓名 此为必填字段；字符长度为1-10；
3.2性别 此为必填字段；
此为下拉单选框，默认值为男；
应能自动映射到后面的预测距离字段，反之验证不通过，触发方式为改变选择框值；
3.3身高 此为必填字段；
仅能填写1-999的整数；
应能自动映射到后面的BMI和预测距离字段，反之验证不通过，触发方式为改变文本框值；
3.4体重 此为必填字段；
仅能填写1-999的整数；
应能自动映射到后面的BMI和预测距离字段，反之验证不通过，触发方式为改变文本框值；
3.5身份证 此为非必填字段；
字符长度为18；
若填写该字段，应能自动映射到后面的生日和年龄字段，反之验证不通过，触发方式为改变文本框值；
3.6生日 此为必填字段；
此为选择框，无法手动输入；
点击该文本框后，选择年月日后，自动映射到后面的年龄字段，反之验证不通过，触发方式为选择年月日；
3.7年龄 此为无法手动编辑字段；
通过上面的生日字段，自动映射；
仅能通过1-999的整数；
应能自动映射到后面的预测距离字段，反之验证不通过，触发方式为改变文本框值；
3.8 BMI 此为无法手动编辑字段；
仅能通过0.1-99.9的小数，小数精确1位；
通过身高和体重字段，自动映射，计算公式=
体重/((身高/100)*(身高/100))
3.9病历号 此为必填字段；字符长度为1-40；
3.10预测距离 此为无法手动编辑字段；
仅能通过0.1-9999.9的小数，小数精确1位；
通过身高、体重、年龄和性别字段，自动映射，计算公式=
男性：757x身高(m)-5.02x年龄-1.76x体重(kg)-309
女性：211x身高(m)-5.78x年龄-2.29x体重(kg)+677
3.11病史 此为非必填字段；字符长度为0-40；
3.12目前用药 此为非必填字段；字符长度为0-40；
3.13临床诊断 此为非必填字段；字符长度为0-40；
3.14备注 此为非必填字段；字符长度为0-40；*/
@Entity(tableName = "patients")
data class PatientBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val patientId: Long = 0,
    var name: String = "",//姓名
    var sex: String = "",//性别
    var height: String = "",//身高
    var weight: String = "",//体重
    var identityCard: String = "",//身份证
    var birthday: String = "",//生日
    var age: String = "",//年龄
    var BMI: String = "",
    var medicalRecordNumber: String = "",//病例号
    var predictDistances: String = "",//预测距离
    var diseaseHistory: String = "",//病史
    var currentMedications: String = "",//目前用药
    var clinicalDiagnosis: String = "",//临床诊断
    var remark: String = "",//备注
)
