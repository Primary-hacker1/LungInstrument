package com.just.machine.ui.fragment.cardiopulmonary.dynamic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentBreatheBinding
import com.just.news.databinding.FragmentDynamicDataBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.MutableMap.MutableEntry


/**
 *create by 2024/4/2
 * 动态肺的数据界面
 *@author zt
 */
@AndroidEntryPoint
class DynamicDataFragment : CommonBaseFragment<FragmentDynamicDataBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    override fun initView() {
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicDataBinding.inflate(inflater, container, false)

}
