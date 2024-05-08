package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportStride
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface SixMinReportStrideDao {

    @Query("DELETE FROM sixmin_report_stride WHERE reportId == :id")
    fun deleteReportStrideReal(id:String)

    @Query("SELECT * FROM sixmin_report_stride WHERE reportId == :id AND delFlag == '0'")
    fun getReportStrideById(id:String): Flow<List<SixMinReportStride>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportStride(strideBean: SixMinReportStride): Long
}
