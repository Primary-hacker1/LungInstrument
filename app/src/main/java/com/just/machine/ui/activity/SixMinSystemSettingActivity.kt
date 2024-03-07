package com.just.machine.ui.activity

import com.common.base.CommonBaseActivity
import com.just.news.R
import com.just.news.databinding.ActivitySixminSystemSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SixMinSystemSettingActivity : CommonBaseActivity<ActivitySixminSystemSettingBinding>() {
    override fun getViewBinding() = ActivitySixminSystemSettingBinding.inflate(layoutInflater)

    override fun initView() {
        initToolbar()
    }

    private fun initToolbar() {
        binding.toolbar.title = getString(R.string.system_setting)//标题
//        binding.toolbar.tvRight.gone()
//        binding.toolbar.ivTitleBack.visible()
//        binding.toolbar.ivTitleBack.setNoRepeatListener {
//            finish()
//        }
    }
}