package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.model.Constants
import com.just.news.R
import com.just.news.databinding.FragmentDialogCommonBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonDialogFragment : BaseDialogFragment<FragmentDialogCommonBinding>() {

    private var listener: CommonDialogClickListener? = null
    private var dialogContent = ""

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startCommonDialogFragment(

            fragmentManager: FragmentManager,
            dialogContent: String

        ): CommonDialogFragment {

            val dialogFragment = CommonDialogFragment()

            dialogFragment.show(fragmentManager, CommonDialogFragment::javaClass.toString())

            val bundle = Bundle()

            bundle.putString(Constants.commonDialogContent, dialogContent)

            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    interface CommonDialogClickListener {
        fun onPositiveClick()
        fun onNegativeClick()
    }

    fun setCommonDialogOnClickListener(listener: CommonDialogClickListener) {
        this.listener = listener
    }

    override fun start(dialog: Dialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {
        if(dialogContent != ""){
            binding.tvCommonDialogContent.text = dialogContent
        }
    }

    override fun initListener() {
        binding.ivCommonDialogClose.setNoRepeatListener {
            dismiss()
        }
        binding.tvCommonDialogPositive.setNoRepeatListener {
            dismiss()
            listener?.onPositiveClick()
        }
        binding.tvCommonDialogNegative.setNoRepeatListener {
            dismiss()
            listener?.onNegativeClick()
        }
    }

    override fun initData() {
        dialogContent = arguments?.getString(Constants.commonDialogContent,"").toString()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_common
    }

}