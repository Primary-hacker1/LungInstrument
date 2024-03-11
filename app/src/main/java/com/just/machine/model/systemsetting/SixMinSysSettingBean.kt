package com.just.machine.model.systemsetting

data class SixMinSysSettingBean(
    var sysAlarm: SixMinSysSettingAlarmBean,
    var sysOther: SixMinSysSettingOther,
    var sysPwd: SixMinSysSettingPwdBean,
    var sysBlue: SixMinSysSettingBluetoothBean,
    var publishVer: String = "",//发布版本
    var completeVer: String = "",//完整版本
    var copyRight: String = "@2020知心健（南京）科技有限公司版权所有",//版权
)
