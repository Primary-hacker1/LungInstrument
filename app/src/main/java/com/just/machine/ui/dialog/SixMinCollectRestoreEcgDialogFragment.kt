package com.just.machine.ui.dialog

import android.app.Dialog
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentDialogSixminCollectRestoreEcgBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SixMinCollectRestoreEcgDialogFragment :
    BaseDialogFragment<FragmentDialogSixminCollectRestoreEcgBinding>() {

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startRestoreEcgDialogFragment(

            fragmentManager: FragmentManager,

            ): SixMinCollectRestoreEcgDialogFragment {

            val dialogFragment = SixMinCollectRestoreEcgDialogFragment()

            dialogFragment.show(fragmentManager, SixMinCollectRestoreEcgDialogFragment::javaClass.toString())

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
        LiveDataBus.get().with("simMinRestore").observe(this, Observer {
            binding.sixminRestoreTvEcgTime.text =  it.toString()
        })
        binding.sixminReportBtnRestoreEcgConfirm.setNoRepeatListener {
            dismiss()
        }
    }

    override fun initData() {

    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_collect_restore_ecg
    }
}