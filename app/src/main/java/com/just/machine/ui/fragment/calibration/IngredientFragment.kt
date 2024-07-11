package com.just.machine.ui.fragment.calibration

import android.graphics.Color
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.BaseUtil
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent.Companion.INGREDIENTS_SUCCESS
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.just.machine.dao.calibration.IngredientBean
import com.just.machine.ui.adapter.calibration.IngredientAdapter
import com.just.machine.ui.fragment.serial.ModbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentIngredientBinding
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

/**
 *create by 2024/6/1
 * 成分定标
 *@author zt
 */
@AndroidEntryPoint
class IngredientFragment : CommonBaseFragment<FragmentIngredientBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var tts: TextToSpeech

    private var standardOneGasO2Concentration = 0.0 //标准1气体氧气(O2)浓度
    private var standardOneGasCO2Concentration = 0.0 //标准1气体二氧化碳(CO2)浓度

    private var standardTwoGasO2Concentration = 0.0 //标准2气体氧气(O2)浓度
    private var standardTwoGasCO2Concentration = 0.0 //标准2气体二氧化碳(CO2)浓度

    private var measuredO2Concentration = 0.0 //氧气(O2)测量浓度
    private var measuredCO2Concentration = 0.0 //二氧化碳(CO2)测量浓度

    private var measuredO2DataSet: LineDataSet? = null //氧气(O2)实测流量曲线
    private var measuredCO2DataSet: LineDataSet? = null //二氧化碳(CO2)实测流量曲线

    private var o2IngredientCalibrationModelList = mutableListOf<IngredientBean>() //氧气(O2)成分定标结果列表
    private var co2IngredientCalibrationModelList = mutableListOf<IngredientBean>() //二氧化碳(CO2)成分定标结果列表

    private var kCO2 = 0.01 //二氧化碳kx+b的k
    private var bCO2 = 0.0 //二氧化碳kx+b的b
    private var kO2 = 0.0017 //氧气kx+b的k
    private var bO2 = -1 //氧气kx+b的k

    private val o2SensorList = ArrayList<ArrayList<Double>>() //氧气传感器值
    private val co2SensorList = ArrayList<ArrayList<Double>>() //二氧化碳传感器值

    private var listO2 = ArrayList<Double>()
    private var listCO2 = ArrayList<Double>()

    private var ingredientState = false //成分定标状态
    private var ingredientStateList = ArrayList<Boolean>() //成分定标状态列表

    private val o2Adapter by lazy {
        IngredientAdapter(requireContext())
    }

    private val co2Adapter by lazy {
        IngredientAdapter(requireContext())
    }

    override fun initView() {

        if (binding.swDepthToggle.isChecked) {
            binding.etOneO2.isEnabled = true
            binding.etOneCo2.isEnabled = true
            binding.etTwoCo2.isEnabled = true
            binding.etTwoCo2.isEnabled = true
            enableEditTextStyle()
        } else {
            binding.etOneO2.isEnabled = false
            binding.etOneCo2.isEnabled = false
            binding.etTwoCo2.isEnabled = false
            binding.etTwoCo2.isEnabled = false
            disEnableEditTextStyle()
        }

        binding.rvIngredient.layoutManager = LinearLayoutManager(requireContext())

        o2Adapter.setItemClickListener { _, position ->
            o2Adapter.toggleItemBackground(position)
        }

        binding.rvIngredient.adapter = o2Adapter
        binding.rvIngredient2.adapter = co2Adapter

        initLineChart()

        tts = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.CHINESE)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    println("语言数据丢失或不支持")
                } else {
                    binding.llStart.isEnabled = true
                }
            } else {
                println("初始化失败!")
            }
        }
    }

    override fun initListener() {
        binding.swDepthToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableEditTextStyle()
            } else {
                disEnableEditTextStyle()
            }
        }

        binding.llStart.setNoRepeatListener {
            tts.speak("开始成分定标", TextToSpeech.QUEUE_FLUSH, null, "")

            SerialPortManager.sendMessage(ModbusProtocol.FLOW_CALIBRATION_COMMAND)//发送流量定标
        }

        LiveDataBus.get().with("测试3").observe(this) {//解析串口消息
            if (it is ByteArray) {
                LogUtils.e(tag + BaseUtil.bytes2HexStr(it) + "字节长度" + BaseUtil.bytes2HexStr(it).length)
                val data = ModbusProtocol.parseControlBoardResponse(it)
                if (data != null) {
                    LogUtils.e(tag + data.toString())
                    val o2Bean = IngredientBean()
                    o2Bean.actual = data.o2SensorData.toString()
                    o2Bean.isO2 = true
                    val co2Bean = IngredientBean()
                    co2Bean.actual = data.co2SensorData.toString()
                    co2Bean.isO2 = false
                    viewModel.setIngredientBean(o2Bean)
                    viewModel.setIngredientBean(co2Bean)
                }
            }
        }

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                INGREDIENTS_SUCCESS -> {
                    val flowsO2Bean: MutableList<IngredientBean> = ArrayList()
                    val flowsCo2Bean: MutableList<IngredientBean> = ArrayList()
                    if (it.any !is List<*>) {
                        return@observe
                    }
                    val list = it.any as List<*>
                    for (index in list) {
                        if (index !is IngredientBean) {
                            return@observe
                        }
                        if (index.isO2 == true)
                            flowsO2Bean.add(index) else
                            flowsCo2Bean.add(index)
                    }

                    o2Adapter.setItemsBean(flowsO2Bean)
                    co2Adapter.setItemsBean(flowsCo2Bean)
                    LogUtils.d(tag + flowsO2Bean + flowsCo2Bean)
                }
            }
        }
    }

    private fun enableEditTextStyle() {
        binding.etOneO2.isEnabled = true
        binding.etOneCo2.isEnabled = true
        binding.etTwoCo2.isEnabled = true
        binding.etTwoCo2.isEnabled = true
        binding.etOneO2.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_white_solid)
        binding.etOneCo2.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_white_solid)
        binding.etTwoO2.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_white_solid)
        binding.etTwoCo2.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_white_solid)
    }

    private fun disEnableEditTextStyle() {
        binding.etOneO2.isEnabled = false
        binding.etOneCo2.isEnabled = false
        binding.etTwoCo2.isEnabled = false
        binding.etTwoCo2.isEnabled = false
        binding.etOneO2.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_gray_solid)
        binding.etOneCo2.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_gray_solid)
        binding.etTwoO2.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_gray_solid)
        binding.etTwoCo2.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_gray_solid)
    }

    /**
     * 懒加载
     */
    override fun loadData() {
        viewModel.getIngredients()
    }

    override fun onDestroy() {
        // 释放 TextToSpeech 资源
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentIngredientBinding.inflate(inflater, container, false)

    private fun initLineChart() {
        binding.chartIngredient.apply {
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
                axisMaximum = 60F
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
                setLabelCount(35, false)
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

            measuredO2DataSet = LineDataSet(null, "")
            measuredO2DataSet!!.lineWidth = 1.0f
            measuredO2DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.green)
            measuredO2DataSet!!.setDrawValues(false)
            measuredO2DataSet!!.setDrawCircles(false)
            measuredO2DataSet!!.setDrawCircleHole(false)
            measuredO2DataSet!!.setDrawFilled(false)
            measuredO2DataSet!!.mode = LineDataSet.Mode.LINEAR

            measuredCO2DataSet = LineDataSet(null, "")
            measuredCO2DataSet!!.lineWidth = 1.0f
            measuredCO2DataSet!!.color =
                ContextCompat.getColor(requireContext(), R.color.colorTextOrange)
            measuredCO2DataSet!!.setDrawValues(false)
            measuredCO2DataSet!!.setDrawCircles(false)
            measuredCO2DataSet!!.setDrawCircleHole(false)
            measuredCO2DataSet!!.setDrawFilled(false)
            measuredCO2DataSet!!.mode = LineDataSet.Mode.LINEAR

            val lineDataSets: MutableList<ILineDataSet> = ArrayList()
            lineDataSets.add(measuredO2DataSet!!)
            lineDataSets.add(measuredCO2DataSet!!)
            val lineData = LineData(lineDataSets)
            data = lineData
        }
    }

    private fun prepareIngredientCalibration(){
        o2IngredientCalibrationModelList.clear()
        co2IngredientCalibrationModelList.clear()
        o2SensorList.clear()
        co2SensorList.clear()
        ingredientStateList.clear()
        ingredientState = false

        measuredCO2DataSet!!.clear()
        measuredO2DataSet!!.clear()

        binding.chartIngredient.lineData.notifyDataChanged()
        binding.chartIngredient.notifyDataSetChanged()
        binding.chartIngredient.invalidate()

        listO2 = arrayListOf()
        listCO2 = arrayListOf()
    }
}