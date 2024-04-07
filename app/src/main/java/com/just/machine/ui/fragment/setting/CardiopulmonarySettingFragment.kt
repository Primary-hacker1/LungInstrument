package com.just.machine.ui.fragment.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.FragmentPagerAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentCardiopulmonarySettingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CardiopulmonarySettingFragment : CommonBaseFragment<FragmentCardiopulmonarySettingBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    override fun loadData() {

    }

    override fun initView() {
        initToolbar()

        val adapter = FragmentPagerAdapter(requireActivity())

        adapter.addFragment(AllSettingFragment())
        adapter.addFragment(StaticSettingFragment())
        adapter.addFragment(DynamicSettingFragment())


        binding.vpTitle.setCurrentItem(0, true)

        binding.vpTitle.adapter = adapter

        binding.llSave.setNoRepeatListener { //点击保存
            LiveDataBus.get().with(Constants.llSave).value = binding.vpTitle.currentItem
        }

    }

    override fun initListener() {

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
//                LogUtils.d(tag + "ViewPager2Selected page: $position")

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

    private fun setButtonStyle(
        textView1: TextView,
        textView2: TextView,
        textView3: TextView,
    ) {// 设置按钮的样式

        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        textView1.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.super_edittext_bg)

        textView2.setTextColor(ContextCompat.getColor(requireContext(), R.color.cD9D9D9))
        textView2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        textView3.setTextColor(ContextCompat.getColor(requireContext(), R.color.cD9D9D9))
        textView3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun initToolbar() {
        binding.toolbar.title = getString(R.string.system_setting)//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.visible()
        binding.toolbar.ivTitleBack.setNoRepeatListener {
            popBackStack()
        }

    }

    private fun popBackStack(){
        val navController = findNavController()//fragment返回数据处理
        navController.previousBackStackEntry?.savedStateHandle?.set("key", "返回")
        navController.popBackStack()
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCardiopulmonarySettingBinding.inflate(inflater, container, false)
}
