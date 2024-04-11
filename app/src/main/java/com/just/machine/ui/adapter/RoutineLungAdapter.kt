package com.just.machine.ui.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.staticlung.RoutineLungBean
import com.just.news.R
import com.just.news.databinding.ItemRoutineBinding

class RoutineLungAdapter(val context: Context) :
    BaseRecyclerViewAdapter<RoutineLungBean, ItemRoutineBinding>() {


    override fun bindData(item: RoutineLungBean, position: Int) {
        binding.bean = item
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_routine
    }
}