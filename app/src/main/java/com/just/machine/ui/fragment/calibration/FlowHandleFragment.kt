package com.just.machine.ui.fragment.calibration

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.viewmodel.LiveDataEvent
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.ui.adapter.calibration.FlowAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentFlowHandleBinding
import com.justsafe.libview.chart.CustomLineChartRenderer
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint

/**
 * 手动流量定标
 */
@AndroidEntryPoint
class FlowHandleFragment : CommonBaseFragment<FragmentFlowHandleBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val inHaleFlowAdapter by lazy {
        FlowAdapter(requireContext())
    }

    private val exHaleFlowAdapter by lazy {
        FlowAdapter(requireContext())
    }


    override fun loadData() {
        viewModel.getFlows()
    }

    override fun initView() {
        binding.rvFlowHandleInhale.layoutManager = LinearLayoutManager(requireContext())

        binding.rvFlowHandleInhale.adapter = inHaleFlowAdapter

        binding.rvFlowHandleExhale.layoutManager = LinearLayoutManager(requireContext())

        binding.rvFlowHandleExhale.adapter = exHaleFlowAdapter

        initLineChart(binding.chartFlowHandleCapacityTime, 1)
        initLineChart(binding.chartFlowHandleFlowCapacity, 2)
        //设置初始数据
        binding.chartFlowHandleCapacityTime.setLineDataSetData(
            flowDataSetList()
        )
    }

    override fun initListener() {
        inHaleFlowAdapter.setItemClickListener { _, position ->
            inHaleFlowAdapter.toggleItemBackground(position)
        }

        inHaleFlowAdapter.setItemsBean(
            mutableListOf
                (FlowBean(0, "", 1, "吸气容积1", "3", "3.003", "0.92"))
        )

        exHaleFlowAdapter.setItemClickListener { _, position ->
            exHaleFlowAdapter.toggleItemBackground(position)
        }

        exHaleFlowAdapter.setItemsBean(
            mutableListOf
                (FlowBean(0, "", 1, "呼气容积1", "3", "2.993", "-1.44" ))
        )

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.FLOWS_SUCCESS -> {
                    val flowsBean: MutableList<FlowBean> = ArrayList()
                    if (it.any is List<*>) {
                        val list = it.any as List<*>
                        for (index in list) {
                            if (index is FlowBean) {
                                flowsBean.add(index)
                            }
                        }
                    }
//                    flowAdapter.setItemsBean(flowsBean)
                }
            }
        }
        binding
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFlowHandleBinding.inflate(inflater, container, false)

    private fun initLineChart(lineChart: LineChart, type: Int) {
        lineChart.apply {
            dragDecelerationFrictionCoef = 0.9f
            isDragEnabled = false
            //开启缩放功能
            setScaleEnabled(false)
            clearAnimation()
            //绘制网格线的背景
            setDrawGridBackground(false)
            //是否开启右边Y轴
            axisRight?.isEnabled = false
            //设置图标的标题
            setNoDataText("")
            setTouchEnabled(false)
            isDragEnabled = false
            description.textSize = 9f
            description?.apply {
                text = ""
            }
            xAxis?.apply {
                textSize = 8.5f
                textColor = ContextCompat.getColor(requireContext(), R.color.text3)
                //X轴最大值和最小值
                axisMaximum = if (type == 1) 15f else 3.8f
                axisMinimum = if (type == 1) 0f else 0.2f
                offsetLeftAndRight(10)
                //X轴每个值的差值(缩放时可以体现出来)
                granularity = 1.5f
                //X轴的位置
                position = XAxis.XAxisPosition.CENTER
                //是否绘制X轴的网格线(垂直于X轴)
                setDrawGridLines(false)
                //X轴的颜色
                axisLineColor = Color.parseColor("#FFD8E5FA")
                //X轴的宽度
                axisLineWidth = 2f
                //设置X轴显示固定条目,放大缩小都显示这个数
                setLabelCount(if (type == 1) 16 else 19, true)
                //是否绘制X轴
                setDrawAxisLine(false)
                //X轴每个刻度对应的值(展示的值和设置的值不同)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return String.format(if (type == 1) "%.0f" else "%.1f", value)
                    }
                }
            }
            axisLeft?.apply {
                textSize = 10f
                textColor = ContextCompat.getColor(requireContext(), R.color.text3)
                //左侧Y轴的最大值和最小值
                axisMaximum = if (type == 1) 4f else 15f
                axisMinimum = if (type == 1) -4f else -15f
                setLabelCount(if (type == 1) 9 else 11, true)
                //绘制网格线(样式虚线)
                enableGridDashedLine(0.2f, 2f, 0f)
                gridColor = ContextCompat.getColor(requireContext(), R.color.text3)
                setDrawGridLines(true)
                setDrawAxisLine(false) //绘制左边Y轴是否显示
                setDrawZeroLine(true) //是否开启0线
                isGranularityEnabled = true
            }

            legend?.apply {
                setDrawInside(false)
                formSize = 0f
            }

            val tempDataSet = LineDataSet(null, "")
            tempDataSet.lineWidth = 1.0f
            tempDataSet.color = ContextCompat.getColor(requireContext(), R.color.blue)
            tempDataSet.setDrawValues(false)
            tempDataSet.setDrawCircles(false)
            tempDataSet.setDrawCircleHole(false)
            tempDataSet.setDrawFilled(false)
            tempDataSet.mode = LineDataSet.Mode.LINEAR

            val actualDataSet = LineDataSet(null, "")
            actualDataSet.lineWidth = 1.0f
            actualDataSet.color =
                ContextCompat.getColor(requireContext(), R.color.wheel_title_bar_ok_color)
            actualDataSet.setDrawValues(false)
            actualDataSet.setDrawCircles(false)
            actualDataSet.setDrawCircleHole(false)
            actualDataSet.setDrawFilled(false)
            actualDataSet.mode = LineDataSet.Mode.LINEAR

            val lineDataSets: MutableList<ILineDataSet> = ArrayList()
            val lineData = LineData(lineDataSets)
            data = lineData
        }
    }

    private fun flowDataSetList(
        valueTextColor: Int? = com.justsafe.libview.R.color.Indigo_colorPrimary,
        dataSetColors: List<Int>? = listOf(
            com.justsafe.libview.R.color.gray,
            com.justsafe.libview.R.color.gray,
            com.justsafe.libview.R.color.gray,
            com.justsafe.libview.R.color.gray,
            com.justsafe.libview.R.color.gray,
            com.justsafe.libview.R.color.gray,
            com.justsafe.libview.R.color.gray,
            com.justsafe.libview.R.color.gray
        ),
    ): MutableList<LineDataSet> {

        val dataSetEntriesMap = mutableMapOf<String, MutableList<Entry>>()

        val entries1 = mutableListOf<Entry>()
        val entries2 = mutableListOf<Entry>()
        val entries3 = mutableListOf<Entry>()
        val entries4 = mutableListOf<Entry>()
        val entries5 = mutableListOf<Entry>()
        val entries6 = mutableListOf<Entry>()
        val entries7 = mutableListOf<Entry>()
        val entries8 = mutableListOf<Entry>()

        // 模拟十个数据点
        for (i in 0..10) {
            // 在 x 轴上以递增的方式设置数据点的 x 值，y 值可以根据需要设置
            val entry1 = Entry(i * 0.1.toFloat(), i * 0.3.toFloat())
            entries1.add(entry1)

            val entry2 = Entry(i * 0.3.toFloat(), i * 0.3.toFloat())
            entries2.add(entry2)

            val entry3 = Entry(i * 0.65.toFloat(), i * 0.3.toFloat())
            entries3.add(entry3)

            val entry4 = Entry(i * 0.9.toFloat(), i * 0.3.toFloat())
            entries4.add(entry4)

            val entry5 = Entry(i * 0.1.toFloat(), -i * 0.3.toFloat())
            entries5.add(entry5)

            val entry6 = Entry(i * 0.3.toFloat(), -i * 0.3.toFloat())
            entries6.add(entry6)

            val entry7 = Entry(i * 0.65.toFloat(), -i * 0.3.toFloat())
            entries7.add(entry7)

            val entry8 = Entry(i * 0.9.toFloat(), -i * 0.3.toFloat())
            entries8.add(entry8)
        }

        dataSetEntriesMap["1"] = entries1
        dataSetEntriesMap["2"] = entries2
        dataSetEntriesMap["3"] = entries3
        dataSetEntriesMap["4"] = entries4
        dataSetEntriesMap["5"] = entries5
        dataSetEntriesMap["6"] = entries6
        dataSetEntriesMap["7"] = entries7
        dataSetEntriesMap["8"] = entries8

        // 创建多个 LineDataSet 对象
        val dataSet1 = LineDataSet(dataSetEntriesMap["1"], "")
        val dataSet2 = LineDataSet(dataSetEntriesMap["2"], "")
        val dataSet3 = LineDataSet(dataSetEntriesMap["3"], "")
        val dataSet4 = LineDataSet(dataSetEntriesMap["4"], "")
        val dataSet5 = LineDataSet(dataSetEntriesMap["5"], "")
        val dataSet6 = LineDataSet(dataSetEntriesMap["6"], "")
        val dataSet7 = LineDataSet(dataSetEntriesMap["7"], "")
        val dataSet8 = LineDataSet(dataSetEntriesMap["8"], "")

        // 将 LineDataSet 对象添加到一个列表中
        val list = mutableListOf(
            dataSet1,
            dataSet2,
            dataSet3,
            dataSet4,
            dataSet5,
            dataSet6,
            dataSet7,
            dataSet8
        )

        for ((index, dataSet) in list.withIndex()) {

            // 确保颜色列表不为空，且颜色足够多以覆盖所有的 LineDataSet
            val color = dataSetColors?.get(index) ?: com.justsafe.libview.R.color.colorPrimary

            dataSet.setDrawCircleHole(false)

            dataSet.setDrawCircles(false)

            dataSet.enableDashedLine(1f, 2f, 1f)

            dataSet.color = ContextCompat.getColor(requireContext(), color)

            dataSet.setCircleColor(Color.BLUE)

            dataSet.lineWidth = 1f

            dataSet.circleRadius = 2f

            dataSet.valueTextSize = 10f

            if (valueTextColor != null) {
                dataSet.setCircleColor(valueTextColor)
            }
            dataSet.setDrawValues(false)

            dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        return list
    }
}