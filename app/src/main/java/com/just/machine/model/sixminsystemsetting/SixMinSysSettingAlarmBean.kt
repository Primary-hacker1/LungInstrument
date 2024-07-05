package com.just.machine.model.sixminsystemsetting

data class SixMinSysSettingAlarmBean(
    var highPressure: String = "140",//收缩压报警值
    var lowPressure: String = "90",//舒张压报警值
    var heartBeat: String = "60",//心率报警值
    var bloodOxy: String = "95"//血氧报警值
)
