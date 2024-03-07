package com.just.machine.dao

import com.common.BaseResponseDB
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用于处理数据操作的存储库模块。
 *
 * 从 [PlantDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
 * 主线程外的查询执行。
 */
class PlantRepository @Inject constructor(private val plantDao: PlantDao) {

    fun deletePatient(id: Long) = plantDao.deletePatient(id)

    fun getPatients(): Flow<List<PatientBean>> = plantDao.getPatients()

    fun getNameOrId(nameId: String): Flow<List<PatientBean>> = plantDao.getNameOrId(nameId)

    fun getPatient(id: Long): Flow<PatientBean> = plantDao.getPatient(id)

    suspend fun insertAll(plants: List<PatientBean>) = plantDao.insertAll(plants)

    fun insertPatient(patients: PatientBean): Long = plantDao.insertPatient(patients)

}
