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
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.common.base.BaseUtil
import com.just.machine.model.EnviorDataModel
import com.just.machine.ui.dialog.LungCommonDialogFragment
import com.just.machine.util.LiveDataBus
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.FragmentEnvironmentalBinding
import com.justsafe.libview.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

/**
 *create by 2024/6/19
 * 环境定标
 *@author zt
 */
@AndroidEntryPoint
class EnvironmentalFragment : CommonBaseFragment<FragmentEnvironmentalBinding>() {

    private val viewModel by viewModels<MainViewModel>()

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
            if (start == getString(R.string.begin)) {
//                if (Constants.isDebug) {
//                    val temperature: Short = 250 // 温度，单位为摄氏度
//                    val humidity: Short = 60 // 湿度，单位为百分比
//                    val pressure = 101325 // 气压，单位为帕斯卡
//
//                    val environmentData = MudbusProtocol.generateSerialCommand(
//                        temperature,
//                        humidity,
//                        pressure
//                    )
//
//                    LiveDataBus.get().with(Constants.serialCallback).value = environmentData
//                }

//                val startTime = System.currentTimeMillis()
//                scope = lifecycleScope.launch(Dispatchers.IO) {
//                    var nextPrintTime = startTime
//                    var i = 0
//                    while (isActive) {
//                        if (System.currentTimeMillis() >= nextPrintTime) {
//                            println("job: I'm sleeping ${i++} ...")
//                            nextPrintTime += 1000L
//                        }
//                    }
//                }
//                binding.tvCalibrationStart.text = getString(R.string.cancel)
                isBegin = true
                USBTransferUtil.getInstance().write(MudbusProtocol.allowOneSensor)//发送环境定标
            }
        }

        binding.llSave.setNoRepeatListener {
            if (binding.etTemperature.text.toString().trim() == "") {
                Toast.makeText(requireContext(), "温度不能为空!", Toast.LENGTH_SHORT).show()
                return@setNoRepeatListener
            }
            if (binding.etHumidity.text.toString().trim() == "") {
                Toast.makeText(requireContext(), "湿度不能为空!", Toast.LENGTH_SHORT).show()
                return@setNoRepeatListener
            }
            if (binding.etPressure.text.toString().trim() == "") {
                Toast.makeText(requireContext(), "气压不能为空!", Toast.LENGTH_SHORT).show()
                return@setNoRepeatListener
            }
            val time = DateUtils.nowTimeString
            //插入一条手动环境定标
            viewModel.setEnvironmental(
                EnvironmentalCalibrationBean(//假设用户id为1
                    0,
                    1,
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

        LiveDataBus.get().with(Constants.serialCallback).observe(this) {//解析串口消息
            if (isBegin) {
                if (it is ByteArray) {
//                if(BaseUtil.isDoubleClick()) return@observe//有个粘性事件先不处理
                    LogUtils.e(
                        tag + BaseUtil.bytes2HexStr(it) + "字节长度" + BaseUtil.bytes2HexStr(
                            it
                        ).length
                    )
                    val environmentData = MudbusProtocol.parseEnvironmentData(it)//环境定标

                    if (environmentData != null) {
                        val (temperature, humidity, pressure) = environmentData
                        val time = DateUtils.nowTimeString

                        if (pressure > 1000 || pressure < 500 || temperature > 50 || temperature <= 0 || humidity > 100 || humidity <= 0)
                        {
                            //定标失败
                            LungCommonDialogFragment.startCommonDialogFragment(
                                requireActivity().supportFragmentManager, "定标失败!", "2"
                            )
                        }else{
                            viewModel.setEnvironmental(
                                EnvironmentalCalibrationBean(//假设用户id为1
                                    0, 1, time, temperature.toString(),
                                    humidity.toString(), pressure.toString(), "1"
                                )
                            )//插入数据库并查询
                        }
                    } else {
                        LogUtils.e(tag + "接收到的环境定标数据无效！")
                    }
                }
                isBegin = false
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
     * 获取自动定标数据
     */
    private fun exitLowPower() {
        try {
            SerialPortManager.sendMessage(MudbusProtocol.EXIT_LOW_POWER_COMMAND)
        }catch (e:Exception){
            e.printStackTrace()
        }
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
        LiveDataBus.get().with(Constants.serialCallback).removeObservers(viewLifecycleOwner)
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEnvironmentalBinding.inflate(inflater, container, false)
}