package com.just.machine.ui.adapter

import android.content.Context
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.sixmininfo.SixMinReportPatientSelfItemBean
import com.just.news.R
import com.just.news.databinding.ItemSixminReportPatientSelfItemBinding


class SixMinReportPatientSelfItemAdapter(var mContext: Context) :
    BaseRecyclerViewAdapter<SixMinReportPatientSelfItemBean, ItemSixminReportPatientSelfItemBinding>() {

    private var listener: PatientReportItemClickListener? = null

    override fun bindData(item: SixMinReportPatientSelfItemBean, position: Int) {
        binding.siminReportTvPatientSelfItemName.text = item.itemName
        binding.siminReportTvPatientSelfItemLevel.text = item.itemLevel
        binding.siminReportRbPatientSelfItem.isChecked = item.itemCheck == "1"
        binding.siminReportRbPatientSelfItem.isEnabled = item.itemStatus == "1"
        if(item.itemStatus == "0"){
            binding.siminReportRbPatientSelfItem.alpha = 0.5f
        }else{
            binding.siminReportRbPatientSelfItem.alpha = 1.0f
        }
        binding.siminReportRbPatientSelfItem.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                items.forEach {
                    it.itemCheck = "0"
                }
                item.itemCheck = "1"
                binding.siminReportRbPatientSelfItem.post {
                    notifyDataSetChanged()
                }
            }
            listener?.onReportItemCheck(position,isChecked)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_sixmin_report_patient_self_item
    }

    interface PatientReportItemClickListener {
        fun onReportItemCheck(position: Int,isChecked: Boolean)
    }

    fun setItemOnCheckListener(listener: PatientReportItemClickListener) {
        this.listener = listener
    }
}