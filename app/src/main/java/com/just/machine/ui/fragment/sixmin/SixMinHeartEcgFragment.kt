package com.just.machine.ui.fragment.sixmin

import android.graphics.Bitmap
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.viewmodel.LiveDataEvent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.just.machine.model.sixmininfo.SixMinEcgBean
import com.just.machine.model.sixmininfo.SixMinRecordsBean
import com.just.machine.ui.activity.SixMinDetectActivity
import com.just.machine.ui.dialog.SixMinCaptureEcgDialogFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.CommonUtil
import com.just.machine.util.ECGDataParse
import com.just.machine.util.FileUtil
import com.just.machine.util.SeekBarPopUtils
import com.just.news.databinding.FragmentSixminHeartEcgBinding
import com.seeker.luckychart.charts.ECGChartView
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
    private var visibleLeft: Int = 0 //可见试图的左坐标，也是数据的起始位置，随着seekbar的滑动，起始位置也会随着变化
    private var maxTime: Int = 0
    private var ecgBeanList = mutableListOf<SixMinEcgBean>()
    private var ecgDataList = mutableListOf<MutableList<Float>>()

    override fun loadData() {
        viewModel.getSixMinReportInfoById(
            mActivity.sixMinPatientId.toLong(), mActivity.sixMinReportNo
        )
        lifecycleScope.launch(Dispatchers.Default) {
//            val path =
//                "SixMin/SixMinReportEcg" + File.separator + mActivity.sixMinReportNo + File.separator + "ecgData.json"
//            val file = File(Environment.getExternalStorageDirectory().absolutePath, path)
//            if (file.exists()) {
//                val readEcg = FileUtil.readEcg(file.absolutePath)
//                readEcg.entries.forEach {
//                    ecgBeanList.add(it.value)
//                }
//                maxTime = 360 - readEcg.keys.first()+1
//                withContext(Dispatchers.Main) {
//                    binding.sixminStaticEcgEndTime.text = CommonUtil.secondsToMMSS(maxTime)
//                    ecgBeanList.forEachIndexed { _, it ->
//                        binding.sixminStaticHeartEcg.showAllLine(it.ecgList as ArrayList<Float>?)
//                    }
//                }
//            }

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
                    SeekBarPopUtils.move(
                        mActivity,
                        progress,
                        CommonUtil.secondsToMMSS((percent * maxTime).toInt()),
                        seekBar
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                SeekBarPopUtils.showPop(mActivity, seekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                SeekBarPopUtils.dismiss()
            }
        })

        binding.sixminStaticHeartEcg.setOnVisibleCoorPortChangedListener(
            ECGChartView.OnVisibleCoorPortChangedListener { visiblePort, maxPort ->
                visibleLeft = visiblePort.left.toInt()
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
                override fun onClickSaveEcg(imagePreview: Bitmap, type: Int) {
                    val ecgSavePath =
                        File.separator + "SixMin/SixMinReportPng" + File.separator + mActivity.sixMinReportNo
                    val file = File(
                        Environment.getExternalStorageDirectory().absolutePath,
                        ecgSavePath + File.separator + "imageEcg${type}.png"
                    )
                    val saveBitmapToFile = FileUtil.getInstance(mActivity)
                        .saveBitmapToFile(imagePreview, file.absolutePath)
                    if (saveBitmapToFile) {
                        mActivity.showMsg("心电截图保存成功")
                        if (type == 3) {
                            mActivity.sixMinReportBloodHeartEcg.jietuOr = "1"
                            viewModel.setSixMinReportHeartEcg(mActivity.sixMinReportBloodHeartEcg)
                        }
                    } else {
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
}