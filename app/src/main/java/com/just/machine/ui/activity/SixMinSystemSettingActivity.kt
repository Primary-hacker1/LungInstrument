package com.just.machine.ui.activity

import androidx.core.content.ContextCompat
import com.common.base.CommonBaseActivity
import com.just.news.R
import com.just.news.databinding.ActivitySixminSystemSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SixMinSystemSettingActivity : CommonBaseActivity<ActivitySixminSystemSettingBinding>() {

    private var autoCircleCount: Boolean = true
    override fun getViewBinding() = ActivitySixminSystemSettingBinding.inflate(layoutInflater)

    override fun initView() {
        initToolbar()
        initClickListener()
    }

    private fun initClickListener() {
        binding.sixminRlAutoCircleCount.setOnClickListener {
            if (!autoCircleCount) {
                binding.sixminRlAutoCircleCount.setBackgroundResource(R.drawable.sixmin_circle_select_bg)
                binding.sixminRlHandleCircleCount.setBackgroundResource(R.drawable.sixmin_circle_unselect_bg)
                binding.sixminTvAutoCircleCount.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )
                binding.sixminTvHandleCircleCount.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.text3
                    )
                )
            }
            autoCircleCount = !autoCircleCount
        }
        binding.sixminRlHandleCircleCount.setOnClickListener {
            if (autoCircleCount) {
                binding.sixminRlAutoCircleCount.setBackgroundResource(R.drawable.sixmin_circle_unselect_bg)
                binding.sixminRlHandleCircleCount.setBackgroundResource(R.drawable.sixmin_circle_select_bg)
                binding.sixminTvAutoCircleCount.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.text3
                    )
                )
                binding.sixminTvHandleCircleCount.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )
            }
            autoCircleCount = !autoCircleCount
        }
    }

    private fun initToolbar() {
        binding.toolbar.title = getString(R.string.system_setting)//标题
//        binding.toolbar.tvRight.gone()
//        binding.toolbar.ivTitleBack.visible()
//        binding.toolbar.ivTitleBack.setNoRepeatListener {
//            finish()
//        }
        binding.rbBroadcastGuidanceVoiceYes.isChecked = true
        binding.rbAutoMearsureBloodYes.isChecked = true
        binding.rbAutoStartTestYes.isChecked = true
        binding.rbPositionOfTrolleyMiddle.isChecked = true
        binding.rbStepsOrBreathSteps.isChecked = true
        binding.rbShowRestTimeYes.isChecked = true
    }
}