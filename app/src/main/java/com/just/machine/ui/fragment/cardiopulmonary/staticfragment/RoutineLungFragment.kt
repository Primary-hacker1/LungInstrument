package com.just.machine.ui.fragment.cardiopulmonary.staticfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.common.base.CommonBaseFragment
import com.just.news.databinding.FragmentRoutineBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/4/2
 * 静态肺的用力呼吸
 *@author zt
 */
@AndroidEntryPoint
class RoutineLungFragment : CommonBaseFragment<FragmentRoutineBinding>() {

    override fun loadData() {//懒加载

    }


    override fun initView() {
        val layout = binding.fragmentLayout
        layout.setInitView("FVC")
    }

    override fun initListener() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRoutineBinding.inflate(inflater, container, false)

}
