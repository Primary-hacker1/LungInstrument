package com.just.machine.dao.sixmin

import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportOther
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用于处理数据操作的存储库模块。
 *
 * 从 [SixMinReportWalkDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
 * 主线程外的查询执行。
 */
class SixMinReportOtherRepository @Inject constructor(private val reportDao: SixMinReportOtherDao) {

    suspend fun updateReportOther(
        reportNo: String,
        beforeHigh: String,
        beforeLow: String,
        afterHigh: String,
        afterLow: String
    ) = reportDao.updateReportOther(
        reportNo,
        beforeHigh,
        beforeLow,
        afterHigh,
        afterLow
    )

    fun getReportOther(id: String): Flow<List<SixMinReportOther>> = reportDao.getReportOtherById(id)

    suspend fun insertReportOther(reportOther: SixMinReportOther): Long =
        reportDao.insertReportOther(reportOther)

}