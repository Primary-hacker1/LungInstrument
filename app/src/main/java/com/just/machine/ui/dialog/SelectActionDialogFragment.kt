package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.dao.PatientBean
import com.just.machine.model.Constants
import com.just.news.R
import com.just.news.databinding.FragmentDialogSelectActionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectActionDialogFragment : BaseDialogFragment<FragmentDialogSelectActionBinding>() {

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startSelectActionDialogFragment(

            fragmentManager: FragmentManager,
            selectType: String = ""

        ): SelectActionDialogFragment {

            val dialogFragment = SelectActionDialogFragment()

            dialogFragment.show(fragmentManager, SelectActionDialogFragment::javaClass.toString())

            val bundle = Bundle()

            bundle.putString(Constants.commonDialogType, selectType)

            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    override fun start(dialog: Dialog?) {

    }

    override fun initView() {
        if (selectType.isEmpty()) {
            binding.llTestType.visibility = View.VISIBLE
            binding.llReportType.visibility = View.GONE
        } else {
            binding.llTestType.visibility = View.GONE
            binding.llReportType.visibility = View.VISIBLE
        }
    }

    override fun initListener() {
        binding.ivBtnSixmin.setNoRepeatListener {
            listener?.onClickConfirm(1)
            dismiss()
        }
        binding.ivBtnXf.setNoRepeatListener {
            listener?.onClickConfirm(0)
            dismiss()
        }
        binding.sixminReportLlView.setNoRepeatListener {
            reportListener?.onClickView()
            dismiss()
        }
        binding.sixminReportLlEdit.setNoRepeatListener {
            reportListener?.onClickEdit()
            dismiss()
        }
        binding.sixminReportLlExport.setNoRepeatListener {
            reportListener?.onClickExport()
            dismiss()
        }
        binding.sixminReportLlDelete.setNoRepeatListener {
            reportListener?.onClickDelete()
            dismiss()
        }
    }

    override fun initData() {
        selectType = arguments?.getString(Constants.commonDialogType, "").toString()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_select_action
    }

    private lateinit var selectType: String
    private var listener: SelectActionDialogListener? = null
    private var reportListener: SelectReportActionDialogListener? = null

    interface SelectActionDialogListener {
        fun onClickConfirm(actionType: Int)
    }

    fun setSelectActionDialogListener(listener: SelectActionDialogListener) {
        this.listener = listener
    }

    interface SelectReportActionDialogListener {
        fun onClickView()
        fun onClickEdit()
        fun onClickExport()
        fun onClickDelete()
    }

    fun setSelectReportActionDialogListener(reportListener: SelectReportActionDialogListener) {
        this.reportListener = reportListener
    }
}