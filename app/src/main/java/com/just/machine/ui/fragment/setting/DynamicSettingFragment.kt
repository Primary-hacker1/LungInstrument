package com.just.machine.ui.fragment.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.*
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentAllSettingBinding
import com.just.news.databinding.FragmentDynamicSettingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 运动肺设置
 *@author zt
 */
@AndroidEntryPoint
class DynamicSettingFragment : CommonBaseFragment<FragmentDynamicSettingBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    override fun loadData() {//懒加载

    }
    override fun initView() {
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicSettingBinding.inflate(inflater, container, false)
}