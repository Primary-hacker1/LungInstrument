package com.just.machine.ui.adapter

import androidx.databinding.ObservableList
import com.common.base.BaseDataBingViewHolder
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.dao.PatientBean
import com.just.news.databinding.ItemLayoutPatientBinding

/**
 *create by 2024/3/6
 * 患者信息adapter
 *@author zt
 */
class PatientsAdapter(itemData: ObservableList<PatientBean>, layoutId: Int, dataId: Int) :
    BaseRecyclerViewAdapter<PatientBean, ItemLayoutPatientBinding>(
        itemData, layoutId, dataId
    ) {

    override fun bindViewHolder(
        viewHolder: BaseDataBingViewHolder<ItemLayoutPatientBinding>,
        position: Int,
        t: PatientBean
    ) {
        super.bindViewHolder(viewHolder, position, t)

        viewHolder.binding.atvName.text = itemData[position].name
        viewHolder.binding.atvRecordNumber.text = itemData[position].medicalRecordNumber

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
        fun onClickItem(bean: PatientBean)
    }

    fun setItemOnClickListener(listener: PatientListener) {
        this.listener = listener
    }

}