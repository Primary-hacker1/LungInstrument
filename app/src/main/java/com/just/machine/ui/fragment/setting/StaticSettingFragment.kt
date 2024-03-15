package com.just.machine.ui.fragment.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.*
import com.just.machine.model.setting.FvcSettingBean
import com.just.machine.model.setting.MvvSettingBean
import com.just.machine.model.setting.SvcSettingBean
import com.just.machine.ui.adapter.setting.FVCSettingAdapter
import com.just.machine.ui.adapter.setting.MVVSettingAdapter
import com.just.machine.ui.adapter.setting.SVCSettingAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FrgamentStaticSettingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2018/6/19
 * 静态肺设置
 *@author zt
 */
@AndroidEntryPoint
class StaticSettingFragment : CommonBaseFragment<FrgamentStaticSettingBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val adapterSvc by lazy { SVCSettingAdapter(requireContext()) }

    private val adapterFvc by lazy { FVCSettingAdapter(requireContext()) }

    private val adapterMvv by lazy { MVVSettingAdapter(requireContext()) }
    override fun loadData() {//懒加载

    }

    override fun initView() {

        binding.rvSvc.layoutManager = LinearLayoutManager(context)
        binding.rvFvc.layoutManager = LinearLayoutManager(context)
        binding.rvMvv.layoutManager = LinearLayoutManager(context)

        adapterSvc.setItemsBean(
            mutableListOf(
                SvcSettingBean("1", "2", "3", true),
                SvcSettingBean("2", "2", "3", true),
                SvcSettingBean("3", "2", "3", true),
                SvcSettingBean("4", "2", "3", true),
                SvcSettingBean("5", "2", "3", true),
                SvcSettingBean("6", "2", "3", true),
                SvcSettingBean("7", "2", "3", true),
                SvcSettingBean("8", "2", "3", true),
                SvcSettingBean("9", "2", "3", true),
                SvcSettingBean("10", "2", "3", true),
                SvcSettingBean("11", "5", "6", true)
            )
        )

        binding.rvSvc.adapter = adapterSvc


        adapterFvc.setItemsBean(
            mutableListOf(
                FvcSettingBean("1", "2", "3", true),
                FvcSettingBean("4", "5", "6", true)
            )
        )

        binding.rvFvc.adapter = adapterFvc


        adapterMvv.setItemsBean(
            mutableListOf(
                MvvSettingBean("1", "2", "3", true),
                MvvSettingBean("4", "5", "6", true)
            )
        )

        binding.rvMvv.adapter = adapterMvv

    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FrgamentStaticSettingBinding.inflate(inflater, container, false)
}