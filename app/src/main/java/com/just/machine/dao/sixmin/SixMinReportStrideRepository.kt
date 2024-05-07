package com.just.machine.dao.sixmin

import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportStride
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用于处理数据操作的存储库模块。
 *
 * 从 [SixMinReportWalkDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
 * 主线程外的查询执行。
 */
class SixMinReportStrideRepository @Inject constructor(private val reportDao: SixMinReportStrideDao) {

    fun deleteReportStride(id:String) = reportDao.deleteReportStrideReal(id)
    fun getReportStride(id:String): Flow<List<SixMinReportStride>> = reportDao.getReportStrideById(id)

    suspend fun insertReportStride(reportStride: SixMinReportStride): Long = reportDao.insertReportStride(reportStride)

}
