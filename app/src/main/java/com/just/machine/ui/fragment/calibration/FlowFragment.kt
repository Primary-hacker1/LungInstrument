package com.just.machine.ui.fragment.calibration


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent.Companion.FLOWS_SUCCESS
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.FragmentChildAdapter
import com.just.machine.ui.adapter.calibration.FlowAdapter
import com.just.machine.ui.fragment.cardiopulmonary.staticfragment.BreatheHardInFragment
import com.just.machine.ui.fragment.cardiopulmonary.staticfragment.MaxVentilationFragment
import com.just.machine.ui.fragment.cardiopulmonary.staticfragment.RoutineLungFragment
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.BaseUtil
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentFlowBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 流量定标
 *@author zt
 */
@AndroidEntryPoint
class FlowFragment : CommonBaseFragment<FragmentFlowBinding>() {

    override fun initView() {

        val adapter = FragmentChildAdapter(this)

        adapter.addFragment(FlowHandleFragment())
        adapter.addFragment(FlowAutoFragment())

        binding.vpFlowTitle.setCurrentItem(1, true)

        binding.vpFlowTitle.adapter = adapter

        binding.vpFlowTitle.isUserInputEnabled = false

        binding.llStart.setNoRepeatListener {
            if (Constants.isDebug) {
                val smallRangeFlow = 0 // 例如，120 L/min
                val largeRangeFlow = 30 // 例如，3000 L/min

                val data = MudbusProtocol.generateFlowCalibrationCommand(
                    smallRangeFlow,
                    largeRangeFlow,
                )

                LogUtils.e(tag + BaseUtil.bytes2HexStr(data) + "发送的数据")

                LiveDataBus.get().with("测试").value = data

                return@setNoRepeatListener
            }

            SerialPortManager.sendMessage(MudbusProtocol.FLOW_CALIBRATION_COMMAND)//发送流量定标
        }

        LiveDataBus.get().with("测试").observe(this) {//解析串口消息
            if (it is ByteArray) {
                LogUtils.e(tag + BaseUtil.bytes2HexStr(it) + "字节长度" + BaseUtil.bytes2HexStr(it).length)
                val data = MudbusProtocol.parseFlowCalibrationData(it)
                if (data != null) {
                    val (smallRangeFlow, largeRangeFlow) = data

                    LogUtils.e(tag + smallRangeFlow)

                    LogUtils.e(tag + largeRangeFlow)

                }

            }
        }

    }

    override fun initListener() {
        binding.btnHandle.setNoRepeatListener {
            binding.vpFlowTitle.currentItem = 0
            setButtonPosition(0)
        }
        binding.btnAuto.setNoRepeatListener {
            binding.vpFlowTitle.currentItem = 1
            setButtonPosition(1)
        }
    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFlowBinding.inflate(inflater, container, false)

    private fun setButtonPosition(position: Int) {
        when (position) {
            0 -> {
                setButtonStyle(
                    binding.btnHandle,
                    binding.btnAuto
                )
            }

            1 -> {
                setButtonStyle(
                    binding.btnAuto,
                    binding.btnHandle
                )
            }
        }
    }

    private fun setButtonStyle(
        textView1: TextView,
        textView2: TextView,
    ) {// 设置按钮的样式

        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        textView1.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.super_edittext_bg)

        textView2.setTextColor(ContextCompat.getColor(requireContext(), R.color.cD9D9D9))
        textView2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
    }
}