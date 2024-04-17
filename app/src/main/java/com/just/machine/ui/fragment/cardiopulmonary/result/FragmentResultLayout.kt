package com.just.machine.ui.fragment.cardiopulmonary.result

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.network.LogUtils
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.just.machine.model.DynamicResultBean
import com.just.machine.ui.adapter.ResultAdapter
import com.just.news.R
import com.just.news.databinding.FragmentResultBinding
import com.justsafe.libview.view.DoubleTapFrameLayout


class FragmentResultLayout : FrameLayout {

    private val tag = FragmentResultLayout::class.java.name

    var binding: FragmentResultBinding

    private var mContext: Context

    private var adapter: ResultAdapter? = null

    enum class ChartLayout {
        EXTREMUM,//极值分析
        OXYGEN,//无氧域分析
        COMPENSATORY,//呼吸代偿点分析
        SLOP,//斜率分析
        FLOWRATE//动态流速环分析
    }

    constructor(context: Context) : super(context) {
        mContext = context
        val layoutInflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(layoutInflater, getLayout(), this, true)
        initView()
    }

    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes) {
        mContext = context
        val layoutInflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(layoutInflater, getLayout(), this, true)
        initView()
    }

    constructor(context: Context, attributes: AttributeSet?, int: Int) : super(
        context,
        attributes,
        int
    ) {
        mContext = context
        val layoutInflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(layoutInflater, getLayout(), this, true)
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        adapter = ResultAdapter(context)
        adapter?.setItemsBean(
            mutableListOf(
                DynamicResultBean("Time", "2024-4-16"),
                DynamicResultBean("Load", "80"),
                DynamicResultBean("HR(1/min)", "2024-4-16"),
            )
        )
        binding.rvResultData.layoutManager = LinearLayoutManager(context)
        binding.rvResultData.adapter = adapter

        val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                // 在双击事件中执行你的逻辑
                onChartClick(binding.chart1)
                return true
            }
        })

        binding.scChart1.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            // 如果你还想处理其他触摸事件，可以在这里添加逻辑
            false // 返回 true 表示已经消费了触摸事件
        }

        binding.chart2.setOnDoubleTapListener(object : DoubleTapFrameLayout.OnDoubleTapListener {
            override fun onDoubleTap() {
                // 处理双击事件
                onChartClick(binding.chart2)
            }
        })

        binding.chart3.setOnDoubleTapListener(object : DoubleTapFrameLayout.OnDoubleTapListener {
            override fun onDoubleTap() {
                // 处理双击事件
                onChartClick(binding.chart3)
            }
        })

        binding.chart4.setOnDoubleTapListener(object : DoubleTapFrameLayout.OnDoubleTapListener {
            override fun onDoubleTap() {
                // 处理双击事件
                onChartClick(binding.chart4)
            }
        })
    }

    fun setChartLayout(resultLayout: ChartLayout) {
        when (resultLayout.name) {
            ChartLayout.EXTREMUM.name -> {
                val scatterData = generateScatterData(50)
                setupScatterChart(binding.scChart1, scatterData)
            }

            ChartLayout.OXYGEN.name -> {//无氧域分析

            }
        }
    }

    private var isExpanded = false

    private fun generateScatterData(numPoints: Int): ScatterData {
        val entries = mutableListOf<Entry>()
        for (i in 0 until numPoints) {
            val x = (Math.random() * 100).toFloat()
            val y = (Math.random() * 100).toFloat()
            entries.add(Entry(x, y))
        }

        val dataSet = ScatterDataSet(entries, "Scatter Data Set")
        dataSet.color = Color.RED
        dataSet.setDrawValues(false) // 不绘制值

        val dataSets = ArrayList<IScatterDataSet>()
        dataSets.add(dataSet)

        return ScatterData(dataSets)
    }

    private fun setupScatterChart(scatterChart: ScatterChart, scatterData: ScatterData) {
        scatterChart.data = scatterData

        // 设置X轴和Y轴的最大最小值
        scatterChart.xAxis.axisMinimum = 0f
        scatterChart.xAxis.axisMaximum = 100f
        scatterChart.axisLeft.axisMinimum = 0f
        scatterChart.axisLeft.axisMaximum = 100f
        scatterChart.axisRight.axisMinimum = 0f
        scatterChart.axisRight.axisMaximum = 100f

        // 设置Y轴的标签间隔
        scatterChart.axisLeft.granularity = 10f // 每个间隔10个单位
        scatterChart.axisLeft.labelCount = 11 // 尽量分成11个标签（包括最大值和最小值）
        scatterChart.axisRight.granularity = 10f // 每个间隔10个单位
        scatterChart.axisRight.labelCount = 11 // 尽量分成11个标签（包括最大值和最小值）

        // 可选：配置图表样式
        scatterChart.setDrawGridBackground(false)
        scatterChart.axisLeft.setDrawGridLines(false)
        scatterChart.axisRight.setDrawGridLines(false)
        scatterChart.xAxis.setDrawGridLines(false)
        scatterChart.legend.isEnabled = false // 隐藏图例
        scatterChart.description.isEnabled = false // 隐藏描述

        scatterChart.isDragEnabled = false
        scatterChart.setPinchZoom(false)
        scatterChart.isDoubleTapToZoomEnabled = false

        scatterChart.invalidate() // 刷新图表
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


    private fun getLayout(): Int {
        return R.layout.fragment_result
    }
}
