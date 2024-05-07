package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.just.machine.model.sixminreport.SixMinReportHeartBeat
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface SixMinReportHeartDao {

    @Query("DELETE FROM sixmin_report_heart WHERE reportId == :id")
    fun deleteReportHeartReal(id:String)

    @Query("SELECT * FROM sixmin_report_heart WHERE reportId == :id AND delFlag == '0'")
    fun getReportHeartById(id:String): Flow<List<SixMinReportHeartBeat>>

    @Insert
    suspend fun insertReportHeart(heartBean: SixMinReportHeartBeat): Long
}
