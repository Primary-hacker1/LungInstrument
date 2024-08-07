package com.just.machine.ui.fragment.calibration

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.calibration.EnvironmentalCalibrationBean
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.calibration.EnvironmentalAdapter
import com.just.machine.ui.fragment.serial.ModbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.common.base.BaseUtil
import com.common.base.toast
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.dialog.LungCommonDialogFragment
import com.just.machine.util.LiveDataBus
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.FragmentEnvironmentalBinding
import com.justsafe.libview.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/6/19
 * 环境定标
 *@author zt
 */
@AndroidEntryPoint
class EnvironmentalFragment : CommonBaseFragment<FragmentEnvironmentalBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val usbTransferUtil: USBTransferUtil by lazy {
        USBTransferUtil.getInstance()
    }

    private val adapter by lazy { EnvironmentalAdapter(requireContext()) }

    private var isLocked = true

    private var isBegin = false

    override fun initView() {

        binding.rvEnvironmental.layoutManager = LinearLayoutManager(requireContext())
        adapter.setItemClickListener { _, position ->
            adapter.toggleItemBackground(position)
        }
        binding.rvEnvironmental.adapter = adapter
    }

    private var environmentalBeans: MutableList<EnvironmentalCalibrationBean> = ArrayList()

    private fun beanQuery(any: Any) {
        if (any is List<*>) {

            environmentalBeans.clear()

            val datas = any as MutableList<*>

            var fistBean: EnvironmentalCalibrationBean? = null

            for (num in 0 until datas.size) {
                val bean = datas[num] as EnvironmentalCalibrationBean
                environmentalBeans.add(bean)
            }

            if (environmentalBeans.size > 0) {
                fistBean = datas[0] as EnvironmentalCalibrationBean
            }

            binding.atvCreateTime.text = fistBean?.createTime
            binding.etTemperature.setText(fistBean?.temperature)
            binding.etHumidity.setText(fistBean?.humidity)
            binding.etPressure.setText(fistBean?.pressure)

            adapter.setItemsBean(environmentalBeans)

            LogUtils.d(tag + environmentalBeans.toString())

            binding.rvEnvironmental.adapter = adapter
        }
    }


    override fun initListener() {
        binding.llStart.setNoRepeatListener {
            val start = binding.tvCalibrationStart.text
            if (ModbusProtocol.isDeviceConnect) {
                if (start == getString(R.string.begin)) {
                    isBegin = true
                    usbTransferUtil.write(ModbusProtocol.allowOneSensor)//发送环境定标
                }
            }else{
                toast(getString(R.string.device_without_connection_tips))
            }
        }

        binding.llSave.setNoRepeatListener {
            if (binding.etTemperature.text.toString().trim() == "") {
                toast("温度不能为空!")
                return@setNoRepeatListener
            }
            if (binding.etHumidity.text.toString().trim() == "") {
                toast("湿度不能为空!")
                return@setNoRepeatListener
            }
            if (binding.etPressure.text.toString().trim() == "") {
                toast("气压不能为空!")
                return@setNoRepeatListener
            }
            val patientBean = SharedPreferencesUtils.instance.patientBean
            val time = DateUtils.nowTimeString
            //插入一条手动环境定标
            viewModel.setEnvironmental(
                EnvironmentalCalibrationBean(//假设用户id为1
                    0,
                    patientBean!!.patientId,
                    time,
                    binding.etTemperature.text.toString().trim(),
                    binding.etHumidity.text.toString().trim(),
                    binding.etPressure.text.toString().trim(),
                    "0"
                )
            )
            lockButtonStyle()
            LungCommonDialogFragment.startCommonDialogFragment(
                requireActivity().supportFragmentManager, "保存成功!", "1"
            )
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

                        if (pressure > 1000 || pressure < 500 || temperature > 50 || temperature <= 0 || humidity > 100 || humidity <= 0) {
                            //定标失败
                            LungCommonDialogFragment.startCommonDialogFragment(
                                requireActivity().supportFragmentManager, "定标失败!", "2"
                            )
                        } else {
                            val patientBean = SharedPreferencesUtils.instance.patientBean
                            viewModel.setEnvironmental(
                                EnvironmentalCalibrationBean(//假设用户id为1
                                    0, patientBean!!.patientId, time, temperature.toString(),
                                    humidity.toString(), pressure.toString(), "1"
                                )
                            )//插入数据库并查询
                        }
                    }
                    isBegin = false
                }
            } catch (e: Exception) {
                LogUtils.d(e.toString())
            }
        }

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.EnvironmentalsSuccess -> {
                    it.any?.let { it1 -> beanQuery(it1) }
                }
            }
        }
        binding.ivToggleLock.setNoRepeatListener {
            isLocked = if (isLocked) {
                unlockButtonStyle()
                false
            } else {
                lockButtonStyle()
                true
            }
        }
    }

    private fun unlockButtonStyle() {
        binding.ivToggleLock.setImageResource(R.drawable.unlock)
        binding.llSave.setBackgroundResource(R.drawable.save_bg_line)
        binding.etTemperature.isEnabled = true
        binding.etTemperature.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_gray_solid)
        binding.etHumidity.isEnabled = true
        binding.etHumidity.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_gray_solid)
        binding.etPressure.isEnabled = true
        binding.etPressure.setBackgroundResource(R.drawable.frame_with_color_d6d6d6_gray_solid)
        binding.llSave.isEnabled = true
        binding.ivCalibrationSave.setImageResource(R.drawable.save_icon)
        binding.tvCalibrationSave.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
    }

    private fun lockButtonStyle() {
        binding.ivToggleLock.setImageResource(R.drawable.lock)
        binding.llSave.setBackgroundResource(R.drawable.save_bg_gray)
        binding.etTemperature.isEnabled = false
        binding.etTemperature.setBackgroundResource(R.drawable.frame_with_color_transparent)
        binding.etHumidity.isEnabled = false
        binding.etHumidity.setBackgroundResource(R.drawable.frame_with_color_transparent)
        binding.etPressure.isEnabled = false
        binding.etPressure.setBackgroundResource(R.drawable.frame_with_color_transparent)
        binding.llSave.isEnabled = false
        binding.ivCalibrationSave.setImageResource(R.drawable.save_icon_white)
        binding.tvCalibrationSave.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorWhite
            )
        )
    }

    /**
     * 懒加载
     */
    override fun loadData() {
        viewModel.getEnvironmental()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 对于 Fragment，推荐在 onDestroyView 中移除观察者
        LiveDataBus.get().with(Constants.envCaliSerialCallback).removeObservers(viewLifecycleOwner)
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEnvironmentalBinding.inflate(inflater, container, false)
}