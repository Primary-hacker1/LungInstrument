package com.just.machine.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.just.machine.model.Constants
import com.just.machine.model.sixminreport.SixMinReportPrescription
import com.just.machine.util.SpinnerHelper
import com.just.news.R
import com.just.news.databinding.FragmentDialogSixminReportPrescriptionBinding

/**
 * 6分钟预生成报告选择处方参数dialog
 */
class SixMinReportPrescriptionFragment :
    BaseDialogFragment<FragmentDialogSixminReportPrescriptionBinding>() {

    private var listener: SixMinReportPrescriptionDialogListener? = null
    private lateinit var spStride: SpinnerHelper
    private lateinit var spDistance: SpinnerHelper
    private lateinit var spHeatBeat: SpinnerHelper
    private lateinit var spMetab: SpinnerHelper
    private lateinit var spTired: SpinnerHelper
    private var prescriptionBean = SixMinReportPrescription()

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startPrescriptionDialogFragment(

            fragmentManager: FragmentManager,
            bean: SixMinReportPrescription,

            ): SixMinReportPrescriptionFragment {

            val dialogFragment = SixMinReportPrescriptionFragment()

            dialogFragment.show(
                fragmentManager, SixMinReportPrescriptionFragment::javaClass.toString()
            )

            val bundle = Bundle()

            bundle.putParcelable(Constants.prescriptionBean, bean)
            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    interface SixMinReportPrescriptionDialogListener {
        fun onClickConfirm(
            stride: String, distance: String, heart: String, metab: String, borg: String
        )

        fun onClickClose()
    }

    fun setPrescriptionDialogOnClickListener(listener: SixMinReportPrescriptionDialogListener) {
        this.listener = listener
    }

    override fun start(dialog: Dialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {

    }

    override fun initListener() {
        binding.sixminReportTvPrescriptionConfirm.setOnClickListener {
            dismiss()
            listener?.onClickConfirm(
                if (spStride.getSelection() == 0) "出具" else "不出具",
                if (spDistance.getSelection() == 0) "出具" else "不出具",
                if (spHeatBeat.getSelection() == 0) "出具" else "不出具",
                if (spMetab.getSelection() == 0) "出具" else "不出具",
                if (spTired.getSelection() == 0) "出具" else "不出具"
            )
        }
        binding.sixminReportTvPrescriptionClose.setOnClickListener {
            dismiss()
        }
        spStride = SpinnerHelper(
            requireContext(),
            binding.sixminReportTvPrescriptionStride,
            R.array.spinner_sixmin_report_description
        )

        spStride.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                val textView: TextView = view as TextView
                if (selectedItem == "出具") {
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.c2F89DE))
                } else {
                    textView.setTextColor(Color.RED)
                }
            }

            override fun onNothingSelected() {

            }
        })

        spDistance = SpinnerHelper(
            requireContext(),
            binding.sixminReportTvPrescriptionDistance,
            R.array.spinner_sixmin_report_description
        )
        spDistance.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                val textView: TextView = view as TextView
                if (selectedItem == "出具") {
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.c2F89DE))
                } else {
                    textView.setTextColor(Color.RED)
                }
            }

            override fun onNothingSelected() {

            }
        })

        spHeatBeat = SpinnerHelper(
            requireContext(),
            binding.sixminReportTvPrescriptionHeartbeat,
            R.array.spinner_sixmin_report_description
        )
        spHeatBeat.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                val textView: TextView = view as TextView
                if (selectedItem == "出具") {
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.c2F89DE))
                } else {
                    textView.setTextColor(Color.RED)
                }
            }

            override fun onNothingSelected() {

            }
        })

        spMetab = SpinnerHelper(
            requireContext(),
            binding.sixminReportTvPrescriptionMetab,
            R.array.spinner_sixmin_report_description
        )
        spMetab.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                val textView: TextView = view as TextView
                if (selectedItem == "出具") {
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.c2F89DE))
                } else {
                    textView.setTextColor(Color.RED)
                }
            }

            override fun onNothingSelected() {

            }
        })

        spTired = SpinnerHelper(
            requireContext(),
            binding.sixminReportTvPrescriptionTired,
            R.array.spinner_sixmin_report_description
        )
        spTired.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                val textView: TextView = view as TextView
                if (selectedItem == "出具") {
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.c2F89DE))
                } else {
                    textView.setTextColor(Color.RED)
                }
            }

            override fun onNothingSelected() {

            }
        })

        if (prescriptionBean.distanceState == "1" || prescriptionBean.distanceState == "") {
            spDistance.setSelection(0)
        } else {
            spDistance.setSelection(1)
        }
        if (prescriptionBean.heartrateState == "1" || prescriptionBean.heartrateState == "") {
            spHeatBeat.setSelection(0)
        } else {
            spHeatBeat.setSelection(1)
        }
        if (prescriptionBean.metabState == "1" || prescriptionBean.metabState == "") {
            spMetab.setSelection(0)
        } else {
            spMetab.setSelection(1)
        }
        if (prescriptionBean.pllevState == "1" || prescriptionBean.pllevState == "") {
            spTired.setSelection(0)
        } else {
            spTired.setSelection(1)
        }
    }

    override fun initData() {
        val bean = arguments?.getParcelable<SixMinReportPrescription>(Constants.prescriptionBean)
        if (bean != null) {
            prescriptionBean = bean
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_report_prescription
    }

}