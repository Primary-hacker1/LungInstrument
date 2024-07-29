package com.just.machine.ui.fragment.calibration


import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.FragmentChildAdapter
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
                //手动流量定标
                if (binding.vpFlowTitle.currentItem == 0) {
                    LiveDataBus.get().with(Constants.clickStartFlowCalibra).value = Constants.flowHandleCalibra
                } else {
                    LiveDataBus.get().with(Constants.clickStartFlowCalibra).value = Constants.flowAutoCalibra
                }
            } else {
                //手动流量定标
                if (binding.vpFlowTitle.currentItem == 0) {
                    LiveDataBus.get().with(Constants.clickStopFlowCalibra).value = Constants.flowHandleCalibra
                } else {
                    LiveDataBus.get().with(Constants.clickStopFlowCalibra).value = Constants.flowAutoCalibra
                }
                binding.tvFlowStart.text = getString(R.string.begin)
            }
        }

        //定标开始
        LiveDataBus.get().with(Constants.startFlowHCalibra).observe(this) {
            if (it is String) {
                binding.tvFlowStart.text = getString(R.string.stop)
            }
        }

        //定标结束
        LiveDataBus.get().with(Constants.stopFlowCalibra).observe(this) {
            if (it is String) {
                binding.tvFlowStart.text = getString(R.string.begin)
            }
        }
    }

    override fun initListener() {
        binding.btnHandle.setNoRepeatListener {
            if(binding.tvFlowStart.text == "停止"){
                Toast.makeText(requireContext(),"定标已开始，请稍后!",Toast.LENGTH_SHORT).show()
            }else{
                binding.vpFlowTitle.currentItem = 0
                setButtonPosition(0)
            }
        }
        binding.btnAuto.setNoRepeatListener {
            if(binding.tvFlowStart.text == "停止"){
                Toast.makeText(requireContext(),"定标已开始，请稍后!",Toast.LENGTH_SHORT).show()
            }else{
                binding.vpFlowTitle.currentItem = 1
                setButtonPosition(1)
            }
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