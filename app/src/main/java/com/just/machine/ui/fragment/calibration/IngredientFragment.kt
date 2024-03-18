package com.just.machine.ui.fragment.calibration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentFlowBinding
import com.just.news.databinding.FragmentIngredientBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 成分定标
 *@author zt
 */
@AndroidEntryPoint
class IngredientFragment : CommonBaseFragment<FragmentIngredientBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun initView() {

    }

    override fun initListener() {

    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentIngredientBinding.inflate(inflater, container, false)
}