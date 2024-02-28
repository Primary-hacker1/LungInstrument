package com.just.machine.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.common.BaseResponseDB
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface PlantDao {
    @Query("SELECT * FROM patients ORDER BY name")
    fun getPlants(): Flow<List<PatientBean>>

    @Query("SELECT * FROM patients WHERE age = :age ORDER BY name")//条件查询
    fun getPlantsWithGrowZoneNumber(age: Int): Flow<List<PatientBean>>

    @Query("SELECT * FROM patients")
    fun getPatients(): Flow<List<PatientBean>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<PatientBean>)

    @Insert
    fun insertPatient(patients: PatientBean): Long
}
