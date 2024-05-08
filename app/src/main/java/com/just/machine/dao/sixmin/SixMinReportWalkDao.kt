package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface SixMinReportWalkDao {

    @Query("DELETE FROM sixmin_report_walk WHERE reportId == :id")
    fun deleteReportWalkReal(id:String)

    @Query("SELECT * FROM sixmin_report_walk WHERE reportId == :id AND delFlag == '0'")
    fun getReportWalkById(id:String): Flow<List<SixMinReportWalk>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportWalk(walkBean: SixMinReportWalk): Long
}
