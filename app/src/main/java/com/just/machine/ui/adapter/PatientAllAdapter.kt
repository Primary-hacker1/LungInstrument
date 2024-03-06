package com.just.machine.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.just.machine.dao.PatientBean
import com.just.news.R


/**
 *create by 2024/2/27
 * 患者信息adapter
 *@author zt
 */
@Deprecated("PatientsAdapter")
class PatientAllAdapter(private val mDatas: MutableList<PatientBean>) :
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
        val tvContent: AppCompatTextView
        val medicalRecordNumber: AppCompatTextView
        val ivDelete: AppCompatButton

        init {
            tvContent = itemView.findViewById<AppCompatTextView>(R.id.atv_name)
            medicalRecordNumber = itemView.findViewById<AppCompatTextView>(R.id.atv_record_number)
            ivDelete = itemView.findViewById<AppCompatButton>(R.id.btn_delete)
        }
    }
}