package com.just.machine.ui.fragment.calibration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.just.machine.model.Constants
import com.just.machine.model.EnvironmentalBean
import com.just.machine.ui.adapter.calibration.EnvironmentalAdapter
import com.just.machine.ui.adapter.setting.SVCSettingAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentCalibrationBinding
import com.just.news.databinding.FragmentEnvironmentalBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 环境定标
 *@author zt
 */
@AndroidEntryPoint
class EnvironmentalFragment : CommonBaseFragment<FragmentEnvironmentalBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val adapter by lazy { EnvironmentalAdapter(requireContext()) }

    override fun initView() {
        binding.rvEnvironmental.layoutManager = LinearLayoutManager(requireContext())

        adapter.setItemsBean(
            mutableListOf(
                EnvironmentalBean(
                    "2024", "20", "9", "1"
                ), EnvironmentalBean(
                    "2024", "20", "9", "1"
                ), EnvironmentalBean(
                    "2024", "20", "9", "1"
                )
            )
        )

        adapter.setItemClickListener { _, position ->
            adapter.toggleItemBackground(position)
        }

        binding.rvEnvironmental.adapter = adapter

        binding.llStart.setNoRepeatListener {

        }

        binding.llSave.setNoRepeatListener {

        }
    }

    override fun initListener() {

    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEnvironmentalBinding.inflate(inflater, container, false)
}