package com.just.machine.ui.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.staticlung.RoutineLungBean
import com.just.news.R
import com.just.news.databinding.ItemRoutineBinding

class RoutineLungAdapter(val context: Context) :
    BaseRecyclerViewAdapter<RoutineLungBean, ItemRoutineBinding>() {

    private var selectedItem = -1

    override fun bindData(item: RoutineLungBean, position: Int) {
        binding.bean = item

        if (position == selectedItem) {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))
        } else {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
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


    override fun getLayoutRes(): Int {
        return R.layout.item_routine
    }
}