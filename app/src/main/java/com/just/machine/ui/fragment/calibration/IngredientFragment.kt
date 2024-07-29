package com.just.machine.ui.fragment.calibration

import android.graphics.Color
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.base.toast
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent.Companion.INGREDIENTS_SUCCESS
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.just.machine.dao.calibration.IngredientBean
import com.just.machine.dao.calibration.IngredientCalibrationResultBean
import com.just.machine.model.Constants
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.adapter.calibration.IngredientAdapter
import com.just.machine.ui.dialog.LungCommonDialogFragment
import com.just.machine.ui.fragment.serial.ModbusProtocol
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.CerlibraHelper
import com.just.machine.util.LiveDataBus
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.FragmentIngredientBinding
import com.justsafe.libview.util.DateUtils
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import kotlin.math.abs

/**
 *create by 2024/6/1
 * 成分定标
 *@author zt
 */
@AndroidEntryPoint
class IngredientFragment : CommonBaseFragment<FragmentIngredientBinding>() {

    private var usbTransferUtil: USBTransferUtil? = null //usb工具类
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var tts: TextToSpeech
    private var isIngredientStart = false

    private var standardOneGasO2Concentration = 0.0 //标准1气体氧气(O2)浓度
    private var standardOneGasCO2Concentration = 0.0 //标准1气体二氧化碳(CO2)浓度

    private var standardTwoGasO2Concentration = 0.0 //标准2气体氧气(O2)浓度
    private var standardTwoGasCO2Concentration = 0.0 //标准2气体二氧化碳(CO2)浓度

    private var measuredO2Concentration = 0.0 //氧气(O2)测量浓度
    private var measuredCO2Concentration = 0.0 //二氧化碳(CO2)测量浓度

    private var measuredO2DataSet: LineDataSet? = null //氧气(O2)实测流量曲线
    private var measuredCO2DataSet: LineDataSet? = null //二氧化碳(CO2)实测流量曲线

    private var o2IngredientCalibrationModelList = mutableListOf<IngredientBean>() //氧气(O2)成分定标结果列表
    private var co2IngredientCalibrationModelList =
        mutableListOf<IngredientBean>() //二氧化碳(CO2)成分定标结果列表

    private var kCO2 = 0.01 //二氧化碳kx+b的k
    private var bCO2 = 0.0 //二氧化碳kx+b的b
    private var kO2 = 0.0017 //氧气kx+b的k
    private var bO2 = -1.0 //氧气kx+b的k

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

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (s.hashCode() == binding.etOneO2.text.hashCode()) {
                standardOneGasO2Concentration = if (s?.isNotEmpty() == true) {
                    s.toString().toDouble()
                } else {
                    0.0
                }
                SharedPreferencesUtils.instance.ingredientCaliStanderOneO2 =
                    standardOneGasO2Concentration.toString()
            } else if (s.hashCode() == binding.etOneCo2.text.hashCode()) {
                standardOneGasCO2Concentration = if (s?.isNotEmpty() == true) {
                    s.toString().toDouble()
                } else {
                    0.0
                }
                SharedPreferencesUtils.instance.ingredientCaliStanderOneCO2 =
                    standardOneGasCO2Concentration.toString()
            } else if (s.hashCode() == binding.etTwoO2.text.hashCode()) {
                standardTwoGasO2Concentration = if (s?.isNotEmpty() == true) {
                    s.toString().toDouble()
                } else {
                    0.0
                }
                SharedPreferencesUtils.instance.ingredientCaliStanderTwoO2 =
                    standardTwoGasO2Concentration.toString()
            } else if (s.hashCode() == binding.etTwoCo2.text.hashCode()) {
                standardTwoGasCO2Concentration = if (s?.isNotEmpty() == true) {
                    s.toString().toDouble()
                } else {
                    0.0
                }
                SharedPreferencesUtils.instance.ingredientCaliStanderTwoCO2 =
                    standardTwoGasCO2Concentration.toString()
            }
        }
    }


    override fun initView() {
        usbTransferUtil = USBTransferUtil.getInstance()
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

        binding.rvIngredientO2.layoutManager = LinearLayoutManager(requireContext())
        binding.rvIngredientCo2.layoutManager = LinearLayoutManager(requireContext())

        o2Adapter.setItemClickListener { _, position ->
            o2Adapter.toggleItemBackground(position)
        }

        binding.rvIngredientO2.adapter = o2Adapter
        binding.rvIngredientCo2.adapter = co2Adapter

//        o2IngredientCalibrationModelList.add(IngredientBean(0,DateUtils.nowTimeString,0,"标气1","20.83","20.81","0.02","1","80","90"))
//        o2Adapter.setItemsBean(o2IngredientCalibrationModelList)

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
        initStandData()
    }

    private fun initStandData() {
        val ingredientCaliStanderOneO2 = SharedPreferencesUtils.instance.ingredientCaliStanderOneO2
        val ingredientCaliStanderOneCO2 =
            SharedPreferencesUtils.instance.ingredientCaliStanderOneCO2
        val ingredientCaliStanderTwoO2 = SharedPreferencesUtils.instance.ingredientCaliStanderTwoO2
        val ingredientCaliStanderTwoCO2 =
            SharedPreferencesUtils.instance.ingredientCaliStanderTwoCO2

        standardOneGasO2Concentration =
            if (ingredientCaliStanderOneO2?.isEmpty() == true) 21.0 else ingredientCaliStanderOneO2!!.toDouble()
        standardOneGasCO2Concentration =
            if (ingredientCaliStanderOneCO2?.isEmpty() == true) 0.0 else ingredientCaliStanderOneCO2!!.toDouble()
        standardTwoGasO2Concentration =
            if (ingredientCaliStanderTwoO2?.isEmpty() == true) 15.01 else ingredientCaliStanderTwoO2!!.toDouble()
        standardTwoGasCO2Concentration =
            if (ingredientCaliStanderTwoCO2?.isEmpty() == true) 6.02 else ingredientCaliStanderTwoCO2!!.toDouble()

        binding.etOneO2.setText(standardOneGasO2Concentration.toString())
        binding.etOneCo2.setText(standardOneGasCO2Concentration.toString())
        binding.etTwoO2.setText(standardTwoGasO2Concentration.toString())
        binding.etTwoCo2.setText(standardTwoGasCO2Concentration.toString())

        binding.etOneO2.addTextChangedListener(textWatcher)
        binding.etOneCo2.addTextChangedListener(textWatcher)
        binding.etTwoO2.addTextChangedListener(textWatcher)
        binding.etTwoCo2.addTextChangedListener(textWatcher)
    }

    override fun initListener() {
        binding.swDepthToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableEditTextStyle()
            } else {
                disEnableEditTextStyle()
                binding.etOneO2.clearFocus()
                binding.etOneCo2.clearFocus()
                binding.etTwoO2.clearFocus()
                binding.etTwoCo2.clearFocus()
            }
        }

        binding.llStart.setNoRepeatListener {
            tts.speak("开始成分定标", TextToSpeech.QUEUE_FLUSH, null, "")
            if (!ModbusProtocol.isDeviceConnect) {
                if (!isIngredientStart) {
                    prepareIngredientCalibration()
                    sendCalibraCommand()
                    binding.tvIngredientCalibrateStart.text = getString(R.string.stop)
                } else {
                    stopPortSend()
                    binding.tvIngredientCalibrateStart.text = getString(R.string.begin)
                }
            } else {
                toast(getString(R.string.device_without_connection_tips))
            }
        }

        //串口数据
        LiveDataBus.get().with(Constants.twoSensorSerialCallback).observe(this) {
            if (isIngredientStart) {
                if (it is ByteArray) {
                    if (it[10].toInt() == 0x02 && it[11].toInt() == 0x02) {
                        return@observe
                    }
                    var sensorO2 = (it[20] + it[21] * 256).toDouble()//氧气传感器的值
                    var sensorCO2 = (it[18] + it[19] * 256).toDouble()//二氧化碳传感器的值

                    if (it[10].toInt() == 0x01 && it[11].toInt() == 0x02 && ingredientState) {
                        ingredientState = false
                        ingredientStateList.add(ingredientState)
                    }//电磁阀1打开，接入空气且状态为标气进行切换操作
                    else if (it[10].toInt() == 0x02 && it[11].toInt() == 0x01 && ingredientState) {
                        ingredientState = true
                        ingredientStateList.add(ingredientState)
                    }//电磁阀2打开，接入标气且状态为空气，进行切换操作

                    if (ingredientStateList.size == 4 && o2SensorList[4].size == 0)//只计算一次
                    {
                        kO2 = CerlibraHelper.calculateKValue(
                            standardOneGasO2Concentration,
                            standardTwoGasO2Concentration,
                            o2SensorList[2].drop(100).average(),
                            o2SensorList[3].drop(100).average()
                        )
                        bO2 = CerlibraHelper.calculateBValue(
                            standardOneGasO2Concentration, kO2, o2SensorList[3].drop(100).average()
                        )
                        kCO2 = CerlibraHelper.calculateKValue(
                            standardOneGasCO2Concentration,
                            standardTwoGasCO2Concentration,
                            co2SensorList[3].drop(100).average(),
                            co2SensorList[2].drop(100).average()
                        )
                        bCO2 = CerlibraHelper.calculateBValue(
                            standardOneGasCO2Concentration,
                            kCO2,
                            co2SensorList[2].drop(100).average()
                        )

                    }//第一段计算

                    o2SensorList[ingredientStateList.size].add(sensorO2)
                    o2SensorList[ingredientStateList.size].add(sensorCO2)
                    sensorO2 = kO2 * sensorO2 + bO2
                    sensorCO2 = kCO2 * sensorCO2 + bCO2
                    if (sensorO2 <= 0) sensorO2 = 0.00
                    if (sensorCO2 <= 0) sensorCO2 = 0.00

                    measuredO2Concentration =
                        String.format("%.2f", CerlibraHelper.setO2Value(sensorO2)).toDouble()
                    binding.tvActualO2.text = "$measuredO2Concentration%"
                    measuredCO2Concentration =
                        String.format("%.2f", CerlibraHelper.setCo2Value(sensorCO2)).toDouble()
                    binding.tvActualCo2.text = "$measuredCO2Concentration%"

                    listO2.add(measuredO2Concentration)
                    listCO2.add(measuredCO2Concentration)

                    val num = measuredO2DataSet!!.entries.size
                    if (listO2.size - 10 > num) {
                        for (i in 0 until 10) {
                            measuredO2DataSet!!.addEntry(
                                Entry(
                                    ((num + i) * 0.01).toFloat(),
                                    listO2[num + i].toFloat()
                                )
                            )
                            measuredCO2DataSet!!.addEntry(
                                Entry(
                                    ((num + i) * 0.01).toFloat(),
                                    listCO2[num + i].toFloat()
                                )
                            )
                        }
                    }

                    if (o2SensorList[11].size > 360) {
                        LogUtils.d("-------------------成分定标--------------------" + it[20] + it[21])
                        calculateIngredient()
                    }
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
                        if (index.isO2 == true) flowsO2Bean.add(index) else flowsCo2Bean.add(index)
                    }

//                    o2Adapter.setItemsBean(flowsO2Bean)
//                    co2Adapter.setItemsBean(flowsCo2Bean)
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

    private fun prepareIngredientCalibration() {
        o2IngredientCalibrationModelList.clear()
        co2IngredientCalibrationModelList.clear()
        o2Adapter.setItemsBean(o2IngredientCalibrationModelList)
        co2Adapter.setItemsBean(co2IngredientCalibrationModelList)
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

    private fun stopPortSend() {
        try {
            usbTransferUtil!!.write(ModbusProtocol.banTwoSensor)
            isIngredientStart = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendCalibraCommand() {
        try {
            usbTransferUtil?.write(ModbusProtocol.allowTwoSensor)
            usbTransferUtil?.write(ModbusProtocol.setIngredientSolenoidValve)
            isIngredientStart = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun calculateIngredient() {
        var o2offset = 0.0
        var co2offset = 0.0
        var o2t90 = 0.0
        var co2t90 = 0.0
        var o2mvalue1 = 0.0
        var co2mvalue1 = 0.0
        var o2mvalue2 = 0.0
        var co2mvalue2 = 0.0
        var ko2 = 0.0
        var bo2 = 0.0
        var kco2 = 0.0
        var bco2 = 0.0
        val listko2 = arrayListOf<Double>()
        val listbo2 = arrayListOf<Double>()
        val listkco2 = arrayListOf<Double>()
        val listbco2 = arrayListOf<Double>()
        for (i in 0 until 5) {
            if (o2SensorList[(i + 1) * 2].size == 0 || o2SensorList[(i + 1) * 2 + 1].size == 0 || co2SensorList[(i + 1) * 2].size == 0 || co2SensorList[(i + 1) * 2 + 1].size == 0)
                continue
            val o1 = o2SensorList[(i + 1) * 2].drop(100).average()
            val o2 = o2SensorList[(i + 1) * 2 + 1].drop(100).average()
            val c1 = co2SensorList[(i + 1) * 2].drop(100).average()
            val c2 = co2SensorList[(i + 1) * 2 + 1].drop(100).average()
            if (o1 == o2 || c1 == c2) {
                continue
            } else {
                ko2 = (standardOneGasO2Concentration - standardTwoGasO2Concentration) / (o1 - o2)
                bo2 = standardOneGasO2Concentration - ko2 * o2
                kco2 = (standardOneGasCO2Concentration - standardTwoGasCO2Concentration) / (c2 - c1)
                bco2 = standardOneGasCO2Concentration - kco2 * c1
                listko2.add(ko2)
                listbo2.add(bo2);
                listkco2.add(kco2);
                listbco2.add(bco2);
            }

            if (listko2.size > 0) {
                ko2 = listko2.average()
                bo2 = listbo2.average()
                kco2 = listkco2.average()
                bco2 = listbco2.average()
            }
            var num = 0
            for (j in 0 until 5) {
                if (o2SensorList[(i + 1) * 2].size == 0 || o2SensorList[(i + 1) * 2 + 1].size == 0 || co2SensorList[(i + 1) * 2].size == 0 || co2SensorList[(i + 1) * 2 + 1].size == 0)
                    continue;
                val listo2s = o2SensorList[(i + 1) * 2 + 1].toList();
                val listco2s = co2SensorList[(i + 1) * 2 + 1].toList();
                val o2ac1 = o2SensorList[(i + 1) * 2].drop(80).average();
                val o2ac2 = o2SensorList[(i + 1) * 2 + 1].drop(80).average();
                val co2ac1 = co2SensorList[(i + 1) * 2].drop(80).average();
                val co2ac2 = co2SensorList[(i + 1) * 2 + 1].drop(80).average();
                o2mvalue1 += ko2 * o2ac1 + bo2;
                o2mvalue2 += ko2 * o2ac2 + bo2;
                co2mvalue1 += kco2 * co2ac1 + bco2;
                co2mvalue2 += kco2 * co2ac2 + bco2;

                for (k in listo2s.indices) {
                    if (abs(listo2s[k] - o2ac1) > abs(o2ac1 - o2ac2) * 0.067) {
                        o2offset += k + 1
                        break
                    }
                }

                for (k in listco2s.indices) {
                    if (abs(listco2s[k] - co2ac1) > abs(co2ac1 - co2ac2) * 0.067) {
                        co2offset += k + 1
                        break
                    }
                }
                num++
            }

            if (num != 0) {
                o2mvalue1 = String.format("%2.f", o2mvalue1 / num.toDouble()).toDouble()
                co2mvalue1 = String.format("%2.f", co2mvalue1 / num.toDouble()).toDouble()
                o2mvalue2 = String.format("%2.f", o2mvalue2 / num.toDouble()).toDouble()
                co2mvalue2 = String.format("%2.f", co2mvalue2 / num.toDouble()).toDouble()
                o2offset = String.format("%2.f", o2offset / num.toDouble()).toDouble()
                co2offset = String.format("%2.f", co2offset / num.toDouble()).toDouble()
            }

            o2SensorList[6].addAll(o2SensorList[7]);
            o2SensorList[6].addAll(o2SensorList[8]);
            o2SensorList[6].addAll(o2SensorList[9]);
            o2SensorList[6].addAll(o2SensorList[10]);
            o2SensorList[6].addAll(o2SensorList[11]);
            co2SensorList[6].addAll(co2SensorList[7]);
            co2SensorList[6].addAll(co2SensorList[8]);
            co2SensorList[6].addAll(co2SensorList[9]);
            co2SensorList[6].addAll(co2SensorList[10]);
            co2SensorList[6].addAll(co2SensorList[11]);
            o2t90 = CerlibraHelper.signalFilter(o2SensorList[6]).toDouble()
            co2t90 = CerlibraHelper.signalFilterCO2(co2SensorList[6]).toDouble()

            val measurOneO2 = String.format("%.2f", CerlibraHelper.setO2Value(o2mvalue1)).toDouble()
            val o2OneErr = abs(measurOneO2 - standardOneGasO2Concentration)
            val o2OneResult = if (o2OneErr < 5) "1" else "0"
            o2IngredientCalibrationModelList.add(
                IngredientBean(
                    0,
                    DateUtils.nowTimeString,
                    0,
                    "标气1",
                    standardOneGasO2Concentration.toString(),
                    measurOneO2.toString(),
                    o2OneErr.toString(),
                    o2OneResult, o2t90.toString(), o2offset.toString()
                )
            )

            val measurTwoO2 = String.format("%.2f", CerlibraHelper.setO2Value(o2mvalue2)).toDouble()
            val o2TwoErr = abs(measurTwoO2 - standardTwoGasO2Concentration)
            val o2TwoResult = if (o2TwoErr < 5) "1" else "0"
            o2IngredientCalibrationModelList.add(
                IngredientBean(
                    0,
                    DateUtils.nowTimeString,
                    0,
                    "标气2",
                    standardOneGasO2Concentration.toString(),
                    measurTwoO2.toString(),
                    o2TwoErr.toString(),
                    o2TwoResult, o2t90.toString(), o2offset.toString()
                )
            )
            o2Adapter.setItemsBean(o2IngredientCalibrationModelList)

            val measurOneCO2 =
                String.format("%.2f", CerlibraHelper.setO2Value(co2mvalue1)).toDouble()
            val co2OneErr = abs(measurOneCO2 - standardOneGasCO2Concentration)
            val co2OneResult = if (co2OneErr < 5) "1" else "0"
            co2IngredientCalibrationModelList.add(
                IngredientBean(
                    0,
                    DateUtils.nowTimeString,
                    0,
                    "标气1",
                    standardOneGasO2Concentration.toString(),
                    measurOneCO2.toString(),
                    co2OneErr.toString(),
                    co2OneResult, co2t90.toString(), co2offset.toString()
                )
            )

            val measurTwoCO2 =
                String.format("%.2f", CerlibraHelper.setO2Value(co2mvalue2)).toDouble()
            val co2TwoErr = abs(measurTwoCO2 - standardTwoGasCO2Concentration)
            val co2TwoResult = if (co2TwoErr < 5) "1" else "0"
            co2IngredientCalibrationModelList.add(
                IngredientBean(
                    0,
                    DateUtils.nowTimeString,
                    0,
                    "标气2",
                    standardTwoGasO2Concentration.toString(),
                    measurTwoCO2.toString(),
                    co2TwoErr.toString(),
                    co2TwoResult, co2t90.toString(), co2offset.toString()
                )
            )
            co2Adapter.setItemsBean(co2IngredientCalibrationModelList)

            val listIng = arrayListOf<IngredientBean>()
            listIng.add(
                IngredientBean(
                    0,
                    DateUtils.nowTimeString,
                    0,
                    "标气1",
                    standardOneGasO2Concentration.toString(),
                    measurOneO2.toString(),
                    o2OneErr.toString(),
                    o2OneResult, o2t90.toString(), o2offset.toString()
                )
            )
            listIng.add(
                IngredientBean(
                    0,
                    DateUtils.nowTimeString,
                    0,
                    "标气2",
                    standardOneGasO2Concentration.toString(),
                    measurTwoO2.toString(),
                    o2TwoErr.toString(),
                    o2TwoResult, o2t90.toString(), o2offset.toString()
                )
            )
            listIng.add(
                IngredientBean(
                    0,
                    DateUtils.nowTimeString,
                    0,
                    "标气1",
                    standardTwoGasO2Concentration.toString(),
                    measurTwoCO2.toString(),
                    co2TwoErr.toString(),
                    co2TwoResult, co2t90.toString(), co2offset.toString()
                )
            )
            listIng.add(
                IngredientBean(
                    0,
                    DateUtils.nowTimeString,
                    0,
                    "标气2",
                    standardTwoGasO2Concentration.toString(),
                    measurTwoCO2.toString(),
                    co2TwoErr.toString(),
                    co2TwoResult, co2t90.toString(), co2offset.toString()
                )
            )
            val result = listIng.any { it.calibrationResults == "0" }
            val patientBean = SharedPreferencesUtils.instance.patientBean
            val ingredientCalibrationResultEntity = IngredientCalibrationResultBean()
            ingredientCalibrationResultEntity.calibrationResult = if(result) "1" else "0"
            ingredientCalibrationResultEntity.ingredientId = patientBean?.patientId!!
            ingredientCalibrationResultEntity.calibrationTime = DateUtils.nowTimeString

            ingredientCalibrationResultEntity.kO2 = ko2.toString()
            ingredientCalibrationResultEntity.bO2 = bo2.toString()
            ingredientCalibrationResultEntity.o2Offset = o2offset.toString()
            ingredientCalibrationResultEntity.o2T90 = o2t90.toString()
            ingredientCalibrationResultEntity.o2Error =
                listIng.take(2).maxOf { it.errorRate.toString() }

            ingredientCalibrationResultEntity.kCO2 = kco2.toString()
            ingredientCalibrationResultEntity.bCO2 = bco2.toString()
            ingredientCalibrationResultEntity.cO2T90 = co2offset.toString()
            ingredientCalibrationResultEntity.cO2T90 = co2t90.toString()
            ingredientCalibrationResultEntity.cO2Error =
                listIng.drop(2).take(2).maxOf { it.errorRate.toString() }

            viewModel.setIngredientCaliResultBean(ingredientCalibrationResultEntity)

            if (result) {
                LungCommonDialogFragment.startCommonDialogFragment(
                    requireActivity().supportFragmentManager,
                    "成分定标失败！定标参数保存到数据库！",
                    "2"
                )
            } else {
                LungCommonDialogFragment.startCommonDialogFragment(
                    requireActivity().supportFragmentManager,
                    "成分定标成功！定标参数保存到数据库！",
                    "1"
                )
            }
        }
    }
}