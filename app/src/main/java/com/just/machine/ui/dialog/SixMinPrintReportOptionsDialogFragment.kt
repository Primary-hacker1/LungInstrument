package com.just.machine.ui.dialog

import android.app.Dialog
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.news.R
import com.just.news.databinding.FragmentDialogSixminPrintReportOptionsBinding
import dagger.hilt.android.AndroidEntryPoint
/**
 * 6分钟打印dialog
 */
@AndroidEntryPoint
class SixMinPrintReportOptionsDialogFragment :
    BaseDialogFragment<FragmentDialogSixminPrintReportOptionsBinding>() {

    private lateinit var listener: PrintReportOptionsClickListener
    private var ecgOptions = "1"
    private var evaluationOptions = "1"
    private var prescriptionOptions = "1"

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startSixMinPrintReportOptionsDialogFragment(

            fragmentManager: FragmentManager,
        ): SixMinPrintReportOptionsDialogFragment {

            val dialogFragment = SixMinPrintReportOptionsDialogFragment()

            dialogFragment.show(
                fragmentManager,
                SixMinPrintReportOptionsDialogFragment::javaClass.toString()
            )

            return dialogFragment
        }
    }

    override fun start(dialog: Dialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {
        if (ecgOptions == "1") {
            binding.sixminPrintReportEcgYes.isChecked = true
        } else {
            binding.sixminPrintReportEcgNo.isChecked = true
        }
        if (evaluationOptions == "1") {
            binding.sixminPrintReportEvaluationYes.isChecked = true
        } else {
            binding.sixminPrintReportEvaluationNo.isChecked = true
        }
        if (prescriptionOptions == "1") {
            binding.sixminPrintReportPrescriptionYes.isChecked = true
        } else {
            binding.sixminPrintReportPrescriptionNo.isChecked = true
        }
    }

    override fun initListener() {
        binding.sixminReportPrintTvConfirm.setNoRepeatListener {
            listener.onConfirmClick(ecgOptions, evaluationOptions, prescriptionOptions)
        }
        binding.sixminReportPrintTvCancel.setNoRepeatListener {
            dismiss()
            listener.onCancelClick()
        }

        binding.sixminPrintReportEcgYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                ecgOptions = "1"
            }
        }
        binding.sixminPrintReportEcgNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                ecgOptions = "0"
            }
        }
        binding.sixminPrintReportEvaluationYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                evaluationOptions = "1"
            }
        }
        binding.sixminPrintReportEvaluationNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                evaluationOptions = "0"
            }
        }
        binding.sixminPrintReportPrescriptionYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                prescriptionOptions = "1"
            }
        }
        binding.sixminPrintReportPrescriptionNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                prescriptionOptions = "0"
            }
        }
    }

    override fun initData() {

    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_print_report_options
    }

    interface PrintReportOptionsClickListener {
        fun onConfirmClick(
            ecgOptions: String,
            evaluationOptions: String,
            prescriptionOptions: String
        )

        fun onCancelClick()
    }

    fun setPrintReportOptionsClickListener(listener: PrintReportOptionsClickListener) {
        this.listener = listener
    }
}