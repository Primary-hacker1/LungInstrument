package com.just.machine.ui.fragment.cardiopulmonary.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentRateLoopsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/6/19
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

        val entries = mutableListOf<Entry>()
        // 示例数据
        entries.add(Entry(0f, 1f))
        entries.add(Entry(1f, 2f))
        entries.add(Entry(2f, 3f))
        entries.add(Entry(3f, 4f))
        entries.add(Entry(4f, 5f))
        entries.add(Entry(5f, -4f))
        entries.add(Entry(6f, -3f))
        entries.add(Entry(7f, -2f))
        entries.add(Entry(8f, -1f))

        val dataSet = LineDataSet(entries, "Gas Flow Rate")
        val lineData = LineData(dataSet)
        binding.lineChart.data = lineData

        configureChartAppearance()

    }

    private fun configureChartAppearance() {
        binding.lineChart.description.isEnabled = false
        binding.lineChart.setTouchEnabled(true)
        binding.lineChart.isDragEnabled = true
        binding.lineChart.setScaleEnabled(true)
        binding.lineChart.setPinchZoom(true)

        val xAxis: XAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        val leftAxis: YAxis = binding.lineChart.axisLeft
        leftAxis.axisMinimum = 0f

        val rightAxis: YAxis = binding.lineChart.axisRight
        rightAxis.isEnabled = false
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRateLoopsBinding.inflate(inflater, container, false)

}
