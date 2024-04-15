package com.just.machine.ui.fragment.cardiopulmonary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.FragmentChildAdapter
import com.just.machine.ui.fragment.cardiopulmonary.result.DynamicCleanFragment
import com.just.machine.ui.fragment.cardiopulmonary.staticfragment.BreatheHardInFragment
import com.just.machine.ui.fragment.cardiopulmonary.staticfragment.MaxVentilationFragment
import com.just.machine.ui.fragment.cardiopulmonary.staticfragment.RoutineLungFragment
import com.just.news.databinding.FragmentMeBinding
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.news.databinding.FragmentDynamicResultBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 运动评估
 *@author zt
 */
@AndroidEntryPoint
class DynamicResultFragment : CommonBaseFragment<FragmentDynamicResultBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    override fun initView() {

        val adapter = FragmentChildAdapter(this)

        adapter.addFragment(DynamicCleanFragment())

        binding.viewpager.setCurrentItem(1, true)

        binding.viewpager.adapter = adapter

        binding.viewpager.isUserInputEnabled = false

    }

    override fun initListener() {

    }

    fun onSaveCLick():LinearLayout{
        return binding.llSave
    }

    fun onResetCLick():LinearLayout{
        return binding.llReset
    }
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicResultBinding.inflate(inflater, container, false)

}
