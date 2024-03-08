package com.just.machine.ui.adapter

import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.dao.PatientBean
import com.just.news.R
import com.just.news.databinding.ItemLayoutPatientBinding

/**
 *create by 2024/3/6
 * 患者信息adapter
 *@author zt
 */
class PatientsAdapter
    : BaseRecyclerViewAdapter<PatientBean, ItemLayoutPatientBinding>() {

    private var itemClickListener: ((PatientBean) -> Unit)? = null

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        super.onBindViewHolder(holder, position)
//
//        holder.binding.atvRecordNumber.text = itemData[position].medicalRecordNumber
//        holder.binding.atvName.text = itemData[position].name
//        holder.binding.atvSex.text = itemData[position].sex
//        holder.binding.atvAge.text = itemData[position].age
//        holder.binding.atvHeight.text = itemData[position].height
//        holder.binding.atvWeight.text = itemData[position].weight
//        holder.binding.atvCreateTime.text = itemData[position].addTime
//
//        holder.binding.btnDelete.setNoRepeatListener {
////            removeItem(viewHolder.adapterPosition)
//            LogUtils.e("" + position + "--viewHolder.adapterPosition--" + holder.adapterPosition)
////            listener?.deleteItem(itemData[viewHolder.adapterPosition].patientId)
//        }
//
//        holder.binding.llItem.setNoRepeatListener {
//            listener?.onClickItem(itemData[holder.adapterPosition])
//        }
//
//    }


    override fun bindData(item: PatientBean) {
        binding.item = item

        binding.btnDelete.setOnClickListener{
            itemClickListener?.invoke(item)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_layout_patient
    }

    fun setDeleteClickListener(listener: (PatientBean) -> Unit) {
        this.itemClickListener = listener
    }

}