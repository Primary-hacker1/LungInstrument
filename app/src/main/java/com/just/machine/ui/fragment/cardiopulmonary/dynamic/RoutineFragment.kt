package com.just.machine.ui.fragment.cardiopulmonary.dynamic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.viewmodel.LiveDataEvent
import com.github.mikephil.charting.data.Entry
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.news.databinding.FragmentRoutineDynmicBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/4/2
 * 动态肺的常规右边的动态数据
 *@author zt
 */
@AndroidEntryPoint
class RoutineFragment : CommonBaseFragment<FragmentRoutineDynmicBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private var cpxBreathInOutData: CPXBreathInOutData = CPXBreathInOutData()

    override fun loadData() {//懒加载

    }

    override fun initView() {

        // 创建折线图的样本数据
        val entries = arrayListOf<Entry>()
        for (index in 0..30) {
            entries.add(Entry(index.toFloat(), index.toFloat() / 6 - 3))
        }

        binding.chart.setLineDataSetData(
            binding.chart.flowDataSetList()
        )//设置数据

        binding.chart.setLineChartFlow(
            yAxisMinimum = -5f,
            yAxisMaximum = 5f,
            countMaxX = 30f,
            granularityY = 1f,
            granularityX = 1f,
            titleCentent = "动态肺常规"
        )

        binding.chart1.setLineDataSetData(
            binding.chart.flowDataSetList()
        )//设置数据

        binding.chart1.setLineChartFlow(
            yAxisMinimum = -5f,
            yAxisMaximum = 5f,
            countMaxX = 30f,
            granularityY = 1f,
            granularityX = 1f,
            titleCentent = "动态肺常规"
        )

        binding.chart.setDynamicDragLine()

        binding.chart1.setDynamicDragLine()

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.CPXDYNAMICBEAN -> {
                    if (it.any !is List<*>) {
                        return@observe
                    }
                    val listBean = it.any as List<*>
                    for (bean in listBean) {
                        if (bean !is CPXBreathInOutData) {
                            return@observe
                        }
                        cpxBreathInOutData = bean
                    }
                    binding.layoutDynamicData.setDynamicData(cpxBreathInOutData)
                }
            }
        }

        binding.chart2.setDynamicDragLine()

        LiveDataBus.get().with("动态心肺测试").observe(this) {//解析串口消息
            if (it is CPXBreathInOutData) {
                binding.layoutDynamicData.setDynamicData(it)
                binding.chart2.startUpdatingData() // 模拟数据散点图
            }
        }

    }


    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRoutineDynmicBinding.inflate(inflater, container, false)

}
