package com.just.machine.ui.fragment.cardiopulmonary.staticfragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.toast
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.just.machine.model.staticlung.RoutineLungBean
import com.just.machine.ui.adapter.RoutineLungAdapter
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

    private val adapter by lazy { RoutineLungAdapter(requireContext()) }

    override fun loadData() {//懒加载

    }


    override fun initView() {
        val lineChart = binding.chartFvc

        // 创建折线图的样本数据
        val entries = arrayListOf<Entry>()
        entries.add(Entry(0f, 10f))
        entries.add(Entry(1f, 20f))
        entries.add(Entry(2f, 15f))
        entries.add(Entry(3f, 25f))
        entries.add(Entry(4f, 30f))
        entries.add(Entry(5f, 40f))
        entries.add(Entry(6f, 50f))
        entries.add(Entry(7f, 80f))
        entries.add(Entry(8f, -10f))
        entries.add(Entry(9f, -20f))
        entries.add(Entry(10f, -15f))
        entries.add(Entry(11f, -25f))
        entries.add(Entry(12f, -30f))
        entries.add(Entry(13f, -50f))
        entries.add(Entry(14f, -80f))


        // 设置 Y 轴的最小值和最大值，以确保包含所有单数刻度
        lineChart.axisLeft.axisMinimum = -90f // 设置 Y 轴最小值
        lineChart.axisLeft.axisMaximum = 90f // 设置 Y 轴最大值

        // 创建一个 LineDataSet 来保存数据并自定义外观
        val dataSet = LineDataSet(entries, "样本数据")
        dataSet.color = Color.BLUE // 线条颜色
        dataSet.valueTextColor = Color.RED // 值文本颜色

        // 从 LineDataSet 创建一个 LineData 对象
        val lineData = LineData(dataSet)

        // 将 LineData 对象设置到 LineChart 上
        lineChart.data = lineData

        val xAxis = lineChart.xAxis

        xAxis.granularity = 1f     //这个很重要

        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // 设置折线图的上下边距
//        lineChart.setExtraOffsets(10f, 50f, 10f, 10f) // 上、左、下、右边距

        // 设置标题
        lineChart.description.text = "负数折线图示例"
        lineChart.description.setPosition(15f,15f)
        lineChart.description.textSize = 12f
        lineChart.description.textColor = Color.BLACK
//        lineChart.description.xOffset = 120f
//        lineChart.description.yOffset = 200f // 正表示向上偏移



        // 刷新图表
        lineChart.invalidate()


        binding.rvFvc.layoutManager = LinearLayoutManager(requireContext())

        adapter.setItemsBean(
            mutableListOf(
                RoutineLungBean(
                    "2024qwqdqwqqeqweq", "20", "9", "1","111","111"
                ), RoutineLungBean(
                    "2024121313123", "20", "9", "1"
                ), RoutineLungBean(
                    "2024aghhhhjjhhh", "20", "9", "1"
                )
            )
        )

        adapter.setItemClickListener { _, position ->
            adapter.toggleItemBackground(position)
        }

        binding.rvFvc.adapter = adapter
    }

    override fun initListener() {
        // 设置选中值监听器
        binding.chartFvc.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                // 当用户选择值时触发该方法
                if (e == null) return

                // 获取选中的值
                val xValue = e.x
                val yValue = e.y

                toast("Selected value: x = $xValue, y = $yValue")
            }

            override fun onNothingSelected() {
                // 当没有值被选中时触发该方法
            }
        })
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLungBinding.inflate(inflater, container, false)

}
