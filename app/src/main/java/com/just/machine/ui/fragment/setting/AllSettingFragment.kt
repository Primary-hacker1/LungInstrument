package com.just.machine.ui.fragment.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.*
import com.common.network.LogUtils
import com.just.machine.model.Constants
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.machine.util.SpinnerHelper
import com.just.news.R
import com.just.news.databinding.FragmentAllSettingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 *@author zt
 */
@AndroidEntryPoint
class AllSettingFragment : CommonBaseFragment<FragmentAllSettingBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    override fun loadData() {//懒加载

    }

    override fun initView() {
    }

    override fun initListener() {

        val spScenarios =
            SpinnerHelper(requireContext(), binding.spScenarios, R.array.spinner_items)
        spScenarios.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {

            }

            override fun onNothingSelected() {

            }
        })

        val spBreathing =
            SpinnerHelper(requireContext(), binding.spBreathing, R.array.spinner_items)
        spBreathing.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {

            }

            override fun onNothingSelected() {

            }
        })

        val spEcg = SpinnerHelper(requireContext(), binding.spEcg, R.array.spinner_items)
        spEcg.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {

            }

            override fun onNothingSelected() {

            }
        })


        LiveDataBus.get().with(Constants.llSave).observe(this) {
            if (it == 0) {
                LogUtils.d(tag + it.toString())
            }
        }

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAllSettingBinding.inflate(inflater, container, false)
}