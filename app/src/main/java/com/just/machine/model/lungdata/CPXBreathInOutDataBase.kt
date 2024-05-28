package com.just.machine.model.lungdata

// 定义一个数据类 CPXBreathInOutDataBase，用于存储呼吸进出数据
data class CPXBreathInOutDataBase(
    var EndRealIndex: Int = 0,   // 结束的真实索引
    var Tin: Double = 0.0,       // 吸气时间
    var TempiTotal: Double = 0.0, // 吸气总时间
    var TempiO2: Double = 0.0,    // 吸气氧气时间
    var TempiCO2: Double = 0.0,   // 吸气二氧化碳时间
    var FiTO2: Double = 0.0,      // 吸气末氧气浓度
    var Tex: Double = 0.0,        // 呼气时间
    var TempeTotal: Double = 0.0, // 呼气总时间
    var TempeO2: Double = 0.0,    // 呼气氧气时间
    var TempeCO2: Double = 0.0,   // 呼气二氧化碳时间
    var FeTO2: Double = 0.0,      // 呼气末氧气浓度
    var FeTCO2: Double = 0.0,     // 呼气末二氧化碳浓度
    var T: Double = 0.0,          // 温度
    var H: Double = 0.0,          // 湿度
    var P: Double = 0.0           // 压力
)
