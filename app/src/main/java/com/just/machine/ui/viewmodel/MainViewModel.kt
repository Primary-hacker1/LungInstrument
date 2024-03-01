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
import com.just.machine.model.Data
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
    private var plantDao: PlantRepository
) : BaseViewModel() {

    var itemNews: ObservableList<Data> = ObservableArrayList()

    /**
     *@param patient 协程请求->直接获取结果的
     */
    fun setDates(patient: PatientBean) {


        patient.name = "张三"

        patient.age = (28).toString()

        doAsync {
            val patient = plantDao.insertPatient(patient)

            if (patient as Int == 0){
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QuerySuccess,
                    patient
                )
            }
        }

        viewModelScope.launch {

        }
    }


    fun getPatient() {//查询所有患者
        viewModelScope.launch {
            plantDao.getPatients().collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.QuerySuccess,
                    it
                )
            }
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
        repository.getRxNews(type)
            .`as`(auto(this))
            .subscribes({

            }, {

            })
    }
}



