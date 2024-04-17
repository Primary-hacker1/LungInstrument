package com.just.machine.dao.sixmin

import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用于处理数据操作的存储库模块。
 *
 * 从 [SixMinReportWalkDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
 * 主线程外的查询执行。
 */
class SixMinReportWalkRepository @Inject constructor(private val reportDao: SixMinReportWalkDao) {

    fun getReportWalk(id:String): Flow<List<SixMinReportWalk>> = reportDao.getReportWalkById(id)

    suspend fun insertReportWalk(reportWalk: SixMinReportWalk): Long = reportDao.insertReportWalk(reportWalk)

}
