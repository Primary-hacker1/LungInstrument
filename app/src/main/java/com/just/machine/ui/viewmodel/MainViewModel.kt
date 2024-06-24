package com.just.machine.ui.viewmodel

import android.os.Build
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.viewModelScope
import com.common.network.LogUtils
import com.common.viewmodel.BaseViewModel
import com.common.viewmodel.LiveDataEvent
import com.just.machine.api.UserRepository
import com.just.machine.dao.PatientBean
import com.just.machine.dao.PlantRepository
import com.just.machine.dao.calibration.CalibrationRepository
import com.just.machine.dao.calibration.EnvironmentalCalibrationBean
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.dao.calibration.IngredientBean
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.dao.lung.LungRepository
import com.just.machine.dao.setting.AllSettingBean
import com.just.machine.dao.setting.DynamicSettingBean
import com.just.machine.dao.setting.SettingRepository
import com.just.machine.dao.setting.StaticSettingBean
import com.just.machine.dao.sixmin.SixMinReportInfoRepository
import com.just.machine.model.Data
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.lungdata.DynamicBean
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinHeartEcg
import com.just.machine.model.sixminreport.SixMinReportBreathing
import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.model.sixminreport.SixMinReportHeartBeat
import com.just.machine.model.sixminreport.SixMinReportInfo
import com.just.machine.model.sixminreport.SixMinReportOther
import com.just.machine.model.sixminreport.SixMinReportPrescription
import com.just.machine.model.sixminreport.SixMinReportStride
import com.just.machine.model.sixminreport.SixMinReportWalk
import com.justsafe.libview.view.DateManagementTool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * create by 2024/6/19
 *
 * @author zt
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private var repository: UserRepository,
    private var plantDao: PlantRepository,
    private var environmentalDao: CalibrationRepository,
    private var settingDao: SettingRepository,
    private var lungDao: LungRepository,
    private var sixMinReportInfoDao: SixMinReportInfoRepository,
) : BaseViewModel() {

    var itemNews: ObservableList<Data> = ObservableArrayList()

    /**
     *@param patient 协程请求->直接获取结果的
     */
    fun setDates(patient: PatientBean) {
        viewModelScope.launch {
            val patientId = plantDao.insertPatient(patient)
            patient.patientId = patientId
            SharedPreferencesUtils.instance.patientBean = patient//存到本地
            LogUtils.e(tag + patient.toString())
            getPatients()
        }
    }


    fun setEnvironmental(bean: EnvironmentalCalibrationBean) {
        setBean(
            bean,
            environmentalDao::insertEnvironmental,
            environmentalDao::getEnvironmentals,
            LiveDataEvent.EnvironmentalsSuccess
        )
    }

    fun setFlowBean(bean: FlowBean) {
        setBean(
            bean,
            environmentalDao::insertFlows,
            environmentalDao::getFlows,
            LiveDataEvent.FLOWS_SUCCESS
        )
    }

    fun setIngredientBean(bean: IngredientBean) {
        setBean(
            bean,
            environmentalDao::insertIngredient,
            environmentalDao::getIngredients,
            LiveDataEvent.INGREDIENTS_SUCCESS
        )
    }

    fun getEnvironmental() {//查询所有环境定标
        viewModelScope.launch {
            environmentalDao.getEnvironmentals().collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.EnvironmentalsSuccess, it
                )
            }
        }
    }


    fun getFlows() {//查询所有流量定标
        viewModelScope.launch {
            environmentalDao.getFlows().collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.FLOWS_SUCCESS, it
                )
            }
        }
    }


    fun getIngredients() {//查询所有流量定标
        viewModelScope.launch {
            environmentalDao.getIngredients().collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.INGREDIENTS_SUCCESS, it
                )
            }
        }
    }


    fun insertCPXBreathInOutData(bean: CPXBreathInOutData) {//新增运动参数
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bean.createTime = DateManagementTool.getCurrentDateTime()
        }
        viewModelScope.launch {
            val patient = lungDao.insertCPXBreathInOutData(bean)
            getCPXBreathInOutData()
        }
    }

    fun getCPXBreathInOutData() {//查询当前患者动态肺数据
        viewModelScope.launch {
            val spBena = SharedPreferencesUtils.instance.patientBean
            spBena?.patientId?.let { it ->
                lungDao.getCPXBreathInOutData(it).collect {
                    LogUtils.e(tag + it.toString())
                    mEventHub.value = LiveDataEvent(
                        LiveDataEvent.CPXDYNAMICBEAN, it
                    )
                }
            }
        }
    }

    fun setAllSettingBean(bean: AllSettingBean) {
        bean.allSettingId = 1 //每次都设置id为1覆盖数据库数据
        val allBeans = mutableListOf(bean)
        viewModelScope.launch {
            settingDao.insertAllSettingBeans(allBeans)
        }
    }

    fun getAllSettingBeans() {
        viewModelScope.launch {
            settingDao.getAllSettings().collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.ALL_SETTING_SUCCESS, it
                )
            }
        }
    }

    fun getStaticSettings() {
        viewModelScope.launch {
            settingDao.getStaticSettings().collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.STATICSETTINGSSUCCESS, it
                )
            }
        }
    }


    fun setStaticSettingBean(staticSettingBean: StaticSettingBean) {
//        staticSettingBean.settingId = 1
        viewModelScope.launch {
            try {
                val existingSetting = settingDao.getStaticSettingById(staticSettingBean.settingId)
                if (existingSetting != null) {
                    settingDao.updateStaticSettingBean(staticSettingBean)
                } else {
                    settingDao.insertStaticSettingBean(mutableListOf(staticSettingBean))
                }
            } catch (e: Exception) {
                LogUtils.e(tag + e.message)
            }
        }
    }

    fun getDynamicSettings() {
        viewModelScope.launch {
            settingDao.getDynamicSettings().collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.DYNAMICSUCCESS, it
                )
            }
        }
    }

    fun setDynamicSettingBean(staticSettingBean: DynamicSettingBean) {
        staticSettingBean.dynamicId = 1 //每次都设置id为1覆盖数据库数据
        val staticSettingBeans = mutableListOf(staticSettingBean)
        viewModelScope.launch {
            settingDao.insertDynamicSettingAll(staticSettingBeans)
        }
    }


    fun getPatients() {//查询所有患者
        viewModelScope.launch {
            plantDao.getPatients().collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QuerySuccess, it
                )
            }
        }
    }

    fun getPatientsMax() {//查询最新患者
        viewModelScope.launch {
            val patient = plantDao.getMaxPatient()
            patient?.let {
                it.collect { patientData ->
                    mEventHub.value = LiveDataEvent(
                        LiveDataEvent.MaxPatient, patientData
                    )
                }
            }
        }

    }

    fun getNameOrId(nameOrId: String) {//模糊查询姓名和病历号
        viewModelScope.launch {
            plantDao.getNameOrId(nameOrId).collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QueryNameId, it
                )
            }
        }
    }

    fun getPatient(patientId: Long) {//查询单个患者
        viewModelScope.launch {
            plantDao.getPatient(patientId).collect {
                if (it != null) {
                    mEventHub.value = LiveDataEvent(
                        LiveDataEvent.QueryPatient, it
                    )
                } else {
                    mEventHub.value = LiveDataEvent(
                        LiveDataEvent.QueryPatientNull, "null"
                    )
                }
            }
        }
    }

    fun deletePatient(patientId: Long?) {//删除单个患者
        viewModelScope.launch {
            if (patientId != null) {
                plantDao.deletePatient(patientId)
            }
        }
    }

    /**
     *@param patient 患者更新数据覆盖原数据
     */
    fun updatePatients(patient: PatientBean) {//update单个患者
        viewModelScope.launch {
            plantDao.updatePatients(patient)
        }
    }

    /**
     * 插入6分钟报告步数
     */
    fun setSixMinReportWalkData(bean: SixMinReportWalk) {
        viewModelScope.launch {
            sixMinReportInfoDao.insertReportWalk(bean)
        }
    }

    /**
     * 获取6分钟报告步数
     */
    fun getSixReportWalk(id: String) {
        viewModelScope.launch {
            sixMinReportInfoDao.getReportWalk(id).collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QuerySixMinReportWalkSuccess, it
                )
            }
        }
    }

    /**
     * 插入6分钟报告综合评估
     */
    fun setSixMinReportEvaluationData(bean: SixMinReportEvaluation) {
        viewModelScope.launch {
            sixMinReportInfoDao.insertReportEvaluation(bean)
        }
    }

    /**
     * 插入6分钟报告血氧数据
     */
    fun setSixMinReportBloodOxyData(bean: SixMinBloodOxygen) {
        viewModelScope.launch {
            sixMinReportInfoDao.insertReportBloodOxy(bean)
        }
    }

    /**
     * 获取6分钟报告血氧数据
     */
    fun getSixMinReportBloodOxyDat(id: String) {
        viewModelScope.launch {
            sixMinReportInfoDao.getReportBloodOxy(id).collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QuerySixMinReportBloodOxySuccess, it
                )
            }
        }
    }

    /**
     * 插入6分钟报告信息
     */
    fun setSixMinReportInfo(bean: SixMinReportInfo) {
        viewModelScope.launch {
            sixMinReportInfoDao.insertReportInfo(bean)
        }
    }

    /**
     * 删除6分钟报告信息
     */
    fun deleteSixMinReportInfo(reportNo: String) {
        viewModelScope.launch {
            sixMinReportInfoDao.deleteReportInfo(reportNo)
        }
    }

    /**
     * 删除6分钟报告信息根据患者id
     */
    fun deleteSixMinReportInfoById(reportId: String) {
        viewModelScope.launch {
            sixMinReportInfoDao.deleteReportInfoById(reportId)
        }
    }

    /**
     * 获取6分钟报告信息
     */
    fun getSixMinReportInfoById(id: Long, reportNo: String) {
        viewModelScope.launch {
            sixMinReportInfoDao.getReportInfo(id, reportNo).collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QuerySixMinReportInfoSuccess, it
                )
            }
        }
    }

    /**
     * 获取6分钟报告信息
     */
    fun getSixMinReportInfo() {
        viewModelScope.launch {
            sixMinReportInfoDao.getReportInfo().collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QuerySixMinReportInfoSuccess, it
                )
            }
        }
    }

    /**
     * 获取6分钟报综合评估
     */
    fun getSixMinReportEvaluationById(id: String) {
        viewModelScope.launch {
            sixMinReportInfoDao.getReportEvaluationById(id).collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QuerySixMinReportEvaluationSuccess, it
                )
            }
        }
    }

    /**
     * 获取所有6分钟报综合评估
     */
    fun getSixMinReportEvaluation() {
        viewModelScope.launch {
            sixMinReportInfoDao.getReportEvaluation().collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QuerySixMinReportEvaluationSuccess, it
                )
            }
        }
    }

    /**
     * 插入6分钟报告综合评估信息
     */
    fun setSixMinReportEvaluation(bean: SixMinReportEvaluation) {
        viewModelScope.launch {
            sixMinReportInfoDao.insertReportEvaluation(bean)
        }
    }

    /**
     * 更新6分钟报告综合评估信息
     */
    fun updateSixMinReportEvaluation(reportNo: String, fatigueLevel: String, breathLevel: String) {
        viewModelScope.launch {
            sixMinReportInfoDao.updateReportEvaluation(reportNo, fatigueLevel, breathLevel)
        }
    }

    /**
     * 获取6分钟报告其它信息
     */
    fun getSixMinReportOther(id: String) {
        viewModelScope.launch {
            sixMinReportInfoDao.getReportOther(id).collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QuerySixMinReportOtherSuccess, it
                )
            }
        }
    }

    /**
     * 插入6分钟报告其它信息
     */
    fun setSixMinReportOther(bean: SixMinReportOther) {
        viewModelScope.launch {
            sixMinReportInfoDao.insertReportOther(bean)
        }
    }

    /**
     * 更新6分钟报告其它信息
     */
    fun updateSixMinReportOther(
        reportNo: String,
        beforeHigh: String,
        beforeLow: String,
        afterHigh: String,
        afterLow: String
    ) {
        viewModelScope.launch {
            sixMinReportInfoDao.updateReportOther(
                reportNo,
                beforeHigh,
                beforeLow,
                afterHigh,
                afterLow
            )
        }
    }

    /**
     * 插入6分钟报告处方建议
     */
    fun setSixMinReportPrescription(bean: SixMinReportPrescription) {
        viewModelScope.launch {
            sixMinReportInfoDao.insertReportPrescription(bean)
        }
    }

    /**
     * 插入6分钟报告步速
     */
    fun setSixMinReportStride(bean: SixMinReportStride) {
        viewModelScope.launch {
            sixMinReportInfoDao.insertReportStride(bean)
        }
    }

    /**
     * 插入6分钟报告呼吸率
     */
    fun setSixMinReportBreathing(bean: SixMinReportBreathing) {
        viewModelScope.launch {
            sixMinReportInfoDao.insertReportBreathing(bean)
        }
    }

    /**
     * 插入6分钟报告心电
     */
    fun setSixMinReportHeartEcg(bean: SixMinHeartEcg) {
        viewModelScope.launch {
            sixMinReportInfoDao.insertReportHeartEcg(bean)
        }
    }

    /**
     * 插入6分钟报告心率
     */
    fun setSixMinReportHeartBeat(bean: SixMinReportHeartBeat) {
        viewModelScope.launch {
            sixMinReportInfoDao.insertReportHeart(bean)
        }
    }

    /**
     * 更新6分钟处方参数
     */
    fun updateSixMinReportPrescription(bean: SixMinReportPrescription) {
        viewModelScope.launch {
            sixMinReportInfoDao.updateReportPrescription(bean)
        }
    }

    /**
     * 更新6分钟综合评估
     */
    fun updateSixMinReportEvaluation(bean: SixMinReportEvaluation) {
        viewModelScope.launch {
            sixMinReportInfoDao.updateReportEvaluationAll(bean)
        }
    }

    /**
     * 更新6分钟报告信息
     */
    fun updateSixMinReportInfo(bean: SixMinReportInfo) {
        viewModelScope.launch {
            sixMinReportInfoDao.updateReportInfoAll(bean)
        }
    }

    /**
     * 永久删除6分钟报告信息
     */
    fun deleteSixMinReportInfoReal(reportId: String) {
        viewModelScope.launch {
            sixMinReportInfoDao.deleteReportInfoReal(reportId)
            sixMinReportInfoDao.deleteReportBloodOxyReal(reportId)
            sixMinReportInfoDao.deleteReportBreathing(reportId)
            sixMinReportInfoDao.deleteReportEvaluation(reportId)
            sixMinReportInfoDao.deleteReportHeart(reportId)
            sixMinReportInfoDao.deleteReportHeartEcg(reportId)
            sixMinReportInfoDao.deleteReportOther(reportId)
            sixMinReportInfoDao.deleteReportPrescription(reportId)
            sixMinReportInfoDao.deleteReportStride(reportId)
            sixMinReportInfoDao.deleteReportWalk(reportId)
        }
    }

    val fvcBeans = arrayOf(
        DynamicBean.spinnerItemData("FVC"),
        DynamicBean.spinnerItemData("FEV1"),
        DynamicBean.spinnerItemData("FEV2"),
        DynamicBean.spinnerItemData("FEV3"),
        DynamicBean.spinnerItemData("FEV6"),
        DynamicBean.spinnerItemData("FEV1/FVC"),
        DynamicBean.spinnerItemData("FEV2/FVC"),
        DynamicBean.spinnerItemData("FEV3/FVC"),
        DynamicBean.spinnerItemData("FEV6/FVC"),
        DynamicBean.spinnerItemData("PEF"),
        DynamicBean.spinnerItemData("MEF"),
        DynamicBean.spinnerItemData("FEF25"),
        DynamicBean.spinnerItemData("FEF75"),
        DynamicBean.spinnerItemData("MMEF"),
        DynamicBean.spinnerItemData("FET"),
        DynamicBean.spinnerItemData("FEF200-1200"),
        DynamicBean.spinnerItemData("PIF"),
        DynamicBean.spinnerItemData("FIF50"),
        DynamicBean.spinnerItemData("FIV1"),
        DynamicBean.spinnerItemData("FIV1%FVC"),
        DynamicBean.spinnerItemData("FEF50%FIF50"),
        DynamicBean.spinnerItemData("FEV1%FIV1"),
        DynamicBean.spinnerItemData("FEF75/85"),
        DynamicBean.spinnerItemData("TIN/ TTOT"),
        DynamicBean.spinnerItemData("TEX/ TTOT"),
        DynamicBean.spinnerItemData("TIN/TEX"),
        DynamicBean.spinnerItemData("T TOT"),
        DynamicBean.spinnerItemData("MIF"),
        DynamicBean.spinnerItemData("Vol extrap"),
        DynamicBean.spinnerItemData("MMEF"),
        DynamicBean.spinnerItemData("FVC IN"),
        DynamicBean.spinnerItemData("Time(S)"),
        DynamicBean.spinnerItemData("FVC IN"),
//                DynamicBean.spinnerItemData("P0.1"),
        DynamicBean.spinnerItemData("FVC IN"),
    )

    val dynamicBeans = arrayOf(
        DynamicBean.spinnerItemData("SVC"),
        DynamicBean.spinnerItemData("VC_ex"),
        DynamicBean.spinnerItemData("ERV"),
        DynamicBean.spinnerItemData("IRV"),
        DynamicBean.spinnerItemData("VT"),
        DynamicBean.spinnerItemData("IC")
    )

    val mvvBeans = arrayOf(
        DynamicBean.spinnerItemData("MVV"),
        DynamicBean.spinnerItemData("TIME_MVV"),
        DynamicBean.spinnerItemData("BF"),
        DynamicBean.spinnerItemData("Time"),
    )
}



