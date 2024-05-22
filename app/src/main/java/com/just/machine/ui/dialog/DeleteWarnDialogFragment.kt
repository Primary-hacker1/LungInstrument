package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.model.Constants
import com.just.news.R
import com.just.news.databinding.FragmentDialogDeleteWarnBinding

/**
 * 删除条目警告dialog
 */
class DeleteWarnDialogFragment : BaseDialogFragment<FragmentDialogDeleteWarnBinding>() {

    private var listener: DeleteWarnDialogListener? = null

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startDeleteWarnDialogFragment(
            fragmentManager: FragmentManager, dialogContent: String

        ): DeleteWarnDialogFragment {

            val dialogFragment = DeleteWarnDialogFragment()

            dialogFragment.show(fragmentManager, DeleteWarnDialogFragment::javaClass.toString())

            val bundle = Bundle()

            bundle.putString(Constants.deleteWarnDialogContent, dialogContent)

            dialogFragment.arguments = bundle

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
        binding.tvDeleteWarnDialogPositive.setNoRepeatListener {
            dismiss()
            listener?.onClickConfirm()
        }
        binding.tvDeleteWarnDialogNegative.setNoRepeatListener {
            dismiss()
            listener?.onClickCancel()
        }
    }

    override fun initData() {
        val content = arguments?.getString(Constants.deleteWarnDialogContent, "")
        binding.tvDeleteWarnDialogContent.text = content
    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_delete_warn
    }

    interface DeleteWarnDialogListener {
        fun onClickConfirm()

        fun onClickCancel()
    }

    fun setDeleteWarnDialogListener(listener: DeleteWarnDialogListener) {
        this.listener = listener
    }
}