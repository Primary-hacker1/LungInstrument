package com.just.machine.ui.fragment.cardiopulmonary.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentRateLoopsBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 动态流速环分析
 *@author zt
 */
@AndroidEntryPoint
class FlowRateLoopsFragment : CommonBaseFragment<FragmentRateLoopsBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    override fun initView() {

        binding.llSave.setNoRepeatListener {

        }

        binding.llReset.setNoRepeatListener {

        }

    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRateLoopsBinding.inflate(inflater, container, false)

}
