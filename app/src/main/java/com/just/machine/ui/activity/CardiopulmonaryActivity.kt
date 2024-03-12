package com.just.machine.ui.activity


import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.common.base.CommonBaseActivity
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.just.machine.model.Constants
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.ActivityCardiopulmonaryBinding
import com.just.news.databinding.ActivityCardiopulmonarySettingBinding
import com.just.news.databinding.ActivityPatientBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/3/4
 * 心肺测试界面
 *@author zt
 */
@AndroidEntryPoint
class CardiopulmonaryActivity : CommonBaseActivity<ActivityCardiopulmonaryBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    companion object {
        /**
         * @param context context
         */
        fun startCardiopulmonaryActivity(context: Context?) {
            val intent = Intent(context, CardiopulmonaryActivity::class.java)
            context?.startActivity(intent)
        }
    }


    private fun initToolbar() {
        binding.toolbar.title = Constants.cardiopulmonary//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.visible()

        binding.setting.setNoRepeatListener{
            CardiopulmonarySettingActivity.startCSettingActivity(this)

        }
    }

    override fun initView() {
        initToolbar()
    }

    override fun getViewBinding() = ActivityCardiopulmonaryBinding.inflate(layoutInflater)
}