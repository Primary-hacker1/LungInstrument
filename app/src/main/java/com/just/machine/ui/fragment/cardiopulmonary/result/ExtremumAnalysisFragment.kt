package com.just.machine.ui.fragment.cardiopulmonary.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentExtremumAnalysisBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/6/19
 * 运动极值分析散点图
 *@author zt
 */
@AndroidEntryPoint
class ExtremumAnalysisFragment : CommonBaseFragment<FragmentExtremumAnalysisBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    override fun initView() {

        val chartSetting = ChartAxisSettings() //自定义X轴和Y轴的刻度
        chartSetting.axisMaximumYL = 2850f
        chartSetting.axisMinimumYL = 0f
        chartSetting.granularity = 150f

        chartSetting.axisMaximumYR = 2850f
        chartSetting.axisMinimumYR = 0f
        chartSetting.granularityR = 150f

        chartSetting.axisMaximumL = 1f
        chartSetting.axisMinimumL = 0f
        chartSetting.xGranularity = 0.2f

        binding.layoutResult.setChartLayout(//设置布局
            FragmentResultLayout.ChartLayout.EXTREMUM,
            chartSetting
        )

        binding.llSave.setNoRepeatListener {//保存

        }

        binding.llReset.setNoRepeatListener {//重置

        }

    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentExtremumAnalysisBinding.inflate(inflater, container, false)

}
