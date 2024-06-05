package com.just.machine.ui.fragment.onkeycalibration

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.common.base.CommonBaseFragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.just.machine.util.FixCountDownTime
import com.just.news.R
import com.just.news.databinding.FragmentOnekeyIngredientBinding
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random

/**
 * 一键定标-成分
 */
@AndroidEntryPoint
class OneKeyIngredientFragment : CommonBaseFragment<FragmentOnekeyIngredientBinding>() {

    private lateinit var mCountDownTime: FixCountDownTime
    private lateinit var tempDataSet: LineDataSet
    private lateinit var actualDataSet: LineDataSet

    override fun loadData() {
        mCountDownTime.start(object : FixCountDownTime.OnTimerCallBack{
            override fun onStart() {

            }

            override fun onTick(times: Int) {
                val index = 50 - times
                tempDataSet.addEntry(Entry(index.toFloat(),(Random().nextInt(30)).toFloat()))
                actualDataSet.addEntry(Entry(index.toFloat(),(Random().nextInt(30)).toFloat()))

                binding.chartIngredientOnekey.lineData.notifyDataChanged()
                binding.chartIngredientOnekey.notifyDataSetChanged()
                binding.chartIngredientOnekey.invalidate()
            }

            override fun onFinish() {

            }
        })
    }

    override fun initView() {
        mCountDownTime = object : FixCountDownTime(50, 1000) {}
        initLineChart()
    }

    override fun initListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentOnekeyIngredientBinding.inflate(inflater, container, false)

    private fun initLineChart() {
        binding.chartIngredientOnekey.apply {
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
            axisRight.isEnabled = true
            description.textSize = 9f
            description?.apply {
                text = ""
            }
            xAxis?.apply {
                textSize = 10f
                textColor = ContextCompat.getColor(requireContext(), R.color.text3)
                //X轴最大值和最小值
                axisMaximum = 50F
                axisMinimum = 0F
                offsetLeftAndRight(10)
                //X轴每个值的差值(缩放时可以体现出来)
                granularity = 2f
                //X轴的位置
                position = XAxis.XAxisPosition.BOTTOM
                //是否绘制X轴的网格线(垂直于X轴)
                setDrawGridLines(false)
                //X轴的颜色
                axisLineColor = Color.parseColor("#FFD8E5FA")
                //X轴的宽度
                axisLineWidth = 2f
                //设置X轴显示固定条目,放大缩小都显示这个数
                setLabelCount(30, false)
                //是否绘制X轴
                setDrawAxisLine(false)
                //X轴每个刻度对应的值(展示的值和设置的值不同)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return String.format("%.0f", value)
                    }
                }
            }
            axisLeft?.apply {
                textColor = ContextCompat.getColor(requireContext(), R.color.colorTextOrange)
                //左侧Y轴的最大值和最小值
                axisMaximum = 30f
                axisMinimum = 0f
                setLabelCount(21, true)
                //绘制网格线(样式虚线)
                enableGridDashedLine(1.5f, 2f, 0f)
                gridColor = ContextCompat.getColor(requireContext(), R.color.text3)
                setDrawGridLines(false)
                setDrawAxisLine(false) //绘制左边Y轴是否显示
                setDrawZeroLine(false) //是否开启0线
                isGranularityEnabled = true
            }

            axisRight?.apply {
                textColor = ContextCompat.getColor(requireContext(), R.color.green)
                //左侧Y轴的最大值和最小值
                axisMaximum = 30f
                axisMinimum = 0f
                setLabelCount(21, true)
                //绘制网格线(样式虚线)
                enableGridDashedLine(1.5f, 2f, 0f)
                gridColor = ContextCompat.getColor(requireContext(), R.color.text3)
                setDrawGridLines(false)
                setDrawAxisLine(false) //绘制左边Y轴是否显示
                setDrawZeroLine(false) //是否开启0线
                isGranularityEnabled = true
            }

            legend?.apply {
                setDrawInside(false)
                formSize = 0f
            }

            tempDataSet = LineDataSet(null, "")
            tempDataSet.lineWidth = 1.0f
            tempDataSet.color = ContextCompat.getColor(requireContext(), R.color.green)
            tempDataSet.setDrawValues(false)
            tempDataSet.setDrawCircles(false)
            tempDataSet.setDrawCircleHole(false)
            tempDataSet.setDrawFilled(false)
            tempDataSet.mode = LineDataSet.Mode.LINEAR

            actualDataSet = LineDataSet(null, "")
            actualDataSet.lineWidth = 1.0f
            actualDataSet.color = ContextCompat.getColor(requireContext(), R.color.colorTextOrange)
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