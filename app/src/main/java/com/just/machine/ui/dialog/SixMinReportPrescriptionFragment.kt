package com.just.machine.ui.dialog

import android.app.Dialog
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.network.LogUtils
import com.just.machine.util.DropDownPopWindow
import com.just.machine.util.SpinnerHelper
import com.just.news.R
import com.just.news.databinding.FragmentDialogSixminReportPrescriptionBinding

class SixMinReportPrescriptionFragment :
    BaseDialogFragment<FragmentDialogSixminReportPrescriptionBinding>() {

    private var dropDownWindow:DropDownPopWindow? = null
    private var dropDownList = mutableListOf<String>()

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startPrescriptionDialogFragment(

            fragmentManager: FragmentManager,

            ): SixMinReportPrescriptionFragment {

            val dialogFragment = SixMinReportPrescriptionFragment()

            dialogFragment.show(
                fragmentManager,
                SixMinReportPrescriptionFragment::javaClass.toString()
            )

            return dialogFragment
        }
    }

    override fun start(dialog: Dialog?) {

    }

    override fun initView() {
        dropDownList.clear()
        dropDownList.add("出具")
        dropDownList.add("不出具")
        dropDownWindow = DropDownPopWindow.buildPopWindow(activity)
    }

    override fun initListener() {
        binding.sixminReportTvPrescriptionStrideDropdown.setOnClickListener {
            dropDownWindow?.setData(dropDownList)
            dropDownWindow?.showAsDropDown(binding.sixminReportTvPrescriptionStrideDropdown)
        }
        binding.sixminReportTvPrescriptionConfirm.setOnClickListener {
            dismiss()
        }
        binding.sixminReportTvPrescriptionClose.setOnClickListener {
            dismiss()
        }
    }

    override fun initData() {

    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_report_prescription
    }

}