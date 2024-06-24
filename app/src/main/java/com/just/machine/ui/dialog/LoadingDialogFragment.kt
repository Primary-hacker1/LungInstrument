package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.just.machine.model.Constants
import com.just.news.R
import com.just.news.databinding.FragmentDialogLoadingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/6/19
 * loading弹框
 */
@AndroidEntryPoint
class LoadingDialogFragment:  BaseDialogFragment<FragmentDialogLoadingBinding>() {

    private var dialogContent = ""

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startLoadingDialogFragment(

            fragmentManager: FragmentManager,
            content:String
        ): LoadingDialogFragment {

            val dialogFragment = LoadingDialogFragment()
            dialogFragment.show(
                fragmentManager,
                LoadingDialogFragment::javaClass.toString()
            )

            val bundle = Bundle()

            bundle.putString(Constants.loadingDialogContent, content)

            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    override fun start(dialog: Dialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {
        binding.loadingTips.text = dialogContent
    }

    override fun initListener() {

    }

    override fun initData() {
        dialogContent = arguments?.getString(Constants.loadingDialogContent, "").toString()
    }

    override fun getLayout(): Int {
       return R.layout.fragment_dialog_loading
    }
}