package com.just.machine.ui.adapter.result

import android.content.Context
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.result.DynamicResultBean
import com.just.news.R
import com.just.news.databinding.ItemResultBinding

/**
 *create by 2024/3/15
 * 运动评估数据表
 *@author zt
 */
class ResultAdapter(val context: Context) :
    BaseRecyclerViewAdapter<DynamicResultBean, ItemResultBinding>() {

    override fun bindData(item: DynamicResultBean, position: Int) {
        binding.item = item

        if (position % 2 != 0) {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))
        } else {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

    }

    override fun getLayoutRes(): Int {
        return R.layout.item_result
    }
}