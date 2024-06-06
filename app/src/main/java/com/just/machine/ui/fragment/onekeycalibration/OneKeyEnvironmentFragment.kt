package com.just.machine.ui.fragment.onekeycalibration

import android.view.LayoutInflater
import android.view.ViewGroup
import com.common.base.CommonBaseFragment
import com.just.news.databinding.FragmentOnekeyEnvironmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 一键定标-环境
 */
@AndroidEntryPoint
class OneKeyEnvironmentFragment : CommonBaseFragment<FragmentOnekeyEnvironmentBinding>() {
    override fun loadData() {

    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnekeyEnvironmentBinding.inflate(inflater, container, false)
}