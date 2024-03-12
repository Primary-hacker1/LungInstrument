package com.just.machine.model.systemsetting

data class SixMinSysSettingBean(
    var sysAlarm: SixMinSysSettingAlarmBean = SixMinSysSettingAlarmBean(),
    var sysOther: SixMinSysSettingOther = SixMinSysSettingOther(),
    var sysPwd: SixMinSysSettingPwdBean = SixMinSysSettingPwdBean(),
    var sysBlue: SixMinSysSettingBluetoothBean = SixMinSysSettingBluetoothBean(),
    var publishVer: String = "v1",//发布版本
    var completeVer: String = "v1.0.2.210708",//完整版本
    var copyRight: String = "@2020知心健（南京）科技有限公司版权所有",//版权
)
