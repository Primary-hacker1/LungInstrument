package com.just.machine.ui.dialog

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.common.base.toast
import com.google.gson.Gson
import com.just.machine.model.Constants
import com.just.machine.model.sixmininfo.SixMinEcgBean
import com.just.machine.util.ECGDataParse
import com.just.news.R
import com.just.news.databinding.FragmentDialogSixminCaptureEcgBinding
import com.seeker.luckychart.model.ECGPointValue
import com.seeker.luckychart.soft.LuckySoftRenderer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * 6分钟心电截图dialog
 */
@AndroidEntryPoint
class SixMinCaptureEcgDialogFragment : BaseDialogFragment<FragmentDialogSixminCaptureEcgBinding>() {

    private lateinit var listener: CaptureEcgDialogListener
    private lateinit var imagePreview: Bitmap
    private var type: Int = 0 //心电回放截图类型 1 最快心电 2最慢心电 3截取心电
    private var ecgVisibleLeft = 0 //心电回放位置
    private var reportNo = "" //报告编号
    private var ecgBeanList = mutableListOf<SixMinEcgBean>()
    private var ecgMap = sortedMapOf<Int, SixMinEcgBean>()
    private var ecgInfoList = mutableListOf<Float>()
    private var captureTime = 0
    private var captureHeartBeat = "0"
    private var captureEcgData = ""

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startSixMinCaptureEcgDialogFragment(

            fragmentManager: FragmentManager, visibleLeft: Int = 0, reportNo: String = ""

        ): SixMinCaptureEcgDialogFragment {

            val dialogFragment = SixMinCaptureEcgDialogFragment()

            dialogFragment.show(
                fragmentManager, SixMinCaptureEcgDialogFragment::javaClass.toString()
            )

            val bundle = Bundle()

            bundle.putInt(Constants.sixMinEcgVisibleLeft, visibleLeft)
            bundle.putString(Constants.sixMinReportNo, reportNo)

            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }

    override fun start(dialog: Dialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {
        lifecycleScope.launch(Dispatchers.IO) {
            val path =
                "SixMin/SixMinReportEcg" + File.separator + reportNo + File.separator + "ecgData.json"
            val file = File(Environment.getExternalStorageDirectory().absolutePath, path)
            if (file.exists()) {
                try {
                    val dataParse = ECGDataParse(requireContext(), file.absolutePath)
                    ecgMap = dataParse.values
                    if (ecgMap.isNotEmpty()) {
                        ecgBeanList.clear()
                        ecgMap.entries.forEach {
                            ecgBeanList.add(it.value)
                        }
                        ecgInfoList.clear()
                        ecgBeanList.forEach {
                            ecgInfoList.addAll(it.ecgList)
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        toast("解析心电数据失败!")
                    }
                }
            }
        }
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
            listener.onClickSaveEcg(
                imagePreview, type, captureTime, captureHeartBeat, captureEcgData
            )
            dismiss()
        }
        binding.tvCaptureCancel.setNoRepeatListener {
            dismiss()
        }
    }

    private fun computerEcg(type: Int) {
        //一条完整的心电波形至少包含1960个点的数据，所有截图的时候，根据起始和结束的index来截取点的数据，然后将数据转换成bitmap
        this.type = type
        when (type) {
            1 -> {
                //最快心率
                handleCaptureEcgData(1)
            }

            2 -> {
                //最慢心率
                handleCaptureEcgData(2)
            }

            3 -> {
                //当前截取的心率
                try {
                    if (ecgMap.isNotEmpty() && ecgBeanList.isNotEmpty()) {
                        //截取心电图
                        binding.tvCaptureTitle.text = "截取心电图预览"
                        var sum = 0
                        var captureIndex = 0
                        for (index in ecgBeanList.indices) {
                            if (sum < ecgVisibleLeft) {
                                sum += ecgBeanList[index].ecgList.size
                            } else {
                                captureIndex = index
                                break
                            }
                        }
                        val sixMinEcgBean = ecgBeanList[captureIndex]
                        captureHeartBeat = sixMinEcgBean.heartBeat
                        captureTime = ecgMap.keys.elementAt(captureIndex)

                        val values: Array<ECGPointValue?>
                        val ecgDataList = mutableListOf<Float>()
                        if (ecgInfoList.size > 1960) {
                            values = arrayOfNulls(1960)
                            var ecgPointValue: ECGPointValue
                            ecgInfoList.forEachIndexed { index, it ->
                                if (index >= ecgVisibleLeft && index < ecgVisibleLeft + 1960) {
                                    ecgPointValue = ECGPointValue()
                                    ecgPointValue.coorY = -it
                                    ecgPointValue.coorX = 0.0f
                                    values[index - ecgVisibleLeft] = ecgPointValue
                                    ecgDataList.add(it)
                                }
                            }
                        } else {
                            values = arrayOfNulls(ecgInfoList.size)
                            var ecgPointValue: ECGPointValue
                            ecgInfoList.forEachIndexed { index, it ->
                                ecgPointValue = ECGPointValue()
                                ecgPointValue.coorY = -it
                                ecgPointValue.coorX = 0.0f
                                values[index] = ecgPointValue
                                ecgDataList.add(it)
                            }
                        }
                        captureEcgData = Gson().toJson(ecgDataList)
                        imagePreview = LuckySoftRenderer.instantiate(
                            requireContext(), values
                        ).startRender()
                        binding.ivEcgPreview.setImageBitmap(imagePreview)
                        binding.tvCaptureEcgHeart.text = "心率: ${captureHeartBeat}bmp"
                    }
                } catch (e: Exception) {
                    Log.e("handleCaptureEcgData", e.message.toString())
                }
            }
        }
    }

    private fun handleCaptureEcgData(type: Int) {
        try {
            if (ecgMap.isNotEmpty() && ecgBeanList.isNotEmpty()) {
                if (type == 1) {
                    //最快心率
                    binding.tvCaptureTitle.text = "最快心电图预览"
                    captureHeartBeat = ecgBeanList.maxOf { it.heartBeat }
                } else {
                    //最慢心率
                    binding.tvCaptureTitle.text = "最慢心电图预览"
                    captureHeartBeat = ecgBeanList.minOf { it.heartBeat }
                }
                val filterBean = ecgBeanList.find { it.heartBeat == captureHeartBeat }
                captureTime = ecgMap.entries.firstOrNull {
                    it.value.heartBeat == filterBean?.heartBeat
                }?.key!!
                val filterIndex = ecgBeanList.indexOfFirst { it.heartBeat == captureHeartBeat }
                val values: Array<ECGPointValue?>
                var sum = 0
                val filterList = mutableListOf<Float>()
                if (ecgInfoList.size > 1960) {
                    values = arrayOfNulls(1960)
                    var ecgPointValue: ECGPointValue
                    ecgBeanList.forEachIndexed { index, it ->
                        if (index >= filterIndex && sum <= 1960) {
                            filterList.addAll(it.ecgList)
                            sum += it.ecgList.size
                        }
                    }
                    if (filterList.size > 1960) {
                        val ecgDataList = mutableListOf<Float>()
                        filterList.subList(0, 1960).forEachIndexed { index, it ->
                            ecgDataList.add(it)
                            ecgPointValue = ECGPointValue()
                            ecgPointValue.coorY = -it
                            ecgPointValue.coorX = 0.0f
                            values[index] = ecgPointValue
                        }
                        captureEcgData = Gson().toJson(ecgDataList)
                    } else {
                        filterList.forEachIndexed { index, it ->
                            ecgPointValue = ECGPointValue()
                            ecgPointValue.coorY = -it
                            ecgPointValue.coorX = 0.0f
                            values[index] = ecgPointValue
                        }
                        captureEcgData = Gson().toJson(filterList)
                    }
                } else {
                    values = arrayOfNulls(ecgInfoList.size)
                    var ecgPointValue: ECGPointValue
                    ecgInfoList.forEachIndexed { index, it ->
                        ecgPointValue = ECGPointValue()
                        ecgPointValue.coorY = -it
                        ecgPointValue.coorX = 0.0f
                        values[index] = ecgPointValue
                    }
                    captureEcgData = Gson().toJson(ecgInfoList)
                }

                imagePreview = LuckySoftRenderer.instantiate(
                    requireContext(), values
                ).startRender()
                binding.ivEcgPreview.setImageBitmap(imagePreview)
                binding.tvCaptureEcgHeart.text = "心率: ${captureHeartBeat}bmp"
            }
        } catch (e: Exception) {
            Log.e("handleCaptureEcgData", e.message.toString())
        }
    }

    override fun initData() {
        ecgVisibleLeft = arguments?.getInt(Constants.sixMinEcgVisibleLeft, 0)!!

        reportNo = arguments?.getString(Constants.sixMinReportNo, "").toString()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_capture_ecg
    }

    interface CaptureEcgDialogListener {
        fun onClickSaveEcg(
            imagePreview: Bitmap, type: Int, time: Int, heartBeat: String, ecgData: String
        )
    }

    fun setCaptureEcgDialogListener(listener: CaptureEcgDialogListener) {
        this.listener = listener
    }
}