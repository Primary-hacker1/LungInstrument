package com.just.machine.ui.fragment.onekeycalibration

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.just.machine.dao.calibration.IngredientBean
import com.just.machine.ui.adapter.calibration.IngredientAdapter
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
    private lateinit var actualO2DataSet: LineDataSet
    private lateinit var actualCO2DataSet: LineDataSet

    private val o2Adapter by lazy {
        IngredientAdapter(requireContext())
    }

    private val co2Adapter by lazy {
        IngredientAdapter(requireContext())
    }

    override fun loadData() {

    }

    override fun initView() {
        binding.rvIngredientO2.layoutManager = LinearLayoutManager(requireContext())
        binding.rvIngredientCo2.layoutManager = LinearLayoutManager(requireContext())

        binding.rvIngredientO2.adapter = o2Adapter
        binding.rvIngredientCo2.adapter = co2Adapter

        mCountDownTime = object : FixCountDownTime(50, 1000) {}
        initLineChart()

        o2Adapter.setItemsBean(mutableListOf(IngredientBean(0,"",0,"3.00","1.2","0.5","0.8","3.3","5.5","1.0")))
        co2Adapter.setItemsBean(mutableListOf(IngredientBean(0,"",0,"3.00","1.2","0.5","0.8","3.3","5.5","1.0")))
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
                textSize = 8f
                isGranularityEnabled = true
                //左侧Y轴的最大值和最小值
                axisMaximum = 30f
                axisMinimum = 0f
                granularity = 1.5f
                setLabelCount(((axisMaximum - axisMinimum) / granularity + 1).toInt(), true)
                gridColor = ContextCompat.getColor(requireContext(), R.color.text3)
                setDrawGridLines(false)
                setDrawAxisLine(false) //绘制左边Y轴是否显示
                setDrawZeroLine(false) //是否开启0线
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return String.format("%.1f", value)
                    }
                }
            }

            axisRight?.apply {
                textColor = ContextCompat.getColor(requireContext(), R.color.green)
                textSize = 8f
                isGranularityEnabled = true
                //左侧Y轴的最大值和最小值
                axisMaximum = 30f
                axisMinimum = 0f
                granularity = 1.5f
                setLabelCount(((axisMaximum - axisMinimum) / granularity + 1).toInt(), true)
                gridColor = ContextCompat.getColor(requireContext(), R.color.text3)
                setDrawGridLines(false)
                setDrawAxisLine(false) //绘制左边Y轴是否显示
                setDrawZeroLine(false) //是否开启0线
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return String.format("%.1f", value)
                    }
                }
            }

            legend?.apply {
                setDrawInside(false)
                formSize = 0f
            }

            actualO2DataSet = LineDataSet(null, "")
            actualO2DataSet.lineWidth = 1.0f
            actualO2DataSet.color = ContextCompat.getColor(requireContext(), R.color.green)
            actualO2DataSet.setDrawValues(false)
            actualO2DataSet.setDrawCircles(false)
            actualO2DataSet.setDrawCircleHole(false)
            actualO2DataSet.setDrawFilled(false)
            actualO2DataSet.mode = LineDataSet.Mode.LINEAR

            actualCO2DataSet = LineDataSet(null, "")
            actualCO2DataSet.lineWidth = 1.0f
            actualCO2DataSet.color = ContextCompat.getColor(requireContext(), R.color.colorTextOrange)
            actualCO2DataSet.setDrawValues(false)
            actualCO2DataSet.setDrawCircles(false)
            actualCO2DataSet.setDrawCircleHole(false)
            actualCO2DataSet.setDrawFilled(false)
            actualCO2DataSet.mode = LineDataSet.Mode.LINEAR

            val lineDataSets: MutableList<ILineDataSet> = ArrayList()
            lineDataSets.add(actualO2DataSet)
            lineDataSets.add(actualCO2DataSet)
            val lineData = LineData(lineDataSets)
            data = lineData
        }
    }

    override fun onResume() {
        mCountDownTime.start(object : FixCountDownTime.OnTimerCallBack{
            override fun onStart() {

            }

            override fun onTick(times: Int) {
                binding.tvOnekeyActualO2.text = "O2(%): ${Random().nextInt(30).toFloat()}"
                binding.tvOnekeyActualCo2.text = "CO2(%): ${Random().nextInt(30).toFloat()}"
                val index = 50 - times
                actualO2DataSet.addEntry(Entry(index.toFloat(),Random().nextInt(30).toFloat()))
                actualCO2DataSet.addEntry(Entry(index.toFloat(),Random().nextInt(30).toFloat()))

                binding.chartIngredientOnekey.lineData.notifyDataChanged()
                binding.chartIngredientOnekey.notifyDataSetChanged()
                binding.chartIngredientOnekey.invalidate()
            }

            override fun onFinish() {

            }
        })
        super.onResume()
    }
}