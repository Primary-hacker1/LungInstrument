package com.just.machine.dao

import com.just.machine.model.PatientInfoBean
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用于处理数据操作的存储库模块。
 *
 * 从 [PlantDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
 * 主线程外的查询执行。
 */
class PlantRepository @Inject constructor(private val plantDao: PlantDao) {

    suspend fun deletePatient(id: Long) = plantDao.deletePatient(id)

    suspend fun updatePatients(patients: PatientBean) =
        plantDao.updatePatients(patients)

    fun getPatients(): Flow<List<PatientInfoBean>> = plantDao.getPatients()

    fun getNameOrId(nameId: String): Flow<List<PatientInfoBean>> = plantDao.getNameOrId(nameId)

    fun getPatient(id: Long): Flow<PatientBean?> = plantDao.getPatient(id)

    suspend fun insertAll(plants: List<PatientBean>) = plantDao.insertAll(plants)

    suspend fun updateBean(plants: PatientBean) = plantDao.updateBean(plants)

    suspend fun insertPatient(patients: PatientBean): Long = plantDao.insertPatient(patients)

    fun getMaxPatient(): Flow<PatientBean>? = plantDao.getMaxPatient()
}
