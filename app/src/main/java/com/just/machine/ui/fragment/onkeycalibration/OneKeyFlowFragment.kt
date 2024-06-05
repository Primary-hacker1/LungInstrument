package com.just.machine.ui.fragment.onkeycalibration

import android.view.LayoutInflater
import android.view.ViewGroup
import com.common.base.CommonBaseFragment
import com.just.news.databinding.FragmentOnekeyFlowBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 一键定标-流量
 */
@AndroidEntryPoint
class OneKeyFlowFragment : CommonBaseFragment<FragmentOnekeyFlowBinding>() {
    override fun loadData() {

    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnekeyFlowBinding.inflate(inflater, container, false)

}