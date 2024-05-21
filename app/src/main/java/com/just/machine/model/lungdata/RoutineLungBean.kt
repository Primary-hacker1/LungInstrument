package com.just.machine.model.lungdata

import android.view.View

data class RoutineLungBean(
    var severalParticipants: String? = "",//参数名
    var unit: String? = "",//单位
    var estimated: String? = "",//预计值
    var optimalValue: String? = "",//最佳值
    var bp: String? = "",//B/P(%)
    var test1: String? = "",//测试
    var test2: String? = "",//测试
    var test3: String? = "",//测试
    var test4: String? = "",//测试
    var test5: String? = "",//测试
    var isVisible: Int? = View.GONE,
){

    override fun toString(): String {
        return "RoutineLungBean(severalParticipants=$severalParticipants, unit=$unit, estimated=$estimated, " +
                "optimalValue=$optimalValue, bp=$bp, test1=$test1, test2=$test2, test3=$test3, test4=$test4, test5=$test5)"
    }
}