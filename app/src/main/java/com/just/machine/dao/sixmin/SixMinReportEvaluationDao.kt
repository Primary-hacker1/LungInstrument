package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface SixMinReportEvaluationDao {

    @Query("SELECT * FROM sixmin_report_evaruation WHERE reportId == :id AND delFlag == '0'")
    fun getReportEvaluationById(id:String): Flow<List<SixMinReportEvaluation>>

    @Insert
    suspend fun insertReportEvaluation(walkBean: SixMinReportEvaluation): Long
}
