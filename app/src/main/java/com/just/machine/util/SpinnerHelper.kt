package com.just.machine.util

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.just.news.R

class SpinnerHelper(
    private val context: Context,
    private val spinner: Spinner,
    private val spinnerItems: Int,
) {

    private var listener: SpinnerSelectionListener? = null

    init {
        setupSpinner()
    }

    private fun setupSpinner() {
        // 创建一个 ArrayAdapter 使用字符串数组和默认布局
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            context,
            spinnerItems, // 字符串数组资源
            R.layout.item_spinner // 默认布局
        )

        // 指定下拉列表的布局样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // 将适配器设置到 Spinner 上
        spinner.adapter = adapter

        // 设置 Spinner 选择监听器
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 处理选中项的逻辑
                val selectedItem = parent?.getItemAtPosition(position).toString()
                // 在这里可以执行相应的操作
                listener?.onItemSelected(selectedItem,view)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 当没有选中项时的处理逻辑
                listener?.onNothingSelected()
            }
        }
    }

    fun setSpinnerSelectionListener(spinnerSelectionListener: SpinnerSelectionListener) {
        listener = spinnerSelectionListener
    }

    interface SpinnerSelectionListener {
        fun onItemSelected(selectedItem: String,view: View?=null)
        fun onNothingSelected()
    }

    fun setSelection(position:Int){
        spinner.setSelection(position)
    }

    fun getSelection():Any{
        return spinner.selectedItemPosition
    }
}
