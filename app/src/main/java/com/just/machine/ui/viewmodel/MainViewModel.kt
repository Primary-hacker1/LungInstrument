package com.just.machine.ui.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.viewModelScope
import com.common.base.subscribes
import com.common.viewmodel.BaseViewModel
import com.common.viewmodel.LiveDataEvent
import com.just.machine.api.UserRepository
import com.just.machine.dao.PatientBean
import com.just.machine.dao.PlantRepository
import com.just.machine.dao.calibration.EnvironmentalCalibrationBean
import com.just.machine.dao.calibration.EnvironmentalCalibrationRepository
import com.just.machine.dao.sixmin.SixMinReportBloodRepository
import com.just.machine.dao.sixmin.SixMinReportBreathingRepository
import com.just.machine.dao.sixmin.SixMinReportEvaluationRepository
import com.just.machine.dao.sixmin.SixMinReportHeartEcgRepository
import com.just.machine.dao.sixmin.SixMinReportHeartRepository
import com.just.machine.dao.sixmin.SixMinReportInfoRepository
import com.just.machine.dao.sixmin.SixMinReportOtherRepository
import com.just.machine.dao.sixmin.SixMinReportPrescriptionRepository
import com.just.machine.dao.sixmin.SixMinReportStrideRepository
import com.just.machine.dao.sixmin.SixMinReportWalkRepository
import com.just.machine.model.Data
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * create by 2020/6/19
 *
 * @author zt
 */
@Suppress("CAST_NEVER_SUCCEEDS")
@HiltViewModel
class MainViewModel @Inject constructor(
    private var repository: UserRepository,
    private var plantDao: PlantRepository,
    private var sixMinReportWalkDao: SixMinReportWalkRepository,
    private var environmentalDao: EnvironmentalCalibrationRepository,
    private var sixMinReportBloodDao: SixMinReportBloodRepository,
    private var sixMinReportBreathingDao: SixMinReportBreathingRepository,
    private var sixMinReportEvaluationDao: SixMinReportEvaluationRepository,
    private var sixMinReportHeartDao: SixMinReportHeartRepository,
    private var sixMinReportHeartEcgDao: SixMinReportHeartEcgRepository,
    private var sixMinReportInfoDao: SixMinReportInfoRepository,
    private var sixMinReportOtherDao: SixMinReportOtherRepository,
    private var sixMinReportPrescriptionDao: SixMinReportPrescriptionRepository,
    private var sixMinReportStrideDao: SixMinReportStrideRepository,
) : BaseViewModel() {

    var itemNews: ObservableList<Data> = ObservableArrayList()

    /**
     *@param patient 协程请求->直接获取结果的
     */
    fun setDates(patient: PatientBean) {
        viewModelScope.launch {
            val patient = plantDao.insertPatient(patient)
            getPatients()
//            mEventHub.value = LiveDataEvent(
//                LiveDataEvent.addPatient, patient
//            )
        }
    }

    fun setEnvironmental(bean: EnvironmentalCalibrationBean) {//新增环境定标
        viewModelScope.launch {
            val patient = environmentalDao.insertEnvironmental(bean)
            getEnvironmental()
        }
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


    fun getPatients() {//查询所有患者
        viewModelScope.launch {
            plantDao.getPatients().collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QuerySuccess, it
                )
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
            sixMinReportWalkDao.insertReportWalk(bean)
        }
    }

    /**
     * 获取6分钟报告步数
     */
    fun getSixReportWalk(id: String) {
        viewModelScope.launch {
            sixMinReportWalkDao.getReportWalk(id).collect {
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
            sixMinReportEvaluationDao.insertReportEvaluation(bean)
        }
    }

    /**
     * 插入6分钟报告血氧数据
     */
    fun setSixMinReportBloodOxyData(bean: SixMinBloodOxygen) {
        viewModelScope.launch {
            sixMinReportBloodDao.insertReportBloodOxy(bean)
        }
    }

    /**
     * 获取6分钟报告血氧数据
     */
    fun getSixMinReportBloodOxyDat(id: String) {
        viewModelScope.launch {
            sixMinReportBloodDao.getReportBloodOxy(id).collect {
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
    fun deleteSixMinReportInfo(reportNo: String){
        viewModelScope.launch {
            sixMinReportInfoDao.deleteReportInfo(reportNo)
        }
    }

    /**
     * 获取6分钟报告信息
     */
    fun getSixMinReportInfoById(id: Long,reportNo:String) {
        viewModelScope.launch {
            sixMinReportInfoDao.getReportInfo(id,reportNo).collect {
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
            sixMinReportEvaluationDao.getReportEvaluationById(id).collect {
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
            sixMinReportEvaluationDao.getReportEvaluation().collect {
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
            sixMinReportEvaluationDao.insertReportEvaluation(bean)
        }
    }

    /**
     * 更新6分钟报告综合评估信息
     */
    fun updateSixMinReportEvaluation(reportNo:String,fatigueLevel:String,breathLevel:String) {
        viewModelScope.launch {
            sixMinReportEvaluationDao.updateReportEvaluation(reportNo,fatigueLevel,breathLevel)
        }
    }

    /**
     * 获取6分钟报告其它信息
     */
    fun getSixMinReportOther(id: String) {
        viewModelScope.launch {
            sixMinReportOtherDao.getReportOther(id).collect {
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
            sixMinReportOtherDao.insertReportOther(bean)
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
            sixMinReportOtherDao.updateReportOther(reportNo,beforeHigh,beforeLow,afterHigh,afterLow)
        }
    }

    /**
     * 插入6分钟报告处方建议
     */
    fun setSixMinReportPrescription(bean: SixMinReportPrescription) {
        viewModelScope.launch {
            sixMinReportPrescriptionDao.insertReportPrescription(bean)
        }
    }

    /**
     * 插入6分钟报告步速
     */
    fun setSixMinReportStride(bean: SixMinReportStride) {
        viewModelScope.launch {
            sixMinReportStrideDao.insertReportStride(bean)
        }
    }

    /**
     * 插入6分钟报告呼吸率
     */
    fun setSixMinReportBreathing(bean: SixMinReportBreathing) {
        viewModelScope.launch {
            sixMinReportBreathingDao.insertReportBreathing(bean)
        }
    }

    /**
     * 插入6分钟报告心电
     */
    fun setSixMinReportHeartEcg(bean: SixMinHeartEcg) {
        viewModelScope.launch {
            sixMinReportHeartEcgDao.insertReportHeartEcg(bean)
        }
    }

    /**
     * 插入6分钟报告心率
     */
    fun setSixMinReportHeartBeat(bean: SixMinReportHeartBeat) {
        viewModelScope.launch {
            sixMinReportHeartDao.insertReportHeart(bean)
        }
    }

    /**
     * 更新6分钟处方参数
     */
    fun updateSixMinReportPrescription(bean: SixMinReportPrescription) {
        viewModelScope.launch {
            sixMinReportPrescriptionDao.updateReportPrescription(bean)
        }
    }

    /**
     * 永久删除6分钟报告信息
     */
    fun deleteSixMinReportInfoReal(reportId: String) {
        viewModelScope.launch {
            sixMinReportInfoDao.deleteReportInfoReal(reportId)
            sixMinReportBloodDao.deleteReportBloodOxyReal(reportId)
            sixMinReportBreathingDao.deleteReportBreathing(reportId)
            sixMinReportEvaluationDao.deleteReportEvaluation(reportId)
            sixMinReportHeartDao.deleteReportHeart(reportId)
            sixMinReportHeartEcgDao.deleteReportHeartEcg(reportId)
            sixMinReportOtherDao.deleteReportOther(reportId)
            sixMinReportPrescriptionDao.deleteReportPrescription(reportId)
            sixMinReportStrideDao.deleteReportStride(reportId)
            sixMinReportWalkDao.deleteReportWalk(reportId)
        }
    }
}



