package com.just.machine.ui.fragment

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
 *@author zt
 */
@AndroidEntryPoint
class MeFragment : CommonBaseFragment<FragmentMeBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }


    override fun initView() {

        LogUtils.e(TAG + viewModel.itemNews.toString())


        val navController = findNavController()//fragment返回数据处理

        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")?.observe(this
            ) {
                LogUtils.e(TAG + it.toString())
            }

    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMeBinding.inflate(inflater, container, false)

}
