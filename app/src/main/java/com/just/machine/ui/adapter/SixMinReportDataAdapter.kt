package com.just.machine.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.just.news.R


class SixMinReportDataAdapter(val mList: MutableList<String>, val mContext: Context) :
    BaseAdapter() {

    override fun getCount(): Int {
        return mList.size
    }

    override fun getItem(position: Int): Any {
        return mList[0]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater =
            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = layoutInflater.inflate(R.layout.item_sixmin_report, null)

        convertView.findViewById<TextView>(R.id.sixmin_tv_report).text = mList[position]
        return convertView
    }
}