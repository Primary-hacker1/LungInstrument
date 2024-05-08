package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.model.sixminreport.SixMinReportInfo
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
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
}
