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
import com.just.machine.util.BaseUtil
import com.just.news.R
import com.just.news.databinding.FragmentResultBinding
import com.justsafe.libview.view.DoubleTapFrameLayout


class FragmentResultLayout : FrameLayout {

    private val tag = FragmentResultLayout::class.java.name

    var binding: FragmentResultBinding

    private var mContext: Context

    private var adapter: ResultAdapter? = null

    private var chartLayout: ChartLayout? = null

    private var dynamicResultBeans: MutableList<DynamicResultBean> = ArrayList()

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

    private fun initView() {
        adapter = ResultAdapter(context)
        dynamicResultBeans.let { adapter?.setItemsBean(it) }
        binding.rvResultData.layoutManager = LinearLayoutManager(context)
        binding.rvResultData.adapter = adapter

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

        setOnTouchListenerForChart(binding.scChart1, gestureDetector1) {
        }


        setOnTouchListenerForChart(binding.scChart2, gestureDetector2) {
        }


        setOnTouchListenerForChart(binding.scChart3, gestureDetector3) {
        }


        setOnTouchListenerForChart(binding.scChart4, gestureDetector4) {
        }
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


    fun setDynamicResultBeans(beans: MutableList<DynamicResultBean>) {
        dynamicResultBeans.clear()
        dynamicResultBeans.addAll(beans)
    }

    fun setChartLayout(resultLayout: ChartLayout) {
        chartLayout = resultLayout
        when (resultLayout) {
            ChartLayout.EXTREMUM -> {
                val scatterData = generateScatterData(50)
                setupScatterChart(binding.scChart1, scatterData, ChartAxisSettings())
                onChartClick(binding.chart1)
            }

            ChartLayout.OXYGEN -> { // 无氧域分析的实现
                val scatterData = generateScatterData(50)
                setupScatterChart(binding.scChart2, scatterData, ChartAxisSettings())
            }

            ChartLayout.COMPENSATORY -> { // 呼吸代偿点分析的实现
                val scatterData = generateScatterData(50)//散点图数据
                setupScatterChart(binding.scChart2, scatterData, ChartAxisSettings())
                setupScatterChart(binding.scChart3, scatterData, ChartAxisSettings())
            }

            ChartLayout.SLOP -> TODO()
            ChartLayout.FLOWRATE -> TODO()
        }
    }


    private var isExpanded = false


    private fun generateScatterData(numPoints: Int): ScatterData {
        val entries = mutableListOf<Entry>()
        for (i in 0 until numPoints) {
            val x = i.toFloat()
            val y = i.toFloat()
            entries.add(Entry(x, y))
        }

        val dataSet = ScatterDataSet(entries, "Scatter Data Set")
        dataSet.color = Color.RED
        dataSet.setDrawValues(false)

        val dataSets = ArrayList<IScatterDataSet>()
        dataSets.add(dataSet)

        return ScatterData(dataSets)
    }


    private fun setupScatterChart(
        scatterChart: ScatterChart,
        scatterData: ScatterData,
        chartBean: ChartAxisSettings
    ) {
        scatterChart.data = scatterData
        // 设置X轴和Y轴的最大最小值

        scatterChart.xAxis.axisMinimum = chartBean.axisMinimumL
        scatterChart.xAxis.axisMaximum = chartBean.axisMaximumL
        scatterChart.axisLeft.axisMinimum = chartBean.axisMinimumR
        scatterChart.axisLeft.axisMaximum = chartBean.axisMaximumR
        scatterChart.axisRight.axisMinimum = chartBean.axisMinimumR
        scatterChart.axisRight.axisMaximum = chartBean.axisMaximumR

        // 设置Y轴的标签间隔
        scatterChart.axisLeft.granularity = chartBean.granularity!! // 每个间隔10个单位
        scatterChart.axisLeft.labelCount = chartBean.labelCount!! // 尽量分成11个标签（包括最大值和最小值）
        scatterChart.axisRight.granularity = chartBean.granularity!! // 每个间隔10个单位
        scatterChart.axisRight.labelCount = chartBean.labelCount!! // 尽量分成11个标签（包括最大值和最小值）

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

data class ChartAxisSettings(
    //x轴刻度
    val axisMinimumL: Float = 0f,
    val axisMaximumL: Float = 100f,

    //y轴刻度
    val axisMinimumR: Float = 0f,
    val axisMaximumR: Float = 100f,

    //y轴标签,10刻度为一单位
    var granularity: Float? = null,
    var labelCount: Int? = null
) {
    init {
        granularity = axisMaximumR.div(10)
        labelCount = (axisMaximumR.div(10).plus(1)).toInt()
    }
}
