package com.just.machine.model

data class CardiopulmonaryRecordsBean(
    var createTime: String? = "0",//测试时间
    var conventionalVentilation: String? = "0",//常规通气量
    var forcedVitalCapacity: String? = "0",//用力肺活量
    var maximumVentilation: String? = "0",//最大通气量
    var exerciseLungTest: String? = "0",//运动肺测试
    var assess: String? = "0",//评估
)
