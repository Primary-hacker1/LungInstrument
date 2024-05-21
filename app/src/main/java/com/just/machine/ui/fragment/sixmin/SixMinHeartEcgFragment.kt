package com.just.machine.ui.fragment.sixmin

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.ui.activity.SixMinDetectActivity
import com.just.machine.util.ECGDataParse
import com.just.news.databinding.FragmentSixminHeartEcgBinding
import com.seeker.luckychart.charts.ECGChartView.OnVisibleCoorPortChangedListener
import com.seeker.luckychart.model.chartdata.ECGChartData
import com.seeker.luckychart.model.container.ECGPointContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SixMinHeartEcgFragment : CommonBaseFragment<FragmentSixminHeartEcgBinding>() {

    private lateinit var mActivity: SixMinDetectActivity

    override fun loadData() {
        lifecycleScope.launch(Dispatchers.Default) {
            val dataParse = ECGDataParse(mActivity)
            val count = dataParse.values.size
            val containers = arrayOfNulls<ECGPointContainer>(count)

            for (i in 0 until count) {
                val container1 = ECGPointContainer.create(dataParse.values)
                container1.isDrawRpeak = true
                container1.isDrawNoise = true
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
        binding.sixminStaticEcgSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val percent = 1f * progress / seekBar.max
                    binding.sixminStaticHeartEcg.setProgress(percent)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.sixminStaticHeartEcg.setOnVisibleCoorPortChangedListener(OnVisibleCoorPortChangedListener { visiblePort, maxPort ->
            val progress = visiblePort.left / (maxPort.width() - visiblePort.width())
            binding.sixminStaticEcgSeekbar.progress = (binding.sixminStaticEcgSeekbar.max * progress).toInt()
        })

        binding.sixminStaticEcgClose.setNoRepeatListener {
            mActivity.finish()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentSixminHeartEcgBinding =
        FragmentSixminHeartEcgBinding.inflate(inflater, container, false)
}