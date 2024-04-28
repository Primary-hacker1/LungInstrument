package com.just.machine.ui.fragment.cardiopulmonary.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.just.machine.ui.fragment.cardiopulmonary.DynamicResultFragment
import com.just.machine.ui.fragment.cardiopulmonary.FragmentResultLayout
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentExtremumAnalysisBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 运动极值分析散点图
 *@author zt
 */
@AndroidEntryPoint
class ExtremumAnalysisFragment : CommonBaseFragment<FragmentExtremumAnalysisBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    override fun initView() {

        val fragment = parentFragment

        if (fragment is DynamicResultFragment){//保存
            fragment.onSaveCLick().setNoRepeatListener {
                LogUtils.d(tag+"onClick")
            }
            fragment.onResetCLick().setNoRepeatListener { //重置

            }
        }

        binding.layoutResult.setChartLayout(FragmentResultLayout.ChartLayout.EXTREMUM)

    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentExtremumAnalysisBinding.inflate(inflater, container, false)

}
