package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface SixMinReportBloodDao {

    @Query("DELETE FROM sixmin_report_blood WHERE reportId == :id")
    fun deleteReportBloodReal(id:String)

    @Query("SELECT * FROM sixmin_report_blood WHERE reportId == :id AND delFlag == '0'")
    fun getReportBlood(id:String): Flow<List<SixMinBloodOxygen>>

    @Insert
    suspend fun insertReportBlood(bloodOxygen: SixMinBloodOxygen): Long
}
