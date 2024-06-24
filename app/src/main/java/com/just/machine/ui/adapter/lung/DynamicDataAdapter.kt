package com.just.machine.ui.adapter.lung

import android.content.Context
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.news.R
import com.just.news.databinding.ItemDynamicDataBinding


/**
 *create by 2024/6/6
 * 动态肺数据适配器
 *@author zt
 */
class DynamicDataAdapter(val context: Context) :
    BaseRecyclerViewAdapter<CPXBreathInOutData, ItemDynamicDataBinding>() {

    private var isClick: Boolean? = false
    override fun bindData(item: CPXBreathInOutData, position: Int) {
        binding.bean = item
    }

    /**
     * @param position 点击 item 的 position
     * */
    fun toggleItemTstBackground(isClick: Boolean) {
        this.isClick = isClick
        notifyDataSetChanged()
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_dynamic_data
    }
}