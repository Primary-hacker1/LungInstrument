package com.just.machine.ui.fragment.cardiopulmonary.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentOxygenDomainBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 无氧域分析
 *@author zt
 */
@AndroidEntryPoint
class OxygenDomainFragment : CommonBaseFragment<FragmentOxygenDomainBinding>() {

    private val viewModel by viewModels<MainViewModel>()


    override fun loadData() {//懒加载

    }

    override fun initView() {

        val chartSetting = ChartAxisSettings()
        chartSetting.axisMaximumYL = 2750f
        chartSetting.axisMinimumYL = 0f
        chartSetting.granularity = 250f

        chartSetting.axisMaximumL = 10f
        chartSetting.axisMinimumL = 0f
        chartSetting.xGranularity = 1f

        val chartSetting2 = ChartAxisSettings()
        chartSetting2.axisMaximumYL = 2750f
        chartSetting2.axisMinimumYL = 0f
        chartSetting2.granularity = 250f

        chartSetting2.axisMaximumL = 2750f
        chartSetting2.axisMinimumL = 0f
        chartSetting2.xGranularity = 250f

        val chartSetting3 = ChartAxisSettings()
        chartSetting3.axisMaximumYL = 150f
        chartSetting3.axisMinimumYL = 0f
        chartSetting3.granularity = 15f

        chartSetting3.axisMaximumL = 10f
        chartSetting3.axisMinimumL = 0f
        chartSetting3.xGranularity = 1f

        val chartSetting4 = ChartAxisSettings()
        chartSetting4.axisMaximumYL = 60f
        chartSetting4.axisMinimumYL = 0f
        chartSetting4.granularity = 10f

        chartSetting4.axisMaximumL = 10f
        chartSetting4.axisMinimumL = 0f
        chartSetting4.xGranularity = 1f

        binding.layoutResult.setChartLayout(
            FragmentResultLayout.ChartLayout.OXYGEN,
            chartSetting,
            chartSetting2,
            chartSetting3,
            chartSetting4,
        )

        binding.layoutResult.setScatterData()//模拟散点图数据

        binding.llSave.setNoRepeatListener {

        }

        binding.llReset.setNoRepeatListener {

        }

        binding.layoutResult.setDynamicResultBeans()

    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentOxygenDomainBinding.inflate(inflater, container, false)

}
