package com.just.machine.ui.fragment.calibration

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.calibration.FlowAdapter
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.BaseUtil
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentFlowBinding
import dagger.hilt.android.AndroidEntryPoint
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.model.Viewport
import lecho.lib.hellocharts.view.LineChartView

/**
 *create by 2020/6/19
 * 流量定标
 *@author zt
 */
@AndroidEntryPoint
class FlowFragment : CommonBaseFragment<FragmentFlowBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val flowAdapter by lazy {
        FlowAdapter(requireContext())
    }

    override fun initView() {

        binding.rvFlow.layoutManager = LinearLayoutManager(requireContext())

        flowAdapter.setItemClickListener { item, position ->
            flowAdapter.toggleItemBackground(position)
        }

        flowAdapter.setItemsBean(
            mutableListOf
                (FlowBean(0, 1, "容积1", "3", "3.003"))
        )

        binding.rvFlow.adapter = flowAdapter


        binding.llStart.setNoRepeatListener {
            if (Constants.isDebug) {
                val smallRangeFlow = 120 // 例如，120 L/min
                val largeRangeFlow = 3000 // 例如，3000 L/min

                val data = MudbusProtocol.generateFlowCalibrationCommand(
                    smallRangeFlow,
                    largeRangeFlow,
                )

                LogUtils.e(tag + BaseUtil.bytes2HexStr(data) + "发送的数据")

                LiveDataBus.get().with("测试").value = data

                return@setNoRepeatListener
            }

            SerialPortManager.sendMessage(MudbusProtocol.FLOW_CALIBRATION_COMMAND)//发送流量定标
        }

        LiveDataBus.get().with("测试").observe(this) {//解析串口消息
            if (it is ByteArray) {
                LogUtils.e(tag + BaseUtil.bytes2HexStr(it) + "字节长度" + BaseUtil.bytes2HexStr(it).length)
                val data = MudbusProtocol.parseFlowCalibrationData(it)
                if (data != null) {
                    val (smallRangeFlow,largeRangeFlow) = data

                    LogUtils.e(tag+smallRangeFlow)

                    LogUtils.e(tag+largeRangeFlow)

                }

            }
        }

    }

    override fun initListener() {

    }

    /**
     * @param previewChart  另一个区域折线图 echo.lib.hellocharts.view
     * */
    @SuppressLint("ClickableViewAccessibility")
    private fun lineChartView(previewChart: LineChartView, entries: MutableList<PointValue>) {

        val line = Line(entries)

        line.color = ContextCompat.getColor(requireContext(), R.color.colorPrimary) // 设置线条颜色

        line.strokeWidth = 2 // 设置线条粗细

        line.pointRadius = 1 // 设置点的半径大小

        line.isCubic = true // 设置线条为曲线

        val lines: MutableList<Line> = ArrayList()

        val data = LineChartData(lines)

        lines.add(line)

        data.lines = lines

        // 创建X轴
        val axisX = Axis()

        axisX.setTextColor(Color.BLACK) // 设置字体颜色

//        axisX.setName("X Axis") // 设置轴名称

        axisX.setMaxLabelChars(6) // 最多几个字符显示在x轴的标签里

        // 创建Y轴
        val axisY: Axis = Axis().setHasLines(true)
        axisY.setTextColor(Color.BLACK)
//        axisY.setName("Y Axis")

        data.axisXBottom = axisX
        data.axisYLeft = axisY

        previewChart.lineChartData = data

        previewChart.zoomType = ZoomType.HORIZONTAL // 限制为水平缩放

        // 自动计算视口
        val viewport = Viewport(previewChart.maximumViewport)
        viewport.top = 110f // 为顶部增加一些额外空间

        previewChart.maximumViewport = viewport
        previewChart.currentViewport = viewport

        // 假设你已经初始化了图表数据
        // 设置最大视口和初始视口
        val maxViewport = Viewport(previewChart.maximumViewport)
        previewChart.maximumViewport = maxViewport


        val newViewport = Viewport(previewChart.maximumViewport)
        // 确定你希望的视口宽度，例如，始终显示最大视口宽度的10%
        val desiredWidth = maxViewport.width() * 0.05f

        // 计算中心点
        val midPointX = newViewport.centerX()

        // 设置新的视口，以中心点为中心，左右扩展 desiredWidth / 2
        val modifiedViewport = Viewport(
            midPointX - desiredWidth / 2,
            newViewport.top,
            midPointX + desiredWidth / 2,
            newViewport.bottom
        )

        previewChart.currentViewport = modifiedViewport

    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFlowBinding.inflate(inflater, container, false)
}