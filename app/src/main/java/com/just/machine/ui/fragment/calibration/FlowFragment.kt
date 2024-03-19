package com.just.machine.ui.fragment.calibration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.just.machine.model.calibration.FlowBean
import com.just.machine.ui.adapter.calibration.FlowAdapter
import com.just.machine.ui.viewmodel.MainViewModel
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

        flowAdapter.setItemsBean(mutableListOf
            (FlowBean("容积1","3","3.003")))

        binding.rvFlow.adapter = flowAdapter

    }

    override fun initListener() {

    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFlowBinding.inflate(inflater, container, false)
}