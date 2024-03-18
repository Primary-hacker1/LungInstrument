package com.just.machine.ui.fragment.calibration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
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

    override fun initView() {

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