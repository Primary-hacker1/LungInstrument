package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportInfo
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface SixMinReportInfoDao {

    @Query("SELECT * FROM sixmin_report_info WHERE delFlag == '0' ORDER BY addTime DESC")
    fun getReportInfo(): Flow<List<SixMinRecordsBean>>

    @Query("SELECT * FROM sixmin_report_info WHERE patientId == :id AND reportNo ==:reportNo AND delFlag == '0' ORDER BY addTime DESC")
    fun getReportInfoById(id:String,reportNo:String): Flow<List<SixMinRecordsBean>>

    @Insert
    suspend fun insertReportInfo(infoBean: SixMinReportInfo): Long
}