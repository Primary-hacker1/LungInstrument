package com.just.machine.model.systemsetting


data class SixMinSysSettingBluetoothBean(
    var ecgBlue: String = "", //心电蓝牙
    var bloodBlue: String = "", //血压蓝牙
    var bloodOxyBlue: String = ""//血氧蓝牙
)
