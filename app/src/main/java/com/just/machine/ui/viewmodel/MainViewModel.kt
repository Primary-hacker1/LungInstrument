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


//        doAsync {
//            val patient = plantDao.insertPatient(patient)
//
//            if (patient as Int == 0) {
//                mEventHub.value = LiveDataEvent(
//                    LiveDataEvent.QuerySuccess, patient
//                )
//            }
//        }

        val list = ArrayList<PatientBean>()

        for (index in 0..10) {
            val patient = PatientBean()

            patient.name = "张三+$index"

            patient.age = (1 + index).toString()

            patient.sex = "男"

            patient.height = "18$index"

            patient.addTime = "2024-3-$index 13:00"

            val testRecordsBeans: MutableList<CardiopulmonaryRecordsBean> = ArrayList()//心肺测试记录

            val sixMinRecordsBeans: MutableList<SixMinRecordsBean> = ArrayList()//六分钟测试记录

            val sixMinRecordsBean = SixMinRecordsBean("", "123456", "2024-3-7 13:00")

            val cardiopulmonaryRecordsBean = CardiopulmonaryRecordsBean(
                "测试1",
                "测试2", "测试3", "测试4", "测试5",
            )

            sixMinRecordsBeans.add(sixMinRecordsBean)

            testRecordsBeans.add(cardiopulmonaryRecordsBean)

            patient.testRecordsBean = testRecordsBeans

            patient.sixMinRecordsBean = sixMinRecordsBeans

            list.add(patient)
        }

        viewModelScope.launch {
            plantDao.insertAll(list)
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
        doAsync {
            plantDao.deletePatient(patientId)
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



