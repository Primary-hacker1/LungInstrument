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
    private var envSuccess = false
    private lateinit var timer: Timer
    private val mCountDownTime by lazy {
        object : FixCountDownTime(110, 1000) {}
    }

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
            timer = fixedRateTimer("", false, 0, 1000) {
                Log.d("oneky", "正计时======")
                if (countSec == 0) {
                    iFlag = 1
                    binding.vpOnekey.setCurrentItem(1, true)
                }
                if (countSec < 5 && iFlag == 1) {
                    if(envSuccess){
                        binding.vpOnekey.setCurrentItem(2, true)
                        binding.tvOnekeyCalibrationEnvironment.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        binding.ivOnekeyCalibrationEnvironment.setImageResource(R.drawable.environment_highlight)
                        iFlag = 2
                    }else{
                        LungCommonDialogFragment.startCommonDialogFragment(
                            requireActivity().supportFragmentManager, "一键定标失败!", "2"
                        )
                        timer.cancel()
                        enableStartBtn()
                        binding.vpOnekey.setCurrentItem(0, true)
                    }
                } else if (countSec == 5 && iFlag == 1) {
                    binding.vpOnekey.setCurrentItem(2, true)
                    iFlag = 2
                }
                countSec++
            }
            mCountDownTime.start(object : FixCountDownTime.OnTimerCallBack {
                override fun onStart() {
                    disEnableStartBtn()
                    binding.vpOnekey.setCurrentItem(1, true)
                    LiveDataBus.get().with("oneKeyCalibra").value = "environment"
                }

                override fun onTick(times: Int) {
                    Log.d("oneky", "倒计时======$times")
                    when (times) {
                        105 -> {
                            binding.vpOnekey.setCurrentItem(2, true)
                            binding.tvOnekeyCalibrationEnvironment.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.green
                                )
                            )
                            binding.ivOnekeyCalibrationEnvironment.setImageResource(R.drawable.environment_highlight)
                        }

                        50 -> {
                            binding.vpOnekey.setCurrentItem(3, true)
                            binding.tvOnekeyCalibrationLineOne.setBackgroundColor(
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
                        }

                        0, 1 -> {
                            binding.tvOnekeyCalibrationLineTwo.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.green
                                )
                            )
                            binding.tvOnekeyCalibrationIngredient.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.green
                                )
                            )
                            binding.ivOnekeyCalibrationIngredient.setImageResource(R.drawable.ingredient_highlight)
                            enableStartBtn()
                        }
                    }
                }

                override fun onFinish() {

                }
            })
        }

        LiveDataBus.get().with("oneKeyCalibra").observe(this) {
            if (it is String) {
                if (it == "environmentFailed") {
                    envSuccess = false
                }else if(it == "environmentSuccess"){
                    envSuccess = true
                }
            }
        }
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

    fun enableStartBtn() {
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
        mCountDownTime.cancel()
        super.onDestroy()
    }
}