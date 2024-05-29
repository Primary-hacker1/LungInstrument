package com.just.machine.ui.adapter

import android.content.Context
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.SixMinReportPatientSelfBean
import com.just.news.R
import com.just.news.databinding.ItemSixminReportPatientSelfBinding


class SixMinReportPatientSelfAdapter(var mContext: Context) :
    BaseRecyclerViewAdapter<SixMinReportPatientSelfBean, ItemSixminReportPatientSelfBinding>() {
    private var listener: PatientReportItemCheckListener? = null
    override fun bindData(item: SixMinReportPatientSelfBean, position: Int) {
        binding.sixminReportTvPatientSelfName.text = item.itemName
        if (item.itemType == "1") {
            binding.sixminReportIvPatientSelf.setImageResource(R.mipmap.breath)
        } else {
            binding.sixminReportIvPatientSelf.setImageResource(R.mipmap.tired)
        }
        binding.sixminReportTvPatientSelfConclusion.text = item.itemConclusion
        binding.sixminReportRvPatientSelf.layoutManager = LinearLayoutManager(mContext)
        val sixMinReportPatientSelfItemAdapter = SixMinReportPatientSelfItemAdapter(mContext)
        sixMinReportPatientSelfItemAdapter.setItemsBean(item.itemList)
        binding.sixminReportRvPatientSelf.adapter = sixMinReportPatientSelfItemAdapter
        sixMinReportPatientSelfItemAdapter.setItemOnCheckListener(object :
            SixMinReportPatientSelfItemAdapter.PatientReportItemClickListener {
            override fun onReportItemCheck(childPosition: Int, isChecked: Boolean) {
                item.itemConclusion = if(position ==0) "您当前的呼吸状况为: (${item.itemList[childPosition].itemName})呼吸困难状况${item.itemList[childPosition].itemLevel}" else "您当前的疲劳状况为: (${item.itemList[childPosition].itemName})疲劳状况${item.itemList[childPosition].itemLevel}"
                binding.sixminReportTvPatientSelfConclusion.post {
                    notifyDataSetChanged()
                }
            }
        })
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_sixmin_report_patient_self
    }

    interface PatientReportItemCheckListener {
        fun onReportItemCheck(position: Int,isChecked: Boolean, mView: RadioButton)
    }

    fun setItemOnCheckListener(listener: PatientReportItemCheckListener) {
        this.listener = listener
    }
}