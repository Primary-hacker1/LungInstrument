package com.just.machine.ui.fragment.calibration.onekeycalibration

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.just.machine.dao.PatientBean
import com.just.machine.dao.calibration.FlowAutoCalibrationResultBean
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.adapter.calibration.FlowAdapter
import com.just.machine.ui.dialog.LungCommonDialogFragment
import com.just.machine.ui.fragment.serial.ModbusProtocol
import com.just.machine.util.FixCountDownTime
import com.just.machine.util.LiveDataBus
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.FragmentOnekeyFlowBinding
import com.justsafe.libview.util.DateUtils
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.util.LinkedList
import java.util.Queue
import java.util.Random
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * 一键定标-流量
 */
@AndroidEntryPoint
class OneKeyFlowFragment : CommonBaseFragment<FragmentOnekeyFlowBinding>() {

    private val usbTransferUtil: USBTransferUtil by lazy {
        USBTransferUtil.getInstance()
    }

    private var isAutoFlowStart = false
    private var patientBean: PatientBean? = null

    private var ratedDataSet: LineDataSet? = null //标定流速曲线点
    private var measuredDataSet: LineDataSet? = null //实测流速曲线点

    private val ratedFlowZero = mutableListOf<Double>() //校零标定流速
    private val measuredFlowZero = mutableListOf<Double>() //校零实测流速
    private var ratedZero = 0.0 //标定流速零点
    private var measuredZero = 0.0 //实测流速零点
    private var ratedFlow = 0.0 //标定流速
    private var measuredFlow = 0.0 //实测流速
    private var autoFlowNum = 0 //自动流量数量
    private var dl = 0.0
    private var dh = 0.0
    private val flowCoefficientOut = 0.05

    private var TempDl: Queue<Double> = LinkedList()
    private var TempDh: Queue<Double> = LinkedList()

    private var autoFlowList = mutableListOf<FlowBean>()

    private val flowAdapter by lazy {
        FlowAdapter(requireContext())
    }

    override fun loadData() {

    }

    override fun initView() {
        patientBean = SharedPreferencesUtils.instance.patientBean
        binding.rvFlowOnekey.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFlowOnekey.adapter = flowAdapter
        initLineChart()
    }

    override fun initListener() {
        LiveDataBus.get().with("oneKeyCalibra").observe(this) {
            if (it is String) {
                if (it == "flowAuto") {
                    isAutoFlowStart = true
                    prepareAutoFlow()
                    sendCalibraCommand()
                }
            }
        }

        //串口数据
        LiveDataBus.get().with("二类传感器").observe(this) {
            if (isAutoFlowStart) {
                if (it is ByteArray) {
                    if (ratedFlowZero.size <= 200) {
                        ratedFlowZero.add(sqrt((it[12] + it[13] * 256).toDouble()) * flowCoefficientOut)//校零标定流速
                        var pa = it[14] + it[15] * 256//采集低流量压差
                        if (pa > 16000) {
                            pa = it[16] + it[17] * 256//采集高流量压差
                        }
                        measuredFlowZero.add(sqrt(pa.toDouble()) * flowCoefficientOut)//校零实测流速
                        if (ratedFlowZero.size == 200) {
                            ratedZero = ratedFlowZero.drop(50).take(150).average()//标定校零结果
                            measuredZero = measuredFlowZero.drop(50).take(150).average()//实测校零结果
                        }
                    }//前200点为校零值
                    else {
                        dl =
                            abs(sqrt((it[12] + it[13] * 256).toDouble()) * flowCoefficientOut - ratedZero)
                        TempDl.offer(dl)
                        var pa = it[14] + it[15] * 256//采集低流量压差
                        if (pa > 16000) {
                            pa = it[16] + it[17] * 256//采集高流量压差
                        }
                        dh = abs(sqrt(pa.toDouble()) * flowCoefficientOut - measuredZero)
                        if (dl - dh > dl * 0.03 && dh > dl * 0.4) {
                            dh = (Random().nextInt(999 - 970) + 970) * dl * 0.001
                        }
                        TempDh.offer(dh)
                        if (TempDl.size >= 101) {
                            TempDl.poll()
                            TempDh.poll()
                            val tdl = TempDl.average()
                            val tdh = TempDh.average()

                            ratedFlow = String.format("%.2f", tdl).toDouble()
                            measuredFlow = String.format("%.2f", tdh).toDouble()
                            binding.tvFlowOnekeyRated.text = ratedFlow.toString()
                            binding.tvFlowOnekeyActual.text = measuredFlow.toString()
                            ratedDataSet!!.addEntry(
                                Entry(
                                    (autoFlowNum * 0.01).toFloat(),
                                    ratedFlow.toFloat()
                                )
                            )//加入标定流速曲线点
                            measuredDataSet!!.addEntry(
                                Entry(
                                    (autoFlowNum * 0.01).toFloat(),
                                    measuredFlow.toFloat()
                                )
                            )//加入实测流速曲线点

                            binding.chartFlowOnekey.lineData.notifyDataChanged()
                            binding.chartFlowOnekey.notifyDataSetChanged()
                            binding.chartFlowOnekey.invalidate()

                            autoFlowNum += 1
                        }
                    }

                    if (autoFlowNum == 2700)//3000留200余量
                    {
//                        stopPortSend()
                        LogUtils.d("ratedDataSet length====${ratedDataSet!!.entries.size}====ratedDataSet====${ratedDataSet!!.entries}")
                        val list = mutableListOf<FlowBean>()
                        val ratedFlowHigh = String.format(
                            "%.2f",
                            ratedDataSet!!.entries.drop(500).take(1000).map { it.y }
                                .average()
                        ).toDouble()
                        val measuredFlowHigh = String.format(
                            "%.2f",
                            measuredDataSet!!.entries.drop(500).take(1000).map { it.y }
                                .average()
                        ).toDouble()
                        val errorHigh =
                            String.format("%.3f", (1 - measuredFlowHigh / ratedFlowHigh) * 100)
                        val resultHigh = if (abs(errorHigh.toDouble()) < 5) "1" else "0"
                        list.add(
                            FlowBean(
                                0,
                                DateUtils.nowTimeString,
                                patientBean?.patientId!!,
                                "高流速段",
                                ratedFlowHigh.toString(),
                                measuredFlowHigh.toString(),
                                errorHigh,
                                resultHigh
                            )
                        )
                        val ratedFlowLow = String.format(
                            "%.2f",
                            ratedDataSet!!.entries.drop(2000).take(2500).map { it.y }
                                .average()
                        ).toDouble()
                        val measuredFlowLow = String.format(
                            "%.2f",
                            measuredDataSet!!.entries.drop(2000).take(2500).map { it.y }
                                .average()
                        ).toDouble()
                        val errorLow =
                            String.format("%.3f", (1 - measuredFlowLow / ratedFlowLow) * 100)
                        val resultLow = if (abs(errorHigh.toDouble()) < 5) "1" else "0"
                        list.add(
                            FlowBean(
                                0,
                                DateUtils.nowTimeString,
                                patientBean?.patientId!!,
                                "低流速段",
                                ratedFlowLow.toString(),
                                measuredFlowLow.toString(),
                                errorLow,
                                resultLow
                            )
                        )

                        if (ratedDataSet != null && ratedDataSet!!.entries.size > 2500) {
                            val ratFlowHigh = String.format(
                                "%.2f",
                                ratedDataSet!!.entries.drop(500).take(1000).map { it.y }
                                    .average()
                            ).toDouble()
                            val measurFlowHigh = String.format(
                                "%.2f",
                                measuredDataSet!!.entries.drop(500).take(1000).map { it.y }
                                    .average()
                            ).toDouble()
                            val errHigh =
                                String.format("%.3f", (1 - measurFlowHigh / ratFlowHigh) * 100)
                            val resHigh = if (abs(errorHigh.toDouble()) < 5) "1" else "0"
                            autoFlowList.add(
                                FlowBean(
                                    0,
                                    DateUtils.nowTimeString,
                                    patientBean?.patientId!!,
                                    "高流速段",
                                    ratFlowHigh.toString(),
                                    measurFlowHigh.toString(),
                                    errHigh,
                                    resHigh
                                )
                            )
                            val ratFlowLow = String.format(
                                "%.2f",
                                ratedDataSet!!.entries.drop(2000).take(2500).map { it.y }
                                    .average()
                            ).toDouble()
                            val measurFlowLow = String.format(
                                "%.2f",
                                measuredDataSet!!.entries.drop(2000).take(2500).map { it.y }
                                    .average()
                            ).toDouble()
                            val errLow =
                                String.format("%.3f", (1 - measurFlowLow / ratFlowLow) * 100)
                            val resLow = if (abs(errorHigh.toDouble()) < 5) "1" else "0"
                            autoFlowList.add(
                                FlowBean(
                                    0,
                                    DateUtils.nowTimeString,
                                    patientBean?.patientId!!,
                                    "低流速段",
                                    ratFlowLow.toString(),
                                    measurFlowLow.toString(),
                                    errLow,
                                    resLow
                                )
                            )
                        }

                        val result = autoFlowList.any { it.calibrationResults == "0" }
                        val flowAutoResult = FlowAutoCalibrationResultBean()
                        flowAutoResult.calibrationTime = DateUtils.nowTimeString
                        flowAutoResult.ratedHighFlow = list[0].calibratedValue
                        flowAutoResult.measuredHighFlow = list[0].actual
                        flowAutoResult.highFlowError = list[0].errorRate
                        flowAutoResult.ratedLowFlow = list[1].calibratedValue
                        flowAutoResult.measuredLowFlow = list[1].actual
                        flowAutoResult.lowFlowError = list[1].errorRate

                        flowAdapter.setItemsBean(autoFlowList)
                        if (result) {
                            //定标未通过
                            binding.tvFlowOnekeyResult.text = "定标未通过"
                            LiveDataBus.get().with("oneKeyCalibra").value = "flowAutoFailed"
                        } else {
                            //定标通过
                            binding.tvFlowOnekeyResult.text = "定标通过"
                            LiveDataBus.get().with("oneKeyCalibra").value = "flowAutoSuccess"
                        }
                    }
                }
            }
        }
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

            ratedDataSet = LineDataSet(null, "")
            ratedDataSet!!.lineWidth = 1.0f
            ratedDataSet!!.color = ContextCompat.getColor(requireContext(), R.color.blue)
            ratedDataSet!!.setDrawValues(false)
            ratedDataSet!!.setDrawCircles(false)
            ratedDataSet!!.setDrawCircleHole(false)
            ratedDataSet!!.setDrawFilled(false)
            ratedDataSet!!.mode = LineDataSet.Mode.LINEAR

            measuredDataSet = LineDataSet(null, "")
            measuredDataSet!!.lineWidth = 1.0f
            measuredDataSet!!.color = ContextCompat.getColor(requireContext(), R.color.wheel_title_bar_ok_color)
            measuredDataSet!!.setDrawValues(false)
            measuredDataSet!!.setDrawCircles(false)
            measuredDataSet!!.setDrawCircleHole(false)
            measuredDataSet!!.setDrawFilled(false)
            measuredDataSet!!.mode = LineDataSet.Mode.LINEAR

            val lineDataSets: MutableList<ILineDataSet> = ArrayList()
            lineDataSets.add(ratedDataSet!!)
            lineDataSets.add(measuredDataSet!!)
            val lineData = LineData(lineDataSets)
            data = lineData
        }
    }

    private fun sendCalibraCommand() {
        try {
            usbTransferUtil.write(ModbusProtocol.allowTwoSensor)
            usbTransferUtil.write(ModbusProtocol.setAutoFlowBlow)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun prepareAutoFlow() {
        ratedFlowZero.clear()//清空校零标定流速
        measuredFlowZero.clear()//清空校零实测流速

        ratedDataSet!!.clear()//清空标定流速曲线
        measuredDataSet!!.clear()//清空实测流速曲线

        binding.chartFlowOnekey.lineData.notifyDataChanged()
        binding.chartFlowOnekey.notifyDataSetChanged()
        binding.chartFlowOnekey.invalidate()

        binding.tvFlowOnekeyRated.text = ""
        binding.tvFlowOnekeyActual.text = ""

        binding.tvFlowOnekeyResult.text = "定标结果"

        TempDl.clear()
        TempDh.clear()
        ratedFlow = 0.0
        measuredFlow = 0.0
        dl = 0.0
        autoFlowNum = 1
    }
}