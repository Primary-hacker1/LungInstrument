package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.model.Constants
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentDialogSixminCollectRestoreEcgBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 6分钟试验采集运动后恢复心率dialog
 */
@AndroidEntryPoint
class SixMinCollectRestoreEcgDialogFragment :
    BaseDialogFragment<FragmentDialogSixminCollectRestoreEcgBinding>() {

    private var timeRemaining = ""

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startRestoreEcgDialogFragment(

            fragmentManager: FragmentManager,
            timeRemain:String

            ): SixMinCollectRestoreEcgDialogFragment {

            val dialogFragment = SixMinCollectRestoreEcgDialogFragment()

            dialogFragment.show(
                fragmentManager,
                SixMinCollectRestoreEcgDialogFragment::javaClass.toString()
            )
            val bundle = Bundle()

            bundle.putString("timeRemain", timeRemain)

            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }

    override fun start(dialog: Dialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {
        if(timeRemaining == ""){
            timeRemaining = "59"
        }
        binding.sixminRestoreTvEcgTime.text = timeRemaining
    }

    override fun initListener() {
        LiveDataBus.get().with("simMinRestore").observe(this, Observer {
            timeRemaining = it.toString()
            binding.sixminRestoreTvEcgTime.text = timeRemaining
            if(timeRemaining == "0"){
               dismiss()
            }
        })
        binding.sixminReportBtnRestoreEcgConfirm.setNoRepeatListener {
            dismiss()
        }
    }

    override fun initData() {
        timeRemaining = arguments?.getString("timeRemain","").toString()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_collect_restore_ecg
    }
}