package com.just.machine.ui.adapter

import androidx.databinding.ObservableList
import com.common.base.BaseDataBingViewHolder
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.SixMinRecordsBean
import com.just.news.databinding.ItemLayoutSixTestBinding

/**
 *create by 2024/3/6
 * 患者六分钟测试数据
 *@author zt
 */
class SixMinAdapter(itemData: ObservableList<SixMinRecordsBean>, layoutId: Int, dataId: Int) :
    BaseRecyclerViewAdapter<SixMinRecordsBean, ItemLayoutSixTestBinding>(
        itemData, layoutId, dataId
    ) {

    override fun bindViewHolder(
        viewHolder: BaseDataBingViewHolder<ItemLayoutSixTestBinding>,
        position: Int,
        t: SixMinRecordsBean
    ) {
        super.bindViewHolder(viewHolder, position, t)

        viewHolder.binding.atvTestTime.text = itemData[position].testTime

        viewHolder.binding.atvNumber.text = itemData[position].reportNo

        viewHolder.binding.btnDelete.setOnClickListener {
            deleteItem(viewHolder.adapterPosition)
        }

        viewHolder.binding.llItem.setOnClickListener {
            listener?.onClickItem(itemData[position])
        }

    }

    private fun deleteItem(position: Int) {
        itemData.removeAt(position)
        notifyItemRemoved(position)
    }


    private var listener: PatientListener? = null

    interface PatientListener {
        fun onClickItem(bean: SixMinRecordsBean)
    }

    fun setItemOnClickListener(listener: PatientListener) {
        this.listener = listener
    }

}