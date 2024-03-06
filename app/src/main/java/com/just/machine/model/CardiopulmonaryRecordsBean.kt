package com.just.machine.model

data class CardiopulmonaryRecordsBean(
    var id: String? = "0",
    var title: String? = "xx公",
    var weight: String? = "",//重量
    var price: String? = "",//单价
    var subtotal: String? = "",//小计
    var isCheckBox: Boolean? = false,//是否选中
    var isCheckBoxVisible: Boolean? = false,// 是否显示
    var isMove: Boolean? = null,// 上移还是下移动 true 上移动
)
