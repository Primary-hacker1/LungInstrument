package com.just.machine.ui.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.viewModelScope
import com.common.base.doAsync
import com.common.base.subscribes
import com.common.viewmodel.BaseViewModel
import com.common.viewmodel.LiveDataEvent
import com.just.machine.api.UserRepository
import com.just.machine.dao.PatientBean
import com.just.machine.dao.PlantRepository
import com.just.machine.model.CardiopulmonaryRecordsBean
import com.just.machine.model.Data
import com.just.machine.model.SixMinRecordsBean
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
    private var repository: UserRepository, private var plantDao: PlantRepository
) : BaseViewModel() {

    var itemNews: ObservableList<Data> = ObservableArrayList()

    /**
     *@param patient 协程请求->直接获取结果的
     */
    fun setDates(patient: PatientBean) {
        viewModelScope.launch {
            val patient = plantDao.insertPatient(patient)
            mEventHub.value = LiveDataEvent(
                LiveDataEvent.QuerySuccess, patient
            )
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
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QueryPatient, it
                )
            }
        }
    }

    fun deletePatient(patientId: Long) {//删除单个患者
        viewModelScope.launch {
            plantDao.deletePatient(patientId)
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
     *@param type 协程请求->带loading的
     */
    fun getNewsLoading(type: String) {
        async({ repository.getNews("") }, {
            //返回结果
        }, true, {}, {})
    }

    /**
     *@param type rxjava请求->直接获取结果的
     */
    fun getRxNews(type: String) {
        repository.getRxNews(type).`as`(auto(this)).subscribes({

        }, {

        })
    }
}



