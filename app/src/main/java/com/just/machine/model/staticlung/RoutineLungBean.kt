package com.just.machine.model.staticlung

data class RoutineLungBean(
    var severalParticipants: String? = "",//参数名
    var unit: String? = "",//单位
    var estimated: String? = "",//预计值
    var optimalValue: String? = "",//最佳值
    var bp: String? = "",//B/P(%)
    var test1: String? = "",//测试
) {
}