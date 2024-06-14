package com.just.machine.ui.fragment.cardiopulmonary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.just.machine.model.LungTestData
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.lungdata.BreathState
import com.just.machine.model.lungdata.CPXSerializeData
import com.just.machine.model.lungdata.DyCalculeSerializeCore
import com.just.machine.ui.adapter.FragmentPagerAdapter
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.DynamicDataFragment
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.RoutineFragment
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.WassermanFragment
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.model.lungdata.CPXCalcule
import com.just.machine.model.lungdata.TestModel
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentDynamicBinding
import com.justsafe.libview.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 *create by 2020/6/19
 * 动态肺测试
 *@author zt
 */
@AndroidEntryPoint
class DynamicFragment : CommonBaseFragment<FragmentDynamicBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }


    private fun initToolbar() {

    }

    override fun initView() {
        initToolbar()

        initViewPager()

        binding.llStart.setNoRepeatListener {
            return@setNoRepeatListener
            SerialPortManager.sendMessage(MudbusProtocol.FLOW_CALIBRATION_COMMAND)//发送流量定标a
        }

        binding.llClean.setNoRepeatListener {

            val dataList = TestModel().dataList

            val byteArrayList = dataList.map { TestModel().hexStringToByteArray(it) }

            val lungTestDatas: MutableList<LungTestData> = mutableListOf()

            byteArrayList.forEach { data ->
                MudbusProtocol.parseLungTestData(data)?.let { it1 -> lungTestDatas.add(it1) }
            }

            // 生成吸气和呼气数据
            val lungTestDataList = generateBreathCycleData()

//            test1(lungTestDatas)
            test1(lungTestDataList)
        }

    }

    fun generateLungTestData(
        flowValue: Int,
        index: Int,
        bloodOxygen: Int = 95,
        co2: Int = 40,
        o2: Int = 21,
        temperature: Int = 37,
        humidity: Int = 50,
        atmosphericPressure: Float = 1013.25f,
        gasFlowSpeedSensorData: Int = 200,
        gasPressureSensorData: Int = 100,
        batteryLevel: Int = 100
    ): LungTestData {
        return LungTestData(
            returnCommand = 0x07,
            temperature = temperature,
            humidity = humidity,
            atmosphericPressure = atmosphericPressure,
            highRangeFlowSensorData = flowValue,
            lowRangeFlowSensorData = 0,
            co2SensorData = co2,
            o2SensorData = o2,
            gasFlowSpeedSensorData = gasFlowSpeedSensorData,
            gasPressureSensorData = gasPressureSensorData,
            bloodOxygen = bloodOxygen,
            batteryLevel = batteryLevel
        )
    }

    fun generateBreathCycleData(): MutableList<LungTestData> {
        val dataList = mutableListOf<LungTestData>()
        var flowValue = -1

        // 生成几十组吸气数据 (Flow < 0)
        for (i in 1..30) {
            dataList.add(generateLungTestData(flowValue, i))
            flowValue -= 1
        }

        // 重置 flowValue
        flowValue = 1

        // 生成几十组呼气数据 (Flow > 0)
        for (i in 1..30) {
            dataList.add(generateLungTestData(flowValue, i + 30))
            flowValue += 1
        }

        return dataList
    }


    // 使用GlobalScope启动一个新的协程
    fun test1(lungTestDatas: MutableList<LungTestData>) =
        GlobalScope.launch(Dispatchers.IO) { // 使用IO调度器启动协程
            lungTestDatas.forEach { data ->
                breathTest(lungTestDatas)
                delay(1000) // 休息10毫秒
            }
        }

    fun breathTest(lungTestDatas: MutableList<LungTestData>) {

        val breathInData = mutableListOf<CPXSerializeData>()

        for (i in 0 until lungTestDatas.size) {
            breathInData.add(
                CPXSerializeData().convertLungTestDataToCPXSerializeData(lungTestDatas[i])
            )
        }

        var cpxSerializeData: CPXSerializeData

        for (index in breathInData) {
            // 处理每组呼吸数据
            val dyCalculeSerializeCore = DyCalculeSerializeCore()

            dyCalculeSerializeCore.setBegin(BreathState.None)

            cpxSerializeData = dyCalculeSerializeCore.enqueDyDataModel(index)

            // 处理结果
//            val processedData: CPXBreathInOutData =
            dyCalculeSerializeCore.caluculeData(dyCalculeSerializeCore.observeBreathModel)

//            LogUtils.e(tag + processedData)

//            LogUtils.e(tag + cpxSerializeData)

//            LogUtils.e(tag + dyCalculeSerializeCore.cpxBreathInOutDataBase)

            val cpxBreathInOutData = CPXCalcule.calDyBreathInOutData(
                cpxSerializeData,
                dyCalculeSerializeCore.cpxBreathInOutDataBase
            ) // 计算出动态肺参数

            val patientBean = SharedPreferencesUtils.instance.patientBean

            val id = patientBean?.patientId

            if (id != null) {
                cpxBreathInOutData.patientId = id
            }

            cpxBreathInOutData.createTime = DateUtils.nowMinutesDataString

            LogUtils.e(tag + cpxBreathInOutData.toString())

//        viewModel.insertCPXBreathInOutData(cpxBreathInOutData) // 插入数据库

            LiveDataBus.get().with("动态心肺测试").postValue(cpxBreathInOutData)
        }
    }


    private fun initViewPager() {

        val adapter = FragmentPagerAdapter(requireActivity())

        adapter.addFragment(RoutineFragment())
        adapter.addFragment(WassermanFragment())
        adapter.addFragment(DynamicDataFragment())

        binding.vpTitle.setCurrentItem(0, true)

        binding.vpTitle.adapter = adapter

        binding.vpTitle.isUserInputEnabled = false

        binding.llStart.setNoRepeatListener {

        }

        binding.llClean.setNoRepeatListener { //点击取消

        }

    }

    override fun initListener() {

        binding.btnRoutine.setNoRepeatListener {
            binding.vpTitle.currentItem = 0
            setButtonPosition(0)
        }

        binding.btnWasserman.setNoRepeatListener {
            binding.vpTitle.currentItem = 1
            setButtonPosition(1)
        }

        binding.btnData.setNoRepeatListener {
            binding.vpTitle.currentItem = 2
            setButtonPosition(2)
        }

        //这个必须写，不然会产生Fata
        binding.vpTitle.isSaveEnabled = false

        binding.vpTitle.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 当页面被选中时执行你的操作
//                LogUtils.d(tag + "ViewPager2Selected page: $position")

                setButtonPosition(position)

            }
        })
    }

    private fun setButtonPosition(position: Int) {
        when (position) {
            0 -> {
                setButtonStyle(
                    binding.btnRoutine, binding.btnWasserman, binding.btnData
                )
            }

            1 -> {
                setButtonStyle(
                    binding.btnWasserman, binding.btnRoutine, binding.btnData
                )
            }

            2 -> {
                setButtonStyle(
                    binding.btnData, binding.btnWasserman, binding.btnRoutine
                )
            }

        }
    }

    private fun setButtonStyle(
        textView1: TextView,
        textView2: TextView,
        textView3: TextView,
    ) {// 设置按钮的样式

        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        textView1.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.super_edittext_bg)

        textView2.setTextColor(ContextCompat.getColor(requireContext(), R.color.cD9D9D9))
        textView2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        textView3.setTextColor(ContextCompat.getColor(requireContext(), R.color.cD9D9D9))
        textView3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicBinding.inflate(inflater, container, false)

}
