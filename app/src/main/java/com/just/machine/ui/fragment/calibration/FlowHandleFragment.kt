package com.just.machine.ui.fragment.calibration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.ui.adapter.calibration.FlowAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentFlowHandleBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 手动流量定标
 */
@AndroidEntryPoint
class FlowHandleFragment : CommonBaseFragment<FragmentFlowHandleBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val flowAdapter by lazy {
        FlowAdapter(requireContext())
    }


    override fun loadData() {
        viewModel.getFlows()
    }

    override fun initView() {
        binding.rvFlowHandle.layoutManager = LinearLayoutManager(requireContext())

        binding.rvFlowHandle.adapter = flowAdapter

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
    }

    override fun initListener() {
        flowAdapter.setItemClickListener { item, position ->
            flowAdapter.toggleItemBackground(position)
        }

        flowAdapter.setItemsBean(
            mutableListOf
                (FlowBean(0, "", 1, "容积1", "3", "3.003"))
        )

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.FLOWS_SUCCESS -> {
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

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFlowHandleBinding.inflate(inflater, container, false)
}