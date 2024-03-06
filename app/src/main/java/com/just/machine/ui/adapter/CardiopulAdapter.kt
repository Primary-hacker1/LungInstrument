package com.just.machine.ui.adapter

import androidx.databinding.ObservableList
import com.common.base.BaseDataBingViewHolder
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.CardiopulmonaryRecordsBean
import com.just.news.databinding.ItemLayoutCardiopulTestBinding

/**
 *create by 2024/3/6
 * 患者心肺测试数据
 *@author zt
 */
class CardiopulAdapter(itemData: ObservableList<CardiopulmonaryRecordsBean>, layoutId: Int, dataId: Int) :
    BaseRecyclerViewAdapter<CardiopulmonaryRecordsBean, ItemLayoutCardiopulTestBinding>(
        itemData, layoutId, dataId
    ) {

    override fun bindViewHolder(
        viewHolder: BaseDataBingViewHolder<ItemLayoutCardiopulTestBinding>,
        position: Int,
        t: CardiopulmonaryRecordsBean
    ) {
        super.bindViewHolder(viewHolder, position, t)

        viewHolder.binding.atvAssess.text = itemData[position].assess

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
        fun onClickItem(bean: CardiopulmonaryRecordsBean)
    }

    fun setItemOnClickListener(listener: PatientListener) {
        this.listener = listener
    }

}