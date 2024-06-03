package com.just.machine.ui.fragment.calibration

import android.view.LayoutInflater
import android.view.ViewGroup
import com.common.base.CommonBaseFragment
import com.just.news.databinding.FragmentFlowAutoBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 自动流量定标
 */
@AndroidEntryPoint
class FlowAutoFragment : CommonBaseFragment<FragmentFlowAutoBinding>() {
    override fun loadData() {

    }

    override fun initView() {
        binding.chartFlowAuto.setLineDataSetData(
            binding.chartFlowAuto.flowDataSetList()
        )//设置数据
        binding.chartFlowAuto.setLineChartFlow(
            yAxisMinimum = 0f,
            yAxisMaximum = 3.8f,
            countMaxX = 30f,
            granularityY = 0.2f,
            granularityX = 1.5f,
            titleCentent = "自动流量定标"
        )

    }

    override fun initListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFlowAutoBinding.inflate(inflater, container, false)

}