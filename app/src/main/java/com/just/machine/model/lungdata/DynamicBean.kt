package com.just.machine.model.lungdata

import com.just.machine.model.CPETParameter

object DynamicBean {

    val cpetParameters = mutableListOf(
        CPETParameter(
            id = 84,
            parameterName = "SVC",
            parameterNameCH = "吸气肺活量",
            parameterType = "svc",
            lowRange = -3.0,
            highRange = 3.0,
            unit = "L",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 85,
            parameterName = "VC_ex",
            parameterNameCH = "呼气肺活量",
            parameterType = "svc",
            lowRange = 0.0,
            highRange = 10.0,
            unit = "L",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 86,
            parameterName = "ERV",
            parameterNameCH = "补呼气量",
            parameterType = "svc",
            lowRange = -10.0,
            highRange = 30.0,
            unit = "L",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 87,
            parameterName = "IRV",
            parameterNameCH = "补吸气量",
            parameterType = "svc",
            lowRange = 100.0,
            highRange = 1000.0,
            unit = "L",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),

        CPETParameter(
            id = 88,
            parameterName = "VT",
            parameterNameCH = "潮气量",
            parameterType = "svc",
            lowRange = -10000.0,
            highRange = 10000.0,
            unit = "L",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 89,
            parameterName = "IC",
            parameterNameCH = "深吸气量",
            parameterType = "svc",
            lowRange = -3.0,
            highRange = 3.0,
            unit = "L",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),

        CPETParameter(
            id = 91,
            parameterName = "FVC",
            parameterNameCH = "用力肺活量",
            parameterType = "fvc",
            unit = "L",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 92,
            parameterName = "FEV1",
            parameterNameCH = "一秒量",
            parameterType = "fvc",
            unit = "L",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 93,
            parameterName = "FEV2",
            parameterNameCH = "二秒量",
            parameterType = "fvc",
            unit = "L",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ), CPETParameter(
            id = 94,
            parameterName = "FEV3",
            parameterNameCH = "三秒量",
            parameterType = "fvc",
            unit = "L",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),

        CPETParameter(
            id = 95,
            parameterName = "FEV6",
            parameterNameCH = "六秒量",
            parameterType = "fvc",
            unit = "L",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 96,
            parameterName = "FEV1/FVC",
            parameterNameCH = "一秒率",
            parameterType = "fvc",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),

        CPETParameter(
            id = 97,
            parameterName = "FEV2/FVC",
            parameterNameCH = "二秒率",
            parameterType = "fvc",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),

        CPETParameter(
            id = 98,
            parameterName = "FEV3/FVC",
            parameterNameCH = "三秒率",
            parameterType = "fvc",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 99,
            parameterName = "FEV6/FVC",
            parameterNameCH = "六秒率",
            parameterType = "fvc",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),

        CPETParameter(
            id = 100,
            parameterName = "PEF",
            parameterNameCH = "用力呼气峰流速",
            parameterType = "fvc",
            unit = "L/s",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 101,
            parameterName = "MVV",
            parameterNameCH = "每分最大通气量",
            parameterType = "mvv",
            unit = "L",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 102,
            parameterName = "TIME_MVV",
            parameterNameCH = "最大通气量的测量时间",
            parameterType = "mvv",
            unit = "S",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 103,
            parameterName = "MEF",
            parameterNameCH = "平均呼气流速",
            parameterType = "fvc",
            unit = "L/s",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 104,
            parameterName = "FEF25",
            parameterNameCH = "25%时用力呼气流速",
            parameterType = "fvc",
            unit = "L/s",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 105,
            parameterName = "FEF50",
            parameterNameCH = "50%时用力呼气流速",
            parameterType = "fvc",
            unit = "L/s",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 106,
            parameterName = "FEF75",
            parameterNameCH = "75%时用力呼气流速",
            parameterType = "fvc",
            unit = "L/s",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 107,
            parameterName = "MMEF",
            parameterNameCH = "25%到75%时平均中期流速",
            parameterType = "fvc",
            unit = "L/s",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 108,
            parameterName = "FET",
            parameterNameCH = "用力呼气时间",
            parameterType = "fvc",
            unit = "S",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 109,
            parameterName = "FEF200-1200",
            parameterNameCH = "用力呼气200-1200ml时间",
            parameterType = "fvc",
            unit = "L/s",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 110,
            parameterName = "PIF",
            parameterNameCH = "吸气峰流速",
            parameterType = "fvc",
            unit = "L/s",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 111,
            parameterName = "FIF50",
            parameterNameCH = "吸气50%流速",
            parameterType = "fvc",
            unit = "L/s",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 112,
            parameterName = "FIV1",
            parameterNameCH = "吸气一秒量",
            parameterType = "fvc",
            unit = "L",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 113,
            parameterName = "FIV1%FVC",
            parameterNameCH = "吸气一秒率",
            parameterType = "fvc",
            unit = "%",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 114,
            parameterName = "FEF50%FIF50",
            parameterNameCH = "吸呼50%比",
            parameterType = "fvc",
            unit = "",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 115,
            parameterName = "FEV1%FIV1",
            parameterNameCH = "呼、吸一秒量比",
            parameterType = "fvc",
            unit = "",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 116,
            parameterName = "FEF75/85",
            parameterNameCH = "75%用力呼气流速比85%",
            parameterType = "fvc",
            unit = "",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 117,
            parameterName = "TIN/ TTOT",
            parameterNameCH = "吸气时间比",
            parameterType = "fvc",
            unit = "",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 118,
            parameterName = "TEX/ TTOT",
            parameterNameCH = "呼气时间比",
            parameterType = "fvc",
            unit = "",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 119,
            parameterName = "TIN/TEX",
            parameterNameCH = "吸呼时间比",
            parameterType = "fvc",
            unit = "",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 120,
            parameterName = "T TOT",
            parameterNameCH = "吸呼总时间",
            parameterType = "fvc",
            unit = "",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 121,
            parameterName = "MIF",
            parameterNameCH = "平均吸气流速",
            parameterType = "fvc",
            unit = "L/s",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 122,
            parameterName = "Vol extrap",
            parameterNameCH = "外推容量",
            parameterType = "fvc",
            unit = "L",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 126,
            parameterName = "FET25--75%",
            parameterNameCH = "用力呼气25%到75%时间",
            parameterType = "fvc",
            unit = "S",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 127,
            parameterName = "FVC IN",
            parameterNameCH = "用力肺活量（吸气）",
            parameterType = "fvc",
            unit = "L",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 132,
            parameterName = "BF",
            parameterNameCH = "呼吸频率",
            parameterType = "MVV",
            unit = "1/Min",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 133,
            parameterName = "MMEF",
            parameterNameCH = "呼气中段平均流速",
            parameterType = "fvc",
            unit = "L/min",
            isShow = false,
            isReport = null,
            remark = "静态肺"
        ),
        CPETParameter(
            id = 134,
            parameterName = "VO2",
            parameterNameCH = "氧耗量",
            parameterType = "cpx",
            unit = "ml/Min",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 135,
            parameterName = "VO2/KG",
            parameterNameCH = "公斤氧耗量",
            parameterType = "cpx",
            unit = "ml/min/kg",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 136,
            parameterName = "VO2/HR",
            parameterNameCH = "每博耗氧量",
            parameterType = "cpx",
            unit = "ml/beat",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 137,
            parameterName = "Psys",
            parameterNameCH = "收缩压",
            parameterType = "cpx",
            unit = "mmhg",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 138,
            parameterName = "VTin",
            parameterNameCH = "潮气量",
            parameterType = "cpx",
            unit = "L",
            isShow = true,
            isReport = true,
            remark = null
        ),
        CPETParameter(
            id = 139,
            parameterName = "RER",
            parameterNameCH = "呼吸交换律",
            parameterType = "cpx",
            unit = "1",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 141,
            parameterName = "VCO2",
            parameterNameCH = "二氧化碳残排出量",
            parameterType = "cpx",
            unit = "ml/Min",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 142,
            parameterName = "Pdia",
            parameterNameCH = "舒张压",
            parameterType = "cpx",
            unit = "mmhg",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 143,
            parameterName = "SPO2",
            parameterNameCH = "血氧饱和度",
            parameterType = "cpx",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 144,
            parameterName = "Load",
            parameterNameCH = "功率",
            parameterType = "cpx",
            unit = "W",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 145,
            parameterName = "RPM",
            parameterNameCH = "转速",
            parameterType = "cpx",
            unit = "r/Min",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 146,
            parameterName = "Speed",
            parameterNameCH = "速度",
            parameterType = "cpx",
            unit = "KM/Hour",
            isShow = false,
            isReport = null,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 147,
            parameterName = "Grade",
            parameterNameCH = "坡度",
            parameterType = "cpx",
            unit = "o",
            isShow = false,
            isReport = null,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 148,
            parameterName = "VD/VT",
            parameterNameCH = "死腔量与潮气量比值",
            parameterType = "cpx",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 149,
            parameterName = "Kcal",
            parameterNameCH = "卡路里",
            parameterType = "cpx",
            unit = "Ka",
            isShow = false,
            isReport = null,
            remark = null
        ),
        CPETParameter(
            id = 150,
            parameterName = "VE/VO2",
            parameterNameCH = "氧通气当量",
            parameterType = "cpx",
            unit = "",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 151,
            parameterName = "VE/VCO2",
            parameterNameCH = "二氧化碳通气当量",
            parameterType = "cpx",
            unit = "",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 152,
            parameterName = "BR",
            parameterNameCH = "通气储备",
            parameterType = "cpx",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 153,
            parameterName = "HRR",
            parameterNameCH = "心率储备",
            parameterType = "cpx",
            unit = "1/Min",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 155,
            parameterName = "Time",
            parameterNameCH = "时间(分)",
            parameterType = "cpx",
            unit = "Min",
            isShow = true,
            isReport = true,
            remark = null
        ),
        CPETParameter(
            id = 156,
            parameterName = "FiO2",
            parameterNameCH = "吸入氧浓度",
            parameterType = "cpx",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = ""
        ),
        CPETParameter(
            id = 157,
            parameterName = "PeTO2",
            parameterNameCH = "呼气末氧分压",
            parameterType = "cpx",
            unit = "mmHg",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 158,
            parameterName = "PeTCO2",
            parameterNameCH = "呼气末二氧化碳分压",
            parameterType = "cpx",
            unit = "mmHg",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 159,
            parameterName = "O2pulse",
            parameterNameCH = "每搏氧耗量",
            parameterType = "cpx",
            unit = "L",
            isShow = false,
            isReport = null,
            remark = ""
        ),

        CPETParameter(
            id = 160,
            parameterName = "METS",
            parameterNameCH = "代谢当量",
            parameterType = "cpx",
            unit = "1",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),

        CPETParameter(
            id = 161,
            parameterName = "CO",
            parameterNameCH = "心排血量",
            parameterType = "cpx",
            unit = "L",
            isShow = false,
            isReport = null,
            remark = ""
        ),

        CPETParameter(
            id = 162,
            parameterName = "VO2/W slope",
            parameterNameCH = "摄氧量做功斜率",
            parameterType = "calc",
            unit = "",
            isShow = true,
            isReport = null,
            remark = ""
        ),

        CPETParameter(
            id = 163,
            parameterName = "VO2@AT",
            parameterNameCH = "AT摄氧量",
            parameterType = "calc",
            unit = "ml/Min",
            isShow = true,
            isReport = null,
            remark = ""
        ),

        CPETParameter(
            id = 164,
            parameterName = "OUES",
            parameterNameCH = "摄氧效率斜率",
            parameterType = "calc",
            unit = "",
            isShow = true,
            isReport = null,
            remark = ""
        ),

        CPETParameter(
            id = 165,
            parameterName = "EE",
            parameterNameCH = "热量消耗",
            parameterType = "cpx",
            unit = "cal/day",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 166,
            parameterName = "Time(S)",
            parameterNameCH = "时间(秒)",
            parameterType = "fvc",
            unit = "Sec",
            isShow = false,
            isReport = null,
            remark = null
        ),

        CPETParameter(
            id = 168,
            parameterName = "VE/VCO2 slope",
            parameterNameCH = "二氧化碳通气当量斜率",
            parameterType = "calc",
            unit = "",
            isShow = true,
            isReport = null,
            remark = ""
        ),

        CPETParameter(
            id = 169,
            parameterName = "PaO2",
            parameterNameCH = "呼气前氧分压",
            parameterType = "cpx",
            unit = "mmHg",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),

        CPETParameter(
            id = 170,
            parameterName = "PaCO2",
            parameterNameCH = "呼气前二氧化碳分压",
            parameterType = "cpx",
            unit = "mmHg",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),

        CPETParameter(
            id = 171,
            parameterName = "VE",
            parameterNameCH = "每分通气量",
            parameterType = "cpx",
            unit = "L/Min",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),

        CPETParameter(
            id = 172,
            parameterName = "FeO2",
            parameterNameCH = "呼出氧浓度",
            parameterType = "cpx",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = ""
        ),

        CPETParameter(
            id = 173,
            parameterName = "FeCO2",
            parameterNameCH = "呼出二氧化碳浓度",
            parameterType = "cpx",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = ""
        ),

        CPETParameter(
            id = 174,
            parameterName = "FETO2",
            parameterNameCH = "完结时氧浓度",
            parameterType = "cpx",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = ""
        ),

        CPETParameter(
            id = 175,
            parameterName = "FETCO2",
            parameterNameCH = "完结时二氧化碳浓度",
            parameterType = "cpx",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = ""
        ),

        CPETParameter(
            id = 176,
            parameterName = "FiCO2",
            parameterNameCH = "吸入二氧化碳浓度",
            parameterType = "cpx",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 177,
            parameterName = "Time",
            parameterNameCH = "时间(秒)",
            parameterType = "cpx",
            unit = "0:00",
            isShow = true,
            isReport = null,
            remark = "运动肺"
        ),

        CPETParameter(
            id = 178,
            parameterName = "Time(S)",
            parameterNameCH = "时间(秒)",
            parameterType = "MVV",
            unit = "Sec",
            isShow = false,
            isReport = null,
            remark = null
        ),

        CPETParameter(
            id = 180,
            parameterName = "FEV05",
            parameterNameCH = "半秒量",
            parameterType = "fvc",
            unit = "L",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),

        CPETParameter(
            id = 181,
            parameterName = "FEV05/FVC",
            parameterNameCH = "半秒率",
            parameterType = "fvc",
            unit = "%",
            isShow = true,
            isReport = true,
            remark = "静态肺"
        ),

        CPETParameter(
            id = 183,
            parameterName = "HR",
            parameterNameCH = "心率",
            parameterType = "cpx",
            unit = "1/Min",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),

        CPETParameter(
            id = 188,
            parameterName = "EqO2@AT",
            parameterNameCH = "AT氧气通气当量",
            parameterType = "calc",
            unit = null,
            isShow = false,
            isReport = null,
            remark = ""
        ),

        CPETParameter(
            id = 189,
            parameterName = "BF",
            parameterNameCH = "呼吸频率",
            parameterType = "cpx",
            unit = null,
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),

        CPETParameter(
            id = 190,
            parameterName = "VTex",
            parameterNameCH = "呼气潮气量",
            parameterType = "cpx",
            unit = "L",
            isShow = true,
            isReport = true,
            remark = null
        ),

        CPETParameter(
            id = 191,
            parameterName = "Prot",
            parameterNameCH = "蛋白消耗",
            parameterType = "cpx",
            unit = "g/day",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),

        CPETParameter(
            id = 192,
            parameterName = "Fat",
            parameterNameCH = "脂肪消耗",
            parameterType = "cpx",
            unit = "g/day",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        ),
        CPETParameter(
            id = 193,
            parameterName = "Cho",
            parameterNameCH = "糖分消耗",
            parameterType = "cpx",
            unit = "g/day",
            isShow = true,
            isReport = true,
            remark = "运动肺"
        )
    )


    fun spinnerItemData(parameterType: String): CPETParameter {
        // 使用 find 函数查找第一个符合条件的元素
        return cpetParameters.find { it.parameterName == parameterType }!!
    }

    fun spinnerItemDatas(): MutableList<CPETParameter> {//全部
        return cpetParameters

    }

    fun getParameterAndValue(
        parameterType: String,
        age: Double,
        height: Double,
        weight: Double,
        isMale: Boolean
    ): Pair<String?, String?> {
        val parameter = spinnerItemData(parameterType)
        val value = LungFormula.main(
            parameter = parameterType,
            age = age,
            heightCm = height,
            weightKg = weight,
            isMale = isMale
        ).toString()
        return Pair(parameter?.parameterNameCH, value)
    }

}
