package com.just.machine.ui.fragment.calibration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.toast
import com.common.base.visible
import com.just.machine.model.Constants
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.activity.MainActivity
import com.just.machine.ui.fragment.FragmentPagerAdapter
import com.just.machine.ui.fragment.setting.AllSettingFragment
import com.just.machine.ui.fragment.setting.DynamicSettingFragment
import com.just.machine.ui.fragment.setting.StaticSettingFragment
import com.just.news.databinding.FragmentLoginBinding
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentCalibrationBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 定标界面
 *@author zt
 */
@AndroidEntryPoint
class CalibrationFragment : CommonBaseFragment<FragmentCalibrationBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun initView() {
        binding.toolbar.title = Constants.cardiopulmonary//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.visible()
        binding.toolbar.ivTitleBack.setNoRepeatListener {
            activity?.finish()
        }

        val adapter = FragmentPagerAdapter(activity!!)
        // 添加三个 Fragment
        adapter.addFragment(EnvironmentalFragment())
        adapter.addFragment(FlowFragment())
        adapter.addFragment(IngredientFragment())
        adapter.addFragment(CalibrationResultFragment())

        binding.vpCalibration.setCurrentItem(0, true)

        binding.vpCalibration.adapter = adapter

    }

    override fun initListener() {


    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCalibrationBinding.inflate(inflater, container, false)
}