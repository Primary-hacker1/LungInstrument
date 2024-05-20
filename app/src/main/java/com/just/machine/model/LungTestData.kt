package com.just.machine.model

import com.just.machine.model.lungdata.CPXSerializeData

/**
 * 表示从下位机接收到的肺活量测试数据。
 * @property temperature 温度数据。
 * @property humidity 湿度数据。
 * @property atmosphericPressure 大气压力数据。
 * @property highRangeFlowSensorData 高量程流量传感器数据。
 * @property lowRangeFlowSensorData 低量程流量传感器数据。
 * @property co2SensorData CO2传感器数据。
 * @property o2SensorData O2传感器数据。
 * @property gasFlowSpeedSensorData 气体流速传感器数据。
 * @property gasPressureSensorData 气体压力传感器数据。
 * @property bloodOxygen 血氧数据。
 * @property batteryLevel 电池电量数据。
 */
data class LungTestData(
    val returnCommand: Byte = 0x07,
    val temperature: Int,
    val humidity: Int,
    val atmosphericPressure: Float,
    val highRangeFlowSensorData: Int,
    val lowRangeFlowSensorData: Int,
    val co2SensorData: Int,
    val o2SensorData: Int,
    val gasFlowSpeedSensorData: Int,
    val gasPressureSensorData: Int,
    val bloodOxygen: Int,
    val batteryLevel: Int
) {

    override fun toString(): String {
        return "LungTestData(" +
                "temperature=$temperature, " +
                "humidity=$humidity, " +
                "atmosphericPressure=$atmosphericPressure, " +
                "highRangeFlowSensorData=$highRangeFlowSensorData, " +
                "lowRangeFlowSensorData=$lowRangeFlowSensorData, " +
                "co2SensorData=$co2SensorData, " +
                "o2SensorData=$o2SensorData, " +
                "gasFlowSpeedSensorData=$gasFlowSpeedSensorData, " +
                "gasPressureSensorData=$gasPressureSensorData, " +
                "bloodOxygen=$bloodOxygen, " +
                "batteryLevel=$batteryLevel" +
                ")"
    }
}
