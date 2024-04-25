package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinHeartEcg
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface SixMinReportHeartEcgDao {

    @Query("SELECT * FROM sixmin_report_heart_ecg WHERE reportId == :id AND delFlag == '0'")
    fun getReportHeartEcg(id:String): Flow<List<SixMinHeartEcg>>

    @Insert
    suspend fun insertReportHeartEcg(bloodOxygen: SixMinHeartEcg): Long
}