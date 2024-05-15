package com.just.machine.dao.lung

import androidx.room.Dao
import androidx.room.Insert
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
    fun getCPXBreathInOutDatas(): Flow<List<CPXBreathInOutData>>

    @Insert
    suspend fun insertCPXBreathInOutData(flow: CPXBreathInOutData)
}
