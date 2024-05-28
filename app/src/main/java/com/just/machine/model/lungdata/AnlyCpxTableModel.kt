package com.just.machine.model.lungdata

// 定义一个数据类 AnlyCpxTableModel，用于存储分析结果的表格数据
data class AnlyCpxTableModel(
    var time: String = "",       // 时间
    var load: Int = 0,           // 负载
    var speed: Int = 0,          // 速度
    var grade: Int = 0,          // 坡度
    var HR: Int = 0,             // 心率 (Heart Rate)
    var VO2: String = "",        // 摄氧量 (VO2)
    var VCO2: String = "",       // 排出二氧化碳量 (VCO2)
    var VO2_div_kg: String = "", // 每公斤体重的摄氧量 (VO2/kg)
    var RER: String  = "",       // 呼吸交换率 (Respiratory Exchange Ratio)
    var VE: String  = "",        // 通气量 (Ventilation)
    var BF: Double = 0.0,        // 呼吸频率 (Breathing Frequency)
    var psys: Int = 0,           // 收缩压 (Systolic Blood Pressure)
    var pdia: Int = 0,           // 舒张压 (Diastolic Blood Pressure)
    var RPE: Double = 0.0,       // 运动自觉疲劳程度 (Rating of Perceived Exertion)
    var EE: String = "",         // 能量消耗 (Energy Expenditure)
    var prot: String = "",       // 蛋白质 (Protein)
    var cho_kal: String = "",    // 碳水化合物热量 (Carbohydrates Calories)
    var fat_kal: String = "",    // 脂肪热量 (Fat Calories)
    var cho_g: String = "",      // 碳水化合物克数 (Carbohydrates in grams)
    var fat_g: String = "",      // 脂肪克数 (Fat in grams)
    var mets: String = ""        // 代谢当量 (Metabolic Equivalent of Task)
)

class AnlyCpxTableModelNew {
    var colname: String = ""
    var parvalue: String = ""
}
