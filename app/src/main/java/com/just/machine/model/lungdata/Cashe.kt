//package com.just.machine.model.lungdata
//
//import androidx.databinding.ObservableList
//import java.util.*
//
//object Cashe {
//    val flow = mutableListOf<Float>()
//    val flowino2 = mutableListOf<Float>()
//    val flowinco2 = mutableListOf<Float>()
//    val Vol = mutableListOf<Float>()
//    val time1 = mutableListOf<Float>()
//
//    private var _electricity = -1
//    private var _deviceConnect = false
//    private var _warmLeaveSec = 1800
//    private var _isEnvmentCalibra = false
//    private var _isFlowCalibra = false
//    private var _isIngreCalibra = false
//    private val _programStateModel = ProgramStateModel()
//    private val _patientList = mutableListOf<Patient>()
//    private val _equpType = ObservableList<EquipType>()
//    private val _dyPresentDataModel = CPXPresentDataModel()
//    private var _ND = 12.0
//    private val _listcont = ObservableList<StopReason>()
//    private val _devices = ObservableList<t_devices>()
//    private val _NPRQs = mutableListOf<NPRQModel>()
//    private var _calibrateNum = 0
//    private var _isMysqlConnect = false
//
//    internal var ecgtype: IECGHelper? = null
//
//    // 返回病人列表页面所需
//    var personSearch = ""
//    var personPageNum = 1
//    var personPageIndex = 0
//
//    var pRCForRePort: CPXSerializeData? = null
//    var psportlimitForReport: CPXSerializeData? = null
//    var pATForReport: CPXSerializeData? = null
//    var pMaxO2ForReport: CPXSerializeData? = null
//    var pMaxLoadForReport = 0.0
//    var pRestingForReport: CPXSerializeData? = null
//    var t_Patientdisease: t_patientdisease? = null
//    var lSlopeLoadx_VO2y: MathLinearFunction? = null
//    var lSlopeVCO2x_VEy: MathLinearFunction? = null
//    var lSlopeVEx_VO2y: MathLinearFunction? = null
//    var curdevicetype: DeviceType? = null
//
//    fun clearAnlyCashe() {
//        CurrentRecord = null
//        CurrentPatient = null
//        pRCForRePort = null
//        psportlimitForReport = null
//        pATForReport = null
//        pMaxO2ForReport = null
//        pRestingForReport = null
//        lSlopeLoadx_VO2y = null
//        lSlopeVCO2x_VEy = null
//        lSlopeVEx_VO2y = null
//    }
//
//    var bestNormalBreathTest = -1.0
//    var bestLoopTest = -1.0
//    var bestMaxBreathTest = -1.0
//
//    var electricity: Int
//        get() = _electricity
//        set(value) {
//            _electricity = value
//        }
//
//    var deviceConnect: Boolean
//        get() = _deviceConnect
//        set(value) {
//            _deviceConnect = value
//        }
//
//    var warmLeaveSec: Int
//        get() = _warmLeaveSec
//        set(value) {
//            _warmLeaveSec = value
//        }
//
//    var calibrateNum: Int
//        get() = _calibrateNum
//        set(value) {
//            _calibrateNum = value
//        }
//
//    var needReAnlydy = false
//    var programStateModel: ProgramStateModel
//        get() = _programStateModel
//        set(value) {
//            _programStateModel = value
//        }
//
//    var isEnvmentCalibra: Boolean
//        get() = _isEnvmentCalibra
//        set(value) {
//            _isEnvmentCalibra = value
//        }
//
//    var isFlowCalibra: Boolean
//        get() = _isFlowCalibra
//        set(value) {
//            _isFlowCalibra = value
//        }
//
//    var isIngreCalibra: Boolean
//        get() = _isIngreCalibra
//        set(value) {
//            _isIngreCalibra = value
//        }
//
//    var devices: ObservableList<t_devices>
//        get() = _devices
//        set(value) {
//            _devices = value
//        }
//
//    var isMysqlConnect: Boolean
//        get() = _isMysqlConnect
//        set(value) {
//            _isMysqlConnect = value
//        }
//
//    var currentRecord: SimpleData2<TestSingleRecord>? = null
//    var currentPatient: Patient? = null
//    var currentRecordbeOverdueWhenEnterTest = false
//
//    var patientList: List<Patient>
//        get() = _patientList
//        set(value) {
//            _patientList = value.toMutableList()
//        }
//
//    var equpTypeList: ObservableList<EquipType>
//        get() = _equpType
//        set(value) {
//            _equpType = value
//        }
//
//    var cPXPresentDataModel: CPXPresentDataModel
//        get() = _dyPresentDataModel
//        set(value) {
//            _dyPresentDataModel = value
//        }
//
//    var ND: Double
//        get() = _ND
//        set(value) {
//            _ND = value
//        }
//
//    var listContent: ObservableList<StopReason>
//        get() = _listcont
//        set(value) {
//            _listcont = value
//        }
//
//    var nprqTableValues: List<NPRQModel>
//        get() = _NPRQs
//        set(value) {
//            _NPRQs = value.toMutableList()
//        }
//
//    var istest = false
//    var appraise = ""
//    var isBeforeTestZero = false
//    var anly_diagnose_graph4_do2divhr = 0.0
//    var anly_diagnose_graph4_o2divhr_max = 0.0
//    var anly_diagnose_graph4_spo295min = 0
//    var anly_diagnose_graph4_dspo2 = 0.0
//
//    fun getAdjustNPRQmodel(nprq: Double): NPRQModel {
//        var num = Math.abs(NPRQTableValues[0].NPRQ - nprq)
//        var nprqModel = NPRQTableValues[0]
//        for (nprqTableValue in NPRQTableValues) {
//            if (num > Math.abs(nprqTableValue.NPRQ - nprq)) {
//                num = Math.abs(nprqTableValue.NPRQ - nprq)
//                nprqModel = nprqTableValue
//            }
//        }
//        return nprqModel
//    }
//
//    fun init() {
//        NPRQTable.init(NPRQTableValues)
//    }
//
//    init {
//        init()
//    }
//
//    fun initCashe() {
//        NPRQTable.init(NPRQTableValues)
//        InstanceBase<TestDataCashe>.Instance.getHistoryEnvironment()
//        var indexAT = 0
//        var indexRC = 0
//        var startIndex = 0f
//        var endIndex = 0f
//        var startvco2 = 0f
//        var endvco2 = 0f
//        var startve = 0f
//        var endve = 0f
//        var indexmax = 0
//        val report = DBHelper<t_report>.getObj { it.TestId eq Cashe.currentPatient?.testid }
//        val anly = DBHelper<t_anly>.getObj { it.testid eq Cashe.currentPatient?.testid }
//        if (anly != null) {
//            val max = anly.sportlimit
//            if (max != null) {
//                indexmax = max.split(',')[1].toInt()
//            }
//            val sportstr = anly.anaerobic
//            indexAT = sportstr?.split(',')[3]?.toInt() ?: 0
//            val rcstr = anly.compensation
//            if (rcstr?.split(',')[0] == "1") {
//                indexRC = rcstr.split(',')[1].toInt()
//            }
//            val slopestr = anly.slope
//            if (slopestr != null) {
//                startIndex = slopestr.split(',')[0].toFloat()
//                endIndex = slopestr.split(',')[1].toFloat()
//                startvco2 = slopestr.split(',')[2].toFloat()
//                endvco2 = slopestr.split(',')[3].toFloat()
//                startve = slopestr.split(',')[4].toFloat()
//                endve = slopestr.split(',')[5].toFloat()
//                try {
//                    Cashe.lSlopeVCO2x_VEy = CalculeAnlySlope.calculeVCO2x_VEy(0, startvco2, endvco2, Cashe.currentRecord?.data?.cpxTest?.breathoutList ?: listOf())
//                    Cashe.lSlopeVEx_VO2y = CalculeAnlySlope.calculeVEx_VO2y(0, startve, endve, Cashe.currentRecord?.data?.cpxTest ?: CPXTestData())
//                    Cashe.lSlopeLoadx_VO2y = CalculeAnlySlope.calculedO2_dLoad(0, startIndex, endIndex, Cashe.currentRecord?.data?.cpxTest?.breathoutList ?: listOf(), DeviceType.Ergometer)
//                } catch (a: Exception) {
//                }
//            }
//            t_Patientdisease = anly.prescriptiondetail?.let { DBHelper<t_patientdisease>.getObj(it) }
//        }
//        val cpxdate = InstanceBase<Cashe>.Instance.currentRecord?.data?.cpxTest?.breathoutList
//        if (cpxdate != null && cpxdate.isNotEmpty()) {
//            if (indexAT > 0) {
//                val close = cpxdate.minByOrNull { Math.abs(it.index - indexAT) }?.index ?: 0
//                pATForReport = cpxdate.firstOrNull { it.index == close }
//            }
//            if (indexRC > 0) {
//                val close = cpxdate.minByOrNull { Math.abs(it.index - indexRC) }?.index ?: 0
//                pRCForRePort = cpxdate.firstOrNull { it.index == close }
//            }
//            if (indexmax > 0) {
//                val close = cpxdate.minByOrNull { Math.abs(it.index - indexmax) }?.index ?: 0
//                pMaxO2ForReport = cpxdate.firstOrNull { it.index == close }
//                pMaxLoadForReport = cpxdate.maxOf { it.Load }
//            }
//        }
//    }
//
//    fun getCpxData() {
//        initCashe()
//        val reportData = ReportDataModel()
//        val cpxdate = Cashe.CurrentRecord.Data.cpxTest.breathoutList
//        if (cpxdate == null) return
//
//        // 获取正常值
//        val dc = Cashe.CurrentPatient.normalParModel
//        reportData.vo2atnormal = ">40%"
//        reportData.vo2maxnormal = ">84%"
//        reportData.bfnormal = "8 - 50"
//        val psys = dc["Psys"]
//        val pdia = dc["Pdia"]
//        reportData.bpnormal = "$psys/$pdia"
//        reportData.vdvtnormal = dc["VD/VT"]
//        reportData.hrrnormal = dc["HRR"]
//        reportData.o2hrnormal = dc["VO2/HR"]
//        reportData.vemaxnormal = dc["VE"]
//        reportData.brnormal = "${dc["BR"]}%"
//        reportData.vevco2slopenormal = dc["VE/VCO2 slope"]
//        reportData.vo2_w_slopenormal = dc["VO2/W slope"]
//        reportData.ouesnormal = dc["OUES"]
//        reportData.STTStartTime = InstanceBase<Cashe>.Instance.CurrentRecord.Data.testTime
//        reportData.ExerciseHabit = InstanceBase<Cashe>.Instance.CurrentPatient.ExerciseHabit
//        reportData.RelativeInspection = InstanceBase<Cashe>.Instance.CurrentPatient.RelativeInspection
//        reportData.Remark = InstanceBase<Cashe>.Instance.CurrentPatient.Remarks
//        reportData.TestDate = InstanceBase<Cashe>.Instance.CurrentRecord.Data.testTime.toString("yyyy年MM月dd日")
//        reportData.HospDoor = InstanceBase<Cashe>.Instance.CurrentPatient.AdmissionNumber ?: InstanceBase<Cashe>.Instance.CurrentPatient.PatientNumber
//        reportData.AttendingDoctor = InstanceBase<Cashe>.Instance.CurrentPatient.AttendingDoctor
//
//        if (cpxdate.isNotEmpty()) {
//            val indexwarmup = InstanceBase<Cashe>.Instance.CurrentRecord.Data.cpxTest.Indexwarmup
//            val closesrest = cpxdate.minByOrNull { Math.abs(it.index - indexwarmup + 200) }?.index
//            val cPXrest = cpxdate.filter { it.index < indexwarmup && it.index > indexwarmup - 6000 }
//            val cPXrest1 = cpxdate.firstOrNull { it.index == closesrest }
//            val conert = cpxdate.minByOrNull { Math.abs(it.index - InstanceBase<Cashe>.Instance.CurrentRecord.Data.cpxTest.Indexcovert) }?.index
//            val Cpxcovert = cpxdate.firstOrNull { it.index == conert }
//            val recov1min = cpxdate.minByOrNull { Math.abs(it.index - InstanceBase<Cashe>.Instance.CurrentRecord.Data.cpxTest.Indexcovert - 12000) }?.index
//            val Cpxcovert1min = cpxdate.firstOrNull { it.index == recov1min }
//            val imaxvo2 = cpxdate.filter { it.index <= conert }.maxOf { it.index }
//            val pMaxO2ForReport = cpxdate.firstOrNull { it.index == imaxvo2 }
//            val pMaxLoadForReport = cpxdate.maxOf { it.Load }
//
//            reportData.indexwarmup = indexwarmup
//            reportData.cPXrest = cPXrest
//            reportData.cPXrest1 = cPXrest1
//            reportData.conert = conert
//            reportData.Cpxcovert = Cpxcovert
//            reportData.recov1min = recov1min
//            reportData.Cpxcovert1min = Cpxcovert1min
//            reportData.imaxvo2 = imaxvo2
//            reportData.pMaxO2ForReport = pMaxO2ForReport
//            reportData.pMaxLoadForReport = pMaxLoadForReport
//        }
//        InstanceBase<Cashe>.Instance.CurrentPatient.ReportDataModel = reportData
//    }
//
//    fun getExcisePrescription(detailID: Int): String {
//        val str = StringBuilder()
//        val t_Patientdisease = // 你的 t_Patientdisease 对象
//            t_Patientdisease?.apply {
//                if (Aerobic) {
//                    str.appendln("【有氧】  运动频率：$AerobicFrequency")
//                    str.appendln("运动强度建议：$AerobicStrength")
//                    str.appendln("运动时间参考：$AerobicTime")
//                    str.appendln("可选择运动设备：$AerobicExciseType")
//                }
//                if (Resistance) {
//                    str.appendln("【抗阻】  运动频率：$ResistanceFrequency")
//                    str.appendln("运动强度建议：$ResistanceStrength")
//                    str.appendln("运动时间参考：$ResistanceTime")
//                    str.appendln("可选择运动设备：$ResistanceExciseType")
//                }
//                if (Traction) {
//                    str.appendln("【牵拉】  运动频率：$TractionFrequency")
//                    str.appendln("运动强度建议：$TractionStrength")
//                    str.appendln("运动时间参考：$TractionTime")
//                    str.appendln("可选择运动设备：$TractionExciseType")
//                }
//                if (Aerobic || Resistance || Traction) {
//                    str.appendln("注意事项：为了您的身体健康，请不要过量运动，")
//                    str.appendln("且运动前做5-10分钟的热身运动和运动后做5-10分钟的整理运动，")
//                    str.appendln("运动过程中请注意及时补充水分。")
//                }
//            }
//        return str.toString()
//    }
//
//}
