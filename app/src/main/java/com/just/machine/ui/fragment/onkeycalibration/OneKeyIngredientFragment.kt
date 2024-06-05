package com.just.machine.ui.fragment.onkeycalibration

import android.view.LayoutInflater
import android.view.ViewGroup
import com.common.base.CommonBaseFragment
import com.just.news.databinding.FragmentOnekeyIngredientBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 一键定标-成分
 */
@AndroidEntryPoint
class OneKeyIngredientFragment : CommonBaseFragment<FragmentOnekeyIngredientBinding>() {
    override fun loadData() {

    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentOnekeyIngredientBinding.inflate(inflater, container, false)
}