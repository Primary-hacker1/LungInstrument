package com.just.machine.ui.fragment.cardiopulmonary.static

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.network.LogUtils
import com.just.machine.model.Constants
import com.just.news.R
import com.just.news.databinding.FragmentMeBinding
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentMainBinding
import com.just.news.databinding.FragmentRoutineBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/4/2
 * 静态肺的分钟最大通气量
 *@author zt
 */
@AndroidEntryPoint
class MaximalVentilationFragment : CommonBaseFragment<FragmentMainBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }


    override fun initView() {

    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

}
