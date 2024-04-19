package com.just.machine.dao.calibration

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface EnvironmentalCalibrationDao {

    @Query("SELECT * FROM patients")
    fun getPlantWithEnvironmental(): List<PlantWithEnvironmental> //关联查询环境定标

    @Query("SELECT * FROM environmental ORDER BY createTime DESC")
    fun getEnvironmentals(): Flow<List<EnvironmentalCalibrationBean>>

    @Insert
    suspend fun insertEnvironmental(environmental: EnvironmentalCalibrationBean): Long
}
