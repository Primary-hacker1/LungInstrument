package com.just.machine.dao.setting

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
interface SettingDao {
    @Transaction
    @Query("SELECT * FROM static_setting")
    fun getStaticSettings(): Flow<MutableList<StaticSettingBean>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaticSettingAll(beans: MutableList<StaticSettingBean>)

    @Transaction
    @Query("SELECT * FROM dynamic_setting")
    fun getDynamicSettings(): Flow<MutableList<DynamicSettingBean>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDynamicSettingAll(beans: MutableList<DynamicSettingBean>)
}
