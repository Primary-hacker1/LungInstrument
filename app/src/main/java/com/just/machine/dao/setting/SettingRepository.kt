package com.just.machine.dao.setting

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用于处理数据操作的存储库模块。
 *
 * 从 [PlantDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
 * 主线程外的查询执行。
 */
class SettingRepository @Inject constructor(private val dao: SettingDao) {

    fun getStaticSettings(): Flow<List<StaticSettingBean>>  = dao.getStaticSettings()

    suspend fun insertStaticSettingBean(bean: MutableList<StaticSettingBean>) = dao.insertStaticSettingAll(bean)

    fun getDynamicSettings(): Flow<List<DynamicSettingBean>>  = dao.getDynamicSettings()

    suspend fun insertDynamicSettingAll(bean: MutableList<DynamicSettingBean>) = dao.insertDynamicSettingAll(bean)

    fun getAllSettings(): Flow<List<AllSettingBean>>  = dao.getAllSettings()

    suspend fun insertAllSettingBeans(bean: MutableList<AllSettingBean>) = dao.insertAllSettingBeans(bean)
}
