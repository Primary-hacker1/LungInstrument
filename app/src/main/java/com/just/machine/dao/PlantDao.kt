package com.just.machine.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.common.BaseResponseDB
import com.just.machine.model.PatientInfoBean
import kotlinx.coroutines.flow.Flow

/**
 * Plant 类的数据访问对象。
 */
@Dao
interface PlantDao {
    @Transaction
    @Query("update patients set predictDistances =:predictDistances where id =:id")
    suspend fun updatePatient(predictDistances: String, id: Long)

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePatients(patients: PatientBean)

    @Transaction
    @Query("UPDATE patients SET deleteTheTag = 1 WHERE id = :id")
    suspend fun deletePatient(id: Long)

    @Transaction
    @Query("SELECT * FROM patients WHERE age = :age ORDER BY name")//条件查询
    fun getPlantsWithGrowZoneNumber(age: Int): Flow<List<PatientBean>>

    @Transaction
    @Query("SELECT * FROM patients WHERE name LIKE '%' || :nameId || '%' or medicalRecordNumber LIKE '%' || :nameId || '%'")//条件查询
    fun getNameOrId(nameId: String): Flow<List<PatientInfoBean>>

    @Transaction
    @Query("SELECT * FROM patients WHERE deleteTheTag == 0 ORDER BY addTime DESC")
    fun getPatients(): Flow<List<PatientInfoBean>>

    @Transaction
    @Query("SELECT * FROM patients WHERE id = :id")//条件查询
    fun getPatient(id: Long): Flow<PatientBean?>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<PatientBean>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBean(plants: PatientBean)

    @Insert
    suspend fun insertPatient(patients: PatientBean): Long

    @Query("SELECT * FROM patients  WHERE Id = (SELECT MAX(Id) FROM patients)")//查询最新插入的患者信息
    fun getMaxPatient(): Flow<PatientBean>?
}
