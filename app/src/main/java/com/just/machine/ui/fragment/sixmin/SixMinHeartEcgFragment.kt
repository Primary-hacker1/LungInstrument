package com.just.machine.ui.fragment.sixmin

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.viewmodel.LiveDataEvent
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.ui.activity.SixMinDetectActivity
import com.just.machine.ui.dialog.SixMinCaptureEcgDialogFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.CommonUtil
import com.just.machine.util.ECGDataParse
import com.just.machine.util.FileUtil
import com.just.machine.util.SeekBarPopUtils
import com.just.news.databinding.FragmentSixminHeartEcgBinding
import com.seeker.luckychart.charts.ECGChartView.OnVisibleCoorPortChangedListener
import com.seeker.luckychart.model.chartdata.ECGChartData
import com.seeker.luckychart.model.container.ECGPointContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class SixMinHeartEcgFragment : CommonBaseFragment<FragmentSixminHeartEcgBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var mActivity: SixMinDetectActivity

    override fun loadData() {
        lifecycleScope.launch(Dispatchers.Default) {
            val dataParse = ECGDataParse(mActivity)
            val count = dataParse.values.size
            val containers = arrayOfNulls<ECGPointContainer>(count)

            for (i in 0 until count) {
                val container1 = ECGPointContainer.create(dataParse.values)
                container1.isDrawRpeak = false
                container1.isDrawNoise = false
                containers[i] = container1
            }
            val chartData = ECGChartData.create(*containers)
            binding.sixminStaticHeartEcg.chartData = chartData
            binding.sixminStaticHeartEcg.applyRenderUpdate()
        }
    }

    override fun initView() {
        if (activity is SixMinDetectActivity) {
            mActivity = activity as SixMinDetectActivity
        }
        viewModel.getSixMinReportInfoById(
            mActivity.sixMinPatientId.toLong(),
            mActivity.sixMinReportNo
        )
    }

    override fun initListener() {
        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.QuerySixMinReportInfoSuccess -> {
                    it.any?.let { it1 -> beanQuery(it1) }
                }
            }
        }
        binding.sixminStaticEcgSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val percent = 1f * progress / seekBar.max
                    binding.sixminStaticHeartEcg.setProgress(percent)
                    SeekBarPopUtils.move(mActivity,progress,CommonUtil.secondsToMMSS((percent*360).toInt()),seekBar)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                SeekBarPopUtils.showPop(mActivity,seekBar)
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                SeekBarPopUtils.dismiss()
            }
        })

        binding.sixminStaticHeartEcg.setOnVisibleCoorPortChangedListener(
            OnVisibleCoorPortChangedListener { visiblePort, maxPort ->
                val progress = visiblePort.left / (maxPort.width() - visiblePort.width())
                binding.sixminStaticEcgSeekbar.progress =
                    (binding.sixminStaticEcgSeekbar.max * progress).toInt()
            })

        binding.sixminStaticEcgClose.setNoRepeatListener {
            mActivity.finish()
        }
        binding.sixminStaticEcgCapture.setNoRepeatListener {
            val captureEcgDialogFragment =
                SixMinCaptureEcgDialogFragment.startSixMinCaptureEcgDialogFragment(mActivity.supportFragmentManager)
            captureEcgDialogFragment.setCaptureEcgDialogListener(object :
                SixMinCaptureEcgDialogFragment.CaptureEcgDialogListener {
                override fun onClickSaveEcg(imagePreview: Bitmap,type:Int) {
                    val ecgSavePath =
                        File.separator + "sixmin/sixminreportpng" + File.separator + mActivity.sixMinReportNo
                    val file = File(
                        mActivity.getExternalFilesDir("")?.absolutePath,
                        ecgSavePath + File.separator + "imageEcg${type}.png"
                    )
                    val saveBitmapToFile = FileUtil.getInstance(mActivity)
                        .saveBitmapToFile(imagePreview, file.absolutePath)
                    if(saveBitmapToFile){
                        mActivity.showMsg("心电截图保存成功")
                        if(type == 3){
                            mActivity.sixMinReportBloodHeartEcg.jietuOr = "1"
                            viewModel.setSixMinReportHeartEcg(mActivity.sixMinReportBloodHeartEcg)
                        }
                    }else{
                        mActivity.showMsg("心电截图保存失败")
                    }
                }
            })
        }
    }

    private fun beanQuery(any: Any) {
        try {
            if (any is List<*>) {
                val datas = any as MutableList<*>
                val sixMinRecordsBean = datas[0] as SixMinRecordsBean
                mActivity.sixMinReportBloodHeartEcg = sixMinRecordsBean.heartEcgBean[0]
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentSixminHeartEcgBinding =
        FragmentSixminHeartEcgBinding.inflate(inflater, container, false)

    override fun onPause() {
        binding.sixminStaticHeartEcg.onPause()
        super.onPause()
    }

    override fun onResume() {
        binding.sixminStaticHeartEcg.onResume()
        super.onResume()
    }
}