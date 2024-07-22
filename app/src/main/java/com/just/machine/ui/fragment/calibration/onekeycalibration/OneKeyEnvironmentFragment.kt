package com.just.machine.ui.fragment.calibration.onekeycalibration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.just.machine.dao.calibration.EnvironmentalCalibrationBean
import com.just.machine.model.Constants
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.dialog.LungCommonDialogFragment
import com.just.machine.ui.fragment.serial.ModbusProtocol
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.FragmentOnekeyEnvironmentBinding
import com.justsafe.libview.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 * 一键定标-环境
 */
@AndroidEntryPoint
class OneKeyEnvironmentFragment : CommonBaseFragment<FragmentOnekeyEnvironmentBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val usbTransferUtil: USBTransferUtil by lazy {
        USBTransferUtil.getInstance()
    }

    private var isBegin = true

    override fun loadData() {

    }

    override fun initView() {

    }

    override fun initListener() {
        LiveDataBus.get().with("oneKeyCalibra").observe(this) {
            if (it is String) {
                if (it == "environment") {
                    isBegin = true
                    usbTransferUtil.write(ModbusProtocol.allowOneSensor)//发送环境定标
                }
            }
        }

        LiveDataBus.get().with(Constants.envCaliSerialCallback).observe(this) {//解析串口消息
            try {
                if (isBegin) {
                    if (it is ModbusProtocol.EnvironmentData) {

                        LogUtils.e(
                            "接收环境定标数据 ----temperature----${it.temperature}----humidity----${it.humidity}----atmosphericPressure----${it.pressure}"
                        )

                        val (temperature, humidity, pressure) = it

                        val time = DateUtils.nowTimeString

                        binding.tvOnekeyTemperature.text = temperature.toString()
                        binding.tvOnekeyHumidity.text = humidity.toString()
                        binding.tvOnekeyPressure.text = pressure.toString()

                        if (pressure > 1000 || pressure < 500 || temperature > 50 || temperature <= 0 || humidity > 100 || humidity <= 0) {
                            //定标失败
                            binding.tvOnekeyCalibrationResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                            binding.tvOnekeyCalibrationResult.text = "定标未通过"
                            LiveDataBus.get().with("oneKeyCalibra").value = "environmentFailed"
                        } else {
                            binding.tvOnekeyCalibrationResult.text = "定标通过"
                            binding.tvOnekeyCalibrationResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                            val patientBean = SharedPreferencesUtils.instance.patientBean
                            viewModel.setEnvironmental(
                                EnvironmentalCalibrationBean(//假设用户id为1
                                    0, patientBean!!.patientId, time, temperature.toString(),
                                    humidity.toString(), pressure.toString(), "1"
                                )
                            )//插入数据库并查询
                            LiveDataBus.get().with("oneKeyCalibra").value = "environmentSuccess"
                        }
                    }
                    isBegin = false
                    usbTransferUtil.write(ModbusProtocol.banOneSensor)//停止环境定标
                }
            } catch (e: Exception) {
                LogUtils.d(e.toString())
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnekeyEnvironmentBinding.inflate(inflater, container, false)
}