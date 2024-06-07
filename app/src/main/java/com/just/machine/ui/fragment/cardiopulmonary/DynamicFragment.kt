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
import com.just.machine.model.lungdata.CPXDataModel
import com.just.machine.model.lungdata.CPXDataModelSerializeCashe
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentDynamicBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random


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
            test1()
            return@setNoRepeatListener
            SerialPortManager.sendMessage(MudbusProtocol.FLOW_CALIBRATION_COMMAND)//发送流量定标a
        }

        binding.llClean.setNoRepeatListener {

        }

    }

    // 假设这是第i个数据点，totalPoints表示总的数据点数
    private fun generateBreathCycleData(i: Int, totalPoints: Int): Int {
        val cycleLength = totalPoints / 2  // 一半吸气，一半呼气
        val angle = (i % cycleLength) * 2 * PI / cycleLength
        return (sin(angle) * 100).toInt()  // 生成的流量在-100到100之间变化
    }

    private fun computeTheDataModel(lungTestData: LungTestData) {

        val cpxSerializeData =
            CPXSerializeData().convertLungTestDataToCPXSerializeData(lungTestData)

        val core = DyCalculeSerializeCore() // 计算出CPXSerializeData数据

//        core.setBegin(BreathState.None) // 假设吸气
//
//        val serializeData = core.enqueDyDataModel(cpxSerializeData) // 计算一口气参数
//
//        val cpxBreathInOutData = CPXCalcule.calDyBreathInOutData(
//            serializeData,
//            core.cpxBreathInOutDataBase
//        ) // 计算出动态肺参数
//
//        core.setBegin(BreathState.breathOut) // 假设吸气

        val serializeData1 = core.enqueDyDataModel(cpxSerializeData) // 计算一口气参数

        val cpxBreathInOutData1 = CPXCalcule.calDyBreathInOutData(
            serializeData1,
            core.cpxBreathInOutDataBase
        ) // 计算出动态肺参数

        val patientBean = SharedPreferencesUtils.instance.patientBean
        val id = patientBean?.patientId

        if (id != null) {
            cpxBreathInOutData1.patientId = id
        }

        LogUtils.e(tag + cpxBreathInOutData1.toString())

//        viewModel.insertCPXBreathInOutData(cpxBreathInOutData) // 插入数据库

        LiveDataBus.get().with("动态心肺测试").postValue(cpxBreathInOutData1)
    }

    private var currentIndex = 0  // 全局变量，跟踪数据点的索引

    private val totalPoints = 200  // 假设每个完整呼吸周期有200个数据点


    // 使用GlobalScope启动一个新的协程
    fun test1() = GlobalScope.launch(Dispatchers.IO) { // 使用IO调度器启动协程
        val totalCycles = 5  // 假设模拟5个呼吸周期

        for (i in 1..totalCycles * totalPoints) {
            val controlBoardResponse = LungTestData(
                temperature = Random.nextInt(0, 100),
                humidity = Random.nextInt(0, 100),
                atmosphericPressure = Random.nextInt(800, 1200).toFloat(),
                highRangeFlowSensorData = -300100,
                lowRangeFlowSensorData = generateBreathCycleData(currentIndex, totalPoints),
                co2SensorData = Random.nextInt(0, 100),
                o2SensorData = Random.nextInt(0, 100),
                gasFlowSpeedSensorData = Random.nextInt(0, 100),
                gasPressureSensorData = Random.nextInt(0, 100),
                bloodOxygen = Random.nextInt(0, 100),
                batteryLevel = Random.nextInt(0, 100)
            )

            currentIndex++  // 每次生成数据后增加索引

            // 在协程中处理耗时操作
            computeTheDataModel(controlBoardResponse).let {
                // 可以在这里处理结果，比如更新UI（注意：更新UI需要切换到主线程）
                withContext(Dispatchers.Main) {
                    // 更新UI操作
                }
            }

            delay(10) // 休息10毫秒
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
