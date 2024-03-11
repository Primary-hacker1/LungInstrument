package com.just.machine.model.systemsetting


data class SixMinSysSettingPwdBean(
    var exportPwd: String = "",//导出密码
    var modifyPwd: String = "",//修改密码
    var confirmPwd: String = ""//确认密码
)