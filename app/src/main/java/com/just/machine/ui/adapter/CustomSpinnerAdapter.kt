package com.just.machine.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.just.news.R

class CustomSpinnerAdapter(context: Context, private val dataList: MutableList<Pair<String, String>>? = ArrayList()) :
    ArrayAdapter<Pair<String, String>>(context, R.layout.custom_spinner_item, dataList!!) {

    fun updateSpinnerText(spinnerText: String) {
        for (i in 0 until dataList!!.size) {
            dataList[i] = dataList[i].copy(second = spinnerText)
        }
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

        spinnerTitle.text = pair?.first
        spinnerText.text = pair?.second

        return view
    }
}