package com.just.machine.ui.adapter.calibration

import android.content.Context
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.dao.calibration.FlowBean
import com.just.news.R
import com.just.news.databinding.ItemFlowBinding

class FlowAdapter(val context: Context) :
    BaseRecyclerViewAdapter<FlowBean, ItemFlowBinding>() {

    private var selectedItem = -1

    override fun bindData(item: FlowBean, position: Int) {
        binding.bean = item

        if (position == selectedItem) {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))
        } else {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        if(item.errorRate.toString().toFloat() < 0){
            item.calibrationResults = "未通过"
            binding.atvCalibrationResults.setTextColor(ContextCompat.getColor(context,R.color.red))
        }else{
            item.calibrationResults = "通过"
            binding.atvCalibrationResults.setTextColor(ContextCompat.getColor(context,R.color.text3))
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
        return R.layout.item_flow
    }
}