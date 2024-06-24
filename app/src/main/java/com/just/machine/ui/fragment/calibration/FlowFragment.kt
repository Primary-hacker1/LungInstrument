package com.just.machine.ui.fragment.calibration


import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.FragmentChildAdapter
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.common.base.BaseUtil
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentFlowBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


/**
 *create by 2024/6/19
 * 流量定标
 *@author zt
 */
@AndroidEntryPoint
class FlowFragment : CommonBaseFragment<FragmentFlowBinding>() {

    private lateinit var tts: TextToSpeech

    override fun initView() {

        val adapter = FragmentChildAdapter(this)

        adapter.addFragment(FlowHandleFragment())
        adapter.addFragment(FlowAutoFragment())

        binding.vpFlowTitle.setCurrentItem(1, true)

        binding.vpFlowTitle.adapter = adapter

        binding.vpFlowTitle.isUserInputEnabled = false

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

        binding.llStart.setNoRepeatListener {
            if (binding.tvFlowStart.text == "开始") {
                tts.speak("开始流量定标", TextToSpeech.QUEUE_FLUSH, null, "")
//            if (Constants.isDebug) {
//                val smallRangeFlow = 0 // 例如，120 L/min
//                val largeRangeFlow = 30 // 例如，3000 L/min
//
//                val data = MudbusProtocol.generateFlowCalibrationCommand(
//                    smallRangeFlow,
//                    largeRangeFlow,
//                )
//
//                LogUtils.d(tag + BaseUtil.bytes2HexStr(data) + "发送的数据")
//
//                LiveDataBus.get().with("测试").value = data
//
//                return@setNoRepeatListener
//            }
                //手动流量定标
                if (binding.vpFlowTitle.currentItem == 0) {
                    LiveDataBus.get().with("flow").value = "handleFlow"
                    SerialPortManager.sendMessage(MudbusProtocol.FLOW_CALIBRATION_COMMAND)//发送手动流量定标
                } else {
                    LiveDataBus.get().with("flow").value = "autoFlow"
                    SerialPortManager.sendMessage(MudbusProtocol.FLOW_AUTO_CALIBRATION_COMMAND)//发送自动流量定标
                }
                binding.tvFlowStart.text = "停止"
            } else {
                SerialPortManager.sendMessage(MudbusProtocol.FLOW_STOP_COMMAND)
                binding.tvFlowStart.text = "开始"
            }
        }
        LiveDataBus.get().with("测试").observe(this) {//解析串口消息
            if (it is ByteArray) {
                LogUtils.d(tag + BaseUtil.bytes2HexStr(it) + "字节长度" + BaseUtil.bytes2HexStr(it).length)
                val data = MudbusProtocol.parseFlowCalibrationData(it)
                if (data != null) {
                    val (smallRangeFlow, largeRangeFlow) = data

                    LogUtils.d(tag + smallRangeFlow)

                    LogUtils.d(tag + largeRangeFlow)

                }

            }
        }
        LiveDataBus.get().with("flow").observe(this) {//解析串口消息
            if (it is String) {
                if (it == "complete") {
                    binding.tvFlowStart.text = "开始"
                }
            }
        }
    }

    override fun initListener() {
        binding.btnHandle.setNoRepeatListener {
            binding.vpFlowTitle.currentItem = 0
            setButtonPosition(0)
        }
        binding.btnAuto.setNoRepeatListener {
            binding.vpFlowTitle.currentItem = 1
            setButtonPosition(1)
        }
    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFlowBinding.inflate(inflater, container, false)

    private fun setButtonPosition(position: Int) {
        when (position) {
            0 -> {
                setButtonStyle(
                    binding.btnHandle, binding.btnAuto
                )
            }

            1 -> {
                setButtonStyle(
                    binding.btnAuto, binding.btnHandle
                )
            }
        }
    }

    override fun onDestroy() {
        // 释放 TextToSpeech 资源
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }

    private fun setButtonStyle(
        textView1: TextView,
        textView2: TextView,
    ) {// 设置按钮的样式

        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        textView1.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.super_edittext_bg)

        textView2.setTextColor(ContextCompat.getColor(requireContext(), R.color.cD9D9D9))
        textView2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
    }
}