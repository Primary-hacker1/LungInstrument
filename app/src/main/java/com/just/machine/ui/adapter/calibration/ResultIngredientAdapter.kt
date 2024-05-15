package com.just.machine.ui.adapter.calibration

import android.content.Context
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.dao.calibration.IngredientBean
import com.just.news.R
import com.just.news.databinding.ItemResultIngredientBinding

class ResultIngredientAdapter(val context: Context) :
    BaseRecyclerViewAdapter<IngredientBean, ItemResultIngredientBinding>() {

    private var selectedItem = -1

    override fun bindData(item: IngredientBean, position: Int) {
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
        selectedItem = if (selectedItem == position) {
            -1
        } else {
            position
        }
        notifyDataSetChanged()
    }


    override fun getLayoutRes(): Int {
        return R.layout.item_result_ingredient
    }
}