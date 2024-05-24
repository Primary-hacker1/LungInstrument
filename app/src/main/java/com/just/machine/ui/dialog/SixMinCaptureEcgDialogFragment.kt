package com.just.machine.ui.dialog

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.model.Constants
import com.just.machine.util.ECGDataParse
import com.just.news.R
import com.just.news.databinding.FragmentDialogSixminCaptureEcgBinding
import com.seeker.luckychart.soft.LuckySoftRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 6分钟心电截图dialog
 */
class SixMinCaptureEcgDialogFragment : BaseDialogFragment<FragmentDialogSixminCaptureEcgBinding>() {

    private lateinit var listener: CaptureEcgDialogListener
    private lateinit var imagePreview:Bitmap

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startSixMinCaptureEcgDialogFragment(

            fragmentManager: FragmentManager,

            ): SixMinCaptureEcgDialogFragment {

            val dialogFragment = SixMinCaptureEcgDialogFragment()

            dialogFragment.show(
                fragmentManager,
                SixMinCaptureEcgDialogFragment::javaClass.toString()
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
        binding.ivCaptureClose.setNoRepeatListener {
            dismiss()
        }
        binding.tvCaptureHighEcg.setNoRepeatListener {
            binding.llCaptureAction.visibility = View.GONE
            binding.llCaptureSave.visibility = View.VISIBLE
            computerEcg(1)
        }
        binding.tvCaptureLowEcg.setNoRepeatListener {
            binding.llCaptureAction.visibility = View.GONE
            binding.llCaptureSave.visibility = View.VISIBLE
            computerEcg(2)
        }
        binding.tvCaptureAvgEcg.setNoRepeatListener {
            binding.llCaptureAction.visibility = View.GONE
            binding.llCaptureSave.visibility = View.VISIBLE
            computerEcg(3)
        }
        binding.tvCaptureSave.setNoRepeatListener {
            listener.onClickSaveEcg(imagePreview)
            dismiss()
        }
        binding.tvCaptureCancel.setNoRepeatListener {
            dismiss()
        }
    }

    private fun computerEcg(type: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val dataParse = ECGDataParse(activity)
            imagePreview = LuckySoftRenderer.instantiate(
                activity!!,
                dataParse.values
            ).startRender()
            withContext(Dispatchers.Main){
                binding.ivEcgPreview.setImageBitmap(imagePreview)
            }
        }
        when (type) {
            1 -> {
                //最快心率
                binding.tvCaptureTitle.text = "最快心电图预览"
                binding.tvCaptureEcgHeart.text = "心率: 71bmp"
            }

            2 -> {
                //最慢心率
                binding.tvCaptureTitle.text = "最慢心电图预览"
                binding.tvCaptureEcgHeart.text = "心率: 65bmp"
            }

            else -> {
                //平均心率
                binding.tvCaptureTitle.text = "截取心电图预览"
                binding.tvCaptureEcgHeart.text = "心率: 68bmp"
            }
        }
    }

    override fun initData() {

    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_capture_ecg
    }

    interface CaptureEcgDialogListener {
        fun onClickSaveEcg(imagePreview: Bitmap)
    }

    fun setCaptureEcgDialogListener(listener: CaptureEcgDialogListener) {
        this.listener = listener
    }
}