package com.just.machine.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import com.common.base.BaseRecyclerViewAdapter
import com.common.base.setNoRepeatListener
import com.just.machine.model.sixminreport.SixMinReportInfo
import com.just.news.R
import com.just.news.databinding.ItemLayoutSixTestBinding

/**
 *create by 2024/3/6
 * 患者六分钟测试数据
 *@author zt
 */
class SixMinAdapter(var context: Context) : BaseRecyclerViewAdapter<SixMinReportInfo, ItemLayoutSixTestBinding>() {

    private var selectedItem = -1

    override fun bindData(item: SixMinReportInfo, position: Int) {
        binding.item = item

        binding.btnCheck.setNoRepeatListener {
            listener?.onCheckItem(item)
        }

        binding.btnUpdate.setNoRepeatListener {
            listener?.onUpdateItem(item)
        }
        binding.btnExport.setNoRepeatListener {
            listener?.onExportItem(item)
        }

        if (position == selectedItem) {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))
        } else {
            binding.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_layout_six_test
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

    private var listener: SixMinReportListener? = null

    interface SixMinReportListener {
        fun onExportItem(bean: SixMinReportInfo)
        fun onUpdateItem(bean: SixMinReportInfo)
        fun onCheckItem(bean: SixMinReportInfo)
    }

    fun setItemOnClickListener(listener: SixMinReportListener) {
        this.listener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    fun btnHover(btn: Button, down:Int, up:Int){
        btn.setOnTouchListener(View.OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 按下时改变按钮状态，例如改变背景颜色
                    btn.setBackgroundResource(down)
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    btn.setBackgroundResource(up)
                }
            }
            // 返回true表示处理了触摸事件
            true
        })
    }
}