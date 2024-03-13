package com.just.machine.ui.activity

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.common.base.CommonBaseActivity
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.just.machine.model.Constants
import com.just.machine.ui.fragment.AllSettingFragment
import com.just.machine.ui.fragment.DynamicSettingFragment
import com.just.machine.ui.fragment.FragmentPagerAdapter
import com.just.machine.ui.fragment.StaticSettingFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
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

        val adapter = FragmentPagerAdapter(this)
        // 添加三个 Fragment
        adapter.addFragment(AllSettingFragment())
        adapter.addFragment(StaticSettingFragment())
        adapter.addFragment(DynamicSettingFragment())

        binding.vpTitle.setCurrentItem(0, true)

        binding.vpTitle.adapter = adapter

//        // 使用封装类 SpinnerHelper 处理 Spinner 相关逻辑
//        val spinnerHelper = SpinnerHelper(this, binding.spEcg, R.array.spinner_items)
//        spinnerHelper.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
//            override fun onItemSelected(selectedItem: String) {
//
//            }
//
//            override fun onNothingSelected() {
//
//            }
//        })

        binding.llSave.setNoRepeatListener { //点击保存
                LiveDataBus.get().with(Constants.llSave).value = binding.vpTitle.currentItem
        }

        initClickListener()
    }

    private fun initClickListener() {

        binding.btnGeneralSettings.setNoRepeatListener {
            binding.vpTitle.currentItem = 0
        }

        binding.btnStaticSetting.setNoRepeatListener {
            binding.vpTitle.currentItem = 1
        }

        binding.btnDynamicSettings.setNoRepeatListener {
            binding.vpTitle.currentItem = 2
        }
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
