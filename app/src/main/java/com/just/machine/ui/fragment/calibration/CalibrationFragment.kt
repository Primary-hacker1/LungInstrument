package com.just.machine.ui.fragment.calibration

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
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.FragmentChildAdapter
import com.just.machine.ui.fragment.calibration.onekeycalibration.OneKeyCalibrationFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentCalibrationBinding
import com.justsafe.libview.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/6/19
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

        binding.toolbarCardi.title = Constants.cardiopulmonary//标题
        binding.toolbarCardi.tvRight.setTime(
            System.currentTimeMillis(),
            DateUtils.nowTimeDataString
        )

        val adapter = FragmentChildAdapter(this)

        adapter.addFragment(EnvironmentalFragment())
        adapter.addFragment(FlowFragment())
        adapter.addFragment(IngredientFragment())
        adapter.addFragment(OneKeyCalibrationFragment())
        adapter.addFragment(CalibrationResultFragment())


        onButtonClick(binding.btnEnvironment, 0)

        binding.vpCalibration.adapter = adapter

        binding.vpCalibration.isUserInputEnabled = false

        binding.vpCalibration.orientation = ViewPager2.ORIENTATION_VERTICAL // 设置垂直方向滑动
        binding.vpCalibration.offscreenPageLimit = 3
    }

    private fun onButtonClick(button: AppCompatButton, position: Int) {
        binding.vpCalibration.setCurrentItem(position,true)// 切换ViewPager页面，如果设置成true会出现直接点击一键定标，但是显示的是定标结果Fragment
        resetButtonColors()// 切换按钮颜色
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cF5FCFF))
    }

    private fun resetButtonColors() {
        binding.btnEnvironment.setBackgroundColor(Color.WHITE)
        binding.btnFlow.setBackgroundColor(Color.WHITE)
        binding.btnIngredient.setBackgroundColor(Color.WHITE)
        binding.btnOneKey.setBackgroundColor(Color.WHITE)
        binding.btnResult.setBackgroundColor(Color.WHITE)
        binding.btnCalibrationClose.setBackgroundColor(Color.WHITE)
    }

    override fun initListener() {
        binding.toolbar.ivTitleBack.setNoRepeatListener {
            popBackStack()
        }
        binding.toolbarCardi.ibBack.setNoRepeatListener {
            popBackStack()
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

        binding.btnOneKey.setNoRepeatListener {
            onButtonClick(binding.btnOneKey, 3)
        }

        binding.btnResult.setNoRepeatListener {
            onButtonClick(binding.btnResult, 4)
        }

        binding.btnCalibrationClose.setOnClickListener {
            popBackStack()
        }
    }

    private fun popBackStack(){
        val navController = findNavController()//fragment返回数据处理
        navController.previousBackStackEntry?.savedStateHandle?.set("key", "返回")
        navController.popBackStack()
    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCalibrationBinding.inflate(inflater, container, false)
}