package com.just.machine.model.lungdata

import java.io.File

class SystemSetting private constructor() {
    companion object {
        private val filePath =""
//        = File(AppDomain.CurrentDomain.BaseDirectory, "Config\\system.ini").absolutePath
        private val commonPath = ""
//            File(AppDomain.CurrentDomain.BaseDirectory, "config\\common.ini").absolutePath

        // Singleton instance
        private var instance: SystemSetting? = null

        fun getInstance(): SystemSetting {
            if (instance == null) {
                instance = SystemSetting()
            }
            return instance!!
        }
    }

    // 通用设置
    var hospitalname: String = ""
    var ecgdevice: Int = 0
    var testdevice: Int = 0
    var prename: Int = 0
    var vd: Int = 0
    var breathmask: Int = 0
    var isVoice: Int = 0
    var isRememberPassword: String = ""
    var device: String = ""

    // SVC设置
    var svcx: Int = 0
    var svcymax: Int = 0
    var svcymin: Int = 0
    var vc: Int = 0
    var vt: Int = 0
    var svcMedia: Int = 0

    // FVC设置
    var fvcx: Int = 0
    var fvcymax: Int = 0
    var fvcymin: Int = 0
    var isshowfvcpre: Int = 0
    var fvcMedia: Int = 0

    // MVV设置
    var mvvx: Int = 0
    var mvvymax: Int = 0
    var mvvymin: Int = 0
    var isshowstartend: Int = 0
    var isshowmvv: Int = 0
    var mvvMedia: Int = 0

    // 运动肺设置
    var cPXSmoth: Int = 0
    var pointnum: Int = 0
    var isfilter: Int = 0
    var isAutoSpeed: Int = 0
    var vo2: Int = 0
    var vco2: Int = 0
    var hr: Int = 0
    var preRange: Int = 0
    var penwidth: Int = 0
    var excizelimitAnly: Boolean = false
    var atAnly: Boolean = false
    var rcAnly: Boolean = false
    var slopeAnly: Boolean = false
    var flowLooperAnly: Boolean = false
    var rpeAnly: Boolean = false
    var nutritionAnly: Boolean = false
    var exerciseAnly: Boolean = false
    var autoAnly: Boolean = false
    var isBorg: Boolean = false
    var isStt: Boolean = false
    var isCPX: Boolean = false
    var isEcgChange: Boolean = false
    var isEcgConclusion: Boolean = false
    var isExercisePrescription: Boolean = false
    var isBlood: Boolean = false
    var isAppriase: Boolean = false
    var linetype: Int = 0

    // 节拍器
    var warmup: Int = 0
    var excise: Int = 0

    // Common
    var hospitalcode: String = ""
    var isCloud: Boolean = false
    var isHeart: Boolean = false
    var isPlay: Boolean = false

    fun updateTestSetting() {
        val comList = IniHelper.readKeys("通用设置", filePath)
        if (comList.isNotEmpty()) {
            hospitalname = IniHelper.getValue("通用设置", "医院名称", filePath)
            ecgdevice = IniHelper.getValue("通用设置", "心电设备", filePath).toInt()
            testdevice = IniHelper.getValue("通用设置", "运动设备", filePath).toInt()
            prename = IniHelper.getValue("通用设置", "预计值方案选择", filePath).toInt()
            vd = IniHelper.getValue("通用设置", "死腔量", filePath).toInt()
            breathmask = IniHelper.getValue("通用设置", "呼吸面罩", filePath).toInt()
            isVoice = IniHelper.getValue("通用设置", "语音播报", filePath).toInt()
            isRememberPassword = IniHelper.getValue("通用设置", "登录密码", filePath)
        }
        val svcList = IniHelper.readKeys("SVC设置", filePath)
        if (svcList.isNotEmpty()) {
            svcx = IniHelper.getValue("SVC设置", "X轴时间量程", filePath).toInt()
            svcymax = IniHelper.getValue("SVC设置", "Y轴量程上限", filePath).toInt()
            svcymin = IniHelper.getValue("SVC设置", "Y轴量程下限", filePath).toInt()
            vc = IniHelper.getValue("SVC设置", "VC设置", filePath).toInt()
            vt = IniHelper.getValue("SVC设置", "VT基线", filePath).toInt()
            svcMedia = IniHelper.getValue("SVC设置", "视频自动播放", filePath).toInt()
        }
        val fvcList = IniHelper.readKeys("FVC设置", filePath)
        if (fvcList.isNotEmpty()) {
            fvcx = IniHelper.getValue("FVC设置", "X轴时间量程", filePath).toInt()
            fvcymax = IniHelper.getValue("FVC设置", "Y轴量程上限", filePath).toInt()
            fvcymin = IniHelper.getValue("FVC设置", "Y轴量程下限", filePath).toInt()
            isshowfvcpre = IniHelper.getValue("FVC设置", "预测值环", filePath).toInt()
            fvcMedia = IniHelper.getValue("FVC设置", "视频自动播放", filePath).toInt()
        }
        val mvvList = IniHelper.readKeys("MVV设置", filePath)
        if (mvvList.isNotEmpty()) {
            mvvx = IniHelper.getValue("MVV设置", "X轴时间量程", filePath).toInt()
            mvvymax = IniHelper.getValue("MVV设置", "Y轴量程上限", filePath).toInt()
            mvvymin = IniHelper.getValue("MVV设置", "Y轴量程下限", filePath).toInt()
            isshowstartend = IniHelper.getValue("MVV设置", "开始结束标尺线", filePath).toInt()
            isshowmvv = IniHelper.getValue("MVV设置", "通气量曲线", filePath).toInt()
            mvvMedia = IniHelper.getValue("MVV设置", "视频自动播放", filePath).toInt()
        }
        val cpxList = IniHelper.readKeys("运动肺设置", filePath)
        if (cpxList.isNotEmpty()) {
            cPXSmoth = IniHelper.getValue("运动肺设置", "数据平均方法", filePath).toInt()
            pointnum = IniHelper.getValue("运动肺设置", "平均值个数", filePath).toInt()
            isfilter = if (IniHelper.getValue("运动肺设置", "是否消除奇异点", filePath) == "True") 1 else 0
            isAutoSpeed = if (IniHelper.getValue("运动肺设置", "是否自动流速容量环", filePath) == "True") 1 else 0
            vo2 = IniHelper.getValue("运动肺设置", "VO2阈值", filePath).toInt()
            vco2 = IniHelper.getValue("运动肺设置", "VCO2阈值", filePath).toInt()
            hr = IniHelper.getValue("运动肺设置", "心率", filePath).toInt()
            preRange = IniHelper.getValue("运动肺设置", "九图预计值显示", filePath).toInt(85)
            linetype = IniHelper.getValue("运动肺设置", "九图显示类型", filePath).toInt()
            excizelimitAnly = IniHelper.getValue("运动肺设置", "运动极值分析", filePath) == "True"
            atAnly = IniHelper.getValue("运动肺设置", "无氧阈分析", filePath) == "True"
            rcAnly = IniHelper.getValue("运动肺设置", "呼吸代偿点分析", filePath) == "True"
            slopeAnly = IniHelper.getValue("运动肺设置", "斜率分析", filePath) == "True"
            flowLooperAnly = IniHelper.getValue("运动肺设置", "动态流速环分析", filePath) == "True"
            rpeAnly = IniHelper.getValue("运动肺设置", "RPE量表分析", filePath) == "True"
            nutritionAnly = IniHelper.getValue("运动肺设置", "营养代谢分析", filePath) == "True"
            exerciseAnly = IniHelper.getValue("运动肺设置", "运动处方选择", filePath) == "True"
            autoAnly = IniHelper.getValue("运动肺设置", "自动诊断", filePath) == "True"
            penwidth = IniHelper.getValue("运动肺设置", "九图宽度设置", filePath).toInt(1)
            isBorg = IniHelper.getValue("运动肺设置", "Borg", filePath) == "True"
            isStt = IniHelper.getValue("运动肺设置", "静态肺", filePath) == "True"
            isCPX = IniHelper.getValue("运动肺设置", "动态肺", filePath) == "True"
            isEcgChange = IniHelper.getValue("运动肺设置", "心电图变化", filePath) == "True"
            isEcgConclusion = IniHelper.getValue("运动肺设置", "心电图结论", filePath) == "True"
            isExercisePrescription = IniHelper.getValue("运动肺设置", "运动处方", filePath) == "True"
            isBlood = IniHelper.getValue("运动肺设置", "血压", filePath) == "True"
            isAppriase = IniHelper.getValue("运动肺设置", "评价", filePath) == "True"
        }
        val abutmentList = IniHelper.readKeys("通用设置", commonPath)
        if (abutmentList.isNotEmpty()) {
            isHeart = IniHelper.getValue("通用设置", "心脏卫士", commonPath) == "True"
            isCloud = IniHelper.getValue("通用设置", "云平台", commonPath) == "True"
            isPlay = IniHelper.getValue("通用设置", "锦播", commonPath) == "True"
            hospitalcode = IniHelper.getValue("通用设置", "医院代码", commonPath)
        }
        val meterList = IniHelper.readKeys("节拍器", filePath)
        if (meterList.isNotEmpty()) {
            warmup = IniHelper.getValue("节拍器", "热身节拍次数", filePath).toInt()
            excise = IniHelper.getValue("节拍器", "运动节拍次数", filePath).toInt()
        }
    }

}
