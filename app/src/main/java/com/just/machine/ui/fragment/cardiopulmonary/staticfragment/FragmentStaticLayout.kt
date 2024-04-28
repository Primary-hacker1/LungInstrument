package com.just.machine.ui.fragment.cardiopulmonary.staticfragment

import CustomMarkerView
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.just.machine.model.staticlung.RoutineLungBean
import com.just.machine.ui.adapter.RoutineLungAdapter
import com.just.news.R
import com.just.news.databinding.FragmentLungBinding
import lecho.lib.hellocharts.model.PointValue


class FragmentStaticLayout : FrameLayout {

    private val tag = FragmentStaticLayout::class.java.name

    var binding: FragmentLungBinding

    private var mContext: Context

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


    private var test1 = listOf("value1", "value2", "value3", "value4", "value5", "value6")

    private var test2 = listOf("kotlin1", "kotlin2", "kotlin3", "kotlin4", "kotlin5", "kotlin6")

    private var test3 = listOf("num1", "num2", "num3", "num4", "num5", "num6")

    private var test4 = listOf("value1", "value2", "value3", "value4", "num5", "num6")

    private var test5 = listOf("num", "num2", "num3", "num4", "num5", "num6")


    private var onCLickbutton: Click? = null

    private val adapter by lazy { RoutineLungAdapter(context) }

    private var routineLungList: MutableList<RoutineLungBean>? = ArrayList()

    enum class Click {
        TEST1, TEST2, TEST3, TEST4, TEST5
    }

    fun setInitView(title: String) {
        binding.title.text = title
    }

    fun setData(test1: MutableList<String>) {
        this.test1 = test1
    }

    fun initView() {

        initData()

        initListener()

        val values: MutableList<PointValue> = ArrayList()// 模拟数据

        for (i in 0..99) {
            values.add(PointValue(i.toFloat(), Math.random().toFloat() * 100))
        }

        binding.previewChart.setData(values)

        binding.previewChartFlow.setData(values)

        binding.previewChartFVC.setData(values)

        // 获取初始视口数据
        val initialViewport = binding.previewChart.currentViewport

        // 初始时应用视口数据到 previewChartFlow
        binding.previewChartFlow.maximumViewport = initialViewport
        binding.previewChartFlow.currentViewport = initialViewport

        // 设置视口变化监听器，调整视口宽度
        binding.previewChart.setViewportChangeListener {
            // 将previewChart的视口数据应用到previewChartFlow的坐标轴数据中
            binding.previewChartFlow.maximumViewport = it
            binding.previewChartFlow.currentViewport = it

        }

    }

    @Deprecated("暂时不用，用lineChartView这个不支持区域数据动态监听！")
    private fun initChartLow() {

        // 创建折线图的样本数据
        val entries = arrayListOf<Entry>()
        for (index in 0..30) {
            entries.add(Entry(index.toFloat(), index.toFloat() / 6 - 3))
        }


        val lineChartFvc = binding.chartFvc

        // 设置 MarkerView
        val markerView = CustomMarkerView(context, R.layout.custom_marker_view)
        lineChartFvc.marker = markerView


        // 监听 LineChart 的拖动事件
        lineChartFvc.onChartGestureListener = object : OnChartGestureListener {
            override fun onChartGestureStart(
                me: MotionEvent?,
                lastPerformedGesture: ChartTouchListener.ChartGesture?
            ) {
            }

            override fun onChartGestureEnd(
                me: MotionEvent?,
                lastPerformedGesture: ChartTouchListener.ChartGesture?
            ) {
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

        binding.rvFvc.layoutManager = LinearLayoutManager(context)

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

    // 用于保存按钮样式的映射
    private var button: Button? = null

    fun initListener() {
        // 设置选中值监听器
        binding.chartFvc.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                // 当用户选择值时触发该方法
                if (e == null) return

                // 获取选中的值
                val xValue = e.x
                val yValue = e.y

                LogUtils.d(tag + "Selected value: x = $xValue, y = $yValue")
            }

            override fun onNothingSelected() {
                // 当没有值被选中时触发该方法
            }
        })

        binding.btnTest1.setNoRepeatListener {

            handleButtonClick(
                Click.TEST1,
                "测试1",
                test1
            )
        }

        binding.btnTest2.setNoRepeatListener {
            handleButtonClick(
                Click.TEST2,
                "测试2",
                test2,
            )
        }

        binding.btnTest3.setNoRepeatListener {
            handleButtonClick(
                Click.TEST3,
                "测试3",
                test3,
            )
        }

        binding.btnTest4.setNoRepeatListener {
            handleButtonClick(
                Click.TEST4,
                "测试4",
                test4,
            )
        }

        binding.btnTest5.setNoRepeatListener {
            handleButtonClick(
                Click.TEST5,
                "测试5",
                test5,
            )
        }

        binding.btnStart.setNoRepeatListener {
            when (onCLickbutton) {
                Click.TEST1 -> {
                    setButtonStyle(binding.btnTest1, isActive = false, isStart = true)
                    setButtonStyle(binding.btnTest2, false)
                    setButtonStyle(binding.btnTest3, false)
                    setButtonStyle(binding.btnTest4, false)
                    setButtonStyle(binding.btnTest5, false)
                }

                Click.TEST2 -> {
                    setButtonStyle(binding.btnTest2, isActive = false, isStart = true)
                    setButtonStyle(binding.btnTest1, false)
                    setButtonStyle(binding.btnTest3, false)
                    setButtonStyle(binding.btnTest4, false)
                    setButtonStyle(binding.btnTest5, false)
                }

                Click.TEST3 -> {
                    setButtonStyle(binding.btnTest3, isActive = false, isStart = true)
                    setButtonStyle(binding.btnTest1, false)
                    setButtonStyle(binding.btnTest2, false)
                    setButtonStyle(binding.btnTest4, false)
                    setButtonStyle(binding.btnTest5, false)
                }

                Click.TEST4 -> {
                    setButtonStyle(binding.btnTest4, isActive = false, isStart = true)
                    setButtonStyle(binding.btnTest1, false)
                    setButtonStyle(binding.btnTest2, false)
                    setButtonStyle(binding.btnTest3, false)
                    setButtonStyle(binding.btnTest5, false)
                }

                Click.TEST5 -> {
                    setButtonStyle(binding.btnTest5, isActive = false, isStart = true)
                    setButtonStyle(binding.btnTest1, false)
                    setButtonStyle(binding.btnTest2, false)
                    setButtonStyle(binding.btnTest3, false)
                    setButtonStyle(binding.btnTest4, false)
                }

                null -> TODO()
            }

        }

    }

    private fun handleButtonClick(
        clickType: Click,
        buttonText: String,
        otherList: List<String>,
//        isStart: Boolean
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
                val isStart1 = button == binding.btnTest1
                LogUtils.e(tag + isStart1)
                if (!isStart1) {
                    setButtonStyle(binding.btnTest1, true)
                }

                val isStart2 = button == binding.btnTest2

                if (!isStart2) {
                    setButtonStyle(binding.btnTest2, false)
                }

                val isStart3 = button == binding.btnTest3

                if (!isStart3) {
                    setButtonStyle(binding.btnTest3, false)
                }

                val isStart4 = button == binding.btnTest4

                if (!isStart4) {
                    setButtonStyle(binding.btnTest4, false)
                }

                val isStart5 = button == binding.btnTest5

                if (!isStart5) {
                    setButtonStyle(binding.btnTest5, false)
                }
            }

            Click.TEST2 -> {
                val isStart1 = button == binding.btnTest1
                LogUtils.e(tag + isStart1)
                if (!isStart1) {
                    setButtonStyle(binding.btnTest1, false)
                }

                val isStart2 = button == binding.btnTest2

                if (!isStart2) {
                    setButtonStyle(binding.btnTest2, true)
                }

                val isStart3 = button == binding.btnTest3

                if (!isStart3) {
                    setButtonStyle(binding.btnTest3, false)
                }

                val isStart4 = button == binding.btnTest4

                if (!isStart4) {
                    setButtonStyle(binding.btnTest4, false)
                }

                val isStart5 = button == binding.btnTest5

                if (!isStart5) {
                    setButtonStyle(binding.btnTest5, false)
                }
            }

            Click.TEST3 -> {
                val isStart1 = button == binding.btnTest1
                LogUtils.e(tag + isStart1)
                if (!isStart1) {
                    setButtonStyle(binding.btnTest1, false)
                }

                val isStart2 = button == binding.btnTest2

                if (!isStart2) {
                    setButtonStyle(binding.btnTest2, false)
                }

                val isStart3 = button == binding.btnTest3

                if (!isStart3) {
                    setButtonStyle(binding.btnTest3, true)
                }

                val isStart4 = button == binding.btnTest4

                if (!isStart4) {
                    setButtonStyle(binding.btnTest4, false)
                }

                val isStart5 = button == binding.btnTest5

                if (!isStart5) {
                    setButtonStyle(binding.btnTest5, false)
                }
            }

            Click.TEST4 -> {
                val isStart1 = button == binding.btnTest1
                LogUtils.e(tag + isStart1)
                if (!isStart1) {
                    setButtonStyle(binding.btnTest1, false)
                }

                val isStart2 = button == binding.btnTest2

                if (!isStart2) {
                    setButtonStyle(binding.btnTest2, false)
                }

                val isStart3 = button == binding.btnTest3

                if (!isStart3) {
                    setButtonStyle(binding.btnTest3, false)
                }

                val isStart4 = button == binding.btnTest4

                if (!isStart4) {
                    setButtonStyle(binding.btnTest4, true)
                }

                val isStart5 = button == binding.btnTest5

                if (!isStart5) {
                    setButtonStyle(binding.btnTest5, false)
                }
            }

            Click.TEST5 -> {
                val isStart1 = button == binding.btnTest1
                LogUtils.e(tag + isStart1)
                if (!isStart1) {
                    setButtonStyle(binding.btnTest1, false)
                }

                val isStart2 = button == binding.btnTest2

                if (!isStart2) {
                    setButtonStyle(binding.btnTest2, false)
                }

                val isStart3 = button == binding.btnTest3

                if (!isStart3) {
                    setButtonStyle(binding.btnTest3, false)
                }

                val isStart4 = button == binding.btnTest4

                if (!isStart4) {
                    setButtonStyle(binding.btnTest4, false)
                }

                val isStart5 = button == binding.btnTest5

                if (!isStart5) {
                    setButtonStyle(binding.btnTest5, true)
                }
            }
            // 继续添加其他点击类型的处理...

        }
    }

    private fun setButtonStyle(button: Button, isActive: Boolean, isStart: Boolean? = false) {
        if (isStart == true) {
            this.button = button
            button.setTextColor(ContextCompat.getColor(context, R.color.white))
            button.background = ContextCompat.getDrawable(context, R.drawable.circle_start)
            return
        }

        if (isActive) {
            button.setTextColor(ContextCompat.getColor(context, R.color.white))
            button.background = ContextCompat.getDrawable(context, R.drawable.circle_click)
        } else {
            button.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            button.background = ContextCompat.getDrawable(context, R.drawable.circle)
        }
    }

    fun getLayout(): Int {
        return R.layout.fragment_lung
    }
}
