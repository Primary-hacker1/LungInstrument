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
import com.common.viewmodel.LiveDataEvent.Companion.FLOWS_SUCCESS
import com.common.viewmodel.LiveDataEvent.Companion.INGREDIENTS_SUCCESS
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.dao.calibration.IngredientBean
import com.just.machine.ui.adapter.calibration.ResultFlowAdapter
import com.just.machine.ui.adapter.calibration.ResultIngredientAdapter
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

        binding.rvResultFlow.layoutManager = LinearLayoutManager(requireContext())

        binding.rvResultFlow.adapter = adapterFlow

        binding.rvResultIngredient.layoutManager = LinearLayoutManager(requireContext())

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