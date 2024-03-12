package com.just.machine.ui.activity

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.common.base.CommonBaseActivity
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.SpinnerHelper
import com.just.news.R
import com.just.news.databinding.ActivityCardiopulmonarySettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardiopulmonarySettingActivity : CommonBaseActivity<ActivityCardiopulmonarySettingBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    companion object {
        /**
         * @param context context
         */
        fun startCSettingActivity(context: Context?) {
            val intent = Intent(context, CardiopulmonarySettingActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun initView() {
        initToolbar()

        // 使用封装类 SpinnerHelper 处理 Spinner 相关逻辑
        val spinnerHelper = SpinnerHelper(this, binding.spEcg, R.array.spinner_items)
        spinnerHelper.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String) {

            }

            override fun onNothingSelected() {

            }
        })

        initClickListener()
    }

    private fun initClickListener() {

    }

    private fun initToolbar() {
        binding.toolbar.title = getString(R.string.system_setting)//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.visible()
        binding.toolbar.ivTitleBack.setNoRepeatListener {
            finish()
        }

    }

    override fun getViewBinding() = ActivityCardiopulmonarySettingBinding.inflate(layoutInflater)
}
