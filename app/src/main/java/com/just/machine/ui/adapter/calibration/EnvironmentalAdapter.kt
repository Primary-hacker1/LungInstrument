package com.just.machine.ui.adapter.calibration

import android.content.Context
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.dao.calibration.EnvironmentalCalibrationBean
import com.just.news.R
import com.just.news.databinding.ItemEnvironmentalBinding

/**
 *create by 2024/6/6
 * 环境定标适配器
 *@author zt
 */
class EnvironmentalAdapter(val context: Context) :
    BaseRecyclerViewAdapter<EnvironmentalCalibrationBean, ItemEnvironmentalBinding>() {

    private var selectedItem = -1

    override fun bindData(item: EnvironmentalCalibrationBean, position: Int) {
        binding.bean = item

        if (position == selectedItem) {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))
        } else {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        if(item.calibrationType == "0"){
            binding.atvCalibrationMethod.text = "手动"
        }else{
            binding.atvCalibrationMethod.text = "自动"
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
        return R.layout.item_environmental
    }
}