package com.just.machine.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.common.BaseResponseDB
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface PlantDao {
    @Query("update patients set predictDistances =:predictDistances where id =:id")
    suspend fun updatePatient(predictDistances: String, id: Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePatients(patients: PatientBean)

    @Query("DELETE FROM patients WHERE id = :id")
    suspend fun deletePatient(id: Long)

    @Query("SELECT * FROM patients WHERE age = :age ORDER BY name")//条件查询
    fun getPlantsWithGrowZoneNumber(age: Int): Flow<List<PatientBean>>

    @Query("SELECT * FROM patients WHERE name LIKE '%' || :nameId || '%' or medicalRecordNumber LIKE '%' || :nameId || '%'")//条件查询
    fun getNameOrId(nameId: String): Flow<List<PatientBean>>

    @Query("SELECT * FROM patients ORDER BY addTime DESC")
    fun getPatients(): Flow<List<PatientBean>>

    @Query("SELECT * FROM patients WHERE id = :id")//条件查询
    fun getPatient(id: Long): Flow<PatientBean?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<PatientBean>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBean(plants: PatientBean)

    @Insert
    suspend fun insertPatient(patients: PatientBean): Long
}
