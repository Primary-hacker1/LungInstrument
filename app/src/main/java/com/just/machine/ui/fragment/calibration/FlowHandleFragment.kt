package com.just.machine.ui.fragment.calibration

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.toast
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.just.machine.dao.calibration.FlowBean
import com.just.machine.dao.calibration.FlowCalibrationResultBean
import com.just.machine.dao.calibration.FlowManualCalibrationResultBean
import com.just.machine.model.Constants
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.calibrate.Definition
import com.just.machine.ui.adapter.calibration.FlowAdapter
import com.just.machine.ui.dialog.LoadingDialogFragment
import com.just.machine.ui.dialog.LungCommonDialogFragment
import com.just.machine.ui.fragment.serial.ModbusProtocol
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.CRC16Util
import com.just.machine.util.FixCountDownTime
import com.just.machine.util.LiveDataBus
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.FragmentFlowHandleBinding
import com.justsafe.libview.util.DateUtils
import com.xxmassdeveloper.mpchartexample.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.util.LinkedList
import java.util.Queue
import java.util.Timer
import kotlin.math.abs
import kotlin.math.sqrt


/**
 *create by 2024/6/19
 * 手动流量定标
 *@author zt
 */
@AndroidEntryPoint
class FlowHandleFragment : CommonBaseFragment<FragmentFlowHandleBinding>() {

    private var usbTransferUtil: USBTransferUtil? = null //usb工具类
    private val viewModel by viewModels<MainViewModel>()
    private var startLoadingDialogFragment: LoadingDialogFragment? = null
    private var isHandleFlowStart = false

    private val strVol = arrayOf(
        "吸气容积1",
        "呼气容积1",
        "吸气容积2",
        "呼气容积2",
        "吸气容积3",
        "呼气容积3",
        "吸气容积4",
        "呼气容积4",
        "吸气容积5",
        "呼气容积5"
    )

    // 容量-时间
    private var inVolSec1DataSet: LineDataSet? = null
    private var outVolSec1DataSet: LineDataSet? = null

    private var inVolSec2DataSet: LineDataSet? = null
    private var outVolSec2DataSet: LineDataSet? = null

    private var inVolSec3DataSet: LineDataSet? = null
    private var outVolSec3DataSet: LineDataSet? = null

    private var inVolSec4DataSet: LineDataSet? = null
    private var outVolSec4DataSet: LineDataSet? = null

    private var inVolSec5DataSet: LineDataSet? = null
    private var outVolSec5DataSet: LineDataSet? = null

    // 流速-容量
    private var inFlowVol1DataSet: LineDataSet? = null
    private var outFlowVol1DataSet: LineDataSet? = null

    private var inFlowVol2DataSet: LineDataSet? = null
    private var outFlowVol2DataSet: LineDataSet? = null

    private var inFlowVol3DataSet: LineDataSet? = null
    private var outFlowVol3DataSet: LineDataSet? = null

    private var inFlowVol4DataSet: LineDataSet? = null
    private var outFlowVol4DataSet: LineDataSet? = null

    private var inFlowVol5DataSet: LineDataSet? = null
    private var outFlowVol5DataSet: LineDataSet? = null

    private var k = 1 //定标计数器
    private var ftemplow = 0
    private var ftemphigh = 0
    private var ftemp = 0f
    private var iscer = false
    private var startsec = 0f
    private var Autoindex = 0
    private var tempvol = 0f
    private var dl = 0.0
    private var dh = 0.0
    private var tempv = 0
    private var tempq = 0
    private var tempcalc = 0f
    private var tempcalcHigh = 0f
    private var ftempHigh = 0f
    private var ManualFlowState = false //false为拉(吸)，true为推(呼)

    private var ADC_OUT_LDES_temp = mutableListOf<Float>()
    private var ADC_IN_LDES_temp = mutableListOf<Float>()
    private var ADC_OUT_HDIM_temp = mutableListOf<Float>()
    private var ADC_IN_HDIM_temp = mutableListOf<Float>()

    private var ADC_OUT_LDES = 0f
    private var ADC_IN_LDES = 0f
    private var ADC_OUT_HDIM = 0f
    private var ADC_IN_HDIM = 0f
    private var iset = 1f

    private var m_AccuracyIn = 0f
    private var m_AccuracyOut = 0f
    private var m_PrecisionIn = 0f
    private var m_PrecisionOut = 0f

    var TempDl: Queue<Double> = LinkedList()
    var TempDh: Queue<Double> = LinkedList()

    var dicvol: MutableMap<Int, MutableList<Float>> = mutableMapOf()

    private var inHaleFlowList = mutableListOf<FlowBean>()
    private var exHaleFlowList = mutableListOf<FlowBean>()

    private val inHaleFlowAdapter by lazy {
        FlowAdapter(requireContext())
    }

    private val exHaleFlowAdapter by lazy {
        FlowAdapter(requireContext())
    }


    override fun loadData() {
        viewModel.getFlows()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView() {
        usbTransferUtil = USBTransferUtil.getInstance()

        binding.rvFlowHandleInhale.layoutManager = LinearLayoutManager(requireContext())

        binding.rvFlowHandleInhale.adapter = inHaleFlowAdapter

        binding.rvFlowHandleExhale.layoutManager = LinearLayoutManager(requireContext())

        binding.rvFlowHandleExhale.adapter = exHaleFlowAdapter

        initLineChart(binding.chartFlowHandleCapacityTime, 1)
        initLineChart(binding.chartFlowHandleFlowCapacity, 2)
    }

    override fun initListener() {
        inHaleFlowAdapter.setItemClickListener { _, position ->
            inHaleFlowAdapter.toggleItemBackground(position)
        }

        exHaleFlowAdapter.setItemClickListener { _, position ->
            exHaleFlowAdapter.toggleItemBackground(position)
        }

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.FLOWS_SUCCESS -> {
                    val flowsBean: MutableList<FlowBean> = ArrayList()
                    if (it.any is List<*>) {
                        val list = it.any as List<*>
                        for (index in list) {
                            if (index is FlowBean) {
                                flowsBean.add(index)
                            }
                        }
                    }
//                    flowAdapter.setItemsBean(flowsBean)
                }
            }
        }

        //点击定标开始
        LiveDataBus.get().with(Constants.clickStartFlowCalibra).observe(this) {
            if (it is String) {
                if (it == Constants.flowHandleCalibra) {
                    //开始手动定标
                    if (ModbusProtocol.isDeviceConnect) {
                        prepareManualFlowCalibration()
                        sendCalibraCommand()
                        startLoadingDialogFragment =
                            LoadingDialogFragment.startLoadingDialogFragment(
                                activity!!.supportFragmentManager,
                                "正在校零..."
                            )
                    } else {
                        toast(getString(R.string.device_without_connection_tips))
                    }
                }
            }
        }
        //点击定标结束
        LiveDataBus.get().with(Constants.clickStopFlowCalibra).observe(this) {
            if (it is String) {
                stopPortSend()
            }
        }

        //串口数据
        LiveDataBus.get().with(Constants.twoSensorSerialCallback).observe(this) {
            try {
                if (isHandleFlowStart) {
                    if (it is ByteArray) {
                        Autoindex++
                        if (Autoindex <= 200) {
                            ftemplow += it[14] + it[15] * 256
                            ftemphigh += it[16] + it[17] * 256
                        }
                        if (Autoindex == 200) {
                            Definition.fzeroLow = ftemplow / Autoindex.toFloat()
                            Definition.fzeroHigh = ftemphigh / Autoindex.toFloat()
                        }
                        if (Autoindex > 200) {
                            calculateFlow(
                                (it[14] + it[15] * 256).toFloat(),
                                (it[16] + it[17] * 256).toFloat()
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                LogUtils.e(e.toString())
            }
        }
    }

    /**
     * 计算流速
     */
    private fun calculateFlow(signallow: Float, signalhigh: Float) {
        var flow = 0f
        dl = (signallow - Definition.fzeroLow).toDouble()
        dh = (signalhigh - Definition.fzeroHigh).toDouble()
        var dtemp = 0.0
        var dtemphigh = 0.0
        TempDl.offer(dl)
        TempDh.offer(dh)

        if (TempDl.size >= 21) {
            TempDl.poll()
            TempDh.poll()

            dtemp = TempDl.sum() / TempDl.size
            dtemphigh = TempDh.sum() / TempDh.size

            if (k == 0) {
                Log.i(TAG, "dtemp 数据: $dtemp")
                startLoadingDialogFragment!!.dismiss()
                if (dtemp > Definition.Noise_AD) {
                    tempv++
                    if (tempv > 1) {
                        iscer = true
                        k = 0
                        if (dtemp < Definition.LDES_AD && dtemp >= Definition.LowLimit_AD) {
                            flow = (sqrt(dtemp) * Definition.Cur_ADC_IN_LDES * iset).toFloat()
                            ftemp += flow / Definition.Sample_rate.toFloat()
                            tempcalc += (sqrt(dtemp) * iset / Definition.Sample_rate.toFloat()).toFloat()
                            tempvol = abs(ftemp + ftempHigh)
                        } else if (dtemp > Definition.LDES_AD && dtemp <= Definition.HighLimit_AD) {
                            flow = (sqrt(dtemp) * Definition.Cur_ADC_IN_LDES).toFloat()
                            ftemp += flow / Definition.Sample_rate.toFloat()
                            tempcalc += (sqrt(dtemp) / Definition.Sample_rate.toFloat()).toFloat()
                            tempvol = abs(ftemp + ftempHigh)
                        } else if (dtemp > Definition.HighLimit_AD || dtemp < Definition.LowLimit_AD) {
                            flow = (sqrt(dtemphigh) * Definition.Cur_ADC_IN_HDIM).toFloat()
                            ftempHigh += flow / Definition.Sample_rate.toFloat()
                            tempcalcHigh += (sqrt(dtemphigh) / Definition.Sample_rate.toFloat()).toFloat()
                            tempvol = abs((ftemp + ftempHigh))
                        }

                        Log.i(
                            TAG,
                            "startsec====$startsec====ftemp + ftempHigh 数据: ${ftemp + ftempHigh}====flow===$flow"
                        )

                        inVolSec1DataSet!!.addEntry(
                            Entry(
                                startsec,
                                (ftemp + ftempHigh)
                            )
                        )
                        inFlowVol1DataSet!!.addEntry(
                            Entry(
                                (ftemp + ftempHigh),
                                flow
                            )
                        )
                        startsec += 0.01f
                        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
                        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
                        binding.chartFlowHandleCapacityTime.invalidate()

                        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
                        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
                        binding.chartFlowHandleFlowCapacity.invalidate()

                        if (dicvol.containsKey(k)) {
                            dicvol[k]!!.add((ftemp + ftempHigh))
                        } else {
                            dicvol[k] = arrayListOf((ftemp + ftempHigh))
                        }
                    }
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && !iscer) {
                    tempv = 0
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && iscer) {
                    tempq++
                    if (tempq == 50) {
                        if (tempvol > 2 && tempvol < 4) {
                            checkCerbra()
                            if (ftemp > 2.6) {
                                ADC_IN_LDES_temp.add(3 / tempcalc)
                                ADC_IN_LDES = ADC_IN_LDES_temp.average().toFloat()
                            } else {
                                if (tempcalcHigh == 0f) {
                                    ADC_IN_HDIM = 0f
                                } else {
                                    ADC_IN_HDIM_temp.add((3 - ftemp) / tempcalcHigh)
                                    ADC_IN_HDIM = ADC_IN_HDIM_temp.average().toFloat()
                                }
                            }
                            resetElement()
                            setPushStyle()
                            k = 1
                        } else {
                            resetParmet(0)
                            setPullStyle()
                            k = 0
                            if (dicvol.containsKey(k))
                                dicvol[k]?.clear()
                        }
                    }
                }
            }
            if (k == 1) {
                if (dtemp < -Definition.Noise_AD) {
                    tempv++
                    if (tempv > 1) {
                        iscer = true
                        k = 1
                        if (dtemp > -Definition.LDES_AD) {
                            flow = (sqrt(-dtemp) * Definition.Cur_ADC_OUT_LDES * iset).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(-dtemp) * iset / Definition.Sample_rate).toFloat()
                            tempvol = abs(-ftemp - ftempHigh)
                        } else if (dtemp < -Definition.LDES_AD && dtemp > Definition.LowLimit_AD) {
                            flow = (sqrt(-dtemp) * Definition.Cur_ADC_OUT_LDES).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(-dtemp) / Definition.Sample_rate).toFloat()
                            tempvol = abs(-ftemp - ftempHigh)
                        } else if (dtemp < Definition.LowLimit_AD) {
                            flow = (sqrt(-dtemphigh) * Definition.Cur_ADC_OUT_HDIM).toFloat()
                            ftempHigh += flow / Definition.Sample_rate
                            tempcalcHigh += (sqrt(-dtemphigh) / Definition.Sample_rate).toFloat()
                            tempvol = abs((-ftemp - ftempHigh))
                        }

                        outVolSec1DataSet!!.addEntry(
                            Entry(
                                startsec,
                                -ftemp - ftempHigh
                            )
                        )
                        outFlowVol1DataSet!!.addEntry(
                            Entry(
                                abs(-ftemp - ftempHigh),
                                -flow
                            )
                        )
                        startsec += 0.01f
                        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
                        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
                        binding.chartFlowHandleCapacityTime.invalidate()

                        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
                        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
                        binding.chartFlowHandleFlowCapacity.invalidate()

                        if (dicvol.containsKey(k)) {
                            dicvol[k]!!.add(-ftemp - ftempHigh)
                        } else {
                            dicvol[k] = arrayListOf(-ftemp - ftempHigh)
                        }
                    }
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && !iscer) {
                    tempv = 0
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && iscer) {
                    tempq++
                    if (tempq == 50) {
                        if (tempvol > 2 && tempvol < 4) {
                            checkCerbra()
                            if (ftemp > 2.6) {
                                ADC_OUT_LDES_temp.add(3 / tempcalc)
                                ADC_OUT_LDES = ADC_OUT_LDES_temp.average().toFloat()
                            } else {
                                ADC_OUT_HDIM = if (tempcalcHigh == 0f) {
                                    0f
                                } else {
                                    ADC_OUT_HDIM_temp.add((3 - ftemp) / tempcalcHigh)
                                    ADC_OUT_HDIM_temp.average().toFloat()
                                }
                            }
                            resetElement()
                            setPullStyle()
                            k = 2
                        } else {
                            resetParmet(1)
                            setPushStyle()
                            k = 1
                            if (dicvol.containsKey(k))
                                dicvol[k]?.clear()
                        }
                    }
                }
            }
            if (k == 2) {
                if (dtemp > Definition.Noise_AD) {
                    tempv++
                    if (tempv > 1) {
                        iscer = true
                        k = 2
                        if (dtemp < Definition.LDES_AD) {
                            flow = (sqrt(dtemp) * Definition.Cur_ADC_IN_LDES * iset).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(dtemp) * iset / Definition.Sample_rate).toFloat()
                            tempvol = abs(ftemp + ftempHigh)
                        } else if (dtemp > Definition.LDES_AD && dtemp < Definition.HighLimit_AD) {
                            flow = (sqrt(dtemp) * Definition.Cur_ADC_IN_LDES).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(dtemp) / Definition.Sample_rate).toFloat()
                            tempvol = abs(ftemp + ftempHigh)
                        } else if (dtemp > Definition.HighLimit_AD) {
                            flow = (sqrt(dtemphigh) * Definition.Cur_ADC_IN_HDIM).toFloat()
                            ftempHigh += flow / Definition.Sample_rate
                            tempcalcHigh += (sqrt(dtemphigh) / Definition.Sample_rate).toFloat()
                            tempvol = abs((ftemp + ftempHigh))
                        }

                        inVolSec2DataSet!!.addEntry(
                            Entry(
                                startsec,
                                ftemp + ftempHigh
                            )
                        )
                        inFlowVol2DataSet!!.addEntry(
                            Entry(
                                ftemp + ftempHigh,
                                flow
                            )
                        )
                        startsec += 0.01f
                        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
                        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
                        binding.chartFlowHandleCapacityTime.invalidate()

                        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
                        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
                        binding.chartFlowHandleFlowCapacity.invalidate()

                        if (dicvol.containsKey(k)) {
                            dicvol[k]!!.add((ftemp + ftempHigh))
                        } else {
                            dicvol[k] = arrayListOf((ftemp + ftempHigh))
                        }
                    }
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && !iscer) {
                    tempv = 0
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && iscer) {
                    tempq++
                    if (tempq == 50) {
                        if (tempvol > 2 && tempvol < 4) {
                            checkCerbra()
                            if (ftemp > 2.6) {
                                ADC_IN_LDES_temp.add(3 / tempcalc)
                                ADC_IN_LDES = ADC_IN_LDES_temp.average().toFloat()
                            } else {
                                if (tempcalcHigh == 0f) {
                                    ADC_IN_HDIM = 0f
                                } else {
                                    ADC_IN_HDIM_temp.add((3 - ftemp) / tempcalcHigh)
                                    ADC_IN_HDIM = ADC_IN_HDIM_temp.average().toFloat()
                                }
                            }
                            resetElement()
                            setPushStyle()
                            k = 3
                        } else {
                            resetParmet(2)
                            setPullStyle()
                            k = 2
                            if (dicvol.containsKey(k))
                                dicvol[k]?.clear()
                        }
                    }
                }
            }
            if (k == 3) {
                if (dtemp < -Definition.Noise_AD) {
                    tempv++
                    if (tempv > 1) {
                        iscer = true
                        k = 3
                        if (dtemp > -Definition.LDES_AD) {
                            flow = (sqrt(-dtemp) * Definition.Cur_ADC_OUT_LDES * iset).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(-dtemp) * iset / Definition.Sample_rate).toFloat()
                            tempvol = abs(-ftemp - ftempHigh)
                        } else if (dtemp < -Definition.LDES_AD && dtemp > Definition.LowLimit_AD) {
                            flow = (sqrt(-dtemp) * Definition.Cur_ADC_OUT_LDES).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(-dtemp) / Definition.Sample_rate).toFloat()
                            tempvol = abs(-ftemp - ftempHigh)
                        } else if (dtemp < Definition.LowLimit_AD) {
                            flow = (sqrt(-dtemphigh) * Definition.Cur_ADC_OUT_HDIM).toFloat()
                            ftempHigh += flow / Definition.Sample_rate
                            tempcalcHigh += (sqrt(-dtemphigh) / Definition.Sample_rate).toFloat()
                            tempvol = abs((-ftemp - ftempHigh))
                        }

                        outVolSec2DataSet!!.addEntry(
                            Entry(
                                startsec,
                                -ftemp - ftempHigh
                            )
                        )
                        outFlowVol2DataSet!!.addEntry(
                            Entry(
                                abs(-ftemp - ftempHigh),
                                -flow
                            )
                        )
                        startsec += 0.01f
                        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
                        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
                        binding.chartFlowHandleCapacityTime.invalidate()

                        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
                        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
                        binding.chartFlowHandleFlowCapacity.invalidate()

                        if (dicvol.containsKey(k)) {
                            dicvol[k]!!.add(-ftemp - ftempHigh)
                        } else {
                            dicvol[k] = arrayListOf(-ftemp - ftempHigh)
                        }
                    }
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && !iscer) {
                    tempv = 0
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && iscer) {
                    tempq++
                    if (tempq == 50) {
                        if (tempvol > 2 && tempvol < 4) {
                            checkCerbra()
                            if (ftemp > 2.6) {
                                ADC_OUT_LDES_temp.add(3 / tempcalc)
                                ADC_OUT_LDES = ADC_OUT_LDES_temp.average().toFloat()
                            } else {
                                if (tempcalcHigh == 0f) {
                                    ADC_OUT_HDIM = 0f
                                } else {
                                    ADC_OUT_HDIM_temp.add((3 - ftemp) / tempcalcHigh)
                                    ADC_OUT_HDIM = ADC_OUT_HDIM_temp.average().toFloat()
                                }
                            }
                            resetElement()
                            setPullStyle()
                            k = 4
                        } else {
                            resetParmet(3)
                            setPushStyle()
                            k = 3
                            if (dicvol.containsKey(k))
                                dicvol[k]?.clear()
                        }
                    }
                }
            }
            if (k == 4) {
                dtemp = TempDl.average()
                if (dtemp > Definition.Noise_AD) {
                    tempv++
                    if (tempv > 1) {
                        iscer = true
                        k = 4
                        if (dtemp < Definition.LDES_AD) {
                            flow = (sqrt(dtemp) * Definition.Cur_ADC_IN_LDES * iset).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(dtemp) / Definition.Sample_rate).toFloat()
                            tempvol = abs(ftemp + ftempHigh)
                        } else if (dtemp > Definition.LDES_AD && dtemp < Definition.HighLimit_AD) {
                            flow = (sqrt(dtemp) * Definition.Cur_ADC_IN_LDES).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(dtemp) / Definition.Sample_rate).toFloat()
                            tempvol = abs(ftemp + ftempHigh)
                        } else if (dtemp > Definition.HighLimit_AD) {
                            flow = (sqrt(dtemphigh) * Definition.Cur_ADC_IN_HDIM).toFloat()
                            ftempHigh += flow / Definition.Sample_rate
                            tempcalcHigh += (sqrt(dtemphigh) / Definition.Sample_rate).toFloat()
                            tempvol = abs((ftemp + ftempHigh))
                        }

                        inVolSec3DataSet!!.addEntry(
                            Entry(
                                startsec,
                                ftemp + ftempHigh
                            )
                        )
                        inFlowVol3DataSet!!.addEntry(
                            Entry(
                                ftemp + ftempHigh,
                                flow
                            )
                        )
                        startsec += 0.01f
                        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
                        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
                        binding.chartFlowHandleCapacityTime.invalidate()

                        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
                        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
                        binding.chartFlowHandleFlowCapacity.invalidate()

                        if (dicvol.containsKey(k)) {
                            dicvol[k]!!.add((ftemp + ftempHigh))
                        } else {
                            dicvol[k] = arrayListOf((ftemp + ftempHigh))
                        }
                    }
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && !iscer) {
                    tempv = 0
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && iscer) {
                    tempq++
                    if (tempq == 50) {
                        if (tempvol > 2 && tempvol < 4) {
                            if (ftemp > 2.6) {
                                ADC_IN_LDES_temp.add(3 / tempcalc)
                                ADC_IN_LDES = ADC_IN_LDES_temp.average().toFloat()
                            } else {
                                if (tempcalcHigh == 0f) {
                                    ADC_IN_HDIM = 0f
                                } else {
                                    ADC_IN_HDIM_temp.add((3 - ftemp) / tempcalcHigh)
                                    ADC_IN_HDIM = ADC_IN_HDIM_temp.average().toFloat()
                                }
                            }
                            checkCerbra()
                            resetElement()
                            setPushStyle()
                            k = 5
                        } else {
                            resetParmet(4)
                            setPullStyle()
                            k = 4
                            if (dicvol.containsKey(k))
                                dicvol[k]?.clear()
                        }
                    }
                }
            }
            if (k == 5) {
                dtemp = TempDl.average()
                if (dtemp < -Definition.Noise_AD) {
                    tempv++
                    if (tempv > 1) {
                        iscer = true
                        k = 5
                        if (dtemp > -Definition.LDES_AD) {
                            flow = (sqrt(-dtemp) * Definition.Cur_ADC_OUT_LDES * iset).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(-dtemp) * iset / Definition.Sample_rate).toFloat()
                            tempvol = abs(-ftemp - ftempHigh)
                        } else if (dtemp < -Definition.LDES_AD && dtemp > Definition.LowLimit_AD) {
                            flow = (sqrt(-dtemp) * Definition.Cur_ADC_OUT_LDES).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(-dtemp) / Definition.Sample_rate).toFloat()
                            tempvol = abs(-ftemp - ftempHigh)
                        } else if (dtemp < Definition.LowLimit_AD) {
                            flow = (sqrt(-dtemphigh) * Definition.Cur_ADC_OUT_HDIM).toFloat()
                            ftempHigh += flow / Definition.Sample_rate
                            tempcalcHigh += (sqrt(-dtemphigh) / Definition.Sample_rate).toFloat()
                            tempvol = abs((-ftemp - ftempHigh))
                        }

                        outVolSec3DataSet!!.addEntry(
                            Entry(
                                startsec,
                                -ftemp - ftempHigh
                            )
                        )
                        outFlowVol3DataSet!!.addEntry(
                            Entry(
                                abs(-ftemp - ftempHigh),
                                -flow
                            )
                        )
                        startsec += 0.01f
                        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
                        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
                        binding.chartFlowHandleCapacityTime.invalidate()

                        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
                        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
                        binding.chartFlowHandleFlowCapacity.invalidate()

                        if (dicvol.containsKey(k)) {
                            dicvol[k]!!.add(-ftemp - ftempHigh)
                        } else {
                            dicvol[k] = arrayListOf(-ftemp - ftempHigh)
                        }
                    }
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && !iscer) {
                    tempv = 0
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && iscer) {
                    tempq++
                    if (tempq == 50) {
                        if (tempvol > 2 && tempvol < 4) {
                            if (ftemp > 2.6) {
                                ADC_OUT_LDES_temp.add(3 / tempcalc)
                                ADC_OUT_LDES = ADC_OUT_LDES_temp.average().toFloat()
                            } else {
                                if (tempcalcHigh == 0f) {
                                    ADC_OUT_HDIM = 0f
                                } else {
                                    ADC_OUT_HDIM_temp.add((3 - ftemp) / tempcalcHigh)
                                    ADC_OUT_HDIM = ADC_OUT_HDIM_temp.average().toFloat()
                                }
                            }
                            checkCerbra()
                            resetElement()
                            setPullStyle()
                            k = 6
                        } else {
                            resetParmet(5)
                            setPushStyle()
                            k = 5
                            if (dicvol.containsKey(k))
                                dicvol[k]?.clear()
                        }
                    }
                }
            }
            if (k == 6) {
                dtemp = TempDl.average()
                if (dtemp > Definition.Noise_AD) {
                    tempv++
                    if (tempv > 1) {
                        iscer = true
                        k = 6
                        if (dtemp < Definition.LDES_AD) {
                            flow = (sqrt(dtemp) * Definition.Cur_ADC_IN_LDES * iset).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(dtemp) / Definition.Sample_rate).toFloat()
                            tempvol = abs(ftemp + ftempHigh)
                        } else if (dtemp > Definition.LDES_AD && dtemp < Definition.HighLimit_AD) {
                            flow = (sqrt(dtemp) * Definition.Cur_ADC_IN_LDES).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(dtemp) / Definition.Sample_rate).toFloat()
                            tempvol = abs(ftemp + ftempHigh)
                        } else if (dtemp > Definition.HighLimit_AD) {
                            flow = (sqrt(dtemphigh) * Definition.Cur_ADC_IN_HDIM).toFloat()
                            ftempHigh += flow / Definition.Sample_rate
                            tempcalcHigh += (sqrt(dtemphigh) / Definition.Sample_rate).toFloat()
                            tempvol = abs((ftemp + ftempHigh))
                        }

                        inVolSec4DataSet!!.addEntry(
                            Entry(
                                startsec,
                                ftemp + ftempHigh
                            )
                        )
                        inFlowVol4DataSet!!.addEntry(
                            Entry(
                                ftemp + ftempHigh,
                                flow
                            )
                        )
                        startsec += 0.01f
                        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
                        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
                        binding.chartFlowHandleCapacityTime.invalidate()

                        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
                        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
                        binding.chartFlowHandleFlowCapacity.invalidate()

                        if (dicvol.containsKey(k)) {
                            dicvol[k]!!.add((ftemp + ftempHigh))
                        } else {
                            dicvol[k] = arrayListOf((ftemp + ftempHigh))
                        }
                    }
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && !iscer) {
                    tempv = 0
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && iscer) {
                    tempq++
                    if (tempq == 50) {
                        if (tempvol > 2 && tempvol < 4) {
                            if (ftemp > 2.6) {
                                ADC_IN_LDES_temp.add(3 / tempcalc)
                                ADC_IN_LDES = ADC_IN_LDES_temp.average().toFloat()
                            } else {
                                if (tempcalcHigh == 0f) {
                                    ADC_IN_HDIM = 0f
                                } else {
                                    ADC_IN_HDIM_temp.add((3 - ftemp) / tempcalcHigh)
                                    ADC_IN_HDIM = ADC_IN_HDIM_temp.average().toFloat()
                                }
                            }
                            checkCerbra()
                            resetElement()
                            setPushStyle()
                            k = 7
                        } else {
                            resetParmet(6)
                            setPullStyle()
                            k = 6
                            if (dicvol.containsKey(k))
                                dicvol[k]?.clear()
                        }
                    }
                }
            }
            if (k == 7) {
                dtemp = TempDl.average()
                if (dtemp < -Definition.Noise_AD) {
                    tempv++
                    if (tempv > 1) {
                        iscer = true
                        k = 7
                        if (dtemp > -Definition.LDES_AD) {
                            flow = (sqrt(-dtemp) * Definition.Cur_ADC_OUT_LDES * iset).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(-dtemp) * iset / Definition.Sample_rate).toFloat()
                            tempvol = abs(-ftemp - ftempHigh)
                        } else if (dtemp < -Definition.LDES_AD && dtemp > Definition.LowLimit_AD) {
                            flow = (sqrt(-dtemp) * Definition.Cur_ADC_OUT_LDES).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(-dtemp) / Definition.Sample_rate).toFloat()
                            tempvol = abs(-ftemp - ftempHigh)
                        } else if (dtemp < Definition.LowLimit_AD) {
                            flow = (sqrt(-dtemphigh) * Definition.Cur_ADC_OUT_HDIM).toFloat()
                            ftempHigh += flow / Definition.Sample_rate
                            tempcalcHigh += (sqrt(-dtemphigh) / Definition.Sample_rate).toFloat()
                            tempvol = abs((-ftemp - ftempHigh))
                        }

                        outVolSec4DataSet!!.addEntry(
                            Entry(
                                startsec,
                                -ftemp - ftempHigh
                            )
                        )
                        outFlowVol4DataSet!!.addEntry(
                            Entry(
                                abs(-ftemp - ftempHigh),
                                -flow
                            )
                        )
                        startsec += 0.01f
                        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
                        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
                        binding.chartFlowHandleCapacityTime.invalidate()

                        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
                        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
                        binding.chartFlowHandleFlowCapacity.invalidate()

                        if (dicvol.containsKey(k)) {
                            dicvol[k]!!.add(-ftemp - ftempHigh)
                        } else {
                            dicvol[k] = arrayListOf(-ftemp - ftempHigh)
                        }
                    }
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && !iscer) {
                    tempv = 0
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && iscer) {
                    tempq++
                    if (tempq == 50) {
                        if (tempvol > 2 && tempvol < 4) {
                            if (ftemp > 2.6) {
                                ADC_OUT_LDES_temp.add(3 / tempcalc)
                                ADC_OUT_LDES = ADC_OUT_LDES_temp.average().toFloat()
                            } else {
                                if (tempcalcHigh == 0f) {
                                    ADC_OUT_HDIM = 0f
                                } else {
                                    ADC_OUT_HDIM_temp.add((3 - ftemp) / tempcalcHigh)
                                    ADC_OUT_HDIM = ADC_OUT_HDIM_temp.average().toFloat()
                                }
                            }
                            checkCerbra()
                            resetElement()
                            setPullStyle()
                            k = 8
                        } else {
                            resetParmet(7)
                            setPushStyle()
                            k = 7
                            if (dicvol.containsKey(k))
                                dicvol[k]?.clear()
                        }
                    }
                }
            }
            if (k == 8) {
                dtemp = TempDl.average()
                if (dtemp > Definition.Noise_AD) {
                    tempv++
                    if (tempv > 1) {
                        iscer = true
                        k = 8
                        if (dtemp < Definition.LDES_AD) {
                            flow = (sqrt(dtemp) * Definition.Cur_ADC_IN_LDES * iset).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(dtemp) / Definition.Sample_rate).toFloat()
                            tempvol = abs(ftemp + ftempHigh)
                        } else if (dtemp > Definition.LDES_AD && dtemp < Definition.HighLimit_AD) {
                            flow = (sqrt(dtemp) * Definition.Cur_ADC_IN_LDES).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(dtemp) / Definition.Sample_rate).toFloat()
                            tempvol = abs(ftemp + ftempHigh)
                        } else if (dtemp > Definition.HighLimit_AD) {
                            flow = (sqrt(dtemphigh) * Definition.Cur_ADC_IN_HDIM).toFloat()
                            ftempHigh += flow / Definition.Sample_rate
                            tempcalcHigh += (sqrt(dtemphigh) / Definition.Sample_rate).toFloat()
                            tempvol = abs((ftemp + ftempHigh))
                        }

                        inVolSec5DataSet!!.addEntry(
                            Entry(
                                startsec,
                                ftemp + ftempHigh
                            )
                        )
                        inFlowVol5DataSet!!.addEntry(
                            Entry(
                                ftemp + ftempHigh,
                                flow
                            )
                        )
                        startsec += 0.01f
                        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
                        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
                        binding.chartFlowHandleCapacityTime.invalidate()

                        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
                        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
                        binding.chartFlowHandleFlowCapacity.invalidate()

                        if (dicvol.containsKey(k)) {
                            dicvol[k]!!.add((ftemp + ftempHigh))
                        } else {
                            dicvol[k] = arrayListOf((ftemp + ftempHigh))
                        }
                    }
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && !iscer) {
                    tempv = 0
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && iscer) {
                    tempq++
                    if (tempq == 50) {
                        if (tempvol > 2 && tempvol < 4) {
                            if (ftemp > 2.6) {
                                ADC_IN_LDES_temp.add(3 / tempcalc)
                                ADC_IN_LDES = ADC_IN_LDES_temp.average().toFloat()
                            } else {
                                if (tempcalcHigh == 0f) {
                                    ADC_IN_HDIM = 0f
                                } else {
                                    ADC_IN_HDIM_temp.add((3 - ftemp) / tempcalcHigh)
                                    ADC_IN_HDIM = ADC_IN_HDIM_temp.average().toFloat()
                                }
                            }
                            checkCerbra()
                            resetElement()
                            setPushStyle()
                            k = 9
                        } else {
                            resetParmet(8)
                            setPullStyle()
                            k = 8
                            if (dicvol.containsKey(k))
                                dicvol[k]?.clear()
                        }
                    }
                }
            }
            if (k == 9) {
                dtemp = TempDl.average()
                if (dtemp < -Definition.Noise_AD) {
                    tempv++
                    if (tempv > 1) {
                        iscer = true
                        k = 9
                        if (dtemp > -Definition.LDES_AD) {
                            flow = (sqrt(-dtemp) * Definition.Cur_ADC_OUT_LDES * iset).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(-dtemp) * iset / Definition.Sample_rate).toFloat()
                            tempvol = abs(-ftemp - ftempHigh)
                        } else if (dtemp < -Definition.LDES_AD && dtemp > Definition.LowLimit_AD) {
                            flow = (sqrt(-dtemp) * Definition.Cur_ADC_OUT_LDES).toFloat()
                            ftemp += flow / Definition.Sample_rate
                            tempcalc += (sqrt(-dtemp) / Definition.Sample_rate).toFloat()
                            tempvol = abs(-ftemp - ftempHigh)
                        } else if (dtemp < Definition.LowLimit_AD) {
                            flow = (sqrt(-dtemphigh) * Definition.Cur_ADC_OUT_HDIM).toFloat()
                            ftempHigh += flow / Definition.Sample_rate
                            tempcalcHigh += (sqrt(-dtemphigh) / Definition.Sample_rate).toFloat()
                            tempvol = abs((-ftemp - ftempHigh))
                        }

                        outVolSec5DataSet!!.addEntry(
                            Entry(
                                startsec,
                                -ftemp - ftempHigh
                            )
                        )
                        outFlowVol5DataSet!!.addEntry(
                            Entry(
                                abs(-ftemp - ftempHigh),
                                -flow
                            )
                        )
                        startsec += 0.01f
                        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
                        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
                        binding.chartFlowHandleCapacityTime.invalidate()

                        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
                        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
                        binding.chartFlowHandleFlowCapacity.invalidate()

                        if (dicvol.containsKey(k)) {
                            dicvol[k]!!.add(-ftemp - ftempHigh)
                        } else {
                            dicvol[k] = arrayListOf(-ftemp - ftempHigh)
                        }
                    }
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && !iscer) {
                    tempv = 0
                }
                if (-Definition.Noise_AD < dtemp && dtemp < Definition.Noise_AD && iscer) {
                    tempq++
                    if (tempq == 50) {
                        if (tempvol > 2 && tempvol < 4) {
                            if (ftemp > 2.6) {
                                ADC_OUT_LDES_temp.add(3 / tempcalc)
                                ADC_OUT_LDES = ADC_OUT_LDES_temp.average().toFloat()
                            } else {
                                if (tempcalcHigh == 0f) {
                                    ADC_OUT_HDIM = 0f
                                } else {
                                    ADC_OUT_HDIM_temp.add((3 - ftemp) / tempcalcHigh)
                                    ADC_OUT_HDIM = ADC_OUT_HDIM_temp.average().toFloat()
                                }
                            }
                            checkCerbra()
                            resetElement()
                            setPullStyle()
                            k = 10
                        } else {
                            resetParmet(9)
                            setPushStyle()
                            k = 9
                            if (dicvol.containsKey(k))
                                dicvol[k]?.clear()
                        }
                    }
                }
            }

            if (k == 10) {
                stopPortSend()
                resetElement()
                ADC_IN_LDES = if (ADC_IN_LDES == 0f) Definition.Cur_ADC_IN_LDES else ADC_IN_LDES
                ADC_OUT_LDES = if (ADC_OUT_LDES == 0f) Definition.Cur_ADC_OUT_LDES else ADC_OUT_LDES
                ADC_IN_HDIM = if (ADC_IN_HDIM == 0f) Definition.Cur_ADC_IN_HDIM else ADC_IN_HDIM
                ADC_OUT_HDIM = if (ADC_OUT_HDIM == 0f) Definition.Cur_ADC_OUT_HDIM else ADC_OUT_HDIM
                Definition.Cur_ADC_IN_LDES = ADC_IN_LDES
                Definition.Cur_ADC_OUT_LDES = ADC_OUT_LDES
                Definition.Cur_ADC_IN_HDIM = ADC_IN_HDIM
                Definition.Cur_ADC_OUT_HDIM = ADC_OUT_HDIM

                val result =
                    inHaleFlowList.any { it.calibrationResults == "0" } || exHaleFlowList.any { it.calibrationResults == "0" }
                //定标结果写入数据库
                val flowResult = FlowCalibrationResultBean()
                flowResult.calibrationTime = DateUtils.nowTimeString
                flowResult.calibrationType = 0
                flowResult.inCoefficient = ADC_IN_LDES.toString()
                flowResult.outCoefficient = ADC_OUT_LDES.toString()
                flowResult.inHighCoefficient = ADC_IN_HDIM.toString()
                flowResult.outHighCoefficient = ADC_OUT_HDIM.toString()
                flowResult.calibrationResult = if (result) "0" else "1"
                viewModel.setFlowCaliResultBean(flowResult)
                val flowManualResult = FlowManualCalibrationResultBean()
                flowManualResult.calibrationTime = DateUtils.nowTimeString
                flowManualResult.inFluctuation = String.format("%.2f", m_AccuracyIn)
                flowManualResult.inError = String.format("%.2f", m_PrecisionIn)
                flowManualResult.outFluctuation = String.format("%.2f", m_AccuracyOut)
                flowManualResult.outError = String.format("%.2f", m_PrecisionOut)
                flowManualResult.calibrationResult = if (result) "0" else "1"
                viewModel.setFlowManualCaliResultBean(flowManualResult)
                if (result) {
                    LungCommonDialogFragment.startCommonDialogFragment(
                        requireActivity().supportFragmentManager,
                        "流量手动定标失败！定标参数保存到数据库！",
                        "2"
                    )
                } else {
                    LungCommonDialogFragment.startCommonDialogFragment(
                        requireActivity().supportFragmentManager,
                        "流量手动定标成功！定标参数保存到数据库！",
                        "1"
                    )
                }
                return
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentFlowHandleBinding.inflate(inflater, container, false)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initLineChart(lineChart: LineChart, type: Int) {
        lineChart.apply {
            dragDecelerationFrictionCoef = 0.9f
            isDragEnabled = false
            //开启缩放功能
            setScaleEnabled(false)
            clearAnimation()
            //绘制网格线的背景
            setDrawGridBackground(false)
            //是否开启右边Y轴
            axisRight?.isEnabled = false
            //设置图标的标题
            setNoDataText("")
            setTouchEnabled(false)
            isDragEnabled = false
            description.textSize = 9f
            description?.apply {
                text = ""
            }
            xAxis?.apply {
                textSize = 8.5f
                textColor = ContextCompat.getColor(requireContext(), R.color.text3)
                //X轴最大值和最小值
                axisMaximum = if (type == 1) 15f else 3.8f
                axisMinimum = 0f
                offsetLeftAndRight(10)
                //X轴每个值的差值(缩放时可以体现出来)
                granularity = 1.5f
                //X轴的位置
                position = XAxis.XAxisPosition.CENTER
                //是否绘制X轴的网格线(垂直于X轴)
                setDrawGridLines(false)
                //X轴的颜色
                axisLineColor = Color.parseColor("#FFD8E5FA")
                //X轴的宽度
                axisLineWidth = 2f
                //设置X轴显示固定条目,放大缩小都显示这个数
                setLabelCount(if (type == 1) 16 else 19, true)
                //是否绘制X轴
                setDrawAxisLine(false)
                //X轴每个刻度对应的值(展示的值和设置的值不同)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return String.format(if (type == 1) "%.0f" else "%.1f", value)
                    }
                }
            }
            axisLeft?.apply {
                textSize = 10f
                textColor = ContextCompat.getColor(requireContext(), R.color.text3)
                //左侧Y轴的最大值和最小值
                axisMaximum = if (type == 1) 4f else 15f
                axisMinimum = if (type == 1) -4f else -15f
                setLabelCount(if (type == 1) 9 else 11, true)
                //绘制网格线(样式虚线)
                enableGridDashedLine(0.2f, 2f, 0f)
                gridColor = ContextCompat.getColor(requireContext(), R.color.text3)
                setDrawGridLines(true)
                setDrawAxisLine(false) //绘制左边Y轴是否显示
                setDrawZeroLine(true) //是否开启0线
                isGranularityEnabled = true
            }

            legend?.apply {
                setDrawInside(false)
                formSize = 0f
            }
            val lineDataSets: MutableList<ILineDataSet> = ArrayList()
            if (type == 1) {
                val dataSetEntriesMap = mutableMapOf<String, MutableList<Entry>>()

                val entries1 = mutableListOf<Entry>()
                val entries2 = mutableListOf<Entry>()
                val entries3 = mutableListOf<Entry>()
                val entries4 = mutableListOf<Entry>()
                val entries5 = mutableListOf<Entry>()
                val entries6 = mutableListOf<Entry>()
                val entries7 = mutableListOf<Entry>()
                val entries8 = mutableListOf<Entry>()

                // 模拟十个数据点
                for (i in 0..10) {
                    // 在 x 轴上以递增的方式设置数据点的 x 值，y 值可以根据需要设置
                    val entry1 = Entry(i * 0.1.toFloat(), i * 0.3.toFloat())
                    entries1.add(entry1)

                    val entry2 = Entry(i * 0.3.toFloat(), i * 0.3.toFloat())
                    entries2.add(entry2)

                    val entry3 = Entry(i * 0.65.toFloat(), i * 0.3.toFloat())
                    entries3.add(entry3)

                    val entry4 = Entry(i * 0.9.toFloat(), i * 0.3.toFloat())
                    entries4.add(entry4)

                    val entry5 = Entry(i * 0.1.toFloat(), -i * 0.3.toFloat())
                    entries5.add(entry5)

                    val entry6 = Entry(i * 0.3.toFloat(), -i * 0.3.toFloat())
                    entries6.add(entry6)

                    val entry7 = Entry(i * 0.65.toFloat(), -i * 0.3.toFloat())
                    entries7.add(entry7)

                    val entry8 = Entry(i * 0.9.toFloat(), -i * 0.3.toFloat())
                    entries8.add(entry8)
                }

                dataSetEntriesMap["1"] = entries1
                dataSetEntriesMap["2"] = entries2
                dataSetEntriesMap["3"] = entries3
                dataSetEntriesMap["4"] = entries4
                dataSetEntriesMap["5"] = entries5
                dataSetEntriesMap["6"] = entries6
                dataSetEntriesMap["7"] = entries7
                dataSetEntriesMap["8"] = entries8

                dataSetEntriesMap.forEach { (key, value) ->
                    val line = LineDataSet(null, "")
                    line.entries = value
                    line.lineWidth = 1.0f
                    line.color =
                        ContextCompat.getColor(requireContext(), com.justsafe.libview.R.color.gray)
                    line.setDrawValues(false)
                    line.setDrawCircles(false)
                    line.setDrawCircleHole(false)
                    line.setDrawFilled(false)
                    line.enableDashedLine(1f, 2f, 1f)
                    line.mode = LineDataSet.Mode.LINEAR

                    lineDataSets.add(line)
                }

                inVolSec1DataSet = LineDataSet(null, "")
                inVolSec1DataSet!!.lineWidth = 1.0f
                inVolSec1DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.green)
                inVolSec1DataSet!!.setDrawValues(false)
                inVolSec1DataSet!!.setDrawCircles(false)
                inVolSec1DataSet!!.setDrawCircleHole(false)
                inVolSec1DataSet!!.setDrawFilled(false)
                inVolSec1DataSet!!.mode = LineDataSet.Mode.LINEAR

                outVolSec1DataSet = LineDataSet(null, "")
                outVolSec1DataSet!!.lineWidth = 1.0f
                outVolSec1DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.green)
                outVolSec1DataSet!!.setDrawValues(false)
                outVolSec1DataSet!!.setDrawCircles(false)
                outVolSec1DataSet!!.setDrawCircleHole(false)
                outVolSec1DataSet!!.setDrawFilled(false)
                outVolSec1DataSet!!.mode = LineDataSet.Mode.LINEAR

                inVolSec2DataSet = LineDataSet(null, "")
                inVolSec2DataSet!!.lineWidth = 1.0f
                inVolSec2DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.blue)
                inVolSec2DataSet!!.setDrawValues(false)
                inVolSec2DataSet!!.setDrawCircles(false)
                inVolSec2DataSet!!.setDrawCircleHole(false)
                inVolSec2DataSet!!.setDrawFilled(false)
                inVolSec2DataSet!!.mode = LineDataSet.Mode.LINEAR

                outVolSec2DataSet = LineDataSet(null, "")
                outVolSec2DataSet!!.lineWidth = 1.0f
                outVolSec2DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.blue)
                outVolSec2DataSet!!.setDrawValues(false)
                outVolSec2DataSet!!.setDrawCircles(false)
                outVolSec2DataSet!!.setDrawCircleHole(false)
                outVolSec2DataSet!!.setDrawFilled(false)
                outVolSec2DataSet!!.mode = LineDataSet.Mode.LINEAR

                inVolSec3DataSet = LineDataSet(null, "")
                inVolSec3DataSet!!.lineWidth = 1.0f
                inVolSec3DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.brown)
                inVolSec3DataSet!!.setDrawValues(false)
                inVolSec3DataSet!!.setDrawCircles(false)
                inVolSec3DataSet!!.setDrawCircleHole(false)
                inVolSec3DataSet!!.setDrawFilled(false)
                inVolSec3DataSet!!.mode = LineDataSet.Mode.LINEAR

                outVolSec3DataSet = LineDataSet(null, "")
                outVolSec3DataSet!!.lineWidth = 1.0f
                outVolSec3DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.brown)
                outVolSec3DataSet!!.setDrawValues(false)
                outVolSec3DataSet!!.setDrawCircles(false)
                outVolSec3DataSet!!.setDrawCircleHole(false)
                outVolSec3DataSet!!.setDrawFilled(false)
                outVolSec3DataSet!!.mode = LineDataSet.Mode.LINEAR

                inVolSec4DataSet = LineDataSet(null, "")
                inVolSec4DataSet!!.lineWidth = 1.0f
                inVolSec4DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.olive)
                inVolSec4DataSet!!.setDrawValues(false)
                inVolSec4DataSet!!.setDrawCircles(false)
                inVolSec4DataSet!!.setDrawCircleHole(false)
                inVolSec4DataSet!!.setDrawFilled(false)
                inVolSec4DataSet!!.mode = LineDataSet.Mode.LINEAR

                outVolSec4DataSet = LineDataSet(null, "")
                outVolSec4DataSet!!.lineWidth = 1.0f
                outVolSec4DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.olive)
                outVolSec4DataSet!!.setDrawValues(false)
                outVolSec4DataSet!!.setDrawCircles(false)
                outVolSec4DataSet!!.setDrawCircleHole(false)
                outVolSec4DataSet!!.setDrawFilled(false)
                outVolSec4DataSet!!.mode = LineDataSet.Mode.LINEAR

                inVolSec5DataSet = LineDataSet(null, "")
                inVolSec5DataSet!!.lineWidth = 1.0f
                inVolSec5DataSet!!.color =
                    ContextCompat.getColor(requireContext(), R.color.blueViolet)
                inVolSec5DataSet!!.setDrawValues(false)
                inVolSec5DataSet!!.setDrawCircles(false)
                inVolSec5DataSet!!.setDrawCircleHole(false)
                inVolSec5DataSet!!.setDrawFilled(false)
                inVolSec5DataSet!!.mode = LineDataSet.Mode.LINEAR

                outVolSec5DataSet = LineDataSet(null, "")
                outVolSec5DataSet!!.lineWidth = 1.0f
                outVolSec5DataSet!!.color =
                    ContextCompat.getColor(requireContext(), R.color.blueViolet)
                outVolSec5DataSet!!.setDrawValues(false)
                outVolSec5DataSet!!.setDrawCircles(false)
                outVolSec5DataSet!!.setDrawCircleHole(false)
                outVolSec5DataSet!!.setDrawFilled(false)
                outVolSec5DataSet!!.mode = LineDataSet.Mode.LINEAR

                lineDataSets.add(inVolSec1DataSet!!)
                lineDataSets.add(outVolSec1DataSet!!)
                lineDataSets.add(inVolSec2DataSet!!)
                lineDataSets.add(outVolSec2DataSet!!)
                lineDataSets.add(inVolSec3DataSet!!)
                lineDataSets.add(outVolSec3DataSet!!)
                lineDataSets.add(inVolSec4DataSet!!)
                lineDataSets.add(outVolSec4DataSet!!)
                lineDataSets.add(inVolSec5DataSet!!)
                lineDataSets.add(outVolSec5DataSet!!)
            } else {
                inFlowVol1DataSet = LineDataSet(null, "")
                inFlowVol1DataSet!!.lineWidth = 1.0f
                inFlowVol1DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.green)
                inFlowVol1DataSet!!.setDrawValues(false)
                inFlowVol1DataSet!!.setDrawCircles(false)
                inFlowVol1DataSet!!.setDrawCircleHole(false)
                inFlowVol1DataSet!!.setDrawFilled(false)
                inFlowVol1DataSet!!.mode = LineDataSet.Mode.LINEAR

                outFlowVol1DataSet = LineDataSet(null, "")
                outFlowVol1DataSet!!.lineWidth = 1.0f
                outFlowVol1DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.green)
                outFlowVol1DataSet!!.setDrawValues(false)
                outFlowVol1DataSet!!.setDrawCircles(false)
                outFlowVol1DataSet!!.setDrawCircleHole(false)
                outFlowVol1DataSet!!.setDrawFilled(false)
                outFlowVol1DataSet!!.mode = LineDataSet.Mode.LINEAR

                inFlowVol2DataSet = LineDataSet(null, "")
                inFlowVol2DataSet!!.lineWidth = 1.0f
                inFlowVol2DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.blue)
                inFlowVol2DataSet!!.setDrawValues(false)
                inFlowVol2DataSet!!.setDrawCircles(false)
                inFlowVol2DataSet!!.setDrawCircleHole(false)
                inFlowVol2DataSet!!.setDrawFilled(false)
                inFlowVol2DataSet!!.mode = LineDataSet.Mode.LINEAR

                outFlowVol2DataSet = LineDataSet(null, "")
                outFlowVol2DataSet!!.lineWidth = 1.0f
                outFlowVol2DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.blue)
                outFlowVol2DataSet!!.setDrawValues(false)
                outFlowVol2DataSet!!.setDrawCircles(false)
                outFlowVol2DataSet!!.setDrawCircleHole(false)
                outFlowVol2DataSet!!.setDrawFilled(false)
                outFlowVol2DataSet!!.mode = LineDataSet.Mode.LINEAR

                inFlowVol3DataSet = LineDataSet(null, "")
                inFlowVol3DataSet!!.lineWidth = 1.0f
                inFlowVol3DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.brown)
                inFlowVol3DataSet!!.setDrawValues(false)
                inFlowVol3DataSet!!.setDrawCircles(false)
                inFlowVol3DataSet!!.setDrawCircleHole(false)
                inFlowVol3DataSet!!.setDrawFilled(false)
                inFlowVol3DataSet!!.mode = LineDataSet.Mode.LINEAR

                outFlowVol3DataSet = LineDataSet(null, "")
                outFlowVol3DataSet!!.lineWidth = 1.0f
                outFlowVol3DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.brown)
                outFlowVol3DataSet!!.setDrawValues(false)
                outFlowVol3DataSet!!.setDrawCircles(false)
                outFlowVol3DataSet!!.setDrawCircleHole(false)
                outFlowVol3DataSet!!.setDrawFilled(false)
                outFlowVol3DataSet!!.mode = LineDataSet.Mode.LINEAR

                inFlowVol4DataSet = LineDataSet(null, "")
                inFlowVol4DataSet!!.lineWidth = 1.0f
                inFlowVol4DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.olive)
                inFlowVol4DataSet!!.setDrawValues(false)
                inFlowVol4DataSet!!.setDrawCircles(false)
                inFlowVol4DataSet!!.setDrawCircleHole(false)
                inFlowVol4DataSet!!.setDrawFilled(false)
                inFlowVol4DataSet!!.mode = LineDataSet.Mode.LINEAR

                outFlowVol4DataSet = LineDataSet(null, "")
                outFlowVol4DataSet!!.lineWidth = 1.0f
                outFlowVol4DataSet!!.color = ContextCompat.getColor(requireContext(), R.color.olive)
                outFlowVol4DataSet!!.setDrawValues(false)
                outFlowVol4DataSet!!.setDrawCircles(false)
                outFlowVol4DataSet!!.setDrawCircleHole(false)
                outFlowVol4DataSet!!.setDrawFilled(false)
                outFlowVol4DataSet!!.mode = LineDataSet.Mode.LINEAR

                inFlowVol5DataSet = LineDataSet(null, "")
                inFlowVol5DataSet!!.lineWidth = 1.0f
                inFlowVol5DataSet!!.color =
                    ContextCompat.getColor(requireContext(), R.color.blueViolet)
                inFlowVol5DataSet!!.setDrawValues(false)
                inFlowVol5DataSet!!.setDrawCircles(false)
                inFlowVol5DataSet!!.setDrawCircleHole(false)
                inFlowVol5DataSet!!.setDrawFilled(false)
                inFlowVol5DataSet!!.mode = LineDataSet.Mode.LINEAR

                outFlowVol5DataSet = LineDataSet(null, "")
                outFlowVol5DataSet!!.lineWidth = 1.0f
                outFlowVol5DataSet!!.color =
                    ContextCompat.getColor(requireContext(), R.color.blueViolet)
                outFlowVol5DataSet!!.setDrawValues(false)
                outFlowVol5DataSet!!.setDrawCircles(false)
                outFlowVol5DataSet!!.setDrawCircleHole(false)
                outFlowVol5DataSet!!.setDrawFilled(false)
                outFlowVol5DataSet!!.mode = LineDataSet.Mode.LINEAR

                lineDataSets.add(inFlowVol1DataSet!!)
                lineDataSets.add(outFlowVol1DataSet!!)
                lineDataSets.add(inFlowVol2DataSet!!)
                lineDataSets.add(outFlowVol2DataSet!!)
                lineDataSets.add(inFlowVol3DataSet!!)
                lineDataSets.add(outFlowVol3DataSet!!)
                lineDataSets.add(inFlowVol4DataSet!!)
                lineDataSets.add(outFlowVol4DataSet!!)
                lineDataSets.add(inFlowVol5DataSet!!)
                lineDataSets.add(outFlowVol5DataSet!!)
            }
            val lineData = LineData(lineDataSets)
            data = lineData
        }
    }

    private fun stopPortSend() {
        try {
            usbTransferUtil!!.write(ModbusProtocol.banTwoSensor)
            LiveDataBus.get().with(Constants.stopFlowCalibra).postValue(Constants.flowHandleCalibra)
            isHandleFlowStart = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendCalibraCommand() {
        try {
            usbTransferUtil?.write(ModbusProtocol.allowTwoSensor)
            LiveDataBus.get().with(Constants.startFlowHCalibra)
                .postValue(Constants.flowHandleCalibra)
            isHandleFlowStart = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 重置曲线
     */
    private fun resetParmet(type: Int) {
        resetElement()
        when (type) {
            0 -> {
                inVolSec1DataSet!!.clear()
                inFlowVol1DataSet!!.clear()
            }

            1 -> {
                outVolSec1DataSet!!.clear()
                outFlowVol1DataSet!!.clear()
            }

            2 -> {
                inVolSec2DataSet!!.clear()
                inFlowVol2DataSet!!.clear()
            }

            3 -> {
                outVolSec2DataSet!!.clear()
                outFlowVol2DataSet!!.clear()
            }

            4 -> {
                inVolSec3DataSet!!.clear()
                inFlowVol3DataSet!!.clear()
            }

            5 -> {
                outVolSec3DataSet!!.clear()
                outFlowVol3DataSet!!.clear()
            }

            6 -> {
                inVolSec4DataSet!!.clear()
                inFlowVol4DataSet!!.clear()
            }

            7 -> {
                outVolSec4DataSet!!.clear()
                outFlowVol4DataSet!!.clear()
            }

            8 -> {
                inVolSec5DataSet!!.clear()
                inFlowVol5DataSet!!.clear()
            }

            9 -> {
                outVolSec5DataSet!!.clear()
                outFlowVol5DataSet!!.clear()
            }
        }

        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
        binding.chartFlowHandleCapacityTime.invalidate()

        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
        binding.chartFlowHandleFlowCapacity.invalidate()
    }

    /**
     * 重置参数
     */
    private fun resetElement() {
        ftempHigh = 0f
        tempv = 0
        tempcalc = 0f
        tempcalcHigh = 0f
        ftemp = 0f
        iscer = false
        startsec = 0f
        tempq = 0
    }

    /**
     * 准备流量手动定标初始操作
     */
    private fun prepareManualFlowCalibration() {
        ManualFlowState = false
        Autoindex = 0
        k = 0
        ftemplow = 0
        ftemphigh = 0
        TempDl.clear()
        TempDh.clear()
        dl = 0.0
        dh = 0.0
        ftemp = 0f
        tempv = 0
        tempq = 0
        tempcalc = 0f
        tempcalcHigh = 0f
        tempvol = 0f
        ftempHigh = 0f
        startsec = 0f
        ADC_OUT_LDES_temp = mutableListOf()
        ADC_IN_LDES_temp = mutableListOf()
        ADC_OUT_HDIM_temp = mutableListOf()
        ADC_IN_HDIM_temp = mutableListOf()
        m_AccuracyIn = 0f
        m_AccuracyOut = 0f
        m_PrecisionIn = 0f
        m_PrecisionOut = 0f

        inFlowVol1DataSet!!.clear()
        inFlowVol2DataSet!!.clear()
        inFlowVol3DataSet!!.clear()
        inFlowVol4DataSet!!.clear()
        inFlowVol5DataSet!!.clear()
        outFlowVol1DataSet!!.clear()
        outFlowVol2DataSet!!.clear()
        outFlowVol3DataSet!!.clear()
        outFlowVol4DataSet!!.clear()
        outFlowVol5DataSet!!.clear()

        inVolSec1DataSet!!.clear()
        inVolSec2DataSet!!.clear()
        inVolSec3DataSet!!.clear()
        inVolSec4DataSet!!.clear()
        inVolSec5DataSet!!.clear()
        outVolSec1DataSet!!.clear()
        outVolSec2DataSet!!.clear()
        outVolSec3DataSet!!.clear()
        outVolSec4DataSet!!.clear()
        outVolSec5DataSet!!.clear()

        binding.chartFlowHandleCapacityTime.lineData.notifyDataChanged()
        binding.chartFlowHandleCapacityTime.notifyDataSetChanged()
        binding.chartFlowHandleCapacityTime.invalidate()

        binding.chartFlowHandleFlowCapacity.lineData.notifyDataChanged()
        binding.chartFlowHandleFlowCapacity.notifyDataSetChanged()
        binding.chartFlowHandleFlowCapacity.invalidate()

        inHaleFlowList.clear()
        exHaleFlowList.clear()

        inHaleFlowAdapter.notifyDataSetChanged()
        exHaleFlowAdapter.notifyDataSetChanged()
    }

    /**
     * 定标结果
     */
    private fun checkCerbra() {
        val vol = dicvol[k]
        var curvol1 = 0f
        if (vol?.isNotEmpty() == true) {
            if (k % 2 == 0) {
                curvol1 = vol[vol.size - 1]
                ManualFlowState = true
                if (abs(curvol1 - 3) > m_AccuracyIn)
                    m_AccuracyIn = abs(curvol1 - 3)

            } else if (k == 1 || k == 3 || k == 5 || k == 7) {
                curvol1 = -vol[vol.size - 1]
                ManualFlowState = false
                if (abs(curvol1 - 3) > m_AccuracyOut)
                    m_AccuracyOut = abs(curvol1 - 3)
            } else {
                val volin = arrayListOf(
                    dicvol[0]?.get(dicvol[0]!!.size - 1) ?: 0f,
                    dicvol[2]?.get(dicvol[2]!!.size - 1) ?: 0f,
                    dicvol[4]?.get(dicvol[4]!!.size - 1) ?: 0f,
                    dicvol[6]?.get(dicvol[6]!!.size - 1) ?: 0f,
                    dicvol[8]?.get(dicvol[8]!!.size - 1) ?: 0f
                )
                val volout = arrayListOf<Float>(
                    dicvol[1]?.get(dicvol[1]!!.size - 1) ?: 0f,
                    dicvol[3]?.get(dicvol[3]!!.size - 1) ?: 0f,
                    dicvol[5]?.get(dicvol[5]!!.size - 1) ?: 0f,
                    dicvol[7]?.get(dicvol[7]!!.size - 1) ?: 0f,
                    dicvol[9]?.get(dicvol[9]!!.size - 1) ?: 0f
                )
                curvol1 = -vol[vol.size - 1]

                if (abs(curvol1 - 3) > m_AccuracyOut)
                    m_AccuracyOut = abs(curvol1 - 3)
                m_PrecisionIn = abs(volin.minOrNull()!! - volin.maxOrNull()!!)
                m_PrecisionOut = abs(volout.minOrNull()!! - volout.maxOrNull()!!)
            }
            val patientBean = SharedPreferencesUtils.instance.patientBean
            if (k == 0 || k == 2 || k == 4 || k == 6 || k == 8) {
                val error = String.format("%.2f", (3 - curvol1) / 3 * 100)
                val result = if (abs(error.toFloat()) < 3) "1" else "0"
                inHaleFlowList.add(
                    FlowBean(
                        patientBean!!.patientId,
                        DateUtils.nowTimeString,
                        patientBean.patientId,
                        strVol[k],
                        "3",
                        String.format("%.3f", curvol1),
                        error,
                        result
                    )
                )
                inHaleFlowAdapter.setItemsBean(
                    inHaleFlowList
                )
            }

            if (k == 1 || k == 3 || k == 5 || k == 7 || k == 9) {
                val error = String.format("%.2f", (3 - curvol1) / 3 * 100)
                val result = if (abs(error.toFloat()) < 3) "1" else "0"
                exHaleFlowList.add(
                    FlowBean(
                        patientBean!!.patientId,
                        DateUtils.nowTimeString,
                        patientBean.patientId,
                        strVol[k],
                        "3",
                        String.format("%.3f", curvol1),
                        error,
                        result
                    )
                )
                exHaleFlowAdapter.setItemsBean(
                    exHaleFlowList
                )
            }
        }
    }

    private fun setPullStyle() {
        binding.tvPullDirection.text = "拉"
        binding.tvPullDirection.setBackgroundResource(R.drawable.flow_pull)
    }

    private fun setPushStyle() {
        binding.tvPullDirection.text = "推"
        binding.tvPullDirection.setBackgroundResource(R.drawable.flow_down)
    }
}