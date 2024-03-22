package com.just.machine.ui.fragment.cardiopulmonary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentStaticBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 静态肺测试
 *@author zt
 */
@AndroidEntryPoint
class StaticFragment : CommonBaseFragment<FragmentStaticBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    private fun initToolbar() {

    }

    override fun initView() {
        initToolbar()
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStaticBinding.inflate(inflater, container, false)

}
