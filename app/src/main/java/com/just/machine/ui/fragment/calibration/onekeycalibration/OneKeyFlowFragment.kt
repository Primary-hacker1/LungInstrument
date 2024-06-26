package com.just.machine.ui.fragment.calibration.onekeycalibration

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
import com.just.news.databinding.FragmentOnekeyFlowBinding
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random

/**
 * 一键定标-流量
 */
@AndroidEntryPoint
class OneKeyFlowFragment : CommonBaseFragment<FragmentOnekeyFlowBinding>() {

    private lateinit var mCountDownTime: FixCountDownTime
    private lateinit var tempDataSet: LineDataSet
    private lateinit var actualDataSet: LineDataSet

    override fun loadData() {

    }

    override fun initView() {
        mCountDownTime = object : FixCountDownTime(35, 1000) {}
        initLineChart()
    }

    override fun initListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnekeyFlowBinding.inflate(inflater, container, false)

    private fun initLineChart() {
        binding.chartFlowOnekey.apply {
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
                axisMaximum = 34.5F
                axisMinimum = 0F
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
                setLabelCount(30, false)
                //是否绘制X轴
                setDrawAxisLine(false)
                //X轴每个刻度对应的值(展示的值和设置的值不同)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return String.format("%.1f", value)
                    }
                }
            }
            axisLeft?.apply {
                textColor = ContextCompat.getColor(requireContext(), R.color.text3)
                //左侧Y轴的最大值和最小值
                axisMaximum = 3.8f
                axisMinimum = 0f
                setLabelCount(20, true)
                //绘制网格线(样式虚线)
                enableGridDashedLine(0.2f, 2f, 0f)
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
            tempDataSet.color = ContextCompat.getColor(requireContext(), R.color.blue)
            tempDataSet.setDrawValues(false)
            tempDataSet.setDrawCircles(false)
            tempDataSet.setDrawCircleHole(false)
            tempDataSet.setDrawFilled(false)
            tempDataSet.mode = LineDataSet.Mode.LINEAR

            actualDataSet = LineDataSet(null, "")
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

    override fun onResume() {
        mCountDownTime.start(object : FixCountDownTime.OnTimerCallBack{
            override fun onStart() {

            }

            override fun onTick(times: Int) {
                binding.tvFlowOnekeyTemp.text = (Random().nextInt(3)+1).toFloat().toString()
                binding.tvFlowOnekeyActual.text = (Random().nextInt(3)+1).toFloat().toString()

                val index = 35 - times
                tempDataSet.addEntry(Entry(index.toFloat(),(Random().nextInt(3)+1).toFloat()))
                actualDataSet.addEntry(Entry(index.toFloat(),(Random().nextInt(3)+1).toFloat()))

                binding.chartFlowOnekey.lineData.notifyDataChanged()
                binding.chartFlowOnekey.notifyDataSetChanged()
                binding.chartFlowOnekey.invalidate()
            }

            override fun onFinish() {

            }
        })
        super.onResume()
    }
}