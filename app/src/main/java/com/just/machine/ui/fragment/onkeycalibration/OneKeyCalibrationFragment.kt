package com.just.machine.ui.fragment.onkeycalibration

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.ui.adapter.FragmentChildAdapter
import com.just.machine.util.FixCountDownTime
import com.just.news.R
import com.just.news.databinding.FragmentOnekeyCalibrationBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 一键定标
 */
@AndroidEntryPoint
class OneKeyCalibrationFragment : CommonBaseFragment<FragmentOnekeyCalibrationBinding>() {

    private lateinit var mCountDownTime: FixCountDownTime

    override fun loadData() {

    }

    override fun initView() {
        mCountDownTime = object : FixCountDownTime(30, 1000) {}
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
            mCountDownTime.start(object : FixCountDownTime.OnTimerCallBack {
                override fun onStart() {
                    disEnableStartBtn()
                    binding.vpOnekey.setCurrentItem(1, true)
                }

                override fun onTick(times: Int) {
                    Log.d("oneky", "倒计时======$times")
                    when (times) {
                        20 -> {
                            binding.vpOnekey.setCurrentItem(2, true)
                            binding.tvOnekeyCalibrationEnvironment.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.green
                                )
                            )
                            binding.ivOnekeyCalibrationEnvironment.setImageResource(R.drawable.environment_highlight)
                        }

                        10 -> {
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

}