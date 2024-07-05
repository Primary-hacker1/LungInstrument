package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.just.machine.model.sixmininfo.SixMinRecordsBean
import com.just.machine.model.sixmininfo.SixMinReportInfoAndEvaluation
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

/**
 * 6分钟报告信息类的数据访问对象。
 */
@Dao
interface SixMinReportInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReportInfoAll(bean: SixMinReportInfo)

    @Query("DELETE FROM sixmin_report_info WHERE reportNo == :id")
    fun deleteReportInfoReal(id:String)

    @Transaction
    @Query("SELECT * FROM sixmin_report_info ORDER BY addTime DESC")
    fun getReportInfo(): Flow<List<SixMinRecordsBean>>

    @Transaction
    @Query("SELECT * FROM sixmin_report_info WHERE patientId == :id AND reportNo ==:reportNo AND delFlag == '0' ORDER BY addTime DESC")
    fun getReportInfoById(id:Long,reportNo:String): Flow<List<SixMinRecordsBean>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportInfo(infoBean: SixMinReportInfo): Long

    @Query("UPDATE sixmin_report_info SET delFlag = '1' WHERE reportNo ==:reportNo")
    suspend fun deleteReportInfo(reportNo:String)

    @Query("UPDATE sixmin_report_info SET delFlag = '1' WHERE patientId ==:patientId")
    suspend fun deleteReportInfoById(patientId:String)

    @Query("select ri.*,re.* from sixmin_report_info ri Inner join sixmin_report_evaluation re on ri.reportNo = re.reportId and re.delFlag = '0' where ri.patientId =:id and ri.delFlag = '0'")
    fun getReportEvaluationById(id:String): Flow<List<SixMinReportInfoAndEvaluation>>

    //血氧
    @Query("DELETE FROM sixmin_report_blood WHERE reportId == :id")
    fun deleteReportBloodReal(id:String)

    @Query("SELECT * FROM sixmin_report_blood WHERE reportId == :id AND delFlag == '0'")
    fun getReportBlood(id:String): Flow<List<SixMinBloodOxygen>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportBlood(bloodOxygen: SixMinBloodOxygen): Long

    //呼吸率
    @Query("DELETE FROM sixmin_report_breathing WHERE reportId == :id")
    fun deleteReportBreathingReal(id:String)

    @Query("SELECT * FROM sixmin_report_breathing WHERE reportId == :id AND delFlag == '0'")
    fun getReportBreathingById(id:String): Flow<List<SixMinReportBreathing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportBreathing(breathingBean: SixMinReportBreathing): Long

    //运动评估
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReportEvaluationAll(bean: SixMinReportEvaluation)

    @Query("DELETE FROM sixmin_report_evaluation WHERE reportId == :id")
    fun deleteReportEvaluationReal(id:String)

    @Query("SELECT * FROM sixmin_report_evaluation WHERE delFlag == '0'")
    fun getReportEvaluation(): Flow<List<SixMinReportEvaluation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportEvaluation(walkBean: SixMinReportEvaluation): Long

    @Query("UPDATE sixmin_report_evaluation SET fatigueLevel=:fatigueLevel,breathingLevel=:breathLevel WHERE reportId ==:reportNo")
    suspend fun updateReportEvaluation(reportNo:String,fatigueLevel: String,breathLevel:String)

    //心率
    @Query("DELETE FROM sixmin_report_heart WHERE reportId == :id")
    fun deleteReportHeartReal(id:String)

    @Query("SELECT * FROM sixmin_report_heart WHERE reportId == :id AND delFlag == '0'")
    fun getReportHeartById(id:String): Flow<List<SixMinReportHeartBeat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportHeart(heartBean: SixMinReportHeartBeat): Long

    //其它
    @Query("DELETE FROM sixmin_report_other WHERE reportId == :id")
    fun deleteReportOtherReal(id:String)

    @Query("UPDATE sixmin_report_other SET startHighPressure =:beforeHigh, startLowPressure =:beforeLow , stopHighPressure =:afterHigh , stopLowPressure =:afterLow WHERE reportId =:reportNo")
    suspend fun updateReportOther(
        reportNo: String, beforeHigh: String, beforeLow: String, afterHigh: String, afterLow: String
    )

    @Query("SELECT * FROM sixmin_report_other WHERE reportId == :id AND delFlag == '0'")
    fun getReportOtherById(id: String): Flow<List<SixMinReportOther>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportOther(otherBean: SixMinReportOther): Long

    //处方建议
    @Query("DELETE FROM sixmin_report_prescription WHERE reportId == :id")
    fun deleteReportPrescriptionReal(id:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReportPrescription(bean: SixMinReportPrescription)

    @Query("SELECT * FROM sixmin_report_prescription WHERE reportId == :id AND delFlag == '0'")
    fun getReportPrescriptionById(id:String): Flow<List<SixMinReportPrescription>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportPrescription(prescriptionBean: SixMinReportPrescription): Long

    //步速
    @Query("DELETE FROM sixmin_report_stride WHERE reportId == :id")
    fun deleteReportStrideReal(id:String)

    @Query("SELECT * FROM sixmin_report_stride WHERE reportId == :id AND delFlag == '0'")
    fun getReportStrideById(id:String): Flow<List<SixMinReportStride>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportStride(strideBean: SixMinReportStride): Long

    //步数
    @Query("DELETE FROM sixmin_report_walk WHERE reportId == :id")
    fun deleteReportWalkReal(id:String)

    @Query("SELECT * FROM sixmin_report_walk WHERE reportId == :id AND delFlag == '0'")
    fun getReportWalkById(id:String): Flow<List<SixMinReportWalk>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportWalk(walkBean: SixMinReportWalk): Long

    //心电
    @Query("DELETE FROM sixmin_report_heart_ecg WHERE reportId == :id")
    fun deleteReportHeartEcgReal(id:String)

    @Query("SELECT * FROM sixmin_report_heart_ecg WHERE reportId == :id AND delFlag == '0'")
    fun getReportHeartEcg(id:String): Flow<List<SixMinHeartEcg>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportHeartEcg(bloodOxygen: SixMinHeartEcg): Long

}
