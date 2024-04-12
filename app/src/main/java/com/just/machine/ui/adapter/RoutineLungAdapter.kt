package com.just.machine.ui.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.staticlung.RoutineLungBean
import com.just.news.R
import com.just.news.databinding.ItemRoutineBinding

class RoutineLungAdapter(val context: Context) :
    BaseRecyclerViewAdapter<RoutineLungBean, ItemRoutineBinding>() {

    private var isClick: Boolean? = false
    override fun bindData(item: RoutineLungBean, position: Int) {
        binding.bean = item
        if (isClick == true)
            binding.atvTest1.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))

    }

    /**
     * @param position 点击 item 的 position
     * */
    fun toggleItemTstBackground(isClick: Boolean) {
        this.isClick = isClick
        notifyDataSetChanged()
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_routine
    }
}