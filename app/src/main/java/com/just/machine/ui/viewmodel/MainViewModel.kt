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
import com.just.machine.dao.sixmin.SixMinReportWalkRepository
import com.just.machine.model.Data
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
    private var sixMinReportBloodDao: SixMinReportBloodRepository,
    private var environmentalDao: EnvironmentalCalibrationRepository,
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
     * 获取6分钟步数
     */
    fun setSixMinReportWalkData(bean: SixMinReportWalk) {
        viewModelScope.launch {
            sixMinReportWalkDao.insertReportWalk(bean)
        }
    }

    /**
     * 插入6分钟步数
     */
    fun getReportWalk(id:String) {
        viewModelScope.launch {
            sixMinReportWalkDao.getReportWalk(id).collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QuerySuccess, it
                )
            }
        }
    }
}



