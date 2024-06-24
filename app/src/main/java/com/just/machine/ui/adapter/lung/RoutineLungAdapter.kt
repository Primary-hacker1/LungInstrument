package com.just.machine.ui.adapter.lung

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.lungdata.RoutineLungBean
import com.just.news.R
import com.just.news.databinding.ItemRoutineBinding


/**
 *create by 2024/6/6
 * 常规肺适配器
 *@author zt
 */
class RoutineLungAdapter(val context: Context) :
    BaseRecyclerViewAdapter<RoutineLungBean, ItemRoutineBinding>() {

    private var isClick: Boolean = false
    private var isUnfold: Boolean = false

    override fun bindData(item: RoutineLungBean, position: Int) {
        binding.bean = item
        if (isClick) {
            binding.atvTest1.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))
        }
    }

    fun isTestUnfold(isUnfold: Boolean) {
        for (index in items) {
            if (isUnfold) {
                index.isVisible = View.VISIBLE
            } else {
                index.isVisible = View.GONE
            }
        }
        notifyDataSetChanged()
    }

    /**
     * @param isClick 点击 item 的状态
     */
    fun toggleItemTestBackground(isClick: Boolean) {
        this.isClick = isClick
        notifyDataSetChanged()
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_routine
    }
}
