package com.just.machine.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.just.machine.dao.PatientBean
import com.just.news.R


/**
 *create by 2024/2/27
 * 患者信息adapter
 *@author zt
 */
class PatientAdapter(private val mDatas: MutableList<PatientBean>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listener: PatientListener? = null

    interface PatientListener {
        fun onClickItem(bean: PatientBean)
    }

    fun setItemOnClickListener(listener: PatientListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout_patient, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            holder.tvContent.text = mDatas[position].name
            holder.medicalRecordNumber.text = mDatas[position].medicalRecordNumber
            holder.ivDelete.setOnClickListener {
                deleteItem(holder.adapterPosition)
            }
            holder.itemView.setOnClickListener {
                listener?.onClickItem(mDatas[position])
            }
        }
    }

    private fun deleteItem(position: Int) {
        mDatas.removeAt(position)
        notifyItemRemoved(position)
    }

    //多出尾部刷新的item
    override fun getItemCount(): Int {
        return mDatas.size
    }

    //item的holder
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContent: TextView
        val medicalRecordNumber: TextView
        val ivDelete: ImageView

        init {
            tvContent = itemView.findViewById<TextView>(R.id.tvName)
            medicalRecordNumber = itemView.findViewById<TextView>(R.id.tvMedicalRecordNumber)
            ivDelete = itemView.findViewById<ImageView>(R.id.ivLinesItemDelete)
        }
    }
}