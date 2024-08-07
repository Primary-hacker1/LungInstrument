package com.just.machine.ui.fragment

import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.setting.AllSettingBean
import com.just.machine.ui.activity.MainActivity
import com.just.machine.ui.fragment.serial.ModbusProtocol
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.FixCountDownTime
import com.just.news.R
import com.just.news.databinding.FragmentPreheatBinding
import com.justsafe.libview.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 预热界面
 */
@AndroidEntryPoint
class PreHeatFragment : CommonBaseFragment<FragmentPreheatBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private var mCountDownTime: FixCountDownTime? = null

    override fun loadData() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(100)
            binding.batteryStatus.setPower(ModbusProtocol.batteryLevel)
        }
    }

    override fun initView() {
        viewModel.getAllSettingBeans()

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.ALL_SETTING_SUCCESS -> {
                    if (it.any !is MutableList<*>) {
                        return@observe
                    }

                    val settings = it.any as MutableList<*>

                    for (settingBean in settings) {
                        if (settingBean !is AllSettingBean) {
                            return@observe
                        }
                        binding.tvHospitalName.text = settingBean.hospitalName
                    }
                }
            }
        }

        binding.tvSkipPreheat.text = Html.fromHtml("<u>跳过</u>")
        binding.tvSystemTime.setTime(System.currentTimeMillis(),DateUtils.nowTimeDataString)
        mCountDownTime = object : FixCountDownTime(1200-ModbusProtocol.warmLeaveSec, 1000) {}

        val progressbarBg =  ContextCompat.getDrawable(requireContext(),R.drawable.progressbar_bg)

        binding.pbPreheat.progressDrawable = progressbarBg

        if(ModbusProtocol.isDeviceConnect){
            binding.tvPreheatDeviceConnectStatus.text = "已连接"
            binding.ivPreheatDeviceConnectStatus.setImageResource(R.drawable.wifi_on)
        }else{
            binding.tvPreheatDeviceConnectStatus.text = "未连接"
            binding.ivPreheatDeviceConnectStatus.setImageResource(R.drawable.warn_yellow)
        }

        mCountDownTime!!.start(object : FixCountDownTime.OnTimerCallBack{
            override fun onStart() {
                binding.tvPreheatStatus.text = "正在预热..."
            }

            override fun onTick(times: Int) {
                Log.d("PreHeat","预热倒计时===$times")
                val minute = times / 60 % 60
                val second = times % 60
                val progress = (((1200 - times-1) / 1200.toFloat()) * 100).toInt()
                val realSecond: String = if (second.toString().length == 1) {
                    "0$second"
                } else {
                    "${second.toString().substring(0, 1)}${second.toString().substring(1)}"
                }
                binding.pbPreheat.progress = progress
                binding.tvPreheatProgress.text = "$progress %"
                binding.tvPreheatRemainTime.text = "剩余时间: $minute:$realSecond"
            }

            override fun onFinish() {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        })
    }

    override fun initListener() {
        binding.tvSkipPreheat.setNoRepeatListener {
            mCountDownTime!!.cancel()
            MainActivity.startMainActivity(activity)
            activity?.finish()
        }
        binding.ivPreheatClose.setNoRepeatListener {
            mCountDownTime!!.cancel()
            activity?.finish()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) =   FragmentPreheatBinding.inflate(inflater, container, false)
}