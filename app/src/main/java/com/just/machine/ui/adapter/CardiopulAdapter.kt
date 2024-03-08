package com.just.machine.ui.adapter

import androidx.databinding.ObservableList
import com.common.base.BaseDataBingViewHolder
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.dao.PatientBean
import com.just.machine.model.CardiopulmonaryRecordsBean
import com.just.news.R
import com.just.news.databinding.ItemLayoutCardiopulTestBinding
import com.just.news.databinding.ItemLayoutPatientBinding

/**
 *create by 2024/3/6
 * 患者心肺测试数据
 *@author zt
 */
class CardiopulAdapter(itemData: ObservableList<CardiopulmonaryRecordsBean>, layoutId: Int, dataId: Int)
    : BaseRecyclerViewAdapter<CardiopulmonaryRecordsBean, ItemLayoutCardiopulTestBinding>()  {

//    override fun bindViewHolder(
//        viewHolder: BaseDataBingViewHolder<ItemLayoutCardiopulTestBinding>,
//        position: Int,
//        t: CardiopulmonaryRecordsBean
//    ) {
//        super.bindViewHolder(viewHolder, position, t)
//
//        viewHolder.binding.atvConventionalVentilation.text = itemData[position].conventionalVentilation
//        viewHolder.binding.atvForcedVitalCapacity.text = itemData[position].forcedVitalCapacity
//        viewHolder.binding.atvMaximumVentilation.text = itemData[position].maximumVentilation
//        viewHolder.binding.atvExerciseLungTest.text = itemData[position].exerciseLungTest
//        viewHolder.binding.atvAssess.text = itemData[position].assess
//
//        viewHolder.binding.btnDelete.setOnClickListener {
//            deleteItem(viewHolder.adapterPosition)
//        }
//
//        viewHolder.binding.llItem.setOnClickListener {
//            listener?.onClickItem(itemData[position])
//        }
//    }


    override fun bindData(item: CardiopulmonaryRecordsBean) {
        binding.item = item
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_layout_cardiopul_test
    }


    private var listener: PatientListener? = null

    interface PatientListener {
        fun onClickItem(bean: CardiopulmonaryRecordsBean)
    }

    fun setItemOnClickListener(listener: PatientListener) {
        this.listener = listener
    }

}