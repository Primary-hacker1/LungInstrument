package com.just.machine.ui.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.common.base.CommonBaseActivity
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.common.network.LogUtils
import com.just.machine.model.Constants
import com.just.machine.ui.fragment.FragmentPagerAdapter
import com.just.machine.ui.fragment.setting.AllSettingFragment
import com.just.machine.ui.fragment.setting.DynamicSettingFragment
import com.just.machine.ui.fragment.setting.StaticSettingFragment
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

        binding.llSave.setNoRepeatListener { //点击保存
            LiveDataBus.get().with(Constants.llSave).value = binding.vpTitle.currentItem
        }

        initClickListener()
    }

    private fun initClickListener() {

        binding.btnGeneralSettings.setNoRepeatListener {
            binding.vpTitle.currentItem = 0
            setButtonPosition(0)
        }

        binding.btnStaticSetting.setNoRepeatListener {
            binding.vpTitle.currentItem = 1
            setButtonPosition(1)
        }

        binding.btnDynamicSettings.setNoRepeatListener {
            binding.vpTitle.currentItem = 2
            setButtonPosition(2)
        }

        //这个必须写，不然会产生Fata
        binding.vpTitle.isSaveEnabled = false

        binding.vpTitle.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 当页面被选中时执行你的操作
                LogUtils.d(tag + "ViewPager2Selected page: $position")

                setButtonPosition(position)

            }
        })

    }

    private fun setButtonPosition(position: Int) {
        when (position) {
            0->{
                setButtonStyle(
                    binding.btnGeneralSettings,
                    binding.btnStaticSetting, binding.btnDynamicSettings
                )
            }
            1->{
                setButtonStyle(
                    binding.btnStaticSetting,
                    binding.btnGeneralSettings, binding.btnDynamicSettings
                )
            }
            2->{
                setButtonStyle(
                    binding.btnDynamicSettings,
                    binding.btnStaticSetting, binding.btnGeneralSettings
                )
            }

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

    private fun setButtonStyle(
        textView1: TextView,
        textView2: TextView,
        textView3: TextView,
    ) {// 设置按钮的样式

        textView1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        textView1.background =
            ContextCompat.getDrawable(this, R.drawable.super_edittext_bg)

        textView2.setTextColor(ContextCompat.getColor(this, R.color.cD9D9D9))
        textView2.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

        textView3.setTextColor(ContextCompat.getColor(this, R.color.cD9D9D9))
        textView3.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }

    override fun getViewBinding() = ActivityCardiopulmonarySettingBinding.inflate(layoutInflater)
}
