package com.just.machine.model

import com.just.machine.model.lungdata.CPXCalBase
import com.just.machine.model.lungdata.CPXModel
import com.just.machine.model.lungdata.SttlungDataModel
import kotlin.random.Random

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

    // 示例代码：接收串口数据并进行处理
    fun processLungTestData() {
        // 模拟接收串口数据
        val controlBoardResponse = LungTestData(
            temperature = Random.nextInt(0, 100), // 模拟温度数据
            humidity = Random.nextInt(0, 100), // 模拟湿度数据
            atmosphericPressure = Random.nextInt(800, 1200).toFloat(), // 模拟大气压力数据
            highRangeFlowSensorData = Random.nextInt(0, 100), // 模拟高量程流量传感器数据
            lowRangeFlowSensorData = Random.nextInt(0, 100), // 模拟低量程流量传感器数据
            co2SensorData = Random.nextInt(0, 100), // 模拟CO2传感器数据
            o2SensorData = Random.nextInt(0, 100), // 模拟O2传感器数据
            gasFlowSpeedSensorData = Random.nextInt(0, 100), // 模拟气体流速传感器数据
            gasPressureSensorData = Random.nextInt(0, 100), // 模拟气体压力传感器数据
            bloodOxygen = Random.nextInt(0, 100), // 模拟血氧数据
            batteryLevel = Random.nextInt(0, 100) // 模拟电池电量数据
        )

        // 将接收的数据传递给相应的方法进行处理
        val temperature = controlBoardResponse.temperature
        val humidity = controlBoardResponse.humidity
        val atmosphericPressure = controlBoardResponse.atmosphericPressure
        val highRangeFlow = controlBoardResponse.highRangeFlowSensorData
        val lowRangeFlow = controlBoardResponse.lowRangeFlowSensorData
        val co2 = controlBoardResponse.co2SensorData
        val o2 = controlBoardResponse.o2SensorData
        val gasFlowSpeed = controlBoardResponse.gasFlowSpeedSensorData
        val gasPressure = controlBoardResponse.gasPressureSensorData

        // 计算流量
        val flowIn = CPXCalBase.calcFlowIn(
            highRangeFlow,
            atmosphericPressure.toDouble(),
            temperature,
            humidity.toDouble(),
            1.0
        )
        val flowOut =
            CPXCalBase.calcFlowOut(lowRangeFlow, atmosphericPressure.toDouble(), temperature, 1.0)

        // 计算ATP转换
        val atpAtpdValue = CPXCalBase.atpAtpd(
            flowIn,
            atmosphericPressure.toDouble(),
            temperature,
            humidity.toDouble()
        )
        val atpBtpsBreathInValue = CPXCalBase.atpBtpsBreathIn(
            flowIn,
            temperature,
            atmosphericPressure.toDouble(),
            humidity.toDouble()
        )

        // CO2和O2处理
        val co2Adjusted = CPXCalBase.atpAtpd(
            co2.toDouble(),
            atmosphericPressure.toDouble(),
            temperature,
            humidity.toDouble()
        )
        val o2Adjusted = CPXCalBase.atpAtpd(
            o2.toDouble(),
            atmosphericPressure.toDouble(),
            temperature,
            humidity.toDouble()
        )

        // 将结果存储到模型中
        val cpxModel = CPXModel(
            type = SttlungDataModel.CPXDataModelType.DY,
            data = byteArrayOf() // 这里假设data是从串口接收到的原始字节数组
        ).apply {
            this.flow = flowIn
            this.co2 = co2Adjusted
            this.o2 = o2Adjusted
            this.analysis_temp = temperature.toDouble()
            this.T = temperature.toDouble() // 设置温度
            this.H = humidity.toDouble() // 设置湿度
            this.P = atmosphericPressure.toDouble() // 设置压力
            this.breathOut = false // 这里假设都是吸气
            this.door1Open = false // 这里假设门都是关闭的
            this.door2Open = false // 这里假设门都是关闭的
            this.flowori = highRangeFlow.toDouble() // 原始流量
            this.co2ori = co2.toDouble() // 原始CO2
            this.o2ori = o2.toDouble() // 原始O2
            this.flow = flowIn // 计算后的流量
            this.co2 = co2Adjusted // 调整后的CO2
            this.o2 = o2Adjusted // 调整后的O2
            this.co2_ac1 = 0.0 // 第一次加速度波形的CO2值，这里先设置为0
            this.co2_ac2 = 0.0 // 第二次加速度波形的CO2值，这里先设置为0
            this.co2_ac3 = 0.0 // 第三次加速度波形的CO2值，这里先设置为0
            this.o2_ac1 = 0.0 // 第一次加速度波形的O2值，这里先设置为0
            this.o2_ac2 = 0.0 // 第二次加速度波形的O2值，这里先设置为0
            this.o2_ac3 = 0.0 // 第三次加速度波形的O2值，这里先设置为0
            this.analysis_flow = flowIn.toInt() // 分析流量
            this.analysis_pressure = atmosphericPressure.toDouble() // 分析压力
            this.spo2 = bloodOxygen // 血氧饱和度
            this.analysis_temp = temperature.toDouble() // 分析温度
        }

        // 输出或进一步处理
        println("Flow In: $flowIn")
        println("Flow Out: $flowOut")
        println("ATP to ATPS: $atpAtpdValue")
        println("ATP to BTPS (Breath In): $atpBtpsBreathInValue")
        println("Adjusted CO2: $co2Adjusted")
        println("Adjusted O2: $o2Adjusted")
        println("CPX Model: $cpxModel")

    }
}
