package com.just.machine.ui.fragment.calibration


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent.Companion.FLOWSUCCESS
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.calibration.FlowAdapter
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.BaseUtil
import com.just.machine.util.LiveDataBus
import com.just.news.databinding.FragmentFlowBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 流量定标
 *@author zt
 */
@AndroidEntryPoint
class FlowFragment : CommonBaseFragment<FragmentFlowBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val flowAdapter by lazy {
        FlowAdapter(requireContext())
    }

    override fun initView() {

        binding.rvFlow.layoutManager = LinearLayoutManager(requireContext())

        flowAdapter.setItemClickListener { item, position ->
            flowAdapter.toggleItemBackground(position)
        }

        flowAdapter.setItemsBean(
            mutableListOf
                (FlowBean(0, "", 1, "容积1", "3", "3.003"))
        )

        binding.rvFlow.adapter = flowAdapter

        binding.chartTime.setLineDataSetData(
            binding.chartTime.flowDataSetList()
        )//设置数据

        binding.chartTime.setLineChartFlow(
            yAxisMinimum = -5f,
            yAxisMaximum = 5f,
            countMaxX = 30f,
            granularityY = 1f,
            granularityX = 1f,
            titleCentent = "容量-时间"
        )

        binding.chartSpeed.setLineDataSetData(
            binding.chartSpeed.flowDataSetList()
        )//设置数据

        binding.chartSpeed.setLineChartFlow(
            yAxisMinimum = -15f,
            yAxisMaximum = 15f,
            countMaxX = 4f,
            granularityY = 3f,
            granularityX = 0.2f,
            titleCentent = "流速-容量"
        )

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

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                FLOWSUCCESS -> {
                    val flowsBean: MutableList<FlowBean> = ArrayList()
                    if (it.any is List<*>) {
                        val list = it.any as List<*>
                        for (index in list) {
                            if (index is FlowBean) {
                                flowsBean.add(index)
                            }
                        }
                    }
                    flowAdapter.setItemsBean(flowsBean)
                }
            }
        }

    }

    override fun initListener() {

    }

    /**
     * 懒加载
     */
    override fun loadData() {
        viewModel.getFlows()
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFlowBinding.inflate(inflater, container, false)
}