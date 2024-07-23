package com.just.machine.ui.fragment.calibration.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.calibration.FlowManualCalibrationResultBean
import com.just.machine.ui.adapter.calibration.ResultFlowAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentCalibrationFlowHandleResultBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 流量手动定标结果
 */
@AndroidEntryPoint
class CalibrationFlowHandleResultFragment :
    CommonBaseFragment<FragmentCalibrationFlowHandleResultBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val adapterFlow by lazy { ResultFlowAdapter(requireContext()) }
    override fun loadData() {

    }

    override fun initView() {
        binding.rvResultFlowHandle.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResultFlowHandle.adapter = adapterFlow
//        viewModel.getFlowManualCaliResult()
    }

    override fun initListener() {
        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.FLOWS_MANUAL_SUCCESS -> {
                    val flowsBean: MutableList<FlowManualCalibrationResultBean> = ArrayList()
                    if (it.any is List<*>) {
                        val list = it.any as List<*>
                        for (index in list) {
                            if (index is FlowManualCalibrationResultBean) {
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
    ) = FragmentCalibrationFlowHandleResultBinding.inflate(inflater, container, false)

}