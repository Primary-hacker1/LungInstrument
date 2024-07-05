package com.just.machine.dao.sixmin

import com.just.machine.model.sixmininfo.SixMinRecordsBean
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinHeartEcg
import com.just.machine.model.sixminreport.SixMinReportBreathing
import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.model.sixminreport.SixMinReportHeartBeat
import com.just.machine.model.sixminreport.SixMinReportInfo
import com.just.machine.model.sixminreport.SixMinReportOther
import com.just.machine.model.sixminreport.SixMinReportPrescription
import com.just.machine.model.sixminreport.SixMinReportStride
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 6分钟数据库操作类。
 *
 * 从 [SixMinReportInfoDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
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

    fun getReportEvaluationById(id:String) = reportDao.getReportEvaluationById(id)

    //血氧
    fun deleteReportBloodOxyReal(id:String) = reportDao.deleteReportBloodReal(id)

    fun getReportBloodOxy(id:String): Flow<List<SixMinBloodOxygen>> = reportDao.getReportBlood(id)

    suspend fun insertReportBloodOxy(reportBloodOxy: SixMinBloodOxygen): Long = reportDao.insertReportBlood(reportBloodOxy)

    //呼吸率
    fun deleteReportBreathing(id:String) = reportDao.deleteReportBreathingReal(id)

    fun getReportBreathing(id:String): Flow<List<SixMinReportBreathing>> = reportDao.getReportBreathingById(id)

    suspend fun insertReportBreathing(reportBreathing: SixMinReportBreathing): Long = reportDao.insertReportBreathing(reportBreathing)

    //运动评估
    suspend fun  updateReportEvaluationAll(bean: SixMinReportEvaluation) = reportDao.updateReportEvaluationAll(bean)
    fun deleteReportEvaluation(id:String) = reportDao.deleteReportEvaluationReal(id)
    fun getReportEvaluation(): Flow<List<SixMinReportEvaluation>> = reportDao.getReportEvaluation()
    suspend fun insertReportEvaluation(reportEvaluation: SixMinReportEvaluation): Long = reportDao.insertReportEvaluation(reportEvaluation)
    suspend fun updateReportEvaluation(reportNo:String,fatigueLevel: String,breathLevel:String) = reportDao.updateReportEvaluation(reportNo,fatigueLevel,breathLevel)

    //心电
    fun deleteReportHeartEcg(id:String) = reportDao.deleteReportHeartEcgReal(id)
    fun getReportHeartEcg(id:String): Flow<List<SixMinHeartEcg>> = reportDao.getReportHeartEcg(id)

    suspend fun insertReportHeartEcg(reportHeartEcg: SixMinHeartEcg): Long = reportDao.insertReportHeartEcg(reportHeartEcg)

    //心率
    fun deleteReportHeart(id:String) = reportDao.deleteReportHeartReal(id)
    fun getReportHeart(id:String): Flow<List<SixMinReportHeartBeat>> = reportDao.getReportHeartById(id)

    suspend fun insertReportHeart(reportHeart: SixMinReportHeartBeat): Long = reportDao.insertReportHeart(reportHeart)

    //其它
    fun deleteReportOther(id:String) = reportDao.deleteReportOtherReal(id)

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

    //处方建议
    suspend fun updateReportPrescription(bean: SixMinReportPrescription) = reportDao.updateReportPrescription(bean)

    fun deleteReportPrescription(id:String) = reportDao.deleteReportPrescriptionReal(id)

    fun getReportPrescription(id:String): Flow<List<SixMinReportPrescription>> = reportDao.getReportPrescriptionById(id)

    suspend fun insertReportPrescription(reportPrescription: SixMinReportPrescription): Long = reportDao.insertReportPrescription(reportPrescription)


    //步速
    fun deleteReportStride(id:String) = reportDao.deleteReportStrideReal(id)
    fun getReportStride(id:String): Flow<List<SixMinReportStride>> = reportDao.getReportStrideById(id)

    suspend fun insertReportStride(reportStride: SixMinReportStride): Long = reportDao.insertReportStride(reportStride)

    //步数
    fun deleteReportWalk(id:String) = reportDao.deleteReportWalkReal(id)

    fun getReportWalk(id:String): Flow<List<SixMinReportWalk>> = reportDao.getReportWalkById(id)

    suspend fun insertReportWalk(reportWalk: SixMinReportWalk): Long = reportDao.insertReportWalk(reportWalk)

}
