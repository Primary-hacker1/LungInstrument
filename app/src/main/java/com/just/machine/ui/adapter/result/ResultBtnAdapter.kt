package com.just.machine.ui.adapter.result

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.common.base.gone
import com.common.base.visible
import com.just.machine.model.DynamicResultButtonBean
import com.just.news.R
import com.just.news.databinding.ItemResultButtonBinding

/**
 *create by 2024/3/15
 * 运动评估数据表
 *@author zt
 */
class ResultBtnAdapter(val context: Context) :
    BaseRecyclerViewAdapter<DynamicResultButtonBean, ItemResultButtonBinding>() {

    val colorWhile = ContextCompat.getColor(context, R.color.colorWhile)
    private val colorBlue = ContextCompat.getColor(context, R.color.c2F89DE)

    override fun bindData(item: DynamicResultButtonBean, position: Int) {
        binding.item = item

//        LogUtils.e(tag + item.isClick)

        if (item.isClick == true) {
            binding.atvResult.setTextColor(ContextCompat.getColor(context,R.color.colorWhile))
            binding.imgResultClick.background = ContextCompat.getDrawable(context,R.drawable.result_click)
            binding.imgResultIcon.setColorFilter(colorWhile, PorterDuff.Mode.SRC_IN)
            val rlItemBg =
                ContextCompat.getDrawable(context, R.drawable.spinner_dropdown_background)
            rlItemBg?.setColorFilter(colorBlue, PorterDuff.Mode.SRC_IN)
            binding.rlItem.background = rlItemBg
        } else {
            binding.atvResult.setTextColor(ContextCompat.getColor(context,R.color.text3))
            binding.imgResultClick.background = ContextCompat.getDrawable(context,R.drawable.result_on_click)
            binding.imgResultIcon.colorFilter =
                PorterDuffColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP)
            val rlItemBg =
                ContextCompat.getDrawable(context, R.drawable.spinner_dropdown_background)
            binding.rlItem.background = rlItemBg
        }

        if(item.isVisible == true){
            binding.rlItem.visible()
        }else{
            binding.rlItem.gone()
        }

    }

    override fun getLayoutRes(): Int {
        return R.layout.item_result_button
    }
}