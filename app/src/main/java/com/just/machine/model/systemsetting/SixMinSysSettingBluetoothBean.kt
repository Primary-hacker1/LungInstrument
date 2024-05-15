package com.just.machine.model.systemsetting


data class SixMinSysSettingBluetoothBean(
    var ecgBlue: String = "0006661BFB5D", //心电蓝牙
    var bloodBlue: String = "44EAD8FA8FF0", //血压蓝牙
    var bloodOxyBlue: String = "00A050C23588"//血氧蓝牙
)
