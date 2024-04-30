package com.just.machine.dao.sixmin

import com.just.machine.model.sixminreport.SixMinBloodOxygen
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用于处理数据操作的存储库模块。
 *
 * 从 [SixMinReportWalkDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
 * 主线程外的查询执行。
 */
class SixMinReportBloodRepository @Inject constructor(private val reportDao: SixMinReportBloodDao) {

    fun deleteReportBloodOxyReal(id:String) = reportDao.deleteReportBloodReal(id)

    fun getReportBloodOxy(id:String): Flow<List<SixMinBloodOxygen>> = reportDao.getReportBlood(id)

    suspend fun insertReportBloodOxy(reportBloodOxy: SixMinBloodOxygen): Long = reportDao.insertReportBlood(reportBloodOxy)
}
