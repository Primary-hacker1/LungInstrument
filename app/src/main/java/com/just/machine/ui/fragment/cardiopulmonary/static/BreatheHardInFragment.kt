package com.just.machine.ui.fragment.cardiopulmonary.static

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentBreatheBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.MutableMap.MutableEntry


/**
 *create by 2024/4/2
 * 静态肺的用力肺呼吸
 *@author zt
 */
@AndroidEntryPoint
class BreatheHardInFragment : CommonBaseFragment<FragmentBreatheBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }


    override fun initView() {
        val chart: LineChart = binding.chartFvc

        val entries = arrayListOf<Entry>()
        entries.add(Entry(0f, 2f))
        entries.add(Entry(1f, 4f))
        entries.add(Entry(2f, 6f))
        entries.add(Entry(3f, 8f))
        entries.add(Entry(4f, 10f))

        val dataSet = LineDataSet(entries, "Label")

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()

    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentBreatheBinding.inflate(inflater, container, false)

}
