package com.just.machine.ui.fragment.cardiopulmonary.dynamic

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.MPPointD
import com.just.machine.ui.fragment.cardiopulmonary.result.FragmentResultLayout.ChartLayout
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentWassermanBinding
import com.justsafe.libview.R
import com.justsafe.libview.chart.BaseLineChart
import com.justsafe.libview.chart.VerticalLineView
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/4/2
 * 动态肺九图
 *@author zt
 */
@AndroidEntryPoint
class WassermanFragment : CommonBaseFragment<FragmentWassermanBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private var isExpanded = false

    private var chartLayout: ChartLayout? = null

    override fun loadData() {//懒加载

    }

    override fun initView() {
        // 创建折线图的样本数据
        val entries = arrayListOf<Entry>()
        for (index in 0..30) {
            entries.add(Entry(index.toFloat(), index.toFloat() / 6 - 3))
        }

        binding.scChart1.setLineDataSetData(
            binding.scChart1.flowDataSetList()
        )//设置数据

        binding.scChart1.setLineChartFlow(
            yAxisMinimum = -5f,
            yAxisMaximum = 5f,
            countMaxX = 30f,
            granularityY = 1f,
            granularityX = 1f,
            titleCentent = "动态肺常规"
        )

        binding.scChart2.setLineDataSetData(
            binding.scChart2.flowDataSetList()
        )//设置数据

        binding.scChart2.setLineChartFlow(
            yAxisMinimum = -5f,
            yAxisMaximum = 5f,
            countMaxX = 30f,
            granularityY = 1f,
            granularityX = 1f,
            titleCentent = "动态肺常规"
        )

        setDynamicDragLine(binding.scChart1,binding.chart1)
        setDynamicDragLine(binding.scChart2,binding.chart2)
        setDynamicDragLine(binding.scChart3,binding.chart3)
        setDynamicDragLine(binding.scChart4,binding.chart4)
        setDynamicDragLine(binding.scChart5,binding.chart5)
        setDynamicDragLine(binding.scChart6,binding.chart6)
        setDynamicDragLine(binding.scChart7,binding.chart7)
        setDynamicDragLine(binding.scChart8,binding.chart8)
        setDynamicDragLine(binding.scChart9,binding.chart9)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setDynamicDragLine(chart: BaseLineChart, frameLayout: FrameLayout){
        val gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    // 在双击事件中执行你的逻辑
                    onChartClick(frameLayout)
                    return true
                }
            })

        setOnTouchListenerForChart(chart, gestureDetector) {
        }


        val xAxis = chart.xAxis
        val initialPosition = 10f // 初始位置
        val limitLine = LimitLine(initialPosition, "拖拽线")
        limitLine.lineColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        limitLine.lineWidth = 2f
        xAxis.addLimitLine(limitLine)
        chart.invalidate() // 刷新图表

        // 创建 VerticalLineView 实例
        val verticalLineView = VerticalLineView(requireContext())

        // 设置 VerticalLineView 的布局参数，例如宽度和高度
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        verticalLineView.layoutParams = layoutParams

        // 将 VerticalLineView 添加到你的布局中
        chart.addView(verticalLineView)

        chart.setOnTouchListener { _, event ->
            if (gestureDetector.onTouchEvent(event)) return@setOnTouchListener true
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    // 获取触摸点的 x 坐标
                    val x = event.x

                    // 更新 VerticalLineView 的位置
                    verticalLineView.setXPosition(x)

                    val outputPoint = MPPointD.getInstance(0.0, 0.0)
                    chart.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(event.x, event.y, outputPoint)

                    // 从转换后的结果中获取 x 轴的数据值
                    val xValue = outputPoint.x.toFloat() // 将 double 转换为 float

                    // 更新 LimitLine 的位置到新的 x 坐标值
                    limitLine.limit = xValue

                    // 获取与垂直线相交的数据点
                    val entriesMap = mutableMapOf<ILineDataSet, MutableList<Entry>>()
                    val dataSets = chart.data?.dataSets ?: return@setOnTouchListener true
                    for (dataSet in dataSets) {
                        val entries = mutableListOf<Entry>()
                        val entryForXValue = dataSet.getEntryForXValue(xValue, Float.NaN)
                        entryForXValue?.let { entries.add(it) }
                        if (entries.isNotEmpty()) {
                            entriesMap[dataSet] = entries
                        }
                    }

                    // 这里的 entriesMap 包含了每个数据集与垂直线相交的数据点列表
                    for ((dataSet, entries) in entriesMap) {
                        for (entry in entries) {
                            val yValue = entry.y
                            // 这里处理 yValue，例如打印或存储
                            println("DataSet: ${dataSet.label}, Y value: $yValue")
                        }
                    }

                    chart.invalidate() // 刷新图表以显示新位置的 LimitLine

                    MPPointD.recycleInstance(outputPoint) // 回收 MPPointD 实例
                    true
                }
                else -> false
            }
        }
    }

    override fun initListener() {

    }

    @SuppressLint("ClickableViewAccessibility")
    fun setOnTouchListenerForChart(
        chart: View,
        gestureDetector: GestureDetector,
        onDoubleClickAction: () -> Unit
    ) {
        // 用于跟踪最后一次触摸事件的时间戳
        chart.setOnTouchListener { _, event ->
            if (chartLayout == ChartLayout.EXTREMUM) {
                return@setOnTouchListener true
            }

            // 手势检测器处理触摸事件
            gestureDetector.onTouchEvent(event)

            // 返回 true 表示已经消费了触摸事件
            false
        }
    }

    private fun onChartClick(view: View) {
        val chart = view as FrameLayout
        isExpanded = if (isExpanded) {
            // 更新所有子视图的布局参数以适应2x2网格
            for (i in 0 until binding.gridLayout.childCount) {
                val child = binding.gridLayout.getChildAt(i) as FrameLayout
                child.visibility = View.VISIBLE
            }
            false
        } else {
            // 更新所有子视图的布局参数以适应1x1网格（全屏）
            for (i in 0 until binding.gridLayout.childCount) {
                val child = binding.gridLayout.getChildAt(i)
                if (child == chart) {
                    LogUtils.d(tag + child)
                    child.visibility = View.VISIBLE// 切换目标 FrameLayout 的可见性
                } else {
                    child.visibility = View.GONE
                }
            }
            true
        }

        // 请求重新布局
        binding.gridLayout.requestLayout()
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentWassermanBinding.inflate(inflater, container, false)

}
