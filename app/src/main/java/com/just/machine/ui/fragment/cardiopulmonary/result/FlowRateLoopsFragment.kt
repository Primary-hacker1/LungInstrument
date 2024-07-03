package com.just.machine.ui.fragment.cardiopulmonary.result

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentRateLoopsBinding
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/6/19
 * 动态流速环分析
 *@author zt
 */
@AndroidEntryPoint
class FlowRateLoopsFragment : CommonBaseFragment<FragmentRateLoopsBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    // 容量-时间
    private val inVolSec1DataSet: LineDataSet by lazy {
        LineDataSet(null, "")
    }
    private val outVolSec1DataSet: LineDataSet by lazy {
        LineDataSet(null, "")
    }

    private val inVolSec2DataSet: LineDataSet by lazy {
        LineDataSet(null, "")
    }

    private val outVolSec2DataSet: LineDataSet by lazy {
        LineDataSet(null, "")
    }

    private val inVolSec3DataSet: LineDataSet by lazy {
        LineDataSet(null, "")
    }
    private val outVolSec3DataSet: LineDataSet by lazy {
        LineDataSet(null, "")
    }

    private val inVolSec4DataSet: LineDataSet by lazy {
        LineDataSet(null, "")
    }
    private val outVolSec4DataSet: LineDataSet by lazy {
        LineDataSet(null, "")
    }

    private val inVolSec5DataSet: LineDataSet by lazy {
        LineDataSet(null, "")
    }
    private val outVolSec5DataSet: LineDataSet by lazy {
        LineDataSet(null, "")
    }


    override fun loadData() {//懒加载

    }

    override fun initView() {

        binding.llSave.setNoRepeatListener {

        }

        binding.llReset.setNoRepeatListener {

        }

        initLineChart(binding.lcFlow, 1)
        initLineChart(binding.lcVol, 1)
        initLineChart(binding.lcFvcFlow, 1)
    }


    override fun initListener() {

    }

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
                axisMinimum = 0f
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
            val lineDataSets: MutableList<ILineDataSet> = ArrayList()
            if (type == 1) {
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

                dataSetEntriesMap.forEach { (key, value) ->
                    val line = LineDataSet(null, "")
                    line.entries = value
                    line.lineWidth = 1.0f
                    line.color =
                        ContextCompat.getColor(requireContext(), com.justsafe.libview.R.color.gray)
                    line.setDrawValues(false)
                    line.setDrawCircles(false)
                    line.setDrawCircleHole(false)
                    line.setDrawFilled(false)
                    line.enableDashedLine(1f, 2f, 1f)
                    line.mode = LineDataSet.Mode.LINEAR

                    lineDataSets.add(line)
                }

                inVolSec1DataSet.lineWidth = 1.0f
                inVolSec1DataSet.color = ContextCompat.getColor(requireContext(), R.color.green)
                inVolSec1DataSet.setDrawValues(false)
                inVolSec1DataSet.setDrawCircles(false)
                inVolSec1DataSet.setDrawCircleHole(false)
                inVolSec1DataSet.setDrawFilled(false)
                inVolSec1DataSet.mode = LineDataSet.Mode.LINEAR

                outVolSec1DataSet.lineWidth = 1.0f
                outVolSec1DataSet.color = ContextCompat.getColor(requireContext(), R.color.green)
                outVolSec1DataSet.setDrawValues(false)
                outVolSec1DataSet.setDrawCircles(false)
                outVolSec1DataSet.setDrawCircleHole(false)
                outVolSec1DataSet.setDrawFilled(false)
                outVolSec1DataSet.mode = LineDataSet.Mode.LINEAR

                inVolSec2DataSet.lineWidth = 1.0f
                inVolSec2DataSet.color = ContextCompat.getColor(requireContext(), R.color.blue)
                inVolSec2DataSet.setDrawValues(false)
                inVolSec2DataSet.setDrawCircles(false)
                inVolSec2DataSet.setDrawCircleHole(false)
                inVolSec2DataSet.setDrawFilled(false)
                inVolSec2DataSet.mode = LineDataSet.Mode.LINEAR

                outVolSec2DataSet.lineWidth = 1.0f
                outVolSec2DataSet.color = ContextCompat.getColor(requireContext(), R.color.blue)
                outVolSec2DataSet.setDrawValues(false)
                outVolSec2DataSet.setDrawCircles(false)
                outVolSec2DataSet.setDrawCircleHole(false)
                outVolSec2DataSet.setDrawFilled(false)
                outVolSec2DataSet.mode = LineDataSet.Mode.LINEAR

                inVolSec3DataSet.lineWidth = 1.0f
                inVolSec3DataSet.color = ContextCompat.getColor(requireContext(), R.color.brown)
                inVolSec3DataSet.setDrawValues(false)
                inVolSec3DataSet.setDrawCircles(false)
                inVolSec3DataSet.setDrawCircleHole(false)
                inVolSec3DataSet.setDrawFilled(false)
                inVolSec3DataSet.mode = LineDataSet.Mode.LINEAR

                outVolSec3DataSet.lineWidth = 1.0f
                outVolSec3DataSet.color = ContextCompat.getColor(requireContext(), R.color.brown)
                outVolSec3DataSet.setDrawValues(false)
                outVolSec3DataSet.setDrawCircles(false)
                outVolSec3DataSet.setDrawCircleHole(false)
                outVolSec3DataSet.setDrawFilled(false)
                outVolSec3DataSet.mode = LineDataSet.Mode.LINEAR

                inVolSec4DataSet.lineWidth = 1.0f
                inVolSec4DataSet.color = ContextCompat.getColor(requireContext(), R.color.olive)
                inVolSec4DataSet.setDrawValues(false)
                inVolSec4DataSet.setDrawCircles(false)
                inVolSec4DataSet.setDrawCircleHole(false)
                inVolSec4DataSet.setDrawFilled(false)
                inVolSec4DataSet.mode = LineDataSet.Mode.LINEAR

                outVolSec4DataSet.lineWidth = 1.0f
                outVolSec4DataSet.color = ContextCompat.getColor(requireContext(), R.color.olive)
                outVolSec4DataSet.setDrawValues(false)
                outVolSec4DataSet.setDrawCircles(false)
                outVolSec4DataSet.setDrawCircleHole(false)
                outVolSec4DataSet.setDrawFilled(false)
                outVolSec4DataSet.mode = LineDataSet.Mode.LINEAR

                inVolSec5DataSet.lineWidth = 1.0f
                inVolSec5DataSet.color =
                    ContextCompat.getColor(requireContext(), R.color.blueViolet)
                inVolSec5DataSet.setDrawValues(false)
                inVolSec5DataSet.setDrawCircles(false)
                inVolSec5DataSet.setDrawCircleHole(false)
                inVolSec5DataSet.setDrawFilled(false)
                inVolSec5DataSet.mode = LineDataSet.Mode.LINEAR

                outVolSec5DataSet.lineWidth = 1.0f
                outVolSec5DataSet.color =
                    ContextCompat.getColor(requireContext(), R.color.blueViolet)
                outVolSec5DataSet.setDrawValues(false)
                outVolSec5DataSet.setDrawCircles(false)
                outVolSec5DataSet.setDrawCircleHole(false)
                outVolSec5DataSet.setDrawFilled(false)
                outVolSec5DataSet.mode = LineDataSet.Mode.LINEAR

                lineDataSets.add(inVolSec1DataSet)
                lineDataSets.add(outVolSec1DataSet)
                lineDataSets.add(inVolSec2DataSet)
                lineDataSets.add(outVolSec2DataSet)
                lineDataSets.add(inVolSec3DataSet)
            }
            val lineData = LineData(lineDataSets)
            data = lineData
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRateLoopsBinding.inflate(inflater, container, false)

}
