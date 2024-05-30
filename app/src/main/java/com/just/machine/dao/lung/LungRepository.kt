package com.just.machine.dao.lung

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用于处理数据操作的存储库模块。
 *
 * 从 [PlantDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
 * 主线程外的查询执行。
 */
class LungRepository @Inject constructor(private val dao: LungDao) {

    fun getCPXBreathInOutDatas(): Flow<List<CPXBreathInOutData>> = dao.getCPXBreathInOutDatas()

    fun getCPXBreathInOutData(patientId: Long): Flow<List<CPXBreathInOutData>> =
        dao.getCPXBreathInOutData(patientId)

    suspend fun insertCPXBreathInOutData(bean: CPXBreathInOutData): Long =
        dao.insertCPXBreathInOutData(bean)

}
