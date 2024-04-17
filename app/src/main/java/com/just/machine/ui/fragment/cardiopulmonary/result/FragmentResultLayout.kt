package com.just.machine.ui.fragment.cardiopulmonary.result

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.just.machine.model.DynamicResultBean
import com.just.machine.ui.adapter.ResultAdapter
import com.just.news.R
import com.just.news.databinding.FragmentResultBinding


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

        binding.scChart1.setOnClickListener {
            onChartClick(binding.scChart1)
        }
    }

    fun setChartLayout(resultLayout: ChartLayout) {
        when (resultLayout.name) {
            ChartLayout.EXTREMUM.name -> {
                val scatterData = generateScatterData(50)
                setupScatterChart(binding.scChart1, scatterData)
                binding.scChart1.invalidate() // 刷新图表
            }

            ChartLayout.OXYGEN.name -> {//无氧域分析

            }
        }
    }

    private var isExpanded = false

    private fun onChartClick(view: ScatterChart) {
        if (isExpanded) {
            // 恢复布局为2x2
            binding.gridLayout.columnCount = 2
            binding.gridLayout.rowCount = 2
            isExpanded = false
        } else {
            // 展开布局以显示一个全屏的图表
            binding.gridLayout.columnCount = 1
            binding.gridLayout.rowCount = 1
            isExpanded = true
        }

        // 更新布局
        view.layoutParams = GridLayout.LayoutParams()
    }

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


        scatterChart.invalidate() // 刷新图表
    }


    private fun getLayout(): Int {
        return R.layout.fragment_result
    }
}
