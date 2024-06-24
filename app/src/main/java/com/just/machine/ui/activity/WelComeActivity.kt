package com.just.machine.ui.activity

import com.common.base.CommonBaseActivity
import com.just.news.databinding.ActivityWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/6/6
 * 欢迎界面备用
 *@author zt
 */
@AndroidEntryPoint
class WelComeActivity : CommonBaseActivity<ActivityWelcomeBinding>() {

    override fun initView() {}

    override fun getViewBinding() = ActivityWelcomeBinding.inflate(layoutInflater)
}