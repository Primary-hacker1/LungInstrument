package com.just.machine.ui.fragment.cardiopulmonary.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.common.viewmodel.LiveDataEvent.Companion.DYNAMICSUCCESS
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.dao.setting.DynamicSettingBean
import com.just.machine.model.lungdata.AnlyCpxTableModel
import com.just.machine.model.lungdata.CPXSerializeData
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentCompensatoryBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/6/19
 * 呼吸代偿点分析
 *@author zt
 */
@AndroidEntryPoint
class CompensatoryPointFragment : CommonBaseFragment<FragmentCompensatoryBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private var mutableListCPX: MutableList<AnlyCpxTableModel> = mutableListOf()

    override fun loadData() {//懒加载

    }

    override fun initView() {

        val chartSetting = ChartAxisSettings()
        chartSetting.axisMaximumYL = 60f
        chartSetting.axisMinimumYL = 0f

        chartSetting.axisMaximumYR = 60f
        chartSetting.axisMinimumYR = 0f

        chartSetting.axisMaximumL = 10f
        chartSetting.axisMinimumL = 0f
        chartSetting.xGranularity = 1f

        val chartSetting2 = ChartAxisSettings()
        chartSetting2.axisMaximumYL = 150f
        chartSetting2.axisMinimumYL = 0f
        chartSetting2.granularity = 15f

        chartSetting2.axisMaximumYR = 150f
        chartSetting2.axisMinimumYR = 0f
        chartSetting2.granularityR = 15f

        chartSetting2.axisMaximumL = 10f
        chartSetting2.axisMinimumL = 0f
        chartSetting2.xGranularity = 1f

        val chartSetting3 = ChartAxisSettings()
        chartSetting3.axisMaximumYL = 50f
        chartSetting3.axisMinimumYL = 0f

        chartSetting3.axisMaximumYR = 50f
        chartSetting3.axisMinimumYR = 0f

        chartSetting3.axisMaximumL = 2750f
        chartSetting3.axisMinimumL = 0f
        chartSetting3.xGranularity = 250f

        val chartSetting4 = ChartAxisSettings()
        chartSetting4.axisMaximumYL = 2f
        chartSetting4.axisMinimumYL = 0f
        chartSetting4.granularity = 0.2f

        chartSetting4.axisMaximumYR = 50f
        chartSetting4.axisMinimumYR = 0f
        chartSetting4.granularityR = 5f

        chartSetting4.axisMaximumL = 10f
        chartSetting4.axisMinimumL = 0f
        chartSetting4.xGranularity = 1f

        binding.layoutResult.setChartLayout(
            FragmentResultLayout.ChartLayout.COMPENSATORY,
            chartSetting,
            chartSetting2,
            chartSetting3,
            chartSetting4,
        )

        binding.layoutResult.setScatterData()//模拟散点图数据

        binding.layoutResult.setDynamicResultBeans()

        binding.llSave.setNoRepeatListener {

        }

        binding.llReset.setNoRepeatListener {

        }

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


    override fun onPause() {
        super.onPause()


    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCompensatoryBinding.inflate(inflater, container, false)

}
