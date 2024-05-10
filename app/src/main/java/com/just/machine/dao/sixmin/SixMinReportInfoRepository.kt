package com.just.machine.dao.sixmin

import com.just.machine.model.SixMinRecordsBean
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportInfo
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用于处理数据操作的存储库模块。
 *
 * 从 [SixMinReportWalkDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
 * 主线程外的查询执行。
 */
class SixMinReportInfoRepository @Inject constructor(private val reportDao: SixMinReportInfoDao) {

    suspend fun updateReportInfoAll(bean: SixMinReportInfo) = reportDao.updateReportInfoAll(bean)
    fun deleteReportInfoReal(id:String) = reportDao.deleteReportInfoReal(id)
    fun getReportInfo(id:Long,reportNo:String): Flow<List<SixMinRecordsBean>> = reportDao.getReportInfoById(id,reportNo)

    fun getReportInfo(): Flow<List<SixMinRecordsBean>> = reportDao.getReportInfo()

    suspend fun insertReportInfo(reportInfo: SixMinReportInfo): Long = reportDao.insertReportInfo(reportInfo)

    suspend fun deleteReportInfo(reportNo: String) = reportDao.deleteReportInfo(reportNo)
    suspend fun deleteReportInfoById(patientId: String) = reportDao.deleteReportInfoById(patientId)
}
