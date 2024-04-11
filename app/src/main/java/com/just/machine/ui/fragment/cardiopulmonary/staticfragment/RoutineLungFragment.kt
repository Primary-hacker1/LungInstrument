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

    enum class Click {
        TEST1, TEST2, TEST3, TEST4, TEST5
    }


    override fun initView() {
        val lineChart = binding.chartFvc

        // 创建折线图的样本数据
        val entries = arrayListOf<Entry>()
        for (index in 0..30) {
            entries.add(Entry(index.toFloat(), index.toFloat() / 6))
        }


        // 设置 Y 轴的最小值和最大值，以确保包含所有单数刻度
        lineChart.axisLeft.axisMinimum = -5f // 设置 Y 轴最小值
        lineChart.axisLeft.axisMaximum = 5f // 设置 Y 轴最大值

        val xAxis = lineChart.xAxis

        xAxis.granularity = 1f     //这个很重要

        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // 设置折线图的上下边距
//        lineChart.setExtraOffsets(10f, 50f, 10f, 10f) // 上、左、下、右边距

        // 设置标题
        lineChart.description.text = "负数折线图示例"
        lineChart.description.setPosition(15f, 15f)
        lineChart.description.textSize = 12f
        lineChart.description.textColor = Color.BLACK
//        lineChart.description.xOffset = 120f
//        lineChart.description.yOffset = 200f // 正表示向上偏移

// 设置 MarkerView
        val markerView = CustomMarkerView(requireContext(), R.layout.custom_marker_view)
        lineChart.marker = markerView

        // 设置 LineChart 可以拖动
        lineChart.isDragEnabled = true

        // 设置 LineChart 的缩放
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(true)

        // 创建 LineDataSet 对象并添加数据集
        val dataSet1 = LineDataSet(entries, "Data Set 1")

        dataSet1.color = Color.BLUE // 设置曲线颜色
        dataSet1.setCircleColor(Color.BLUE) // 设置曲线上的数据点颜色
        dataSet1.lineWidth = 2f // 设置曲线宽度
        dataSet1.circleRadius = 4f // 设置曲线上的数据点半径
        dataSet1.valueTextSize = 10f // 设置数据点值的字体大小
        dataSet1.valueTextColor = Color.BLUE // 设置数据点值的颜色
        dataSet1.setDrawValues(false) // 设置是否绘制数据点的值
        dataSet1.mode = LineDataSet.Mode.CUBIC_BEZIER // 设置曲线模式为三次贝塞尔曲线

        val lineDataSets = mutableListOf<ILineDataSet>()
        lineDataSets.add(dataSet1)

        val lineData = LineData(lineDataSets)
        lineChart.data = lineData

        // 监听 LineChart 的拖动事件
        lineChart.onChartGestureListener = object : OnChartGestureListener {
            override fun onChartGestureStart(
                me: MotionEvent?,
                lastPerformedGesture: ChartGesture?
            ) {
            }

            override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartGesture?) {
                // 获取当前拖动的范围
                val lowestVisibleX = lineChart.lowestVisibleX
                val highestVisibleX = lineChart.highestVisibleX

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
        lineChart.invalidate()


        binding.rvFvc.layoutManager = LinearLayoutManager(requireContext())

        adapter.setItemsBean(
            mutableListOf(
                RoutineLungBean(
                    "用力肺活量(ERV)", "20", "9", "1","111","11"
                ), RoutineLungBean(
                    "一秒量(ERV)", "20", "9", "1","111","11"
                ), RoutineLungBean(
                    "一秒率(ERV)", "20", "9", "1","111","11"
                ), RoutineLungBean(
                    "用力呼气峰流速(ERV)", "20", "9", "1","111","11"
                ), RoutineLungBean(
                    "25%时呼气流逝(ERV)", "20", "9", "1","111","11"
                ), RoutineLungBean(
                    "50%时呼气流逝(ERV)", "20", "9", "1","111","11"
                )
            )
        )



        binding.rvFvc.adapter = adapter
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
            onCLickbutton = Click.TEST1
            setButtonStyle(
                binding.btnTest1,
                binding.btnTest2,
                binding.btnTest3,
                binding.btnTest4,
                binding.btnTest5
            )
        }

        binding.btnTest2.setNoRepeatListener {
            onCLickbutton = Click.TEST2
            setButtonStyle(
                binding.btnTest2,
                binding.btnTest1,
                binding.btnTest3,
                binding.btnTest4,
                binding.btnTest5
            )
        }

        binding.btnTest3.setNoRepeatListener {
            onCLickbutton = Click.TEST3
            setButtonStyle(
                binding.btnTest3,
                binding.btnTest2,
                binding.btnTest1,
                binding.btnTest4,
                binding.btnTest5
            )
        }

        binding.btnTest4.setNoRepeatListener {
            onCLickbutton = Click.TEST4
            setButtonStyle(
                binding.btnTest4,
                binding.btnTest3,
                binding.btnTest2,
                binding.btnTest1,
                binding.btnTest5
            )
        }

        binding.btnTest5.setNoRepeatListener {
            onCLickbutton = Click.TEST5
            setButtonStyle(
                binding.btnTest5,
                binding.btnTest4,
                binding.btnTest3,
                binding.btnTest2,
                binding.btnTest1
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


    private fun setButtonStyle(
        button1: Button,
        button2: Button,
        button3: Button,
        button4: Button,
        button5: Button,
    ) {// 设置按钮的样式

        button1.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        button1.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.circle_click)

        button2.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        button2.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.circle)

        button3.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        button3.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.circle)

        button4.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        button4.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.circle)

        button5.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        button5.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.circle)
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLungBinding.inflate(inflater, container, false)

}
