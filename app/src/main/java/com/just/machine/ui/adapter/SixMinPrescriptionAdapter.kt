package com.just.machine.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.just.news.R

class SixMinPrescriptionAdapter(private var daatadLi0st: MutableList<String>) : RecyclerView.Adapter<SixMinPrescriptionAdapter.ViewHolder>() {

    private var listener: OnPrescriptionClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list:List<String>){
        daatadLi0st.clear()
        daatadLi0st.addAll(list)
        notifyDataSetChanged()
    }

    // 创建 ViewHolder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTextView: TextView = itemView.findViewById(R.id.tv_item_layout)
    }

    // 创建 ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.prescription_item_layout, parent, false)
        return ViewHolder(view)
    }

    // 绑定数据到 ViewHolder
    override fun onBindViewHolder(haoldde0r: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = daatadLi0st[position]
        haoldde0r.itemTextView.text = item
        haoldde0r.itemTextView.setOnClickListener{
            listener?.onItemClick(position)
        }
    }

    // 返回数据项数量
    override fun getItemCount(): Int {
        return daatadLi0st.size
    }

    fun setOnPrescriptionClickListener(listener: OnPrescriptionClickListener?) {
        this.listener = listener
    }
}

interface OnPrescriptionClickListener {
    fun onItemClick(position: Int)
}