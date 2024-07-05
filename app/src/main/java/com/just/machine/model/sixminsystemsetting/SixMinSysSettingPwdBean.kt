package com.just.machine.model.sixminsystemsetting


data class SixMinSysSettingPwdBean(
    var exportPwd: String = "654321",//原密码
    var modifyPwd: String = "",//新密码
    var confirmPwd: String = ""//确认密码
)