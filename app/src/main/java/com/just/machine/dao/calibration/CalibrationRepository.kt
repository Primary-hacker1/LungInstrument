package com.just.machine.dao.calibration

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 用于处理数据操作的存储库模块。
 *
 * 从 [PlantDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
 * 主线程外的查询执行。
 */
class CalibrationRepository @Inject constructor(private val dao: CalibrationDao) {

    fun getEnvironmentals(): Flow<List<EnvironmentalCalibrationBean>> = dao.getEnvironmentals()

    fun getPlantWithEnvironmental(): List<PlantWithEnvironmental>  = dao.getPlantWithEnvironmental()

    suspend fun insertEnvironmental(patients: EnvironmentalCalibrationBean): Long = dao.insertEnvironmental(patients)

    fun getFlows(): Flow<List<FlowBean>>  = dao.getFlows()

    suspend fun insertFlows(bean: FlowBean) = dao.insertFlow(bean)

    fun getIngredients(): Flow<List<IngredientBean>>  = dao.getIngredients()

    suspend fun insertIngredient(bean: IngredientBean) = dao.insertIngredient(bean)
}
