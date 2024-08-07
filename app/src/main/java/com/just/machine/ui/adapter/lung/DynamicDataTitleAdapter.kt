package com.just.machine.ui.adapter.lung

import android.content.Context
import com.common.base.BaseRecyclerViewAdapter
import com.just.news.R
import com.just.news.databinding.ItemDynamicDataTitleBinding

/**
 *create by 2024/6/6
 * 动态肺数据标题适配器
 *@author zt
 */
class DynamicDataTitleAdapter(val context: Context) :
    BaseRecyclerViewAdapter<String, ItemDynamicDataTitleBinding>() {

    private var isClick: Boolean? = false
    override fun bindData(item: String, position: Int) {
        binding.title = item
    }

    /**
     * @param position 点击 item 的 position
     * */
    fun toggleItemTstBackground(isClick: Boolean) {
        this.isClick = isClick
        notifyDataSetChanged()
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_dynamic_data_title
    }
}