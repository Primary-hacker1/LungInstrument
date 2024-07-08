package com.just.machine.ui.fragment.cardiopulmonary.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.model.lungdata.AnlyCpxTableModel
import com.just.machine.model.lungdata.CPXSerializeData
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentOxygenDomainBinding
import com.justsafe.libview.chart.ResultScatterChart
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/6/19
 * 无氧域分析
 *@author zt
 */
@AndroidEntryPoint
class OxygenDomainFragment : CommonBaseFragment<FragmentOxygenDomainBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private var mutableListCPX: MutableList<AnlyCpxTableModel> = mutableListOf()

    override fun loadData() {//懒加载

    }

    override fun initView() {

        val chartSetting = ChartAxisSettings()
        chartSetting.axisMaximumYL = 2750f
        chartSetting.axisMinimumYL = 0f
        chartSetting.granularity = 250f

        chartSetting.axisMaximumYR = 2750f
        chartSetting.axisMinimumYR = 0f
        chartSetting.granularityR = 250f

        chartSetting.axisMaximumL = 10f
        chartSetting.axisMinimumL = 0f
        chartSetting.xGranularity = 1f

        val chartSetting2 = ChartAxisSettings()
        chartSetting2.axisMaximumYL = 2750f
        chartSetting2.axisMinimumYL = 0f
        chartSetting2.granularity = 250f

        chartSetting2.axisMaximumYR = 2750f
        chartSetting2.axisMinimumYR = 0f
        chartSetting2.granularityR = 250f

        chartSetting2.axisMaximumL = 2750f
        chartSetting2.axisMinimumL = 0f
        chartSetting2.xGranularity = 250f

        val chartSetting3 = ChartAxisSettings()
        chartSetting3.axisMaximumYL = 150f
        chartSetting3.axisMinimumYL = 0f
        chartSetting3.granularity = 15f

        chartSetting3.axisMaximumYR = 150f
        chartSetting3.axisMinimumYR = 0f
        chartSetting3.granularityR = 15f

        chartSetting3.axisMaximumL = 10f
        chartSetting3.axisMinimumL = 0f
        chartSetting3.xGranularity = 1f

        val chartSetting4 = ChartAxisSettings()
        chartSetting4.axisMaximumYL = 60f
        chartSetting4.axisMinimumYL = 0f
        chartSetting4.granularity = 10f

        chartSetting4.axisMaximumYR = 60f
        chartSetting4.axisMinimumYR = 0f
        chartSetting4.granularityR = 10f

        chartSetting4.axisMaximumL = 10f
        chartSetting4.axisMinimumL = 0f
        chartSetting4.xGranularity = 1f


        val titleBean1 = ResultScatterChart.ResultChartBean()

        titleBean1.titleOneL = "VO2"
        titleBean1.titleTwoL = "[ml/min]"
        titleBean1.titleContent = ""
        titleBean1.titleOneR = "VCO2"
        titleBean1.titleTwoR = "[ml/min]"

        val titleBean2 = ResultScatterChart.ResultChartBean()

        titleBean2.titleOneL = "VCO2"
        titleBean2.titleTwoL = "[ml/min]"
        titleBean1.titleContent = ""
        titleBean2.titleOneR = ""
        titleBean2.titleTwoR = ""

        val titleBean3 = ResultScatterChart.ResultChartBean()

        titleBean3.titleOneL = "PetO2"
        titleBean3.titleTwoL = "[ml/min]"
        titleBean1.titleContent = ""
        titleBean3.titleOneR = "PetCO2"
        titleBean3.titleTwoR = "[ml/min]"

        val titleBean4 = ResultScatterChart.ResultChartBean()

        titleBean4.titleOneL = "VE/VO2"
        titleBean4.titleTwoL = "[ml/min]"
        titleBean1.titleContent = ""
        titleBean4.titleOneR = "VE/VCO2"
        titleBean4.titleTwoR = "[ml/min]"

        binding.layoutResult.setChartLayout(
            FragmentResultLayout.ChartLayout.OXYGEN,
            chartSetting,
            chartSetting2,
            chartSetting3,
            chartSetting4,
            titleBean1,
            titleBean2,
            titleBean3,
            titleBean4
        )

        binding.layoutResult.setScatterData()//模拟散点图数据

        binding.llSave.setNoRepeatListener {

        }

        binding.llReset.setNoRepeatListener {

        }

        binding.layoutResult.setDynamicResultBeans()


        viewModel.getCPXBreathInOutData()

        viewModel.mEventHub.observe(this) { event ->
            when (event.action) {
                LiveDataEvent.CPXDYNAMICBEAN -> {
                    if (event.any !is List<*>) {
                        return@observe
                    }
                    mutableListCPX.clear()
                    val listBean = event.any as List<*>
                    for (bean in listBean) {
                        if (bean !is CPXBreathInOutData) {
                            return@observe
                        }
                        val cpxBean = CPXSerializeData().createAnlyCpxTableModels(bean)
                        mutableListCPX.add(cpxBean)
                    }
                }
            }
        }

    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentOxygenDomainBinding.inflate(inflater, container, false)

}
