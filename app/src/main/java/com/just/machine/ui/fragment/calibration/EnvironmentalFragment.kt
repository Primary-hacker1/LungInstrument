package com.just.machine.ui.fragment.calibration

import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.CommonUtil
import com.common.base.delay
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.calibration.EnvironmentalCalibrationBean
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.calibration.EnvironmentalAdapter
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.BaseUtil
import com.just.machine.util.LiveDataBus
import com.just.news.databinding.FragmentEnvironmentalBinding
import com.justsafe.libview.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.Locale

/**
 *create by 2020/6/19
 * 环境定标
 *@author zt
 */
@AndroidEntryPoint
class EnvironmentalFragment : CommonBaseFragment<FragmentEnvironmentalBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var tts: TextToSpeech

    private val adapter by lazy { EnvironmentalAdapter(requireContext()) }

    override fun initView() {
        binding.rvEnvironmental.layoutManager = LinearLayoutManager(requireContext())

        adapter.setItemClickListener { _, position ->
            adapter.toggleItemBackground(position)
        }

        binding.rvEnvironmental.adapter = adapter

        binding.llStart.setNoRepeatListener {
            tts.speak("开始环境定标", TextToSpeech.QUEUE_ADD, null, "")
            if (Constants.isDebug) {
                val temperature: Short = 250 // 温度，单位为摄氏度
                val humidity: Short = 60 // 湿度，单位为百分比
                val pressure = 101325 // 气压，单位为帕斯卡

                val environmentData = MudbusProtocol.generateSerialCommand(
                    temperature,
                    humidity,
                    pressure
                )
                LiveDataBus.get().with(Constants.serialCallback).value = environmentData

                // 延迟任务部分
                tts.delay(10000L, {}, {
                    speak("环境定标完成，是否保存", TextToSpeech.QUEUE_ADD, null, "")
                })

                return@setNoRepeatListener
            }


            SerialPortManager.sendMessage(MudbusProtocol.ENVIRONMENT_CALIBRATION_COMMAND)//发送环境定标
        }

        binding.llSave.setNoRepeatListener {

        }

        LiveDataBus.get().with(Constants.serialCallback).observe(this) {//解析串口消息
            if (it is ByteArray) {
//                if(BaseUtil.isDoubleClick()) return@observe//有个粘性事件先不处理
                LogUtils.e(tag + BaseUtil.bytes2HexStr(it) + "字节长度" + BaseUtil.bytes2HexStr(it).length)
                val environmentData = MudbusProtocol.parseEnvironmentData(it)//环境定标

                if (environmentData != null) {
                    val (temperature, humidity, pressure) = environmentData
                    val time = DateUtils.nowTimeString

                    viewModel.setEnvironmental(
                        EnvironmentalCalibrationBean(//假设用户id为1
                            0, 1, time, temperature.toString(),
                            humidity.toString(), pressure.toString()
                        )
                    )//插入数据库并查询
                } else {
                    LogUtils.e(tag + "接收到的环境定标数据无效！")
                }
            }
        }

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.EnvironmentalsSuccess -> {
                    it.any?.let { it1 -> beanQuery(it1) }
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

    private var environmentalBeans: MutableList<EnvironmentalCalibrationBean> = ArrayList()

    private fun beanQuery(any: Any) {
        if (any is List<*>) {

            environmentalBeans.clear()

            val datas = any as MutableList<*>

            var fistBean: EnvironmentalCalibrationBean? = null

            for (num in 0 until datas.size) {
                val bean = datas[num] as EnvironmentalCalibrationBean
                environmentalBeans.add(bean)
            }

            if (environmentalBeans.size > 0) {
                fistBean = datas[0] as EnvironmentalCalibrationBean
            }

            binding.atvCreateTime.text = fistBean?.createTime
            binding.tvTemperature.text = fistBean?.temperature
            binding.tvHumidity.text = fistBean?.humidity
            binding.tvPressure.text = fistBean?.pressure

            adapter.setItemsBean(environmentalBeans)

            LogUtils.d(tag + environmentalBeans.toString())

            binding.rvEnvironmental.adapter = adapter
        }
    }


    override fun initListener() {

    }

    /**
     * 懒加载
     */
    override fun loadData() {
        viewModel.getEnvironmental()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 对于 Fragment，推荐在 onDestroyView 中移除观察者
        LiveDataBus.get().with(Constants.serialCallback).removeObservers(viewLifecycleOwner)
    }

    override fun onDestroy() {
        // 释放 TextToSpeech 资源
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEnvironmentalBinding.inflate(inflater, container, false)
}