package com.just.machine.ui.fragment.calibration.onekeycalibration

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.ui.adapter.FragmentChildAdapter
import com.just.machine.ui.dialog.LungCommonDialogFragment
import com.just.machine.ui.fragment.serial.ModbusProtocol
import com.just.machine.util.FixCountDownTime
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentOnekeyCalibrationBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

/**
 * 一键定标
 */
@AndroidEntryPoint
class OneKeyCalibrationFragment : CommonBaseFragment<FragmentOnekeyCalibrationBinding>() {

    private var countSec = 0
    private var iFlag = 0
    private var isFlowSend = false //流量定标指令是否发送
    private var envSuccess = false //环境定标是否成功
    private var flowSuccess = false //自动流量定标是否成功
    private var ingredientSuccess = false //成分定标是否成功
    private lateinit var timer: Timer

    override fun loadData() {

    }

    override fun initView() {
        val adapter = FragmentChildAdapter(this)

        adapter.addFragment(OneKeyGuideFragment())
        adapter.addFragment(OneKeyEnvironmentFragment())
        adapter.addFragment(OneKeyFlowFragment())
        adapter.addFragment(OneKeyIngredientFragment())

        binding.vpOnekey.setCurrentItem(0, true)

        binding.vpOnekey.adapter = adapter

        binding.vpOnekey.isUserInputEnabled = false
    }

    override fun initListener() {
        binding.llOnekeyStart.setNoRepeatListener {
            prepareStart()
            disEnableStartBtn()
            timer = fixedRateTimer("", false, 0, 1000) {
                Log.d("oneky", "正计时======")
                if (countSec == 0) {
                    iFlag = 1
                    binding.vpOnekey.setCurrentItem(1, true)
                }
                if (countSec < 5 && iFlag == 1) {
                    if (envSuccess) {
                        binding.vpOnekey.setCurrentItem(2, true)
                        binding.tvOnekeyCalibrationEnvironment.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        binding.ivOnekeyCalibrationEnvironment.setImageResource(R.drawable.environment_highlight)
                        iFlag = 2
                    }
                } else if (countSec == 5 && iFlag == 1) {
                    binding.vpOnekey.setCurrentItem(2, true)
                    iFlag = 2
                } else if (countSec in 6..54 && iFlag == 2) {
                    //自动流量定标开始
                    if (!isFlowSend) {
                        isFlowSend = true
                        LiveDataBus.get().with("oneKeyCalibra").value = "flowAuto"
                    }
                    if (flowSuccess) {
                        binding.vpOnekey.setCurrentItem(3, true)
                        binding.tvOnekeyCalibrationLineTwo.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        binding.tvOnekeyCalibrationFlow.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        binding.ivOnekeyCalibrationFlow.setImageResource(R.drawable.flow_highlight)
                        iFlag = 3
                    }
                }else if(countSec > 54 && iFlag == 3) {
                    if(ingredientSuccess){
                        binding.vpOnekey.setCurrentItem(0, true)
                        binding.tvOnekeyCalibrationLineTwo.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        binding.tvOnekeyCalibrationFlow.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        binding.ivOnekeyCalibrationFlow.setImageResource(R.drawable.flow_highlight)
                        timer.cancel()
                        enableStartBtn()
                    }
                }
                countSec++
            }
        }

        LiveDataBus.get().with("oneKeyCalibra").observe(this) {
            if (it is String) {
                when (it) {
                    "environmentFailed" -> {
                        envSuccess = false
                    }
                    "environmentSuccess" -> {
                        envSuccess = true
                    }
                    "flowAutoFailed" -> {
                        flowSuccess = false
                    }
                    "flowAutoSuccess" -> {
                        flowSuccess = true
                    }
                }
            }
        }
    }

    private fun prepareStart() {
        countSec = 0
        iFlag = 0
        isFlowSend = false
        envSuccess = false
        flowSuccess = false
        ingredientSuccess = false
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnekeyCalibrationBinding.inflate(inflater, container, false)


    fun disEnableStartBtn() {
        binding.llOnekeyStart.setBackgroundResource(R.drawable.save_bg_gray)
        binding.llOnekeyStart.isEnabled = false
        binding.tvOnekeyStart.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.text7
            )
        )
        binding.ivOnekeyStart.setImageResource(R.drawable.baseline_play_gray)
    }

    private fun enableStartBtn() {
        binding.llOnekeyStart.setBackgroundResource(R.drawable.save_bg)
        binding.llOnekeyStart.isEnabled = true
        binding.tvOnekeyStart.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorWhite
            )
        )
        binding.ivOnekeyStart.setImageResource(R.drawable.baseline_play)
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }
}