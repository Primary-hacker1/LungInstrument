package com.just.machine.ui.adapter.calibration

import android.content.Context
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.dao.calibration.FlowAutoCalibrationResultBean
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.dao.calibration.FlowManualCalibrationResultBean
import com.just.news.R
import com.just.news.databinding.ItemResultAutoFlowBinding
import com.just.news.databinding.ItemResultFlowBinding

/**
 *create by 2024/6/6
 * 流量自动定标结果适配器
 *@author zt
 */
class ResultAutoFlowAdapter(val context: Context) :
    BaseRecyclerViewAdapter<FlowAutoCalibrationResultBean, ItemResultAutoFlowBinding>() {

    private var selectedItem = -1

    override fun bindData(item: FlowAutoCalibrationResultBean, position: Int) {
        binding.bean = item

        if (position == selectedItem) {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))
        } else {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        if (item.calibrationResult?.isNotEmpty() == true && item.calibrationResult == "1") {
            binding.tvAutoFlowResult.setTextColor(ContextCompat.getColor(context, R.color.text3))
            binding.tvAutoFlowResult.text = "通过"
        } else {
            binding.tvAutoFlowResult.setTextColor(ContextCompat.getColor(context, R.color.red))
            binding.tvAutoFlowResult.text = "未通过"
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
        return R.layout.item_result_auto_flow
    }
}