package com.just.machine.model.lungdata

import com.common.network.LogUtils
import com.just.machine.dao.lung.CPXBreathInOutData
import kotlin.math.abs

class DyCalculeSerializeCore {

    private val tag = DyCalculeSerializeCore::class.java.name

    @Volatile
    var dylist: MutableList<CPXSerializeData> = mutableListOf()
    var fullBreathInOutList: MutableList<FullBreathInOutModel> = mutableListOf()
    var Noise = 30

    //    private val timer: DispatcherTimer = DispatcherTimer()
    var state: BreathState? = null
    var FlowAccumulation = 0.0

    @Volatile
    var HasFindBegin = false
    var observeBreathModel: FullBreathInOutModel = FullBreathInOutModel()
    private var hasfindoutbegin = false
    private var findoutbeginindex = 0
    private var hasfindinbegin = false
    private var findinbeginindex = 0

//    init {
//        timer.Interval = TimeSpan.FromSeconds(15.0)
//        timer.Tick += EventHandler(this::Timer_Tick)
//    }
//
//    fun ReOpenTimer(needstart: Boolean = true) {
//        timer.stop()
//        if (needstart) timer.start()
//    }
//
//    private fun Timer_Tick(sender: Any, e: EventArgs) {
//        if (dylist.size - 1 > -1) dylist.clear()
//        ObserveBreathModel = FullBreathInOutModel()
//        HasFindBegin = false
//        state = BreathState.None
//        FlowAccumulation = 0.0
//        timer.stop()
//        println("停止！！！ timer 事件")
//    }

    private fun clearnoise() {
        // noiseCount = 0
        // noiseflowsum = 0.0
    }

    fun return2begin() {
        clearnoise()
        HasFindBegin = false
        dylist.clear()
        state = BreathState.None
//        ReOpenTimer(false)
    }

    fun setBegin(breathState: BreathState) {
//        HasFindBegin = true
        state = breathState
    }

    fun enqueDyDataModel(model: CPXSerializeData): CPXSerializeData {//处理原始数据
//        Definition.Noise_AD = 5//燥点消除
        val dySerializeData1 = model
        if (state == BreathState.None) {
            dylist.add(model)
            if (!HasFindBegin && model.flow <= 0) HasFindBegin = true
            if (HasFindBegin) {
                if (model.flow >= 0.0) {
                    HasFindBegin = false
                    dylist.clear()
                    FlowAccumulation = 0.0
                } else {
                    FlowAccumulation += model.flow * 0.005
                    if (FlowAccumulation < -150) {
                        state = BreathState.breathIn
                        FlowAccumulation = 0.0
                        hasfindoutbegin = false
                        findoutbeginindex = 0
                        observeBreathModel = FullBreathInOutModel()
                        observeBreathModel.breathinStartIndex = 0
//                        ReOpenTimer(true)
                    }
                }
            } else {
                dylist.clear()
                FlowAccumulation = 0.0
            }
        } else if (state == BreathState.breathIn) {
//            Definition.Noise_AD = 5
            dylist.add(model)
            val index = dylist.size - 1
            if (!hasfindoutbegin && model.flow > 0.0) {
                findoutbeginindex = index
                hasfindoutbegin = true
            }
            if (hasfindoutbegin) {
                if (model.flow >= 0.0) {
                    FlowAccumulation += model.flow * 0.005
                    if (FlowAccumulation > 150) {
                        observeBreathModel.breathinEndIndex = findoutbeginindex - 1
                        observeBreathModel.breathoutStartIndex = findoutbeginindex
                        FlowAccumulation = 0.0
                        state = BreathState.breathOut
                        hasfindinbegin = false
                        findinbeginindex = 0
//                        ReOpenTimer(true)
                        println("2  呼出 确认开始" + model.index.toString())
                    }
                } else {
                    hasfindoutbegin = false
                    FlowAccumulation = 0.0
                }
            }
        } else if (state == BreathState.breathOut) {
//            Definition.Noise_AD = 5
            dylist.add(model)
            val index = dylist.size - 1

            if (!hasfindinbegin && model.flow < 0.0) {
                hasfindinbegin = true
                findinbeginindex = index
            }
            if (hasfindinbegin) {
                if (model.flow <= 0.0) {
                    FlowAccumulation += model.flow * 0.005
                    if (FlowAccumulation < -150) {
                        observeBreathModel.breathoutEndIndex = findinbeginindex - 1
                        FlowAccumulation = 0.0
                        hasfindoutbegin = false
                        findoutbeginindex = 0
                        state = BreathState.breathIn
                        dySerializeData1.breathData = caluculeData(observeBreathModel)
                        observeBreathModel = FullBreathInOutModel()
                        observeBreathModel.breathinStartIndex = 0
                        dylist.removeAll { dylist.indexOf(it) < findinbeginindex }
//                        ReOpenTimer(true)
                        println("3-->1   呼出结束-->检测确认吸入" + model.index.toString())
                    }
                } else {
                    hasfindinbegin = false
                    FlowAccumulation = 0.0
                }
            }
        }

//        LogUtils.e(tag + state)
        return dySerializeData1
    }

    val cpxBreathInOutDataBase = CPXBreathInOutDataBase()

    fun caluculeData(model: FullBreathInOutModel): CPXBreathInOutData {
        val breathInStartIndex = model.breathinStartIndex
        val breathInEndIndex = model.breathinEndIndex
        val breathOutStartIndex = model.breathoutStartIndex
        val breathOutEndIndex = model.breathoutEndIndex
        val dyBreathInOutData = CPXBreathInOutData()
//        val cpxBreathInOutDataBase = CPXBreathInOutDataBase()
        LogUtils.d(tag + model.toString() + ",dylist.size=" + dylist.size)

        if (dylist.isEmpty()) {
            return CPXBreathInOutData() // 返回一个空的 CPXBreathInOutData 对象
        }
        LogUtils.e(tag + dylist.toString())
        val dy = dylist[model.breathoutEndIndex]

        cpxBreathInOutDataBase.EndRealIndex = dy.index

        if (breathInStartIndex >= 0 && breathInEndIndex >= 0 &&
            breathInStartIndex < dylist.size && breathInEndIndex < dylist.size
        ) {
            // 确保 breathInStartIndex 和 breathInEndIndex 在有效范围内
            cpxBreathInOutDataBase.Tin =
                (dylist[breathInEndIndex].index - dylist[breathInStartIndex].index).toDouble() * 0.005
        } else {
            // 处理索引无效的情况，可以打印日志或者执行其他错误处理操作
            LogUtils.e(tag + "cpxBreathInOutDataBase.Tin索引不对，检查数据源！")
        }
//        cpxBreathInOutDataBase.Tin =
//            (dylist[breathInEndIndex].index - dylist[breathInStartIndex].index).toDouble() * 0.005
        var num1 = 0.0
        var num2 = 0.0
        var num3 = 0.0
        var num4 = 100.0
        for (index5 in breathInStartIndex..breathInEndIndex) {
            val num7 = -abs(dylist[index5].flow) * 0.005
            num1 += num7
            num2 += num7 * dylist[index5].o2 / 100
            num3 += num7 * dylist[index5].co2 / 100
            val num8 = dylist[index5].o2
            if (num4 > num8) num4 = num8
        }
        cpxBreathInOutDataBase.TempiTotal = num1
        cpxBreathInOutDataBase.TempiO2 = num2
        cpxBreathInOutDataBase.TempiCO2 = num3

        cpxBreathInOutDataBase.Tex =
//            (dylist[0].index - dylist[0].index).toDouble() * 0.005
            (dylist[breathOutEndIndex].index - dylist[breathOutStartIndex].index).toDouble() * 0.005
        var num9 = 0.0
        var num10 = 0.0
        var num11 = 0.0
        var num12 = 100.0
        var num13 = 0.0
        for (index5 in breathOutStartIndex..breathOutEndIndex) {
            val num7 = abs(dylist[index5].flow) * 0.005
            num9 += num7
            num10 += num7 * dylist[index5].o2 / 100.0
            num11 += num7 * dylist[index5].co2 / 100.0
            val num8 = dylist[index5].o2
            if (num12 > num8) num12 = num8
            val num14 = dylist[index5].co2
            if (num13 < num14) num13 = num14
        }
        cpxBreathInOutDataBase.TempeTotal = num9
        cpxBreathInOutDataBase.TempeO2 = num10
        cpxBreathInOutDataBase.TempeCO2 = num11
        cpxBreathInOutDataBase.FeTO2 = num12
        cpxBreathInOutDataBase.FeTCO2 = num13

        cpxBreathInOutDataBase.T = TestDataCashe.T
        cpxBreathInOutDataBase.H = TestDataCashe.H
        cpxBreathInOutDataBase.P = TestDataCashe.P

        LogUtils.e(tag + cpxBreathInOutDataBase)
        return dyBreathInOutData
    }
}

//个是呼吸时候辅助计算的，主要用来记录呼跟吸开始结束的位置
class FullBreathInOutModel {
    var breathinStartIndex: Int = 0
    var breathinEndIndex: Int = 0
    var breathoutStartIndex: Int = 0
    var breathoutEndIndex: Int = 0
    override fun toString(): String {
        return "FullBreathInOutModel(BreathIn_start_index=$breathinStartIndex" +
                ", BreathIn_End_index=$breathinEndIndex" +
                ", BreathOut_start_index=$breathoutStartIndex" +
                ", BreathOut_End_index=$breathoutEndIndex)"
    }
}


enum class BreathState {
    //呼吸状态
    breathOut, breathIn, None

}
