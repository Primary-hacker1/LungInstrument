package com.just.machine.ui.fragment.calibration

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
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


        val adapter = FragmentPagerAdapter(activity!!)
        // 添加三个 Fragment
        adapter.addFragment(EnvironmentalFragment())
        adapter.addFragment(FlowFragment())
        adapter.addFragment(IngredientFragment())
        adapter.addFragment(CalibrationResultFragment())

        onButtonClick(binding.btnEnvironment, 0)

        binding.vpCalibration.adapter = adapter

        binding.vpCalibration.isUserInputEnabled = false

        binding.vpCalibration.orientation = ViewPager2.ORIENTATION_VERTICAL // 设置垂直方向滑动

    }

    private fun onButtonClick(button: AppCompatButton, position: Int) {
        binding.vpCalibration.currentItem = position// 切换ViewPager页面
        resetButtonColors()// 切换按钮颜色
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cF5FCFF))
    }

    private fun resetButtonColors() {
        binding.btnEnvironment.setBackgroundColor(Color.WHITE)
        binding.btnFlow.setBackgroundColor(Color.WHITE)
        binding.btnIngredient.setBackgroundColor(Color.WHITE)
        binding.btnResult.setBackgroundColor(Color.WHITE)
        binding.btnCalibrationClose.setBackgroundColor(Color.WHITE)
    }

    override fun initListener() {
        binding.toolbar.ivTitleBack.setNoRepeatListener {
            Navigation.findNavController(it).popBackStack()
        }

        binding.btnEnvironment.setNoRepeatListener {
            onButtonClick(binding.btnEnvironment, 0)
        }

        binding.btnFlow.setNoRepeatListener {
            onButtonClick(binding.btnFlow, 1)
        }

        binding.btnIngredient.setNoRepeatListener {
            onButtonClick(binding.btnIngredient, 2)
        }

        binding.btnResult.setNoRepeatListener {
            onButtonClick(binding.btnResult, 3)
        }

        binding.btnCalibrationClose.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCalibrationBinding.inflate(inflater, container, false)
}