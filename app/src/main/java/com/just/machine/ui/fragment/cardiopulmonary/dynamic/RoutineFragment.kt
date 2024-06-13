package com.just.machine.ui.fragment.cardiopulmonary.dynamic

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAOptionsConstructor
import com.github.aachartmodel.aainfographics.aachartcreator.aa_toAAOptions
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AALabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AATitle
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAYAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.news.databinding.FragmentRoutineDynmicBinding
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/4/2
 * 动态肺的常规右边的动态数据
 *@author zt
 */
@AndroidEntryPoint
class RoutineFragment : CommonBaseFragment<FragmentRoutineDynmicBinding>() {

    private val viewModel by viewModels<MainViewModel>()

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

        binding.layoutDynamicData.setDynamicData()

        LiveDataBus.get().with("动态心肺测试").observe(this) {//解析串口消息
            if (it is CPXBreathInOutData) {
                binding.layoutDynamicData.setDynamicData(it)
            }
        }

        setupScatterChart()

    }

    private fun setupScatterChart() {
        // 设置数据
        val values1 = listOf(200f, 300f, 400f, 380f, 560f)
        val values2 = listOf(0f, 0.3f, 0.6f, 0.9f, 0.8f)

        val entries1 = values1.mapIndexed { index, value -> Entry(index.toFloat(), value) }
        val dataSet1 = ScatterDataSet(entries1, "数据集1").apply {
            // 设置散点的样式和轴依赖性
            color = Color.RED
            scatterShapeSize = 12f
            axisDependency = YAxis.AxisDependency.LEFT
        }

        val entries2 = values2.mapIndexed { index, value -> Entry(index.toFloat(), value) }
        val dataSet2 = ScatterDataSet(entries2, "数据集2").apply {
            // 设置散点的样式和轴依赖性
            color = Color.BLUE
            scatterShapeSize = 12f
            axisDependency = YAxis.AxisDependency.RIGHT
        }

        val scatterData = ScatterData(dataSet1, dataSet2)

        // 设置散点图的数据
        binding.chart2.data = scatterData

        // 刷新图表
        binding.chart2.invalidate()
    }


    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRoutineDynmicBinding.inflate(inflater, container, false)

}
