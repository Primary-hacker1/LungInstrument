package com.just.machine.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.network.LogUtils
import com.just.machine.util.DropDownPopWindow
import com.just.machine.util.SpinnerHelper
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

            dialogFragment.show(
                fragmentManager,
                SixMinReportPrescriptionFragment::javaClass.toString()
            )

            return dialogFragment
        }
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
        }
        binding.sixminReportTvPrescriptionClose.setOnClickListener {
            dismiss()
        }
        val spStride =
            SpinnerHelper(requireContext(), binding.sixminReportTvPrescriptionStride, R.array.spinner_sixmin_report_description)
        spStride.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                val textView:TextView = view as TextView
                if(selectedItem == "出具"){
                    textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.c2F89DE))
                }else{
                    textView.setTextColor(Color.RED)
                }
            }

            override fun onNothingSelected() {

            }
        })

        val spDistance =
            SpinnerHelper(requireContext(), binding.sixminReportTvPrescriptionDistance, R.array.spinner_sixmin_report_description)
        spDistance.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                val textView:TextView = view as TextView
                if(selectedItem == "出具"){
                    textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.c2F89DE))
                }else{
                    textView.setTextColor(Color.RED)
                }
            }

            override fun onNothingSelected() {

            }
        })

        val spHeatBeat =
            SpinnerHelper(requireContext(), binding.sixminReportTvPrescriptionHeartbeat, R.array.spinner_sixmin_report_description)
        spHeatBeat.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                val textView:TextView = view as TextView
                if(selectedItem == "出具"){
                    textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.c2F89DE))
                }else{
                    textView.setTextColor(Color.RED)
                }
            }

            override fun onNothingSelected() {

            }
        })

        val spMetab =
            SpinnerHelper(requireContext(), binding.sixminReportTvPrescriptionMetab, R.array.spinner_sixmin_report_description)
        spMetab.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                val textView:TextView = view as TextView
                if(selectedItem == "出具"){
                    textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.c2F89DE))
                }else{
                    textView.setTextColor(Color.RED)
                }
            }

            override fun onNothingSelected() {

            }
        })

        val spTired =
            SpinnerHelper(requireContext(), binding.sixminReportTvPrescriptionTired, R.array.spinner_sixmin_report_description)
        spTired.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                val textView:TextView = view as TextView
                if(selectedItem == "出具"){
                    textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.c2F89DE))
                }else{
                    textView.setTextColor(Color.RED)
                }
            }

            override fun onNothingSelected() {

            }
        })
    }

    override fun initData() {

    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_report_prescription
    }

}