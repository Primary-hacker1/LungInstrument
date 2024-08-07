package com.just.machine.ui.fragment.cardiopulmonary.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentSlopeBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/6/19
 * 斜率分析
 *@author zt
 */
@Deprecated("这版本不用开发！")
@AndroidEntryPoint
class SlopeFragment : CommonBaseFragment<FragmentSlopeBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    override fun initView() {

        binding.llSave.setNoRepeatListener {

        }

        binding.llReset.setNoRepeatListener {

        }
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSlopeBinding.inflate(inflater, container, false)

}
