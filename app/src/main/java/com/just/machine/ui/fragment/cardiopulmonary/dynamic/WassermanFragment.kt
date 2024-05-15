package com.just.machine.ui.fragment.cardiopulmonary.dynamic

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.just.machine.ui.fragment.cardiopulmonary.result.FragmentResultLayout.ChartLayout
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentBreatheBinding
import com.just.news.databinding.FragmentDynamicDataBinding
import com.just.news.databinding.FragmentWassermanBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.MutableMap.MutableEntry


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

        binding.scChart1.setDynamicDragLine()

        binding.scChart2.setDynamicDragLine()

        val gestureDetector1 =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    // 在双击事件中执行你的逻辑
                    onChartClick(binding.chart1)
                    return true
                }
            })


        val gestureDetector2 =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    // 在双击事件中执行你的逻辑
                    onChartClick(binding.chart2)
                    return true
                }
            })


        val gestureDetector3 =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    // 在双击事件中执行你的逻辑
                    onChartClick(binding.chart3)
                    return true
                }
            })

        val gestureDetector4 =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    // 在双击事件中执行你的逻辑
                    onChartClick(binding.chart4)
                    return true
                }
            })

        val gestureDetector5 =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    // 在双击事件中执行你的逻辑
                    onChartClick(binding.chart5)
                    return true
                }
            })

        val gestureDetector6 =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    // 在双击事件中执行你的逻辑
                    onChartClick(binding.chart6)
                    return true
                }
            })

        val gestureDetector7 =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    // 在双击事件中执行你的逻辑
                    onChartClick(binding.chart7)
                    return true
                }
            })

        val gestureDetector8 =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    // 在双击事件中执行你的逻辑
                    onChartClick(binding.chart8)
                    return true
                }
            })

        val gestureDetector9 =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    // 在双击事件中执行你的逻辑
                    onChartClick(binding.chart9)
                    return true
                }
            })

        setOnTouchListenerForChart(binding.scChart1, gestureDetector1) {
        }


        setOnTouchListenerForChart(binding.scChart2, gestureDetector2) {
        }


        setOnTouchListenerForChart(binding.scChart3, gestureDetector3) {
        }


        setOnTouchListenerForChart(binding.scChart4, gestureDetector4) {
        }

        setOnTouchListenerForChart(binding.scChart5, gestureDetector5) {
        }

        setOnTouchListenerForChart(binding.scChart6, gestureDetector6) {
        }

        setOnTouchListenerForChart(binding.scChart7, gestureDetector7) {
        }

        setOnTouchListenerForChart(binding.scChart8, gestureDetector8) {
        }

        setOnTouchListenerForChart(binding.scChart9, gestureDetector9) {
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
            true
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
                    LogUtils.d(tag + child)
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
