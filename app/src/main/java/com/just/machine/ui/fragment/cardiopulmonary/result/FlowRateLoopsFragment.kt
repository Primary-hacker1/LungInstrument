package com.just.machine.ui.fragment.cardiopulmonary.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentRateLoopsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

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

        setupLineChart()

        startUpdatingChart()
    }

    private fun setupLineChart() {
        binding.lineChart.apply {
            val white = ContextCompat.getColor(requireContext(), R.color.white)
            setBackgroundColor(white)
            setTouchEnabled(true)
            setPinchZoom(true)
            description = Description().apply { text = "动态流速环" }

            val legend: Legend = legend
            legend.form = Legend.LegendForm.LINE

            xAxis.apply {
                setDrawGridLines(false)
                setAvoidFirstLastClipping(true)
            }

            axisLeft.apply {
                setDrawGridLines(false)
                axisMinimum = -100f // 设置 Y 轴最小值为 -100
                axisMaximum = 100f // 设置 Y 轴最大值为 100
            }

            axisRight.isEnabled = false
        }
    }

    private fun startUpdatingChart() {
        val entries = mutableListOf<Entry>()
        val lineDataSet = LineDataSet(entries, "流速").apply {
            val blue = ContextCompat.getColor(requireContext(), R.color.blue)
            color = blue
            val red = ContextCompat.getColor(requireContext(), R.color.red)
            setCircleColor(red)
            lineWidth = 2f
            circleRadius = 4f
            setDrawCircleHole(false)
            valueTextSize = 10f
            setDrawFilled(true)
        }

        val lineData = LineData(lineDataSet)
        binding.lineChart.data = lineData

        Thread {
            for (i in 0..100) {
                com.common.base.onUI {
                    addEntry(lineData, lineDataSet, i.toFloat())
                }
                Thread.sleep(1000)
            }
        }.start()
    }

    private fun addEntry(lineData: LineData, lineDataSet: LineDataSet, xValue: Float) {
        val yValue = Random.nextFloat() * 200 - 100 // 生成范围在 -100 到 100 之间的随机数
        lineData.addEntry(Entry(xValue, yValue), 0)
        lineData.notifyDataChanged()
        binding.lineChart.notifyDataSetChanged()
        binding.lineChart.setVisibleXRangeMaximum(10f)
        binding.lineChart.moveViewToX(xValue)
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRateLoopsBinding.inflate(inflater, container, false)

}
