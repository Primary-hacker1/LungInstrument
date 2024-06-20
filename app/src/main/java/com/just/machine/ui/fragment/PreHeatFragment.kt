package com.just.machine.ui.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.ui.activity.MainActivity
import com.just.machine.util.FixCountDownTime
import com.just.news.R
import com.just.news.databinding.FragmentPreheatBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 预热界面
 */
@AndroidEntryPoint
class PreHeatFragment : CommonBaseFragment<FragmentPreheatBinding>() {

    private lateinit var mCountDownTime: FixCountDownTime

    override fun loadData() {

    }

    override fun initView() {
        mCountDownTime = object : FixCountDownTime(1200, 1000) {}
        binding.pbPreheat.progressDrawable = resources.getDrawable(R.drawable.progressbar_bg)

        mCountDownTime.start(object : FixCountDownTime.OnTimerCallBack{
            override fun onStart() {
                binding.tvPreheatStatus.text = "正在预热..."
            }

            override fun onTick(times: Int) {
                Log.d("PreHeat","预热倒计时===$times")
                val minute = times / 60 % 60
                val second = times % 60
                val progress = (((1200 - times-1) / 1200.toFloat()) * 100).toInt()
                var realSecond = ""
                realSecond = if (second.toString().length == 1) {
                    "0$second"
                } else {
                    "${second.toString().substring(0, 1)}${second.toString().substring(1)}"
                }
                binding.pbPreheat.progress = progress
                binding.tvPreheatProgress.text = "$progress %"
                binding.tvPreheatRemainTime.text = "剩余时间: $minute:$realSecond"
            }

            override fun onFinish() {
                MainActivity.startMainActivity(activity)
                activity?.finish()
            }
        })
    }

    override fun initListener() {
        binding.tvSkipPreheat.setNoRepeatListener {
            mCountDownTime.cancel()
            MainActivity.startMainActivity(activity)
            activity?.finish()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) =   FragmentPreheatBinding.inflate(inflater, container, false)
}