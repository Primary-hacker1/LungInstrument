package com.just.machine.ui.fragment.calibration

import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.BaseUtil
import com.common.base.CommonBaseFragment
import com.common.base.notNull
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent.Companion.INGREDIENTS_SUCCESS
import com.just.machine.dao.calibration.IngredientBean
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.calibration.IngredientAdapter
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentIngredientBinding
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
        } else {
            binding.etOneO2.isEnabled = false
            binding.etOneCo2.isEnabled = false
            binding.etTwoCo2.isEnabled = false
            binding.etTwoCo2.isEnabled = false
        }

        binding.rvIngredient.layoutManager = LinearLayoutManager(requireContext())

        o2Adapter.setItemClickListener { _, position ->
            o2Adapter.toggleItemBackground(position)
        }

        binding.chartView.setLineDataSetData(binding.chartView.flowDataSetList())

        binding.chartView.setLineChartFlow(
            yAxisMinimum = 0f,
            yAxisMaximum = 30f,
            countMaxX = 60f,
            granularityY = 1.5f,
            granularityX = 2f,
            titleCentent = "成分定标"
        )

        binding.rvIngredient.adapter = o2Adapter
        binding.rvIngredient2.adapter = co2Adapter

        binding.llStart.setNoRepeatListener {
            tts.speak("开始成分定标", TextToSpeech.QUEUE_FLUSH, null, "")
            if (Constants.isDebug) {
                // 调用生成主控板返回数据方法并打印生成的数据
                val controlBoardResponse = MudbusProtocol.ControlBoardData(
                    0x12.toByte(), // 返回命令
                    1000, // 大量程流量传感器数据
                    500, // 小量程流量传感器数据
                    800, // CO2传感器数据
                    200, // O2传感器数据
                    1500, // 分析气体流速传感器数据
                    1000, // 分析气体压力传感器数据
                    300, // 温度数据
                    80 // 电量数据
                )

                val data = MudbusProtocol.generateControlBoardResponse(
                    controlBoardResponse
                )

                LogUtils.e(tag + BaseUtil.bytes2HexStr(data) + "发送的数据")

                LiveDataBus.get().with("测试3").value = data

                return@setNoRepeatListener
            }

            SerialPortManager.sendMessage(MudbusProtocol.FLOW_CALIBRATION_COMMAND)//发送流量定标
        }

        LiveDataBus.get().with("测试3").observe(this) {//解析串口消息
            if (it is ByteArray) {
                LogUtils.e(tag + BaseUtil.bytes2HexStr(it) + "字节长度" + BaseUtil.bytes2HexStr(it).length)
                val data = MudbusProtocol.parseControlBoardResponse(it)
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
            if(isChecked){
                enableEditTextStyle()
            }else{
                disEnableEditTextStyle()
            }
        }
    }

    private fun enableEditTextStyle(){
        binding.etOneO2.isEnabled = true
        binding.etOneCo2.isEnabled = true
        binding.etTwoCo2.isEnabled = true
        binding.etTwoCo2.isEnabled = true
        binding.etOneO2.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_white_solid)
        binding.etOneCo2.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_white_solid)
        binding.etTwoO2.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_white_solid)
        binding.etTwoCo2.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_white_solid)
    }

    private fun disEnableEditTextStyle(){
        binding.etOneO2.isEnabled = false
        binding.etOneCo2.isEnabled = false
        binding.etTwoCo2.isEnabled = false
        binding.etTwoCo2.isEnabled = false
        binding.etOneO2.setBackgroundResource(R.drawable.frame_with_color_transparent)
        binding.etOneCo2.setBackgroundResource(R.drawable.frame_with_color_transparent)
        binding.etTwoO2.setBackgroundResource(R.drawable.frame_with_color_transparent)
        binding.etTwoCo2.setBackgroundResource(R.drawable.frame_with_color_transparent)
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
}