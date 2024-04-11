package com.just.machine.model

data class SixMinReportPatientSelfBean(
    val itemName: String = "",
    val itemType: String = "",
    val itemList: MutableList<SixMinReportPatientSelfItemBean> = mutableListOf(),
    var itemConclusion: String = ""
)
