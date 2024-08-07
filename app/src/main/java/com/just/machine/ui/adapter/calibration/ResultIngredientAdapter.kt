package com.just.machine.ui.adapter.calibration

import android.content.Context
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.dao.calibration.IngredientBean
import com.just.machine.dao.calibration.IngredientCalibrationResultBean
import com.just.news.R
import com.just.news.databinding.ItemResultIngredientBinding

/**
 *create by 2024/6/6
 * 成分定标适配器
 *@author zt
 */
class ResultIngredientAdapter(val context: Context) :
    BaseRecyclerViewAdapter<IngredientCalibrationResultBean, ItemResultIngredientBinding>() {

    private var selectedItem = -1

    override fun bindData(item: IngredientCalibrationResultBean, position: Int) {
        binding.bean = item

        if (position == selectedItem) {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))
        } else {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        if (item.calibrationResult?.isNotEmpty() == true && item.calibrationResult == "1") {
            binding.tvIngredientResult.setTextColor(ContextCompat.getColor(context, R.color.text3))
            binding.tvIngredientResult.text = "通过"
        } else {
            binding.tvIngredientResult.setTextColor(ContextCompat.getColor(context, R.color.red))
            binding.tvIngredientResult.text = "未通过"
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