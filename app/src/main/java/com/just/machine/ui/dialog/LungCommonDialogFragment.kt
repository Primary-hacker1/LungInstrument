package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.model.Constants
import com.just.news.R
import com.just.news.databinding.FragmentDialogLungCommonBinding

class LungCommonDialogFragment : BaseDialogFragment<FragmentDialogLungCommonBinding>() {

    private var dialogContent = ""//dialog内容
    private var dialogType = ""//dialog类型  1成功类型  2错误类型

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startCommonDialogFragment(

            fragmentManager: FragmentManager,
            dialogContent: String,
            dialogType: String,
        ): LungCommonDialogFragment {

            val dialogFragment = LungCommonDialogFragment()

            dialogFragment.show(fragmentManager, LungCommonDialogFragment::javaClass.toString())

            val bundle = Bundle()

            bundle.putString(Constants.commonDialogContent, dialogContent)
            bundle.putString(Constants.commonDialogType, dialogType)

            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    override fun start(dialog: Dialog?) {

    }

    override fun initView() {
        binding.tvLungCommonDialogContent.text = dialogContent
        if (dialogType == "1") {
            binding.llLungCommonDialogConfirm.setBackgroundResource(R.drawable.confirm_bg_with_radius_two)
            binding.tvLungCommonDialogConfirm.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
        } else {
            binding.llLungCommonDialogConfirm.setBackgroundResource(R.drawable.confirm_bg_with_stroke)
            binding.tvLungCommonDialogConfirm.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }
    }

    override fun initListener() {
        binding.ivLungCommonDialogClose.setNoRepeatListener {
            dismiss()
        }
        binding.llLungCommonDialogConfirm.setNoRepeatListener {
            dismiss()
        }
    }

    override fun initData() {
        dialogContent = arguments?.getString(Constants.commonDialogContent, "").toString()
        dialogType = arguments?.getString(Constants.commonDialogType, "").toString()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_lung_common
    }
}