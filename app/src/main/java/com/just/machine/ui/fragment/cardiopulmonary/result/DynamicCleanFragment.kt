package com.just.machine.ui.fragment.cardiopulmonary.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.just.machine.ui.fragment.cardiopulmonary.DynamicResultFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentDynamicCleanBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 运动终止fragment
 *@author zt
 */
@AndroidEntryPoint
class DynamicCleanFragment : CommonBaseFragment<FragmentDynamicCleanBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    override fun initView() {

        val fragment = parentFragment

        if (fragment is DynamicResultFragment){//保存
            fragment.onSaveCLick().setNoRepeatListener {
                LogUtils.d(tag+"onClick")
            }
            fragment.onResetCLick().setNoRepeatListener { //重置

            }
        }

    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicCleanBinding.inflate(inflater, container, false)

}
