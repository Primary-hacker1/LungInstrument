package com.just.machine.ui.adapter

import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.CardiopulmonaryRecordsBean
import com.just.news.R
import com.just.news.databinding.ItemLayoutCardiopulTestBinding

/**
 *create by 2024/3/6
 * 患者心肺测试数据
 *@author zt
 */
class CardiopulAdapter
    : BaseRecyclerViewAdapter<CardiopulmonaryRecordsBean, ItemLayoutCardiopulTestBinding>() {

    override fun bindData(item: CardiopulmonaryRecordsBean, position: Int) {
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