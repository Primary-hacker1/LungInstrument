package com.just.machine.ui.fragment.calibration

import android.R.string
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.viewmodel.LiveDataEvent
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.model.lungdata.SttlungDataModel
import com.just.machine.ui.adapter.calibration.FlowAdapter
import com.just.machine.ui.dialog.LoadingDialogFragment
import com.just.machine.ui.dialog.LungCommonDialogFragment
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.FixCountDownTime
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentFlowHandleBinding
import com.justsafe.libview.util.DateUtils
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.fixedRateTimer


/**
 *create by 2024/6/19
 * 手动流量定标
 *@author zt
 */
@AndroidEntryPoint
class FlowHandleFragment : CommonBaseFragment<FragmentFlowHandleBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private var isPull = true
    private lateinit var mDownTime: FixCountDownTime
    private lateinit var timer: Timer
    private lateinit var startLoadingDialogFragment: LoadingDialogFragment

    private val strVol = arrayOf(
        "吸气容积1",
        "呼气容积1",
        "吸气容积2",
        "呼气容积2",
        "吸气容积3",
        "呼气容积3",
        "吸气容积4",
        "呼气容积4",
        "吸气容积5",
        "呼气容积5"
    )

    // 容量-时间
    private var inVolSec1DataSet: LineDataSet? = null
    private var outVolSec1DataSet: LineDataSet? = null

    private var inVolSec2DataSet: LineDataSet? = null
    private var outVolSec2DataSet: LineDataSet? = null

    private var inVolSec3DataSet: LineDataSet? = null
    private var outVolSec3DataSet: LineDataSet? = null

    private var inVolSec4DataSet: LineDataSet? = null
    private var outVolSec4DataSet: LineDataSet? = null

    private var inVolSec5DataSet: LineDataSet? = null
    private var outVolSec5DataSet: LineDataSet? = null

    // 流速-容量
    private var inFlowVol1DataSet: LineDataSet? = null
    private var outFlowVol1DataSet: LineDataSet? = null

    private var inFlowVol2DataSet: LineDataSet? = null
    private var outFlowVol2DataSet: LineDataSet? = null

    private var inFlowVol3DataSet: LineDataSet? = null
    private var outFlowVol3DataSet: LineDataSet? = null

    private var inFlowVol4DataSet: LineDataSet? = null
    private var outFlowVol4DataSet: LineDataSet? = null

    private var inFlowVol5DataSet: LineDataSet? = null
    private var outFlowVol5DataSet: LineDataSet? = null

    private var isStart = false
    private var calibrateCount = 1 //定标计数器
    private var iFlag = 0
    private var time = 0
    private var isStop = false
    private var isZero = false
    private var ftemplow = 0
    private var ftemphigh = 0
    private var ftemp = 0;
    private var iscer = false
    private var startsec = 0
    private var autoIndex = 0
    private var startSec = 0f
    private var startVol = 0f
    private var startFlow = 0f
    private var tempvol = 0f

    private var inHaleFlowList = mutableListOf<FlowBean>()
    private var exHaleFlowList = mutableListOf<FlowBean>()

    private val inHaleFlowAdapter by lazy {
        FlowAdapter(requireContext())
    }

    private val exHaleFlowAdapter by lazy {
        FlowAdapter(requireContext())
    }


    override fun loadData() {
        viewModel.getFlows()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView() {
        mDownTime = object : FixCountDownTime(20, 1000) {}
        binding.rvFlowHandleInhale.layoutManager = LinearLayoutManager(requireContext())

        binding.rvFlowHandleInhale.adapter = inHaleFlowAdapter

        binding.rvFlowHandleExhale.layoutManager = LinearLayoutManager(requireContext())

        binding.rvFlowHandleExhale.adapter = exHaleFlowAdapter

        initLineChart(binding.chartFlowHandleCapacityTime, 1)
        initLineChart(binding.chartFlowHandleFlowCapacity, 2)
    }

    override fun initListener() {
        inHaleFlowAdapter.setItemClickListener { _, position ->
            inHaleFlowAdapter.toggleItemBackground(position)
        }

//        inHaleFlowAdapter.setItemsBean(
//            mutableListOf(FlowBean(0, "", 1, "吸气容积1", "3", "3.003", "0.92"))
//        )

        exHaleFlowAdapter.setItemClickListener { _, position ->
            exHaleFlowAdapter.toggleItemBackground(position)
        }

//        exHaleFlowAdapter.setItemsBean(
//            mutableListOf(FlowBean(0, "", 1, "呼气容积1", "3", "2.993", "-1.44"))
//        )

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
        binding.llPullDirection.setNoRepeatListener {
            if (!isStart) {
                Toast.makeText(requireContext(), "定标未开始!", Toast.LENGTH_SHORT).show()
                return@setNoRepeatListener
            }
            if (isPull) {
                mDownTime.start(object : FixCountDownTime.OnTimerCallBack {
                    override fun onStart() {

                    }

                    override fun onTick(times: Int) {
                        when (calibrateCount) {
                            1 -> {
                                inVolSec1DataSet!!.addEntry(
                                    Entry(
                                        startSec,
                                        startVol
                                    )
                                )
                                inFlowVol1DataSet!!.addEntry(
                                    Entry(
                                        startVol,
                                        startFlow
                                    )
                                )
                                startSec += 0.05f
                                startVol += 0.1f
                                startFlow += 0.2f
                                tempvol += 0.1f
                            }

                            3 -> {
                                inVolSec2DataSet!!.addEntry(
                                    Entry(
                                        startSec,
                                        startVol
                                    )
                                )
                                inFlowVol2DataSet!!.addEntry(
                                    Entry(
                                        startVol,
                                        startFlow
                                    )
                                )
                                startSec += 0.05f
                                startVol += 0.2f
                                startFlow += 0.3f
                            }

                            5 -> {
                                inVolSec3DataSet!!.addEntry(
                                    Entry(
                                        startSec,
                                        startVol
                                    )
                                )
                                inFlowVol3DataSet!!.addEntry(
                                    Entry(
                                        startVol,
                                        startFlow
                                    )
                                )
                                startSec += 0.05f
                                startVol += 0.3f
                                startFlow += 0.4f
                            }

                            7 -> {
                                inVolSec4DataSet!!.addEntry(
                                    Entry(
                                        startSec,
                                        startVol
                                    )
                                )
                                inFlowVol4DataSet!!.addEntry(
                                    Entry(
                                        startVol,
                                        startFlow
                                    )
                                )
                                startSec += 0.05f
                                startVol += 0.4f
                                startFlow += 0.5f
                            }

                            9 -> {
                                inVolSec5DataSet!!.addEntry(
                                    Entry(
                                        startSec,
                                        startVol
                                    )
                                )
                                inFlowVol5DataSet!!.addEntry(
                                    Entry(
                                        startVol,
                                        startFlow
                                    )
                                )
                                startSec += 0.05f
                                startVol += 0.5f
                                startFlow += 0.6f
                            }
                        }

                        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
                        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
                        binding.chartFlowHandleCapacityTime.invalidate()

                        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
                        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
                        binding.chartFlowHandleFlowCapacity.invalidate()
                    }

                    override fun onFinish() {
                        if (tempvol > 2.0) {
                            resetParmet(1)
                        } else {
                            inHaleFlowList.add(
                                FlowBean(
                                    0,
                                    DateUtils.nowTimeString,
                                    1,
                                    strVol[calibrateCount - 1],
                                    "3",
                                    startVol.toString(),
                                    if (calibrateCount == 3) "-1.05" else "0.92"
                                )
                            )
                            inHaleFlowAdapter.setItemsBean(
                                inHaleFlowList
                            )
                            mDownTime.setmTimes(20)

                            calibrateCount++
                            startSec = 0f
                            startVol = 0f
                            startFlow = 0f
                            binding.tvPullDirection.text = "推"
                            binding.tvPullDirection.setBackgroundResource(R.drawable.flow_down)
                            isPull = !isPull
                        }
                    }
                })
            } else {
                mDownTime.start(object : FixCountDownTime.OnTimerCallBack {
                    override fun onStart() {

                    }

                    override fun onTick(times: Int) {
                        when (calibrateCount) {
                            2 -> {
                                outVolSec1DataSet!!.addEntry(
                                    Entry(
                                        startSec,
                                        -startVol
                                    )
                                )
                                outFlowVol1DataSet!!.addEntry(
                                    Entry(
                                        startVol,
                                        -startFlow
                                    )
                                )
                                startSec += 0.05f
                                startVol += 0.1f
                                startFlow += 0.2f
                            }

                            4 -> {
                                outVolSec2DataSet!!.addEntry(
                                    Entry(
                                        startSec,
                                        -startVol
                                    )
                                )
                                outFlowVol2DataSet!!.addEntry(
                                    Entry(
                                        startVol,
                                        -startFlow
                                    )
                                )
                                startSec += 0.05f
                                startVol += 0.2f
                                startFlow += 0.3f
                            }

                            6 -> {
                                outVolSec3DataSet!!.addEntry(
                                    Entry(
                                        startSec,
                                        -startVol
                                    )
                                )
                                outFlowVol3DataSet!!.addEntry(
                                    Entry(
                                        startVol,
                                        -startFlow
                                    )
                                )
                                startSec += 0.05f
                                startVol += 0.3f
                                startFlow += 0.4f
                            }

                            8 -> {
                                outVolSec4DataSet!!.addEntry(
                                    Entry(
                                        startSec,
                                        -startVol
                                    )
                                )

                                outFlowVol4DataSet!!.addEntry(
                                    Entry(
                                        startVol,
                                        -startFlow
                                    )
                                )
                                startSec += 0.05f
                                startVol += 0.4f
                                startFlow += 0.5f
                            }

                            10 -> {
                                outVolSec5DataSet!!.addEntry(
                                    Entry(
                                        startSec,
                                        -startVol
                                    )
                                )
                                outFlowVol5DataSet!!.addEntry(
                                    Entry(
                                        startVol,
                                        -startFlow
                                    )
                                )
                                startSec += 0.05f
                                startVol += 0.5f
                                startFlow += 0.6f
                            }
                        }
                        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
                        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
                        binding.chartFlowHandleCapacityTime.invalidate()

                        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
                        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
                        binding.chartFlowHandleFlowCapacity.invalidate()
                    }

                    override fun onFinish() {
                        exHaleFlowList.add(
                            FlowBean(
                                0,
                                DateUtils.nowTimeString,
                                1,
                                strVol[calibrateCount - 1],
                                "3",
                                startVol.toString(),
                                if (calibrateCount == 8) "-0.95" else "1.02"
                            )
                        )
                        exHaleFlowAdapter.setItemsBean(
                            exHaleFlowList
                        )
                        mDownTime.setmTimes(20)

                        calibrateCount++
                        startSec = 0f
                        startVol = 0f
                        startFlow = 0f
                        binding.tvPullDirection.text = "拉"
                        binding.tvPullDirection.setBackgroundResource(R.drawable.flow_pull)
                        isPull = !isPull
                    }
                })
            }
        }
        //定标开始
        LiveDataBus.get().with("clickFlowStart").observe(this) {
            if (it is String) {
                if (it == "handleFlow") {
                    //开始手动定标
                    stopPortSend()
                    lifecycleScope.launch(Dispatchers.Main) {
                        delay(100)
                        isZeroSuccess()
                    }
                    lifecycleScope.launch(Dispatchers.Main) {
                        delay(100)
                        isZero = false
                        if (isZero) {
                            isStart = true
                            sendCalibraCommand()
                            LiveDataBus.get().with("flowStart").value = "handleFlow"
                            iFlag = 2
                            isStop = false
                            binding.tvPullDirection.text = "拉"
                            binding.tvPullDirection.setBackgroundResource(R.drawable.flow_pull)
                        }else{
                            LungCommonDialogFragment.startCommonDialogFragment(
                                requireActivity().supportFragmentManager, "定标失败!", "2"
                            )
                        }
                    }
                }
            }
        }
        //定标结束
        LiveDataBus.get().with("flowStop").observe(this) {
            if (it is String) {
                isStart = false
                stopPortSend()
                if (it == "handleFlow") {

                }
            }
        }

        //串口数据
        LiveDataBus.get().with("GetDeviceInfo").observe(this) {

        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentFlowHandleBinding.inflate(inflater, container, false)

    @RequiresApi(Build.VERSION_CODES.N)
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

                inVolSec1DataSet = LineDataSet(null, "")
                inVolSec1DataSet!!.lineWidth = 1.0f
                inVolSec1DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.green)
                inVolSec1DataSet!!.setDrawValues(false)
                inVolSec1DataSet!!.setDrawCircles(false)
                inVolSec1DataSet!!.setDrawCircleHole(false)
                inVolSec1DataSet!!.setDrawFilled(false)
                inVolSec1DataSet!!.mode = LineDataSet.Mode.LINEAR

                outVolSec1DataSet = LineDataSet(null, "")
                outVolSec1DataSet!!.lineWidth = 1.0f
                outVolSec1DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.green)
                outVolSec1DataSet!!.setDrawValues(false)
                outVolSec1DataSet!!.setDrawCircles(false)
                outVolSec1DataSet!!.setDrawCircleHole(false)
                outVolSec1DataSet!!.setDrawFilled(false)
                outVolSec1DataSet!!.mode = LineDataSet.Mode.LINEAR

                inVolSec2DataSet = LineDataSet(null, "")
                inVolSec2DataSet!!.lineWidth = 1.0f
                inVolSec2DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.blue)
                inVolSec2DataSet!!.setDrawValues(false)
                inVolSec2DataSet!!.setDrawCircles(false)
                inVolSec2DataSet!!.setDrawCircleHole(false)
                inVolSec2DataSet!!.setDrawFilled(false)
                inVolSec2DataSet!!.mode = LineDataSet.Mode.LINEAR

                outVolSec2DataSet = LineDataSet(null, "")
                outVolSec2DataSet!!.lineWidth = 1.0f
                outVolSec2DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.blue)
                outVolSec2DataSet!!.setDrawValues(false)
                outVolSec2DataSet!!.setDrawCircles(false)
                outVolSec2DataSet!!.setDrawCircleHole(false)
                outVolSec2DataSet!!.setDrawFilled(false)
                outVolSec2DataSet!!.mode = LineDataSet.Mode.LINEAR

                inVolSec3DataSet = LineDataSet(null, "")
                inVolSec3DataSet!!.lineWidth = 1.0f
                inVolSec3DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.brown)
                inVolSec3DataSet!!.setDrawValues(false)
                inVolSec3DataSet!!.setDrawCircles(false)
                inVolSec3DataSet!!.setDrawCircleHole(false)
                inVolSec3DataSet!!.setDrawFilled(false)
                inVolSec3DataSet!!.mode = LineDataSet.Mode.LINEAR

                outVolSec3DataSet = LineDataSet(null, "")
                outVolSec3DataSet!!.lineWidth = 1.0f
                outVolSec3DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.brown)
                outVolSec3DataSet!!.setDrawValues(false)
                outVolSec3DataSet!!.setDrawCircles(false)
                outVolSec3DataSet!!.setDrawCircleHole(false)
                outVolSec3DataSet!!.setDrawFilled(false)
                outVolSec3DataSet!!.mode = LineDataSet.Mode.LINEAR

                inVolSec4DataSet = LineDataSet(null, "")
                inVolSec4DataSet!!.lineWidth = 1.0f
                inVolSec4DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.olive)
                inVolSec4DataSet!!.setDrawValues(false)
                inVolSec4DataSet!!.setDrawCircles(false)
                inVolSec4DataSet!!.setDrawCircleHole(false)
                inVolSec4DataSet!!.setDrawFilled(false)
                inVolSec4DataSet!!.mode = LineDataSet.Mode.LINEAR

                outVolSec4DataSet = LineDataSet(null, "")
                outVolSec4DataSet!!.lineWidth = 1.0f
                outVolSec4DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.olive)
                outVolSec4DataSet!!.setDrawValues(false)
                outVolSec4DataSet!!.setDrawCircles(false)
                outVolSec4DataSet!!.setDrawCircleHole(false)
                outVolSec4DataSet!!.setDrawFilled(false)
                outVolSec4DataSet!!.mode = LineDataSet.Mode.LINEAR

                inVolSec5DataSet = LineDataSet(null, "")
                inVolSec5DataSet!!.lineWidth = 1.0f
                inVolSec5DataSet!!.color =
                    ContextCompat.getColor(requireContext(), R.color.blueViolet)
                inVolSec5DataSet!!.setDrawValues(false)
                inVolSec5DataSet!!.setDrawCircles(false)
                inVolSec5DataSet!!.setDrawCircleHole(false)
                inVolSec5DataSet!!.setDrawFilled(false)
                inVolSec5DataSet!!.mode = LineDataSet.Mode.LINEAR

                outVolSec5DataSet = LineDataSet(null, "")
                outVolSec5DataSet!!.lineWidth = 1.0f
                outVolSec5DataSet!!.color =
                    ContextCompat.getColor(requireContext(), R.color.blueViolet)
                outVolSec5DataSet!!.setDrawValues(false)
                outVolSec5DataSet!!.setDrawCircles(false)
                outVolSec5DataSet!!.setDrawCircleHole(false)
                outVolSec5DataSet!!.setDrawFilled(false)
                outVolSec5DataSet!!.mode = LineDataSet.Mode.LINEAR

                lineDataSets.add(inVolSec1DataSet!!)
                lineDataSets.add(outVolSec1DataSet!!)
                lineDataSets.add(inVolSec2DataSet!!)
                lineDataSets.add(outVolSec2DataSet!!)
                lineDataSets.add(inVolSec3DataSet!!)
                lineDataSets.add(outVolSec3DataSet!!)
                lineDataSets.add(inVolSec4DataSet!!)
                lineDataSets.add(outVolSec4DataSet!!)
                lineDataSets.add(inVolSec5DataSet!!)
                lineDataSets.add(outVolSec5DataSet!!)
            } else {
                inFlowVol1DataSet = LineDataSet(null, "")
                inFlowVol1DataSet!!.lineWidth = 1.0f
                inFlowVol1DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.green)
                inFlowVol1DataSet!!.setDrawValues(false)
                inFlowVol1DataSet!!.setDrawCircles(false)
                inFlowVol1DataSet!!.setDrawCircleHole(false)
                inFlowVol1DataSet!!.setDrawFilled(false)
                inFlowVol1DataSet!!.mode = LineDataSet.Mode.LINEAR

                outFlowVol1DataSet = LineDataSet(null, "")
                outFlowVol1DataSet!!.lineWidth = 1.0f
                outFlowVol1DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.green)
                outFlowVol1DataSet!!.setDrawValues(false)
                outFlowVol1DataSet!!.setDrawCircles(false)
                outFlowVol1DataSet!!.setDrawCircleHole(false)
                outFlowVol1DataSet!!.setDrawFilled(false)
                outFlowVol1DataSet!!.mode = LineDataSet.Mode.LINEAR

                inFlowVol2DataSet = LineDataSet(null, "")
                inFlowVol2DataSet!!.lineWidth = 1.0f
                inFlowVol2DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.blue)
                inFlowVol2DataSet!!.setDrawValues(false)
                inFlowVol2DataSet!!.setDrawCircles(false)
                inFlowVol2DataSet!!.setDrawCircleHole(false)
                inFlowVol2DataSet!!.setDrawFilled(false)
                inFlowVol2DataSet!!.mode = LineDataSet.Mode.LINEAR

                outFlowVol2DataSet = LineDataSet(null, "")
                outFlowVol2DataSet!!.lineWidth = 1.0f
                outFlowVol2DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.blue)
                outFlowVol2DataSet!!.setDrawValues(false)
                outFlowVol2DataSet!!.setDrawCircles(false)
                outFlowVol2DataSet!!.setDrawCircleHole(false)
                outFlowVol2DataSet!!.setDrawFilled(false)
                outFlowVol2DataSet!!.mode = LineDataSet.Mode.LINEAR

                inFlowVol3DataSet = LineDataSet(null, "")
                inFlowVol3DataSet!!.lineWidth = 1.0f
                inFlowVol3DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.brown)
                inFlowVol3DataSet!!.setDrawValues(false)
                inFlowVol3DataSet!!.setDrawCircles(false)
                inFlowVol3DataSet!!.setDrawCircleHole(false)
                inFlowVol3DataSet!!.setDrawFilled(false)
                inFlowVol3DataSet!!.mode = LineDataSet.Mode.LINEAR

                outFlowVol3DataSet = LineDataSet(null, "")
                outFlowVol3DataSet!!.lineWidth = 1.0f
                outFlowVol3DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.brown)
                outFlowVol3DataSet!!.setDrawValues(false)
                outFlowVol3DataSet!!.setDrawCircles(false)
                outFlowVol3DataSet!!.setDrawCircleHole(false)
                outFlowVol3DataSet!!.setDrawFilled(false)
                outFlowVol3DataSet!!.mode = LineDataSet.Mode.LINEAR

                inFlowVol4DataSet = LineDataSet(null, "")
                inFlowVol4DataSet!!.lineWidth = 1.0f
                inFlowVol4DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.olive)
                inFlowVol4DataSet!!.setDrawValues(false)
                inFlowVol4DataSet!!.setDrawCircles(false)
                inFlowVol4DataSet!!.setDrawCircleHole(false)
                inFlowVol4DataSet!!.setDrawFilled(false)
                inFlowVol4DataSet!!.mode = LineDataSet.Mode.LINEAR

                outFlowVol4DataSet = LineDataSet(null, "")
                outFlowVol4DataSet!!.lineWidth = 1.0f
                outFlowVol4DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.olive)
                outFlowVol4DataSet!!.setDrawValues(false)
                outFlowVol4DataSet!!.setDrawCircles(false)
                outFlowVol4DataSet!!.setDrawCircleHole(false)
                outFlowVol4DataSet!!.setDrawFilled(false)
                outFlowVol4DataSet!!.mode = LineDataSet.Mode.LINEAR

                inFlowVol5DataSet = LineDataSet(null, "")
                inFlowVol5DataSet!!.lineWidth = 1.0f
                inFlowVol5DataSet!!.color =
                    ContextCompat.getColor(requireContext(), R.color.blueViolet)
                inFlowVol5DataSet!!.setDrawValues(false)
                inFlowVol5DataSet!!.setDrawCircles(false)
                inFlowVol5DataSet!!.setDrawCircleHole(false)
                inFlowVol5DataSet!!.setDrawFilled(false)
                inFlowVol5DataSet!!.mode = LineDataSet.Mode.LINEAR

                outFlowVol5DataSet = LineDataSet(null, "")
                outFlowVol5DataSet!!.lineWidth = 1.0f
                outFlowVol5DataSet!!.color =
                    ContextCompat.getColor(requireContext(), R.color.blueViolet)
                outFlowVol5DataSet!!.setDrawValues(false)
                outFlowVol5DataSet!!.setDrawCircles(false)
                outFlowVol5DataSet!!.setDrawCircleHole(false)
                outFlowVol5DataSet!!.setDrawFilled(false)
                outFlowVol5DataSet!!.mode = LineDataSet.Mode.LINEAR

                lineDataSets.add(inFlowVol1DataSet!!)
                lineDataSets.add(outFlowVol1DataSet!!)
                lineDataSets.add(inFlowVol2DataSet!!)
                lineDataSets.add(outFlowVol2DataSet!!)
                lineDataSets.add(inFlowVol3DataSet!!)
                lineDataSets.add(outFlowVol3DataSet!!)
                lineDataSets.add(inFlowVol4DataSet!!)
                lineDataSets.add(outFlowVol4DataSet!!)
                lineDataSets.add(inFlowVol5DataSet!!)
                lineDataSets.add(outFlowVol5DataSet!!)
            }
            val lineData = LineData(lineDataSets)
            data = lineData
        }
    }

    private fun isZeroSuccess() {
        //此处添加零位校验代码
        iFlag = 1
        isZero = false
        try {
            isStop = false
            SerialPortManager.sendMessage(MudbusProtocol.FLOW_CALIBRATION_COMMAND)
            timer = fixedRateTimer("", false, 0, 1000) {
                if (iFlag == 1) {
                    time++
                    if (isZero) {
                        time = 0
                        isStop = true
                        startLoadingDialogFragment.dismiss()
                        timer.cancel()
                    } else if (time == 8) {
                        startLoadingDialogFragment.dismiss()
                        timer.cancel()
                        LungCommonDialogFragment.startCommonDialogFragment(
                            requireActivity().supportFragmentManager, "校验超时!", "2"
                        )
                    }
                }
            }
            startLoadingDialogFragment = LoadingDialogFragment.startLoadingDialogFragment(
                requireActivity().supportFragmentManager, "正在校零..."
            )
        } catch (ex: Exception) {
            Toast.makeText(requireContext(), ex.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopPortSend() {
        try {
            SerialPortManager.sendMessage(MudbusProtocol.FLOW_STOP_COMMAND)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendCalibraCommand() {
        try {
            SerialPortManager.sendMessage(MudbusProtocol.FLOW_CALIBRATION_COMMAND)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun resetParmet(type: Int) {
        if (type == 1) {
            when (calibrateCount) {
                1 -> {
                    inVolSec1DataSet!!.clear()
                    inFlowVol1DataSet!!.clear()
                }

                2 -> {
                    inVolSec2DataSet!!.clear()
                    inFlowVol2DataSet!!.clear()
                }

                3 -> {
                    inVolSec3DataSet!!.clear()
                    inFlowVol3DataSet!!.clear()
                }

                4 -> {
                    inVolSec4DataSet!!.clear()
                    inFlowVol4DataSet!!.clear()
                }

                5 -> {
                    inVolSec5DataSet!!.clear()
                    inFlowVol5DataSet!!.clear()
                }
            }

            binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
            binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
            binding.chartFlowHandleCapacityTime.invalidate()

            binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
            binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
            binding.chartFlowHandleFlowCapacity.invalidate()

            startSec = 0f
            startVol = 0f
            startFlow = 0f
            mDownTime.setmTimes(20)
            isPull = true
            binding.tvPullDirection.text = "拉"
            binding.tvPullDirection.setBackgroundResource(R.drawable.flow_pull)
        }else{
            when (calibrateCount) {
                6 -> {
                    outVolSec1DataSet!!.clear()
                    outFlowVol1DataSet!!.clear()
                }

                7 -> {
                    outVolSec2DataSet!!.clear()
                    outFlowVol2DataSet!!.clear()
                }

                8 -> {
                    outVolSec3DataSet!!.clear()
                    outFlowVol3DataSet!!.clear()
                }

                9 -> {
                    outVolSec4DataSet!!.clear()
                    outFlowVol4DataSet!!.clear()
                }

                10 -> {
                    outVolSec5DataSet!!.clear()
                    outFlowVol5DataSet!!.clear()
                }
            }

            binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
            binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
            binding.chartFlowHandleCapacityTime.invalidate()

            binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
            binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
            binding.chartFlowHandleFlowCapacity.invalidate()

            startSec = 0f
            startVol = 0f
            startFlow = 0f
            mDownTime.setmTimes(20)
            isPull = true
            binding.tvPullDirection.text = "推"
            binding.tvPullDirection.setBackgroundResource(R.drawable.flow_down)
        }
    }
}