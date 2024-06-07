package com.just.machine.model.lungdata

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

object CPXDataModelSerializeCashe {
    private val filePath = File("config/common.ini").absolutePath
    private val timechange = IntArray(6)
    private val orilist = mutableListOf<CPXModel>()
    private val dycore = DyCalculeSerializeCore()
    private var maxoffset: Int = 0
    private var co2offset: Int = 0
    private var o2offset: Int = 0
    private var PutResultIndex: Int = 0
    private var O2offsetAdj: Int = 0
    private var CO2offsetAdj: Int = 0
    var checkInitIsOk: Boolean = false

    init {
        try {
//            O2offsetAdj = IniHelper.getValue("通用设置", "O2offsetAdj", filePath).toInt()
//            CO2offsetAdj = IniHelper.getValue("通用设置", "CO2offsetAdj", filePath).toInt()
        } catch (e: Exception) {
            throw e
        }
    }

//    constructor() {
//        this.maxoffset = 0
//        this.co2offset = 0
//        this.o2offset = 0
//        this.CheckInitIsOk = false
//    }
//
//    constructor(co2offset: Int, o2offset: Int) {
//        this.O2offsetAdj = O2offsetAdj
//        this.CO2offsetAdj = CO2offsetAdj
//        this.maxoffset = maxOf(co2offset + CO2offsetAdj, o2offset + O2offsetAdj)
//        this.co2offset = co2offset + CO2offsetAdj
//        this.o2offset = o2offset + O2offsetAdj
//        this.CheckInitIsOk = co2offset in 0..200 && o2offset in 0..200
//    }

    var tt: Int = 0
    var VOLchange: Float = 0.toFloat()

    fun put(model: CPXModel, cPXSerialize: CPXSerializeData? = null): CPXSerializeData {
        orilist.add(model)
        var index = orilist.size - 1 - maxoffset - 1
        var model1: CPXSerializeData
        model1 = if (index >= 0) {
            val tempModel = orilist[index + maxoffset + 1]
            model1 = CPXSerializeData()
            model1.index = tempModel.index
            model1.flow = orilist[index].flow
            model1.vol = orilist[index].sumFlowCopy / 1000.0
            model1.o2 = orilist[index + o2offset].o2
            model1.co2 = orilist[index + co2offset].co2
            model1.SPO2 = orilist[index].spo2
            dycore.enqueDyDataModel(model1)
            refreshVolgraph(model1)
            orilist.removeAt(0)
            model1
        } else {
            CPXSerializeData().apply { index = model.index }
        }
        PutResultIndex = model1.index
        return model1
    }

    private fun refreshVolgraph(serializeData: CPXSerializeData) {
        runBlocking {
            launch(Dispatchers.Default) {
//                if (tt % 3000 == 0) {
//                    tt = 0
//                    VOLchange = 0f
//                    InstanceBase<Cashe>.Instance.time1.clear()
//                    InstanceBase<Cashe>.Instance.flow.clear()
//                    InstanceBase<Cashe>.Instance.Vol.clear()
//                    InstanceBase<Cashe>.Instance.flowino2.clear()
//                    InstanceBase<Cashe>.Instance.flowinco2.clear()
//                }
//                InstanceBase<Cashe>.Instance.time1.add(tt * 0.005f)
//                InstanceBase<Cashe>.Instance.flow.add(serializeData.flow / 1000f)
//                InstanceBase<Cashe>.Instance.flowino2.add(serializeData.o2)
//                InstanceBase<Cashe>.Instance.flowinco2.add(serializeData.co2)
//                VOLchange += serializeData.flow * 0.005f / 1000f
//                InstanceBase<Cashe>.Instance.Vol.add(VOLchange)
//                tt++
            }
        }
    }

    fun clear() {
        orilist.clear()
        dycore.return2begin()
    }

    fun recordStage(number: Int) {
        timechange[number] = PutResultIndex
    }
}
