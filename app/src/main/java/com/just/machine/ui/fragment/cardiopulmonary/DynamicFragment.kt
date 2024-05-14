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
import com.github.mikephil.charting.data.Entry
import com.just.machine.model.CPETParameter
import com.just.machine.model.Constants
import com.just.machine.model.LungTestData
import com.just.machine.ui.adapter.CustomSpinnerAdapter
import com.just.machine.ui.adapter.FragmentPagerAdapter
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.DynamicDataFragment
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.RoutineFragment
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.WassermanFragment
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.BaseUtil
import com.just.machine.model.staticlung.DynamicBean
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentDynamicBinding
import dagger.hilt.android.AndroidEntryPoint
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
            if (Constants.isDebug) {
                // 调用生成主控板返回数据方法并打印生成的数据
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

                val data = MudbusProtocol.generateLungTestData(
                    controlBoardResponse
                )

                LogUtils.e(tag + BaseUtil.bytes2HexStr(data) + "发送的数据")

                LiveDataBus.get().with("动态心肺测试").value = data

                return@setNoRepeatListener
            }

            SerialPortManager.sendMessage(MudbusProtocol.FLOW_CALIBRATION_COMMAND)//发送流量定标
        }

        LiveDataBus.get().with("动态心肺测试").observe(this) {//解析串口消息
            if (it is ByteArray) {
                LogUtils.e(tag + BaseUtil.bytes2HexStr(it) + "字节长度" + BaseUtil.bytes2HexStr(it).length)
                val data = MudbusProtocol.parseLungTestData(it) ?: return@observe
                LogUtils.e(tag + data.toString())
            }
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

        binding.vpTitle.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
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
                    binding.btnRoutine,
                    binding.btnWasserman, binding.btnData
                )
            }

            1 -> {
                setButtonStyle(
                    binding.btnWasserman,
                    binding.btnRoutine, binding.btnData
                )
            }

            2 -> {
                setButtonStyle(
                    binding.btnData,
                    binding.btnWasserman, binding.btnRoutine
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
