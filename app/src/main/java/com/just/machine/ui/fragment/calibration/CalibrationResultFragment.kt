package com.just.machine.ui.fragment.calibration

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.just.machine.model.calibration.IngredientBean
import com.just.machine.model.calibration.ResultBean
import com.just.machine.ui.adapter.calibration.IngredientAdapter
import com.just.machine.ui.adapter.calibration.ResultFlowAdapter
import com.just.machine.ui.adapter.calibration.ResultIngredientAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentCalibrationResultBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 定标结果界面
 *@author zt
 */
@AndroidEntryPoint
class CalibrationResultFragment : CommonBaseFragment<FragmentCalibrationResultBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val adapterFlow by lazy { ResultFlowAdapter(requireContext()) }

    private val adapterIngredient by lazy { ResultIngredientAdapter(requireContext()) }

    override fun initView() {

        binding.rvResultFlow.layoutManager = LinearLayoutManager(requireContext())

        adapterFlow.setItemsBean(
            mutableListOf
                (ResultBean("2021-8-13", "0.012", "0.013"))
        )

        binding.rvResultFlow.adapter = adapterFlow

        binding.rvResultIngredient.layoutManager = LinearLayoutManager(requireContext())

        adapterIngredient.setItemsBean(
            mutableListOf
                (ResultBean("2021-8-13", "0.012", "0.013"))
        )

        binding.rvResultIngredient.adapter = adapterIngredient



        binding.btnFlow.setNoRepeatListener {
            setButtonStyle(
                binding.btnIngredient,
                binding.btnFlow,
                binding.rvResultIngredient,
                binding.rvResultFlow
            )
        }

        binding.btnIngredient.setNoRepeatListener {
            setButtonStyle(
                binding.btnFlow,
                binding.btnIngredient,
                binding.rvResultFlow,
                binding.rvResultIngredient
            )
        }

    }

    override fun initListener() {

    }

    private fun setButtonStyle(
        textView1: TextView,
        textView2: TextView,
        showRecyclerView: RecyclerView,
        hideRecyclerView: RecyclerView
    ) {// 设置按钮的样式

        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.cD9D9D9))
        textView2.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        textView1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        textView2.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.super_edittext_bg)
        showRecyclerView.gone()
        hideRecyclerView.visible()
    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCalibrationResultBinding.inflate(inflater, container, false)
}