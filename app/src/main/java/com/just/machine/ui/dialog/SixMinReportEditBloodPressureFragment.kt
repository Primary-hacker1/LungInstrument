package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.model.Constants
import com.just.machine.model.SixMinReportEditBloodPressure
import com.just.news.R
import com.just.news.databinding.FragmentDialogSixminReportEditPressureBinding

class SixMinReportEditBloodPressureFragment :
    BaseDialogFragment<FragmentDialogSixminReportEditPressureBinding>() {

    private var listener: SixMinReportEditBloodDialogListener? = null

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startEditBloodDialogFragment(

            fragmentManager: FragmentManager,
            bean: SixMinReportEditBloodPressure? = SixMinReportEditBloodPressure()

        ): SixMinReportEditBloodPressureFragment {

            val dialogFragment = SixMinReportEditBloodPressureFragment()

            dialogFragment.show(
                fragmentManager,
                SixMinReportEditBloodPressureFragment::javaClass.toString()
            )

            val bundle = Bundle()

            bundle.putParcelable(Constants.editBloodPressure, bean)

            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    interface SixMinReportEditBloodDialogListener {
        fun onClickConfirm(bean: SixMinReportEditBloodPressure)
    }

    fun setEditBloodDialogOnClickListener(listener: SixMinReportEditBloodDialogListener) {
        this.listener = listener
    }

    override fun start(dialog: Dialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {

    }

    override fun initListener() {
        binding.sixminReportTvEditBloodPressureConfirm.setNoRepeatListener {
            val highBefore = binding.sixminReportEditBloodPressureHighBefore.text.toString()
            val lowBefore = binding.sixminReportEditBloodPressureLowBefore.text.toString()
            val highAfter = binding.sixminReportEditBloodPressureHighAfter.text.toString()
            val lowAfter = binding.sixminReportEditBloodPressureLowAfter.text.toString()
            val bean = SixMinReportEditBloodPressure()
            bean.highBloodPressureBefore = highBefore
            bean.lowBloodPressureBefore = lowBefore
            bean.highBloodPressureAfter = highAfter
            bean.lowBloodPressureAfter = lowAfter
            listener?.onClickConfirm(bean)
        }
        binding.sixminReportTvEditBloodPressureClose.setOnClickListener {
            dismiss()
        }
    }

    override fun initData() {
        val bean =
            arguments?.getParcelable<SixMinReportEditBloodPressure>(Constants.editBloodPressure)
        if (bean != null) {
            binding.sixminReportEditBloodPressureHighBefore.setText(bean.highBloodPressureBefore)
            binding.sixminReportEditBloodPressureLowBefore.setText(bean.lowBloodPressureBefore)
            binding.sixminReportEditBloodPressureHighAfter.setText(bean.highBloodPressureAfter)
            binding.sixminReportEditBloodPressureLowAfter.setText(bean.lowBloodPressureAfter)
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_report_edit_pressure
    }
}