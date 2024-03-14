package com.just.machine.ui.fragment.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.common.base.*
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FrgamentStaticSettingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 静态肺设置
 *@author zt
 */
@AndroidEntryPoint
class StaticSettingFragment : CommonBaseFragment<FrgamentStaticSettingBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    override fun loadData() {//懒加载

    }
    override fun initView() {

    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FrgamentStaticSettingBinding.inflate(inflater, container, false)
}