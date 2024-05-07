package com.just.machine.dao.sixmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.just.machine.dao.PatientBean
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinReportPrescription
import com.just.machine.model.sixminreport.SixMinReportWalk
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface SixMinReportPrescriptionDao {

    @Query("DELETE FROM sixmin_report_prescription WHERE reportId == :id")
    fun deleteReportPrescriptionReal(id:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReportPrescription(bean: SixMinReportPrescription)

    @Query("SELECT * FROM sixmin_report_prescription WHERE reportId == :id AND delFlag == '0'")
    fun getReportPrescriptionById(id:String): Flow<List<SixMinReportPrescription>>

    @Insert
    suspend fun insertReportPrescription(prescriptionBean: SixMinReportPrescription): Long
}
