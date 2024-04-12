package com.just.machine.ui.fragment.cardiopulmonary.staticfragment

import CustomMarkerView
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.base.toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.just.machine.model.staticlung.RoutineLungBean
import com.just.machine.ui.adapter.RoutineLungAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentLungBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt


/**
 *create by 2024/4/2
 * 静态肺的用力呼吸
 *@author zt
 */
@AndroidEntryPoint
class RoutineLungFragment : CommonBaseFragment<FragmentLungBinding>() {

    private var onCLickbutton: Click? = null

    private val viewModel by viewModels<MainViewModel>()

    private val adapter by lazy { RoutineLungAdapter(requireContext()) }

    override fun loadData() {//懒加载

    }

    private var routineLungList: MutableList<RoutineLungBean>? = ArrayList()

    enum class Click {
        TEST1, TEST2, TEST3, TEST4, TEST5
    }


    override fun initView() {

        initData()

        val entriesFow = arrayListOf<Entry>()
        // 创建折线图的样本数据
        val entriesFlowFvc = arrayListOf<Entry>()
        for (index in 0..6) {
            entriesFow.add(Entry(index.toFloat(), index.toFloat() / 6 - 3))
        }

        // 创建折线图的样本数据
        val entries = arrayListOf<Entry>()
        for (index in 0..30) {
            entries.add(Entry(index.toFloat(), index.toFloat() / 6 - 3))
        }

        lineChartFlow(binding.chartFvc, entries, -5f, 5f,11)

        lineChartFlow(binding.chartFlow, entriesFow, -12f, 9f,9)

        lineChartFlow(binding.chartFvcFlow, entriesFlowFvc, -12f, 9f,9)

        val lineChartFvc = binding.chartFvc

        // 设置 MarkerView
        val markerView = CustomMarkerView(requireContext(), R.layout.custom_marker_view)
        lineChartFvc.marker = markerView



        // 监听 LineChart 的拖动事件
        lineChartFvc.onChartGestureListener = object : OnChartGestureListener {
            override fun onChartGestureStart(
                me: MotionEvent?,
                lastPerformedGesture: ChartGesture?
            ) {
            }

            override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartGesture?) {
                // 获取当前拖动的范围
                val lowestVisibleX = lineChartFvc.lowestVisibleX
                val highestVisibleX = lineChartFvc.highestVisibleX

                // 计算拖动的秒数范围
                val secondsRange = highestVisibleX - lowestVisibleX

                // 如果拖动的范围大于等于 5 秒，则更新 MarkerView
                if (secondsRange >= 5f) {
                    // 设置 MarkerView 的内容
                    markerView.refreshContent(null, null)
                }
            }

            // 其他拖动事件回调
            override fun onChartLongPressed(me: MotionEvent?) {}
            override fun onChartDoubleTapped(me: MotionEvent?) {}
            override fun onChartSingleTapped(me: MotionEvent?) {}
            override fun onChartFling(
                me1: MotionEvent?,
                me2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ) {
            }

            override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {}
            override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {}
        }

        // 刷新图表
        lineChartFvc.invalidate()

        binding.rvFvc.layoutManager = LinearLayoutManager(requireContext())

        routineLungList?.let {
            adapter.setItemsBean(
                it
            )
        }

        binding.rvFvc.adapter = adapter
    }


    private fun initData() {
        routineLungList = mutableListOf(
            RoutineLungBean(
                "用力肺活量(ERV)", "20", "9", "1", "111", "11"
            ), RoutineLungBean(
                "一秒量(ERV)", "20", "9", "1", "111", "11"
            ), RoutineLungBean(
                "一秒率(ERV)", "20", "9", "1", "111", "11"
            ), RoutineLungBean(
                "用力呼气峰流速(ERV)", "20", "9", "1", "111", "11"
            ), RoutineLungBean(
                "25%时呼气流逝(ERV)", "20", "9", "1", "111", "11"
            ), RoutineLungBean(
                "50%时呼气流逝(ERV)", "20", "9", "1", "111", "11"
            )
        )
    }


    private fun lineChartFlow(
        lineChart: LineChart,
        entriesFow: ArrayList<Entry>,//xy数据
        fl: Float,//y轴最小值
        fl1: Float,//y轴最大值
        count: Int//y轴的个数
    ) {

        // 设置标题
        lineChart.description.text = "[L/S]"
        lineChart.description.setPosition(80f, 22f)
        lineChart.description.textSize = 9f
        lineChart.description.textColor = Color.BLACK

        // 设置 Y 轴的最小值和最大值，以确保包含所有单数刻度
        lineChart.axisLeft.axisMinimum = fl // 设置 Y 轴最小值
        lineChart.axisLeft.axisMaximum = fl1 // 设置 Y 轴最大值
        lineChart.axisRight.axisMinimum = fl // 设置 Y 轴最小值
        lineChart.axisRight.axisMaximum = fl1 // 设置 Y 轴最大值

        lineChart.setExtraOffsets(0f, 0f, 0f, 0f) // 上、左、下、右边距

        // 设置 LineChart 可以拖动
        lineChart.isDragEnabled = true

        // 设置 LineChart 的缩放
        lineChart.setScaleEnabled(false)

        lineChart.setPinchZoom(false)

        lineChart.axisLeft.setLabelCount(
            count, true
        ) // y轴刻度多少个
        lineChart.axisRight.setLabelCount(
            count, true
        ) // y轴刻度多少个
        // 创建 LineDataSet 对象并添加数据集
        val dataSet1 = LineDataSet(entriesFow, "Data Set 1")

        dataSet1.color = ContextCompat.getColor(requireContext(), R.color.colorPrimary) // 设置曲线颜色
        dataSet1.setCircleColor(Color.BLUE) // 设置曲线上的数据点颜色
        dataSet1.lineWidth = 2f // 设置曲线宽度
        dataSet1.circleRadius = 3f // 设置曲线上的数据点半径
        dataSet1.valueTextSize = 10f // 设置数据点值的字体大小
        dataSet1.valueTextColor = Color.BLUE // 设置数据点值的颜色
        dataSet1.setDrawValues(false) // 设置是否绘制数据点的值
        dataSet1.mode = LineDataSet.Mode.CUBIC_BEZIER // 设置曲线模式为三次贝塞尔曲线

        val xAxis = lineChart.xAxis

        xAxis.granularity = 1f     //这个很重要

        xAxis.position = XAxis.XAxisPosition.BOTTOM

        xAxis.setLabelCount(entriesFow.size, true)//x刻度多少个

        // 禁用X轴的刻度线
        xAxis.setDrawGridLines(false)

        val lineDataSets = mutableListOf<ILineDataSet>()
        lineDataSets.add(dataSet1)

        val lineData = LineData(lineDataSets)
        lineChart.data = lineData

        // 刷新图表
        lineChart.invalidate()
    }

    override fun initListener() {
        // 设置选中值监听器
        binding.chartFvc.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                // 当用户选择值时触发该方法
                if (e == null) return

                // 获取选中的值
                val xValue = e.x
                val yValue = e.y

                toast("Selected value: x = $xValue, y = $yValue")
            }

            override fun onNothingSelected() {
                // 当没有值被选中时触发该方法
            }
        })

        binding.btnTest1.setNoRepeatListener {
            handleButtonClick(
                Click.TEST1,
                "测试1",
                listOf("value1", "value2", "value3", "value4", "value5", "value6")
            )
        }

        binding.btnTest2.setNoRepeatListener {
            handleButtonClick(
                Click.TEST2,
                "测试2",
                listOf("kotlin1", "kotlin2", "kotlin3", "kotlin4", "kotlin5", "kotlin6")
            )
        }

        binding.btnTest3.setNoRepeatListener {
            handleButtonClick(
                Click.TEST3,
                "测试2",
                listOf("num1", "num2", "num3", "num4", "num5", "num6")
            )
        }

        binding.btnTest4.setNoRepeatListener {
            handleButtonClick(
                Click.TEST4,
                "测试2",
                listOf("num1", "num2", "num3", "num4", "num5", "num6")
            )
        }

        binding.btnTest5.setNoRepeatListener {
            handleButtonClick(
                Click.TEST5,
                "测试2",
                listOf("num1", "num2", "num3", "num4", "num5", "num6")
            )
        }

        binding.btnStart.setNoRepeatListener {
            when (onCLickbutton) {
                Click.TEST1 -> {

                }

                Click.TEST2 -> {

                }

                Click.TEST3 -> {

                }

                Click.TEST4 -> {

                }

                Click.TEST5 -> {

                }
            }

        }

    }

    private fun handleButtonClick(
        clickType: Click,
        buttonText: String,
        otherList: List<String>
    ) {
        onCLickbutton = clickType
        binding.atvTest.text = buttonText

        routineLungList?.forEachIndexed { index, bean ->
            if (index < otherList.size) {
                bean.test1 = otherList[index]
            }
        }

        routineLungList?.let {
            adapter.setItemsBean(it)
        }

        when (clickType) {
            Click.TEST1 -> {
                setButtonStyle(binding.btnTest1, true)
                setButtonStyle(binding.btnTest2, false)
                setButtonStyle(binding.btnTest3, false)
                setButtonStyle(binding.btnTest4, false)
                setButtonStyle(binding.btnTest5, false)
            }
            Click.TEST2 -> {
                setButtonStyle(binding.btnTest1, false)
                setButtonStyle(binding.btnTest2, true)
                setButtonStyle(binding.btnTest3, false)
                setButtonStyle(binding.btnTest4, false)
                setButtonStyle(binding.btnTest5, false)
            }
            Click.TEST3 -> {
                setButtonStyle(binding.btnTest1, false)
                setButtonStyle(binding.btnTest2, false)
                setButtonStyle(binding.btnTest3, true)
                setButtonStyle(binding.btnTest4, false)
                setButtonStyle(binding.btnTest5, false)
            }
            Click.TEST4 -> {
                setButtonStyle(binding.btnTest1, false)
                setButtonStyle(binding.btnTest2, false)
                setButtonStyle(binding.btnTest3, false)
                setButtonStyle(binding.btnTest4, true)
                setButtonStyle(binding.btnTest5, false)
            }
            Click.TEST5 -> {
                setButtonStyle(binding.btnTest1, false)
                setButtonStyle(binding.btnTest2, false)
                setButtonStyle(binding.btnTest3, false)
                setButtonStyle(binding.btnTest4, false)
                setButtonStyle(binding.btnTest5, true)
            }
            // 继续添加其他点击类型的处理...
        }
    }

    private fun setButtonStyle(button: Button, isActive: Boolean) {
        if (isActive) {
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            button.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_click)
        } else {
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            button.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle)
        }
    }




    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLungBinding.inflate(inflater, container, false)

}
