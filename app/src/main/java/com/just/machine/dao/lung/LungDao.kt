package com.just.machine.dao.lung

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * 定标 类的数据访问对象。
 */
@Dao
interface LungDao {

    @Transaction
    @Query("SELECT * FROM cpx_breath ORDER BY createTime DESC")
    fun getCPXBreathInOutDataAll(): Flow<List<CPXBreathInOutData>>

    @Transaction
    @Query("SELECT * FROM cpx_breath WHERE patientId = :patientId ORDER BY createTime DESC")
    fun getCPXBreathInOutData(patientId: Long): Flow<List<CPXBreathInOutData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCPXBreathInOutData(bean: CPXBreathInOutData): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCPXBreathInOutData(cpxBreathInOutData: List<CPXBreathInOutData>)
}
