package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportOther
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface SixMinReportOtherDao {

    @Query("SELECT * FROM sixmin_report_other WHERE reportId == :id AND delFlag == '0'")
    fun getReportOtherById(id:String): Flow<List<SixMinReportOther>>

    @Insert
    suspend fun insertReportOther(otherBean: SixMinReportOther): Long
}
