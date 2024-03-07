package com.just.machine.ui.adapter

import androidx.databinding.ObservableList
import com.common.base.BaseDataBingViewHolder
import com.common.base.BaseRecyclerViewAdapter
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
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

    var listener: PatientListener? = null

    override fun bindViewHolder(
        viewHolder: BaseDataBingViewHolder<ItemLayoutPatientBinding>,
        position: Int,
        t: PatientBean
    ) {
        super.bindViewHolder(viewHolder, position, t)
        viewHolder.binding.atvRecordNumber.text = itemData[position].medicalRecordNumber
        viewHolder.binding.atvName.text = itemData[position].name
        viewHolder.binding.atvSex.text = itemData[position].sex
        viewHolder.binding.atvAge.text = itemData[position].age
        viewHolder.binding.atvHeight.text = itemData[position].height
        viewHolder.binding.atvWeight.text = itemData[position].weight
        viewHolder.binding.atvCreateTime.text = itemData[position].addTime

        viewHolder.binding.btnDelete.setOnClickListener {
            deleteItem(viewHolder.adapterPosition)
            LogUtils.e(viewHolder.adapterPosition.toString())
//            listener?.deleteItem(itemData[viewHolder.adapterPosition].patientId)
        }

        viewHolder.binding.llItem.setNoRepeatListener {
            listener?.onClickItem(itemData[position])
        }

    }

    private fun deleteItem(position: Int) {
        itemData.removeAt(position)
        notifyItemRemoved(position)
    }

    interface PatientListener {
        fun onClickItem(bean: PatientBean)
        fun deleteItem(id: Long)
    }

    fun setPatientsClickListener(listener: PatientListener) {
        this.listener = listener
    }

}