package com.just.machine.ui.fragment.calibration.result

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.common.viewmodel.LiveDataEvent.Companion.FLOWS_SUCCESS
import com.common.viewmodel.LiveDataEvent.Companion.INGREDIENTS_SUCCESS
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.dao.calibration.IngredientBean
import com.just.machine.ui.adapter.FragmentChildAdapter
import com.just.machine.ui.adapter.calibration.ResultFlowAdapter
import com.just.machine.ui.adapter.calibration.ResultIngredientAdapter
import com.just.machine.ui.fragment.setting.AllSettingFragment
import com.just.machine.ui.fragment.setting.DynamicSettingFragment
import com.just.machine.ui.fragment.setting.StaticSettingFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentCalibrationResultBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/6/19
 * 定标结果界面
 *@author zt
 */
@AndroidEntryPoint
class CalibrationResultFragment : CommonBaseFragment<FragmentCalibrationResultBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val adapterFlow by lazy { ResultFlowAdapter(requireContext()) }

    private val adapterIngredient by lazy { ResultIngredientAdapter(requireContext()) }

    override fun initView() {

        val adapter = FragmentChildAdapter(this)

        adapter.addFragment(CalibrationFlowHandleResultFragment(), CalibrationFlowHandleResultFragment::class.simpleName)
        adapter.addFragment(CalibrationFlowAutoResultFragment(), CalibrationFlowAutoResultFragment::class.simpleName)
        adapter.addFragment(CalibrationIngredientResultFragment(), CalibrationIngredientResultFragment::class.simpleName)


        binding.vpCalibrationResult.setCurrentItem(0, true)

        binding.vpCalibrationResult.adapter = adapter

        binding.btnFlowHandle.setNoRepeatListener {
            setButtonStyle(
                binding.btnFlowHandle,
                binding.btnFlowAuto,
                binding.btnIngredient
            )
        }

        binding.btnFlowAuto.setNoRepeatListener {
            setButtonStyle(
                binding.btnFlowAuto,
                binding.btnFlowHandle,
                binding.btnIngredient
            )
        }


        binding.btnIngredient.setNoRepeatListener {
            setButtonStyle(
                binding.btnIngredient,
                binding.btnFlowHandle,
                binding.btnFlowAuto
            )
        }

        viewModel.getIngredients()

        viewModel.getFlows()

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                FLOWS_SUCCESS -> {
                    val flowsBean: MutableList<FlowBean> = ArrayList()
                    if (it.any is List<*>) {
                        val list = it.any as List<*>
                        for (index in list) {
                            if (index is FlowBean) {
                                flowsBean.add(index)
                            }
                        }
                    }
                    adapterFlow.setItemsBean(flowsBean)
                }
                INGREDIENTS_SUCCESS -> {
                    val flowsBean: MutableList<IngredientBean> = ArrayList()
                    if (it.any is List<*>) {
                        val list = it.any as List<*>
                        for (index in list) {
                            if (index is IngredientBean) {
                                flowsBean.add(index)
                            }
                        }
                    }
                    adapterIngredient.setItemsBean(flowsBean)
                }
            }
        }

    }

    override fun initListener() {
        binding.btnFlowHandle.setNoRepeatListener {
            binding.vpCalibrationResult.currentItem = 0
        }

        binding.btnFlowAuto.setNoRepeatListener {
            binding.vpCalibrationResult.currentItem = 1
        }

        binding.btnIngredient.setNoRepeatListener {
            binding.vpCalibrationResult.currentItem = 2
        }

        //这个必须写，不然会产生Fata
        binding.vpCalibrationResult.isSaveEnabled = false

        binding.vpCalibrationResult.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 当页面被选中时执行你的操作
                setButtonPosition(position)
            }
        })
    }

    private fun setButtonPosition(position: Int) {
        when (position) {
            0 -> {
                setButtonStyle(
                    binding.btnFlowHandle,
                    binding.btnFlowAuto,
                    binding.btnIngredient
                )
            }

            1 -> {
                setButtonStyle(
                    binding.btnFlowAuto,
                    binding.btnFlowHandle,
                    binding.btnIngredient
                )
            }

            2 -> {
                setButtonStyle(
                    binding.btnIngredient,
                    binding.btnFlowHandle,
                    binding.btnFlowAuto
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

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCalibrationResultBinding.inflate(inflater, container, false)
}