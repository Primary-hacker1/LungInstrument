package com.just.machine.ui.dialog

import android.app.Dialog
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.just.news.R
import com.just.news.databinding.FragmentDialogSixminReportPrescriptionBinding

class SixMinReportPrescriptionFragment :
    BaseDialogFragment<FragmentDialogSixminReportPrescriptionBinding>() {

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startPrescriptionDialogFragment(

            fragmentManager: FragmentManager,

            ): SixMinReportPrescriptionFragment {

            val dialogFragment = SixMinReportPrescriptionFragment()

            dialogFragment.show(fragmentManager, SixMinReportPrescriptionFragment::javaClass.toString())

            return dialogFragment
        }
    }

    override fun start(dialog: Dialog?) {

    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun initData() {

    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_report_prescription
    }

}