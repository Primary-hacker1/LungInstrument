package com.just.machine.ui.dialog

import android.app.Dialog
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.news.R
import com.just.news.databinding.FragmentDialogSixminPermissionBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 6分钟蓝牙设置和删除记录权限dialog
 */
@AndroidEntryPoint
class SixMinPermissionDialogFragment : BaseDialogFragment<FragmentDialogSixminPermissionBinding>() {

    private lateinit var listener: SixMinPermissionDialogListener

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startPermissionDialogFragment(

            fragmentManager: FragmentManager,
        ): SixMinPermissionDialogFragment {

            val dialogFragment = SixMinPermissionDialogFragment()

            dialogFragment.show(
                fragmentManager,
                SixMinPermissionDialogFragment::javaClass.toString()
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
        binding.tvPermissionConfirm.setNoRepeatListener {
            listener.onClickConfirm(binding.sixminPermissionPwd.text.toString().trim())
        }
        binding.tvPermissionCancel.setNoRepeatListener {
            dismiss()
        }
    }

    override fun initData() {

    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_permission
    }

    interface SixMinPermissionDialogListener {
        fun onClickConfirm(pwd: String)
    }

    fun setOnConfirmClickListener(listener: SixMinPermissionDialogListener) {
        this.listener = listener
    }
}