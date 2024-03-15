package com.just.machine.ui.adapter

import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.SixMinRecordsBean
import com.just.news.R
import com.just.news.databinding.ItemLayoutSixTestBinding

/**
 *create by 2024/3/6
 * 患者六分钟测试数据
 *@author zt
 */
class SixMinAdapter() : BaseRecyclerViewAdapter<SixMinRecordsBean, ItemLayoutSixTestBinding>() {

    override fun bindData(item: SixMinRecordsBean, position: Int) {
        binding.item = item
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_layout_six_test
    }

    private var listener: PatientListener? = null

    interface PatientListener {
        fun onClickItem(bean: SixMinRecordsBean)
    }

    fun setItemOnClickListener(listener: PatientListener) {
        this.listener = listener
    }

}