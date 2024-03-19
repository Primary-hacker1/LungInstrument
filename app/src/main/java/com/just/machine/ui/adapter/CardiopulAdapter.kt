package com.just.machine.ui.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.CardiopulmonaryRecordsBean
import com.just.news.R
import com.just.news.databinding.ItemLayoutCardiopulTestBinding

/**
 *create by 2024/3/6
 * 患者心肺测试数据
 *@author zt
 */
class CardiopulAdapter(var context: Context)
    : BaseRecyclerViewAdapter<CardiopulmonaryRecordsBean, ItemLayoutCardiopulTestBinding>() {

    private var selectedItem = -1
    override fun bindData(item: CardiopulmonaryRecordsBean, position: Int) {
        binding.item = item
        if (position == selectedItem) {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))
        } else {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_layout_cardiopul_test
    }

    /**
     * @param position 点击 item 的 position
     * */
    fun toggleItemBackground(position: Int) {
        if (selectedItem == position) {
            selectedItem = -1
        } else {
            selectedItem = position
        }
        notifyDataSetChanged()
    }

}