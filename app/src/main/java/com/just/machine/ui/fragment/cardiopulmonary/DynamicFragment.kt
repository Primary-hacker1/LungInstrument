package com.just.machine.ui.fragment.cardiopulmonary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.common.base.CommonBaseFragment
import com.common.viewmodel.LiveDataEvent
import com.just.machine.ui.adapter.CustomSpinnerAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.news.databinding.FragmentDynamicBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 动态肺测试
 *@author zt
 */
@AndroidEntryPoint
class DynamicFragment : CommonBaseFragment<FragmentDynamicBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    private fun initToolbar() {

    }

    override fun initView() {
        initToolbar()

        val adapterTime = CustomSpinnerAdapter(requireContext())
        val adapterVo = CustomSpinnerAdapter(requireContext())
        val adapterHr = CustomSpinnerAdapter(requireContext())
        val adapterRer = CustomSpinnerAdapter(requireContext())
        val adapterO2Hr = CustomSpinnerAdapter(requireContext())
        val adapterFio2 = CustomSpinnerAdapter(requireContext())
        val adapterVdVt = CustomSpinnerAdapter(requireContext())
        val adapterTex = CustomSpinnerAdapter(requireContext())
        val adapterFeo2 = CustomSpinnerAdapter(requireContext())

        binding.timeSpinner.adapter = adapterTime
        binding.voSpinner.adapter = adapterVo
        binding.hrSpinner.adapter = adapterHr
        binding.rerSpinner.adapter = adapterRer
        binding.o2hrSpinner.adapter = adapterO2Hr
        binding.fioSpinner.adapter = adapterFio2
        binding.vdSpinner.adapter = adapterVdVt
        binding.texSpinner.adapter = adapterTex
        binding.feoSpinner.adapter = adapterFeo2

        LiveDataBus.get().with("msg").observe(this) {//串口消息
            adapterTime.updateSpinnerText("text")

        }
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicBinding.inflate(inflater, container, false)

}
