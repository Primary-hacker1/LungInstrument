package com.just.machine.ui.fragment.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.viewModels
import com.common.base.*
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentDynamicSettingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 运动肺设置
 *@author zt
 */
@AndroidEntryPoint
class DynamicSettingFragment : CommonBaseFragment<FragmentDynamicSettingBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    override fun loadData() {//懒加载

    }

    override fun initView() {

        // 设置复选框监听器
        setCheckBoxListener(binding.checkboxOption1) { isChecked ->
            // 处理复选框 1 的状态变化
            if (isChecked) {
                // 复选框 1 被选中
            } else {
                // 复选框 1 被取消选中
            }
        }
    }

    private fun setCheckBoxListener(checkBoxId: CheckBox, listener: (Boolean) -> Unit) {
        checkBoxId.setOnCheckedChangeListener { _, isChecked -> listener(isChecked) }
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicSettingBinding.inflate(inflater, container, false)
}