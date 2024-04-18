package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportInfo
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface SixMinReportInfoDao {

    @Query("SELECT * FROM sixmin_report_info WHERE patientId == :id AND delFlag == '0'")
    fun getReportInfoById(id:String): Flow<List<SixMinReportInfo>>

    @Insert
    suspend fun insertReportInfo(infoBean: SixMinReportInfo): Long
}
