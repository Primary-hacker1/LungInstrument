package com.just.machine.ui.fragment.cardiopulmonary.result

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.ScatterData
import com.just.machine.model.DynamicResultBean
import com.just.machine.model.lungdata.AnlyCpxTableModel
import com.just.machine.ui.adapter.ResultAdapter
import com.just.machine.ui.fragment.cardiopulmonary.result.FragmentResultLayout.ChartLayout
import com.just.news.R
import com.just.news.databinding.FragmentResultBinding
import com.justsafe.libview.chart.ResultScatterChart
import com.xxmassdeveloper.mpchartexample.ValueFormatter


class FragmentResultLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: FragmentResultBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.fragment_result,
        this,
        true
    )

    private val tag = FragmentResultLayout::class.java.name

    private var adapter: ResultAdapter? = null //用于显示结果适配器

    private var chartLayout: ChartLayout? = null //用于传递fragment的标识

    var model = AnlyCpxTableModel()//数据分析结果

    private var isExpanded = false //是否展开

    private var dynamicResultBeans: MutableList<DynamicResultBean> = ArrayList()//动态结果数据

    enum class ChartLayout {
        EXTREMUM,//极值分析
        OXYGEN,//无氧域分析
        COMPENSATORY,//呼吸代偿点分析

        //        SLOP,//斜率分析
        FLOW_RATE//动态流速环分析
    }

    init {
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

        setOnTouchListenerForChart(binding.scChart1, gestureDetector1)


        setOnTouchListenerForChart(binding.scChart2, gestureDetector2)


        setOnTouchListenerForChart(binding.scChart3, gestureDetector3)


        setOnTouchListenerForChart(binding.scChart4, gestureDetector4)
    }

    /**
     * @param chart 散点图控件
     * @param gestureDetector 手势检测器
     * */
    @SuppressLint("ClickableViewAccessibility")
    fun setOnTouchListenerForChart(
        chart: View,
        gestureDetector: GestureDetector
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


    fun setDynamicResultBeans() {
        dynamicResultBeans.clear()
        val bean: MutableList<Pair<String, Any?>> = model.toMutableList()
        for ((key, value) in bean) {
            val dynamicResultBean = DynamicResultBean()
            dynamicResultBean.resultName = key
            dynamicResultBean.resultData = value.toString()
            dynamicResultBeans.add(dynamicResultBean)
        }
    }

    /**
     * @param resultLayout 枚举类用于传递fragment的标识
     * @param chartAxisSettings1 自定义X轴和Y轴的刻度
     * */
    fun setChartLayout(
        resultLayout: ChartLayout,
        chartAxisSettings1: ChartAxisSettings,
        chartAxisSettings2: ChartAxisSettings? = ChartAxisSettings(),
        chartAxisSettings3: ChartAxisSettings? = ChartAxisSettings(),
        chartAxisSettings4: ChartAxisSettings? = ChartAxisSettings()
    ) {
        chartLayout = resultLayout
        when (resultLayout) {
            ChartLayout.EXTREMUM -> {
                setupScatterChart(binding.scChart1, chartAxisSettings1)
                onChartClick(binding.chart1)
            }

            ChartLayout.OXYGEN -> { // 无氧域分析的实现
                setupScatterChart(binding.scChart1, chartAxisSettings1)
                if (chartAxisSettings2 != null) {
                    setupScatterChart(binding.scChart2, chartAxisSettings2)
                }
                if (chartAxisSettings3 != null) {
                    setupScatterChart(binding.scChart3, chartAxisSettings3)
                }
                if (chartAxisSettings4 != null) {
                    setupScatterChart(binding.scChart4, chartAxisSettings4, true)
                }
            }

            ChartLayout.COMPENSATORY -> { // 呼吸代偿点分析的实现
                setupScatterChart(binding.scChart1, chartAxisSettings1)
                if (chartAxisSettings2 != null) {
                    setupScatterChart(binding.scChart2, chartAxisSettings2)
                }
                if (chartAxisSettings3 != null) {
                    setupScatterChart(binding.scChart3, chartAxisSettings3)
                }
                if (chartAxisSettings4 != null) {
                    setupScatterChart(binding.scChart4, chartAxisSettings4)
                    binding.scChart4.setDynamicAxis()//动态设置双轴
                }
            }

//            ChartLayout.SLOP -> {}}
            ChartLayout.FLOW_RATE -> {}
        }
    }

    fun setScatterData() {//先模拟数据
        binding.scChart1.startUpdatingData()
        binding.scChart2.startUpdatingData()
        binding.scChart3.startUpdatingData()
        binding.scChart4.startUpdatingData()
    }

    private fun setupScatterChart(
        scatterChart: ResultScatterChart,
        chartBean: ChartAxisSettings,
        isChart4: Boolean? = false,
        scatterData: ScatterData? = ScatterData()
    ) {
        scatterChart.data = scatterData

        // 设置X轴和Y轴的最大最小值
        scatterChart.xAxis.axisMinimum = chartBean.axisMinimumL!!
        scatterChart.xAxis.axisMaximum = chartBean.axisMaximumL!!
        scatterChart.axisLeft.axisMinimum = chartBean.axisMinimumYL!!
        scatterChart.axisLeft.axisMaximum = chartBean.axisMaximumYL!!
        scatterChart.axisRight.axisMinimum = chartBean.axisMinimumYR!!
        scatterChart.axisRight.axisMaximum = chartBean.axisMaximumYR!!

        // 设置X轴的标签间隔和标签数量
        scatterChart.xAxis.granularity = chartBean.xGranularity!! // X轴每个间隔的单位
        scatterChart.xAxis.labelCount = chartBean.xLabelCount!! // X轴的标签数量

        scatterChart.xAxis.valueFormatter =
            CustomValueFormatter(chartBean.xGranularity!!)

        // 设置Y轴的标签间隔和标签数量
        scatterChart.axisLeft.granularity = chartBean.granularity!! // Y轴每个间隔的单位
        scatterChart.axisLeft.labelCount = chartBean.labelCount!! // Y轴的标签数量

        if (isChart4 == true) {
            scatterChart.axisLeft.valueFormatter = CustomValueFormatterDecimal(
                chartBean.granularity!!
            )
        } else {
            scatterChart.axisLeft.valueFormatter = CustomValueFormatter(
                chartBean.granularity!!
            )
        }

        scatterChart.axisRight.granularity = chartBean.granularityR!! // Y轴每个间隔的单位
        scatterChart.axisRight.labelCount = chartBean.labelCountR!! // Y轴的标签数量
        scatterChart.axisRight.valueFormatter = CustomValueFormatter(
            chartBean.granularity!!,
        )

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
                    child.visibility = View.VISIBLE// 切换目标 FrameLayout 的可见性
                } else {
                    child.visibility = View.GONE
                }
            }
            true
        }

        binding.gridLayout.requestLayout()// 请求重新布局
    }
}


class CustomValueFormatter(
    private val granularity: Float,
) :
    ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val roundedValue = Math.round(value / granularity) * granularity
        return roundedValue.toInt().toString() // 将结果转换为整数，并转换为字符串
    }
}

class CustomValueFormatterDecimal(
    private val granularity: Float
) :
    ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val roundedValue = Math.round(value / granularity) * granularity
        return roundedValue.toString()
    }
}

data class ChartAxisSettings(
    // X轴刻度
    var axisMinimumL: Float? = 0f,
    var axisMaximumL: Float? = 100f,
    var xGranularity: Float? = axisMaximumL?.div(10),
    var xLabelCount: Int? = (axisMaximumL?.div(xGranularity?.toInt()!!)?.plus(1))?.toInt(),

    // Y轴刻度
    var axisMinimumYL: Float? = 0f,//左轴
    var axisMaximumYL: Float? = 100f,//左轴
    var axisMinimumYR: Float? = axisMinimumYL,//右轴
    var axisMaximumYR: Float? = axisMaximumYL,//右轴
    var granularity: Float? = axisMaximumYL?.div(10),//左轴
    var labelCount: Int? = (axisMaximumYL?.div(granularity?.toInt()!!)?.plus(1))?.toInt(),//左轴
    var granularityR: Float? = axisMaximumYR?.div(10),//右轴
    var labelCountR: Int? = (axisMaximumYR?.div(granularity?.toInt()!!)?.plus(1))?.toInt()//右轴
)


