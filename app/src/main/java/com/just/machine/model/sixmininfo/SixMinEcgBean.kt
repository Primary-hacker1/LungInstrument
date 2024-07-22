package com.just.machine.model.sixmininfo

/**
 * 6分钟心电数据
 */
data class SixMinEcgBean(val heartBeat: String, val ecgList:MutableList<SixMinEcgInfoBean>)
