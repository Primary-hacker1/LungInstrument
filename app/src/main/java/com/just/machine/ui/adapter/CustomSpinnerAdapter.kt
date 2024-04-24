package com.just.machine.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.just.machine.dao.CPETParameter
import com.just.news.R

class CustomSpinnerAdapter(context: Context, private val dataList: MutableList<CPETParameter>? = ArrayList()) :
    ArrayAdapter<CPETParameter>(context, R.layout.custom_spinner_item, dataList!!) {

    fun updateSpinnerText(dataList: MutableList<CPETParameter>) {
        // 清空适配器原有数据
        clear()
        // 将新数据添加到适配器
        addAll(dataList)
        // 通知适配器数据已更改
        notifyDataSetChanged()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_spinner_item, parent, false)
        val spinnerTitle = view.findViewById<TextView>(R.id.spinner_title)
        val spinnerText = view.findViewById<TextView>(R.id.spinner_text)
        val pair = dataList?.get(position)

        spinnerTitle.text = pair?.parameterName
        spinnerText.text = pair?.lowRange.toString()

        return view
    }
}
