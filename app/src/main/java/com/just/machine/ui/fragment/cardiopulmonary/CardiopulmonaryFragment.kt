package com.just.machine.ui.fragment.cardiopulmonary

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.common.network.LogUtils
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.FragmentChildAdapter
import com.just.machine.ui.adapter.FragmentPagerAdapter
import com.just.machine.ui.fragment.NewFragment
import com.just.machine.ui.fragment.calibration.CalibrationFragment
import com.just.machine.ui.fragment.calibration.EnvironmentalFragment
import com.just.machine.ui.fragment.setting.CardiopulmonarySettingFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentCardiopulmonaryBinding
import com.justsafe.libview.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 心肺测试
 *@author zt
 */
@AndroidEntryPoint
class CardiopulmonaryFragment : CommonBaseFragment<FragmentCardiopulmonaryBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    private fun initToolbar() {
        binding.toolbar.tvRight.setTime(
            System.currentTimeMillis(),
            DateUtils.nowTimeDataString
        )

        binding.toolbar.title = Constants.cardiopulmonary//标题
//        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.visible()
    }

    override fun initView() {
        initToolbar()

        val adapter = FragmentChildAdapter(this)

        adapter.addFragment(NewFragment())//随便加个fragment

        adapter.addFragment(StaticLungFragment())

        adapter.addFragment(DynamicFragment())

        adapter.addFragment(DynamicResultFragment())

        adapter.addFragment(NewFragment())

        binding.vpCardiopulmonary.adapter = adapter

        onButtonClick(binding.btnStatic, 1)

        binding.vpCardiopulmonary.isUserInputEnabled = false

        binding.vpCardiopulmonary.orientation = ViewPager2.ORIENTATION_VERTICAL // 设置垂直方向滑动
    }

    override fun initListener() {

        binding.btnEnvironment.setOnClickListener {
            navigate(it, R.id.calibrationFragment)//fragment跳转
        }

        binding.btnStatic.setOnClickListener {
            onButtonClick(binding.btnStatic, 1)
        }

        binding.btnDynamic.setOnClickListener {
            onButtonClick(binding.btnDynamic, 2)
        }

        binding.btnDynamicResult.setOnClickListener {//运动评估
            onButtonClick(binding.btnDynamicResult, 3)
        }

        binding.btnSetting.setNoRepeatListener {
            navigate(it, R.id.cardiopulmonarySettingFragment)
        }

        binding.btnClose.setNoRepeatListener {
            onButtonClick(binding.btnClose, 5)
            activity?.finish()
        }

        binding.toolbar.ivTitleBack.setNoRepeatListener {
            activity?.finish()
        }

        val navController = findNavController()//fragment返回数据处理

        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")?.observe(
            this
        ) {
            onButtonClick(binding.btnStatic, 1)
        }

    }

    private fun onButtonClick(button: AppCompatButton, position: Int) {
        binding.vpCardiopulmonary.currentItem = position// 切换ViewPager页面
        resetButtonColors()// 切换按钮颜色
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cF5FCFF))
    }

    private fun resetButtonColors() {
        binding.btnEnvironment.setBackgroundColor(Color.WHITE)
        binding.btnStatic.setBackgroundColor(Color.WHITE)
        binding.btnDynamic.setBackgroundColor(Color.WHITE)
        binding.btnDynamicResult.setBackgroundColor(Color.WHITE)
        binding.btnSetting.setBackgroundColor(Color.WHITE)
        binding.btnClose.setBackgroundColor(Color.WHITE)
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCardiopulmonaryBinding.inflate(inflater, container, false)

}
