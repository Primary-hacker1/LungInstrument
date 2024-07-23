package com.just.machine.ui.fragment.calibration.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.calibration.FlowAutoCalibrationResultBean
import com.just.machine.ui.adapter.calibration.ResultAutoFlowAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentCalibrationFlowAutoResultBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 流量自动定标结果
 */
@AndroidEntryPoint
class CalibrationFlowAutoResultFragment :
    CommonBaseFragment<FragmentCalibrationFlowAutoResultBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val adapterFlow by lazy { ResultAutoFlowAdapter(requireContext()) }
    override fun loadData() {

    }

    override fun initView() {
        binding.rvResultFlowAuto.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResultFlowAuto.adapter = adapterFlow
        viewModel.getFlowAutoCaliResult()
    }

    override fun initListener() {
        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.FLOWS_AUTO_SUCCESS -> {
                    val flowsBean: MutableList<FlowAutoCalibrationResultBean> = ArrayList()
                    if (it.any is List<*>) {
                        val list = it.any as List<*>
                        for (index in list) {
                            if (index is FlowAutoCalibrationResultBean) {
                                flowsBean.add(index)
                            }
                        }
                    }
                    adapterFlow.setItemsBean(flowsBean)
                }
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCalibrationFlowAutoResultBinding.inflate(inflater, container, false)

}