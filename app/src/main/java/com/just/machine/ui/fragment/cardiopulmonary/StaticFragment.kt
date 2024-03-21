package com.just.machine.ui.fragment.cardiopulmonary

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
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 静态肺测试
 *@author zt
 */
@AndroidEntryPoint
class StaticFragment : CommonBaseFragment<FragmentMeBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    private fun initToolbar() {
        binding.toolbar.title = Constants.me//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.gone()

    }

    override fun initView() {
        initToolbar()
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMeBinding.inflate(inflater, container, false)

}
