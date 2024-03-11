package com.just.machine.ui.adapter

import com.common.base.BaseRecyclerViewAdapter
import com.common.base.setNoRepeatListener
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

    override fun bindData(item: PatientBean) {
        binding.item = item

        binding.btnDelete.setNoRepeatListener{
            listener?.onDeleteItem(item)
        }

        binding.btnUpdate.setNoRepeatListener{
            listener?.onUpdateItem(item)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_layout_patient
    }


    private var listener: PatientListener? = null

    interface PatientListener {
        fun onDeleteItem(bean: PatientBean)
        fun onUpdateItem(bean: PatientBean)
    }

    fun setItemOnClickListener(listener: PatientListener) {
        this.listener = listener
    }

}