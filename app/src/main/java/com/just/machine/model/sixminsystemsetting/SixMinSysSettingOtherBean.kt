package com.just.machine.model.sixminsystemsetting

data class SixMinSysSettingOther(
    var circleCountType: String = "0", //记圈方式 0自动 1手动
    var useOrg: String = "",//使用单位
    var areaLength: String = "30",//场地长度
    var broadcastVoice: String = "1",//是否播报引导语言 0否 1是
    var ectType: String = "1",//心电导联类型
    var autoMeasureBlood: String = "1",//是否自动测量血压 0否 1是
    var stepsOrBreath: String = "0",//步数/呼吸率  0步数 1呼吸率
    var autoStart: String = "1",//是否自动开始试验  0否 1是
    var showResetTime: String = "1"//是否显示休息时长 0否 1是
)