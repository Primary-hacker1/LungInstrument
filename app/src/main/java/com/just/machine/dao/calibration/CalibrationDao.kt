package com.just.machine.dao.calibration

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * 定标 类的数据访问对象。
 */
@Dao
interface CalibrationDao {

    @Transaction
    @Query("SELECT * FROM patients")
    fun getPlantWithEnvironmental(): List<PlantWithEnvironmental> //关联查询环境定标

    @Transaction
    @Query("SELECT * FROM environmental ORDER BY createTime DESC")
    fun getEnvironmentals(): Flow<List<EnvironmentalCalibrationBean>>

    @Insert
    suspend fun insertEnvironmental(environmental: EnvironmentalCalibrationBean): Long

    @Transaction
    @Query("SELECT * FROM flow ORDER BY createTime DESC")
    fun getFlows(): Flow<List<FlowBean>>

    @Insert
    suspend fun insertFlow(flow: FlowBean)

    @Transaction
    @Query("SELECT * FROM ingredient ORDER BY createTime DESC")
    fun getIngredients(): Flow<List<IngredientBean>>

    @Insert
    suspend fun insertIngredient(flow: IngredientBean)

}
