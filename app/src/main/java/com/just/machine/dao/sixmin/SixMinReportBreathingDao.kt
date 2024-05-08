package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportBreathing
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface SixMinReportBreathingDao {

    @Query("DELETE FROM sixmin_report_breathing WHERE reportId == :id")
    fun deleteReportBreathingReal(id:String)

    @Query("SELECT * FROM sixmin_report_breathing WHERE reportId == :id AND delFlag == '0'")
    fun getReportBreathingById(id:String): Flow<List<SixMinReportBreathing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportBreathing(breathingBean: SixMinReportBreathing): Long
}
