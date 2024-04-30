package com.just.machine.dao.sixmin

import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.model.sixminreport.SixMinReportPrescription
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用于处理数据操作的存储库模块。
 *
 * 从 [SixMinReportWalkDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
 * 主线程外的查询执行。
 */
class SixMinReportEvaluationRepository @Inject constructor(private val reportDao: SixMinReportEvaluationDao) {

    suspend fun  updateReportEvaluationAll(bean: SixMinReportEvaluation) = reportDao.updateReportEvaluationAll(bean)
    fun deleteReportEvaluation(id:String) = reportDao.deleteReportEvaluationReal(id)
    fun getReportEvaluation(): Flow<List<SixMinReportEvaluation>> = reportDao.getReportEvaluation()
    fun getReportEvaluationById(id:String): Flow<List<SixMinReportEvaluation>> = reportDao.getReportEvaluationById(id)
    suspend fun insertReportEvaluation(reportEvaluation: SixMinReportEvaluation): Long = reportDao.insertReportEvaluation(reportEvaluation)
    suspend fun updateReportEvaluation(reportNo:String,fatigueLevel: String,breathLevel:String) = reportDao.updateReportEvaluation(reportNo,fatigueLevel,breathLevel)

}
