package com.just.machine.ui.fragment.cardiopulmonary

import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.model.LungTestData
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.lungdata.BreathState
import com.just.machine.model.lungdata.CPXSerializeData
import com.just.machine.model.lungdata.DyCalculeSerializeCore
import com.just.machine.ui.adapter.FragmentPagerAdapter
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.DynamicDataFragment
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.RoutineFragment
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.WassermanFragment
import com.just.machine.ui.fragment.serial.ModbusProtocol
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.model.lungdata.CPXCalcule
import com.just.machine.util.LiveDataBus
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.FragmentDynamicBinding
import com.justsafe.libview.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale


/**
 *create by 2024/6/19
 * 动态肺测试
 *@author zt
 */
@AndroidEntryPoint
class DynamicFragment : CommonBaseFragment<FragmentDynamicBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val tts: TextToSpeech by lazy {
        TextToSpeech(requireContext()) { status ->
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

    override fun loadData() {//懒加载

    }

    private val usbTransferUtil: USBTransferUtil by lazy {
        USBTransferUtil.getInstance()
    }


    private fun initToolbar() {

    }

    override fun initView() {
        initToolbar()

        initViewPager()

        binding.llStart.setNoRepeatListener {

            tts.speak("准备开始正常呼吸3次，然后开始深呼吸", TextToSpeech.QUEUE_FLUSH, null, "")

            lifecycleScope.launch {
                delay(200)
                usbTransferUtil.write(ModbusProtocol.allowTwoSensor)
            }//测试串口非蓝牙模式，需要蓝牙串口链接下面那个

//            SerialPortManager.sendMessage(ModbusProtocol.FLOW_CALIBRATION_COMMAND)//发送流量定标a

            return@setNoRepeatListener
        }

        binding.llClean.setNoRepeatListener {

        }



    }

    private fun initViewPager() {

        val adapter = FragmentPagerAdapter(requireActivity())

        adapter.addFragment(RoutineFragment())

        adapter.addFragment(WassermanFragment())

        adapter.addFragment(DynamicDataFragment())

        binding.vpTitle.setCurrentItem(0, true)

        binding.vpTitle.adapter = adapter

        binding.vpTitle.isUserInputEnabled = false

        binding.llStart.setNoRepeatListener {

        }

        binding.llClean.setNoRepeatListener { //点击取消

        }

    }

    override fun initListener() {

        binding.btnRoutine.setNoRepeatListener {
            binding.vpTitle.currentItem = 0
            setButtonPosition(0)
        }

        binding.btnWasserman.setNoRepeatListener {
            binding.vpTitle.currentItem = 1
            setButtonPosition(1)
        }

        binding.btnData.setNoRepeatListener {
            binding.vpTitle.currentItem = 2
            setButtonPosition(2)
        }

        //这个必须写，不然会产生Fata
        binding.vpTitle.isSaveEnabled = false

        binding.vpTitle.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 当页面被选中时执行你的操作
//                LogUtils.d(tag + "ViewPager2Selected page: $position")

                setButtonPosition(position)

            }
        })
    }

    private fun setButtonPosition(position: Int) {
        when (position) {
            0 -> {
                setButtonStyle(
                    binding.btnRoutine, binding.btnWasserman, binding.btnData
                )
            }

            1 -> {
                setButtonStyle(
                    binding.btnWasserman, binding.btnRoutine, binding.btnData
                )
            }

            2 -> {
                setButtonStyle(
                    binding.btnData, binding.btnWasserman, binding.btnRoutine
                )
            }

        }
    }

    private fun setButtonStyle(
        textView1: TextView,
        textView2: TextView,
        textView3: TextView,
    ) {// 设置按钮的样式

        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        textView1.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.super_edittext_bg)

        textView2.setTextColor(ContextCompat.getColor(requireContext(), R.color.cD9D9D9))
        textView2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        textView3.setTextColor(ContextCompat.getColor(requireContext(), R.color.cD9D9D9))
        textView3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    override fun onDestroy() {
        // 释放 TextToSpeech 资源
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicBinding.inflate(inflater, container, false)

}
