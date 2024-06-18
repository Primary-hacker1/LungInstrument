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
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.ui.adapter.calibration.FlowAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentFlowHandleBinding
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint

/**
 * 手动流量定标
 */
@AndroidEntryPoint
class FlowHandleFragment : CommonBaseFragment<FragmentFlowHandleBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val flowAdapter by lazy {
        FlowAdapter(requireContext())
    }


    override fun loadData() {
        viewModel.getFlows()
    }

    override fun initView() {
        binding.rvFlowHandleInhale.layoutManager = LinearLayoutManager(requireContext())

        binding.rvFlowHandleInhale.adapter = flowAdapter

        initLineChart(binding.chartFlowHandleCapacityTime,1)
        initLineChart(binding.chartFlowHandleFlowCapacity,2)
    }

    override fun initListener() {
        flowAdapter.setItemClickListener { item, position ->
            flowAdapter.toggleItemBackground(position)
        }

        flowAdapter.setItemsBean(
            mutableListOf
                (FlowBean(0, "", 1, "容积1", "3", "3.003","0.92","通过"))
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

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFlowHandleBinding.inflate(inflater, container, false)

    private fun initLineChart(lineChart: LineChart,type:Int) {
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
                textSize = 10f
                textColor = ContextCompat.getColor(activity!!, R.color.text3)
                //X轴最大值和最小值
                axisMaximum = if(type == 1) 13f else 3.8f
                axisMinimum = if(type == 1) 0f else 0.2f
                offsetLeftAndRight(10)
                //X轴每个值的差值(缩放时可以体现出来)
                granularity = 1.5f
                //X轴的位置
                position = XAxis.XAxisPosition.BOTTOM
                //是否绘制X轴的网格线(垂直于X轴)
                setDrawGridLines(false)
                //X轴的颜色
                axisLineColor = Color.parseColor("#FFD8E5FA")
                //X轴的宽度
                axisLineWidth = 2f
                //设置X轴显示固定条目,放大缩小都显示这个数
                setLabelCount(if(type == 1) 14 else 19, true)
                //是否绘制X轴
                setDrawAxisLine(false)
                //X轴每个刻度对应的值(展示的值和设置的值不同)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return String.format(if(type == 1) "%.0f" else "%.1f", value)
                    }
                }
            }
            axisLeft?.apply {
                textColor = ContextCompat.getColor(requireContext(), R.color.text3)
                //左侧Y轴的最大值和最小值
                axisMaximum = if(type == 1) 4f else 15f
                axisMinimum = if(type == 1) -4f else -15f
                setLabelCount(if(type == 1) 9 else 11, true)
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
            actualDataSet.color = ContextCompat.getColor(requireContext(), R.color.wheel_title_bar_ok_color)
            actualDataSet.setDrawValues(false)
            actualDataSet.setDrawCircles(false)
            actualDataSet.setDrawCircleHole(false)
            actualDataSet.setDrawFilled(false)
            actualDataSet.mode = LineDataSet.Mode.LINEAR

            val lineDataSets: MutableList<ILineDataSet> = ArrayList()
            lineDataSets.add(tempDataSet)
            lineDataSets.add(actualDataSet)
            val lineData = LineData(lineDataSets)
            data = lineData
        }
    }
}