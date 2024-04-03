package com.just.machine.ui.fragment.cardiopulmonary.static

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentDynamicDataBinding
import com.just.news.databinding.FragmentLungBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/4/2
 * 静态肺的常规
 *@author zt
 */
@AndroidEntryPoint
class RoutineLungFragment : CommonBaseFragment<FragmentLungBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }


    override fun initView() {
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLungBinding.inflate(inflater, container, false)

}
