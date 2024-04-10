package com.just.machine.ui.fragment.cardiopulmonary.staticfragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentLungBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/4/2
 * 静态肺的用力呼吸
 *@author zt
 */
@AndroidEntryPoint
class RoutineLungFragment : CommonBaseFragment<FragmentLungBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }


    override fun initView() {
        LogUtils.e("$tag===============")
        val lineChart = binding.chartFvc

        // 创建折线图的样本数据
        val entries = arrayListOf<Entry>()
        entries.add(Entry(0f, 10f))
        entries.add(Entry(1f, 20f))
        entries.add(Entry(2f, 15f))
        entries.add(Entry(3f, 25f))
        entries.add(Entry(4f, 30f))
        entries.add(Entry(5f, -10f))
        entries.add(Entry(6f, -20f))
        entries.add(Entry(7f, -15f))
        entries.add(Entry(8f, -25f))
        entries.add(Entry(9f, -30f))

        // 创建一个 LineDataSet 来保存数据并自定义外观
        val dataSet = LineDataSet(entries, "样本数据")
        dataSet.color = Color.BLUE // 线条颜色
        dataSet.valueTextColor = Color.RED // 值文本颜色

        // 从 LineDataSet 创建一个 LineData 对象
        val lineData = LineData(dataSet)

        // 将 LineData 对象设置到 LineChart 上
        lineChart.data = lineData

        // 刷新图表
        lineChart.invalidate()
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLungBinding.inflate(inflater, container, false)

}
