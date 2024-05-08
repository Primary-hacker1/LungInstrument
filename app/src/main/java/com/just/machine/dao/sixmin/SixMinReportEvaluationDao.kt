package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.model.sixminreport.SixMinReportPrescription
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface SixMinReportEvaluationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReportEvaluationAll(bean: SixMinReportEvaluation)

    @Query("DELETE FROM sixmin_report_evaluation WHERE reportId == :id")
    fun deleteReportEvaluationReal(id:String)

    @Query("SELECT * FROM sixmin_report_evaluation WHERE delFlag == '0'")
    fun getReportEvaluation(): Flow<List<SixMinReportEvaluation>>

    @Query("SELECT * FROM sixmin_report_evaluation WHERE reportId == :id AND delFlag == '0'")
    fun getReportEvaluationById(id:String): Flow<List<SixMinReportEvaluation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportEvaluation(walkBean: SixMinReportEvaluation): Long

    @Query("UPDATE sixmin_report_evaluation SET fatigueLevel=:fatigueLevel,breathingLevel=:breathLevel WHERE reportId ==:reportNo")
    suspend fun updateReportEvaluation(reportNo:String,fatigueLevel: String,breathLevel:String)
}
