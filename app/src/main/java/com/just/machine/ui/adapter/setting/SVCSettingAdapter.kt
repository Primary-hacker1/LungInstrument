package com.just.machine.ui.adapter.setting

import android.content.Context
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.CPETParameter
import com.just.news.R
import com.just.news.databinding.ItemSvcSettingBinding

/**
 *create by 2024/3/15
 * 静态肺设置列表
 *@author zt
 */
class SVCSettingAdapter(val context: Context) :
    BaseRecyclerViewAdapter<CPETParameter, ItemSvcSettingBinding>() {

    override fun bindData(item: CPETParameter, position: Int) {
        binding.item = item

        if (position % 2 != 0) {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))
        } else {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        binding.checkbox.setOnCheckedChangeListener(null) // 首先移除之前的监听器，以避免冲突

        binding.checkbox.isChecked = item.isShow!!

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            item.isShow = isChecked
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_svc_setting
    }
}