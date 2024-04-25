package com.just.machine.ui.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.common.base.setNoRepeatListener
import com.just.machine.dao.PatientBean
import com.just.machine.model.PatientInfoBean
import com.just.news.R
import com.just.news.databinding.ItemLayoutPatientBinding

/**
 *create by 2024/3/6
 * 患者信息adapter
 *@author zt
 */
class PatientsAdapter(var context: Context) :
    BaseRecyclerViewAdapter<PatientBean, ItemLayoutPatientBinding>() {

    private var selectedItem = -1

    override fun bindData(item: PatientBean, position: Int) {
        binding.item = item

        binding.btnDelete.setNoRepeatListener {
            listener?.onDeleteItem(item)
        }

        binding.btnUpdate.setNoRepeatListener {
            listener?.onUpdateItem(item)
        }

        binding.btnAction.setNoRepeatListener {
            listener?.onActionItem(item)
        }

        if (position == selectedItem) {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))
        } else {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_layout_patient
    }


    private var listener: PatientListener? = null

    interface PatientListener {
        fun onDeleteItem(bean: PatientBean)
        fun onUpdateItem(bean: PatientBean)
        fun onActionItem(bean: PatientBean)
    }

    /**
     * @param position 点击 item 的 position
     * */
    fun toggleItemBackground(position: Int) {
        if (selectedItem == position) {
            selectedItem = -1
        } else {
            selectedItem = position
        }
        notifyDataSetChanged()
    }

    fun setItemOnClickListener(listener: PatientListener) {
        this.listener = listener
    }

}